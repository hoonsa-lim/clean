package com.example.clean;

import android.os.Parcel;
import android.os.Parcelable;

public class TodayListData implements Parcelable {
    private String pk_fullName;     //pk 공간, 할일이름, 날짜 시간 조함함
    private String t_today;         //오늘 날짜
    private String t_spaceName;     //공간 이름
    private String t_toDoName;    //할일 이름
    private String t_date;        //날짜
    private String t_week;        //요일
    private String t_time;         //시간
    private int t_alarm;          //알람 여부 : 1이면 알람, 0이면 알람 안함.
    private int t_clear;          //달성 여부
    private byte[] t_clearImage;  //달성 인증 사진

    public TodayListData(String pk_fullName, String t_today, String t_spaceName, String t_toDoName, String t_date, String t_week, String t_time, int t_alarm, int t_clear, byte[] t_clearImage) {
        this.pk_fullName = pk_fullName;
        this.t_today = t_today;
        this.t_spaceName = t_spaceName;
        this.t_toDoName = t_toDoName;
        this.t_date = t_date;
        this.t_week = t_week;
        this.t_time = t_time;
        this.t_alarm = t_alarm;
        this.t_clear = t_clear;
        this.t_clearImage = t_clearImage;
    }

    protected TodayListData(Parcel in) {
        pk_fullName = in.readString();
        t_today = in.readString();
        t_spaceName = in.readString();
        t_toDoName = in.readString();
        t_date = in.readString();
        t_week = in.readString();
        t_time = in.readString();
        t_alarm = in.readInt();
        t_clear = in.readInt();
        t_clearImage = in.createByteArray();
    }

    public static final Creator<TodayListData> CREATOR = new Creator<TodayListData>() {
        @Override
        public TodayListData createFromParcel(Parcel in) {
            return new TodayListData(in);
        }

        @Override
        public TodayListData[] newArray(int size) {
            return new TodayListData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pk_fullName);
        parcel.writeString(t_today);
        parcel.writeString(t_spaceName);
        parcel.writeString(t_toDoName);
        parcel.writeString(t_date);
        parcel.writeString(t_week);
        parcel.writeString(t_time);
        parcel.writeInt(t_alarm);
        parcel.writeInt(t_clear);
        parcel.writeByteArray(t_clearImage);
    }

    public String getPk_fullName() {
        return pk_fullName;
    }

    public void setPk_fullName(String pk_fullName) {
        this.pk_fullName = pk_fullName;
    }

    public String getT_today() {
        return t_today;
    }

    public void setT_today(String t_today) {
        this.t_today = t_today;
    }

    public String getT_spaceName() {
        return t_spaceName;
    }

    public void setT_spaceName(String t_spaceName) {
        this.t_spaceName = t_spaceName;
    }

    public String getT_toDoName() {
        return t_toDoName;
    }

    public void setT_toDoName(String t_toDoName) {
        this.t_toDoName = t_toDoName;
    }

    public String getT_date() {
        return t_date;
    }

    public void setT_date(String t_date) {
        this.t_date = t_date;
    }

    public String getT_week() {
        return t_week;
    }

    public void setT_week(String t_week) {
        this.t_week = t_week;
    }

    public String getT_time() {
        return t_time;
    }

    public void setT_time(String t_time) {
        this.t_time = t_time;
    }

    public int getT_alarm() {
        return t_alarm;
    }

    public void setT_alarm(int t_alarm) {
        this.t_alarm = t_alarm;
    }

    public int getT_clear() {
        return t_clear;
    }

    public void setT_clear(int t_clear) {
        this.t_clear = t_clear;
    }

    public byte[] getT_clearImage() {
        return t_clearImage;
    }

    public void setT_clearImage(byte[] t_clearImage) {
        this.t_clearImage = t_clearImage;
    }
}
