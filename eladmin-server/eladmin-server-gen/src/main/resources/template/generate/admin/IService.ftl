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

package ${package}.service;

import ${package}.model.${className};
import ${package}.model.dto.${className}Dto;
import ${package}.model.query.${className}QueryCriteria;
import org.micah.core.web.page.PageResult;
import org.springframework.data.domain.Pageable;
java.util.Set;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @description ${className}服务接口
* @author ${author}
* @date ${date}
**/
public interface I${className}Service extends IService<${className}>{

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return PageResult 分页结果集
    */
    PageResult queryAll(${className}QueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<${className}Dto>
    */
    List<${className}Dto> queryAll(${className}QueryCriteria criteria);

    /**
     * 根据ID查询
     * @param ${pkChangeColName} ID
     * @return ${className}Dto
     */
    ${className}Dto findById(${pkColumnType} ${pkChangeColName});

    /**
     * 创建
     * @param resources /
     * @return ${className}Dto
     */
    ${className}Dto create(${className} resources);

    /**
     * 更新数据
     * @param resources /
     */
    void update${className}(${className} resources);

    /**
     * 批量删除
     * @param ids /
     */
    void deleteAll(Set<${pkColumnType}> ids);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<${className}Dto> all, HttpServletResponse response) throws IOException;
}