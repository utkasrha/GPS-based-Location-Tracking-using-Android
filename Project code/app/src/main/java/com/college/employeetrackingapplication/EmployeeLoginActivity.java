package com.college.employeetrackingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.college.util.AppController;
import com.college.util.Keys;
import com.college.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmployeeLoginActivity extends AppCompatActivity {

    EditText editText_email,editText_password;
    Button button_login;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

       
        progress=findViewById(R.id.progress_login);
     

        editText_email=findViewById(R.id.edt_email);
        editText_password=findViewById(R.id.edt_pass);
        button_login=findViewById(R.id.btn_login);
        
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e_email=editText_email.getText().toString().trim();
                String e_password=editText_password.getText().toString().trim();
                if (e_email.equals("") || e_password.equals("")){
                    Toast.makeText(EmployeeLoginActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(e_email).matches()){
                    editText_email.setError("Invalid Email ID");
                }else {
                    login(e_email,e_password);
                }
            }
        });

    }

    private void login(final String e_email, final String e_password) {
        progress.setVisibility(View.VISIBLE);
        StringRequest request=new StringRequest(Request.Method.POST, Keys.URL.EMPLOYEE_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")){

                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        String e_id=jsonObject1.getString("e_id");
                        SharedPreference.save("e_id",e_id);

                        Toast.makeText(EmployeeLoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(EmployeeLoginActivity.this, EmployeeMenuActivity.class);
                        startActivity(intent);
                        clear();
                        finish();

                    }else {
                        Toast.makeText(EmployeeLoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EmployeeLoginActivity.this, "Technical problem arises", Toast.LENGTH_SHORT).show();
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
                params.put("e_email",e_email);
                params.put("e_password",e_password);
                return params;
            }
        };
        AppController.getInstance().add(request);


    }

    private void clear() {
        editText_email.setText("");
        editText_password.setText("");
    }
}