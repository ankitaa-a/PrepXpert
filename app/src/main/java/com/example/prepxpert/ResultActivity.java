package com.example.prepxpert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    String ques1, ques2, ques3, ques4, ques5, userans1, userans4, userans2, userans3, userans5;
    int rat1, rat2, rat3, rat4, rat5;
    String ans2,ans1,ans3,ans4,ans5;
    String feed1, feed2, feed3, feed4, feed5;
    String user,userid;
    List<String> groupList;
    List<String> childList;
    Map<String,List<String>> resultList;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    Map<String, List<String>> mobileCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        userid = intent.getStringExtra("userid");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("jobdetails");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ques1 = snapshot.child(user).child(userid).child("que1").getValue(String.class);
                    ques2 = snapshot.child(user).child(userid).child("que2").getValue(String.class);
                    ques3 = snapshot.child(user).child(userid).child("que3").getValue(String.class);
                    ques4 = snapshot.child(user).child(userid).child("que4").getValue(String.class);
                    ques5 = snapshot.child(user).child(userid).child("que5").getValue(String.class);

                    ans1 = snapshot.child(user).child(userid).child("ans1").getValue(String.class);
                    ans2 = snapshot.child(user).child(userid).child("ans2").getValue(String.class);
                    ans3 = snapshot.child(user).child(userid).child("ans3").getValue(String.class);
                    ans4 = snapshot.child(user).child(userid).child("ans4").getValue(String.class);
                    ans5 = snapshot.child(user).child(userid).child("ans5").getValue(String.class);

                    userans1 = snapshot.child(user).child(userid).child("userans1").getValue(String.class);
                    userans2 = snapshot.child(user).child(userid).child("userans2").getValue(String.class);
                    userans3 = snapshot.child(user).child(userid).child("userans3").getValue(String.class);
                    userans4 = snapshot.child(user).child(userid).child("userans4").getValue(String.class);
                    userans5 = snapshot.child(user).child(userid).child("userans5").getValue(String.class);

                    feed1 = snapshot.child(user).child(userid).child("feed1").getValue(String.class);
                    feed2 = snapshot.child(user).child(userid).child("feed2").getValue(String.class);
                    feed3 = snapshot.child(user).child(userid).child("feed3").getValue(String.class);
                    feed4 = snapshot.child(user).child(userid).child("feed4").getValue(String.class);
                    feed5 = snapshot.child(user).child(userid).child("feed5").getValue(String.class);

                    rat1 = snapshot.child(user).child(userid).child("rat1").getValue(Integer.class);
                    rat2 = snapshot.child(user).child(userid).child("rat2").getValue(Integer.class);
                    rat3 = snapshot.child(user).child(userid).child("rat3").getValue(Integer.class);
                    rat4 = snapshot.child(user).child(userid).child("rat4").getValue(Integer.class);
                    rat5 = snapshot.child(user).child(userid).child("rat5").getValue(Integer.class);

                    createGroupList();
                    createCollection();

                    expandableListView=findViewById(R.id.expandableListView);
                    expandableListAdapter=new MyExpandableListAdapter(ResultActivity.this,groupList,resultList);
                    expandableListView.setAdapter(expandableListAdapter);
        /*expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition=-1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(lastExpandedPosition!=-1 && groupPosition!=lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition=groupPosition;
            }
        });*/
                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            String selected=expandableListAdapter.getChild(groupPosition,childPosition).toString();
                            Toast.makeText(ResultActivity.this, "selected: "+selected, Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                System.out.println("Error occured in showing result");
            }
        });


    }

    private void createCollection(){
        String[] q1={"Rating: "+Integer.toString(rat1),"YourAns: "+userans1,"AI Ans: "+ans1,"Feedback:"+feed1};
        String[] q2={"Rating: "+Integer.toString(rat2),"YourAns: "+userans2,"AI Ans: "+ans2,"Feedback:"+feed2};
        String[] q3={"Rating: "+Integer.toString(rat3),"YourAns: "+userans3,"AI Ans: "+ans3,"Feedback:"+feed3};
        String[] q4={"Rating: "+Integer.toString(rat4),"YourAns: "+userans4,"AI Ans: "+ans4,"Feedback:"+feed4};
        String[] q5={"Rating: "+Integer.toString(rat5),"YourAns: "+userans5,"AI Ans: "+ans5,"Feedback:"+feed5};

        resultList=new HashMap<String,List<String>>();

        for(String group: groupList){
            if(group.equals(ques1)){
                loadChild(q1);
            }
            if(group.equals(ques2)){
                loadChild(q2);
            }
            if(group.equals(ques3)){
                loadChild(q3);
            }
            if(group.equals(ques4)){
                loadChild(q4);
            }
            if(group.equals(ques5)){
                loadChild(q5);
            }
            resultList.put(group,childList);
        }
    }

    private void loadChild(String[] ques){
        childList=new ArrayList<>();
        for(String q: ques){
            childList.add(q);
        }
    }

    private void createGroupList(){
        groupList=new ArrayList<>();
        groupList.add(ques1);
        groupList.add(ques2);
        groupList.add(ques3);
        groupList.add(ques4);
        groupList.add(ques5);
    }

    /*private void createCollection(){
        String[] h={"hi","hi","hi","hi","hi","hi"};
        String[] hell={"hi12","hi12","hi","hi","hi","hi"};
        String[] hel={"hi12","hi12","hi","hi","hi","hi"};
        String[] ell={"hi12","hi12","hi","hi","hi","hi"};
        //String[] q3={Integer.toString(rat1),userans1,ans1,feed1};
        //String[] q={Integer.toString(rat1),userans1,ans1,feed1};
        //String[] q2={Integer.toString(rat1),userans1,ans1,feed1};

        resultList=new HashMap<String,List<String>>();

        for(String group: groupList){
            if(group.equals("hi")){
                loadChild(h);
            }
            if(group.equals("hello")){
                loadChild(hell);
            }
            if(group.equals("helo")){
                loadChild(hel);
            }
            if(group.equals("ello")){
                loadChild(ell);
            }
            resultList.put(group,childList);
        }
    }

    private void loadChild(String[] ques){
        childList=new ArrayList<>();
        for(String q: ques){
            childList.add(q);
        }
    }

    private void createGroupList(){
        groupList=new ArrayList<>();
        groupList.add("hi");
        groupList.add("hello");
        groupList.add("helo");
        groupList.add("ello");

    }

    /*private void createCollection() {
        String[] samsungModels = {"Samsung Galaxy M21", "Samsung Galaxy F41",
                "Samsung Galaxy M51", "Samsung Galaxy A50s"};
        String[] googleModels = {"Pixel 4 XL", "Pixel 3a", "Pixel 3 XL", "Pixel 3a XL",
                "Pixel 2", "Pixel 3"};
        String[] redmiModels = {"Redmi 9i", "Redmi Note 9 Pro Max", "Redmi Note 9 Pro"};
        String[] vivoModels = {"Vivo V20", "Vivo S1 Pro", "Vivo Y91i", "Vivo Y12"};
        String[] nokiaModels = {"Nokia 5.3", "Nokia 2.3", "Nokia 3.1 Plus"};
        String[] motorolaModels = { "Motorola One Fusion+", "Motorola E7 Plus", "Motorola G9"};
        mobileCollection = new HashMap<String, List<String>>();
        for(String group : groupList){
            if (group.equals("Samsung")){
                loadChild(samsungModels);
            } else if (group.equals("Google"))
                loadChild(googleModels);
            else if (group.equals("Redmi"))
                loadChild(redmiModels);
            else if (group.equals("Vivo"))
                loadChild(vivoModels);
            else if (group.equals("Nokia"))
                loadChild(nokiaModels);
            else
                loadChild(motorolaModels);
            mobileCollection.put(group, childList);
        }
    }

    private void loadChild(String[] mobileModels) {
        childList = new ArrayList<>();
        for(String model : mobileModels){
            childList.add(model);
        }
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add("Samsung");
        groupList.add("Google");
        groupList.add("Redmi");
        groupList.add("Vivo");
        groupList.add("Nokia");
        groupList.add("Motorola");
    }*/

    @Override
    public void onBackPressed() {
        // Create an intent to redirect to another activity
        Intent intent = new Intent(ResultActivity.this, DashboardActivity.class);

        // Start the new activity
        startActivity(intent);

        // Optional: finish the current activity if you don't want it to be on the back stack
        finish();

        // Disable the default back button behavior (which would go to the previous activity)
        super.onBackPressed();
    }

}