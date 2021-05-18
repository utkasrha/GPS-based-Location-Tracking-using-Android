package com.college.employeetrackingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.college.adapter.EmployeeAdapter;
import com.college.pojo.Employee;
import com.college.util.AppController;
import com.college.util.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeeEmployeeActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView_employee;
    ProgressBar progressBar;
    ArrayList<Employee> employeesList=new ArrayList<>();
    EmployeeAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_employee);
        swipeRefreshLayout=findViewById(R.id.sw_data);
        listView_employee=findViewById(R.id.list_view);
        progressBar=findViewById(R.id.progress_list);
        getSupportActionBar().setTitle("See Employee");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.purple_200));
        employeeList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                employeesList.clear();
                employeeList();


            }
        });
    }

    private void employeeList() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Keys.URL.SEE_EMPLOYEE, new Response.Listener<String>() {
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
                            employeesList.add(new Employee(
                                    jsonObject1.getString("e_id"),
                                    jsonObject1.getString("e_name"),
                                    jsonObject1.getString("e_email"),
                                    jsonObject1.getString("e_phone"),
                                    jsonObject1.getString("e_address"),
                                    jsonObject1.getString("e_city"),
                                    jsonObject1.getString("e_state"),
                                    jsonObject1.getString("e_dob"),
                                    jsonObject1.getString("e_age"),
                                    jsonObject1.getString("e_post"),
                                    jsonObject1.getString("e_gender")));

                        }
                        adapter = new EmployeeAdapter(SeeEmployeeActivity.this,R.layout.custom_user,employeesList);
                        listView_employee.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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
                Toast.makeText(getApplicationContext(), "Technical Problem arises", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SeeEmployeeActivity.this,AdminMenuActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(SeeEmployeeActivity.this,AdminMenuActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}