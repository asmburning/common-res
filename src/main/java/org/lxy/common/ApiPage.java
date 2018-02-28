package org.lxy.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class ApiPage<T> implements Serializable {

    /**
     * 总页数
     */
    private int totalPages;
    /**
     * 总条数
     */
    private long totalElements;
    /**
     * 第几页
     */
    private int pageNo;
    /**
     * 每页大小
     */
    private int pageSize = 10;
    /**
     * 结果集
     */
    private List<T> result = Collections.emptyList();
}
