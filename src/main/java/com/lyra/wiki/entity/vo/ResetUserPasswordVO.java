package com.lyra.wiki.entity.vo;

public class ResetUserPasswordVO {
    private Long id;
    private String oldPassword;
    private String newPassword;

    @Override
    public String toString() {
        return "ResetUserPasswordVO{" +
                "id=" + id +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
