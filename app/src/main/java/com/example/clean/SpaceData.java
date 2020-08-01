package com.example.clean;

import android.os.Parcel;
import android.os.Parcelable;

public class SpaceData implements Parcelable {
    private byte[] image;
    private String spaceName;
    private String toDoName;
    private String date;
    private String time;
    private int repetition;
    private int alarm;

    //공간 gridView를 위한 생성자
    public SpaceData(byte[] image, String spaceName) {
        this.image = image;
        this.spaceName = spaceName;
    }

    //
    public SpaceData(byte[] image, String spaceName, String toDoName, String date, String time, int repetition, int alarm) {
        this.image = image;
        this.spaceName = spaceName;
        this.toDoName = toDoName;
        this.date = date;
        this.time = time;
        this.repetition = repetition;
        this.alarm = alarm;
    }

    protected SpaceData(Parcel in) {
        image = in.createByteArray();
        spaceName = in.readString();
        toDoName = in.readString();
        date = in.readString();
        time = in.readString();
        repetition = in.readInt();
        alarm = in.readInt();
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
        parcel.writeInt(repetition);
        parcel.writeInt(alarm);
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

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }
}
