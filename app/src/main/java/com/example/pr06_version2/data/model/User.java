package com.example.pr06_version2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name, email, address, web;
    private int id, tlf;
    private Avatar img_Avatar;

    public User(int id, String name, String email, String address, String web, int tlf, Avatar img_Avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.web = web;
        this.tlf = tlf;
        this.img_Avatar = img_Avatar;
    }

    public User(String name, String email, String address, String web, int tlf, Avatar img_Avatar) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.web = web;
        this.tlf = tlf;
        this.img_Avatar = img_Avatar;
    }

    private User(Parcel in) {
        name = in.readString();
        email = in.readString();
        address = in.readString();
        web = in.readString();
        tlf = in.readInt();
        img_Avatar = in.readParcelable(Avatar.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public int getTlf() {
        return tlf;
    }

    public void setTlf(int tlf) {
        this.tlf = tlf;
    }

    public Avatar getImg_Avatar() {
        return img_Avatar;
    }

    public void setImg_Avatar(Avatar img_Avatar) {
        this.img_Avatar = img_Avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(web);
        dest.writeInt(tlf);
        dest.writeParcelable(img_Avatar, flags);
    }
}
