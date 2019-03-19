package com.zp.course.storage.database.dao;

import com.zp.course.storage.database.table.AccountEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see AccountDao
 * @since 2019/3/13
 */
@Dao
public interface AccountDao {

    @Insert
    void register(AccountEntity entity);

    @Query("select id from account where username = :username limit 1")
    long findByUsername(String username);

    @Query("select id from account where username = :username and password = :password limit 1")
    long verify(String username, String password);
}
