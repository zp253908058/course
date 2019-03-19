package com.zp.course.storage.database.dao;

import com.zp.course.storage.database.table.CourseEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see CourseDao
 * @since 2019/3/14
 */
@Dao
public interface CourseDao {


    @Query("select id from course where course_name = :name and course_number = :number limit 1")
    long verify(String name, String number);

    @Insert
    void save(CourseEntity entity);

    @Query("select * from course where user_id = :userId")
    List<CourseEntity> getAll(long userId);
}
