package me.amcire.settingschanger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private SettingsAdapter mAdapter;
    final int REQUEST_ID = 978;
    final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setFooterDividersEnabled(true);

        mAdapter = new SettingsAdapter(getApplicationContext());

        View footerView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_footer,null,false);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addSettingsChangeIntent = new Intent(MainActivity.this, AddSettingsChangeActivity.class);

                addSettingsChangeIntent.putExtra("array",mAdapter.getmItems());
                startActivityForResult(addSettingsChangeIntent, REQUEST_ID);
            }
        });

        listView.addFooterView(footerView);
//        setContentView(footerView);
        listView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            mAdapter.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ID && resultCode == RESULT_OK) {
            Bundle settings = data.getExtras();
            SettingsItem item = settings.getParcelable("settingsItem");
            SettingsItem itemWithContext = new SettingsItem(item,getApplicationContext());

            Log.i("MINE", "Wifi is supposed to be on: " + item.isWifiOn());
            Log.i("MINE", "Bluetooth is supposed to be on: " + item.isBluetoothOn());

            mAdapter.add(itemWithContext);
            itemWithContext.startAlarm();

        }


//            View settingsItemView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.settings_item, null, false);

//            TextView wifi = (TextView) settingsItemView.findViewById(R.id.wifi_text_item);
//            TextView bluetooth = (TextView) settingsItemView.findViewById(R.id.bluetooth_text_item);
//            TextView daysOfWeek = (TextView) settingsItemView.findViewById(R.id.days_of_week_item);
//            TextView startTime = (TextView) settingsItemView.findViewById(R.id.start_time_item);
//            TextView endTime = (TextView) settingsItemView.findViewById(R.id.end_time_item);

//            wifi.setText(settings.getString("wifi")=="true" ? "Wifi: On" : "Wifi: Off");
//            bluetooth.setText(settings.getString("bluetooth")=="true" ? "Bluetooth: On" : "Bluetooth: Off");
//            daysOfWeek.setText(settings.getString("daysOfWeek"));
//            startTime.setText(settings.getString("startTime"));
//            endTime.setText(settings.getString("endTime"));

//            Log.i(TAG, "wifi: " + settings.getString("wifi"));
//            Log.i(TAG, "bluetooth: " + settings.getString("bluetooth"));


//            boolean wifiOn = settings.getString("wifi").equalsIgnoreCase("true");
//            boolean bluetoothOn = settings.getString("bluetooth").equalsIgnoreCase("true");
//
//            WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//            if (wifiOn) {
//                Log.i(TAG, "Wifi is turned on");
//                mWifiManager.setWifiEnabled(true);
//            } else {
//                Log.i(TAG, "Wifi is turned off");
//                mWifiManager.setWifiEnabled(false);
//            }
//
//            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            if (bluetoothOn) {
//                Log.i(TAG, "Bluetooth is turned on");
//                mBluetoothAdapter.enable();
//            } else {
//                Log.i(TAG, "Bluetooth is turned off");
//                mBluetoothAdapter.disable();
//            }

    }
}

