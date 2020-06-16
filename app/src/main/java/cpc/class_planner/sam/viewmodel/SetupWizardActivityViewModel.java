package cpc.class_planner.sam.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetupWizardActivityViewModel extends AndroidViewModel {
    private final String TAG = this.getClass().getSimpleName();
    private Application application;
    public SetupWizardActivityViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public List<String> getData(String query){
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

                }
            }
        });
        return spinnerDataList;

    }


}
