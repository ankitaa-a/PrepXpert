package com.example.prepxpert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prepxpert.data.MyDbHandler;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GeneratingResult extends AppCompatActivity {
    ProgressBar progressBar;
    TextView gentext;
    String textques=" depends on question and user answer give us the feedback and areas of improvement and how to improve our answer in 3-5 lines in JSON format with rating between 1-5 field and feedback field as feedback and rating";
    String ques1, ques2, ques3, ques4, ques5, userans1, userans4, userans2, userans3, userans5;
    String user, userid;
    int rat1, rat2, rat3, rat4, rat5;
    String feed1, feed2, feed3, feed4, feed5;
    int cnt = 1;
    private ChatFutures chatModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating_result);

        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        userid = intent.getStringExtra("userid");

        progressBar = findViewById(R.id.progressBar);
        gentext=findViewById(R.id.gentext);
        progressBar.setVisibility(View.VISIBLE);

        MyDbHandler dbHelper = new MyDbHandler(this);
        ques1 = dbHelper.getDatabyId(userid,"ques1");
        ques2 = dbHelper.getDatabyId(userid,"ques2");
        ques3 = dbHelper.getDatabyId(userid,"ques3");
        ques4 = dbHelper.getDatabyId(userid,"ques4");
        ques5 = dbHelper.getDatabyId(userid,"ques5");
        userans1 = dbHelper.getDatabyId(userid,"userans1");
        userans2 = dbHelper.getDatabyId(userid,"userans2");
        userans3 = dbHelper.getDatabyId(userid,"userans3");
        userans4 = dbHelper.getDatabyId(userid,"userans4");
        userans5 = dbHelper.getDatabyId(userid,"userans5");

        processFeedbackAndRatings();

        /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference("jobdetails");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ques1 = snapshot.child(user).child(userid).child("que1").getValue(String.class);
                    ques2 = snapshot.child(user).child(userid).child("que2").getValue(String.class);
                    ques3 = snapshot.child(user).child(userid).child("que3").getValue(String.class);
                    ques4 = snapshot.child(user).child(userid).child("que4").getValue(String.class);
                    ques5 = snapshot.child(user).child(userid).child("que5").getValue(String.class);

                    userans1 = snapshot.child(user).child(userid).child("userans1").getValue(String.class);
                    userans2 = snapshot.child(user).child(userid).child("userans2").getValue(String.class);
                    userans3 = snapshot.child(user).child(userid).child("userans3").getValue(String.class);
                    userans4 = snapshot.child(user).child(userid).child("userans4").getValue(String.class);
                    userans5 = snapshot.child(user).child(userid).child("userans5").getValue(String.class);

                    processFeedbackAndRatings();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });*/
    }

    private void processFeedbackAndRatings() {
        // Sequentially call geminiResp one by one to avoid overlapping issues
        chatModel=getChatModel();
        geminiResp(1, "question: " + ques1 + ", UserAnswer: " + userans1+textques);
    }

    private ChatFutures getChatModel(){
        GeminiResp model=new GeminiResp();
        GenerativeModelFutures modelFutures=model.getModel();

        return modelFutures.startChat();
    }

    private void geminiResp(int count, String query) {
        GeminiResp.getResponse(chatModel, query, new ResponseCallback() {
            @Override
            public void onResponse(String response) {
                String jsonString = response.replace("```json", "").replace("```", "");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String feedback = jsonObject.getString("feedback");
                    int rating = jsonObject.getInt("rating");

                    // Save feedback and rating for the current question
                    saveFeedbackAndRating(count, feedback, rating);
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                throwable.printStackTrace();
            }
        });
    }

    /*private void saveFeedbackAndRating(int count, String feedback, int rating) {
        DatabaseReference userAnswersRef = FirebaseDatabase.getInstance().getReference("jobdetails")
                .child(user)
                .child(userid);

        Map<String, Object> userAnswersUpdates = new HashMap<>();
        switch (count) {
            case 1:
                MyDbHandler dbHelper = new MyDbHandler(GeneratingResult.this);
                int rowsAffected = dbHelper.updateFeedRat(userid, feedback,"feed1",rating,"rat1");

                if (rowsAffected > 0) {
                    Toast.makeText(GeneratingResult.this, "Details updated successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(GeneratingResult.this, "Update failed", Toast.LENGTH_SHORT).show();
                    // Handle any errors
                }
                //userAnswersUpdates.put("feed1", feedback);
                //userAnswersUpdates.put("rat1", rating);
                gentext.setText("Generating Rating");
                break;
            case 2:
                MyDbHandler dbHelper2 = new MyDbHandler(GeneratingResult.this);
                int rowsAffected2 = dbHelper2.updateFeedRat(userid, feedback,"feed2",rating,"rat2");

                if (rowsAffected2 > 0) {
                    Toast.makeText(GeneratingResult.this, "Details updated successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(GeneratingResult.this, "Update failed", Toast.LENGTH_SHORT).show();
                    // Handle any errors
                }
                //userAnswersUpdates.put("feed2", feedback);
                //userAnswersUpdates.put("rat2", rating);
                gentext.setText("Generating Feedback");
                break;
            case 3:
                MyDbHandler dbHelper3 = new MyDbHandler(GeneratingResult.this);
                int rowsAffected3 = dbHelper3.updateFeedRat(userid, feedback,"feed3",rating,"rat3");

                if (rowsAffected3 > 0) {
                    Toast.makeText(GeneratingResult.this, "Details updated successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(GeneratingResult.this, "Update failed", Toast.LENGTH_SHORT).show();
                    // Handle any errors
                }
                //userAnswersUpdates.put("feed3", feedback);
                //userAnswersUpdates.put("rat3", rating);
                gentext.setText("Processing..");
                break;
            case 4:
                MyDbHandler dbHelper4 = new MyDbHandler(GeneratingResult.this);
                int rowsAffected4 = dbHelper4.updateFeedRat(userid, feedback,"feed4",rating,"rat4");

                if (rowsAffected4 > 0) {
                    Toast.makeText(GeneratingResult.this, "Details updated successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(GeneratingResult.this, "Update failed", Toast.LENGTH_SHORT).show();
                    // Handle any errors
                }
                //userAnswersUpdates.put("feed4", feedback);
                //userAnswersUpdates.put("rat4", rating);
                gentext.setText("Loading..");
                break;
            case 5:
                MyDbHandler dbHelper5 = new MyDbHandler(GeneratingResult.this);
                int rowsAffected5 = dbHelper5.updateFeedRat(userid, feedback,"feed5",rating,"rat5");

                if (rowsAffected5 > 0) {
                    Toast.makeText(GeneratingResult.this, "Details updated successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(GeneratingResult.this, "Update failed", Toast.LENGTH_SHORT).show();
                    // Handle any errors
                }
                //userAnswersUpdates.put("feed5", feedback);
                //userAnswersUpdates.put("rat5", rating);
                gentext.setText("Almost there...");
                changeActivity();
                break;
        }

        userAnswersRef.updateChildren(userAnswersUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("User answers updated successfully.");
                        if (count < 5) {
                            // Call the next geminiResp once the previous one is done
                            switch (count) {
                                case 1:
                                    chatModel=getChatModel();
                                    geminiResp(2, "question: " + ques2 + ", UserAnswer: " + userans2+textques);
                                    break;
                                case 2:
                                    chatModel=getChatModel();
                                    geminiResp(3, "question: " + ques3 + ", UserAnswer: " + userans3+textques);
                                    break;
                                case 3:
                                    chatModel=getChatModel();
                                    geminiResp(4, "question: " + ques4 + ", UserAnswer: " + userans4+textques);
                                    break;
                                case 4:
                                    chatModel=getChatModel();
                                    geminiResp(5, "question: " + ques5 + ", UserAnswer: " + userans5+textques);
                                    break;
                            }
                        } else {
                            progressBar.setVisibility(View.GONE); // All done
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        System.out.println("Failed to update user answers: " + e.getMessage());
                    }
                });
    }*/

    public void changeActivity(){
        Intent intent=new Intent(GeneratingResult.this,ResultActivity.class);
        intent.putExtra("userid",userid);
        intent.putExtra("username",user);
        startActivity(intent);
        finish();
    }

    private void saveFeedbackAndRating(int count, String feedback, int rating) {
        MyDbHandler dbHelper = new MyDbHandler(GeneratingResult.this);
        int rowsAffected = 0;

        switch (count) {
            case 1:
                rowsAffected = dbHelper.updateFeedRat(userid, feedback, "feed1", rating, "rat1");
                gentext.setText("Generating Rating");
                break;
            case 2:
                rowsAffected = dbHelper.updateFeedRat(userid, feedback, "feed2", rating, "rat2");
                gentext.setText("Generating Feedback");
                break;
            case 3:
                rowsAffected = dbHelper.updateFeedRat(userid, feedback, "feed3", rating, "rat3");
                gentext.setText("Processing..");
                break;
            case 4:
                rowsAffected = dbHelper.updateFeedRat(userid, feedback, "feed4", rating, "rat4");
                gentext.setText("Loading..");
                break;
            case 5:
                rowsAffected = dbHelper.updateFeedRat(userid, feedback, "feed5", rating, "rat5");
                gentext.setText("Almost there...");
                changeActivity();
                break;
        }

        if (rowsAffected > 0) {
            Toast.makeText(GeneratingResult.this, "Details updated successfully", Toast.LENGTH_SHORT).show();
            if (count < 5) {
                switch (count) {
                    case 1:
                        geminiResp(2, "question: " + ques2 + ", UserAnswer: " + userans2 + textques);
                        break;
                    case 2:
                        geminiResp(3, "question: " + ques3 + ", UserAnswer: " + userans3 + textques);
                        break;
                    case 3:
                        geminiResp(4, "question: " + ques4 + ", UserAnswer: " + userans4 + textques);
                        break;
                    case 4:
                        geminiResp(5, "question: " + ques5 + ", UserAnswer: " + userans5 + textques);
                        break;
                }
            } else {
                progressBar.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(GeneratingResult.this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
