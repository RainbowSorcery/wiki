package com.lyra.wiki.entity.vo;

public class PageRequestVO {
    private Integer current;
    private Integer pageSize;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageRequestVO{" +
                "current=" + current +
                ", pageSize=" + pageSize +
                '}';
    }
}
