package cpc.class_planner.sam.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "routine")
public class Routine {
    @PrimaryKey(autoGenerate = true)
    private long id;                // using an unique id for now on, will use composite key of DOTW and ST in future

    @NonNull
    @ColumnInfo
    private String dayOfTheWeek;    // this is the day of the week. ie: Saturday
    @ColumnInfo
    private String startingTime;    // the time when the class changes, data type may change in future
    @ColumnInfo
    private String endingTime;      // time when class ends, ie: 10 PM
    @ColumnInfo
    private String courseTitle;     // Title of the course
    @ColumnInfo
    private String courseCode;      // course code
    @ColumnInfo
    private String teacher;         // Professor who conducts the course
    @ColumnInfo
    private String roomNo;          // the room no that has been assigned for the course
    @ColumnInfo
    private char section;          // the room no that has been assigned for the course

    // constructor


    public Routine(@NonNull String dayOfTheWeek, String startingTime, String endingTime, String courseTitle, String courseCode, String teacher, String roomNo, char section) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.courseTitle = courseTitle;
        this.courseCode = courseCode;
        this.teacher = teacher;
        this.roomNo = roomNo;
        this.section = section;
    }

    // Getters

    public long getId() {
        return id;
    }

    @NonNull
    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public char getSection() {
        return this.section;
    }

    // Setter

    public void setId(long id) {
        this.id = id;
    }
}
