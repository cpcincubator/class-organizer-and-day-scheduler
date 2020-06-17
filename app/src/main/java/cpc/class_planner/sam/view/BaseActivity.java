package cpc.class_planner.sam.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cpc.class_planner.sam.R;
import cpc.class_planner.sam.data.RoutineDao;
import cpc.class_planner.sam.data.RoutineDatabase;
import cpc.class_planner.sam.model.Routine;
import cpc.class_planner.sam.model.adapter.RoutineViewAdapter;
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;

public class BaseActivity extends FragmentActivity {
    BaseActivityViewModel viewModel; // viewModel for this view
    @BindView(R.id.routine_fragment)
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        // bind view with the viewModel
        viewModel = ViewModelProviders.of(this).get(BaseActivityViewModel.class);

        // load the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoutineViewAdapter routineViewAdapter = new RoutineViewAdapter(fragmentManager);
        viewPager.setAdapter(routineViewAdapter);
        // Get today's date
        int position = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Date today = Calendar.getInstance().getTime();
            position = today.getDay();
            if(today.getHours() >= 22){ // load tomorrow's routine if user is checking after 10PM
                position++;
                Toast.makeText(this, "Loaded tomorrow's routine instead", Toast.LENGTH_SHORT).show();
            }
        }


        viewPager.setCurrentItem(position+1);

    }


    @OnClick(R.id.base_new_routine)
    void loadInsertActivity(){
        Intent intent = new Intent(this, InputRoutineActivity.class);
        startActivity(intent);
    }
}
