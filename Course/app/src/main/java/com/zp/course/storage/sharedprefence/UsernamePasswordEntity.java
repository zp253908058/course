package com.zp.course.storage.sharedprefence;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see UsernamePasswordEntity
 * @since 2019/3/11
 */

@Preference(name = "account")
public class UsernamePasswordEntity {

    private String username;

    private String password;

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
