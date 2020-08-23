package org.micah.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.micah.core.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: 字典实体类
 * @author: Micah
 * @create: 2020-08-05 20:56
 **/
@Setter
@Getter
@TableName("sys_dict")
public class Dict extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7585962169646455990L;

    @TableId(value = "dict_id",type = IdType.AUTO)
    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @TableField(exist = false)
    private List<DictDetail> dictDetails;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;
}
