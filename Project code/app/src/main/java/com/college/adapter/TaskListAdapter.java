package com.college.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.college.employeetrackingapplication.R;
import com.college.employeetrackingapplication.TaskDetailsActivity;
import com.college.pojo.AssignTaskList;
import com.college.pojo.TaskList;
import com.college.util.SharedPreference;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TaskList> list;


    public TaskListAdapter(Context context, ArrayList<TaskList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem = layoutInflater.inflate(R.layout.custom_task, parent, false);
        listItem.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new TaskListAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.ViewHolder holder, int position) {
        TaskList taskList=list.get(position);
        holder.tv_title.setText("Title: "+taskList.getT_title());
        holder.tv_name.setText("Name: "+taskList.getE_name());

        holder.tv_time.setText("Time: "+taskList.getT_time());

        if (taskList.getT_status().equals("0")){
            holder.tv_status.setText("Status: Task Pending");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.blue));
        }else {
            holder.tv_status.setText("Status: Task Completed");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.cd_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TaskDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("t_id",taskList.getT_id());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_name,tv_status,tv_time;
        CardView cd_tasks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.txt_title);
            tv_name=itemView.findViewById(R.id.txt_name);
            tv_status=itemView.findViewById(R.id.txt_status);
            tv_time=itemView.findViewById(R.id.txt_time);
            cd_tasks=itemView.findViewById(R.id.cd_task);


        }
    }
}
