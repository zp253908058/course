package com.zp.course.storage.database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see CourseEntity
 * @since 2019/3/14
 */

@Entity(tableName = "course")
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;                 //id
    @ColumnInfo(name = "course_name")
    private String name;          //课程名
    @ColumnInfo(name = "course_number")
    private String number;        //课程号
    @ColumnInfo
    private String description;   //描述信息
    @ColumnInfo(name = "user_id")
    private long userId;          //用户id
    @ColumnInfo(name = "start_week")
    private int startWeek;        //开始周次
    @ColumnInfo(name = "end_week")
    private int endWeek;          //结束周次

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }
}
