package ru.nkargin.coffeeshopmanager.model;

import com.orm.SugarRecord;


public class User extends SugarRecord<User> {

    private String name;
    private boolean isAdmin;
    private String password;
    private String realName;

    public User() {}

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
