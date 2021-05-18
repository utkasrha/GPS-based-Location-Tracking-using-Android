
package com.college.employeetrackingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.college.util.SharedPreference;

public class AdminMenuActivity extends AppCompatActivity {

    CardView cardView_add_employee,cardView_see_employee,cardView_logout,cardView_addtask,cardView_see_task;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        getSupportActionBar().setTitle("Menu");
        cardView_add_employee=findViewById(R.id.cd_add_employee);
        cardView_see_employee=findViewById(R.id.cd_see_employee);
        cardView_logout=findViewById(R.id.cd_logout);
        cardView_addtask=findViewById(R.id.cd_add_task);
        cardView_see_task=findViewById(R.id.cd_see_task);
        cardView_addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminMenuActivity.this,AddTaskActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cardView_see_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminMenuActivity.this,SeeTaskActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cardView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreference.contains("a_id")){
                    SharedPreference.removeKey("a_id");
                    Intent i = new Intent(AdminMenuActivity.this,AdminLoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        cardView_add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminMenuActivity.this,AddEmployeeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cardView_see_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminMenuActivity.this,SeeEmployeeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AdminMenuActivity.this,MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}