package com.lyra.wiki.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 文档
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Schema(name = "Doc", description = "文档")
public class Doc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Schema(name = "id", description = "文档Id")
    private Long id;

    /**
     * 电子书id
     */
    @Schema(name = "ebookId", description = "电子书id")
    private Long ebookId;

    /**
     * 父id
     */
    @Schema(name = "parent", description = "分类id")
    private Long parent;

    /**
     * 名称
     */
    @Schema(name = "name", description = "文档名称")
    private String name;

    /**
     * 顺序
     */
    @Schema(name = "sort", description = "文档排序")
    private Integer sort;

    /**
     * 阅读数
     */
    @Schema(name = "viewCount", description = "文档阅读数")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @Schema(name = "voteCount", description = "文档点赞数")
    private Integer voteCount;

    @TableField(exist = false)
    @Schema(name = "content", description = "文档内容")
    private String content;

    @TableField(exist = false)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @Schema(name = "children", description = "子文档")
    private List<Doc> children;

    @TableField(exist = false)
    @Schema(name = "disabled", description = "是否被禁用")
    private Boolean disabled;

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", content='" + content + '\'' +
                ", children=" + children +
                ", disabled=" + disabled +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<Doc> getChildren() {
        return children;
    }

    public void setChildren(List<Doc> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }
    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

}
