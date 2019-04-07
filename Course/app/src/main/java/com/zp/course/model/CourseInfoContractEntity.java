package com.zp.course.model;

import com.zp.course.storage.database.table.CourseEntity;
import com.zp.course.storage.database.table.CourseInfoEntity;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see CourseInfoContractEntity
 * @since 2019/4/3
 */
public class CourseInfoContractEntity {

    @Embedded
    private CourseEntity course;

    @Relation(entity = CourseInfoEntity.class, entityColumn = "course_id", parentColumn = "id")
    private List<CourseInfoEntity> courseInfo;

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public List<CourseInfoEntity> getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(List<CourseInfoEntity> courseInfo) {
        this.courseInfo = courseInfo;
    }
}
