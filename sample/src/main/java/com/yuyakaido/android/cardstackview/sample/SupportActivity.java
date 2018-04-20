package com.yuyakaido.android.cardstackview.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import static com.yuyakaido.android.cardstackview.sample.SettingsActivity.PREFS_NAME;

public class SupportActivity extends AppCompatActivity {
    String activityTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);                       //Used for rendering the app theme before page is created
        activityTheme = settings.getString("theme", "Daylight");
        if (activityTheme.equals("Daylight")) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        //invalidateOptionsMenu();

        TextView FAQDetails = (TextView) findViewById(R.id.FAQDetails);
        String input = "<b>How do I change the location?</b><br><br> Go to the Search option from the drop-down menu (top right corner) and select your location. You can choose one of the pre-defined options or to search based on your current location.<br><br><b>How do I change the time for the search?</b><br><br>The starting date and end date can be configured on the Search page. Additionally, the default date range can be changed by accessing the Settings page from the drop-down menu and changing the Date Range value.<br><br><b>No events are being shown when entering a keyword.</b><br><br>If no events are being generated then try changing the search criteria. The more specific the search criteria the fewer events will be found. The keyword is an optional criterion and can be omitted when searching for events.<br><br><b>What if I don’t have a facebook account?</b><br><br>You can register for a free Facebook account <a href='www.facebook.com'>here</a>. It is required to verify users.<br><br>Please click the <a href='https://goo.gl/forms/tfo9SRLkbNGcNb432'>link</a> to provide customer feedback";
        FAQDetails.setText(Html.fromHtml(input));
        FAQDetails.setMovementMethod(LinkMovementMethod.getInstance());
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
        menu.getItem(4).setEnabled(false);
        return true;
    }

    private void Logout() {
        LoginManager.getInstance().logOut();

        Intent i = new Intent(this, MainActivity.class);
// set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
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
}
