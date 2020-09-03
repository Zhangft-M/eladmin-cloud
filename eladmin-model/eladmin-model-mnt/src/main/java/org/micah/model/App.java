/*
 * Copyright 2020-2025 Micah
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.micah.model;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import org.micah.core.base.BaseEntity;

import java.sql.Timestamp;
import java.io.Serializable;

/**
 * @author Micah
 * @description /
 * @date 2020-09-03
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("mnt_app")
public class App extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "app_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "应用名称")
    private String name;
    @ApiModelProperty(value = "上传目录")
    private String uploadPath;
    @ApiModelProperty(value = "部署路径")
    private String deployPath;
    @ApiModelProperty(value = "备份路径")
    private String backupPath;
    @ApiModelProperty(value = "应用端口")
    private Integer port;
    @ApiModelProperty(value = "启动脚本")
    private String startScript;
    @ApiModelProperty(value = "部署脚本")
    private String deployScript;
}