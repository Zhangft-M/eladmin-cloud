package org.micah.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.micah.model.DictDetail;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-09 15:39
 **/
public interface DictDetailMapper extends BaseMapper<DictDetail> {
    /**
     * 通过字典表的名称查询字典详情
     * @param page
     * @param dictName
     * @return
     */
    Page<DictDetail> selectByDictName(Page<DictDetail> page, String dictName);
}
