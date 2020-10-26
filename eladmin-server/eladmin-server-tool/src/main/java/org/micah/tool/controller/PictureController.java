package org.micah.tool.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.log.annotation.Log;
import org.micah.model.Picture;
import org.micah.model.query.PictureQueryCriteria;
import org.micah.security.util.SecurityUtils;
import org.micah.tool.service.IPictureService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-15 16:20
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/pictures")
@Api(tags = "工具：阿里云OSS")
public class PictureController {

    private final IPictureService pictureService;

    @Log("查询图片")
    @PreAuthorize("@el.check('pictures:list')")
    @GetMapping
    @ApiOperation("查询图片")
    public ResponseEntity<PageResult> query(PictureQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pictureService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pictures:list')")
    public void download(HttpServletResponse response, PictureQueryCriteria criteria) throws IOException {
        pictureService.download(pictureService.queryAll(criteria), response);
    }

    @Log("上传图片")
    @PreAuthorize("@el.check('pictures:add')")
    @PostMapping
    @ApiOperation("上传图片")
    public ResponseEntity<Picture> upload(@RequestParam MultipartFile file){
        String userName = SecurityUtils.getCurrentUsername();
        Picture picture = pictureService.upload(file,userName);
        return new ResponseEntity<>(picture,HttpStatus.OK);
    }

    @Log("同步图床数据")
    @ApiOperation("同步图床数据")
    @PostMapping(value = "/synchronize")
    public ResponseEntity<Void> synchronize(){
        pictureService.synchronize();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("多选删除图片")
    @ApiOperation("多选删除图片")
    @PreAuthorize("@el.check('pictures:del')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids) {
        pictureService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
