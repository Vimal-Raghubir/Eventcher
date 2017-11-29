package com.yuyakaido.android.cardstackview.sample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.*;

import com.facebook.CallbackManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SecondActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    AccessToken accessToken;
    Profile profileCurrent;

    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

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

        Button search = (Button) findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findEvents();
            }
        });

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

        Button Logout = (Button) findViewById((R.id.logout));
        Logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity();
            }
        });

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale( this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions( this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }
        else { getPlaces(); }

    }

    private void findEvents() {
        Intent eventIntent = new Intent(this, EventDisplayActivity.class);
        eventIntent.putExtra("eventList", events);
        startActivity(eventIntent);
    }

    private void getPlaces() {
        double longitude = -79.3832;
        double latitude = 43.6532;
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

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

                Log.e("TEST","TWO");

                GraphRequest request = GraphRequest.newGraphPathRequest(
                        accessToken,
                        "/search",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response != null ){
                                    parsePlaces(response);
                                    Log.e("TEST","THREE");
                                }
                            }
                        });

                Bundle param = new Bundle();
                param.putString("type", "place");
                param.putString("center", String.valueOf(latitude) + "," + String.valueOf(longitude) );//String.valueOf(locationManager.getLastKnownLocation(getApplicationContext().LOCATION_SERVICE).getLatitude()) + "," + String.valueOf(locationManager.getLastKnownLocation(getApplicationContext().LOCATION_SERVICE).getLongitude()));
                param.putString("distance","100");
                param.putString("limit", "5");
                param.putString("fields", "name");
                request.setParameters(param);
                request.executeAsync();
            }
        }
    }

    private void parsePlaces(GraphResponse places) {
        try {
            JSONObject plc = places.getJSONObject(); //new JSONObject(places.toString());//.getRawResponse();
            JSONArray jarray = plc.getJSONArray("data");
            for(int i = 1; i < jarray.length(); i++){

                JSONObject place = jarray.getJSONObject(i);
                String p = place.getString("name");
                getEvents(p);
            }

        }catch (Exception e){
            Log.e(TAG, "Place: ", e);
        }
    }

    private void getEvents(String p) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/search",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null ){
                            parseEvents(response);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("type", "event");
        parameters.putString("q", p);
        parameters.putString("fields", "name,description,id,cover,place,end_time,start_time");
        parameters.putString("since", "2017-11-12");
        parameters.putString("until", "2017-12-31");
        parameters.putString("limit", "50");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void parseEvents(GraphResponse events_response) {

        try {
            JSONObject evts = events_response.getJSONObject(); //new JSONObject(places.toString());//.getRawResponse();
            if(evts != null){
                JSONArray earray = evts.getJSONArray("data");
                for(int i = 0; i < earray.length(); i++) {
                    JSONObject event = earray.getJSONObject(i);
                    Event new_event = new Event(event);
                    events.add(new_event);
                }

            }
        }catch (Exception ex){
            Log.e(TAG, "Parse Event: ", ex);
        }
        /*for (int a = 0; a < events.size(); a++) {
            if (textbox.getTextSize() < 5000) textbox.append("Event" + a + ": " + events.get(a).toString());
        }*/
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

                    // permission was granted, yay!
                    getPlaces();
                } else {

                    // permission denied, boo!
                    getPlaces();

                }
                return;
            }
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
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }



}