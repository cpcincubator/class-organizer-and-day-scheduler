package cpc.class_planner.sam.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpc.class_planner.sam.data.RoutineDao;
import cpc.class_planner.sam.data.RoutineDatabase;
import cpc.class_planner.sam.model.Routine;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetupWizardActivityViewModel extends AndroidViewModel {
    private final String TAG = this.getClass().getSimpleName();
    private Application application;
    private RoutineDao routineDao;
    private RoutineDatabase database;

    public SetupWizardActivityViewModel(@NonNull Application application) {
        super(application);
        database = RoutineDatabase.getInstance(application);
        routineDao = database.routineDao();
        this.application = application;
    }

    // Should have created an object instead
    public List<Routine> getRoutine(String year, String semester, String department, String batch, String section) {
        String URL = "http://smnshuvo.000webhostapp.com/cpc_getRoutine.php?year="+year+"&semester="+semester+"&department="+department+"&batch="+batch+"&section="+section;
        OkHttpClient httpClient = new OkHttpClient();
        Request routineRequest = new Request.Builder()
                .url(URL)
                .build();
        String responseText = "{}";
        Response response; // I guess if the response is actually null it may lead some exception
        try {
            response = httpClient.newCall(routineRequest).execute();
            responseText = response.body().string();
            Log.d(TAG, "getRoutine - response: " + responseText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Routine>>(){}.getType();
        List<Routine> routines = gson.fromJson(responseText.trim(), type);
        return routines;
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
    public void insertAll(List<Routine> routines){
        Log.d(TAG, "insertAll: " + routines.toString());
        for(Routine rtn : routines)
        {routineDao.insertData(rtn);
            Log.d(TAG, "insertAll: " + rtn.getCourseTitle());}
    }

    public void deleteAll(){
        routineDao.deleteAll();
    }


}
