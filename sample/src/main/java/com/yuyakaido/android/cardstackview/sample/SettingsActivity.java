package com.yuyakaido.android.cardstackview.sample;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
/*

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String themeName = pref.getString("prefSyncFrequency3", "Theme1");
        if (themeName.equals("Africa")) {
            setTheme(R.style.CardView_Dark);
        } else if (themeName.equals("Colorful Beach")) {
            //Toast.makeText(this, "set theme", Toast.LENGTH_SHORT).show();
            setTheme(R.style.CardView_Light);
        }

*/
        Button changes = (Button) findViewById(R.id.apply_settings);

        changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //tODO: FIX SEEK BAR ERROR
        SeekBar location_range = (SeekBar) findViewById(R.id.location_spinner);
        final TextView progress = (TextView) findViewById(R.id.spinner_value);
        location_range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("SEEK BAR", "in progress");
                progress.setText(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }
}
