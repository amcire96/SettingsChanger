package me.amcire.settingschanger;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eric on 8/29/2015.
 */
public class SettingsAdapter extends BaseAdapter{

    private ArrayList<SettingsItem> mItems = new ArrayList<SettingsItem>();
    private Context mContext;

    public SettingsAdapter(Context context){
        mContext = context;
    }


    public void add(SettingsItem item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    public ArrayList<SettingsItem> getmItems() {
        return mItems;
    }

    public void setmItems(ArrayList<SettingsItem> mItems) {
        this.mItems = mItems;
    }


    public void clear() {
        for(SettingsItem item : mItems){
            item.stopAlarm();
        }
        mItems.clear();
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingsItem item = (SettingsItem) getItem(position);
        View settingsItemView;
        if(convertView == null){
            LayoutInflater layoutInflater = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            settingsItemView = layoutInflater.inflate(R.layout.settings_item,parent,false);

        }
        else{
            settingsItemView = convertView;
        }


        TextView wifi = (TextView) settingsItemView.findViewById(R.id.wifi_text_item);
        TextView bluetooth = (TextView) settingsItemView.findViewById(R.id.bluetooth_text_item);
        TextView daysOfWeek = (TextView) settingsItemView.findViewById(R.id.days_of_week_item);
        TextView startTime = (TextView) settingsItemView.findViewById(R.id.start_time_item);
        TextView endTime = (TextView) settingsItemView.findViewById(R.id.end_time_item);

        wifi.setText(item.isWifiOn() ? "Wifi: On" : "Wifi: Off");
        wifi.setTextColor(Color.BLACK);

        bluetooth.setText(item.isBluetoothOn() ? "Bluetooth: On" : "Bluetooth: Off");
        bluetooth.setTextColor(Color.BLACK);

        daysOfWeek.setText(item.getDaysOfWeekString());
        daysOfWeek.setTextColor(Color.BLACK);

        startTime.setText(item.getStartTime().asString());
        startTime.setTextColor(Color.BLACK);

        endTime.setText(item.getEndTime().asString());
        endTime.setTextColor(Color.BLACK);

        if(!item.isSelected()){
            settingsItemView.setBackground(null);
        }
        if(!item.isEditable()){
            settingsItemView.setOnClickListener(null);
        }


        item.setImplView(settingsItemView);

        return settingsItemView;
    }
}
