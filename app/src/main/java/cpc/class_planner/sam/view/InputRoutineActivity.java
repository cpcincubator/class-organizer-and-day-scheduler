package cpc.class_planner.sam.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTouch;
import cpc.class_planner.sam.R;
import cpc.class_planner.sam.model.Routine;
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;
import cpc.class_planner.sam.viewmodel.InputActivityViewModel;

public class InputRoutineActivity extends AppCompatActivity {
    @BindView(R.id.input_routine_day_of_the_week)
    Spinner classAssociatedDay;
    @BindView(R.id.input_routine_starting_hour)
    Spinner classStartingHour;
    @BindView(R.id.input_routine_starting_minute)
    Spinner classStartingMinute;
    @BindView(R.id.input_routine_starting_am_pm)
    Spinner classStarting_AMPM;
    @BindView(R.id.input_routine_ending_hour)
    Spinner classEndingHour;
    @BindView(R.id.input_routine_ending_minute)
    Spinner classEndingMinute;
    @BindView(R.id.input_routine_ending_am_pm)
    Spinner classEnding_AMPM;
    @BindView(R.id.input_routine_course_title)
    EditText courseTitle;
    @BindView(R.id.input_routine_course_code)
    EditText courseCode;
    @BindView(R.id.input_routine_course_section)
    EditText courseSection;
    @BindView(R.id.input_routine_course_room)
    EditText courseRoom;
    @BindView(R.id.input_routine_course_teacher)
    EditText courseTeacher;
    @BindView(R.id.input_routine_course_selector)
    Spinner courseSelector;
    @BindView(R.id.input_routine_new_course_container)
    LinearLayout courseContainer;
    @BindView(R.id.input_routine_btn_add)
    Button btnAddRoutine;

    String[] arrDaysOfWeek = {"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday"};
    // local variables
    private static String dayOfTheWeek;
    private static int mClassStartingHour;
    private static int mClassStartingMinute;
    private static int mClassEndingHour;
    private static int mClassEndingMinute;
    private static String mCourseTitle;
    private static String mCourseCode;
    private static String mCourseSection;
    private static String mCourseRoom;
    private static String mCourseTeacher;
    private List<String> arrHour;
    private List<String> arrMinute;
    private List<String> arrCourse;
    private final String createNew = "-- CREATE NEW --";
    InputActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_routine);
        viewModel = ViewModelProviders.of(this).get(InputActivityViewModel.class);
        ButterKnife.bind(this);
        ArrayAdapter<String> adapterHour;
        ArrayAdapter<String> adapterMinute;
        ArrayAdapter<String> adapterCourse;
        ArrayAdapter<CharSequence> adapter_am_pm;
        ArrayAdapter<CharSequence> adapterDayList;
        String[] arr_am_pm = {"AM","PM"};
        // creating instance
        arrHour = new ArrayList<>();
        arrMinute = new ArrayList<>();
        arrCourse = new ArrayList<>();
        // populate the lists with data
        NumberFormat formatter = new DecimalFormat("00");
        for(int i=1;i<=12;i++) arrHour.add(formatter.format(i));
        for(int i=0;i<60;i+=5) arrMinute.add(formatter.format(i));
        arrCourse = viewModel.getCourseList();
        arrCourse.add(createNew);
        // assign those data to the spinner
        adapterHour = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrHour);
        adapterHour.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapterMinute = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrMinute);
        adapterMinute.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter_am_pm = new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item, arr_am_pm);
        adapter_am_pm.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapterDayList = new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item, arrDaysOfWeek);
        adapter_am_pm.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapterCourse = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrCourse);
        adapterCourse.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // Set those adapters with spinner
        classStartingHour.setAdapter(adapterHour);
        classEndingHour.setAdapter(adapterHour);
        classStartingMinute.setAdapter(adapterMinute);
        classEndingMinute.setAdapter(adapterMinute);
        classStarting_AMPM.setAdapter(adapter_am_pm);
        classEnding_AMPM.setAdapter(adapter_am_pm);
        classAssociatedDay.setAdapter(adapterDayList);
        courseSelector.setAdapter(adapterCourse);
    }

    // Load the day when a day is selected
    @OnItemSelected(R.id.input_routine_day_of_the_week)
    void getDay(Spinner spinner, int position){
        dayOfTheWeek = arrDaysOfWeek[position];
    }

    // Class Starting Hour
    @OnItemSelected(R.id.input_routine_starting_hour)
    void getClassStartingHour(Spinner spinner, int position) {
        mClassStartingHour = Integer.parseInt(arrHour.get(position));
    }

    // Class Ending  Hour
    @OnItemSelected(R.id.input_routine_ending_hour)
    void getClassEndingHour(Spinner spinner, int position) {
        mClassEndingHour = Integer.parseInt(arrHour.get(position));
    }

    // Class Starting minute
    @OnItemSelected(R.id.input_routine_starting_minute)
    void getClassStartingMinute(Spinner spinner, int position) {
        mClassStartingMinute = Integer.parseInt(arrMinute.get(position));
    }

    // Class Starting minute
    @OnItemSelected(R.id.input_routine_ending_minute)
    void getClassEndingMinute(Spinner spinner, int position) {
        mClassEndingMinute = Integer.parseInt(arrMinute.get(position));
    }

    // AM PM
    @OnItemSelected(R.id.input_routine_starting_am_pm)
    void reshapeStartingHour(Spinner spinner, int position) {
        mClassStartingHour = position == 1 ? mClassStartingHour + 12 : mClassStartingHour;
    }

    // AM PM
    @OnItemSelected(R.id.input_routine_starting_am_pm)
    void reshapeEndingHour(Spinner spinner, int position) {
        mClassEndingHour = position == 1 ? mClassEndingHour + 12 : mClassEndingHour;
    }

    @OnClick(R.id.input_routine_btn_add_course)
    void insertNewRoutine(){
        mCourseTitle = courseTitle.getText().toString();
        mCourseCode = courseCode.getText().toString();
        mCourseSection = courseSection.getText().toString();
        mCourseRoom = courseRoom.getText().toString();
        mCourseTeacher = courseTeacher.getText().toString();
        int courseStartInt = mClassStartingHour * 100 + mClassStartingMinute;
        int courseEndingInt = mClassEndingHour * 100 + mClassEndingMinute;
        Routine routine = new Routine(dayOfTheWeek, String.valueOf(courseStartInt), String.valueOf(courseEndingInt),mCourseTitle,mCourseCode,mCourseTeacher
        ,mCourseRoom,mCourseSection);
        viewModel.insert(routine);

    }

    // course selector
    @OnItemSelected(R.id.input_routine_course_selector)
    void toggleCourseView(Spinner spinner, int position) {
        if(arrCourse.get(position).equalsIgnoreCase(createNew)){
            courseContainer.setVisibility(View.VISIBLE);
            btnAddRoutine.setVisibility(View.GONE);
        }else{
            courseContainer.setVisibility(View.GONE);
            btnAddRoutine.setVisibility(View.VISIBLE);
        }
    }


}
