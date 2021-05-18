package com.college.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.employeetrackingapplication.R;
import com.college.employeetrackingapplication.TaskCompleteActivity;
import com.college.pojo.AssignTaskList;
import com.college.util.SharedPreference;

import java.util.ArrayList;

public class AssignTaskAdapter extends RecyclerView.Adapter<AssignTaskAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AssignTaskList> list;


    public AssignTaskAdapter(Context context, ArrayList<AssignTaskList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AssignTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem = layoutInflater.inflate(R.layout.custom_assign_task, parent, false);
        listItem.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new AssignTaskAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignTaskAdapter.ViewHolder holder, int position) {
        AssignTaskList taskList=list.get(position);
        holder.tv_title.setText("Title: "+taskList.getT_title());
        String source= SharedPreference.get("source");

        holder.tv_desc.setText("Description: "+taskList.getT_description());
        holder.tv_source.setText("Address: "+taskList.getT_address());
        holder.tv_source.setPaintFlags(holder.tv_source.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tv_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTrack(source,taskList.getT_address());
            }
        });
        holder.tv_time.setText("Time: "+taskList.getT_time());

        if (taskList.getT_status().equals("0")){
            holder.tv_status.setText("Status: Task Pending");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.blue));
        }else {
            holder.tv_status.setText("Status: Task Completed");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
        }


        if (taskList.getT_status().equals("0")){
            holder.button_task.setVisibility(View.VISIBLE);
        }else {
            holder.button_task.setVisibility(View.GONE);
        }

        holder.button_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TaskCompleteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("t_id",taskList.getT_id());
                context.startActivity(intent);
            }
        });


    }

    private void displayTrack(String source, String t_source) {
        try {
            Uri uri=Uri.parse("https://www.google.co.in/maps/dir/"+source+"/"+t_source);
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_desc,tv_source,tv_status,tv_time;
        Button button_task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.txt_title);
            tv_desc=itemView.findViewById(R.id.txt_desc);
            tv_source=itemView.findViewById(R.id.txt_address);
            tv_status=itemView.findViewById(R.id.txt_status);
            tv_time=itemView.findViewById(R.id.txt_time);
            button_task=itemView.findViewById(R.id.btn_complete_task);


        }
    }
}
