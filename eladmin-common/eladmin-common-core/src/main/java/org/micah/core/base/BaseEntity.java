package org.micah.core.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-05 15:23
 **/
@Setter
@Getter
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -8295488050684261527L;



    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;


    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;


    @ApiModelProperty(value = "创建时间", hidden = true)
    private Timestamp createTime;


    @ApiModelProperty(value = "更新时间", hidden = true)
    private Timestamp updateTime;


    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }
    /* 分组校验 */
    public @interface Create {}

    /* 分组校验 */
    public @interface Update {}
}
