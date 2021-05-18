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

public class AdminLoginActivity extends AppCompatActivity {


    EditText editText_email,editText_password;
    Button button_login;
    TextView tv_register;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        progress=findViewById(R.id.progress_login);
        


        editText_email=findViewById(R.id.edt_email);
        editText_password=findViewById(R.id.edt_pass);
        button_login=findViewById(R.id.btn_login);
       
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a_email=editText_email.getText().toString().trim();
                String a_password=editText_password.getText().toString().trim();
                if (a_email.equals("") || a_password.equals("")){
                    Toast.makeText(AdminLoginActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(a_email).matches()){
                    editText_email.setError("Invalid Email ID");
                }else {
                    login(a_email,a_password);
                }
            }
        });

    }

    private void login(final String a_email, final String a_password) {
        progress.setVisibility(View.VISIBLE);
        StringRequest request=new StringRequest(Request.Method.POST, Keys.URL.ADMIN_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")){

                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        String a_id=jsonObject1.getString("a_id");
                        SharedPreference.save("a_id",a_id);

                        Toast.makeText(AdminLoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AdminLoginActivity.this, AdminMenuActivity.class);
                        startActivity(intent);
                        clear();
                        finish();

                    }else {
                        Toast.makeText(AdminLoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AdminLoginActivity.this, "Technical problem arises", Toast.LENGTH_SHORT).show();
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
                params.put("a_email",a_email);
                params.put("a_password",a_password);
                return params;
            }
        };
        AppController.getInstance().add(request);




    }

    private void clear() {
        editText_email.setText("");
        editText_password.setText("");

    
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AdminLoginActivity.this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}