package org.micah.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.micah.core.base.BaseDTO;
import org.micah.core.excel.BoolCustomConverter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * 部门传输对象
 * @author micah
 */
@Getter
@Setter
public class DeptDto extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 2452322557782127915L;
    private Long id;

    @ExcelProperty("部门名称")
    private String name;

    @ExcelProperty(value = "部门状态",converter = BoolCustomConverter.class)
    private Boolean enabled;

    @ExcelIgnore
    private Integer deptSort;

    @ExcelIgnore
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDto> children;

    @ExcelIgnore
    private Long pid;


    @ExcelIgnore
    private Integer subCount;

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeptDto deptDto = (DeptDto) o;
        return Objects.equals(id, deptDto.id) &&
                Objects.equals(name, deptDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}