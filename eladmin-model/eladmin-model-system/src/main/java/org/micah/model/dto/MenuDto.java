/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.micah.model.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import org.micah.core.base.BaseDTO;
import org.micah.core.excel.BoolCustomConverter;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Zheng Jie
 * @date 2018-12-17
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
public class MenuDto extends BaseDTO implements Serializable {

    private static final long serialVersionUID = -8332704181833371038L;
    private Long id;

    private List<MenuDto> children;

    private Integer type;

    @ExcelProperty("权限")
    private String permission;

    @ExcelProperty("标题")
    private String title;

    private Integer menuSort;

    @ExcelProperty("前端路径")
    private String path;

    @ExcelProperty("组件路径")
    private String component;

    private Long pid;

    private Integer subCount;

    @ExcelProperty(value = "是否为外链",converter = BoolCustomConverter.class)
    private Boolean iFrame;

    @ExcelProperty(value = "是否缓存",converter = BoolCustomConverter.class)
    private Boolean cache;

    @ExcelProperty(value = "是否隐藏",converter = BoolCustomConverter.class)
    private Boolean hidden;

    @ExcelProperty("组件名称")
    private String componentName;

    private String icon;

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuDto menuDto = (MenuDto) o;
        return Objects.equals(id, menuDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
