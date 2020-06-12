package cpc.class_planner.sam.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cpc.class_planner.sam.R;

public class InputRoutineActivity extends AppCompatActivity {
    @BindView(R.id.input_routine_starting_hour)
    Spinner classStartingHour;
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


}
