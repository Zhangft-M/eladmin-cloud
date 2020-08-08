package org.micah.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.model.Dept;
import org.micah.model.dto.DeptDto;
import org.micah.model.query.DeptQueryCriteria;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-07 14:17
 **/
public interface IDeptService extends IService<Dept> {
    /**
     * 查询所有
     * @param criteria 查询条件
     * @param page
     * @param size
     * @param isQuery 是查询还是导出数据
     * @return
     */
    PageResult queryAll(DeptQueryCriteria criteria, Integer page, Integer size, boolean isQuery);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    DeptDto findById(Long id);

    /**
     * 使用递归查询部门的数据以及该部门的子部门数据
     * @param deptDto
     * @param deptList
     * @return
     */
    List<DeptDto> getSuperior(DeptDto deptDto, List<DeptDto> deptList);

    /**
     * 构建数据成为树形
     * @param deptDtos
     * @return
     */
    Map<String,Object> buildTree(List<DeptDto> deptDtos);

    /**
     * 增加一条数据
     * @param resources
     */
    void create(Dept resources);

    /**
     * 更新一条数据
     * @param resources
     */
    void updateDept(Dept resources);

    /**
     * 通过父部门查询
     * @param id
     * @return
     */
    List<Dept> findByPid(Long id);




    /**
     * 删除部门
     * @param ids 需要删除的部门的id
     */
    void deleteDepts(List<Long> ids);

    /**
     * 导出所有的部门数据
     * @param queryAll
     * @param response
     */
    void download(PageResult queryAll, HttpServletResponse response);

    /**
     * 删除所有的缓存信息
     * @param id
     * @param oldDeptPid
     * @param newDeptPid
     */
    void delCaches(Long id, Long oldDeptPid, Long newDeptPid);
}
