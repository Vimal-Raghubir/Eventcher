package com.yuyakaido.android.cardstackview.sample;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "SettingsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Adding comments to clarify what changed from yesterday
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final String themeChoice = settings.getString("theme", "Daylight");        //This part is for changing theme before the settings page is loaded
        if (themeChoice.equals("Daylight")) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final String dateRange = settings.getString("dateRange", "One Month");       //used for the dateRange dropdown menu along with default value
        final int locationDistance = settings.getInt("locationDistance", 50);
        boolean nameChecked = settings.getBoolean("nameChecked", false);            //for the checkboxes and default is false for now
        boolean dateChecked = settings.getBoolean("dateChecked", false);
        boolean descChecked = settings.getBoolean("descChecked", false);

        final SharedPreferences.Editor editor = settings.edit();

        final Spinner date_range = (Spinner) findViewById(R.id.date_range_spinner);

        final CheckBox name = (CheckBox) findViewById(R.id.name_box);                           //Reference to the checkbox and if it was checked before then check it again when settings is loaded
        if (nameChecked) {
            name.setChecked(true);
        }

        final CheckBox date = (CheckBox) findViewById(R.id.date_box);
        if (dateChecked) {
            date.setChecked(true);
        }

        final CheckBox desc = (CheckBox) findViewById(R.id.description_box);
        if (descChecked) {
            desc.setChecked(true);
        }

        int position = 0;
        switch (dateRange) {                                                                    //Used to set the dateRange dropdown menu with previous selection saved
            case "One Month":
                position = 0;
                break;
            case "Two Months":
                position = 1;
                break;
            case "Three Months":
                position = 2;
                break;
            /*case "One Month":
                position = 3;
                break;*/
        }

        date_range.setSelection(position);

        final Spinner theme = (Spinner) findViewById(R.id.theme_spinner);
        theme.setSelection((themeChoice.equals("Daylight") ? 0 : 1));
        final SeekBar location_range = (SeekBar) findViewById(R.id.location_spinner);
        final TextView progress = (TextView) findViewById(R.id.spinner_value);

        location_range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("SEEK BAR", "in progress");
                progress.setText(String.valueOf(i));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        location_range.setProgress(locationDistance);

        Button changes = (Button) findViewById(R.id.apply_settings);

        changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("theme", theme.getSelectedItem().toString());
                editor.putInt("locationDistance", location_range.getProgress());
                editor.putString("dateRange", date_range.getSelectedItem().toString());
                editor.putBoolean("nameChecked", name.isChecked());                                 //To store the checkboxes checked/unchecked state
                editor.putBoolean("dateChecked", date.isChecked());
                editor.putBoolean("descChecked", desc.isChecked());

                if (theme.getSelectedItem().toString().equals("Daylight")) {
                    setTheme(R.style.Theme_AppCompat_Light);
                } else {
                    setTheme(R.style.Theme_AppCompat);
                }
                recreate();
                editor.commit();
            }
        });

        Button reset = (Button) findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("theme", "Light");
                editor.putInt("locationDistance", 50);
                editor.putString("dateRange", "One Month");
                editor.putBoolean("nameChecked", false);                                            //Set it to default values
                editor.putBoolean("dateChecked", false);
                editor.putBoolean("descChecked", false);

                name.setChecked(false);
                date.setChecked(false);
                desc.setChecked(false);

                date_range.setSelection(0);
                theme.setSelection(0);
                location_range.setProgress(50);
                setTheme(R.style.Theme_AppCompat_Light);
                recreate();
                editor.commit();
            }
        });
    }
}
