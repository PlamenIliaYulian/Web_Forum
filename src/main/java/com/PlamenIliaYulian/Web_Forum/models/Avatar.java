package com.PlamenIliaYulian.Web_Forum.models;

import jakarta.persistence.*;

@Entity
@Table(name = "avatars")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avatar_id")
    private int avatarId;

    @Column(name = "avatar")
    private byte[] avatar;

    public Avatar() {
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

}
