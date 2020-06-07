package cpc.class_planner.sam.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import cpc.class_planner.sam.R;
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;

public class BaseActivity extends AppCompatActivity {
    BaseActivityViewModel viewModel; // viewModel for this view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        // bind view with the viewModel
        viewModel = ViewModelProviders.of(this).get(BaseActivityViewModel.class);
    }
}
