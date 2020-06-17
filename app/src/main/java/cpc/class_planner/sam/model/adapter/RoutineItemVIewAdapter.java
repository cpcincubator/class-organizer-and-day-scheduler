package cpc.class_planner.sam.model.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Calendar;

import cpc.class_planner.sam.R;
import cpc.class_planner.sam.model.Routine;
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;

public class RoutineItemVIewAdapter extends BaseAdapter {
    private ArrayList<Routine> dataSet; // list of data
    private static ViewHolder viewHolder;
    private LayoutInflater inflater; // Layout inflater
    Context context;
    BaseActivityViewModel baseActivityViewModel;
    // this calender will be used to show a live dot after the running class
    Calendar calendar;


    // constructor
    public RoutineItemVIewAdapter(ArrayList<Routine> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
        baseActivityViewModel = ViewModelProviders.of((FragmentActivity) context).get(BaseActivityViewModel.class);
        calendar = Calendar.getInstance();
    }

    public void setDataSet(ArrayList<Routine> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
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
            viewHolder.courseSection = (TextView) convertView.findViewById(R.id.item_routine_course_section);
            viewHolder.courseRoom = (TextView) convertView.findViewById(R.id.item_routine_course_room);
            viewHolder.courseTeacher = (TextView) convertView.findViewById(R.id.item_routine_course_teacher);
            viewHolder.courseNotification = (ImageView) convertView.findViewById(R.id.item_routine_alarm_icon);
            viewHolder.courseDeleteBtn = (Button) convertView.findViewById(R.id.item_routine_action_delete);
            viewHolder.courseLive = (LinearLayout) convertView.findViewById(R.id.item_routine_is_live);

            viewHolder.courseDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("You sure you want to delete this? ");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            baseActivityViewModel.delete(routine);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });



        }
        // setting everything up
        int stTime = routine.getStartingTime();
        int stHour = stTime/100;
        int stMinute = stTime%100;
        int enTime = routine.getEndingTime();
        int enHour = enTime/100;
        int enMinute = enTime%100;
        String stTimeInFormat = "";
        if(stHour <= 12){
            stTimeInFormat = String.format("%02d : %02d AM", stHour, stMinute);
        }else {
            stTimeInFormat = String.format("%02d : %02d PM", stHour-12, stMinute);
        }
        if(stHour >= 12) stHour = stHour == 12 ? 0 : stHour- 12;
        if(enHour >= 12) enHour=  enHour == 12 ? 0 : enHour - 12;
        Log.d("TAG", "getView: " + calendar.get(Calendar.HOUR) + " " + calendar.get(Calendar.MINUTE));
        // my hour starts from 1 and java's calender starts from 0
        if(stHour <= calendar.get(Calendar.HOUR)  &&
            enHour >= calendar.get(Calendar.HOUR)  ){
            viewHolder.courseLive.setVisibility(View.VISIBLE);
        }


        // test area
        viewHolder.classStartingPeriod.setText(stTimeInFormat);
        viewHolder.courseName.setText(routine.getCourseTitle());
        viewHolder.courseCode.setText(routine.getCourseCode());
        viewHolder.courseSection.setText(String.valueOf(routine.getSection()));
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
        ImageView courseNotification;
        Button courseDeleteBtn;
        LinearLayout courseLive;
    }
}
