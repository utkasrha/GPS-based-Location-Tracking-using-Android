package com.college.employeetrackingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.college.util.SharedPreference;

public class MainMenuActivity extends AppCompatActivity {

    CardView cardView_admin,cardView_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        cardView_admin=findViewById(R.id.cd_admin);
        cardView_user=findViewById(R.id.cd_user);

        getSupportActionBar().setTitle("Menu");

        cardView_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreference.contains("a_id")){
                    Intent intent=new Intent(MainMenuActivity.this,AdminMenuActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(MainMenuActivity.this,AdminLoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        cardView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreference.contains("e_id")) {
                    Intent intent = new Intent(MainMenuActivity.this, EmployeeMenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MainMenuActivity.this, EmployeeLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}