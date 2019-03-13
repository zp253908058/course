package com.zp.course.storage.database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see AccountEntity
 * @since 2019/3/13
 */
@Entity(tableName = "account")
public class AccountEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo
    private String username;

    @ColumnInfo
    private String password;

    public AccountEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
