package com.lyra.wiki.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * <p>
 * 文档内容
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Schema(name = "content", description = "文档内容")
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文档id
     */
    @Schema(name = "id", description = "文档id")
    private Long id;

    /**
     * 内容
     */
    @Schema(name = "content", description = "文档内容")
    private String content;

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
