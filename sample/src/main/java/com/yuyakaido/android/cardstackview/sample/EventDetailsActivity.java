package com.yuyakaido.android.cardstackview.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.yuyakaido.android.cardstackview.sample.SettingsActivity.PREFS_NAME;

public class EventDetailsActivity extends AppCompatActivity {
    String activityTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        activityTheme = settings.getString("theme", "Light");
        if (activityTheme.equals("Light")) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Event event = (Event) getIntent().getSerializableExtra("a1");

                TextView textview = (TextView) findViewById(R.id.information);
                textview.setText(event.toString());
        ImageView imageView = (ImageView) findViewById(R.id.cover);
        Glide.with(this).load(event.getCoverURL()).into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String themeChoice = settings.getString("theme", "Light");
        if (!activityTheme.equals(themeChoice)) {
            if (themeChoice.equals("Light")) {
                setTheme(R.style.Theme_AppCompat_Light);
            } else {
                setTheme(R.style.Theme_AppCompat);
            }
            activityTheme = themeChoice;
            recreate();
        }
    }
}
