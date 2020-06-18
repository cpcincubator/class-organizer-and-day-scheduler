package cpc.class_planner.sam.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;


import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cpc.class_planner.sam.R;
import cpc.class_planner.sam.data.RoutineDao;
import cpc.class_planner.sam.data.RoutineDatabase;
import cpc.class_planner.sam.data.repo.CloudApi;
import cpc.class_planner.sam.model.Routine;
import cpc.class_planner.sam.model.adapter.RoutineViewAdapter;
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;

public class BaseActivity extends FragmentActivity {
    BaseActivityViewModel viewModel; // viewModel for this view
    @BindView(R.id.routine_fragment)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Handler updateHandler;
    private static final String SHARED_PREFERENCE = "class_organizer_by_smn";
    SharedPreferences sharedPreferences; // = getSharedPreferences(SHARED_PREFERENCE, Activity.MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        updateHandler = new Handler();
        // Setting title and subtitle
        toolbar.setTitle("Class Organizer");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitle("& Day Scheduler");
        toolbar.setSubtitleTextColor(Color.WHITE);
        // bind view with the viewModel
        viewModel = ViewModelProviders.of(this).get(BaseActivityViewModel.class);
        // check if this the user's first run
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, Activity.MODE_PRIVATE);
        Boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

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
        // start checking for update
        Thread routineUpdateCheckerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int currentVersion = sharedPreferences.getInt("VERSION",0);
                int cloudVersion = new CloudApi().getVersion();
                Log.d("CLOUD VERSION", "run: " + cloudVersion);
                if(cloudVersion>currentVersion){
                    updateHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(BaseActivity.this)
                                    .setIcon(R.drawable.ic_update_black)
                                    .setTitle("Update available!")
                                    .setMessage("It seems someone has uploaded a routine.\nWanna check that out?")
                                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loadSetupWizardActivity();
                                        }
                                    })
                                    .setNegativeButton("No thanks!", null)
                                    .show();

                        }
                    });
                }
            }
        });
        if(isFirstTime){
            Intent i = new Intent(this, SetupWizardActivity.class);
            startActivity(i);
        } else{
            // Shouldn't check for update on the first run
            routineUpdateCheckerThread.start();
        }

    }


    @OnClick(R.id.base_new_routine)
    void loadInsertActivity(){
        Intent intent = new Intent(this, InputRoutineActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.base_export_routine)
    void loadExportActivity(){
        Intent intent = new Intent(this, CloudExportActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.base_update_routine)
    void loadSetupWizardActivity(){
        Intent intent = new Intent(this, SetupWizardActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.about_developer)
    void showDeveloperDetail(){
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Developers")
                .setMessage("The app is developed under DiU CPC's OPEN SOURCE PROJECT.\nDeveloped by SMN Shuvo and his team\nVisit the github page for update and contribution")
                .setPositiveButton("Visit Github", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri webLink = Uri.parse("https://github.com/cpcincubator/class-organizer-and-day-scheduler");
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, webLink);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Okay!", null)
                .show();

    }


}
