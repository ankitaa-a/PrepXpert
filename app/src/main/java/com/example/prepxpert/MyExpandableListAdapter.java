package com.example.prepxpert;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import android.content.Context;  // Correct import


import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String, List<String>> resultList;
    private List<String> groupList;

    public MyExpandableListAdapter(Context context,List<String> groupList,Map<String,List<String>> resultList){
        this.context=context;
        this.resultList=resultList;
        this.groupList=groupList;
    }

    @Override
    public int getGroupCount() {
        return resultList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return resultList.get(groupList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return resultList.get(groupList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String ques=groupList.get(groupPosition);
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);

        }
        TextView item = convertView.findViewById(R.id.groupitemtextview);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(ques);
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.chiditemtextview);
        txtListChild.setText(childText);

        // Set different drawable backgrounds for each child
        switch (childPosition) {
            case 0:
                txtListChild.setBackgroundResource(R.drawable.ratingchild_box);
                txtListChild.setTextColor(ContextCompat.getColor(context, R.color.darkyellow));// First child's background
                break;
            case 1:
                txtListChild.setBackgroundResource(R.drawable.userans_childbox);
                txtListChild.setTextColor(ContextCompat.getColor(context, R.color.midred));// Second child's background
                break;
            case 2:
                txtListChild.setBackgroundResource(R.drawable.ans_childbox);
                txtListChild.setTextColor(ContextCompat.getColor(context, R.color.midgreen));// Third child's background
                break;
            default:
                txtListChild.setBackgroundResource(R.drawable.feedbackchild_box);
                txtListChild.setTextColor(ContextCompat.getColor(context, R.color.darklight2blue));// Default background if needed
                break;
        }

        return convertView;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
