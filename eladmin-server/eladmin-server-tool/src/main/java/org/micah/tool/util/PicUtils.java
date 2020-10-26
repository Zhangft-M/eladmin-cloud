package org.micah.tool.util;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class PicUtils {

    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};
    /**
     * 生成文件的路径
     * @param multipartFile
     * @return
     */
    public static String createFilePath(MultipartFile multipartFile) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                IdWorker.getIdStr() + "." +
                StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");
    }

    /**
     * 校验文件的类型是否正确
     * @param originalFilename
     * @return
     */
    public static Boolean checkFileType(String originalFilename) {
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(originalFilename,type)){
                return true;
            }
        }
        return false;
    }
}