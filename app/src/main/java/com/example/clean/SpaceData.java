package com.example.clean;

import android.os.Parcel;
import android.os.Parcelable;

public class SpaceData implements Parcelable {
    private byte[] image;       //공간 사진
    private String spaceName;   //공간 이름
    private String toDoName;    //할일 이름
    private String date;        //날짜
    private String time;        //시간
    private String repetition;  //반복 요일 : null이면 반복 x, 요일이 들어가있으면 해당 요일 반복
    private int alarm;          //알람 여부 : 1이면 알람, 0이면 알람 안함.
    private int clear;          //달성 여부

    //공간 gridView를 위한 생성자
    public SpaceData(byte[] image, String spaceName) {
        this.image = image;
        this.spaceName = spaceName;
    }

    //기본 생성자
    public SpaceData(byte[] image, String spaceName, String toDoName, String date, String time, String repetition, int alarm, int clear) {
        this.image = image;
        this.spaceName = spaceName;
        this.toDoName = toDoName;
        this.date = date;
        this.time = time;
        this.repetition = repetition;
        this.alarm = alarm;
        this.clear = clear;
    }

    protected SpaceData(Parcel in) {
        image = in.createByteArray();
        spaceName = in.readString();
        toDoName = in.readString();
        date = in.readString();
        time = in.readString();
        repetition = in.readString();
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
        parcel.writeString(repetition);
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

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
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
