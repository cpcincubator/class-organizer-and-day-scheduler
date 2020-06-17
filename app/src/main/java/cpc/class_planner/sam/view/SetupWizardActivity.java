package cpc.class_planner.sam.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
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
    private static int count = -1;
    private static String mySelection;
    SetupWizardActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // bind view with the viewModel
        viewModel = ViewModelProviders.of(this).get(SetupWizardActivityViewModel.class);
        setContentView(R.layout.activity_setup_wizard);
        ButterKnife.bind(this);
        handler = new Handler();



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
            getData(queries[count]);
            titleText.setText("My " + queries[count]);
            if(count == queries.length - 1){
                nextButton.setText("Import");
            }
        } else{
            titleText.setText(myPreferences.toString());
            nextButton.setVisibility(View.GONE);


            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        viewModel.getRoutine(myPreferences.get(queries[0]),
                                myPreferences.get(queries[1]),
                                myPreferences.get(queries[2]),
                                myPreferences.get(queries[3]),
                                myPreferences.get(queries[4])
                        );
                    } catch (Exception e){e.printStackTrace();}
                }
            });
            t.start();



        }
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
                            progressBar.setVisibility(View.GONE);
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
