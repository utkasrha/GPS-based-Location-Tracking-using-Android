package com.college.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.college.employeetrackingapplication.R;
import com.college.pojo.Employee;

import java.util.ArrayList;

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    Context context;
    ArrayList<Employee> list;
    int resource;


    public EmployeeAdapter(Context context, int resource, ArrayList<Employee> list) {
        super(context, resource, list);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.custom_user, null, true);

        final Employee candidateName = list.get(position);
        TextView tv=rowView.findViewById(R.id.txt_name);
        CardView cardView=rowView.findViewById(R.id.cd_user);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                View bottomSheetview= LayoutInflater.from(context)
                        .inflate(R.layout.custom_candidate_details,null);
                alertDialogBuilder.setView(bottomSheetview);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                TextView tv_name,tv_email,tv_phone,tv_address,tv_city,tv_state,tv_gender,tv_dob,tv_age,tv_post;

                tv_name=bottomSheetview.findViewById(R.id.txt_name);
                tv_email=bottomSheetview.findViewById(R.id.txt_email);
                tv_phone=bottomSheetview.findViewById(R.id.txt_phone);
                tv_city=bottomSheetview.findViewById(R.id.txt_city);
                tv_address=bottomSheetview.findViewById(R.id.txt_address);
                tv_state=bottomSheetview.findViewById(R.id.txt_state);
                tv_age=bottomSheetview.findViewById(R.id.txt_age);
                tv_dob=bottomSheetview.findViewById(R.id.txt_dob);
                tv_gender=bottomSheetview.findViewById(R.id.txt_gender);
                tv_post=bottomSheetview.findViewById(R.id.txt_post);

                tv_name.setText(candidateName.getE_name());
                tv_email.setText(candidateName.getE_email());
                tv_phone.setText(candidateName.getE_phone());
                tv_city.setText(candidateName.getE_city());
                tv_address.setText(candidateName.getE_address());
                tv_state.setText(candidateName.getE_state());
                tv_age.setText(candidateName.getE_age());
                tv_dob.setText(candidateName.getE_dob());
                tv_gender.setText(candidateName.getE_gender());
                tv_post.setText(candidateName.getE_post());


            }
        });
        tv.setText(candidateName.getE_name());
        return rowView;

    }
}