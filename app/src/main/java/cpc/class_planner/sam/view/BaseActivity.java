package cpc.class_planner.sam.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
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

        //viewModel.insert(routine);
        // should be removed

        // getting the fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoutineViewAdapter routineViewAdapter = new RoutineViewAdapter(fragmentManager);
        viewPager.setAdapter(routineViewAdapter);

    }
}
