package com.example.talalrashid.ride;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hamza Imtiaz on 10/19/2017.
 */

public class search_ride_adapter extends BaseAdapter {

        ArrayList<Ride_class> allRides;
    public Resources res;
        private Context c;

        public search_ride_adapter(Context context, ArrayList<Ride_class> allRides){

            this.c=context;
            this.allRides = allRides;

            LayoutInflater inflter = (LayoutInflater.from(context));


        }

        @Override
        public int getCount() {
            return allRides.size();
        }

        @Override
        public Object getItem(int position) {
            return allRides.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Ride_class tempRide = (Ride_class) getItem(position);


            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.search_ride_list,parent,false);
            }



            TextView conSrc = (TextView) convertView.findViewById(R.id.source);
            conSrc.setText("Source: " + tempRide.getSource_name());

            TextView conDest = (TextView) convertView.findViewById(R.id.destination);
            conDest.setText("Destination: " + tempRide.getDestination_name());



            return convertView;
        }
    }


