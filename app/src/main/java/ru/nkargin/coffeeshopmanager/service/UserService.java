package ru.nkargin.coffeeshopmanager.service;

import com.orm.query.Condition;
import com.orm.query.Select;

import ru.nkargin.coffeeshopmanager.model.User;


public class UserService {

    public static final UserService INSTANCE = new UserService();

    public UserService() {}

    public User getUserByName(String name) {
        Select<User> userByUsername = Select.from(User.class).where(Condition.prop("name").eq(name));
        return userByUsername.first();
    }
}
