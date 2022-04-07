package com.lyra.wiki.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ResetUserPasswordVO", description = "重置密码中间类")
public class ResetUserPasswordVO {
    @Schema(name = "id", description = "用户id")
    private Long id;
    @Schema(name = "oldPassword", description = "旧密码")
    private String oldPassword;
    @Schema(name = "newPassword", description = "新密码")
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
