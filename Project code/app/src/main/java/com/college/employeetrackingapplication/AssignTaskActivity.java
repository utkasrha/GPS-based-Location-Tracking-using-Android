package com.college.employeetrackingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.college.adapter.AssignTaskAdapter;
import com.college.pojo.AssignTaskList;
import com.college.util.AppController;
import com.college.util.Keys;
import com.college.util.SharedPreference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignTaskActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FusedLocationProviderClient client;
    LocationCallback callback;
    Location location;
    LocationRequest request;
    ArrayList<AssignTaskList> taskLists;
    AssignTaskAdapter assignTaskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        SharedPreference.initialize(getApplicationContext());
        AppController.initialize(getApplicationContext());
        getSupportActionBar().setTitle("Assign Task List");
        taskLists=new ArrayList<>();
        recyclerView=findViewById(R.id.recylcer_assign_task);
        swipeRefreshLayout=findViewById(R.id.sw_data);
        progressBar=findViewById(R.id.progress_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.purple_200));
        String e_id=SharedPreference.get("e_id");
        loadList(e_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                taskLists.clear();
                loadList(e_id);


            }
        });

        if (checkPermissionStatus())
            client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //to get location result
                location = locationResult.getLocations().get(0);
                gpsLocation();
                super.onLocationResult(locationResult);
            }
        };
        request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(3000);

        LinearLayoutManager layoutManager=new LinearLayoutManager(AssignTaskActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);



    }

    private void loadList(String e_id) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Keys.URL.SEE_ASSIGN_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Nik", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            taskLists.add(new AssignTaskList(
                                    jsonObject1.getString("t_id"),
                                    jsonObject1.getString("e_id"),
                                    jsonObject1.getString("t_title"),
                                    jsonObject1.getString("t_description"),
                                    jsonObject1.getString("t_address"),
                                    jsonObject1.getString("t_status"),
                                    jsonObject1.getString("t_time")));

                        }
                        assignTaskAdapter = new AssignTaskAdapter(AssignTaskActivity.this,taskLists);
                        recyclerView.setAdapter(assignTaskAdapter);
                        assignTaskAdapter.notifyDataSetChanged();

                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(AssignTaskActivity.this, "Technical Problem arises", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("e_id",e_id);
                return params;
            }
        };
        AppController.getInstance().add(request);
    }

    private boolean checkPermissionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, 123);
            } else {

            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==123){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                startLocation();
                //sendSMS(phone);
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "You have to accept permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void startLocation() {
        client.requestLocationUpdates(request, callback, Looper.myLooper());
    }
    private void gpsLocation() {
        Geocoder geocoder=new Geocoder(AssignTaskActivity.this);
        try{
            String e_id= SharedPreference.get("e_id");
            List<Address> alAd=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            Address ad=alAd.get(0);
            String address=ad.getAddressLine(0);
            SharedPreference.save("address",address);
            String coordinate=location.getLatitude() + "," + location.getLongitude();
            Log.i("nik",coordinate);

            addLocation(e_id,address,String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addLocation(String e_id, String address, String latitude, String longitude) {
        StringRequest request=new StringRequest(Request.Method.POST, Keys.URL.ADD_LOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")){
             //           Toast.makeText(AssignTaskActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(AssignTaskActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AssignTaskActivity.this, "Technical problem arises", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("e_id",e_id);
                params.put("l_lattitude",latitude);
                params.put("l_longitude",longitude);
                params.put("l_address",address);
                return params;
            }
        };
        AppController.getInstance().add(request);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(AssignTaskActivity.this,EmployeeMenuActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AssignTaskActivity.this,EmployeeMenuActivity.class);
        startActivity(intent);
        finish();
    }
}