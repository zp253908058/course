package com.zp.course.storage.database;

import android.content.Context;

import com.zp.course.storage.database.dao.AccountDao;
import com.zp.course.storage.database.dao.CourseDao;
import com.zp.course.storage.database.dao.TimetableDao;
import com.zp.course.storage.database.dao.UserDao;
import com.zp.course.storage.database.table.AccountEntity;
import com.zp.course.storage.database.table.ClassEntity;
import com.zp.course.storage.database.table.CourseEntity;
import com.zp.course.storage.database.table.CourseInfoEntity;
import com.zp.course.storage.database.table.TimetableEntity;
import com.zp.course.storage.database.table.UserEntity;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see AppDatabase
 * @since 2019/3/11
 */
@Database(entities = {AccountEntity.class, UserEntity.class, CourseEntity.class, CourseInfoEntity.class, TimetableEntity.class, ClassEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    private static final String DB_NAME = "course.db";

    private static final Object sLock = new Object();

    public static AppDatabase getInstance() {
        return INSTANCE;
    }

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            synchronized (sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
    }

    public abstract UserDao getUserDao();

    public abstract AccountDao getAccountDao();

    public abstract CourseDao getCourseDao();

    public abstract TimetableDao getTimetableDao();
}
