package cpc.class_planner.sam.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;
import cpc.class_planner.sam.viewmodel.SetupWizardActivityViewModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetupWizardActivity extends AppCompatActivity {
    @BindView(R.id.setup_wizard_spinner_year)
    Spinner iYearSpinner;
    @BindView(R.id.setup_wizard_text_view_data_title)
    TextView titleText;
    @BindView(R.id.setup_wizard_next_btn)
    Button nextButton;
    // Adapter
    ArrayAdapter<String> adapterYear;
    String[] queries = {"Year","Semester", "Department", "Batch", "Section"};
    HashMap<String, String> myPreferences = new HashMap<String, String>();
    private static int count = 0;
    private static String mySelection;
    SetupWizardActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // bind view with the viewModel
        viewModel = ViewModelProviders.of(this).get(SetupWizardActivityViewModel.class);
        setContentView(R.layout.activity_setup_wizard);
        ButterKnife.bind(this);



    }



    @OnItemSelected(R.id.setup_wizard_spinner_year)
    void getRoutineYear(Spinner spinner, int position) {
        mySelection = spinner.getSelectedItem().toString();
    }

    @OnClick(R.id.setup_wizard_next_btn)
    void toggleNext(){
        if (count < queries.length) {
            getData(queries[count]);
            titleText.setText("My " + queries[count]);
            myPreferences.put(queries[count],mySelection);

        } else if(count == queries.length ){
            nextButton.setText("Import");

        } else {
            nextButton.setClickable(false);
            Log.d("HASH", "toggleNext: " + myPreferences.toString());
        }
        count++;
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
