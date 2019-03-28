package com.zp.course.storage.database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see ClassEntity
 * @since 2019/3/20
 */
@Entity(tableName = "class_info_t")
public class ClassEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;         //class表id
    @ColumnInfo(name = "start_time")
    private long startTime;  //开始时间
    @ColumnInfo(name = "section")
    private int section;     //节数，第几节
    @ColumnInfo(name = "timetable_id")
    private long timetableId; //课表id

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public long getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(long timetableId) {
        this.timetableId = timetableId;
    }
}
