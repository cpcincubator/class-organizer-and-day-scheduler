package cpc.class_planner.sam.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cpc.class_planner.sam.R;

public class InputRoutineActivity extends AppCompatActivity {
    @BindView(R.id.input_routine_starting_hour)
    EditText classStartingHour;
    @BindView(R.id.input_routine_starting_minute)
    EditText classStartingMinute;
    @BindView(R.id.input_routine_ending_hour)
    EditText classEndingHour;
    @BindView(R.id.input_routine_ending_minute)
    EditText classEndingMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_routine);
        ButterKnife.bind(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.input_routine_set_time)
    void openTimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                classStartingHour.setText(String.valueOf(hourOfDay));
                classStartingMinute.setText(String.valueOf(minute));
            }
        },hour,minute, false);
        timePickerDialog.setTitle("Select Starting hour");
        timePickerDialog.show();
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                classEndingHour.setText(String.valueOf(hourOfDay));
                classEndingMinute.setText(String.valueOf(minute));
            }
        },hour,minute, false);
        timePickerDialog.setTitle("Select Starting hour");
        timePickerDialog.show();
    }
}
