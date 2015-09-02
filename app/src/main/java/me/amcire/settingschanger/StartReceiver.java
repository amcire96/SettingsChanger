package me.amcire.settingschanger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Eric on 8/30/2015.
 */
public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i("MINE", "Start Receiver!!");
        Bundle settings = intent.getExtras();
        SettingsItem item = settings.getParcelable("settings");

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean currentWifiOn = wifiManager.isWifiEnabled();

        BluetoothAdapter bluetoothAdapter = (BluetoothAdapter.getDefaultAdapter());
        boolean currentBluetoothOn = bluetoothAdapter.isEnabled();

        //compareTo is impl as difference
        int endStartDiffInMins = item.getEndTime().compareTo(item.getStartTime());
        int endStartDiffInMil = endStartDiffInMins * 60 * 1000;

        //start endreceiver which has to come after startreceiver
        Intent endIntent = new Intent(context,EndReceiver.class);
        endIntent.setType(""+item.getId());
        endIntent.putExtra("currentVolume",currentVolume);
        endIntent.putExtra("currentWifi",currentWifiOn);
        endIntent.putExtra("currentBluetooth",currentBluetoothOn);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, endIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+endStartDiffInMil, 1000 * 60 * 60 * 24 * 7, alarmIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+endStartDiffInMil, alarmIntent);

        //creates exact repeating alarm feature
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000 * 60 * 60 * 24 * 7, PendingIntent.getBroadcast(context,0,intent,0));

        //repeats every 2 mins instead of a week for TEST purposes
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000 * 60 * 2, PendingIntent.getBroadcast(context,0,intent,0));


        int volume = item.getVolume();

        audioManager.setStreamVolume(AudioManager.STREAM_RING,(int) (volume/100.0 * audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)),0);



        boolean isWifiOn = item.isWifiOn();
//        Log.i("MINE", "Wifi is supposed to be on receiver: " + isWifiOn);
        if(isWifiOn){
            if(!currentWifiOn){
                wifiManager.setWifiEnabled(true);
            }
        }
        else if(!isWifiOn){
            if(currentWifiOn){
                wifiManager.setWifiEnabled(false);
            }
        }


        boolean isBluetoothOn = item.isBluetoothOn();
//        Log.i("MINE","Bluetooth is supposed to be on receiver: " + isBluetoothOn);
        if(isBluetoothOn){
            if(!currentBluetoothOn){
                bluetoothAdapter.enable();
            }
        }
        else if(!isBluetoothOn){
            if(currentBluetoothOn){
                bluetoothAdapter.disable();
            }
        }

        //not accurate because it takes a while for wifi and bluetooth to actually be turned off
//        Log.i("MINE","Wifi set to: " + wifiManager.isWifiEnabled());
//        Log.i("MINE", "Bluetooth set to: " + bluetoothAdapter.isEnabled());


    }
}
