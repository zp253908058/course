package com.zp.course.storage.database.dao;

import com.zp.course.model.TimetableClassEntity;
import com.zp.course.storage.database.table.ClassEntity;
import com.zp.course.storage.database.table.TimetableEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

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

    @Query("select * from timetable where user_id = :userId order by timetable_term")
    List<TimetableEntity> getAll(long userId);

    @Insert
    @Transaction
    void add(TimetableEntity entity);

    @Query("select * from timetable order by update_time desc limit 1")
    TimetableEntity getLastOne();

    @Query("select * from timetable where id = :id")
    TimetableClassEntity findById(long id);

    @Query("select * from timetable where id = :id")
    TimetableEntity getById(long id);

    @Query("select id from timetable where timetable_term = :term limit 1")
    long findByTerm(int term);

    @Update
    @Transaction
    void update(TimetableEntity entity);

    @Delete
    @Transaction
    void deleteClasses(List<ClassEntity> entities);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClass(List<ClassEntity> list);

    @Query("select * from class_info_t where timetable_id = :timetableId")
    List<ClassEntity> getClassList(long timetableId);
}
