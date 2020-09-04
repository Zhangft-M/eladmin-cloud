package org.micah.mnt.service.impl;


import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;
import org.micah.exception.global.BadRequestException;
import org.micah.mnt.util.SqlUtils;
import org.micah.model.Database;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.mnt.mapper.DatabaseMapper;
import org.micah.model.dto.DatabaseDto;
import org.micah.model.query.DatabaseQueryCriteria;
import org.micah.model.mapstruct.DatabaseMapStruct;
import org.micah.mnt.service.IDatabaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.IllegalArgumentException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.micah.mp.util.QueryHelpUtils;
import org.springframework.data.domain.Pageable;
import org.micah.mp.util.PageUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.micah.exception.global.CreateFailException;
import org.micah.exception.global.DeleteFailException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author Micah
* @date 2020-09-03
**/
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseServiceImpl extends ServiceImpl<DatabaseMapper,Database> implements IDatabaseService {

    private final DatabaseMapper databaseMapper;

    private final DatabaseMapStruct databaseMapStruct;

    private static final String FILE_SAVE_PATH = FileUtil.getTmpDirPath()+"/";

    @Override
    public PageResult queryAll(DatabaseQueryCriteria databaseCriteria, Pageable pageable){
        Page<Database> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<Database> wrapper = QueryHelpUtils.getWrapper(databaseCriteria, Database.class);
        Page<Database> databasePage = this.databaseMapper.selectPage(page, wrapper);
        return PageResult.success(databasePage.getTotal(), databasePage.getPages(),
                                    this.databaseMapStruct.toDto(databasePage.getRecords()));
    }

    @Override
    public List<DatabaseDto> queryAll(DatabaseQueryCriteria criteria){
        QueryWrapper<Database> wrapper = QueryHelpUtils.getWrapper(criteria, Database.class);
        return this.databaseMapStruct.toDto(this.list(wrapper));
    }

    @Override
    public DatabaseDto findById(String dbId) {
        if (dbId == null) {
            throw new IllegalArgumentException("参数为空");
        }
        Database database = Optional.ofNullable(this.databaseMapper.selectById(dbId)).orElse(null);
        return this.databaseMapStruct.toDto(database);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatabaseDto create(Database resources) {
        if (!this.save(resources)){
            log.warn("插入一条数据失败:{}",resources);
            throw new CreateFailException("插入一条数据失败,请联系管理员");
        }
        return databaseMapStruct.toDto(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDatabase(Database resources) {
        if(this.updateById(resources)){
            log.warn("插入失败:{}", resources);
            throw new CreateFailException("插入一条数据失败,请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Set<String> ids) {
        if(!this.removeByIds(ids)){
            log.warn("删除失败:{}", ids);
            throw new DeleteFailException("批量删除失败,请联系管理员");
        }
    }

    @Override
    public void download(List<DatabaseDto> data, HttpServletResponse response) throws IOException {
        FileUtils.downloadFailedUsingJson(response, "database-info", DatabaseDto.class, data, "database-sheet");
    }

    /**
     * 测试数据库连接
     *
     * @param resources
     * @return
     */
    @Override
    public Boolean testConnection(Database resources) {
        return SqlUtils.testConnection(resources.getJdbcUrl(),resources.getUserName(),resources.getPwd());
    }

    /**
     * 执行sql文件
     *
     * @param id
     * @param file
     * @return
     */
    @Override
    @SneakyThrows
    public String executeSqlFile(String id, MultipartFile file) {
        // 查询执行的数据库
        Database database = this.getById(id);
        String fileName;
        if (!Objects.isNull(database)){
            fileName = file.getOriginalFilename();
            File executeFile = new File(FILE_SAVE_PATH + fileName);
            // 先删除原文件，保证不存在该文件
            FileUtil.del(executeFile);
            // 上传文件
            file.transferTo(executeFile);
            // 执行
            return SqlUtils.executeFile(database.getJdbcUrl(), database.getUserName(), database.getPwd(), executeFile);
        }else {
            log.warn("传入参数可能有误,无法找到该数据库:{}",id);
            throw new BadRequestException("无法找到执行文件的数据库");
        }
    }
}