package com.lyra.wiki.entity.vo;

import java.time.LocalDate;

public class SnapshotStatisticVO {
    private Long viewCount;
    public Long viewIncrease;
    private Long voteCount;
    private Long voteIncrease;
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
