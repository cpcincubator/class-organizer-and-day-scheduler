package cpc.class_planner.sam.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import cpc.class_planner.sam.data.RoutineDao;
import cpc.class_planner.sam.data.RoutineDatabase;
import cpc.class_planner.sam.model.Routine;

public class BaseActivityViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private RoutineDao routineDao;
    private RoutineDatabase database;

    // base constructor for android ViewModel
    public BaseActivityViewModel(@NonNull Application application) {
        super(application);
        database = RoutineDatabase.getInstance(application);
        routineDao = database.routineDao();
    }

    public void insert(final Routine routine){
        // android doesn't allow db operation to run on the main thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                routineDao.insertData(routine);
                Log.d(TAG, "run: Inserted " + routine.getCourseTitle());
            }
        });
        thread.start();
    }

}
