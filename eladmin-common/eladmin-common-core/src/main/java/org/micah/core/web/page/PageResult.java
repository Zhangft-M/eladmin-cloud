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
    private Integer pages;

    /**
     * 数据
     */
    private List<?> records;


    public PageResult() {
    }

    public PageResult(Long total, List<?> records) {
        this.total = total;
        this.records = records;
    }

    public PageResult(Long total, Integer pages, List<?> records) {
        this.total = total;
        this.pages = pages;
        this.records = records;
    }

    public static PageResult success(Long total, Integer pages, List<?> records){
        return new PageResult(total,pages,records);
    }

    public static PageResult success(Long total, List<?> records){
        return new PageResult(total,records);
    }
}
