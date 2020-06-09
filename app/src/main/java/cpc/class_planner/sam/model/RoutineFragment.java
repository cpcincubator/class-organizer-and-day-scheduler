package cpc.class_planner.sam.model;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import cpc.class_planner.sam.R;
import cpc.class_planner.sam.view.BaseActivity;
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;

public class RoutineFragment extends Fragment {
    BaseActivityViewModel viewModel;
    String text;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routine_view_pager, container, false);
        viewModel = ViewModelProviders.of(this).get(BaseActivityViewModel.class);

        /*These will be deleted */
        ListView listView = view.findViewById(R.id.sample_list_view);
        List<Routine> routines = viewModel.getDailyRoutine(this.text);
        Log.d("TEST", "onCreateView: " + routines.toString());
        List<String> data = new ArrayList<>();

        for (Routine routine : routines){
            data.add(routine.getCourseTitle() + "\n" + routine.getStartingTime() + " - " + routine.getEndingTime());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.test_item, R.id.textView, data );
        listView.setAdapter(arrayAdapter);
        /*These will be deleted */
        return view;
    }

    public RoutineFragment(String text){
        this.text = text;
    }
}
