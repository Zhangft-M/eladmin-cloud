package org.micah.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.model.DictDetail;
import org.micah.model.dto.DictDetailDto;
import org.micah.model.query.DictDetailQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-09 15:13
 **/
public interface IDictDetailService extends IService<DictDetail> {
    /**
     * 分页查询所有的字典详情
     * @param queryCriteria 查询条件
     * @param pageable 分页参数封装对象
     * @param isQuery
     * @return
     */
    PageResult queryAll(DictDetailQueryCriteria queryCriteria, Pageable pageable, Boolean isQuery);

    /**
     * 更具字典的名称查询字典的详情
     * @param name
     * @return
     */
    List<DictDetailDto> getDictByName(String name);

    /**
     * 增加一条数据
     * @param resources
     */
    void create(DictDetail resources);

    /**
     * 更新一条数据
     * @param resources
     */
    void updateDictDetail(DictDetail resources);

    /**
     * 删除一条数据
     * @param id
     */
    void delete(Long id);
}
