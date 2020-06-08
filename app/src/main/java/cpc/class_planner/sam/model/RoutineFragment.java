package cpc.class_planner.sam.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cpc.class_planner.sam.R;

public class RoutineFragment extends Fragment {
    String text;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routine_view_pager, container, false);
        TextView textView = view.findViewById(R.id.test_text);
        textView.setText(this.text);
        return view;
    }

    public RoutineFragment(String text){
        this.text = text;
    }
}
