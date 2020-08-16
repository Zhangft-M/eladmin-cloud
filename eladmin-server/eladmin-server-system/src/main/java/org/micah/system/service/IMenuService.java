package org.micah.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.model.Menu;
import org.micah.model.dto.MenuDto;
import org.micah.model.query.MenuQueryCriteria;
import org.micah.model.vo.MenuVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 18:23
 **/
public interface IMenuService extends IService<Menu> {
    /**
     * 更具角色的id来查询菜单信息
     * @param ids
     * @return
     */
    List<MenuDto> queryByRoleIds(Set<Long> ids);

    /**
     * 查询全部数据
     * @param queryCriteria 条件
     * @param isQuery /
     * @return /
     */
    List<MenuDto> queryAll(MenuQueryCriteria queryCriteria, Boolean isQuery);

    /**
     * 查询全部数据
     * @param queryCriteria 条件
     * @param pageable /
     * @return /
     */
    PageResult queryAllByPage(MenuQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    MenuDto findById(Long id);

    /**
     * 创建
     * @param resources /
     */
    void create(Menu resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Menu resources);


    /**
     * 构建菜单树
     * @param menuDtos 原始数据
     * @return /
     */
    List<MenuDto> buildTree(List<MenuDto> menuDtos);

    /**
     * 构建菜单
     * @param menuDtos /
     * @param menuVos /
     * @return /
     */
    List<MenuVo> buildMenus(List<MenuDto> menuDtos,List<MenuVo> menuVos );

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    Menu findOne(Long id);

    /**
     * 删除
     * @param menuIds /
     */
    void delete(Set<Long> menuIds);

    /**
     * 导出
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<MenuDto> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 根据pid查找菜单
     * @param pid /
     * @return /
     */
    List<MenuDto> getMenusByPid(Long pid);

    /**
     * 根据ID获取该节点数据与上级数据,修改数据回显的时候调用
     * @param menuDto /
     * @param menuDtos /
     * @return /
     */
    List<MenuDto> getSuperior(MenuDto menuDto, List<MenuDto> menuDtos);

    /**
     * 根据当前用户获取菜单
     * @param currentUserId /
     * @return /
     */
    List<MenuDto> findByUser(Long currentUserId);
}
