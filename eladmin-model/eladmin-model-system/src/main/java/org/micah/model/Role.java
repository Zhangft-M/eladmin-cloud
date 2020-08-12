package org.micah.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.micah.core.base.BaseEntity;
import org.micah.core.util.enums.DataScopeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description: 角色实体类
 * @author: Micah
 * @create: 2020-08-05 20:49
 **/
@Getter
@Setter
@TableName("sys_role")
public class Role extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5747259498744411870L;

    @TableId(value = "role_id",type = IdType.ASSIGN_ID)
    @NotNull(groups = {Update.class})
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "用户", hidden = true)
    private Set<SysUser> users;

    @TableField(exist = false)
    @ApiModelProperty(value = "菜单", hidden = true)
    private Set<Menu> menus;


    @TableField(exist = false)
    @ApiModelProperty(value = "部门", hidden = true)
    private Set<Dept> depts;

    @NotBlank
    @ApiModelProperty(value = "名称", hidden = true)
    private String name;

    @ApiModelProperty(value = "数据权限，全部 、 本级 、 自定义")
    private String dataScope = DataScopeEnum.THIS_LEVEL.getValue();

    @ApiModelProperty(value = "级别，数值越小，级别越大")
    private Integer level = 3;

    @ApiModelProperty(value = "描述")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
