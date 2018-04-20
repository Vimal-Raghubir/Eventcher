package com.yuyakaido.android.cardstackview.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.yuyakaido.android.cardstackview.sample.SettingsActivity.PREFS_NAME;

public class EventDetailsActivity extends AppCompatActivity {
    String activityTheme;
    Boolean name, date, description;
    String[] eventarray;
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
       // if (name) {
            output = "<b>Name: </b> " + event.getName() + "<br/><br/>";
      //  }
        if (description) {
            output += "<b>Description:</b><br/> " + event.getLongDescription() + "<br/><br/>";
        } else {
            output += "<b>Description:</b><br/> " + event.getShortDescription() + "<br/><br/>";
        }
            output += "<b>Date: </b> " + event.getDate() + "<br/><br/>";
            output += "<b>Location: </b> " + event.getLocation() + "<br/><br/>";


        textview.setText(Html.fromHtml(output));
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        ImageView imageView = (ImageView) findViewById(R.id.cover);
        Glide.with(this).load(event.getCoverURL()).into(imageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Button bookmark = (Button) findViewById(R.id.bookmark);
        final Button rem_bm = (Button) findViewById(R.id.unbookmark);

        FileInputStream fis = null;
        try {
            fis = openFileInput("bookmarkEvents.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fis != null) {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                //Log.d("tempString", tempstring);
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Converting string to an array of events
            String tempstring = sb.toString();
            eventarray = tempstring.split(";;;");
           // Log.d("eventarray", eventarray[1]);

            for (int i = 0; i < eventarray.length; i++) {
                if (eventarray[i].contains(event.getName())) {
                    bookmark.setVisibility(View.GONE);
                    rem_bm.setVisibility(View.VISIBLE);
                }
            }
        }

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = "bookmarkEvents.txt";
                FileOutputStream outputStream;
                String eventdetails = event.getName() + "]]]" + event.getLongDescription() + "]]]" + event.getCoverURL() + "]]]" + event.getDate() + "]]]" + event.getLocation() + ";;;";
                try {
                    outputStream = openFileOutput(filename, Context.MODE_APPEND);
                    outputStream.write(eventdetails.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bookmark.setVisibility(View.GONE);
                rem_bm.setVisibility(View.VISIBLE);
                Toast.makeText(EventDetailsActivity.this, "This event has been added to your bookmarks", Toast.LENGTH_SHORT).show();
            }
        });

        rem_bm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String filename = "bookmarkEvents.txt";

                FileOutputStream outputStream;
                String lineToRemove = event.getName() + "]]]" + event.getLongDescription() + "]]]" + event.getCoverURL() + "]]]" + event.getDate() + "]]]" + event.getLocation();
                Log.d("eventarray", lineToRemove);
                String currentLine;
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < eventarray.length; i++) {
                    // trim newline when comparing with lineToRemove
                    Log.d("eventarray", eventarray[i]);
                    if (!eventarray[i].contains(lineToRemove)) {//eventarray[i].equals(lineToRemove)) {
                        try {
                            outputStream = openFileOutput(filename, Context.MODE_APPEND);
                            String temp = eventarray[i] + ";;;";
                            Log.d("temp ", temp);
                            outputStream.write(temp.getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                rem_bm.setVisibility(View.GONE);
                bookmark.setVisibility(View.VISIBLE);
                Toast.makeText(EventDetailsActivity.this, "This event has been removed from your bookmarks", Toast.LENGTH_SHORT).show();
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

        Intent i = new Intent(this, MainActivity.class);
// set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


}
