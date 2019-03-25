package com.zp.course.storage.database.dao;

import com.zp.course.storage.database.table.TimetableEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see TimetableDao
 * @since 2019/3/20
 */
@Dao
public interface TimetableDao {

    @Query("select * from timetable where user_id = :userId")
    List<TimetableEntity> getAll(long userId);

    @Insert
    void add(TimetableEntity entity);

    @Query("select * from timetable order by update_time desc limit 1")
    TimetableEntity getLastOne();
}
