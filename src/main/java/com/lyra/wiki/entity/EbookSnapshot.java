package com.lyra.wiki.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 电子书快照表
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@TableName("ebook_snapshot")
@Schema(name = "EbookSnapshot", description = "电子书快照")
public class EbookSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "EbookSnapshot", description = "电子书快照id")
    private Long id;

    /**
     * 电子书id
     */
    @Schema(name = "ebookId", description = "电子书id")
    private Long ebookId;

    /**
     * 快照日期
     */
    @Schema(name = "date", description = "快照日期")
    private LocalDate date;

    /**
     * 阅读数
     */
    @Schema(name = "viewCount", description = "阅读数")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @Schema(name = "voteCount", description = "点赞数")
    private Integer voteCount;

    /**
     * 阅读增长
     */
    @Schema(name = "viewIncrease", description = "阅读增长")
    private Integer viewIncrease;

    /**
     * 点赞增长
     */
    @Schema(name = "voteIncrease", description = "点赞增长")
    private Integer voteIncrease;

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
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
    public Integer getViewIncrease() {
        return viewIncrease;
    }

    public void setViewIncrease(Integer viewIncrease) {
        this.viewIncrease = viewIncrease;
    }
    public Integer getVoteIncrease() {
        return voteIncrease;
    }

    public void setVoteIncrease(Integer voteIncrease) {
        this.voteIncrease = voteIncrease;
    }

    @Override
    public String toString() {
        return "EbookSnapshot{" +
            "id=" + id +
            ", ebookId=" + ebookId +
            ", date=" + date +
            ", viewCount=" + viewCount +
            ", voteCount=" + voteCount +
            ", viewIncrease=" + viewIncrease +
            ", voteIncrease=" + voteIncrease +
        "}";
    }
}
