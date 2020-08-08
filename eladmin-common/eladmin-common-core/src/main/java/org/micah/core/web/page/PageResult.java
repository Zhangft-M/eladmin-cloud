package org.micah.core.web.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: 分页结果集
 * @author: Micah
 * @create: 2020-07-30 15:11
 **/
@Data
public class PageResult implements Serializable {

    private static final long serialVersionUID = 4114899885264731527L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总的页数
     */
    private Long pages;

    /**
     * 数据
     */
    private List<?> content;


    public PageResult() {
    }

    public PageResult(Long total, List<?> content) {
        this.total = total;
        this.content = content;
    }

    public PageResult(Long total, Long pages, List<?> content) {
        this.total = total;
        this.pages = pages;
        this.content = content;
    }

    public static PageResult success(Long total, Long pages, List<?> content){
        return new PageResult(total,pages,content);
    }

    public static PageResult success(Long total, List<?> content){
        return new PageResult(total,content);
    }
}
