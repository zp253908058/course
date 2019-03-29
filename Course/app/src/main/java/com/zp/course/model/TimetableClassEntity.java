package com.zp.course.model;

import com.zp.course.storage.database.table.ClassEntity;
import com.zp.course.storage.database.table.TimetableEntity;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

/**
 * Class description:
 * 课表以及课程的实体
 *
 * @author zp
 * @version 1.0
 * @see TimetableClassEntity
 * @since 2019/3/29
 */
public class TimetableClassEntity {

    @Embedded
    private TimetableEntity timetable;

    @Relation(entity = ClassEntity.class, entityColumn = "timetable_id", parentColumn = "id")
    private List<ClassEntity> classInfo;

    public TimetableEntity getTimetable() {
        return timetable;
    }

    public void setTimetable(TimetableEntity timetable) {
        this.timetable = timetable;
    }

    public List<ClassEntity> getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(List<ClassEntity> classInfo) {
        this.classInfo = classInfo;
    }
}
