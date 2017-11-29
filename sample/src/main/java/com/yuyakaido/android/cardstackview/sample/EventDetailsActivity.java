package com.yuyakaido.android.cardstackview.sample;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Event event = (Event) getIntent().getSerializableExtra("a1");

                TextView textview = (TextView) findViewById(R.id.information);
                textview.setText(event.toString());
        ImageView imageView = (ImageView) findViewById(R.id.cover);
        Glide.with(this).load(event.getCoverURL()).into(imageView);
    }
}
