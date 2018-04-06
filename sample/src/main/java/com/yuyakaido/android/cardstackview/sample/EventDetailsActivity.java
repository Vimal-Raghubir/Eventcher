package com.yuyakaido.android.cardstackview.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.yuyakaido.android.cardstackview.sample.SettingsActivity.PREFS_NAME;

public class EventDetailsActivity extends AppCompatActivity {
    String activityTheme;
    Boolean name, date, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);                      //Used for rendering the app theme before page is created
        activityTheme = settings.getString("theme", "Daylight");
        name = settings.getBoolean("nameChecked", true);
        date = settings.getBoolean("dateChecked", true);
        description = settings.getBoolean("descChecked", true);
        if (activityTheme.equals("Daylight")) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        //final SharedPreferences.Editor editor = settings.edit();

        final Event event = (Event) getIntent().getSerializableExtra("a1");

        TextView textview = (TextView) findViewById(R.id.information);

        String output = "";
        if (name) {
            output = "<b>name: " + event.getName() + " ";
        }
        if (description) {
            output += "<b>description: " + event.getLongDescription() + " ";
        } else {
            output += "<b>description: " + event.getShortDescription() + " ";
        }
        if (date) {
            output += "<b>date: " + event.getStartTime() + " ";
        }

        textview.setText(Html.fromHtml(output));
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        ImageView imageView = (ImageView) findViewById(R.id.cover);
        Glide.with(this).load(event.getCoverURL()).into(imageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button bookmark = (Button) findViewById(R.id.bookmark);

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = "bookmarkEvents.txt";
                FileOutputStream outputStream;
                String eventdetails = event.getName() + "]]]" + event.getLongDescription() + "]]]" + event.getCoverURL() + ";;;";
                try {
                    outputStream = openFileOutput(filename, Context.MODE_APPEND);
                    outputStream.write(eventdetails.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {                                                                     //Used for rendering the app theme
        super.onResume();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String themeChoice = settings.getString("theme", "Daylight");
        Boolean cur_name = settings.getBoolean("nameChecked", true);
        Boolean cur_date = settings.getBoolean("dateChecked", true);
        Boolean cur_description = settings.getBoolean("descChecked", true);
        if (!activityTheme.equals(themeChoice) || cur_name != name || cur_date != date || cur_description != description) {
            if (themeChoice.equals("Daylight")) {
                setTheme(R.style.Theme_AppCompat_Light);
            } else {
                setTheme(R.style.Theme_AppCompat);
            }
            activityTheme = themeChoice;
            name = cur_name;
            date = cur_date;
            description = cur_description;
            recreate();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_main_logout:
                Logout();
                break;
            case R.id.profile:
                startActivity(new Intent(this, SecondActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.search_:
                startActivity(new Intent(this,SearchActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.support:
                startActivity(new Intent(this, SupportActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Logout() {
        LoginManager.getInstance().logOut();

        Intent leave = new Intent(this, MainActivity.class);
        //leave.setAction(Intent.ACTION_MAIN);

        startActivity(leave);
    }
}
