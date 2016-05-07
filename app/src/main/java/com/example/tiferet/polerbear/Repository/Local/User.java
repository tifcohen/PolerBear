package com.example.tiferet.polerbear.Repository.Local;

/**
 * Created by TIFERET on 27-Mar-16.
 */
public class User {
    int userId;
    String userName;
    String pwd;
    String email;
    String birthdate;
    int level;

    public User(int userId, String userName, String pwd, String email, String birthdate, int level) {
        this.userId = userId;
        this.userName = userName;
        this.pwd = pwd;
        this.email = email;
        this.birthdate = birthdate;
        this.level = level;
    }

    /*public User() {
        int size = 20;
        for(int i=0;i<size;i++){
            this.userId = userId;
            this.userName = userName;
            this.pwd = pwd;
            this.email = email;
            this.birthdate = birthdate;
            this.level = level;
        }

    }*/

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
