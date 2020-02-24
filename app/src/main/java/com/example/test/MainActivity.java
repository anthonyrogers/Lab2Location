package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private PagerAdapter mPageAdapter;
    private ViewPager mViewPager;
    private Fragment mainFrag;
    private Fragment mapFrag;
    RequestQueue mQueue;
    Users appUser;
    String name = "Anthony";

    LocationManager locationManager;
    LocationListener locationListener;

    TimerTask scanTask;
    Handler handler;
    Timer mTimer;

    public ArrayList<Users> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            userArrayList = new ArrayList<>();
            mQueue = Volley.newRequestQueue(this);
            jsonParse();
            GetLocation();
            mViewPager = findViewById(R.id.container);

        handler  = new Handler();
        mTimer = new Timer();
        updateLocation();
    }

    private void setupViewPager(ViewPager viewPager){

        mainFrag = new MainFragment();
        mapFrag = new MapFragment();

        Bundle args = new Bundle();
        args.putSerializable("array", userArrayList);
        mainFrag.setArguments(args);

        Bundle args2 = new Bundle();
        args2.putSerializable("array", userArrayList);
        mapFrag.setArguments(args2);

        mPageAdapter= new PagerAdapter(getSupportFragmentManager());
        mPageAdapter.addFragment(mainFrag);
        mPageAdapter.addFragment(mapFrag);
        mPageAdapter.notifyDataSetChanged();

        mViewPager.setAdapter(mPageAdapter);
    }

    public void updateLocation(){

        scanTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        userArrayList.clear();
                        jsonParse();
                        GetLocation();
                        Log.d("LOGGEDLocation", "New refresh");
                    }
                });
            }};

        mTimer.schedule(scanTask, 30000, 30000);
    }

    public void jsonParse(){
        String url = "https://kamorris.com/lab/get_locations.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for(int x = 0; x < response.length(); x++) {
                        Users use = new Users();
                        JSONObject jsonObject = response.getJSONObject(x);
                        if (jsonObject.getString("username").equals("Anthony")){
                            use.username = jsonObject.getString("username");
                            use.lat = jsonObject.getString("latitude");
                            use.lon = jsonObject.getString("longitude");
                            use.getLocation();
                            appUser = use;
                        }
                    }

                    for(int i = 0; i < response.length(); i++){
                        JSONObject jsonObject = response.getJSONObject(i);
                        Users user = new Users(jsonObject.getString("username"),jsonObject.getString("latitude"),
                                jsonObject.getString("longitude"), appUser);
                        userArrayList.add(user);

                    }
                    Collections.sort(userArrayList);
                    setupViewPager(mViewPager);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error", "THIS IS AN ERROR");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "THIS IS AN ERROR");
            }
        });

        mQueue.add(request);

    }

    private void GetLocation(){
        locationManager = getSystemService(LocationManager.class);



        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {



                String URL = "https://kamorris.com/lab/register_location.php";
                StringRequest sr = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("user", name );
                        params.put("latitude", Double.toString(location.getLatitude()));
                        params.put("longitude", Double.toString(location.getLatitude()));
                        return params;
                    }

                };
                mQueue.add(sr);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

}
