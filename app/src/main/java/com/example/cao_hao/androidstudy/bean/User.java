package com.example.cao_hao.androidstudy.bean;

/**
 * Created by cao-hao on 17-8-9.
 */

public class User {

    private String username;
    private String nickname;
    private String gender;
    private String age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public User(String username, String nickname, String gender, String age) {
        this.username = username;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
    }

    public User() {
    }

}
