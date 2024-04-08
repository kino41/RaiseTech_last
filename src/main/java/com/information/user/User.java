package com.information.user;

public class User {
    private Integer id;
    private String name;
    private String birthdate;

    public User(Integer id, String name, String birthdate) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
    }

    public static User createUser(String name, String birthdate) {
        return new User(null, name, birthdate);
    }

    public void updateUser(String name, String birthdate) {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }
}
