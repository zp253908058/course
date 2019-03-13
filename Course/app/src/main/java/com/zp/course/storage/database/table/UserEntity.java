package com.zp.course.storage.database.table;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see UserEntity
 * @since 2019/3/6
 */
@Entity(tableName = "user")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    private String nickname;            //昵称
    @ColumnInfo(name = "real_name")
    private String realName;            //实名
    @ColumnInfo(name = "student_id")
    private String number;          // 学号
    @ColumnInfo
    private int sex;                //性别  1男 2女
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;     //手机号
    @ColumnInfo(name = "account_id")
    private long accountId;

    public UserEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
