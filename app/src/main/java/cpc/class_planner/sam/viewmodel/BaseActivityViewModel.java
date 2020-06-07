package cpc.class_planner.sam.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BaseActivityViewModel extends AndroidViewModel {

    // base constructor for android ViewModel
    public BaseActivityViewModel(@NonNull Application application) {
        super(application);
    }

}
