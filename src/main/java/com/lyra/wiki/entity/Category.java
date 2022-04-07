package com.lyra.wiki.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Schema(name = "category", description = "Wiki分类")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(name = "id", description = "分类id")
    private Long id;

    /**
     * 父id
     */
    @Schema(name = "parent", description = "父分类id")
    private Long parent;

    /**
     * 名称
     */
    @Schema(name = "name", description = "分类名称")
    private String name;

    /**
     * 顺序
     */
    @Schema(name = "sort", description = "顺序")
    private Integer sort;

    @TableField(exist = false)
    @Schema(name = "children", description = "子分类")
    private List<Category> children;

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Category{" +
            "id=" + id +
            ", parent=" + parent +
            ", name=" + name +
            ", sort=" + sort +
        "}";
    }
}
