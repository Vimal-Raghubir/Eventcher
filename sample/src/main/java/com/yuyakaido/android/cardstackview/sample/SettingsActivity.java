package com.yuyakaido.android.cardstackview.sample;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "SettingsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final String themeChoice = settings.getString("theme", "Light");
        if (themeChoice.equals("Light")) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        final int locationDistance = settings.getInt("locationDistance", 50);
        final SharedPreferences.Editor editor = settings.edit();

        final Spinner theme = (Spinner) findViewById(R.id.theme_spinner);
        theme.setSelection((themeChoice.equals("Light") ? 0 : 1));
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
                if (theme.getSelectedItem().toString().equals("Light")) {
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
                theme.setSelection(0);
                location_range.setProgress(50);
                setTheme(R.style.Theme_AppCompat_Light);
                recreate();
                editor.commit();
            }
        });
    }
}
