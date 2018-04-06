package com.yuyakaido.android.cardstackview.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class TouristSpotCardAdapter extends ArrayAdapter<Event> {

    public TouristSpotCardAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_tourist_spot_card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        Event spot = getItem(position);

        holder.name.setText(spot.getName());
        //holder.city.setText(spot.city);
       /* holder.description.setText(spot.getShortDescription());
        holder.start_time.setText(spot.getStartTime().toString());
        holder.end_time.setText(spot.getEndTime().toString());
        */
        //Glide.with(getContext()).load(spot.getCoverURL()).into(holder.image);
        Picasso.with(getContext()).load(spot.getCoverURL()).into(holder.image);

        return contentView;
    }

    private static class ViewHolder {
        public TextView name;
        //public TextView city;
        public TextView description;
        public TextView start_time;
        public TextView end_time;
        public ImageView image;

        public ViewHolder(View view) {
            this.name = (TextView) view.findViewById(R.id.item_tourist_spot_card_name);
            //this.city = (TextView) view.findViewById(R.id.item_tourist_spot_card_city);
            /*this.description = (TextView) view.findViewById(R.id.item_tourist_spot_card_desc);
            this.start_time = (TextView) view.findViewById(R.id.item_tourist_spot_card_start_time);
            this.end_time = (TextView) view.findViewById(R.id.item_tourist_spot_card_end_time);
            */
            this.image = (ImageView) view.findViewById(R.id.item_tourist_spot_card_image);
        }
    }

}

