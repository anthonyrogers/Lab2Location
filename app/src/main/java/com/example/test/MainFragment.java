package com.example.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment {


   public ArrayList<Users> mArraylist;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mlayout;
    RecyclerView.Adapter mAdapter;
    Button submitButton;
    EditText userName;
    String name = "Anthony";

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArraylist = new ArrayList<>();
        mlayout = new LinearLayoutManager(getContext());

        mArraylist = (ArrayList<Users>) getArguments().getSerializable("array");

        Log.d("key", mArraylist.get(0).username);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new MainAdapter(mArraylist);
        mRecyclerView.setLayoutManager(mlayout);
        mRecyclerView.setAdapter(mAdapter);
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
                if (getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    name = userName.getText().toString();
                }

            }

        });

        return view;
    }

}
