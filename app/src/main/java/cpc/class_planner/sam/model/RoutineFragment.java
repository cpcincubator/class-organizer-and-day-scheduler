package cpc.class_planner.sam.model;

import android.app.AlertDialog;
import android.app.AliasActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cpc.class_planner.sam.R;
import cpc.class_planner.sam.model.adapter.RoutineItemVIewAdapter;
import cpc.class_planner.sam.view.BaseActivity;
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;

public class RoutineFragment extends Fragment {
    @BindView(R.id.routine_no_class_bg)
    ImageView noClassBg;
    BaseActivityViewModel viewModel;
    private String dayOfTheWeek;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routine_view_pager, container, false);
        viewModel = ViewModelProviders.of(this).get(BaseActivityViewModel.class);
        ButterKnife.bind(this, view);
        // TODO: Need the change the listView ID
        ListView listView = view.findViewById(R.id.sample_list_view);
        // get the routine for today's day of the week
        //List<Routine> routines =
        viewModel.getDailyRoutine(this.dayOfTheWeek).observe(this, new Observer<List<Routine>>() {
            @Override
            public void onChanged(List<Routine> routines) {
                if(routines.size() == 0){
                    noClassBg.setVisibility(View.VISIBLE);
                } else{
                    RoutineItemVIewAdapter adapter = new RoutineItemVIewAdapter((ArrayList<Routine>) routines, getContext());
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
            }
        });


        return view;
    }

    public RoutineFragment(String text){
        this.dayOfTheWeek = text;
    }
}
