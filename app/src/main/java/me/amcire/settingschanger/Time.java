package me.amcire.settingschanger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eric on 8/30/2015.
 */
public class Time implements Comparable<Time>, Parcelable {
    private int hour;
    private int min;

    public Time(int hour, int minute){
        this.hour = hour;
        this.min = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String asString(){
        String hourStr = "" + hour;
        String minStr = "" + min;

        if (hour < 10) {
            hourStr = "0" + hourStr;
        }
        if (min < 10){
            minStr = "0" + minStr;
        }
        return  hourStr + ":" + minStr;
    }

    public boolean inBetween(Time start, Time end){
        return this.compareTo(start)>=0 && this.compareTo(end)<=0;
    }


    @Override
    public int compareTo(Time another) {
        return (this.getHour()-another.getHour()) * 60 + (this.getMin()-another.getMin());
    }


    protected Time(Parcel in) {
        hour = in.readInt();
        min = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(hour);
        dest.writeInt(min);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Time> CREATOR = new Parcelable.Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };
}