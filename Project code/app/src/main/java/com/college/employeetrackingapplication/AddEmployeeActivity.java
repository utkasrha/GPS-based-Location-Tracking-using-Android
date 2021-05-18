package com.college.employeetrackingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEmployeeActivity extends AppCompatActivity {

    EditText editText_date,editText_name,editText_email,editText_phone,editText_password,editText_c_pass,
            editText_age,editText_address,editText_city,editText_state,editText_post;
    Calendar myCalendar;
    ImageView imageView;
    Button button_add_employee;

    RelativeLayout progressBar;
    RadioButton btn1,btn2,r;
    RadioGroup radioGroup;
    String e_gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        progressBar=findViewById(R.id.progress);

        radioGroup=findViewById(R.id.rd_group);
        btn1=findViewById(R.id.rd_btn1);
        btn2=findViewById(R.id.rd_btn2);
        editText_password=findViewById(R.id.edt_pass);
        editText_c_pass=findViewById(R.id.edt_c_pass);
        button_add_employee=findViewById(R.id.btn_add_employee);
        editText_name=findViewById(R.id.edt_name);
        editText_email=findViewById(R.id.edt_email);
        editText_phone=findViewById(R.id.edt_phone);
        editText_age=findViewById(R.id.edt_age);
        editText_address=findViewById(R.id.edt_address);
        editText_city=findViewById(R.id.edt_city);
        editText_state=findViewById(R.id.edt_state);
        editText_post=findViewById(R.id.edt_post);
        editText_date = findViewById(R.id.edt_dob);
        imageView=findViewById(R.id.back_btn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddEmployeeActivity.this,AdminMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                r=findViewById(checkedId);
                e_gender=r.getText().toString();

            }
        });

        // myCalendar = Calendar.getInstance();
        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(editText_date);
