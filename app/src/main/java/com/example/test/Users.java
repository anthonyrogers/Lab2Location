package com.example.test;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

public class Users implements Comparable {
    public String username;
    public String lat;
    public String lon;
    public Users myLocation;
    public Location location;



    public Users(){
        //myLocation = this;

    }

    public Users(String username, String lat, String lon, Users user){
        this.username = username;
        this.lat = lat;
        this.lon = lon;
        this.myLocation = user;
        getLocation();

    }



    public Location getLocation(){
        location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(Double.valueOf(lat));
        location.setLongitude(Double.valueOf(lon));
        return location;
    }

    @Override
    public int compareTo(Object o) {
        if(location.distanceTo(myLocation.location) > ((Users) o).location.distanceTo(myLocation.location)) {
            return 1;
        }else if(location.distanceTo(myLocation.location) == ((Users) o).location.distanceTo(myLocation.location)){
            return 0;
        }else{
            return -1;
        }

    }
}
