package com.example.yusup.bludrive.Util;

import android.os.Parcel;
import android.os.Parcelable;


public class Download implements Parcelable {
    private int progress;
    private int currentFileSize;
    private int totalFileSize;

    public Download() {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(progress);
        parcel.writeInt(currentFileSize);
        parcel.writeInt(totalFileSize);
    }

    public Download(Parcel in) {
        progress = in.readInt();
        currentFileSize = in.readInt();
        totalFileSize = in.readInt();
    }

    public static final Parcelable.Creator<Download> CREATOR = new Parcelable.Creator<Download>(){
        public Download createFromParcel(Parcel in){
            return new Download(in);
        }

        public Download[] newArray(int size){
            return new Download[size];
        }
    };
}
