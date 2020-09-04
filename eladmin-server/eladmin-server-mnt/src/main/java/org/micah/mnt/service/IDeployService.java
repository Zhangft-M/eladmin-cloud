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

import org.micah.model.Deploy;
import org.micah.model.DeployHistory;
import org.micah.model.dto.DeployDto;
import org.micah.model.query.DeployQueryCriteria;
import org.micah.core.web.page.PageResult;
import org.springframework.data.domain.Pageable;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @description Deploy服务接口
* @author Micah
* @date 2020-09-03
**/
public interface IDeployService extends IService<Deploy>{

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return PageResult 分页结果集
    */
    PageResult queryAll(DeployQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DeployDto>
    */
    List<DeployDto> queryAll(DeployQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param deployId ID
     * @return DeployDto
     */
    DeployDto findById(Long deployId);

    /**
     * 创建
     * @param resources /
     * @return DeployDto
     */
    DeployDto create(Deploy resources);

    /**
     * 更新数据
     * @param resources /
     */
    void updateDeploy(Deploy resources);

    /**
     * 批量删除
     * @param ids /
     */
    void deleteAll(Set<Long> ids);

    /**
     * 导出数据
     * @param data 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<DeployDto> data, HttpServletResponse response) throws IOException;

    /**
     * 上传文件部署
     * @param id
     * @param file
     * @return
     */
    Map<String, Object> deployApp(Long id, MultipartFile file);

    /**
     * 系统还原
     * @param resources
     * @return
     */
    String serverReduction(DeployHistory resources);

    /**
     * 获取服务运行状态
     * @param resources
     * @return
     */
    String serverStatus(Deploy resources);

    /**
     * 启动服务
     * @param resources
     * @return
     */
    String startServer(Deploy resources);

    /**
     * 停止服务
     * @param resources
     * @return
     */
    String stopServer(Deploy resources);
}