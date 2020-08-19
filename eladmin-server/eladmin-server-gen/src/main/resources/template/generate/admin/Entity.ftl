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

package ${package}.model;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
<#if isNotNullColumns??>
import javax.validation.constraints.*;
</#if>
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
<#if hasTimestamp>
    import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
    import java.math.BigDecimal;
</#if>
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author ${author}
* @date ${date}
**/
@Data
@TableName("${tableName}")
public class ${className} implements Serializable {

    private static final long serialVersionUID = 1L;

<#if columns??>

    <#list columns as column>
        <#if column.columnKey = 'PRI'>
            <#if auto>
        @TableId(value = "${column.columnName}",type = IdType.AUTO)
            <#else >
        @TableId(value = "${column.columnName}",type = IdType.ASSIGN_ID)
            </#if>

        </#if>
        <#if column.istNotNull && column.columnKey != 'PRI'>
            <#if column.columnType = 'String'>
        @NotBlank
            <#else>
        @NotNull
            </#if>
        </#if>
        <#if column.remark != ''>
        @ApiModelProperty(value = "${column.remark}")
        <#else>
        @ApiModelProperty(value = "${column.changeColumnName}")
        </#if>
        private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
