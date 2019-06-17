package com.huang.base.bean;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBean implements Parcelable {
    private int id;

    private String account;

    private String password;

    private String token;

    private String headview;

    private String name;

    private String mobile;

    private String idCard;

    private int gender;//0为未设置  1为男  2为女

    private long birthday;

    private String province;

    private String city;

    private long buildTime;//创建时间

       @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.account);
        dest.writeString(this.password);
        dest.writeString(this.token);
        dest.writeString(this.headview);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.idCard);
        dest.writeInt(this.gender);
        dest.writeLong(this.birthday);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeLong(this.buildTime);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.id = in.readInt();
        this.account = in.readString();
        this.password = in.readString();
        this.token = in.readString();
        this.headview = in.readString();
        this.name = in.readString();
        this.mobile = in.readString();
        this.idCard = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readLong();
        this.province = in.readString();
        this.city = in.readString();
        this.buildTime = in.readLong();
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
