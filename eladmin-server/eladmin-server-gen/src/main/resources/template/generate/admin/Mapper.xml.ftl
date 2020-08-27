<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${package}.${moduleName}.mapper.${className}Mapper">

    <resultMap id="${className}Map" type="${package}.model.${className}">
        <#list columns as column>
            <#if column.columnKey = 'PRI'>
       <id property="${column.changeColumnName}" column="${column.columnName}"/>
            </#if>
       <result property="${column.changeColumnName}" column="${column.columnName}"/>
        </#list>
    </resultMap>

    <sql id="baseSelectSqlWithProperties">
        <#list columns as column>
            <#if column_has_next>
        ${r"${propertise}"}.${column.columnName},
            <#else>
        ${r"${propertise}"}.${column.columnName}
            </#if>
        </#list>
    </sql>

    <sql id="baseSql">
        <#list columns as column>
            <#if column_has_next>
        ${column.columnName},
            <#else>
        ${column.columnName}
            </#if>
        </#list>
    </sql>
</mapper>