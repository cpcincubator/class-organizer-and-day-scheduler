package cpc.class_planner.sam.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cpc.class_planner.sam.data.RoutineDao;
import cpc.class_planner.sam.data.RoutineDatabase;
import cpc.class_planner.sam.model.Routine;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CloudExportActivityViewModel extends AndroidViewModel {
    private String TAG = this.getClass().getSimpleName();
    private RoutineDao routineDao;
    private RoutineDatabase database;
    public CloudExportActivityViewModel(@NonNull Application application) {
        super(application);
        database = RoutineDatabase.getInstance(application);
        routineDao = database.routineDao();
        Log.d(TAG, "CloudExportActivityViewModel: " + getAllRoutineJSON());
    }

    public String getAllRoutineJSON(){
        Gson gson = new Gson();
        return gson.toJson(routineDao.getFullRoutine(),
                new TypeToken<List<Routine>>() {}.getType());
    }


    // I can feel the code smell here
    // I should have created an object to make my task
    // but this thing should be temporary
    public void sendPostRequest(String year, String Semester, String Department, String Batch, String Section, String Sender){
        String[] fields = {"Year", "Semester", "Department", "Batch", "Section", "Submitter"};
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add(fields[0],year)
                .add(fields[1],Semester)
                .add(fields[2],Department)
                .add(fields[3],Batch)
                .add(fields[4],Section)
                .add(fields[5],Sender)
                .add("json", getAllRoutineJSON())
                .build();
        Request myRequest = new Request.Builder()
                .url("http://smnshuvo.000webhostapp.com/cpc_postRoutine.php")
                .post(requestBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(myRequest).execute();
                    Log.d("TAG", "run: " + response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
