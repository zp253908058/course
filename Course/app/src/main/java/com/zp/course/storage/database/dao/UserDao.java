package com.zp.course.storage.database.dao;

import com.zp.course.storage.database.table.UserEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see UserDao
 * @since 2019/3/13
 */
@Dao
public interface UserDao {

    @Insert
    void add(UserEntity entity);

    @Query("select * from user where account_id = :accountId")
    UserEntity getUser(long accountId);
}
