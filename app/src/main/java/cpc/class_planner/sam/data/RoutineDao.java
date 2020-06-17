package cpc.class_planner.sam.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cpc.class_planner.sam.model.Routine;

@Dao
public interface RoutineDao {

    @Insert
    void insertData(Routine routine);

    @Query("SELECT * FROM routine WHERE dayOfTheWeek LIKE :today ORDER BY startingTime")
    LiveData<List<Routine>> getTodayRoutine(String today);

    @Query("SELECT DISTINCT courseTitle FROM routine")
    List<String> getCourseNames();

    @Query("SELECT * FROM routine WHERE courseTitle = :courseTitle LIMIT 1")
    Routine getRoutineByCourseName(String courseTitle);

    @Delete
    void deleteData(Routine routine);

    @Query("SELECT * FROM routine")
    List<Routine> getFullRoutine();
}
