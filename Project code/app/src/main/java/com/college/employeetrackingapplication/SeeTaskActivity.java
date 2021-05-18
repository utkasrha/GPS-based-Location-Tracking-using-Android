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
import com.college.adapter.TaskListAdapter;
import com.college.pojo.AssignTaskList;
import com.college.pojo.TaskList;
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

public class SeeTaskActivity extends AppCompatActivity {

    RecyclerView recyclerView_task;
    ProgressBar progressBar;
    ArrayList<TaskList> taskLists;
    SwipeRefreshLayout swipeRefreshLayout;
    TaskListAdapter taskListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_task);
        taskLists=new ArrayList<>();
        getSupportActionBar().setTitle("See Task List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView_task=findViewById(R.id.recylcer_see_task);
        progressBar=findViewById(R.id.progress_list);
        swipeRefreshLayout=findViewById(R.id.sw_data);
        loadList();
        LinearLayoutManager layoutManager=new LinearLayoutManager(SeeTaskActivity.this);
        recyclerView_task.setHasFixedSize(true);
        recyclerView_task.setLayoutManager(layoutManager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                taskLists.clear();
                loadList();


            }
        });


    }

    private void loadList() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Keys.URL.SEE_TASK, new Response.Listener<String>() {
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
                            taskLists.add(new TaskList(
                                    jsonObject1.getString("t_id"),
                                    jsonObject1.getString("e_id"),
                                    jsonObject1.getString("t_title"),
                                    jsonObject1.getString("t_description"),
                                    jsonObject1.getString("t_address"),
                                    jsonObject1.getString("t_status"),
                                    jsonObject1.getString("t_time"),
                                    jsonObject1.getString("e_name")));

                        }
                        taskListAdapter = new TaskListAdapter(SeeTaskActivity.this,taskLists);
                        recyclerView_task.setAdapter(taskListAdapter);
                        taskListAdapter.notifyDataSetChanged();

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
                Toast.makeText(SeeTaskActivity.this, "Technical Problem arises", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        AppController.getInstance().add(request);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(SeeTaskActivity.this,AdminMenuActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SeeTaskActivity.this,AdminMenuActivity.class);
        startActivity(intent);
        finish();
    }
}