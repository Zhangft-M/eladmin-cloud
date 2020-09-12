package org.micah.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName(value = "tool_qiniu_content")
public class QiniuContent implements Serializable {

    private static final long serialVersionUID = -2280163678912593510L;


    @TableId(value = "content_id")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @TableField(value = "name")
    @ApiModelProperty(value = "文件名")
    private String key;

    @ApiModelProperty(value = "空间名")
    private String bucket;

    @ApiModelProperty(value = "大小")
    private String size;

    @ApiModelProperty(value = "文件地址")
    private String url;

    @ApiModelProperty(value = "文件类型")
    private String suffix;

    @ApiModelProperty(value = "空间类型：公开/私有")
    private String type = "公开";

    @ApiModelProperty(value = "创建或更新时间")
    private Timestamp updateTime;
}