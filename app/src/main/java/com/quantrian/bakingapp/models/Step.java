package com.quantrian.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinnie on 12/27/2017.
 */

public class Step implements Parcelable{
    public int id;
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;

    public Step()
    {}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);

    }

    protected Step(Parcel in){
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();

    }

    //I don't quite get the purpose of the CREATOR
    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    /*public Step(int id, String shtDesc, String desc, String vidURL, String thumbURL){
        this.id = id;
        this.shortDescription = shtDesc;
        this.description = desc;
        this.videoURL = vidURL;
        this.thumbnailURL = thumbURL;
    }*/
}
