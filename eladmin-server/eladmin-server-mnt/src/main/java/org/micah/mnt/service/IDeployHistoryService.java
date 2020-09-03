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

package org.micah.mnt.service;

import org.micah.model.DeployHistory;
import org.micah.model.dto.DeployHistoryDto;
import org.micah.model.query.DeployHistoryQueryCriteria;
import org.micah.core.web.page.PageResult;
import org.springframework.data.domain.Pageable;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @description DeployHistory服务接口
* @author Micah
* @date 2020-09-03
**/
public interface IDeployHistoryService extends IService<DeployHistory>{

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return PageResult 分页结果集
    */
    PageResult queryAll(DeployHistoryQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DeployHistoryDto>
    */
    List<DeployHistoryDto> queryAll(DeployHistoryQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param historyId ID
     * @return DeployHistoryDto
     */
    DeployHistoryDto findById(String historyId);

    /**
     * 创建
     * @param resources /
     * @return DeployHistoryDto
     */
    DeployHistoryDto create(DeployHistory resources);

    /**
     * 更新数据
     * @param resources /
     */
    void updateDeployHistory(DeployHistory resources);

    /**
     * 批量删除
     * @param ids /
     */
    void deleteAll(Set<String> ids);

    /**
     * 导出数据
     * @param data 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<DeployHistoryDto> data, HttpServletResponse response) throws IOException;
}