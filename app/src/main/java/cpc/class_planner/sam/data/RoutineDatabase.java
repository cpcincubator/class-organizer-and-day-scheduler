package cpc.class_planner.sam.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import cpc.class_planner.sam.model.Routine;

@Database(entities = Routine.class, version = 1)
public abstract class RoutineDatabase extends RoomDatabase {
    static final String databaseName = "routine_database";
    public abstract RoutineDao routineDao();

    /*
            implementing singleton design pattern here
            We want a single copy of the instance to ensure
            there's no data leak and misbehaving
    */
    private static volatile RoutineDatabase routineDatabaseInstance;
    /*
            Notes to the contributors, reason behind using volatile:
            Suppose that two threads are working on SharedObj.
            If two threads run on different processors each thread
            may have its own local copy of sharedVariable.
            If one thread modifies its value the change might not reflect
            in the original one in the main memory instantly.
            This depends on the write policy of cache.
            Now the other thread is not aware of the modified
            value which leads to data inconsistency.
     */

    public static RoutineDatabase getInstance(final Context context){
        if(routineDatabaseInstance == null) {
            routineDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                                        RoutineDatabase.class,
                                            databaseName)
                                                .build();
        }

        return routineDatabaseInstance;
    }

}

