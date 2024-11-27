package com.example.prepxpert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prepxpert.data.MyDbHandler;
import com.example.prepxpert.model.Sqljobdetails;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNewInterview extends AppCompatActivity {

    String[] permission={"android.permission.CAMERA","android.permission.RECORD_AUDIO"};
    TextView newrole,newdesc,newyrs,newuser;
    String ans1="",ans2="",ans3="",ans4="",ans5="";
    String que1="",que2="",que3="",que4="",que5="";
    String role,desc,user,userid;
    private ChatFutures chatModel;
    int yrs;
    long date;
    Button startintbtn;
    private DatabaseReference database;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_interview);

        database = FirebaseDatabase.getInstance().getReference("jobdetails");
        //DatabaseReference reference=database.getReference("jobdetails");

        newrole=findViewById(R.id.rolejob);
        newdesc=findViewById(R.id.descjob);
        newyrs=findViewById(R.id.expyrs);
        startintbtn=findViewById(R.id.startintbtn);
        progressBar = findViewById(R.id.progressBar);
        progressBar = findViewById(R.id.progressBar);

        startintbtn.setBackgroundColor(getResources().getColor(R.color.grey));
        startintbtn.setTextColor(getResources().getColor(R.color.darkgrey));
        startintbtn.setEnabled(false);  // Disable the button initially

        Intent intent=getIntent();
        user=intent.getStringExtra("username");
        userid=intent.getStringExtra("userid");
        role=intent.getStringExtra("jobrole");
        desc=intent.getStringExtra("jobdesc");
        yrs=intent.getIntExtra("yrsofexp",0);
        date=intent.getLongExtra("createdate",0);

        if(role!=null){
            newrole.setText("Job Role: "+role);
        }
        if(desc!=null){
            newdesc.setText("Job Description: "+desc);
        }
        newyrs.setText("Experience(yrs): "+yrs);

        String query1="Job Position: "+role+", job description: "+desc+", job experience: "+yrs+", depends on this information give 5 interview question and its answers in json format . give question and answer as fields in json as fields question1, answer1, question2 .. and so on";

        chatModel=getChatModel();
        progressBar.setVisibility(View.VISIBLE);
        geminiResp(query1);

        startintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String query1="Job Position: "+role+", job description: "+desc+", job experience: "+yrs+", depends on this information give 5 interview question and its answers in json format . give question and answer as fields in json as fields question1, answer1, question2 .. and so on";

                chatModel=getChatModel();

                geminiResp(query1);*/

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    requestPermissions(permission,80);
                }*/

                Intent intent=new Intent(AddNewInterview.this,QuestionSection.class);
                intent.putExtra("userid",userid);
                intent.putExtra("username",user);
                startActivity(intent);
            }
        });



    }

    private ChatFutures getChatModel(){
        GeminiResp model=new GeminiResp();
        GenerativeModelFutures modelFutures=model.getModel();

        return modelFutures.startChat();
    }

    public void geminiResp(String query){

        GeminiResp.getResponse(chatModel, query, new ResponseCallback() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddNewInterview.this, "Inside gemini", Toast.LENGTH_SHORT).show();
                String jsonString = response.replace("```json","").replace("```","");

                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(jsonObject);
                try {
                    que1=jsonObject.getString("question1");
                    System.out.println(que1);
                    ans1=jsonObject.getString("answer1");
                    System.out.println(ans1);

                    que2=jsonObject.getString("question2");
                    System.out.println(que2);
                    ans2=jsonObject.getString("answer2");
                    System.out.println(ans2);

                    que3=jsonObject.getString("question3");
                    System.out.println(que3);
                    ans3=jsonObject.getString("answer3");
                    System.out.println(ans3);

                    que4=jsonObject.getString("question4");
                    System.out.println(que4);
                    ans4=jsonObject.getString("answer4");
                    System.out.println(ans4);

                    que5=jsonObject.getString("question5");
                    System.out.println(que5);
                    ans5=jsonObject.getString("answer5");
                    System.out.println(ans5);

                    MyDbHandler dbHelper = new MyDbHandler(AddNewInterview.this);
                    int rowsAffected = dbHelper.updateJobDetails(userid, ans1, ans2, ans3, ans4, ans5,que1, que2, que3, que4, que5);

                    if (rowsAffected > 0) {
                        Toast.makeText(AddNewInterview.this, "Details updated successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        startintbtn.setBackground(ContextCompat.getDrawable(AddNewInterview.this, R.drawable.beginint_button));;
                        startintbtn.setTextColor(getResources().getColor(R.color.white));
                        startintbtn.setEnabled(true);
                    } else {
                        Toast.makeText(AddNewInterview.this, "Update failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddNewInterview.this, "Unable to Generate Questions. Please try again later !!", Toast.LENGTH_SHORT).show();
                        // Handle any errors
                    }


                    /*JobDetails jobDetails = new JobDetails(user, role, desc, yrs,date,userid);
                    jobDetails.JobQues(que1, que2, que3, que4, que5);
                    jobDetails.JobAns(ans1, ans2, ans3, ans4, ans5);

                    //database.child(user).child(userid).setValue(jobDetails);
                    database.child(user).child(userid).setValue(jobDetails)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Enable the submit button after successful data storage
                                    progressBar.setVisibility(View.GONE);
                                    startintbtn.setBackground(ContextCompat.getDrawable(AddNewInterview.this, R.drawable.beginint_button));;
                                    startintbtn.setTextColor(getResources().getColor(R.color.white));
                                    startintbtn.setEnabled(true);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(AddNewInterview.this, "Unable to Generate Questions. Please try again later !!", Toast.LENGTH_SHORT).show();
                                    // Handle any errors
                                }
                            });*/

                    //sample.setText(ques);
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddNewInterview.this, "inside catch", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddNewInterview.this, "inside onerror", Toast.LENGTH_SHORT).show();
                //quesdef1.setText("Unable to Generate Questions!! Try Again");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==80){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                startintbtn.setEnabled(true);
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddNewInterview.this,QuestionSection.class);
                intent.putExtra("userid",userid);
                intent.putExtra("username",user);
                startActivity(intent);
            }
            else{
                startintbtn.setEnabled(false);

            }
        }
    }

}