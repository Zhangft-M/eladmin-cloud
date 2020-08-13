package org.micah.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.StringUtils;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.DeleteFailException;
import org.micah.model.Dict;
import org.micah.model.DictDetail;
import org.micah.model.dto.DictDetailDto;
import org.micah.model.mapstruct.DictDetailMapStruct;
import org.micah.model.query.DictDetailQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.mp.util.SortUtils;
import org.micah.redis.util.RedisUtils;
import org.micah.system.mapper.DictDetailMapper;
import org.micah.system.mapper.DictMapper;
import org.micah.system.service.IDictDetailService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: 字典详情业务
 * @author: Micah
 * @create: 2020-08-09 15:38
 **/
@Slf4j
@Service
public class DictDetailServiceImpl extends ServiceImpl<DictDetailMapper, DictDetail> implements IDictDetailService {

    private final DictDetailMapper dictDetailMapper;

    private final DictDetailMapStruct dictDetailMapStruct;

    private final RedisUtils redisUtils;

    private final DictMapper dictMapper;

    public static final String DICT_KEY_PRE = "dept::id:";

    public DictDetailServiceImpl(DictDetailMapper dictDetailMapper,
                                 DictDetailMapStruct dictDetailMapStruct,
                                 RedisUtils redisUtils,
                                 DictMapper dictMapper) {
        this.dictDetailMapper = dictDetailMapper;
        this.dictDetailMapStruct = dictDetailMapStruct;
        this.redisUtils = redisUtils;
        this.dictMapper = dictMapper;
    }

    /**
     * 分页查询所有的字典详情
     *
     * @param queryCriteria 查询条件
     * @param pageable      分页对象，包含分页的页码和每页数据量以及排序信息
     * @param isQuery
     * @return
     */
    @Override
    public PageResult queryAll(DictDetailQueryCriteria queryCriteria, Pageable pageable, Boolean isQuery) {
        if (!isQuery) {
            // 导出数据，查询所有的数据
            QueryWrapper<DictDetail> queryWrapper = SortUtils.startSort(pageable.getSort());
            List<DictDetail> dictDetails = this.dictDetailMapper.selectList(queryWrapper);
            return PageResult.success((long) dictDetails.size(), this.dictDetailMapStruct.toDto(dictDetails));
        }
        // 查询数据，根据条件查询
        Page<DictDetail> page = PageUtils.startPageAndSort(pageable);
        if (StringUtils.isNotBlank(queryCriteria.getDictName())){
            // 通过dict表中的name字段查询dictDetail表的信息
            Page<DictDetail> dictDetailPage = this.dictDetailMapper.selectByDictName(page,queryCriteria.getDictName());
            return PageResult.success(dictDetailPage.getTotal(),
                    this.dictDetailMapStruct.toDto(dictDetailPage.getRecords()));

        }
        // 根据label条件查询
        QueryWrapper<DictDetail> queryWrapper = QueryHelpUtils.getWrapper(queryCriteria, DictDetail.class);
        Page<DictDetail> dictDetailPage = this.dictDetailMapper.selectPage(page, queryWrapper);
        return PageResult.success(dictDetailPage.getTotal(),
                this.dictDetailMapStruct.toDto(dictDetailPage.getRecords()));
    }

    /**
     * 根据字典的名称查询字典的详情
     *
     * @param name
     * @return
     */
    @Override
    public List<DictDetailDto> getDictByName(String name) {
        Page<DictDetail> dictDetailPage = this.dictDetailMapper.selectByDictName(null, name);
        return this.dictDetailMapStruct.toDto(dictDetailPage.getRecords());
    }

    /**
     * 增加一条数据
     *
     * @param resources
     */
    @Override
    public void create(DictDetail resources) {
        int insert = this.dictDetailMapper.insert(resources);
        if (insert != 0){
            log.info("添加成功，开始删除缓存");
            // 删除掉字典表（dict）的缓存数据
            this.delCaches(resources);
        }else {
            log.info("添加失败:{}",resources);
            throw new DeleteFailException("添加失败"+resources.toString());
        }
    }

    /**
     * 更新一条数据
     *
     * @param resources
     */
    @Override
    public void updateDictDetail(DictDetail resources) {
        int result = this.dictDetailMapper.updateById(resources);
        if (result != 0){
            log.info("更新成功，开始删除缓存");
            this.delCaches(resources);
        }else {
            log.info("更新失败:{}",resources);
            throw new DeleteFailException("更新失败"+resources.toString());
        }
    }

    /**
     * 删除一条数据
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        int result = this.dictDetailMapper.deleteById(id);
        if (result != 0){
            log.info("删除成功，开始删除缓存");
            DictDetail dictDetail = this.dictDetailMapper.selectById(id);
            this.delCaches(dictDetail);
        }else {
            log.info("删除失败，需要删除的数据的id为:{}",id);
            throw new DeleteFailException("删除失败，需要删除的数据的id为:"+id);
        }
    }

    /**
     * 删除数据缓存
     * @param dictDetail
     */
    private void delCaches(DictDetail dictDetail) {
        this.redisUtils.del(DICT_KEY_PRE + dictDetail.getId());
    }
}
