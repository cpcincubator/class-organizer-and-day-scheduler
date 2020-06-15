package cpc.class_planner.sam.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import cpc.class_planner.sam.data.RoutineDao;
import cpc.class_planner.sam.data.RoutineDatabase;
import cpc.class_planner.sam.model.Routine;

public class InputActivityViewModel extends AndroidViewModel {
    private String TAG = this.getClass().getSimpleName();
    private RoutineDao routineDao;
    private RoutineDatabase database;

    public InputActivityViewModel(@NonNull Application application) {
        super(application);
        database = RoutineDatabase.getInstance(application);
        routineDao = database.routineDao();
    }

    // for smooth experience we will not run this on main thread
    public void insert(final Routine routine){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                routineDao.insertData(routine);
                Log.d(TAG, "run: Inserted " + routine.getCourseTitle());
            }
        });
        thread.start();
    }

    public List<String> getCourseList(){
        return routineDao.getCourseNames();
    }

    public Routine getCourseByName(String courseTitle){
        return routineDao.getRoutineByCourseName(courseTitle);
    }


}
