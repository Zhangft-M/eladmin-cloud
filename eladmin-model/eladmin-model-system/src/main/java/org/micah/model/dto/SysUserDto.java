/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.micah.model.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.micah.core.base.BaseDTO;
import org.micah.core.excel.BoolCustomConverter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
public class SysUserDto extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 967234921171721967L;
    private Long id;

    private Set<RoleSmallDto> roles;

    private Set<JobSmallDto> jobs;

    private DeptSmallDto dept;

    private Long deptId;

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("昵称")
    private String nickName;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("性别")
    private String gender;

    private String avatarName;

    private String avatarPath;

    @JsonIgnore
    private String password;

    @ExcelProperty(value = "是否启用",converter = BoolCustomConverter.class)
    private Boolean enabled;

    @ExcelProperty(value = "是否为管理员",converter = BoolCustomConverter.class)
    @JsonIgnore
    private Boolean isAdmin = false;

    private Date pwdResetTime;
}
