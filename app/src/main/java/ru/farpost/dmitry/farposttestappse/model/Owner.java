package ru.farpost.dmitry.farposttestappse.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Owner implements Parcelable{

    public String avatar_url;

    public Owner(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    protected Owner(Parcel in) {
        avatar_url = in.readString();
    }

    public static final Creator<Owner> CREATOR = new Creator<Owner>() {
        @Override
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        @Override
        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar_url);
    }
}
