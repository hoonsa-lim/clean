package com.example.clean;

import android.os.Parcel;
import android.os.Parcelable;

public class SpaceData implements Parcelable {
    private byte[] image;       //공간 사진
    private String spaceName;   //공간 이름
    private String toDoName;    //할일 이름
    private String date;        //날짜
    private String time;        //시간
    private String mon;  //반복 요일
    private String tus;  //반복 요일
    private String wen;  //반복 요일
    private String tur;  //반복 요일
    private String fri;  //반복 요일
    private String sat;  //반복 요일
    private String sun;  //반복 요일
    private int alarm;          //알람 여부 : 1이면 알람, 0이면 알람 안함.
    private int clear;          //달성 여부

    //공간 gridView를 위한 생성자
    public SpaceData(byte[] image, String spaceName) {
        this.image = image;
        this.spaceName = spaceName;
    }

    //기본 생성자
    public SpaceData(byte[] image, String spaceName, String toDoName, String date, String time,
                     String mon, String tus, String wen, String tur, String fri, String sat, String sun,
                     int alarm, int clear) {
        this.image = image;
        this.spaceName = spaceName;
        this.toDoName = toDoName;
        this.date = date;
        this.time = time;
        this.mon = mon;
        this.tus = tus;
        this.wen = wen;
        this.tur = tur;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.alarm = alarm;
        this.clear = clear;
    }

    //이미지, 공간명 만 가져오지 않는 생성자 : 공간에 대한 할일목록을 볼 때 이미지와 공간명은 이미 있음
    public SpaceData(String toDoName, String date, String time,
                     String mon, String tus, String wen, String tur, String fri, String sat, String sun,
                     int alarm, int clear) {
        this.toDoName = toDoName;
        this.date = date;
        this.time = time;
        this.mon = mon;
        this.tus = tus;
        this.wen = wen;
        this.tur = tur;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.alarm = alarm;
        this.clear = clear;
    }

    protected SpaceData(Parcel in) {
        image = in.createByteArray();
        spaceName = in.readString();
        toDoName = in.readString();
        date = in.readString();
        time = in.readString();
        mon = in.readString();
        tus = in.readString();
        wen = in.readString();
        tur = in.readString();
        fri = in.readString();
        sat = in.readString();
        sun = in.readString();
        alarm = in.readInt();
        clear = in.readInt();
    }

    public static final Creator<SpaceData> CREATOR = new Creator<SpaceData>() {
        @Override
        public SpaceData createFromParcel(Parcel in) {
            return new SpaceData(in);
        }

        @Override
        public SpaceData[] newArray(int size) {
            return new SpaceData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(image);
        parcel.writeString(spaceName);
        parcel.writeString(toDoName);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(mon);
        parcel.writeString(tus);
        parcel.writeString(wen);
        parcel.writeString(tur);
        parcel.writeString(fri);
        parcel.writeString(sat);
        parcel.writeString(sun);
        parcel.writeInt(alarm);
        parcel.writeInt(clear);
    }

    //getter, setter

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getToDoName() {
        return toDoName;
    }

    public void setToDoName(String toDoName) {
        this.toDoName = toDoName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getTus() {
        return tus;
    }

    public void setTus(String tus) {
        this.tus = tus;
    }

    public String getWen() {
        return wen;
    }

    public void setWen(String wen) {
        this.wen = wen;
    }

    public String getTur() {
        return tur;
    }

    public void setTur(String tur) {
        this.tur = tur;
    }

    public String getFri() {
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public int getClear() {
        return clear;
    }

    public void setClear(int clear) {
        this.clear = clear;
    }
}
