package com.zp.course.storage.database.table;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see ClassEntity
 * @since 2019/3/20
 */
@Entity(tableName = "class_info")
public class ClassEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;         //class表id

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
