package cpc.class_planner.sam.data.repo;
// This class should have been utilized better

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CloudApi {





    // reused the existing version
    public int getVersion(){
        OkHttpClient httpClient = new OkHttpClient();
        String URL = "http://smnshuvo.000webhostapp.com/cpc_getList.php?query=Serial";
        List<String> spinnerDataList = new ArrayList<String>();
        String responseText;
        int cloudVersion = 0;
        Request request = new Request.Builder()
                .url(URL)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            responseText = response.body().string();
            JSONArray jsonArray = new JSONArray(responseText);
            cloudVersion = jsonArray.optInt(0);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return cloudVersion;

    }

}
