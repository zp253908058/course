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
    @ColumnInfo(name = "course_id")
    private long courseId;        //课程id   CourseEntity.id
    @ColumnInfo(name = "teacher_name")
    private String teacherName;   //老师
    @ColumnInfo(name = "course_address")
    private String address;       //上课的教室
    @ColumnInfo(name = "day_in_week")
    private int dayInWeek;        //星期几
    @ColumnInfo(name = "course_section")
    private int section;          //第几节
    @ColumnInfo
    private String description;   //描述 如：实验，理论

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
