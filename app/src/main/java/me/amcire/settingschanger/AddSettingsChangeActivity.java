package me.amcire.settingschanger;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Eric on 8/25/2015.
 */
public class AddSettingsChangeActivity extends AppCompatActivity{
    private static String dayOfWeekString;
    private static TextView dayOfWeekText;

    private static Time startTime;
    private static String startTimeString;
    private static TextView startTimeText;

    private static Time endTime;
    private static String endTimeString;
    private static TextView endTimeText;

    final static String TAG = "MINE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_settings_change);

        final ArrayList<SettingsItem> settingsItemList = this.getIntent().getParcelableArrayListExtra("array");

        final CheckBox wifiCB = (CheckBox) findViewById(R.id.wifiCB);
        final CheckBox bluetoothCB = (CheckBox) findViewById(R.id.bluetoothCB);
//        final CheckBox airplaneCB = (CheckBox) findViewById(R.id.airplaneCB);
//        final CheckBox locationCB = (CheckBox) findViewById(R.id.locationCB);

        RelativeLayout wifiRelativeLayout = (RelativeLayout) findViewById(R.id.wifi_rel_layout);
        wifiRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiCB.setChecked(!wifiCB.isChecked());
            }
        });

        RelativeLayout bluetoothRelativeLayout = (RelativeLayout) findViewById(R.id.bluetooth_rel_layout);
        bluetoothRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothCB.setChecked(!bluetoothCB.isChecked());
            }
        });



        dayOfWeekText = (TextView) findViewById(R.id.day_of_week_text);
        Button dayOfWeekButton = (Button) findViewById(R.id.day_of_week_button);

        dayOfWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dayOfWeekPicker = new DayOfWeekPickerFragment();
                dayOfWeekPicker.show(getFragmentManager(), "Day Of Week Picker");
            }
        });

        startTimeText = (TextView) findViewById(R.id.start_time_text);
        Button startTimeButton = (Button) findViewById(R.id.start_time_button);

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new StartTimePickerFragment();
                timePicker.show(getFragmentManager(), "Start Time Picker");
            }
        });

        endTimeText = (TextView) findViewById(R.id.end_time_text);
        Button endTimeButton = (Button) findViewById(R.id.end_time_button);

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new EndTimePickerFragment();
                timePicker.show(getFragmentManager(), "End Time Picker");
            }
        });


        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG,""+dayOfWeekText.getText());
