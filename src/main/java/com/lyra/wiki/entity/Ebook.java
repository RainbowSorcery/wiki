package com.lyra.wiki.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * <p>
 * 电子书
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Schema(name = "ebook", description = "电子书")
public class Ebook implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(name = "id", description = "电子书id")
    private Long id;

    /**
     * 名称
     */
    @Schema(name = "name", description = "电子书名称")
    private String name;

    /**
     * 分类1
     */
    @Schema(name = "category1Id", description = "一级分类id")
    private Long category1Id;

    /**
     * 分类2
     */
    @Schema(name = "category2Id", description = "二级分类id")
    private Long category2Id;

    /**
     * 描述
     */
    @Schema(name = "description", description = "电子书简介")
    private String description;

    /**
     * 封面
     */
    @Schema(name = "cover", description = "电子书封面")
    private String cover;

    /**
     * 文档数
     */
    @Schema(name = "docCount", description = "电子书文档数")
    private Integer docCount;

    /**
     * 阅读数
     */
    @Schema(name = "viewCount", description = "电子书浏览数")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @Schema(name = "voteCount", description = "电子书点赞数")
    private Integer voteCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Long getCategory1Id() {
        return category1Id;
    }

    public void setCategory1Id(Long category1Id) {
        this.category1Id = category1Id;
    }
    public Long getCategory2Id() {
        return category2Id;
    }

    public void setCategory2Id(Long category2Id) {
        this.category2Id = category2Id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
    public Integer getDocCount() {
        return docCount;
    }

    public void setDocCount(Integer docCount) {
        this.docCount = docCount;
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

    @Override
    public String toString() {
        return "Ebook{" +
            "id=" + id +
            ", name=" + name +
            ", category1Id=" + category1Id +
            ", category2Id=" + category2Id +
            ", description=" + description +
            ", cover=" + cover +
            ", docCount=" + docCount +
            ", viewCount=" + viewCount +
            ", voteCount=" + voteCount +
        "}";
    }
}
