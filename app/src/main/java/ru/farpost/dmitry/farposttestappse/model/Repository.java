package ru.farpost.dmitry.farposttestappse.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Repository implements Parcelable {

    public String full_name;
    public String description;
    public Owner owner;

    public Repository(String full_name, String description, Owner owner) {
        this.full_name = full_name;
        this.description = description;
        this.owner = owner;
    }

    protected Repository(Parcel in) {
        full_name = in.readString();
        description = in.readString();
        owner = in.readParcelable(getClass().getClassLoader());
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(full_name);
        dest.writeString(description);
        dest.writeParcelable(owner, flags);
    }
}
