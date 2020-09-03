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

package org.micah.model.dto;

import lombok.Data;
import org.micah.core.base.BaseDTO;

import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author Micah
* @date 2020-09-03
**/
@Data
public class AppDto extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 应用编号
     */
    private Long id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 上传目录
     */
    private String uploadPath;

    /**
     * 部署目录
     */
    private String deployPath;

    /**
     * 备份目录
     */
    private String backupPath;

    /**
     * 启动脚本
     */
    private String startScript;

    /**
     * 部署脚本
     */
    private String deployScript;
}