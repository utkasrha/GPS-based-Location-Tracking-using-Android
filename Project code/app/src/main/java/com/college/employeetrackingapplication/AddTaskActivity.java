package com.college.employeetrackingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.college.util.AppController;
import com.college.util.Keys;
import com.college.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    EditText editText_title,editText_desc,editText_address;
    Spinner spinner_employee;
    Button button_add_task;
    RelativeLayout progress;
    ArrayList<String> employeeList=new ArrayList<>();
    ArrayList<String> e_id=new ArrayList<>();
    String emp_id;
    ArrayAdapter<String> adapter;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        spinner_employee=findViewById(R.id.spinner_employee);
        editText_title=findViewById(R.id.edt_title);
        editText_address=findViewById(R.id.edt_address);
        editText_desc=findViewById(R.id.edt_description);
        button_add_task=findViewById(R.id.btn_add_task);
        progress=findViewById(R.id.progress);
        imageView=findViewById(R.id.back_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddTaskActivity.this,AdminMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        StringRequest request=new StringRequest(Request.Method.POST, Keys.URL.SEE_EMPLOYEE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("nik",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")){

                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            e_id.add(jsonObject1.getString("e_id"));
                            employeeList.add(jsonObject1.getString("e_name"));
                        }
                        adapter=new ArrayAdapter<String>(AddTaskActivity.this,android.R.layout.simple_spinner_dropdown_item,employeeList);
                        spinner_employee.setAdapter(adapter);




                    }else {

                        Toast.makeText(AddTaskActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddTaskActivity.this, "Technical problem arises", Toast.LENGTH_SHORT).show();
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

                return params;
            }
        };
        AppController.getInstance().add(request);
        button_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t_title=editText_title.getText().toString().trim();
                String t_address=editText_address.getText().toString().trim();
                String t_description=editText_desc.getText().toString().trim();
//                int pos =spinner_employee.getSelectedItemPosition();
//                if(pos!=0)
//                {
//                    emp_id = e_id.get(spinner_employee.getSelectedItemPosition());
//                }else{
//                    Toast.makeText(AddTaskActivity.this, "Please Select Team", Toast.LENGTH_LONG).show();
//                    return;
//                }
                if (t_title.equals("") || t_address.equals("") || t_description.equals("")){
                    Toast.makeText(AddTaskActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }else {
                    emp_id=e_id.get(spinner_employee.getSelectedItemPosition());
                    addTask(emp_id,t_title,t_description,t_address);
                }


            }
        });


    }

    private void addTask(String e_id, String t_title, String t_description, String t_address) {
        progress.setVisibility(View.VISIBLE);
        StringRequest request=new StringRequest(Request.Method.POST, Keys.URL.ADD_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")){
                        Toast.makeText(AddTaskActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        editText_address.setText("");
                        editText_desc.setText("");
                        editText_title.setText("");

                    }else {
                        Toast.makeText(AddTaskActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddTaskActivity.this, "Technical problem arises", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("e_id",e_id);
                params.put("t_title",t_title);
                params.put("t_description",t_description);
                params.put("t_address",t_address);
                return params;
            }
        };
        AppController.getInstance().add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AddTaskActivity.this,AdminMenuActivity.class);
        startActivity(intent);
        finish();
    }
}