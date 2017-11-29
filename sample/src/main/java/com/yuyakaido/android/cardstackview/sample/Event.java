package com.yuyakaido.android.cardstackview.sample;

import android.util.Log;

import com.google.zxing.common.StringUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Mera on 2017-11-22.
 */

public class Event implements Serializable {
    String name;
    String description;
    String id;
    Date start_time;
    Date end_time;
    String source;
    //JSONObject location; //TODO: this later

    public Event (JSONObject event){
        String format = "YYYY-MM-DD'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Log.i("TEST", event.toString());
            name = event.getString("name");
            description = (event.has("description")) ? event.getString("description") : "";
            id = event.getString("id");
            start_time = (event.has("start_time")) ? simpleDateFormat.parse(event.getString("start_time")) : null;
            end_time = (event.has("end_time")) ? simpleDateFormat.parse(event.getString("end_time")) : null;

            source = (event.has("cover")) ? event.getJSONObject("cover").getString("source") : "";
            //if (event.has("location")) location =  event.getJSONObject("location");
        }
        catch (Exception e){
            Log.e(TAG, "Event: ", e);
        }
    }

    public String toString(){

        return "\nname:" + this.name + "\ndescription:" + this.description + "\nid:" +
                id + "\nstart_time:" + start_time + "\nend_time:" + end_time + "\nsource:" + source + "\n\n"; //+ " location:" + ((location!=null)?location.toString():"")
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return (description.length() > 300) ? description.substring(0,299) : description;
    }
    public Date getStartTime() {
        return (start_time != null) ? start_time : new Date();
    }

    public Date getEndTime() {
        return (end_time != null) ? end_time : new Date();
    }

    public String getLongDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getCoverURL() {
        return source;
    }


}
