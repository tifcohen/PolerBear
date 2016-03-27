package com.example.tiferet.polerbear.Classes;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by TIFERET on 27-Mar-16.
 */
public class UserDB {
    private final static UserDB instance = new UserDB();
    List<User> db = new LinkedList<User>();

    private UserDB() {
        init();
    }

    public static UserDB getInstance() {
        return instance;
    }

    private void init(){
        for (int i = 0; i < 10; i++) {
            User u = new User(i,"username"+i,i+"",""+i+"@gmail.com", "1/1/2000", i);
            addUser(u);
        }
    }

    public void addUser(User user){
        db.add(user);
    }

    public List<User> getAllUsers(){
        return db;
    }

}
