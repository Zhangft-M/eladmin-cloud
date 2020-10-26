package org.micah.tool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.model.Picture;
import org.micah.model.query.PictureQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-16 16:47
 **/
public interface IPictureService extends IService<Picture> {
    /**
     * 分页查询
     * @param criteria
     * @param pageable
     * @return
     */
    PageResult queryAll(PictureQueryCriteria criteria, Pageable pageable);

    /**
     * 不分页查询
     * @param criteria
     * @return
     */
    List<Picture> queryAll(PictureQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll
     * @param response
     */
    void download(List<Picture> queryAll, HttpServletResponse response);

    /**
     * 上传图片
     * @param file
     * @param userName
     * @return
     */
    Picture upload(MultipartFile file, String userName);

    /**
     * 同步数据
     */
    void synchronize();

    /**
     * 删除数据
     * @param ids
     */
    void deleteAll(Set<Long> ids);
}
