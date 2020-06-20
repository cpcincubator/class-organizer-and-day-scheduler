package cpc.class_planner.sam.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import cpc.class_planner.sam.R;
import cpc.class_planner.sam.data.repo.CloudApi;
import cpc.class_planner.sam.model.Routine;
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;
import cpc.class_planner.sam.viewmodel.SetupWizardActivityViewModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**Cound't implement MVVM in this activity, code has became a mess*/
public class SetupWizardActivity extends AppCompatActivity {
    @BindView(R.id.setup_wizard_spinner_year)
    Spinner iYearSpinner;
    @BindView(R.id.setup_wizard_text_view_data_title)
    TextView titleText;
    @BindView(R.id.setup_wizard_next_btn)
    Button nextButton;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    Handler handler;
    // Adapter
    ArrayAdapter<String> adapterYear;
    String[] queries = {"Year","Semester", "Department", "Batch", "Section"};
    HashMap<String, String> myPreferences = new HashMap<String, String>();
    private int count = -1; // update: changed from static to class variable
    private static String mySelection;
    SetupWizardActivityViewModel viewModel;
    private static final String SHARED_PREFERENCE = "class_organizer_by_smn";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // bind view with the viewModel
        viewModel = ViewModelProviders.of(this).get(SetupWizardActivityViewModel.class);
        setContentView(R.layout.activity_setup_wizard);
        ButterKnife.bind(this);
        handler = new Handler();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, Activity.MODE_PRIVATE);
        Boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
        if(!isFirstTime) titleText.setText(R.string.when_ready);

    }



    @OnItemSelected(R.id.setup_wizard_spinner_year)
    void getSpinnerData(Spinner spinner, int position) {
        mySelection = spinner.getSelectedItem().toString();
        Log.d("TAG", "getRoutineYear: " + mySelection);
    }

    @OnClick(R.id.setup_wizard_next_btn)
    void toggleNext(){
        if(count>-1 && count<queries.length) myPreferences.put(queries[count],mySelection);
        count++;
        if (count < queries.length) {
            progressBar.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
            getData(queries[count]);
            titleText.setText("My " + queries[count]);
            if(count == queries.length - 1){
                nextButton.setText("Import");
            }
        } else{
            titleText.setText("Importing routine....");
            nextButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // empty the database first
                        viewModel.deleteAll();
                        // populate the data now
                        viewModel.insertAll(viewModel.getRoutine(myPreferences.get(queries[0]),
                                myPreferences.get(queries[1]),
                                myPreferences.get(queries[2]),
                                myPreferences.get(queries[3]),
                                myPreferences.get(queries[4])
                        ));
                        int latestVersion = new CloudApi().getVersion();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setSharedPreferences(latestVersion);
                                // Exit the activity after importing data
                                Toast.makeText(SetupWizardActivity.this, "Routine updated!", Toast.LENGTH_SHORT).show();
                                SetupWizardActivity.this.finish();
                            }
                        });
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(SetupWizardActivity.this, "Error\n" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            t.start();



        }
    }

    @OnClick(R.id.setup_wizard_abort_btn)
    void abortWizard(){
        new AlertDialog.Builder(this)
                .setTitle("Are your sure about aborting?")
                .setMessage(getString(R.string.set_my_own))
                .setPositiveButton("I'm Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setSharedPreferences(sharedPreferences.getInt("VERSION", 0));
                        SetupWizardActivity.this.finish();
                    }
                })
                .setNegativeButton("Abort", null)
                .show();
    }

    public void setSharedPreferences(int latestVersion){
        // Save the current version number of the id if successful
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("VERSION", latestVersion);
        editor.putBoolean("isFirstTime",false);
        editor.apply();
    }



    public void getData(String query){
        OkHttpClient httpClient = new OkHttpClient();
        String URL = "http://smnshuvo.000webhostapp.com/cpc_getList.php?query=" + query;
        List<String> spinnerDataList = new ArrayList<String>();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            nextButton.setVisibility(View.VISIBLE);
                        }
                    });
                    String webResponse = response.body().string();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(webResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i=0;i<jsonArray.length();i++){
                        spinnerDataList.add(jsonArray.optString(i));
                    }
                    SetupWizardActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterYear = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, spinnerDataList);
                           // adapterYear.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            iYearSpinner.setAdapter(adapterYear);
                        }
                    });

                }
            }
        });

    }
}
