package com.example.prepxpert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import java.util.concurrent.ExecutionException;
public class QuestionSection4 extends AppCompatActivity {
    private PreviewView previewView;
    String answer4="";
    String question4="";
    String userans4="";
    TextView quesdef4;
    Button submitbtn;
    ImageView prevbtn,nextbtn,recordbtn;
    private ChatFutures chatModel;
    String userans2,userans1,userans3,user,userid;
    int yrs;
    TextView displayans;
    private SpeechRecognizer speechRecognizer;
    private boolean isRecording = false;
    private static final int RECORD_AUDIO_REQUEST_CODE = 101,CAMERA_REQUEST_CODE=100;  // or any unique integer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_section4);

        Intent intent=getIntent();
        user=intent.getStringExtra("username");
        userid=intent.getStringExtra("userid");
        userans1=intent.getStringExtra("userans1");
        userans2=intent.getStringExtra("userans2");
        userans3=intent.getStringExtra("userans3");


        checkPermission();

        quesdef4=findViewById(R.id.quesdef4);
        prevbtn=findViewById(R.id.prevbtn4);
        nextbtn=findViewById(R.id.nextbtn4);
        displayans=findViewById(R.id.displaytext4);
        recordbtn=findViewById(R.id.recordbtn4);
        submitbtn=findViewById(R.id.submitansbtn4);
        previewView = findViewById(R.id.previewView4);

        MyDbHandler dbHelper = new MyDbHandler(this);
        String ques1 = dbHelper.getDatabyId(userid,"ques4");

        if (ques1 != null) {
            //Toast.makeText(this, "Question 1: " + ques1, Toast.LENGTH_SHORT).show();
            quesdef4.setText(ques1);
        } else {
            quesdef4.setText("Not found");
            Toast.makeText(this, "No question found for this user ID", Toast.LENGTH_SHORT).show();
        }

        /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference("jobdetails");
        //Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String ques = snapshot.child(user).child(userid).child("que4").getValue(String.class);
                    quesdef4.setText(ques);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            startCamera();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);  // Enable partial results
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000); // 5 seconds of silence
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000);

        recordbtn.setOnClickListener(v -> {
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
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuestionSection4.this,QuestionSection5.class);
                intent.putExtra("userid",userid);
                intent.putExtra("username",user);
                intent.putExtra("userans1",userans1);
                intent.putExtra("userans2",userans2);
                intent.putExtra("userans3",userans3);
                intent.putExtra("userans4",userans4);
                startActivity(intent);
            }
        });

        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuestionSection4.this,QuestionSection3.class));
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
            }

            @Override
            public void onError(int error) {
                // Handle errors
                Toast.makeText(QuestionSection4.this, "Error: " + error, Toast.LENGTH_SHORT).show();
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
                userans4=displayans.getText().toString();
                if(!userans4.isEmpty()){
                    Toast.makeText(QuestionSection4.this, "Answer Saved!!", Toast.LENGTH_SHORT).show();
                    submitbtn.setBackgroundColor(getResources().getColor(R.color.white));
                    submitbtn.setEnabled(false);
                }
                else {
                    Toast.makeText(QuestionSection4.this, "Unable to save Answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
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

}