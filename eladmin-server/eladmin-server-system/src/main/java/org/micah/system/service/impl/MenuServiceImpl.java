package org.micah.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.CacheKey;
import org.micah.core.util.FileUtils;
import org.micah.core.util.StringUtils;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.exception.global.EntityExistException;
import org.micah.exception.global.UpdateFailException;
import org.micah.model.Menu;
import org.micah.model.Role;
import org.micah.model.SysUser;
import org.micah.model.dto.MenuDto;
import org.micah.model.mapstruct.MenuMapStruct;
import org.micah.model.query.MenuQueryCriteria;
import org.micah.model.vo.MenuMetaVo;
import org.micah.model.vo.MenuVo;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.redis.util.RedisUtils;
import org.micah.system.mapper.MenuMapper;
import org.micah.system.mapper.SysUserMapper;
import org.micah.system.service.IMenuService;
import org.micah.system.service.IRoleService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description: 菜单业务实现类
 * @author: Micah
 * @create: 2020-08-12 18:24
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "menu")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final MenuMapper menuMapper;

    private final MenuMapStruct menuMapStruct;

    private final RedisUtils redisUtils;

    private final IRoleService roleService;

    private final SysUserMapper userMapper;

    private static final String FIRST_LEVEL_PATH_PRE = "/";



    private static final String HTTP_PRE = "http://";

    private static final String HTTPS_PRE = "https://";

    private static final String DEFAULT_COMPONENT = "Layout";

    public MenuServiceImpl(MenuMapper menuMapper, MenuMapStruct menuMapStruct, RedisUtils redisUtils, IRoleService roleService, SysUserMapper userMapper) {
        this.menuMapper = menuMapper;
        this.menuMapStruct = menuMapStruct;
        this.redisUtils = redisUtils;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }

    /**
     * 根据角色的id来查询菜单信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<MenuDto> queryByRoleIds(Set<Long> ids) {
        List<Menu> menu = this.menuMapper.queryByRoleIds(ids);
        // 去重
        List<Menu> collect = menu.stream().distinct().collect(Collectors.toList());
        return this.menuMapStruct.toDto(collect);
    }

    /**
     * 查询全部数据
     *
     * @param queryCriteria 条件
     * @param isQuery       /
     * @return /
     */
    @Override
    public List<MenuDto> queryAll(MenuQueryCriteria queryCriteria, Boolean isQuery) {
        List<Menu> menuList = null;
        if (!isQuery) {
            menuList = this.menuMapper.queryAll(null);
        } else {
            menuList = this.menuMapper.queryAll(QueryHelpUtils.getWrapper(queryCriteria, Menu.class));
        }
        return this.menuMapStruct.toDto(menuList);
    }

    /**
     * 查询全部数据
     *
     * @param queryCriteria 条件
     * @param pageable      /
     * @return /
     */
    @Override
    public PageResult queryAllByPage(MenuQueryCriteria queryCriteria, Pageable pageable) {
        Page<Menu> page = PageUtils.startPageAndSort(pageable);
        if (BeanUtil.isEmpty(queryCriteria)){
            queryCriteria.setPid(0L);
        }
        QueryWrapper<Menu> wrapper = QueryHelpUtils.getWrapper(queryCriteria, Menu.class);
        Page<Menu> menuPage = this.menuMapper.selectPage(page, wrapper);
        return PageResult.success(menuPage.getTotal(), menuPage.getPages(), this.menuMapStruct.toDto(menuPage.getRecords()));
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    @Override
    @Cacheable(key = "'id' + #p0")
    public MenuDto findById(Long id) {
        Menu menu = this.menuMapper.queryById(id);
        return this.menuMapStruct.toDto(menu);
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Menu resources) {
        // 1.验证title是否重复
        Optional.ofNullable(this.menuMapper.selectOne(Wrappers.<Menu>lambdaQuery().eq(Menu::getTitle, resources.getTitle()))).ifPresent(
                menu1 -> {
                    log.error("存在相同的title:{}", menu1.getTitle());
                    throw new EntityExistException(Menu.class, "title", menu1.getTitle());
                });
        // 2. 验证componentName是否重复
        Optional.ofNullable(this.menuMapper.selectOne(Wrappers.<Menu>lambdaQuery().eq(Menu::getComponentName, resources.getComponentName()))).ifPresent(
                menu1 -> {
                    log.error("存在相同的title:{}", menu1.getComponentName());
                    throw new EntityExistException(Menu.class, "title", menu1.getComponentName());
                });
        // 3.验证外链是否正确
        this.verifyIframe(resources);
        // 4.设置下级菜单的数量
        resources.setSubCount(0);
        // 5.存储
        if (this.save(resources)) {
            // 更新上级菜单的subCount的信息
            this.updateSuperiorSubCount(resources.getPid());
            // 删除上级菜单的缓存信息
            this.redisUtils.del(CacheKey.MENU_PID_KEY_PRE + resources.getPid());
        }
    }


    /**
     * 编辑
     *
     * @param resources /
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Menu resources) {
        // 验证上级菜单的id是否有误
        if (resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        // 验证外链是否有误
        this.verifyIframe(resources);
        // 验证title是否与其他数据重复
        Optional.ofNullable(this.menuMapper.selectOne(Wrappers.<Menu>lambdaQuery().eq(Menu::getTitle, resources.getTitle()))).ifPresent(
                menu1 -> {
                    if (!resources.getId().equals(menu1.getId())) {
                        log.error("存在相同的title:{}", menu1.getTitle());
                        throw new EntityExistException(Menu.class, "title", menu1.getTitle());
                    }
                });
        // 查询旧的数据
        Menu oldMenu = this.menuMapper.selectById(resources.getId());
        // 获取旧的pid
        Long oldMenuPid = oldMenu.getPid();
        // 执行更新操作
        if (this.updateById(resources)) {
            // 更新成功
            log.info("更新成功，开始更新上级菜单的信息和缓存信息");
            if (!resources.getPid().equals(oldMenuPid)) {
                // 上级菜单发生变化
                // 1.更新新的和旧的上级菜单的信息
                this.updateSuperiorSubCount(resources.getPid());
                this.updateSuperiorSubCount(oldMenuPid);
            }
            // 删除所有的缓存
            this.delCaches(resources.getId(), oldMenuPid, resources.getPid());
        } else {
            log.error("更新失败:{}", resources);
            throw new UpdateFailException("更新失败，请联系管理员");
        }

    }


    /**
     * 构建为树形结构
     *
     * @param menuDtos 原始数据
     * @return /
     */
    @Override
    public List<MenuDto> buildTree(List<MenuDto> menuDtos) {
        if (CollUtil.isNotEmpty(menuDtos)) {
            List<MenuDto> tree = new LinkedList<>();
            Set<Long> childrenIds = new HashSet<>();
            // 遍历所有的菜单
            for (MenuDto menuDto : menuDtos) {
                // 将该菜单加入到树形列表中
                if (menuDto.getPid().equals(0L)) {
                    // 加入到树形结构列表中
                    tree.add(menuDto);
                }
                // 再次遍历,获取子节点
                for (MenuDto dto : menuDtos) {
                    // 比较是否为外层节点的子节点
                    if (!dto.getId().equals(0L) && dto.getPid().equals(menuDto.getId())) {
                        // 为子节点则加入到children属性中
                        if (menuDto.getChildren() == null) {
                            menuDto.setChildren(new ArrayList<>());
                        }
                        menuDto.getChildren().add(dto);
                        // 将当前节点加入到childrenIds中做一个标记
                        childrenIds.add(menuDto.getId());
                    }
                }
            }
            // 极端情况，没有顶级节点，即没有一级菜单，则再次遍历，将菜单中的子节点过滤掉
            if (CollUtil.isEmpty(tree)) {
                // 过滤掉子节点
                tree = menuDtos.stream().filter(menuDto -> !childrenIds.contains(menuDto.getId())).collect(Collectors.toList());
            }
            return tree;
        }
        return null;
    }

    /**
     * 构建菜单
     *
     * @param menuDtos /
     * @return /
     */
    @Override
    public List<MenuVo> buildMenus(List<MenuDto> menuDtos, List<MenuVo> menuVos) {
        menuDtos.forEach(menuDto -> {
            if (menuDto != null) {
                // 获取子节点
                List<MenuDto> children = menuDto.getChildren();
                // 定义一个MenuVo对象
                MenuVo menuVo = new MenuVo();
                menuVo.setName(StringUtils.isNotBlank(menuDto.getComponentName()) ? menuDto.getComponentName() : menuDto.getTitle());
                menuVo.setPath(menuDto.getPid().equals(0L) ? FIRST_LEVEL_PATH_PRE + menuDto.getPath() : menuDto.getPath());
                menuVo.setHidden(menuDto.getHidden());
                // 设置前端组件的路径
                if (!menuDto.getIFrame()) {
                    // 如果不是外部链接
                    if (menuDto.getPid().equals(0L)) {
                        // 如果是顶级菜单
                        menuVo.setComponent(StringUtils.isNotBlank(menuDto.getComponent()) ? menuDto.getComponent() : DEFAULT_COMPONENT);
                    } else if (StringUtils.isNotBlank(menuDto.getComponent())){
                        // 不是顶级菜单
                        menuVo.setComponent(menuDto.getComponent());
                    }
                }
                // 设置元信息
                // TODO: 2020/8/14 是否缓存需要看前端进行调整
                menuVo.setMeta(new MenuMetaVo(menuDto.getTitle(), menuDto.getIcon(), menuDto.getCache()));
                if (CollUtil.isNotEmpty(children)) {
                    // 如果当前的菜单存在下级菜单
                    // 此时父级菜单必须永远显示
                    menuVo.setAlwaysShow(true);
                    menuVo.setRedirect("noredirect");
                    // TODO: 2020/8/15 第二个参数相当于每个MenuVo对象中的children对象，不能用该方法的menuVos参数，否则会出现无限套娃的现象
                    menuVo.setChildren(this.buildMenus(children, new ArrayList<>()));
                } else if (menuDto.getId().equals(0L)) {
                    // 当前节点是一级节点，并且该节点没有子节点
                    // TODO: 2020/8/14 这一块原作者写的，我也不太明白,大概的意思是构建一个子节点，子节点的信息和之前的menuVo差不多
                    // TODO: 2020/8/14 然后将前面的menuVo的属性进行改变，最后把自己设置的子节点作为menuVo的子节点属性
                    MenuVo menuVo1 = new MenuVo();
                    BeanUtil.copyProperties(menuVo, menuVo1);
                    if (!menuDto.getIFrame()) {
                        // 不是外链
                        menuVo1.setPath("index");
                    } else {
                        menuVo1.setPath(menuDto.getPath());
                    }
                    menuVo.setName(null);
                    menuVo.setMeta(null);
                    menuVo.setComponent(DEFAULT_COMPONENT);
                    menuVo.setChildren(Lists.newArrayList(menuVo1));
                }
                menuVos.add(menuVo);
            }

        });
        return menuVos;
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    @Override
    public Menu findOne(Long id) {
        return Optional.ofNullable(this.getById(id)).orElse(null);
    }

    /**
     * 删除
     *
     * @param menuIds /
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> menuIds) {
        Set<Long> deleteIds = new HashSet<>();
        if (CollUtil.isNotEmpty(menuIds)) {
            menuIds.forEach(menuId -> {
                deleteIds.add(menuId);
                deleteIds.addAll(this.getDeleteMenus(this.getMenusByPid(menuId), deleteIds));
            });
        }
        deleteIds.forEach(delId -> {
            // 1.解除与角色的绑定关系
            this.roleService.untiedMenu(delId);
            // 2.更新上级节点的信息
            Long pid = this.getById(delId).getPid();
            this.updateSuperiorSubCount(pid);
            // 3.删除缓存
            this.delCaches(delId, pid, null);
            // 4.删除数据库中的数据
            this.removeById(delId);
        });
    }

    /**
     * 导出
     *
     * @param data 待导出的数据
     * @param response /
     * @throws IOException /
     */
    @Override
    public void download(List<MenuDto> data, HttpServletResponse response) throws IOException {
        // TODO: 2020/8/15 后续处理导出数据
        FileUtils.downloadFailedUsingJson(response,"menu-info",MenuDto.class,data,"sheet1");
    }

    /**
     * 根据pid查找菜单
     *
     * @param pid /
     * @return /
     */
    @Override
    @Cacheable(key = "'pid:' + #p0")
    public List<MenuDto> getMenusByPid(Long pid) {
        List<Menu> menuList = this.menuMapper.selectList(Wrappers.<Menu>lambdaQuery().eq(pid != null, Menu::getPid, pid));
        return Optional.ofNullable(this.menuMapStruct.toDto(menuList)).orElseGet(ArrayList::new);
    }

    /**
     * 根据ID获取该节点数据与上级数据,修改数据回显的时候调用
     *
     * @param menuDto  /
     * @param menuDtos /
     * @return /
     */
    @Override
    public List<MenuDto> getSuperior(MenuDto menuDto, List<MenuDto> menuDtos) {
        // 将该节点加入到列表中
        menuDtos.add(menuDto);
        if (menuDto.getPid().equals(0L)) {
            // 查找一级菜单
            menuDtos.addAll(this.queryFirstLevelMenu());
            return menuDtos;
        }
        return this.getSuperior(this.menuMapStruct.toDto(this.getById(menuDto.getPid())), menuDtos);
    }

    /**
     * 获取所有的一级菜单
     *
     * @return
     */
    private List<MenuDto> queryFirstLevelMenu() {
        List<Menu> menuList = this.menuMapper.selectList(Wrappers.<Menu>lambdaQuery().eq(Menu::getPid, 0));
        return Optional.ofNullable(
                this.menuMapStruct.toDto(menuList)).orElseGet(ArrayList::new);
    }

    /**
     * 根据当前用户获取菜单
     *
     * @param currentUserId /
     * @return /
     */
    @Override
    @Cacheable(key = "'user:' + #p0")
    public List<MenuDto> findByUser(Long currentUserId) {
        List<Menu> menus = this.menuMapper.queryByUserId(currentUserId);
        return Optional.ofNullable(this.menuMapStruct.toDto(menus)).orElseGet(ArrayList::new);
    }

    /**
     * 获取待删除的菜单
     *
     * @param menuList 需要删除的菜单集合  /
     * @param deleteIds /
     * @return /
     */
    public Set<Long> getDeleteMenus(List<MenuDto> menuList, Set<Long> deleteIds) {
        for (MenuDto menuDto : menuList) {
            deleteIds.add(menuDto.getId());
            // 查找下一个子节点
            List<MenuDto> menusByPid = this.getMenusByPid(menuDto.getId());
            if (CollUtil.isNotEmpty(menusByPid)) {
                // 递归查找子节点
                this.getDeleteMenus(menusByPid, deleteIds);
            }
        }
        return deleteIds;
    }

    /**
     * 更新上级菜单的subCount信息
     *
     * @param pid
     */
    private void updateSuperiorSubCount(Long pid) {
        if (pid != null && !pid.equals(0L)) {
            // 如果不是一级菜单
            // 通过pid查询同级菜单的个数
            Integer count = this.menuMapper.selectCount(Wrappers.<Menu>lambdaQuery().eq(Menu::getPid, pid));
            // 更新上级菜单的subCount信息
            this.menuMapper.update(null, Wrappers.<Menu>lambdaUpdate().set(count != null, Menu::getSubCount, count).eq(Menu::getId, pid));
        }
    }

    /**
     * 有外链的情况下验证外链是否正确
     *
     * @param resources
     */
    private void verifyIframe(Menu resources) {
        if (resources.getIFrame()) {
            String path = resources.getPath();
            if (StringUtils.startWithIgnoreCase(path, HTTP_PRE) || StringUtils.startWithIgnoreCase(path, HTTPS_PRE)) {
                return;
            }
            throw new BadRequestException("外链必须以http://或者https://开头");
        }
    }

    /**
     * 删除缓存信息
     *
     * @param id     /
     * @param oldPid /
     * @param newPid /
     */
    private void delCaches(Long id, Long oldPid, Long newPid) {
        // 根据当前的菜单查询与菜单关联的用户
        List<SysUser> users = this.userMapper.queryByMenuId(id);
        redisUtils.del(CacheKey.MENU_ID_KEY_PRE + id);
        // 删除当前用户的菜单缓存
        redisUtils.delByKeys(CacheKey.MENU_USER_KEY_PRE, users.stream().map(SysUser::getId).collect(Collectors.toSet()));
        redisUtils.del(CacheKey.MENU_PID_KEY_PRE + (oldPid == null ? 0 : oldPid));
        redisUtils.del(CacheKey.MENU_PID_KEY_PRE + (newPid == null ? 0 : newPid));
        // 清楚角色相关的缓存
        List<Role> roles = null;
        if (newPid == null) {
            roles = this.roleService.findInMenuId(Lists.newArrayList(id, oldPid));
        } else {
            roles = this.roleService.findInMenuId(Lists.newArrayList(id, oldPid, newPid));
        }
        this.redisUtils.delByKeys(CacheKey.ROLE_KEY_PRE, roles.stream().map(Role::getId).collect(Collectors.toSet()));
    }
}
