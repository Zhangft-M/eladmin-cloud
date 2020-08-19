package ${package}.service.impl;

import ${package}.model.${className};
<#if columns??>
    <#list columns as column>
        <#if column.columnKey = 'UNI'>
            <#if column_index = 1>
                import org.micah.exception.global.EntityExistException;
            </#if>
        </#if>
    </#list>
</#if>
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import ${package}.mapper.${className}Mapper;
import ${package}.model.dto.${className}Dto;
import ${package}.model.query.${className}QueryCriteria;
import ${package}.service.mapstruct.${className}MapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
<#if !auto && pkColumnType = 'Long'>
    import cn.hutool.core.lang.Snowflake;
    import cn.hutool.core.util.IdUtil;
</#if>
<#if !auto && pkColumnType = 'String'>
    import cn.hutool.core.util.IdUtil;
</#if>
import java.lang.IllegalArgumentException
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.micah.mp.util.QueryHelpUtils
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.micah.exception.global.CreateFailException
import org.micah.exception.global.DeleteFailException

import java.util.*;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author ${author}
* @date ${date}
**/
@Service
@RequiredArgsConstructor
public class ${className}ServiceImpl implements ${className}Service {

    private final ${className}Mapper ${changeClassName}Mapper;

    private final ${className}MapStruct ${changeClassName}MapStruct;

    @Override
    public PageResult queryAll(${className}QueryCriteria ${changeClassName}Criteria, Pageable pageable){
        Page<${className}> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<${className}> wrapper = QueryHelpUtils.getWrapper(${changeClassName}Criteria, ${className}.class);
        Page<${className}> ${changeClassName}Page = this.${changeClassName}Mapper.selectPage(page, wrapper);
        return PageResult.success(${changeClassName}Page.getTotal(), ${changeClassName}Page.getPages(),
                                    this.${changeClassName}MapStruct.toDto(${changeClassName}Page.getRecords()));
    }

    @Override
    public List<${className}Dto> queryAll(${className}QueryCriteria ${changeClassName}criteria){
        QueryWrapper<${className}> wrapper = QueryHelpUtils.getWrapper(${changeClassName}Criteria, ${className}.class);
        return this.${changeClassName}MapStruct.toDto(this.list(wrapper))
    }

    @Override
    @Transactional
    public ${className}Dto findById(${pkColumnType} ${pkChangeColName}) {
        if (${pkChangeColName} == null) {
            throw new IllegalArgumentException("参数为空");
        }
        ${className} ${changeClassName} = Optional.ofNullable(this.${changeClassName}Mapper.selectById(${pkChangeColName})).orElse(null);
        return this.${changeClassName}MapStruct.toDto(${changeClassName});
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${className}Dto create(${className} resources) {
    <#if columns??>
        <#list columns as column>
            <#if column.columnKey = 'UNI'>resources.get${column.capitalColumnName}()
        if(${changeClassName}mapper.selectOne(Wrappers.<${className}>lambdaQuery().eq(resources.get${column.capitalColumnName} != null,${className}::get${column.capitalColumnName},resources.get${column.capitalColumnName}())) != null){
            throw new EntityExistException(${className}.class,"${column.columnName}",resources.get${column.capitalColumnName}());
        }
            </#if>
        </#list>
    </#if>
        return ${changeClassName}MapStruct.toDto(this.${changeClassName}Mapper.insert(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(${className} resources) {
    <#if columns??>
        <#list columns as column>
            <#if column.columnKey = 'UNI'>
                <#if column_index = 1>
        ${className} other${changeClassName} = null;
                </#if>
        other${changeClassName} = this.${changeClassName}Mapper.selectOne(Wrappers.<${className}>lambdaQuery().eq(resources.get${column.capitalColumnName} != null,${className}::get${column.capitalColumnName},resources.get${column.capitalColumnName}()));
        if(other${changeClassName} != null && !other${changeClassName}.get${pkCapitalColName}().equals(${changeClassName}.get${pkCapitalColName}())){
           log.error("数据与其他数据有重复:{}", resources);
           throw new EntityExistException(${className}.class,"${column.columnName}",resources.get${column.capitalColumnName}());
        }
            </#if>
        </#list>
    </#if>
        if(this.updateById(resources)){
            log.error("插入失败:{}", resources);
            throw new CreateFailException("插入一条数据失败,请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Set<${pkColumnType}> ids) {
        if(!this.removeByIds(ids)){
            log.error("删除失败:{}", ids);
            throw new DeleteFailException("批量删除失败,请联系管理员")
        }
    }

    @Override
    public void download(List<${className}Dto> data, HttpServletResponse response) throws IOException {
        FileUtils.downloadFailedUsingJson(response, "${changeClassName}-info", ${className}Dto.class, data, "${changeClassName}-sheet");
    }
}