package com.example.prepxpert.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prepxpert.model.Sqljobdetails;
import com.example.prepxpert.params.Params;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {

    public MyDbHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + Params.TABLE_NAME + " ("
                + Params.KEY_ID + " TEXT PRIMARY KEY, "
                + Params.KEY_EMAIL + " TEXT , "
                + Params.KEY_USERNAME + " TEXT, "
                + "jobrole TEXT, "
                + "jobdesc TEXT, "
                + "yrsexp INTEGER, "
                + "ques1 TEXT, ques2 TEXT, ques3 TEXT, ques4 TEXT, ques5 TEXT, "
                + "ans1 TEXT, ans2 TEXT, ans3 TEXT, ans4 TEXT, ans5 TEXT, "
                + "feed1 TEXT, feed2 TEXT, feed3 TEXT, feed4 TEXT, feed5 TEXT, "
                + "userans1 TEXT, userans2 TEXT, userans3 TEXT, userans4 TEXT, userans5 TEXT, "
                + "rat1 INTEGER, rat2 INTEGER, rat3 INTEGER, rat4 INTEGER, rat5 INTEGER, "
                + "createddate LONG"
                + ")";

        db.execSQL(create);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void adddetails(Sqljobdetails sqljobdetails){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Params.KEY_ID,sqljobdetails.getId());
        values.put(Params.KEY_EMAIL, sqljobdetails.getEmail());
        values.put(Params.KEY_USERNAME, sqljobdetails.getUsername());
        values.put("jobrole", sqljobdetails.getJobrole());
        values.put("jobdesc", sqljobdetails.getJobdesc());
        values.put("yrsexp", sqljobdetails.getYrsofexp());  // Experience years
        values.put("createddate", sqljobdetails.getCreatedDate());

        db.insert(Params.TABLE_NAME,null,values);
        db.close();
    }

    // In your DatabaseHelper class (assuming you have one)
    public int updateJobDetails(String id, String ans1, String ans2, String ans3, String ans4, String ans5,
                                String ques1, String ques2, String ques3, String ques4, String ques5) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Update columns
        values.put("ans1", ans1);
        values.put("ans2", ans2);
        values.put("ans3", ans3);
        values.put("ans4", ans4);
        values.put("ans5", ans5);
        values.put("ques1", ques1);
        values.put("ques2", ques2);
        values.put("ques3", ques3);
        values.put("ques4", ques4);
        values.put("ques5", ques5);


        // Update row based on ID
        String whereClause = Params.KEY_ID + " = ?";
        String[] whereArgs = new String[]{id};

        return db.update(Params.TABLE_NAME, values, whereClause, whereArgs);
    }

    // In MyDbHandler.java

    public String getDatabyId(String userId, String datatype) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Value = null;
        Cursor cursor = db.rawQuery("SELECT "+datatype+" FROM " + Params.TABLE_NAME+" WHERE id=?", new String[]{userId});

        if (cursor != null && cursor.moveToFirst()) {
            int Index = cursor.getColumnIndex(datatype);
            if (Index >= 0) {  // Ensure column exists
                Value = cursor.getString(Index);
            } else {
                Log.e("DatabaseError", "Column 'ques1' does not exist in the table.");
            }
            cursor.close();
        } else {
            Log.e("DatabaseError", "No data found for userid: " + userId);
        }
        db.close();
        return Value;

    }

    public int updateUserAns(String id, String userans1, String userans2, String userans3, String userans4, String userans5) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Update columns
        values.put("userans1", userans1);
        values.put("userans2", userans2);
        values.put("userans3", userans3);
        values.put("userans4", userans4);
        values.put("userans5", userans5);

        // Update row based on ID
        String whereClause = Params.KEY_ID + " = ?";
        String[] whereArgs = new String[]{id};

        return db.update(Params.TABLE_NAME, values, whereClause, whereArgs);
    }

    public int updateFeedRat(String id, String feed1, String feed, int rat1,String rat) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Update columns
        values.put(feed, feed1);
        values.put(rat, rat1);

        // Update row based on ID
        String whereClause = Params.KEY_ID + " = ?";
        String[] whereArgs = new String[]{id};

        return db.update(Params.TABLE_NAME, values, whereClause, whereArgs);
    }

    public int getRatById(String userId,String rat) {
        SQLiteDatabase db = this.getReadableDatabase();
        int ratval = -1;
        Cursor cursor = db.rawQuery("SELECT "+rat+" FROM " + Params.TABLE_NAME+" WHERE id=?", new String[]{userId});

        if (cursor != null && cursor.moveToFirst()) {
            int ques1Index = cursor.getColumnIndex(rat);
            if (ques1Index >= 0) {  // Ensure column exists
                ratval = cursor.getInt(ques1Index);
            } else {
                Log.e("DatabaseError", "Column 'ques1' does not exist in the table.");
            }
            cursor.close();
        } else {
            Log.e("DatabaseError", "No data found for userid: " + userId);
        }
        db.close();
        return ratval;

    }

    public List<Sqljobdetails> getInterviewsForUser(String email) {
        List<Sqljobdetails> jobDetailsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+Params.TABLE_NAME+" WHERE "+Params.KEY_EMAIL+" = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                Sqljobdetails jobDetails = new Sqljobdetails();


                // Check for column indices
                int jobRoleIndex = cursor.getColumnIndex("jobrole");
                int jobDescIndex = cursor.getColumnIndex("jobdesc");
                int yearsExpIndex = cursor.getColumnIndex("yrsexp");
                int createdDateIndex = cursor.getColumnIndex("createddate");
                //int interviewIdIndex = cursor.getColumnIndex("interviewid");

                if (jobRoleIndex >= 0) jobDetails.setJobrole(cursor.getString(jobRoleIndex));
                if (jobDescIndex >= 0) jobDetails.setJobdesc(cursor.getString(jobDescIndex));
                if (yearsExpIndex >= 0) jobDetails.setYrsofexp(cursor.getInt(yearsExpIndex));
                if (createdDateIndex >= 0) jobDetails.setCreatedDate(cursor.getLong(createdDateIndex));
                //if (interviewIdIndex >= 0) jobDetails.setInterviewid(cursor.getString(interviewIdIndex));

                jobDetailsList.add(jobDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return jobDetailsList;
    }

}
