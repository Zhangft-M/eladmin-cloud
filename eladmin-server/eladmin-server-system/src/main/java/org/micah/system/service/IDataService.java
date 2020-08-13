package org.micah.system.service;

import org.micah.model.dto.SysUserDto;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-10 21:26
 **/
public interface IDataService {
    /**
     * 通过用户查询用户拥有的部门数据权限
     * @param userDto /
     * @return /
     */
    List<Long> getDeptIds(SysUserDto userDto);
}
