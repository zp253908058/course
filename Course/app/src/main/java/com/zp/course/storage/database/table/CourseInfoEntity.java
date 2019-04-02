package com.zp.course.storage.database.table;

import android.os.Parcel;
import android.os.Parcelable;

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
public class CourseInfoEntity  implements Parcelable {

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

    public CourseInfoEntity() {
    }

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


    @SuppressWarnings("WeakerAccess")
    protected CourseInfoEntity(Parcel in) {
        id = in.readLong();
        courseId = in.readLong();
        teacherName = in.readString();
        address = in.readString();
        dayInWeek = in.readInt();
        section = in.readInt();
        description = in.readString();
    }

    public static final Creator<CourseInfoEntity> CREATOR = new Creator<CourseInfoEntity>() {
        @Override
        public CourseInfoEntity createFromParcel(Parcel in) {
            return new CourseInfoEntity(in);
        }

        @Override
        public CourseInfoEntity[] newArray(int size) {
            return new CourseInfoEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(courseId);
        dest.writeString(teacherName);
        dest.writeString(address);
        dest.writeInt(dayInWeek);
        dest.writeInt(section);
        dest.writeString(description);
    }
}
