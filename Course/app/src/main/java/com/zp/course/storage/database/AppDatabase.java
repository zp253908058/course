package com.zp.course.storage.database;

import android.content.Context;

import com.zp.course.storage.database.dao.AccountDao;
import com.zp.course.storage.database.dao.UserDao;
import com.zp.course.storage.database.table.AccountEntity;
import com.zp.course.storage.database.table.UserEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see AppDatabase
 * @since 2019/3/11
 */
@Database(entities = {AccountEntity.class, UserEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    private static final String DB_NAME = "course.db";

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DB_NAME)
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract UserDao getUserDao();

    public abstract AccountDao getAccountDao();
}
