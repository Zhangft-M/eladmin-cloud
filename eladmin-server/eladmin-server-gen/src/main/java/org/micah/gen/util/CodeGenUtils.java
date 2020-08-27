package org.micah.gen.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.*;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.micah.core.util.FileUtils;
import org.micah.core.util.StringUtils;
import org.micah.gen.model.ColumnInfo;
import org.micah.gen.model.GenConfig;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 代码生成工具类
 * @author: Micah
 * @create: 2020-08-18 17:34
 **/
@UtilityClass
public class CodeGenUtils {
    private final String TIMESTAMP = "Timestamp";

    private final String BIGDECIMAL = "BigDecimal";

    public final String PK = "PRI";

    public final String EXTRA = "auto_increment";

    /**
     * 获取后端代码模板的名称
     *
     * @return
     */
    public List<String> getAdminTemplateName() {
        List<String> adminTemplateNames = new ArrayList<>();
        adminTemplateNames.add("Controller");
        adminTemplateNames.add("Dto");
        adminTemplateNames.add("Entity");
        adminTemplateNames.add("IService");
        adminTemplateNames.add("Mapper");
        adminTemplateNames.add("QueryCriteria");
        adminTemplateNames.add("ServiceImpl");
        adminTemplateNames.add("Mapper.xml");
        return adminTemplateNames;
    }

    /**
     * 获取前端代码模板名称
     *
     * @return
     */
    public List<String> getFrontTemplateNames() {
        List<String> frontTemplateNames = new ArrayList<>();
        frontTemplateNames.add("Api");
        frontTemplateNames.add("Index");
        return frontTemplateNames;
    }

    /**
     * 预览代码
     *
     * @param columnInfoList
     * @param genConfig
     * @return
     */
    public List<Map<String, Object>> preview(List<ColumnInfo> columnInfoList, GenConfig genConfig) {
        // 生成模板的数据
        Map<String, Object> dataMap = CodeGenUtils.getTemplateData(columnInfoList, genConfig);
        // 存储各种页面的信息
        List<Map<String, Object>> genDataList = new ArrayList<>();
        // 获取后端的模板名
        List<String> adminTemplateNames = CodeGenUtils.getAdminTemplateName();
        // 初始化模板引擎
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        // 遍历，生成实际的文件 模板+数据 = 目的文件
        adminTemplateNames.forEach(name -> {
            // 使用map来存储生成的内容
            Map<String, Object> generateData = new HashMap<>(8);
            // 获取模板
            Template template = engine.getTemplate("generate/admin/" + name + ".ftl");
            // 生成对应的文件
            String render = template.render(dataMap);
            // 放入map中
            generateData.put("content", render);
            generateData.put("name", name);
            genDataList.add(generateData);
        });
        // 获取前端模版
        List<String> frontTemplateNames = getFrontTemplateNames();
        for (String templateName : frontTemplateNames) {
            Map<String, Object> map = new HashMap<>(1);
            Template template = engine.getTemplate("generate/front/" + templateName + ".ftl");
            map.put(templateName, template.render(dataMap));
            map.put("content", template.render(dataMap));
            map.put("name", templateName);
            genDataList.add(map);
        }
        return genDataList;
    }

