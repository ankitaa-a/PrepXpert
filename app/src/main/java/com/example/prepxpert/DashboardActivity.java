package com.example.prepxpert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ImageView logout;
    Button addbtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private TextView usernameTextView;
    private String usernamefromlogin;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME="mypref";
    private static final String KEY_USERNAME="username";
    private static final String KEY_NAME="name";
    private RecyclerView recyclerView;
    private InterviewAdapter interviewAdapter;
    private List<JobDetails> interviewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        addbtn=findViewById(R.id.addbtn);
        logout=findViewById(R.id.logoutbtn);
        usernameTextView=findViewById(R.id.usernametext);
        recyclerView = findViewById(R.id.recycler_view);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String usernamefromlogin = sharedPreferences.getString(KEY_USERNAME, null);
        String name = sharedPreferences.getString(KEY_NAME, null);

        if (usernamefromlogin != null) {
            usernameTextView.setText("Welcome Back " + usernamefromlogin);
        } else {
            usernameTextView.setText("Welcome Back");
        }

        interviewList = new ArrayList<>();
        interviewAdapter = new InterviewAdapter(interviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(interviewAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference("jobdetails").child(usernamefromlogin);

        // Fetch previous interviews
        fetchPreviousInterviews(usernamefromlogin);

        //if you want to get data from the previous activity
        /*Intent intent=getIntent();
        usernamefromlogin=intent.getStringExtra("username");

        if (usernamefromlogin != null) {
            usernameTextView.setText("Welcome Back " + usernamefromlogin);
        } else {
            usernameTextView.setText("Welcome Back");
        }*/

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clear login state
                editor.commit();

                // Redirect to login activity
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();  // Close DashboardActivity
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampleaddNewFun(usernamefromlogin);
            }
        });


    }

    private void sampleaddNewFun(String usernamelogin){
        ConstraintLayout addnewconstraintLayout=findViewById(R.id.sampleaddnew);
        View view= LayoutInflater.from(DashboardActivity.this).inflate(R.layout.sample_add_new,addnewconstraintLayout);

        Button generate=view.findViewById(R.id.generatebtn);

        AlertDialog.Builder builder=new AlertDialog.Builder(DashboardActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();

        EditText jobrolebox=view.findViewById(R.id.jobrole);
        EditText jobdescbox=view.findViewById(R.id.jobdesc);
        EditText yrexpbox=view.findViewById(R.id.yrsofexp);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference("jobdetails");

                String jobrole=jobrolebox.getText().toString();
                String jobdesc=jobdescbox.getText().toString();
                String yrofexpString=yrexpbox.getText().toString();

                if(jobrole.isEmpty()){
                    Toast.makeText(DashboardActivity.this, "Job Role cannot be void", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Default value for years of experience
                int yrofexp = 0;

// Convert to integer, handling potential issues
                if (!yrofexpString.isEmpty()) {
                    try {
                        yrofexp = Integer.parseInt(yrofexpString);// Convert string to int
                        if(yrofexp<0 || yrofexp>50){
                            Toast.makeText(DashboardActivity.this, "Enter years between 0 and 50", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(DashboardActivity.this, "Please enter a valid number for years of experience.", Toast.LENGTH_SHORT).show();
                        return;  // Exit method if conversion fails
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "Years of experience field is empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String interviewId = reference.child(usernamelogin).push().getKey();
                long createdDate = System.currentTimeMillis();

                JobDetails jobDetails = new JobDetails(usernamelogin, jobrole, jobdesc, yrofexp,createdDate,interviewId);

                // Store interview under the unique key
                reference.child(usernamelogin).child(interviewId).setValue(jobDetails);

                //JobDetails jobDetails=new JobDetails(usernamelogin,jobrole,jobdesc,yrofexp);
                //reference.child(usernamelogin).setValue(jobDetails);

                Intent intent=new Intent(DashboardActivity.this,AddNewInterview.class);

                intent.putExtra("userid",interviewId);
                intent.putExtra("username",usernamelogin);
                intent.putExtra("jobrole",jobrole);
                intent.putExtra("jobdesc",jobdesc);
                intent.putExtra("yrsofexp",yrofexp);
                intent.putExtra("createdate",createdDate);

                startActivity(intent);
                Toast.makeText(DashboardActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }

    private void fetchPreviousInterviews(String username) {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                interviewList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    JobDetails jobDetails = snapshot.getValue(JobDetails.class);
                    interviewList.add(jobDetails);
                }
                interviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashboardActivity.this, "Failed to load interviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

}