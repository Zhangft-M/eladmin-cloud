package org.micah.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.micah.core.base.BaseEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: eladmin-cloud
 * @description: 字典详情
 * @author: Micah
 * @create: 2020-08-05 21:07
 **/
@Getter
@Setter
@TableName("sys_dict_detail")
public class DictDetail extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7493444951310070323L;

    @TableId(value = "detail_id",type = IdType.AUTO)
    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ApiModelProperty(value = "字典标签")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "排序")
    private Integer dictSort = 999;


    @ApiModelProperty(value = "字典", hidden = true)
    @TableField(exist = false)
    private Dict dict;
}
