package cpc.class_planner.sam.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cpc.class_planner.sam.R;
import cpc.class_planner.sam.model.Routine;

public class RoutineItemVIewAdapter extends BaseAdapter {
    private ArrayList<Routine> dataSet; // list of data
    private static ViewHolder viewHolder;
    private LayoutInflater inflater; // Layout inflater
    Context context;


    // constructor
    public RoutineItemVIewAdapter(ArrayList<Routine> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataSet.size(); // depends on the data set
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Routine routine = dataSet.get(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_routine, parent, false);
            viewHolder.classStartingPeriod = (TextView) convertView.findViewById(R.id.item_routine_starting_time);
            viewHolder.courseName = (TextView) convertView.findViewById(R.id.item_routine_course_name);
            viewHolder.courseCode = (TextView) convertView.findViewById(R.id.item_routine_course_code);
            viewHolder.courseSection = (TextView) convertView.findViewById(R.id.item_routine_course_code);
            viewHolder.courseRoom = (TextView) convertView.findViewById(R.id.item_routine_course_section);
            viewHolder.courseTeacher = (TextView) convertView.findViewById(R.id.item_routine_course_teacher);

        }
        // setting everything up
        viewHolder.classStartingPeriod.setText(routine.getStartingTime());
        viewHolder.courseName.setText(routine.getCourseTitle());
        viewHolder.courseCode.setText(routine.getCourseCode());
        viewHolder.courseSection.setText(routine.getCourseCode());
        viewHolder.courseRoom.setText(routine.getRoomNo());
        viewHolder.courseTeacher.setText(routine.getTeacher());

        // Return the rendered view
        return convertView;
    }


    private static class ViewHolder{
        TextView classStartingPeriod;
        TextView courseName;
        TextView courseCode;
        TextView courseSection;
        TextView courseRoom;
        TextView courseTeacher;
    }
}