//                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                          int dayOfMonth) {
//                        myCalendar.set(Calendar.YEAR, year);
//                        myCalendar.set(Calendar.MONTH, monthOfYear);
//                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                        updateLabel();
//
//                    }
//
//                };
//                new DatePickerDialog(AddCandidatesActivity.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        button_add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_name=editText_name.getText().toString().trim();
                String e_email=editText_email.getText().toString().trim();
                String e_password=editText_password.getText().toString().trim();
                String e_conf_pass=editText_c_pass.getText().toString().trim();
                String e_phone=editText_phone.getText().toString().trim();
                String e_dob=editText_date.getText().toString().trim();
                String e_age=editText_age.getText().toString().trim();
                String e_address=editText_address.getText().toString().trim();
                String e_city=editText_city.getText().toString().trim();
                String e_state=editText_state.getText().toString().trim();
                String e_post=editText_post.getText().toString().trim();

                if (e_name.equals("") || e_email.equals("")|| e_phone.equals("") || e_dob.equals("") || e_age.equals("") ||
                        e_address.equals("") || e_city.equals("") || e_state.equals("") || e_password.equals("") || e_conf_pass.equals("") ||
                        e_post.equals("")){
                    Toast.makeText(AddEmployeeActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(e_email).matches()){
                    Toast.makeText(AddEmployeeActivity.this, "Invalid Email ID", Toast.LENGTH_SHORT).show();
                    editText_email.setError("Invalid Email Id");
                }else if (e_password.length()!=6){
                    Toast.makeText(AddEmployeeActivity.this, "Password must be 6 digits", Toast.LENGTH_SHORT).show();
                    editText_password.setError("Password must be 6 digits");
                }else if (!e_conf_pass.equals(e_password)){
                    Toast.makeText(AddEmployeeActivity.this, "Please Confirm The Password", Toast.LENGTH_SHORT).show();
                    editText_c_pass.setError("Please confirm the password");
                }else if (e_phone.length()!=10){
                    Toast.makeText(AddEmployeeActivity.this, "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
                    editText_phone.setError("Phone number must be 10 digits");
                }else{
                    addEmployee(e_name,e_email,e_password,e_phone,e_address,e_city,e_state,e_dob,e_age,e_post,e_gender);
                }
            }
        });
    }

    private void addEmployee(String e_name, String e_email, String e_password, String e_phone, String e_address, String e_city, String e_state, String e_dob, String e_age, String e_post, String e_gender) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request=new StringRequest(Request.Method.POST, Keys.URL.ADD_EMPLOYEE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")){
                        Toast.makeText(AddEmployeeActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        clear();
                    }else {
                        Toast.makeText(AddEmployeeActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddEmployeeActivity.this, "Technical problem arises", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("e_email",e_email);
                params.put("e_password",e_password);
                params.put("e_name",e_name);
                params.put("e_phone",e_phone);
                params.put("e_post",e_post);
                params.put("e_dob",e_dob);
                params.put("e_age",e_age);
                params.put("e_gender",e_gender);
                params.put("e_address",e_address);
                params.put("e_city",e_city);
                params.put("e_state",e_state);
                return params;
            }
        };
        AppController.getInstance().add(request);
    }


    private void clear() {
        editText_name.setText("");
        editText_email.setText("");
        editText_phone.setText("");
        editText_date.setText("");
        editText_age.setText("");
        editText_address.setText("");
        editText_city.setText("");
        editText_state.setText("");
        editText_password.setText("");
        editText_c_pass.setText("");

    }

    // Define showDatePickerDialog(View).
    public void showDatePickerDialog(View v) {
        // Create and show a new DatePickerFragment.
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Define a DialogFragment class DatePickerFragment.
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        // Define a new Calendar for current date
        Calendar now = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create DatePickerFragment (a DialogFragment) with a new DatePickerDialog
            int y = now.get(Calendar.YEAR);
            int m = now.get(Calendar.MONTH);
            int d = now.get(Calendar.DAY_OF_MONTH);
// Current day of month, month, and year are set as the day, month, and year of this DatePickerDialog.
            return new DatePickerDialog(getActivity(), this, y, m, d);
        }

        // When Date of birth is selected
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int mon = month + 1;
            // Define a new Calendar for birth date.
            Calendar birthDay = Calendar.getInstance();
            String date = day + " / " + mon + " / " + year;
            // Define the 2 EditText again using their IDs in main.xml.
            EditText edtDob=getActivity().findViewById(R.id.edt_dob);
            EditText edtAge=getActivity().findViewById(R.id.edt_age);
            edtDob.setText(date);
            // Set the selected year, month, and day as the year, month, and day of Calendar birthDay.
            birthDay.set(Calendar.YEAR, year);
            birthDay.set(Calendar.MONTH, month);
            birthDay.set(Calendar.DAY_OF_MONTH, day);
            // find difference between present date and selected date in milliseconds.
            double diff = (long) (now.getTimeInMillis() - birthDay.getTimeInMillis());
            // If difference is less than 0, show message that selected date is in future.
            if (diff < 0) {
                Toast.makeText(getContext(), "Please select date is in future.", Toast.LENGTH_SHORT).show();
                edtDob.setText("");
                edtAge.setText("");
            } else {
                // Get difference between years
                int years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
                int currMonth = now.get(Calendar.MONTH) + 1;
                int birthMonth = birthDay.get(Calendar.MONTH) + 1;

                // Get difference between months
                int months = currMonth - birthMonth;

                if (years<18){
                    Toast.makeText(getContext(), "You are not eligible", Toast.LENGTH_SHORT).show();
                    edtDob.setText("");
                    edtAge.setText("");
                }else {
                    edtAge.setText(years + " years");
                }
                // If month difference is negative then reduce years by one
                // and calculate the number of months.
                if (months < 0) {
                    years--;
                    months = 12 - birthMonth + currMonth;
                    if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                        months--;
                } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                    years--;
                    months = 11;
                }

            }
        }
    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AddEmployeeActivity.this,AdminMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
