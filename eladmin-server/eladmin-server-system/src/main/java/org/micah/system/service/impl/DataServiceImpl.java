package org.micah.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.micah.core.util.enums.DataScopeEnum;
import org.micah.model.Dept;
import org.micah.model.RoleDeptRelation;
import org.micah.model.dto.RoleSmallDto;
import org.micah.model.dto.SysUserDto;
import org.micah.system.mapper.RoleDeptMapper;
import org.micah.system.service.IDataService;
import org.micah.system.service.IDeptService;
import org.micah.system.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-11 18:33
 **/
@Service
public class DataServiceImpl implements IDataService {

    private final IRoleService roleService;

    private final IDeptService deptService;

    private final RoleDeptMapper roleDeptMapper;

    // 实例化一个集合存储部门的id
    private final Set<Long> deptIds = new HashSet<>();

    public DataServiceImpl(IRoleService roleService, IDeptService deptService, RoleDeptMapper roleDeptMapper) {
        this.roleService = roleService;
        this.deptService = deptService;
        this.roleDeptMapper = roleDeptMapper;
    }

    /**
     * 通过用户查询用户拥有的部门数据权限
     *
     * @param userDto
     * @return
     */
    @Override
    public List<Long> getDeptIds(SysUserDto userDto) {
        // 根据用户的id查询角色信息
        List<RoleSmallDto> roleSmallDtoList = this.roleService.findByUsersId(userDto.getId());
        for (RoleSmallDto roleSmallDto : roleSmallDtoList) {
            // 获取对应的部门权限等级
            DataScopeEnum dataScopeEnum = DataScopeEnum.find(roleSmallDto.getDataScope());
            switch (Objects.requireNonNull(dataScopeEnum)){
                case THIS_LEVEL:
                    this.deptIds.add(userDto.getDeptId());
                    break;
                case CUSTOMIZE:
                    this.getCustomize(roleSmallDto);
                    break;
                default:
                    break;
            }
        }
        return new ArrayList<>(this.deptIds);
    }

    /**
     * 获取自定义的部门数据权限
     * @param roleSmallDto
     */
    private void getCustomize(RoleSmallDto roleSmallDto) {
        // 通过角色的id查询部门的id
        // List<Dept> deptList = this.deptService.findByRoleId(roleSmallDto.getId());
        List<RoleDeptRelation> roleDeptRelations = this.roleDeptMapper.selectList(Wrappers.<RoleDeptRelation>lambdaQuery()
                .eq(RoleDeptRelation::getRoleId, roleSmallDto.getId()));
        // 遍历部门数据
        roleDeptRelations.forEach(roleDeptRelation -> {
            this.deptIds.add(roleDeptRelation.getDeptId());
            // 查询子部门
            List<Dept> children = this.deptService.findByPid(roleDeptRelation.getDeptId());
            if (CollUtil.isNotEmpty(children)){
                // 加入到deptIds中
                this.deptIds.addAll(this.deptService.getDeptIds(children,this.deptIds));
            }
        });
    }
}
