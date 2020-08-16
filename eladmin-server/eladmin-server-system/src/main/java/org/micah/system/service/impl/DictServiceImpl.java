package org.micah.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.CreateFailException;
import org.micah.exception.global.UpdateFailException;
import org.micah.model.Dict;
import org.micah.model.DictDetail;
import org.micah.model.dto.DictDto;
import org.micah.model.mapstruct.DictMapStruct;
import org.micah.model.query.DictQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.mp.util.SortUtils;
import org.micah.redis.util.RedisUtils;
import org.micah.system.mapper.DictDetailMapper;
import org.micah.system.mapper.DictMapper;
import org.micah.system.service.IDictService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: 字典业务实现类
 * @author: Micah
 * @create: 2020-08-08 21:44
 **/
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    private final DictMapper dictMapper;

    private final DictDetailMapper dictDetailMapper;

    private final DictMapStruct mapStruct;

    private final RedisUtils redisUtils;

    public static final String DICT_KEY_PRE = "dict::name:";

    public DictServiceImpl(DictMapper dictMapper, DictDetailMapper dictDetailMapper, DictMapStruct mapStruct, RedisUtils redisUtils) {
        this.dictMapper = dictMapper;
        this.dictDetailMapper = dictDetailMapper;
        this.mapStruct = mapStruct;
        this.redisUtils = redisUtils;
    }

    /**
     * 分页根据条件查询字典
     *
     * @param queryCriteria
     * @param isQuery
     * @return
     */
    @Override
    public PageResult queryAll(DictQueryCriteria queryCriteria, Pageable pageable, Boolean isQuery) {
        if (!isQuery) {
            // 导出数据，查询所有
            QueryWrapper<Dict> queryWrapper = SortUtils.startSort(pageable.getSort());
            List<Dict> dictList = this.dictMapper.selectList(queryWrapper);
            return PageResult.success((long) dictList.size(), dictList);
        }
        // 查询数据，根据条件查询
        // Page<Dict> startPage = PageUtils.startPage(page, size);
        Page<Dict> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<Dict> queryWrapper = QueryHelpUtils.getWrapper(queryCriteria,Dict.class);
        Page<Dict> selectPage = this.dictMapper.selectPage(page, queryWrapper);
        return PageResult.success(selectPage.getTotal(), selectPage.getPages(),
                this.mapStruct.toDto(selectPage.getRecords()));
    }

    /**
     * 导出数据为excel文件
     *
     * @param result
     * @param response
     */
    @SneakyThrows
    @Override
    public void download(PageResult result, HttpServletResponse response) {
        // TODO: 2020/8/9 需要导出的数据有字典数据以及字典详细数据
        FileUtils.downloadFailedUsingJson(response, "dict", DictDto.class, result.getContent(), "sheet");
    }

    /**
     * 增加一条字典数据
     *
     * @param resources
     */
    @Override
    public void create(Dict resources) {
        int result = this.dictMapper.insert(resources);
        if (result == 0) {
            log.info("添加数据失败:{}", resources);
            throw new CreateFailException("添加数据失败:" + resources.toString());
        }
    }

    /**
     * 更新字典数据
     *
     * @param resources
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(Dict resources) {
        // 删除原有的缓存
        this.delCaches(resources);
        // 进行更新
        int result = this.dictMapper.updateById(resources);
        if (result == 0) {
            log.info("更新数据失败:{}", resources);
            throw new UpdateFailException("更新数据失败:" + resources.toString());
        }
    }

    /**
     * 根据id删除字典数据
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        // 查询所有
        List<Dict> dicts = this.listByIds(ids);
        // 删除缓存
        dicts.forEach(this::delCaches);
        // 删除所有数据
        this.dictMapper.deleteBatchIds(ids);
        // TODO: 2020/8/9 删除字典详细信息(已处理)
        dicts.forEach(dict -> {
            this.dictDetailMapper.delete(new QueryWrapper<DictDetail>().eq("dict_id",dict.getId()));
        });
    }

    /**
     * 删除缓存
     *
     * @param dict
     */
    private void delCaches(Dict dict) {
        this.redisUtils.del(DICT_KEY_PRE + dict.getId());
    }
}
