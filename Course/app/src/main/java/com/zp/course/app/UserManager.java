package com.zp.course.app;

import com.zp.course.storage.database.table.UserEntity;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see UserManager
 * @since 2019/3/13
 */
public class UserManager {

    private static UserManager sInstance;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                }
            }
        }
        return sInstance;
    }

    private UserEntity mUser;

    public UserEntity getUser() {
        return mUser;
    }

    public void setUser(UserEntity user) {
        mUser = user;
    }
}
