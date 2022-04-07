package com.lyra.wiki.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageRequestVO", description = "分页中间类")
public class PageRequestVO {
    @Schema(name = "current", description = "开始页")
    private Integer current;
    @Schema(name = "pageSize", description = "每页条数")
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
