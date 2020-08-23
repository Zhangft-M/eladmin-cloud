package org.micah.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.micah.model.Dept;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-07 14:19
 **/
public interface DeptMapper extends BaseMapper<Dept> {
    /**
     * 根据角色的id查询部门信息
     * @param id
     * @return
     */
    Set<Dept> findByRoleId(Long id);
}
