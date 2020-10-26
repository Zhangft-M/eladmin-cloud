package org.micah.tool.service.impl;

import com.alibaba.alicloud.context.oss.OssProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.model.Picture;
import org.micah.model.query.PictureQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.tool.config.OssConfigProperties;
import org.micah.tool.mapper.PictureMapper;
import org.micah.tool.service.IPictureService;
import org.micah.tool.util.PicUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-16 17:02
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {

    private final OSS ossClient;

    private final OssConfigProperties ossConfigProperties;

    /**
     * 分页查询
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Override
    public PageResult queryAll(PictureQueryCriteria criteria, Pageable pageable) {
        Page<Picture> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<Picture> wrapper = QueryHelpUtils.getWrapper(criteria, Picture.class);
        Page<Picture> picturePage = this.page(page, wrapper);
        return PageResult.success(picturePage.getTotal(), picturePage.getPages(), picturePage.getRecords());
    }

    /**
     * 不分页查询
     *
     * @param criteria
     * @return
     */
    @Override
    public List<Picture> queryAll(PictureQueryCriteria criteria) {
        QueryWrapper<Picture> wrapper = QueryHelpUtils.getWrapper(criteria, Picture.class);
        return this.list(wrapper);
    }

    /**
     * 导出数据
     *
     * @param data
     * @param response
     */
    @Override
    @SneakyThrows
    public void download(List<Picture> data, HttpServletResponse response) {
        FileUtils.downloadFailedUsingJson(response, "picture", Picture.class, data, "pictureSheet");
    }

    /**
     * 上传图片
     *
     * @param file
     * @param userName
     * @return
     */
    @Override
    @SneakyThrows
    public Picture upload(MultipartFile file, String userName) {
        // 校验
        Boolean type = PicUtils.checkFileType(file.getOriginalFilename());
        if (!type) {
            throw new BadRequestException("图片格式有误,仅支持jpg,jepg,bmp,gif,png格式的图片");
        }
        // 生成图片路径
        String filePath = PicUtils.createFilePath(file);
        // 上传
        Picture picture = new Picture();
        PutObjectResult result = this.ossClient.putObject(this.ossConfigProperties.getBucketName(), filePath, file.getInputStream());
        picture.setUrl(result.getResponse().getUri());
        picture.setBucketName(this.ossConfigProperties.getBucketName());
        picture.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        picture.setFilename(FileUtils.getFileNameWithOutEx(file.getOriginalFilename()));
        picture.setMd5Code(FileUtils.getMD5(file.getBytes()));
        picture.setDelete(filePath);
        picture.setSize(FileUtils.transformSize(file.getSize()));
        this.save(picture);
        return picture;
    }

    /**
     * 同步数据
     */
    @Override
    public void synchronize() {

    }

    /**
     * 删除数据
     *
     * @param ids
     */
    @Override
    public void deleteAll(Set<Long> ids) {
        ids.forEach(id->{
            Picture picture = this.getById(id);
            this.ossClient.deleteObject(picture.getBucketName(),picture.getDelete());
            this.removeById(id);
        });

    }
}
