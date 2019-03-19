package com.zp.course.storage.database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see CourseInfoEntity
 * @since 2019/3/14
 */

@Entity(tableName = "course_info")
public class CourseInfoEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;              //id
    @ColumnInfo(name = "course_name")
    private String name;          //课程名字
    @ColumnInfo(name = "course_id")
    private long courseId;        //课程id   CourseEntity.id
    @ColumnInfo(name = "course_term")
    private int term;             //学期
    @ColumnInfo(name = "teacher_name")
    private String teacherName;   //老师
    @ColumnInfo(name = "course_address")
    private String address;       //上课的教室
    @ColumnInfo(name = "course_week")
    private int week;             //周次
    @ColumnInfo(name = "day_in_week")
    private int dayInWeek;        //星期几
    @ColumnInfo(name = "course_section")
    private int section;          //第几节

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

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(int dayInWeek) {
        this.dayInWeek = dayInWeek;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }
}
