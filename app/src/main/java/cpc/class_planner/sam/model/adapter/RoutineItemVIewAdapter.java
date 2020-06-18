package cpc.class_planner.sam.model.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
            viewHolder.setAlarmButton = (ImageButton) convertView.findViewById(R.id.item_routine_alarm_icon);


            // on click listener for delete button
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

            // on click listener for set alarm button

            viewHolder.setAlarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                    DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
                    String weekdays[] = dfs.getWeekdays();
                    int stTime = routine.getStartingTime();
                    int stHour = stTime/100;
                    int stMinute = stTime%100;
                    // if the date is today's or date is not today's but tomorrow's but it's more than 9 PM at night
                    if( weekdays[calendar.get(Calendar.DAY_OF_WEEK)].equalsIgnoreCase(routine.getDayOfTheWeek())
                    || (weekdays[calendar.get(Calendar.DAY_OF_WEEK)+1].equalsIgnoreCase(routine.getDayOfTheWeek()) &&
                            calendar.get(Calendar.HOUR_OF_DAY) > 20
                            )
                    ) {

                        new AlertDialog.Builder(context)
                                .setTitle("Set Alarm")
                                .setMessage("You are setting an alarm for this class. Choose one of the option below to select the time you want to be get notified before the class.\n\nNotify me...")
                                .setPositiveButton("5 Minutes Before", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int ALARM_HOUR = stHour;
                                        int ALARM_MINUTES = stMinute;
                                        ALARM_MINUTES-= 5;
                                        // if the time becomes negative
                                        if(ALARM_MINUTES < 0){
                                            /*
                                            Example: For 10.00 AM alarm the alarm should be set at 9.30 AM
                                            0 - 30 = -30 which is less than 0, so the minute will be 30
                                            and the hour wil be reduced by one step
                                             */
                                            ALARM_HOUR--;
                                            ALARM_MINUTES = 60 - Math.abs(ALARM_MINUTES);
                                        }
                                        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, ALARM_HOUR);
                                        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, ALARM_MINUTES);
                                        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, routine.getCourseTitle());
                                        context.startActivity(alarmIntent);
                                    }
                                })
                                .setNegativeButton("10 Minutes Before", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int ALARM_HOUR = stHour;
                                        int ALARM_MINUTES = stMinute;
                                        ALARM_MINUTES-= 10;
                                        // if the time becomes negative
                                        if(ALARM_MINUTES < 0){
                                            /*
                                            Example: For 10.00 AM alarm the alarm should be set at 9.30 AM
                                            0 - 30 = -30 which is less than 0, so the minute will be 30
                                            and the hour wil be reduced by one step
                                             */
                                            ALARM_HOUR--;
                                            ALARM_MINUTES = 60 - Math.abs(ALARM_MINUTES);
                                        }
                                        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, ALARM_HOUR);
                                        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, ALARM_MINUTES);
                                        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, routine.getCourseTitle());
                                        context.startActivity(alarmIntent);
                                    }
                                })
                                .setNeutralButton("30 Minutes Before", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // those variables couldn't be modified from inner class
                                        int ALARM_HOUR = stHour;
                                        int ALARM_MINUTES = stMinute;
                                        ALARM_MINUTES-= 30;
                                        // if the time becomes negative
                                        if(ALARM_MINUTES < 0){
                                            /*
                                            Example: For 10.00 AM alarm the alarm should be set at 9.30 AM
                                            0 - 30 = -30 which is less than 0, so the minute will be 30
                                            and the hour wil be reduced by one step
                                             */
                                            ALARM_HOUR--;
                                            ALARM_MINUTES = 60 - Math.abs(ALARM_MINUTES);
                                        }
                                        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, ALARM_HOUR);
                                        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, ALARM_MINUTES);
                                        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, routine.getCourseTitle());
                                        context.startActivity(alarmIntent);
                                    }
                                })
                                .show();

                    } else {
                        Toast.makeText(context, "You cannot set the alarm with of this day", Toast.LENGTH_SHORT).show();
                    }
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
        ImageButton setAlarmButton;
        LinearLayout courseLive;
    }
}
