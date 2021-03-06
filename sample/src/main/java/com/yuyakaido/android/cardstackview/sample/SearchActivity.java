package com.yuyakaido.android.cardstackview.sample;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.yuyakaido.android.cardstackview.sample.SettingsActivity.PREFS_NAME;

public class SearchActivity extends AppCompatActivity implements DatePickerFragment.DatePickerDialogListener {
    String activityTheme;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    CallbackManager callbackManager;
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    String startDate;
    String untilDate;
    String end_Date_;
    SharedPreferences settings;
    int id = 0;

    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(PREFS_NAME, 0);
        //Used for rendering the app theme before page is created
        activityTheme = settings.getString("theme", "Daylight");
        if (activityTheme.equals("Daylight")) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        invalidateOptionsMenu();
        events = new ArrayList<Event>();
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };

        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        final Button dateSpinner = (Button) findViewById(R.id.dateSpinner);

        final Button untilDateSpinner = (Button) findViewById(R.id.untilDateSpinner);
        //Continue code to create date functionality for untilDate
        //TODO implement addTextChangedListener to perform untilDateSpinner logic
        untilDateSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String val = untilDateSpinner.getText().toString();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date end_date = dateFormat.parse(val);
                    Date start_ = dateFormat.parse(dateSpinner.getText().toString());
                    if(start_.after(end_date)){
                        Toast.makeText(SearchActivity.this, "Until Date cannot be before the start date", Toast.LENGTH_LONG).show();
                        untilDateSpinner.setText(end_Date_);
                    }else{
                        untilDate = val;
                    }
                }
                catch (Exception e){

                }

            }
        });
        dateSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int newMonth,newYear;
                String[] length = {"31", "28", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31"};
                untilDateSpinner.setEnabled(true);
                String startingDate = dateSpinner.getText().toString();

                Log.d("startingDate", startingDate);
                String dateRange = settings.getString("dateRange", "One Month");
                String[] values = startingDate.split("-");
                newMonth = Integer.parseInt(values[1]);
                newYear = Integer.parseInt(values[0]);

                switch (dateRange) {                                                                    //Used to set the dateRange dropdown menu with previous selection saved
                    case "One Month":
                        if((newMonth  + 1) > 12 ){
                            newMonth = 1;
                            newYear +=1;
                        }else{
                            newMonth += 1;
                        }
                        break;
                    case "Two Months":
                        if((newMonth  + 2) > 12 ){
                            newMonth = (newMonth + 2) > 13 ? 2 : 1;
                            newYear +=1;
                        }else{
                            newMonth += 2;
                        }
                        break;
                    case "Three Months":
                        if((newMonth  + 3) > 12 ){
                            newMonth = ((newMonth + 3) > 13 ? (newMonth +3 > 14 ? 3: 2): 1);
                            newYear +=1;
                        }else{
                            newMonth += 3;
                        }
                        break;
                }

                end_Date_ = Integer.toString(newYear) + "-" + Integer.toString(newMonth) + "-" + length[newMonth-1];
                untilDateSpinner.setText(end_Date_);
            }
        });
        dateSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = 1;
                showDatePickerDialog(view);
            }
        });

        untilDateSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = 2;
                showDatePickerDialog(view);
            }
        });

        Spinner local = (Spinner) findViewById(R.id.location);
        //local.getse

        Button search = (Button) findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (events != null) {
                    events.clear();
                }
                checkValues(dateSpinner);

                //getLocation();
                //findEvents();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Date");
    }

    public void setButtontext(DialogFragment dialog, String l){
        Button spinner;
        Log.d("id", Integer.toString(id));
        if (id == 1) {
            spinner = (Button) findViewById(R.id.dateSpinner);
        } else {
            spinner = (Button) findViewById(R.id.untilDateSpinner);
        }
        //if (dialog)
        spinner.setText(l);
    }
    private void getLocation(){
        String[] spinnerText;
        Double latitude, longitude;
        String[] coordinates;
        //TODO: get spinner input and set location accordingly
        //TODo: change date picker to spinner + dialog and set until date
        Spinner spinner = (Spinner) findViewById(R.id.location);
        int position = spinner.getSelectedItemPosition();
        if (position != -1) {
            if (position == 0) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Log.d("Location", "inside rationale");
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    } else {

                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    }
                } else {
                    Log.d("Location", "calling get places");
                    getPlaces();
                }
            } else {
                coordinates = getResources().getStringArray(R.array.coordinates);
                spinnerText = coordinates[position-1].split(",");
                latitude = Double.parseDouble(spinnerText[0]);
                longitude = Double.parseDouble(spinnerText[1]);
                getPlaces(latitude, longitude, position);
                Log.d("TESTlatitude", latitude.toString());
            }
        }
    }

    private void findEvents() {
        Intent eventIntent = new Intent(this, EventDisplayActivity.class);
        Log.d("TestEvent",Integer.toString(events.size()));
        Log.e("TEST", "FOUR");
        eventIntent.putExtra("eventList", events);
        startActivity(eventIntent);
    }

    private void getPlaces() {
        getPlaces(43.6532, -79.3832, 0);
    }
    private void getPlaces(double latitude,double longitude, int position) {
        Log.e("TEST","ONE");

        final LocationListener locationListener = new LocationListener() {
            double currentLongitude;
            double currentLatitude;

            public void onLocationChanged(Location location) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Latitude","status");
            }
            public void onProviderEnabled(String provider) {
                Log.d("Latitude","enable");
            }
            public void onProviderDisabled(String provider) {
                Log.d("Latitude","disable");
            }
        };

        if (position == 0) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    }
                }
            }
        }
        Log.d("longitude", Double.toString(longitude));
        Log.d("latitude", Double.toString(latitude));
        Log.e("TEST","TWO");

        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/search",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response.getJSONObject() != null){
                            parsePlaces(response);
                            Log.e("TEST","THREE");

                        } else {
                            Toast.makeText(getApplicationContext(), "There was an error when trying to retrieve events.", Toast.LENGTH_LONG);
                        }
                    }
                });

        Bundle param = new Bundle();
        param.putString("type", "place");
        param.putString("center", String.valueOf(latitude) + "," + String.valueOf(longitude) );//String.valueOf(locationManager.getLastKnownLocation(getApplicationContext().LOCATION_SERVICE).getLatitude()) + "," + String.valueOf(locationManager.getLastKnownLocation(getApplicationContext().LOCATION_SERVICE).getLongitude()));
        param.putString("distance","500");
        param.putString("limit", "10");
        param.putString("fields", "name");
        request.setParameters(param);
        request.executeAsync();
    }

    private void parsePlaces(GraphResponse places) {
        try {
            JSONObject plc = places.getJSONObject();
            JSONArray jarray = plc.getJSONArray("data");

            GraphRequestBatch batch = new GraphRequestBatch();
//could by why duplicates
            for(int i = 1; i < jarray.length(); i++){

                JSONObject place = jarray.getJSONObject(i);
                String p = place.getString("name");

                GraphRequest request = GraphRequest.newGraphPathRequest(
                        accessToken,
                        "/search",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response.getJSONObject() != null ){
                                    parseEvents(response);
                                } else {
                                    Toast.makeText(getApplicationContext(), "There was an error when trying to retrieve events.", Toast.LENGTH_LONG);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("type", "event");
                parameters.putString("q", p);
                parameters.putString("fields", "name,description,id,cover,place,start_time,end_time");
                parameters.putString("since", startDate);
                parameters.putString("until", untilDate);
                parameters.putString("limit", "50");
                request.setParameters(parameters);

                batch.add(request);

            }
           // Log.d("Batch Size", Integer.toString(batch.size()));
            batch.addCallback(new GraphRequestBatch.Callback() {
                @Override
                public void onBatchCompleted(GraphRequestBatch graphRequests) {
                    // Application code for when the batch finishes
                    Log.d("TEST Evemts", Integer.toString(events.size()));
                    findEvents();
                }
            });

            batch.executeAsync();


        }catch (Exception e){
            Log.e(TAG, "Place: ", e);
        }
    }

    private void getEventsBySearchWord(String p) {
        //parse events by keyword
    }

    private void parseEvents(GraphResponse events_response) {
        EditText searchKeyword = (EditText) findViewById(R.id.keyword);
        Spinner locationChosen = (Spinner) findViewById(R.id.location);
        try {
            JSONObject evts = events_response.getJSONObject();

            JSONArray earray ;
            //uses test data
            if(evts != null) {
                if (evts.getJSONArray("data").length() == 0) {
                    JSONObject o = new JSONObject(getString(R.string.test_events));
                    earray = o.getJSONArray("data");
                    Log.e("test array",earray.toString());
                } else {
                    earray = evts.getJSONArray("data");
                }
                Log.d("Array length", Integer.toString(earray.length()));
                for (int i = 0; i < earray.length(); i++) {
                    JSONObject event = earray.getJSONObject(i);
                    Event new_event = new Event(event);
                    Log.d("searchKeyword", searchKeyword.getText().toString());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date event_date = dateFormat.parse(new_event.getDate());
                    Date beginning_date = dateFormat.parse(startDate);
                    Date final_date = dateFormat.parse(untilDate);
                    Log.d("All dates", new_event.getDate() + startDate + untilDate);

                    if (searchKeyword.getText().equals(null)) {
                        //TODO: Implement filtering events based on date string from jsonobject
                        //Checks the choice from the location menu and compares the city with the location parameter of the new json objects or else if its current location just add the event
                        if (locationChosen.getSelectedItem().equals(new_event.getLocation()) || locationChosen.getSelectedItem().equals("Use current Location") && new_event.getLocation().equals("Toronto")) {
                            if (event_date.before(final_date) && event_date.after(final_date)) {
                                events.add(new_event);
                                Log.d("url_pic", "tesst anything");
                            }
                        }
                        //had to add lowercase to the string since it wasn't correctly comparing strings with different cases
                    } else if (new_event.getName().toLowerCase().contains(searchKeyword.getText().toString().toLowerCase()) || new_event.getLongDescription().toLowerCase().contains(searchKeyword.getText().toString().toLowerCase())) {
                        if (locationChosen.getSelectedItem().equals(new_event.getLocation()) || locationChosen.getSelectedItem().equals("Use current Location") && new_event.getLocation().equals("Toronto")) {
                            if (event_date.before(final_date) && event_date.after(beginning_date)) {
                                events.add(new_event);
                                Log.d("url_pic", searchKeyword.getText().toString());
                            }
                        }
                    }
                }


            }
        }catch (Exception ex){
            Log.e(TAG, "Parse Event: ", ex);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location Permission was allowed", Toast.LENGTH_LONG).show();
                    // permission was granted, yay!
                    getPlaces();
                } else {

                    // permission denied, boo!
                    //getPlaces();
                    Toast.makeText(this, "Location Permission was denied", Toast.LENGTH_LONG).show();

                }
                return;
            }
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

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.getItem(1).setEnabled(false);
        return true;
    }

    public void checkValues(Button date) {
        boolean current = true;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String[] length = {"31", "28", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31"};

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String todayString = dateFormat.format(todayDate);
        int newMonth = 0, newYear = 0;
        String[] values;
        String endDate = "";
        if (date.getText().equals("Current Date")) {
            values = todayString.split("/");
            Log.d("values", values[0] + values[1]);
            newMonth = Integer.parseInt(values[0]);
            newYear = Integer.parseInt(values[2]);
            startDate = todayString;
            Log.d("TodayString", todayString);
            endDate = setUpdate(startDate);
        }
        else{
            current = false;
            Log.d("Button date", date.getText().toString());
            values = date.getText().toString().split("-");
            newYear = Integer.parseInt(values[0]);
            newMonth = Integer.parseInt(values[1]);
            startDate = date.getText().toString();
            Button untilDateButton = (Button) findViewById(R.id.untilDateSpinner);
            String[] parameters = untilDateButton.getText().toString().split("-");
            endDate = parameters[1] + "/" + parameters[2] + "/" + parameters[0];
        }
        Log.d("ENDING DATE", endDate);
        String beginDate = "";
        if (current) {
            beginDate = values[0] + "/" + values[1] + "/" + values[2];
        } else {
            beginDate = values[1] + "/" + values[2] + "/" + values[0];
        }

        startDate = beginDate;
        untilDate = endDate;
        getLocation();
    }

    private void Logout() {
        LoginManager.getInstance().logOut();

        Intent i = new Intent(this, MainActivity.class);
// set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
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

    public String setUpdate(String startingDate) {
        int newMonth,newYear;
        String[] length = {"31", "28", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31"};

        Log.d("startingDate", startingDate);
        String dateRange = settings.getString("dateRange", "One Month");
        String[] values = startingDate.split("/");
        newMonth = Integer.parseInt(values[0]);
        newYear = Integer.parseInt(values[2]);

        switch (dateRange) {                                                                    //Used to set the dateRange dropdown menu with previous selection saved
            case "One Month":
                if ((newMonth + 1) > 12) {
                    newMonth = 1;
                    newYear += 1;
                } else {
                    newMonth += 1;
                }
                break;
            case "Two Months":
                if ((newMonth + 2) > 12) {
                    newMonth = (newMonth + 2) > 13 ? 2 : 1;
                    newYear += 1;
                } else {
                    newMonth += 2;
                }
                break;
            case "Three Months":
                if ((newMonth + 3) > 12) {
                    newMonth = ((newMonth + 3) > 13 ? (newMonth + 3 > 14 ? 3 : 2) : 1);
                    newYear += 1;
                } else {
                    newMonth += 3;
                }
                break;
        }
        Log.d("Month and Year", Integer.toString(newMonth + newYear));
        return Integer.toString(newMonth) + "/" + length[newMonth-1] + "/" + Integer.toString(newYear);

    }
}