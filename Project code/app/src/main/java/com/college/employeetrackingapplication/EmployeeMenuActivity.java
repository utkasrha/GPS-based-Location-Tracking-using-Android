package com.college.employeetrackingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.college.util.SharedPreference;

public class EmployeeMenuActivity extends AppCompatActivity {

    CardView cardView_see_task,cardView_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_menu);
        getSupportActionBar().setTitle("See Task");
        cardView_see_task=findViewById(R.id.cd_see_task);
        cardView_logout=findViewById(R.id.cd_logout);
        cardView_see_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeMenuActivity.this, AssignTaskActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cardView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreference.contains("e_id")){
                    SharedPreference.removeKey("e_id");
                    Intent i = new Intent(EmployeeMenuActivity.this,EmployeeLoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(EmployeeMenuActivity.this,MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}