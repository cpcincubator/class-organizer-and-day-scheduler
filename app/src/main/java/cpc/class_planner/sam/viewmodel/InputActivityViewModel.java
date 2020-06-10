package cpc.class_planner.sam.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import cpc.class_planner.sam.data.RoutineDao;
import cpc.class_planner.sam.data.RoutineDatabase;

public class InputActivityViewModel extends AndroidViewModel {
    private String TAG = this.getClass().getSimpleName();
    private RoutineDao routineDao;
    private RoutineDatabase database;

    public InputActivityViewModel(@NonNull Application application) {
        super(application);
        database = RoutineDatabase.getInstance(application);
        routineDao = database.routineDao();
    }
}
