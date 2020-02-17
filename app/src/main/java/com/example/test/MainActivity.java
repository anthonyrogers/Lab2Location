package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RequestQueue mQueue;
    ArrayList<Users> mArraylist;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mlayout;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mArraylist = new ArrayList<>();
            mQueue = Volley.newRequestQueue(this);
            jsonParse();
            mRecyclerView = findViewById(R.id.recyclerview);
            mlayout = new LinearLayoutManager(this);

    }

    public void jsonParse(){
        String url = "https://kamorris.com/lab/get_locations.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for(int i = 0; i < response.length(); i++){
                        JSONObject jsonObject = response.getJSONObject(i);
                        Users user = new Users();
                        user.username = jsonObject.getString("username");
                        user.lat = jsonObject.getString("latitude");
                        user.lon = jsonObject.getString("longitude");
                        mArraylist.add(user);

                    }
                    mAdapter = new MainAdapter(mArraylist);
                    mRecyclerView.setLayoutManager(mlayout);
                    mRecyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mQueue.add(request);
    }
}
