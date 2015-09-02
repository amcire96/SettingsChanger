package me.amcire.settingschanger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Eric on 8/29/2015.
 */
public class SettingsItem implements Parcelable {
    private boolean isWifiOn;
    private boolean isBluetoothOn;
    private String daysOfWeekString;
    private String[] daysOfWeekList;
    private Time startTime;
    private Time endTime;
    private View implView;
    private boolean isSelected;
    private boolean isEditable;
    private int id;
    private int volume;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public boolean isEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public View getImplView() {
        return implView;
    }

    public void setImplView(View implView) {
        this.implView = implView;
    }

    private Context mContext;


    public SettingsItem(SettingsItem item, Context c){
        this.isWifiOn = item.isWifiOn();
        this.isBluetoothOn = item.isBluetoothOn();
        this.daysOfWeekString = item.getDaysOfWeekString();
        this.daysOfWeekList = item.getDaysOfWeekList();
        this.startTime = item.getStartTime();
        this.endTime = item.getEndTime();
        this.volume = item.getVolume();
        this.id = (int) (Math.random() * 100000);

        this.mContext = c;

    }


    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public SettingsItem(Bundle settings, Context context){
        mContext = context;

        isWifiOn = settings.getBoolean("wifi");
        isBluetoothOn = settings.getBoolean("bluetooth");

        daysOfWeekString = settings.getString("daysOfWeek");
//        Log.i("MINE","dayString: " + daysOfWeekString);
        daysOfWeekList = daysOfWeekString.replaceAll(" ","").split(",");

//        for(String s : daysOfWeekList){
//            Log.i("MINE","day: "+s);
//        }


        startTime = settings.getParcelable("startTime");
        endTime = settings.getParcelable("endTime");
        volume = settings.getInt("volume");

    }

    public boolean isWifiOn() {
        return isWifiOn;
    }

    public void setIsWifiOn(boolean isWifiOn) {
        this.isWifiOn = isWifiOn;
    }

    public boolean isBluetoothOn() {
        return isBluetoothOn;
    }

    public void setIsBluetoothOn(boolean isBluetoothOn) {
        this.isBluetoothOn = isBluetoothOn;
    }

    public String getDaysOfWeekString() {
        return daysOfWeekString;
    }

    public void setDaysOfWeekString(String daysOfWeekString) {
        this.daysOfWeekString = daysOfWeekString;
    }

    public String[] getDaysOfWeekList() {
        return daysOfWeekList;
    }

    public void setDaysOfWeekList(String[] daysOfWeekList) {
        this.daysOfWeekList = daysOfWeekList;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public boolean overlaps(SettingsItem other){
        String[] otherList = other.getDaysOfWeekList();
        for(String day : this.daysOfWeekList){

            for(int i = 0; i <= Math.min(DaysUtil.getValue(day), otherList.length - 1);i++){
                Log.i("MINE","Checking " + day + " and " + otherList[i]);
                if(otherList[i].equalsIgnoreCase(day)){
                    //days overlap
                    if(this.startTime.inBetween(other.getStartTime(),other.getEndTime())
                            || this.endTime.inBetween(other.getStartTime(),other.getEndTime())
                            || other.getStartTime().inBetween(this.startTime,this.endTime)
                            || other.getEndTime().inBetween(this.startTime,this.endTime)){
                        return true;
                    };
                }
            }
        }
        return false;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void stopAlarm(){
        Log.i("MINE", "alarm cancelling");
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent endIntent = new Intent(mContext,EndReceiver.class);
        endIntent.setType("" + id);

        PendingIntent alarmEndIntent = PendingIntent.getBroadcast(mContext, 0, endIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmEndIntent);

        Intent intent = new Intent(mContext, StartReceiver.class);
        intent.putExtra("settings", this);
        intent.setType(""+id);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
    }

    public void startAlarm(){
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);


        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, this.getStartTime().getHour());
        calendar.set(Calendar.MINUTE, this.getStartTime().getMin());
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        String[] dayList = this.getDaysOfWeekList();

        for(String day : dayList) {
            if (calendar.get(Calendar.DAY_OF_WEEK) != DaysUtil.convertToCalDay(day)) {
                calendar.set(Calendar.DAY_OF_WEEK, DaysUtil.convertToCalDay(day));
            }

            Intent intent = new Intent(mContext, StartReceiver.class);
            intent.setType(""+id);
            intent.putExtra("settings", this);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

            Log.i("MINE", "Date: " + calendar.get(Calendar.DAY_OF_MONTH));
            Log.i("MINE", "Time: " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));

            Log.i("MINE", "difference is " + (calendar.getTimeInMillis() - System.currentTimeMillis()));

//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 * 7, alarmIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    }


    protected SettingsItem(Parcel in) {
        isWifiOn = in.readByte() != 0x00;
        isBluetoothOn = in.readByte() != 0x00;
        daysOfWeekList = in.createStringArray();
        daysOfWeekString = in.readString();
        startTime = (Time) in.readValue(Time.class.getClassLoader());
        endTime = (Time) in.readValue(Time.class.getClassLoader());
        volume = in.readInt();
//        mContext = (Context) in.readValue(Context.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isWifiOn ? 0x01 : 0x00));
        dest.writeByte((byte) (isBluetoothOn ? 0x01 : 0x00));
        dest.writeStringArray(daysOfWeekList);
        dest.writeString(daysOfWeekString);
        dest.writeValue(startTime);
        dest.writeValue(endTime);
        dest.writeInt(volume);
//        dest.writeValue(mContext);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SettingsItem> CREATOR = new Parcelable.Creator<SettingsItem>() {
        @Override
        public SettingsItem createFromParcel(Parcel in) {
            return new SettingsItem(in);
        }

        @Override
        public SettingsItem[] newArray(int size) {
            return new SettingsItem[size];
        }
    };
}