    /**
     * 下载代码
     *
     * @param columnInfoList
     * @param genConfig
     * @return
     */
    public String download(List<ColumnInfo> columnInfoList, GenConfig genConfig) {
        // 定义文件生成的路径
        String temPath = FileUtils.SYS_TEM_DIR + "gen-temp" + File.separator + genConfig.getTableName() + File.separator;
        // 获取模板数据
        Map<String, Object> templateData = getTemplateData(columnInfoList, genConfig);
        // 初始化模板引擎
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        // 获取后端的模板名
        List<String> adminTemplateNames = CodeGenUtils.getAdminTemplateName();
        // 生成数据
        for (String templateName : adminTemplateNames) {
            // 获取对应的模板
            Template template = engine.getTemplate("generate/admin/" + templateName + ".ftl");
            String filePath = getAdminFilePath(templateName, genConfig, templateData.get("className").toString(), temPath + "admin" + File.separator);
            assert filePath != null;
            File file = new File(filePath);
            // 如果已经存在该文件并且非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                // 直接跳过，不再生成该文件
                continue;
            }
            // 生成代码
            genFile(file, template, templateData);
        }
        // 获取前端模板名称
        List<String> frontTemplateNames = getFrontTemplateNames();
        // 生成前端代码
        for (String templateName : frontTemplateNames) {
            // 获取前端模板
            Template template = engine.getTemplate("generate/front/" + templateName + ".ftl");
            // 前端工程目录
            String path = temPath + "web" + File.separator;
            // src下的api目录
            String apiPath = path + "src" + File.separator + "api" + File.separator;
            // src/views/类名称 目录
            String srcPath = path + "src" + File.separator + "views" + File.separator + templateData.get("changeClassName").toString() + File.separator;
            // 生成前端文件的完整路径和名称
            String filePath = getFrontFilePath(templateName, apiPath, srcPath, templateData.get("changeClassName").toString());
            assert filePath != null;
            File file = new File(filePath);
            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, templateData);
        }
        return temPath;
    }

    /**
     * 生成代码
     *
     * @param columnInfos
     * @param genConfig
     */
    public void generateCode(List<ColumnInfo> columnInfos, GenConfig genConfig) {
        Map<String, Object> genMap = getTemplateData(columnInfos, genConfig);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        // 生成后端代码
        List<String> templates = getAdminTemplateName();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generate/admin/" + templateName + ".ftl");
            String filePath = getAdminFilePath(templateName, genConfig, genMap.get("className").toString(), System.getProperty("user.dir"));

            assert filePath != null;
            File file = new File(filePath);

            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }

        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/front/" + templateName + ".ftl");
            String filePath = getFrontFilePath(templateName, genConfig.getApiPath(), genConfig.getPath(), genMap.get("changeClassName").toString());

            assert filePath != null;
            File file = new File(filePath);

            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
    }

    /**
     * 生成代码
     *
     * @param file
     * @param template
     * @param genMap
     */
    @SneakyThrows
    private void genFile(File file, Template template, Map<String, Object> genMap) {
        Writer writer = null;
        // 创建文件
        try {
            File touch = FileUtil.touch(file);
            writer = new FileWriter(touch);
            // 生成代码文件
            template.render(genMap, writer);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        } finally {
            assert writer != null;
            writer.close();
        }
    }

    /**
     * 生成前端文件名
     *
     * @param templateName
     * @param apiPath
     * @param path
     * @param apiName
     * @return
     */
    private String getFrontFilePath(String templateName, String apiPath, String path, String apiName) {
        if ("api".equals(templateName)) {
            return apiPath + File.separator + apiName + ".js";
        }

        if ("index".equals(templateName)) {
            return path + File.separator + "index.vue";
        }

        return null;
    }

    /**
     * 获取后端文件的路径和名称
     *
     * @param templateName
     * @param genConfig
     * @param className
     * @param rootPath
     * @return
     */
    private static String getAdminFilePath(String templateName, GenConfig genConfig, String className, String rootPath) {
        String projectPath = rootPath + File.separator + genConfig.getModuleName();
        String packagePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String resourcesPath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        if (!ObjectUtils.isEmpty(genConfig.getPack())) {
            // 将包名的点替换为系统文件分隔符号
            packagePath += genConfig.getPack().replace(".", File.separator) + File.separator;
        }

        if ("Entity".equals(templateName)) {
            return packagePath + "model" + File.separator + className + ".java";
        }

        if ("Controller".equals(templateName)) {
            return packagePath + "rest" + File.separator + className + "Controller.java";
        }

        if ("Service".equals(templateName)) {
            return packagePath + "service" + File.separator + "I" + className + "Service.java";
        }

        if ("ServiceImpl".equals(templateName)) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if ("Dto".equals(templateName)) {
            return packagePath + "model" + File.separator + "dto" + File.separator + className + "Dto.java";
        }

        if ("QueryCriteria".equals(templateName)) {
            return packagePath + "model" + File.separator + "query" + File.separator + className + "QueryCriteria.java";
        }

        if ("MapStruct".equals(templateName)) {
            return packagePath + "model" + File.separator + "mapstruct" + File.separator + className + "Mapper.java";
        }

        if ("Mapper".equals(templateName)) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }
        if ("Mapper.xml".equals(templateName)) {
            return resourcesPath + "mapper" + File.separator + className + "Mapper.xml";
        }

        return null;
    }

    /**
     * 生成模板的数据
     *
     * @param columnInfoList
     * @param genConfig
     * @return
     */
    private Map<String, Object> getTemplateData(List<ColumnInfo> columnInfoList, GenConfig genConfig) {
        Map<String, Object> templateData = new HashMap<>(32);
        // 初始化代码生成配置的数据
        CodeGenUtils.initGenConfigData(templateData, genConfig);
        // 初始化type配置的数据,先全部置为false，在设置每个column时再进行修改
        CodeGenUtils.initTypeData(templateData);
        // 初始化column信息
        CodeGenUtils.initColumnData(templateData, columnInfoList);
        return templateData;
    }

    /**
     * 初始化column信息
     *
     * @param templateData
     * @param columnInfoList
     */
    private void initColumnData(Map<String, Object> templateData, List<ColumnInfo> columnInfoList) {
        // 保存字段信息
        List<Map<String, Object>> columns = new ArrayList<>(16);
        // 保存查询字段的信息
        List<Map<String, Object>> queryColumns = new ArrayList<>(16);
        // 存储字典信息
        List<String> dictList = new ArrayList<>(8);
        // 存储 between 信息
        List<Map<String, Object>> betweenMaps = new ArrayList<>(8);
        // 存储不为空的字段信息
        List<Map<String, Object>> isNotNullColumns = new ArrayList<>(8);
        // 遍历字段信息
        columnInfoList.forEach(columnInfo -> {
            // 定义一个Map来存储columnInfo的信息
            Map<String, Object> columnMap = new HashMap<>(32);
            String colType = initBasicColumnData(templateData, dictList, columnInfo, columnMap);
            /*if (StringUtils.isNotBlank(columnInfo.getDateAnnotation())) {
                // 有时间注解
                templateData.put("hasDateAnnotation", true);
            }*/

            // 如果该字段是非空字段
            // 添加非空字段信息
            if (columnInfo.getNotNull()) {
                isNotNullColumns.add(columnMap);
            }
            //判断该字段是否为查询字段
            if (StringUtils.isNotBlank(columnInfo.getQueryType())) {
                // 初始化查询数据
                initQueryColumnData(templateData, queryColumns, betweenMaps, columnInfo, columnMap, colType);
            }
            // 添加到字段列表中
            columns.add(columnMap);
        });
        // 保存字段列表
        templateData.put("columns", columns);
        // 保存查询列表
        templateData.put("queryColumns", queryColumns);
        // 保存字段列表
        templateData.put("dicts", dictList);
        // 保存查询列表
        templateData.put("betweens", betweenMaps);
        // 保存非空字段信息
        templateData.put("isNotNullColumns", isNotNullColumns);
    }

    /**
     * 初始化字段的基本数据
     *
     * @param templateData 模板数据map
     * @param dictList     字典数据
     * @param columnInfo   每个字段的信息
     * @param columnMap    字段数据初始化map
     * @return
     */
    private String initBasicColumnData(Map<String, Object> templateData, List<String> dictList, ColumnInfo columnInfo, Map<String, Object> columnMap) {
        // 字段描述
        columnMap.put("remark", columnInfo.getRemark());
        // 主键类型
        columnMap.put("columnKey", columnInfo.getKeyType());
        // 字段类型
        String colType = ColumnUtils.transTypeToJavaType(columnInfo.getColumnType());
        // 小写开头的字段名
        String changeColumnName = StringUtils.toCamelCase(columnInfo.getColumnName());
        // 大写开头的字段名
        String capitalColumnName = StringUtils.toCapitalizeCamelCase(columnInfo.getColumnName());
        // 判断是否为主键
        if (PK.equals(columnInfo.getKeyType())) {
            // 存储字段为主键类型
            templateData.put("pkColumnType", colType);
            // 存储小写开头的字段名
            templateData.put("pkChangeColName", changeColumnName);
            // 存储大写开头的字段名
            templateData.put("pkCapitalColName", capitalColumnName);
        }
        // 判断是否存在TimeStamp类型
        if (TIMESTAMP.equals(colType)) {
            templateData.put("hasTimestamp", true);
        }
        // 是否存在 BigDecimal 类型的字段
        if (BIGDECIMAL.equals(colType)) {
            templateData.put("hasBigDecimal", true);
        }
        // 主键是否自增
        if (EXTRA.equals(columnInfo.getExtra())) {
            templateData.put("auto", true);
        }
        // 主键存在字典，在前端生成对应的字典数据展示
        if (StringUtils.isNotBlank(columnInfo.getDictName())) {
            templateData.put("hasDict", true);
            dictList.add(columnInfo.getDictName());
        }
        // 存储字段的类型
        columnMap.put("columnType", colType);
        // 存储原始字段的名称
        columnMap.put("columnName", columnInfo.getColumnName());
        // 不为空
        columnMap.put("isNotNull", columnInfo.getNotNull());
        // 字段列表显示
        columnMap.put("columnShow", columnInfo.getListShow());
        // 表单显示
        columnMap.put("formShow", columnInfo.getFormShow());
        // 表单组件显示
        columnMap.put("formType", StringUtils.isNotBlank(columnInfo.getFormType()) ? columnInfo.getFormType() : "Input");
        // 小写开头的字段名称
        columnMap.put("changeColumnName", changeColumnName);
        //大写开头的字段名称
        columnMap.put("capitalColumnName", capitalColumnName);
        // 字典名称
        columnMap.put("dictName", columnInfo.getDictName());
        // 日期注解
        columnMap.put("dateAnnotation", columnInfo.getDateAnnotation());
        return colType;
    }

    /**
     * 初始化查询条件的模板数据
     *
     * @param templateData 模板所需要的数据
     * @param queryColumns 查询中模板所需要的数据
     * @param betweenMaps  使用between条件查询时模板所需要的数据
     * @param columnInfo   字段信息
     * @param columnMap    模板所需要的所有的字段的数据
     * @param colType      字段对应的Java类型
     */
    private void initQueryColumnData(Map<String, Object> templateData, List<Map<String, Object>> queryColumns, List<Map<String, Object>> betweenMaps, ColumnInfo columnInfo, Map<String, Object> columnMap, String colType) {
        columnMap.put("queryType", columnInfo.getQueryType());
        // 是否存在查询
        templateData.put("hasQuery", true);
        if (TIMESTAMP.equals(colType)) {
            // 查询中存储 Timestamp 类型
            templateData.put("queryHasTimestamp", true);
        }
        if (BIGDECIMAL.equals(colType)) {
            // 查询中存储 BigDecimal 类型
            templateData.put("queryHasBigDecimal", true);
        }
        if ("between".equalsIgnoreCase(columnInfo.getQueryType())) {
            betweenMaps.add(columnMap);
        } else {
            // 添加到查询列表中
            queryColumns.add(columnMap);
        }
    }

    /**
     * 初始化type配置的数据,先全部置为false，在设置每个column时再进行修改
     *
     * @param templateData
     */
    private void initTypeData(Map<String, Object> templateData) {
        // 存在 Timestamp 字段
        templateData.put("hasTimestamp", false);
        // 查询类中存在 Timestamp 字段
        templateData.put("queryHasTimestamp", false);
        // 存在 BigDecimal 字段
        templateData.put("hasBigDecimal", false);
        // 查询类中存在 BigDecimal 字段
        templateData.put("queryHasBigDecimal", false);
        // 是否需要创建查询
        templateData.put("hasQuery", false);
        // 自增主键
        templateData.put("auto", false);
        // 存在字典
        templateData.put("hasDict", false);
        // 存在日期注解
        templateData.put("hasDateAnnotation", false);
    }

    /**
     * 初始化代码生成配置的数据
     *
     * @param templateData
     * @param genConfig
     */
    private void initGenConfigData(Map<String, Object> templateData, GenConfig genConfig) {
        // 接口名称
        templateData.put("apiAlias", genConfig.getApiAlias());
        // 保命
        templateData.put("package", genConfig.getPack());
        // 模块名
        templateData.put("moduleName", genConfig.getModuleName());
        // 作者
        templateData.put("author", genConfig.getAuthor());
        // 日期
        templateData.put("date", LocalDate.now(ZoneId.systemDefault()).toString());
        // 表名
        templateData.put("tableName", genConfig.getTableName());
        //首字母大写的类名
        String className = StringUtils.toCapitalizeCamelCase(genConfig.getTableName());
        // 首字母小写的类名
        String changeClassName = StringUtils.toCamelCase(genConfig.getTableName());
        // 判断是否去掉前缀
        if (StringUtils.isNotBlank(genConfig.getPrefix())) {
            // 去掉前缀
            className = StringUtils.toCapitalizeCamelCase(StrUtil.removePrefix(genConfig.getTableName(), genConfig.getPrefix()));
            changeClassName = StringUtils.toCamelCase(StrUtil.removePrefix(genConfig.getTableName(), genConfig.getPrefix()));
        }
        // 保存类名
        templateData.put("className", className);
        // 保存小写开头的类名
        templateData.put("changeClassName", changeClassName);
    }
}
