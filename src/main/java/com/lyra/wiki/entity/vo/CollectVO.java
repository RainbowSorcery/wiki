package com.lyra.wiki.entity.vo;

public class CollectVO {
    private Long id;
    private String name;
    private String ebookName;
    private String description;
    private Long ebookId;

    @Override
    public String toString() {
        return "CollectVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ebookName='" + ebookName + '\'' +
                ", description='" + description + '\'' +
                ", ebookId=" + ebookId +
                '}';
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

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

    public String getEbookName() {
        return ebookName;
    }

    public void setEbookName(String ebookName) {
        this.ebookName = ebookName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
