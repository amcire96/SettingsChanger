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
public class EndReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MINE","End Receiver!!");

        Bundle settings = intent.getExtras();
        int volume = settings.getInt("currentVolume");
        boolean isWifiOn = settings.getBoolean("currentWifi");
        boolean isBluetoothOn = settings.getBoolean("currentBluetooth");

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_RING,volume,0);


        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean currentWifiOn = wifiManager.isWifiEnabled();

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


        BluetoothAdapter bluetoothAdapter = (BluetoothAdapter.getDefaultAdapter());
        boolean currentBluetoothOn = bluetoothAdapter.isEnabled();

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



        //not reliable!
//        Log.i("MINE","Wifi restored to: " + wifiManager.isWifiEnabled());
//        Log.i("MINE","Bluetooth restored to: " + bluetoothAdapter.isEnabled());
    }


}
