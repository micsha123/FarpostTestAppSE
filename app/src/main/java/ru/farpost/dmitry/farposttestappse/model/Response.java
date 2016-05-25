package ru.farpost.dmitry.farposttestappse.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Response implements Parcelable {

    public ArrayList<Repository> items;

    public Response(ArrayList<Repository> items) {
        this.items = items;
    }

    protected Response(Parcel in) {
        items = in.readArrayList(getClass().getClassLoader());
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(items.toArray());
    }

}
