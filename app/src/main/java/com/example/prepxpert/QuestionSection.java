package com.example.prepxpert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import com.example.prepxpert.data.MyDbHandler;
import com.google.ai.client.generativeai.java.ChatFutures;

import java.util.ArrayList;
import java.util.Locale;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutionException;
public class QuestionSection extends AppCompatActivity {

    int startcam=0;

    String[] permission={"android.permission.CAMERA","android.permission.RECORD_AUDIO"};
    private PreviewView previewView;
    String answer1="",ans2="",ans3="",ans4="",ans5="";
    String question1="",que2="",que3="",que4="",que5="";
    String userans1="";
    TextView quesdef1;
    Button submitbtn;
    ImageView prevbtn,nextbtn,recordbtn;
    private ChatFutures chatModel;
    String role,desc,user,userid;
    int yrs;
    TextView displayans;
    private SpeechRecognizer speechRecognizer;
    private boolean isRecording = false;
    private DatabaseReference database;
    static final int REQUEST_CODE=123;
    private static final int RECORD_AUDIO_REQUEST_CODE = 101,CAMERA_REQUEST_CODE=100;  // or any unique integer


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_section);

        Intent intent=getIntent();
        user=intent.getStringExtra("username");
        userid=intent.getStringExtra("userid");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permission,80);
        }

        //checkPermission();

        quesdef1=findViewById(R.id.quesdef1);
        //prevbtn=findViewById(R.id.prevbtn);
        nextbtn=findViewById(R.id.nextbtn);
        displayans=findViewById(R.id.displaytext);
        recordbtn=findViewById(R.id.recordbtn);
        submitbtn=findViewById(R.id.submitansbtn);
        previewView = findViewById(R.id.previewView);

        // In AddNewInterview.java
        MyDbHandler dbHelper = new MyDbHandler(this);
        String ques1 = dbHelper.getDatabyId(userid,"ques1");

        if (ques1 != null) {
            Toast.makeText(this, "Question 1: " + ques1, Toast.LENGTH_SHORT).show();
            quesdef1.setText(ques1);
        } else {
            quesdef1.setText("Not found");
            Toast.makeText(this, "No question found for this user ID", Toast.LENGTH_SHORT).show();
        }


        /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference("jobdetails");
        //Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String ques = snapshot.child(user).child(userid).child("que1").getValue(String.class);
                    quesdef1.setText(ques);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        //database = FirebaseDatabase.getInstance().getReference("interviews");
        //database = FirebaseDatabase.getInstance().getReference("jobdetails").child(user).child(userid);
        // Retrieve data
        //retrieveInterviewData();

        //quesdef1.setText(question1);


        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            startCamera();
        }*/



        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);  // Enable partial results
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000); // 5 seconds of silence
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000); // Min length for recognition


        /*recordbtn.setOnClickListener(v -> {
            if (isRecording) {
                // Stop recording
                speechRecognizer.stopListening();
                isRecording = false;
                recordbtn.setText("Start Recording");
            } else {
                // Start recording
                speechRecognizer.startListening(speechIntent);
                isRecording = true;
                recordbtn.setText("Stop Recording");
            }
        });*/

        recordbtn.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);

            }else {
                    if (isRecording) {
                        // Stop recording
                        speechRecognizer.stopListening();
                        isRecording = false;
                        recordbtn.setImageResource(R.drawable.microphone);

                    } else {
                        // Start recording
                        speechRecognizer.startListening(speechIntent);
                        isRecording = true;
                        recordbtn.setImageResource(R.drawable.stop);

                    }
            }


                /*if (isRecording) {
                    // Stop recording
                    speechRecognizer.stopListening();
                    isRecording = false;
                    recordbtn.setImageResource(R.drawable.microphone);

                } else {
                    // Start recording
                    speechRecognizer.startListening(speechIntent);
                    isRecording = true;
                    recordbtn.setImageResource(R.drawable.stop);

                }*/

        });



        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuestionSection.this,QuestionSection2.class);
                intent.putExtra("userid",userid);
                intent.putExtra("username",user);
                intent.putExtra("userans1",userans1);
                //Toast.makeText(QuestionSection.this, "Ans passed", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // Set up recognition listener
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                // Do something when ready to start listening
            }

            @Override
            public void onBeginningOfSpeech() {
                displayans.setText("");
                // Do something when speech begins
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // Do something with the volume
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // Do something with buffer
            }

            @Override
            public void onEndOfSpeech() {
                // Do something when speech ends
                //recordbtn.setText("keep speaking");
            }

            @Override
            public void onError(int error) {
                // Handle errors
                Toast.makeText(QuestionSection.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    // Display the speech text
                    displayans.setText(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> partialMatches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (partialMatches != null && !partialMatches.isEmpty()) {
                    displayans.setText(partialMatches.get(0)); // Real-time transcription as the user speaks
                }
                // Handle partial results if needed
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // Handle other events if needed
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userans1=displayans.getText().toString();
                if(!userans1.isEmpty()){
                    Toast.makeText(QuestionSection.this, "Answer Saved!!", Toast.LENGTH_SHORT).show();
                    submitbtn.setBackgroundColor(getResources().getColor(R.color.white));
                    submitbtn.setEnabled(false);
                }
                else {
                    Toast.makeText(QuestionSection.this, "Unable to save Answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) + ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(QuestionSection.this, Manifest.permission.CAMERA)
                && ActivityCompat.shouldShowRequestPermissionRationale(QuestionSection.this, Manifest.permission.RECORD_AUDIO)){

                    Toast.makeText(this, "Required per", Toast.LENGTH_SHORT).show();

                    /*AlertDialog.Builder builder=new AlertDialog.Builder(QuestionSection.this);

                    builder.setTitle("Permissions Required !!");
                    builder.setMessage("Kindly grant CAMERA and MICROPHONE permissions in order to proceed further.");
                    builder.setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(QuestionSection.this,
                                    new String[]{
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.RECORD_AUDIO
                                    },REQUEST_CODE);
                        }
                    });

                    builder.setNegativeButton("CANCEL",null);

                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();*/
                }
                else{
                    ActivityCompat.requestPermissions(QuestionSection.this,
                            new String[]{
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO
                            },REQUEST_CODE);
                }
            }else {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  // Call the superclass implementation

        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the camera
                startCamera();
            } else {
                // Permission denied, you can show a message to the user if needed
                Toast.makeText(this, "Camera permission is required to use the camera",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    /*protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
    }*/
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    private void retrieveInterviewData() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through all the interviews for this user
                for (DataSnapshot interviewSnapshot : dataSnapshot.getChildren()) {
                    // Extract each interview as a JobDetails object
                    JobDetails jobDetails = interviewSnapshot.getValue(JobDetails.class);

                    if (jobDetails != null) {
                        // Now you can access the interview data
                        String jobRole = jobDetails.getJobrole();
                        question1 = jobDetails.getQue1();
                        String answer1 = jobDetails.getAns1();

                        System.out.println("Job Role: " + jobRole);
                        System.out.println("Question 1: " + question1);
                        System.out.println("Answer 1: " + answer1);

                        // Continue accessing other questions and answers if needed
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==80){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED){
                if(startcam==0){
                    startCamera();
                    startcam=1;
                }

                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }
            else{
                showPermissionDeniedDialog();
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This app requires Camera and Audio Recording permissions to function. Please enable them in Settings.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    finish();  // Optionally, close the activity if permission is essential
                })
                .create()
                .show();
    }
}