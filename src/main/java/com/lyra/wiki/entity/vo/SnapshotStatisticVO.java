package com.lyra.wiki.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "SnapshotStatisticVO", description = "快照中间类")
public class SnapshotStatisticVO {
    @Schema(name = "viewCount", description = "浏览数")
    private Long viewCount;
    @Schema(name = "viewIncrease", description = "点赞数")
    public Long viewIncrease;
    @Schema(name = "voteCount", description = "浏览增量")
    private Long voteCount;
    @Schema(name = "voteIncrease", description = "点赞增量")
    private Long voteIncrease;
    @Schema(name = "date", description = "创建日期")
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getViewIncrease() {
        return viewIncrease;
    }

    public void setViewIncrease(Long viewIncrease) {
        this.viewIncrease = viewIncrease;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Long getVoteIncrease() {
        return voteIncrease;
    }

    public void setVoteIncrease(Long voteIncrease) {
        this.voteIncrease = voteIncrease;
    }
}
