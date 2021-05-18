package com.college.employeetrackingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TaskDetailsActivity extends AppCompatActivity {

    TextView tv_title,tv_desc,tv_address,tv_status,tv_time,tv_current_location;
    ImageView task_complete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        AppController.initialize(getApplicationContext());
        SharedPreference.initialize(getApplicationContext());
        getSupportActionBar().setTitle("Task Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        task_complete=findViewById(R.id.task_complete_image);
        tv_title=findViewById(R.id.txt_title);
        tv_desc=findViewById(R.id.txt_desc);
        tv_address=findViewById(R.id.txt_address);
        tv_status=findViewById(R.id.txt_status);
        tv_time=findViewById(R.id.txt_time);
        tv_current_location=findViewById(R.id.txt_current_address);
        Intent i=getIntent();
        String t_id=i.getStringExtra("t_id");
        loadList(t_id);
        loadPhotList(t_id);

    }

    private void loadPhotList(String t_id) {
        StringRequest request = new StringRequest(Request.Method.POST, Keys.URL.COMPLETE_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Nik", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String t_id=jsonObject1.getString("t_id");
                            String ta_photo=jsonObject1.getString("ta_photo");
                            Picasso.with(TaskDetailsActivity.this).load(Keys.TASK_PHOTO + ta_photo).into(task_complete);

                        }


                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(TaskDetailsActivity.this, "Technical Problem arises", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("t_id",t_id);
                return params;
            }
        };
        AppController.getInstance().add(request);
    }

    private void loadList(String t_id) {
        StringRequest request = new StringRequest(Request.Method.POST, Keys.URL.TASK_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Nik", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String t_id=jsonObject1.getString("t_id");
                            String e_id=jsonObject1.getString("e_id");
                            String t_title=jsonObject1.getString("t_title");
                            String t_description=jsonObject1.getString("t_description");
                            String t_address=jsonObject1.getString("t_address");
                            String t_status=jsonObject1.getString("t_status");
                            String t_time=jsonObject1.getString("t_time");
                            String l_address=jsonObject1.getString("l_address");
                            String l_latitude=jsonObject1.getString("l_lattitude");
                            String l_longitude=jsonObject1.getString("l_longitude");
                            String coordinate=l_latitude + "," + l_longitude;

                            tv_title.setText(t_title);
                            tv_desc.setText(t_description);
                            tv_address.setText(t_address);
                            tv_current_location.setText(l_address);
                            tv_time.setText(t_time);
                            if (t_status.equals("0")){
                                tv_status.setText("Task Pending");
                            }else {
                                tv_status.setText("Task Completed");
                            }

                            tv_current_location.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        Uri uri = Uri.parse("http://maps.google.com/?q=" + coordinate);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        intent.setPackage("com.google.android.apps.maps");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }catch (ActivityNotFoundException e){
                                        Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                                        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                        }


                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(TaskDetailsActivity.this, "Technical Problem arises", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("t_id",t_id);
                return params;
            }
        };
        AppController.getInstance().add(request);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}