package com.example.prepxpert;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prepxpert.model.Sqljobdetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InterviewAdapter extends RecyclerView.Adapter<InterviewAdapter.ViewHolder> {

    //private List<JobDetails> interviewList;
    private List<Sqljobdetails> interviewList;

    public InterviewAdapter(List<Sqljobdetails> interviewList) {
        this.interviewList = interviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sqljobdetails jobDetails = interviewList.get(position);
        holder.jobRole.setText(jobDetails.getJobrole());
        holder.jobDesc.setText(jobDetails.getJobdesc());
        holder.yearsExp.setText("Experience(yrs): " + String.valueOf(jobDetails.getYrsofexp()));


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = sdf.format(new Date(jobDetails.getCreatedDate()));
        holder.createdDate.setText(dateString);
        /*JobDetails jobDetails = interviewList.get(position);
        holder.jobRole.setText(jobDetails.getJobrole());
        holder.jobDesc.setText(jobDetails.getJobdesc());
        holder.yearsExp.setText("Experience(yrs): "+String.valueOf(jobDetails.getYrsofexp()));

        // Convert timestamp to a readable date format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = sdf.format(new Date(jobDetails.getCreatedDate()));
        holder.createdDate.setText(dateString);*/  // Set the created date

        // Handle Start Interview button click
        holder.startInterviewButton.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), QuestionSection.class);
            intent.putExtra("username", jobDetails.getUsername()); // Pass username only
            intent.putExtra("userid", jobDetails.getId());     // Pass userid only
            holder.itemView.getContext().startActivity(intent);
        });

        // Handle See Results button click
        holder.seeResultsButton.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ResultActivity.class);
            intent.putExtra("username", jobDetails.getUsername()); // Pass username only
            intent.putExtra("userid", jobDetails.getId());     // Pass userid only
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return interviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobRole, jobDesc, yearsExp, createdDate;
        Button startInterviewButton, seeResultsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobRole = itemView.findViewById(R.id.tv_job_role);
            jobDesc = itemView.findViewById(R.id.tv_job_desc);
            yearsExp = itemView.findViewById(R.id.tv_years_exp);
            createdDate = itemView.findViewById(R.id.tv_created_date);
            startInterviewButton = itemView.findViewById(R.id.btn_start_interview);
            seeResultsButton = itemView.findViewById(R.id.btn_see_results);
        }
    }
}

