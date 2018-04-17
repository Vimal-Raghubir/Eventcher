package com.yuyakaido.android.cardstackview.sample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.StringPrepParseException;
import android.support.v4.app.*;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ServiceWorkerController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.location.*;
import android.widget.Toast;
import android.app.DialogFragment;

import com.facebook.CallbackManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.yuyakaido.android.cardstackview.sample.SettingsActivity.PREFS_NAME;

public class SecondActivity extends AppCompatActivity {
    ProfileTracker profileTracker;
    String activityTheme;

    int id = 0;

    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);                       //Used for rendering the app theme before page is created
        activityTheme = settings.getString("theme", "Daylight");
        if (activityTheme.equals("Daylight")) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };

        TextView text = (TextView) findViewById(R.id.textView);
        Profile profile = Profile.getCurrentProfile();

        //logging in after logging out does something to the current profile and returns null
        if(profile != null) {
            ProfilePictureView profileImage = (ProfilePictureView) findViewById(R.id.profilePicture);
            profileImage.setProfileId(profile.getId());
            text.setText(profile.getFirstName() + " " + profile.getLastName());
        }
       // final Spinner eventList = (Spinner) findViewById(R.id.EventList);
        final ArrayList<Event> bookmarkedEvents = new ArrayList<Event>();
        //Reading from file
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
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Converting string to an array of events
            String tempstring = sb.toString();
            Log.d("tempString", tempstring);
            String[] eventarray = tempstring.split(";;;");

            //Extracts names of the events in for loop below
            ArrayList<String> eventnames = new ArrayList<String>();

            for (int i = 0; i < eventarray.length; i++) {
                String[] temp = eventarray[i].split("]]]");
                Event event = new Event();
                event.setName(temp[0]);
                event.setDescription(temp[1]);
                //event.setStart_time(new Date(temp[2]));
                //event.setEnd_time(new Date(temp[3]));
                event.setSource(temp[2]);
                bookmarkedEvents.add(event);

                eventnames.add(temp[0]);
            }

            LinearLayout ll_= (LinearLayout) findViewById(R.id.layoutb);

            for(int i = 0; i < eventnames.size(); i++){
                LinearLayout frame = new LinearLayout(SecondActivity.this);
                frame.setOrientation(LinearLayout.HORIZONTAL);
               // TextView e_name = new TextView(SecondActivity.this);
               // e_name.setLayoutParams(new LinearLayout.LayoutParams(650,ViewGroup.LayoutParams.MATCH_PARENT));
               // e_name.setTextSize(16);

                Button e_button = new Button(SecondActivity.this);
                e_button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
              //  e_name.setText(eventnames.get(i));
                e_button.setText(eventnames.get(i));
                //e_button.   (800005);
                final int temp = i;
                e_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Pass intent to eventdetails page with event object
                        Intent eventIntent = new Intent(getBaseContext().getApplicationContext(), EventDetailsActivity.class);
                        eventIntent.putExtra("a1", bookmarkedEvents.get(temp));
                        startActivity(eventIntent);

                    }
                });
               // frame.addView(e_name);
                frame.addView(e_button);
                frame.setHorizontalGravity(10);//800005
                if(i % 2 == 0){
             //       frame.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
                }
                else{
              //      frame.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                }
                ll_.addView(frame);
            }

            //Sets spinner with the event names only
           // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventnames);
            //eventList.setAdapter(spinnerArrayAdapter);
        }

        Button go_to = (Button) findViewById(R.id.goto_search);
        go_to.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this,SearchActivity.class));
            }
        });

        /*/Sets button listener to extract user choice when selecting spinner option
        Button bookmark = (Button) findViewById(R.id.ViewBookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get string of selected choice
                String text = eventList.getSelectedItem().toString();
                if (text != null) {
                    Event eventObject = new Event();
                    Log.d("View Bookmark", text);
                    //Loop through the events array and check the names of each event and compares it to passed in text
                    for (int i = 0; i < bookmarkedEvents.size(); i++) {
                        if (bookmarkedEvents.get(i).getName() == text) {
                            Log.d("matchedObject", text);
                            eventObject = bookmarkedEvents.get(i);
                            break;
                        }
                    }
                        //Pass intent to eventdetails page with event object
                        Intent eventIntent = new Intent(getBaseContext().getApplicationContext(), EventDetailsActivity.class);
                        eventIntent.putExtra("a1", eventObject);
                        startActivity(eventIntent);
                }
            }
            });  */
    }

    @Override
    protected void onResume() {                                                                     //Used for rendering the app theme
        super.onResume();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String themeChoice = settings.getString("theme", "Daylight");
        if (!activityTheme.equals(themeChoice)) {
            if (themeChoice.equals("Daylight")) {
                setTheme(R.style.Theme_AppCompat_Light);
            } else {
                setTheme(R.style.Theme_AppCompat);
            }
            activityTheme = themeChoice;
            recreate();
        }
    }



    private void startActivity() {
        LoginManager.getInstance().logOut();

        Intent leave = new Intent(this, MainActivity.class);
        //leave.setAction(Intent.ACTION_MAIN);

        startActivity(leave);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private void Logout() {
        LoginManager.getInstance().logOut();

        Intent leave = new Intent(this, MainActivity.class);
        //leave.setAction(Intent.ACTION_MAIN);

        startActivity(leave);
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
}
