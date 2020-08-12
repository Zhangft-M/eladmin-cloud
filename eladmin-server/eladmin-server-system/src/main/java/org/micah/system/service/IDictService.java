package org.micah.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.model.Dict;
import org.micah.model.query.DictQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-08 21:32
 **/
public interface IDictService extends IService<Dict> {
    /**
     * 分页根据条件查询字典
     * @param criteria
     * @param isQuery
     * @return
     */
    PageResult queryAll(DictQueryCriteria criteria, Pageable pageable, Boolean isQuery);

    /**
     * 导出数据为excel文件
     * @param queryAll
     * @param response
     */
    void download(PageResult queryAll, HttpServletResponse response);

    /**
     * 增加一条字典数据
     * @param resources
     */
    void create(Dict resources);

    /**
     * 更新字典数据
     * @param resources
     */
    void updateDict(Dict resources);

    /**
     * 根据id删除字典数据
     * @param ids
     */
    void delete(List<Long> ids);


}
