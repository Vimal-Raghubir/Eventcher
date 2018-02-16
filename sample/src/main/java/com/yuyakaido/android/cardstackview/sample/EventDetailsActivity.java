package com.yuyakaido.android.cardstackview.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import static com.yuyakaido.android.cardstackview.sample.SettingsActivity.PREFS_NAME;

public class EventDetailsActivity extends AppCompatActivity {
    String activityTheme;
    Boolean name, date, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);                      //Used for rendering the app theme before page is created
        activityTheme = settings.getString("theme", "Light");
        name = settings.getBoolean("nameChecked", true);
        date = settings.getBoolean("dateChecked", true);
        description = settings.getBoolean("descChecked", true);
        if (activityTheme.equals("Light")) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Event event = (Event) getIntent().getSerializableExtra("a1");

                TextView textview = (TextView) findViewById(R.id.information);

                String output = "";
                if(name){
                    output = "\nname: " +  event.getName() + " ";
                }
                if(description){
                    output += "\ndescription: " +  event.getLongDescription() + " ";
                }else{
                    output += "\ndescription: " +  event.getShortDescription() + " ";
                }
                if(date){
                    output += "\ndate: " +  event.getStartTime() + " ";
                }

                textview.setText(output);
        ImageView imageView = (ImageView) findViewById(R.id.cover);
        Glide.with(this).load(event.getCoverURL()).into(imageView);
    }

    @Override
    protected void onResume() {                                                                     //Used for rendering the app theme
        super.onResume();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String themeChoice = settings.getString("theme", "Light");
        Boolean cur_name = settings.getBoolean("nameChecked", true);
        Boolean cur_date = settings.getBoolean("dateChecked", true);
        Boolean cur_description = settings.getBoolean("descChecked", true);
        if (!activityTheme.equals(themeChoice) || cur_name != name || cur_date != date || cur_description != description) {
            if (themeChoice.equals("Light")) {
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
            case R.id.settings:
                //send intent to settings page
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.search_:
                startActivity(new Intent(this,SecondActivity.class));
                break;
            /*case R.id.menu_activity_main_reload:
                reload();
                break;
            case R.id.menu_activity_main_add_first:
                addFirst();
                break;
            case R.id.menu_activity_main_add_last:
                addLast();
                break;
            case R.id.menu_activity_main_remove_first:
                removeFirst();
                break;
            case R.id.menu_activity_main_remove_last:
                removeLast();
                break;
            case R.id.menu_activity_main_swipe_left:
                swipeLeft();
                break;
            case R.id.menu_activity_main_swipe_right:
                swipeRight();
                break;
            case R.id.menu_activity_main_reverse:
                reverse();
                break;
        */
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
