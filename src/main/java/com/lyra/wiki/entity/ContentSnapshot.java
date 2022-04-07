package com.lyra.wiki.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 文档内容
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Schema(name = "ContentSnapshot", description = "文档内容快照")
public class ContentSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文档id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(name = "id", description = "快照id")
    private Long id;

    /**
     * 内容
     */
    @Schema(name = "content", description = "快照内容")
    private String content;

    @Schema(name = "date", description = "创建快照日期")
    private LocalDateTime date;

    @Schema(name = "contentId", description = "文档id")
    @TableField("content_id")
    private Long contentId;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Content{" +
            "id=" + id +
            ", content=" + content +
        "}";
    }
}
