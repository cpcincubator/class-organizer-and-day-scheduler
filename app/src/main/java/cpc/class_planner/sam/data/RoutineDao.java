package cpc.class_planner.sam.data;

import androidx.room.Dao;
import androidx.room.Insert;

import cpc.class_planner.sam.model.Routine;

@Dao
public interface RoutineDao {

    @Insert
    void insertData(Routine routine);
}
