package org.micah.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.CacheKey;
import org.micah.core.util.FileUtils;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.exception.global.CreateFailException;
import org.micah.exception.global.DeleteFailException;
import org.micah.model.Dept;
import org.micah.model.RoleDeptRelation;
import org.micah.model.SysUser;
import org.micah.model.UserRoleRelation;
import org.micah.model.dto.DeptDto;
import org.micah.model.mapstruct.DeptMapStruct;
import org.micah.model.query.DeptQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.mp.util.SortUtils;
import org.micah.redis.util.RedisUtils;
import org.micah.system.mapper.DeptMapper;
import org.micah.system.mapper.RoleDeptMapper;
import org.micah.system.mapper.SysUserMapper;
import org.micah.system.mapper.UserRoleMapper;
import org.micah.system.service.IDeptService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description: 部门业务实现类
 * @author: Micah
 * @create: 2020-08-07 14:18
 **/
@Slf4j
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    private final DeptMapper deptMapper;

    private final DeptMapStruct deptMapStruct;

    private final UserRoleMapper userRoleMapper;

    private final RoleDeptMapper roleDeptMapper;

    private final SysUserMapper userMapper;

    private final RedisUtils redisUtils;



    public DeptServiceImpl(DeptMapper deptMapper, DeptMapStruct deptMapStruct, UserRoleMapper userRoleMapper, RoleDeptMapper roleDeptMapper, SysUserMapper userMapper, RedisUtils redisUtils) {
        this.deptMapper = deptMapper;
        this.deptMapStruct = deptMapStruct;
        this.userRoleMapper = userRoleMapper;
        this.roleDeptMapper = roleDeptMapper;
        this.userMapper = userMapper;
        this.redisUtils = redisUtils;
    }

    /**
     * 查询所有
     *
     * @param criteria 查询条件
     * @param pageable 分页和排序参数对象
     * @param isQuery  是查询还是导出数据
     * @return
     */
    @Override
    public PageResult queryAll(DeptQueryCriteria criteria, Pageable pageable, boolean isQuery) {
        if (!isQuery) {
            // 如果不是进行查询，只是用来导出数据
            // 进行排序查询
            QueryWrapper<Dept> queryWrapper = SortUtils.startSort(pageable.getSort());
            List<DeptDto> deptDtoList = this.deptMapStruct.toDto(this.list(queryWrapper));
            return PageResult.success((long) deptDtoList.size(), deptDtoList);
            DeleteFailException
        }
        // 初始化分页条件
        Page<Dept> page = PageUtils.startPageAndSort(pageable);
        // 初始化查询条件
        QueryWrapper<Dept> wrapper = QueryHelpUtils.getWrapper(criteria, Dept.class);
        Page<Dept> selectPage = this.deptMapper.selectPage(page, wrapper);
        return PageResult.success(selectPage.getTotal(), selectPage.getPages(), this.deptMapStruct.toDto(selectPage.getRecords()));
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public DeptDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("参数为空");
        }
        Dept dept = Optional.ofNullable(this.deptMapper.selectById(id)).orElse(null);
        return this.deptMapStruct.toDto(dept);
    }

    /**
     * 使用递归查询部门的数据以及该部门的子部门数据
     *
     * @param deptDto
     * @param deptDtos
     * @return
     */
    @Override
    public List<DeptDto> getSuperior(DeptDto deptDto, List<DeptDto> deptDtos) {
        if (deptDto.getPid() == 0) {
            deptDtos.addAll(this.deptMapStruct.toDto(this.findByPid(deptDto.getPid())));
            return deptDtos;
        }
        return this.getSuperior(this.findById(deptDto.getPid()), deptDtos);
    }

    /**
     * 构建数据成为树形
     *
     * @param deptDtos
     * @return
     */
    @Override
    public Map<String, Object> buildTree(List<DeptDto> deptDtos) {
        // 定义一个树形列表数据结构对象
        List<DeptDto> tree = new LinkedList<>();
        // 定义一个没有顶级节点的列表树形结构对象
        List<DeptDto> treeWithoutTopNode = new LinkedList<>();
        // 获取部门列表所有部门的名称
        List<String> deptNames = deptDtos.stream().map(DeptDto::getName).collect(Collectors.toList());
        boolean hasChildren;
        for (DeptDto deptDto : deptDtos) {
            hasChildren = false;
            if (deptDto.getPid() == 0) {
                // 为顶级节点
                // 直接加入树形数据结构列表中
                tree.add(deptDto);
            }
            // 二次遍历与外层循环遍历做比较，看是否是外层遍历节点的子节点
            for (DeptDto dto : deptDtos) {
                if (dto.getPid() != 0 && dto.getPid().equals(deptDto.getId())) {
                    hasChildren = true;
                    // 如果该节点不是顶级节点并且该节点是外层所遍历节点的子节点
                    if (deptDto.getChildren() == null) {
                        deptDto.setChildren(new LinkedList<>());
                    }
                    // 加入到子节点成员变量中
                    deptDto.getChildren().add(dto);
                }
            }
            // 当该节点不是顶级节点，并且传过来的参数列表没有节点是他的父节点的时候
            // 有子节点
            if (hasChildren) {
                treeWithoutTopNode.add(deptDto);
            } else if (deptDto.getPid() != 0 && !deptNames.contains(this.findById(deptDto.getPid()).getName())) {
                // 没有子节点,但是他是父节点
                treeWithoutTopNode.add(deptDto);
            }

        }
        // 如果传过来的数据很特殊，没有顶级节点，即pid==0的节点，则tree就是空的了
        // treeWithoutTopNode的存在就是避免这种情况,此时将treeWithoutTopNode的值给tree
        if (tree.isEmpty()) {
            tree = treeWithoutTopNode;
        }
        // 构建返回的数据结构
        Map<String, Object> map = new HashMap<>(3);
        map.put("totalElements", deptDtos.size());
        // 如果数据结构不能构成树形，则直接返回原来的数据
        map.put("content", CollectionUtil.isEmpty(tree) ? deptDtos : tree);
        return map;
    }

    /**
     * 增加一条数据
     *
     * @param resources
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Dept resources) {
        resources.setSubCount(0);
        int insert = this.deptMapper.insert(resources);
        // 插入数据成功则处理父节点数据
        if (insert != 0) {
            // 如果该节点有父节点，则清理父节点缓存然后更新父节点的subCount信息
            if (resources.getPid() != null && resources.getPid() != 0) {
                log.info("插入成功，处理父节点");
                // 删除父节点的缓存信息
                this.redisUtils.del(CacheKey.DEPT_PID_KEY_PRE + (resources.getPid() == null ? 0 : resources.getPid()));
                // 更新父节点的subCount信息
                this.updateSubCount(resources.getPid());
            }

        } else {
            log.error("插入失败:{}", resources);
            throw new CreateFailException("插入一条数据失败:" + resources.toString());
        }

    }

    /**
     * 在执行更新，添加，删除操作的时候对该节点的父节点的subCount属性进行修改
     *
     * @param pid
     */
    private void updateSubCount(Long pid) {
        // 首先查询该父节点有多少子节点
        Integer count = this.deptMapper.selectCount(Wrappers.<Dept>lambdaQuery().eq(Dept::getPid, pid));
        // 再修改父节点的信息
        this.deptMapper.update(null, Wrappers.<Dept>lambdaUpdate().set(Dept::getSubCount, count));
        System.out.println(count);
    }

    /**
     * 更新一条数据
     *
     * @param resources 需要修改的数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(Dept resources) {
        // 通过id查询数据库中存在的旧的数据
        Dept dept = this.deptMapper.selectById(resources.getId());
        // 获取旧部门的pid
        Long oldDeptPid = dept.getPid();
        // 获取新的数据中的pid
        Long newDeptPid = resources.getPid();
        // 如果新的数据不是顶级部门
        // 验证数据是否有误，即验证pid与id是否相同
        if (resources.getPid() != 0 && resources.getPid().equals(resources.getId())) {
            // 上级部门是自己，数据有误
            log.error("数据有误，部门的id与pid相同，请修改数据,id:{},pid{}", resources.getId(), resources.getPid());
            throw new IllegalArgumentException("数据有误，部门的id与pid相同，请修改数据");
        }
        // 数据无误,进行修改操作
        int result = this.deptMapper.updateById(resources);
        if (result > 0) {
            // 修改成功，开始修改新旧父部门的subCount信息
            if (!oldDeptPid.equals(newDeptPid)) {
                // 修改后的数据与修改前的数据的父部门id不相同则修改
                this.updateSubCount(oldDeptPid);
                this.updateSubCount(newDeptPid);
                // 删除所有的缓存
                this.delCaches(resources.getId(), oldDeptPid, newDeptPid);
            } else {
                // 只删除修改的数据的缓存
                this.redisUtils.del(CacheKey.DEPT_ID_KEY_PRE + resources.getId());
            }
        } else {
            log.info("修改失败,需要修改的数据为:{}", resources);
            throw new RuntimeException("修改失败，请联系管理员");
        }
    }

    /**
     * 删除所有的缓存
     *
     * @param id
     * @param oldDeptPid
     * @param newDeptPid
     */
    @Override
    public void delCaches(Long id, Long oldDeptPid, Long newDeptPid) {
        // TODO: 2020/8/8 删除用户拥有的部门权限，后续处理(已处理)
        List<UserRoleRelation> relations = this.userRoleMapper.selectByDeptId(id);
        Set<Long> userIds = relations.stream().map(UserRoleRelation::getUserId).collect(Collectors.toSet());
        // 删除数据权限
        redisUtils.delByKeys("data::user:", userIds);
        this.redisUtils.del(CacheKey.DEPT_ID_KEY_PRE + id);
        this.redisUtils.del(CacheKey.DEPT_PID_KEY_PRE + oldDeptPid);
        this.redisUtils.del(CacheKey.DEPT_PID_KEY_PRE + newDeptPid);
    }

    /**
     * 根据当前部门的id获取所有子部门的id
     *
     * @param childrenDeptList
     * @param deptIds
     * @return
     */
    @Override
    public Set<Long> getDeptIds(List<Dept> childrenDeptList, Set<Long> deptIds) {
        // Set<Long> deptIds = new HashSet<>();
        childrenDeptList.forEach(dept -> {
            // 首先把当前的id加入到集合中
            deptIds.add(dept.getId());
            // 查询是否存在子节点
            List<Dept> deptList = this.findByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptList)) {
                // 递归查询加入到
                deptIds.addAll(this.getDeptIds(deptList, deptIds));
            }
        });
        return deptIds;
    }

    /**
     * 通过roleId查询部门信息
     *
     * @param id
     * @return
     */
    @Override
    public List<Dept> findByRoleId(Long id) {
        List<Dept> deptList = this.deptMapper.findByRoleId(id);
        return Optional.ofNullable(deptList).orElse(null);
    }

    /**
     * 通过父部门查询
     *
     * @param id
     * @return
     */
    @Override
    public List<Dept> findByPid(Long id) {
        return this.deptMapper.selectList(Wrappers.<Dept>lambdaQuery().eq(Dept::getPid, id));
    }

    /**
     * 该方法的作用就是使用递归遍历所有的子节点，找到节点的所有子节点，然后加入需要删除的列表中
     *
     * @param deptList 需要删除的子节点，每个子节点可能还包含子节点
     * @param deptDtos 所有需要删除的节点列表
     * @return
     */
    public void getDeleteDepts(List<Dept> deptList, List<DeptDto> deptDtos) {
        deptList.forEach(dept -> {
            // 首先将该节点加入到需要删除的节点列表中
            deptDtos.add(this.deptMapStruct.toDto(dept));
            // 再进行查询，看是否还存在子节点
            List<Dept> depts = this.findByPid(dept.getId());
            if (CollUtil.isNotEmpty(depts)) {
                // 递归调用，直到查询出最后一个子节点
                getDeleteDepts(depts, deptDtos);
            }
        });
    }

    /**
     * 验证部门是否被角色或者用户关联
     *
     * @param deptDtos
     */
    private void verification(List<DeptDto> deptDtos) {
        // TODO: 2020/8/8 对部门与用户进行验证，看是否有部门与用户绑定或者与某个角色绑定(已解决)
        // 查询是否与用户绑定，即该部门下是否存在用户
        Set<Long> deptIds = deptDtos.stream().map(DeptDto::getId).collect(Collectors.toSet());
        Integer count = this.userMapper.selectCount(Wrappers.<SysUser>lambdaQuery().in(SysUser::getDeptId, deptIds));
        if (count > 0) {
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        Integer count1 = this.roleDeptMapper.selectCount(Wrappers.<RoleDeptRelation>lambdaQuery()
                .in(RoleDeptRelation::getDeptId, deptIds));
        if (count1 > 0) {
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    /**
     * 删除部门
     *
     * @param ids 需要删除的部门id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepts(List<Long> ids) {
        // 定义一个列表用来存储需要删除的部门
        final List<DeptDto> deptDtoList = new ArrayList<>();
        // 遍历需要删除的部门的id
        ids.forEach(id -> {
            // 查询需要删除的部门信息
            DeptDto deptDto = this.findById(id);
            // 加入到需要删除的列表中来
            deptDtoList.add(deptDto);
            // 查询该部门是否存在子部门
            List<Dept> deptList = this.findByPid(id);
            // 判断是否存在子部门
            if (CollUtil.isNotEmpty(deptList)) {
                // 存在子节点
                // 需要遍历子节点，查找出所有需要删除的节点
                this.getDeleteDepts(deptList, deptDtoList);
            }
        });
        // 验证需要删除的部门与用户或者角色是否有关联
        this.verification(deptDtoList);
        // 进行删除操作
        deptDtoList.forEach(deptDto -> {
            // 先删除
            int result = this.deptMapper.deleteById(deptDto.getId());
            // 判断是否删除成功
            if (result > 0) {
                // 删除缓存
                this.delCaches(deptDto.getId(), deptDto.getPid(), null);
                // 更新父节点信息
                this.updateSubCount(deptDto.getPid());
            } else {
                log.info("删除失败，需要删除的数据的id为:{}", deptDto.getId());
                throw new RuntimeException("删除失败");
            }
        });
    }

    /**
     * 导出所有的部门数据
     *
     * @param pageResult
     * @param response
     */
    @SneakyThrows
    @Override
    public void download(PageResult pageResult, HttpServletResponse response) {
        FileUtils.downloadFailedUsingJson(response, "dept-info", DeptDto.class, pageResult.getContent(), "sheet");
    }
}
