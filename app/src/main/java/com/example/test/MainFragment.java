package com.example.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainFragment extends Fragment {

    RequestQueue mQueue;
    ArrayList<Users> mArraylist;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mlayout;
    RecyclerView.Adapter mAdapter;
    Button submitButton;
    EditText userName;
    String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArraylist = new ArrayList<>();
        mQueue = Volley.newRequestQueue(getContext());
        jsonParse();

        mlayout = new LinearLayoutManager(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragment, container, false);
        submitButton = view.findViewById(R.id.nameSubmitButton);
        userName = view.findViewById(R.id.nameTextBox);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            name = userName.getText().toString();
            }
        });
        return view;
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
