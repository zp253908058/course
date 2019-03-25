package com.zp.course.storage.database.table;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see TimetableEntity
 * @since 2019/3/20
 */
@Entity(tableName = "timetable")
public class TimetableEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;         //id 自动生成
    @ColumnInfo(name = "timetable_name")
    private String name;     //课程表的名字
    @ColumnInfo(name = "timetable_term")
    private int term;        //学期数
    @ColumnInfo(name = "class_duration")
    private int duration;    //一节课多少分钟
    @ColumnInfo(name = "week_count")
    private int weekCount;   //多少周
    @ColumnInfo(name = "start_mills")
    private long startMills;  //最开始的时间毫秒数
    @ColumnInfo(name = "user_id")
    private long userId;      //用户id
    @ColumnInfo(name = "update_time")
    private long updateTime;   //更新时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(int weekCount) {
        this.weekCount = weekCount;
    }

    public long getStartMills() {
        return startMills;
    }

    public void setStartMills(long startMills) {
        this.startMills = startMills;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