//                Log.i(TAG,""+startTimeText.getText());
//                Log.i(TAG,""+endTimeText.getText());
                if((""+dayOfWeekText.getText()).equalsIgnoreCase("No Days Selected")
                        || (""+startTimeText.getText()).equalsIgnoreCase("No Time Selected")
                        || (""+endTimeText.getText()).equalsIgnoreCase("No Time Selected")){
                    Toast.makeText(getApplicationContext(),"Please Complete All Fields", Toast.LENGTH_SHORT).show();
                }

                else if(startTime.compareTo(endTime)>=0){
                    Toast.makeText(getApplicationContext(),"Start Time Has To Be Before End Time", Toast.LENGTH_SHORT).show();
                }

                else {



                    Bundle settings = new Bundle();
                    settings.putBoolean("wifi", wifiCB.isChecked());
                    settings.putBoolean("bluetooth", bluetoothCB.isChecked());
                    settings.putString("daysOfWeek", "" + dayOfWeekText.getText());
                    settings.putParcelable("startTime", startTime);
                    settings.putParcelable("endTime", endTime);

                    SettingsItem item = new SettingsItem(settings, getApplicationContext());


                    for(SettingsItem si : settingsItemList){
                        Log.i(TAG,"Checking for overlap");
                        if(item.overlaps(si)){
                            Log.i(TAG,"Does Overlap");
                            Toast.makeText(getApplication(),"This Overlaps With A Previous Setting: " +
                                    si.getDaysOfWeekString() + " at " + si.getStartTime().asString() +
                                    " to " + si.getEndTime().asString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    //                settings.putString("airplane", "" + airplaneCB.isChecked());
                    //                settings.putString("location", "" + locationCB.isChecked());

//                    item.startAlarm();

                    Intent data = new Intent();
                    data.putExtra("settingsItem",item);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    public static class StartTimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startTime = new Time(hourOfDay,minute);
            startTimeString = startTime.asString();

            startTimeText.setText(startTimeString);
        }
    }

    public static class EndTimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endTime = new Time(hourOfDay,minute);
            endTimeString = endTime.asString();

            endTimeText.setText(endTimeString);
        }
    }

    public static class DayOfWeekPickerFragment extends DialogFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.day_of_week_dialog, container);

            getDialog().setTitle("Set Day Of The Week");


            final CheckBox sunCB = (CheckBox) view.findViewById(R.id.sundayCB);
            final CheckBox monCB = (CheckBox) view.findViewById(R.id.mondayCB);
            final CheckBox tuesCB = (CheckBox) view.findViewById(R.id.tuesdayCB);
            final CheckBox wedCB = (CheckBox) view.findViewById(R.id.wednesdayCB);
            final CheckBox thursCB = (CheckBox) view.findViewById(R.id.thursdayCB);
            final CheckBox friCB = (CheckBox) view.findViewById(R.id.fridayCB);
            final CheckBox satCB = (CheckBox) view.findViewById(R.id.saturdayCB);


            final RelativeLayout sunRelativeLayout = (RelativeLayout) view.findViewById(R.id.sunRL);
            sunRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sunCB.setChecked(!sunCB.isChecked());
                }
            });

            final RelativeLayout monRelativeLayout = (RelativeLayout) view.findViewById(R.id.monRL);
            monRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monCB.setChecked(!monCB.isChecked());
                }
            });

            final RelativeLayout tuesRelativeLayout = (RelativeLayout) view.findViewById(R.id.tuesRL);
            tuesRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tuesCB.setChecked(!tuesCB.isChecked());
                }
            });

            final RelativeLayout wedRelativeLayout = (RelativeLayout) view.findViewById(R.id.wedRL);
            wedRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wedCB.setChecked(!wedCB.isChecked());
                }
            });

            final RelativeLayout thursRelativeLayout = (RelativeLayout) view.findViewById(R.id.thursRL);
            thursRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thursCB.setChecked(!thursCB.isChecked());
                }
            });

            final RelativeLayout friRelativeLayout = (RelativeLayout) view.findViewById(R.id.friRL);
            friRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friCB.setChecked(!friCB.isChecked());
                }
            });

            final RelativeLayout satRelativeLayout = (RelativeLayout) view.findViewById(R.id.satRL);
            satRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    satCB.setChecked(!satCB.isChecked());
                }
            });

            Button submit = (Button) view.findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dayOfWeekString = "";
                    if(sunCB.isChecked()){

                        dayOfWeekString+="Su";
                    }
                    if(monCB.isChecked()){
                        if(dayOfWeekString!=""){
                            dayOfWeekString+=", ";
                        }
                        dayOfWeekString+="M";
                    }
                    if(tuesCB.isChecked()){
                        if(dayOfWeekString!=""){
                            dayOfWeekString+=", ";
                        }
                        dayOfWeekString+="Tu";
                    }
                    if(wedCB.isChecked()){
                        if(dayOfWeekString!=""){
                            dayOfWeekString+=", ";
                        }
                        dayOfWeekString+="W";
                    }
                    if(thursCB.isChecked()){
                        if(dayOfWeekString!=""){
                            dayOfWeekString+=", ";
                        }
                        dayOfWeekString+="Th";
                    }
                    if(friCB.isChecked()){
                        if(dayOfWeekString!=""){
                            dayOfWeekString+=", ";
                        }
                        dayOfWeekString+="F";
                    }
                    if(satCB.isChecked()){
                        if(dayOfWeekString!=""){
                            dayOfWeekString+=", ";
                        }
                        dayOfWeekString+="Sa";
                    }

                    if(dayOfWeekString==""){
                        dayOfWeekString = "No Days Selected";
                    }
                    dayOfWeekText.setText(dayOfWeekString);
                    dismiss();
                }
            });

            return view;
        }
    }
}
