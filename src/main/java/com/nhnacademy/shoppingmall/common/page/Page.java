package com.nhnacademy.shoppingmall.common.page;

import java.util.List;

public class Page<T> {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final List<T> content;

    private final long totalCount;

    public Page(List<T> content, long totalCount) {
        this.content = content;
        this.totalCount = totalCount;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalCount() {
        return totalCount;
    }

}
