package com.information.user;

public class UserRequest {
    private String name;
    private String birthdate;

    public UserRequest(String name, String birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }

    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public User convertToUser() {
        return new User(null, this.name, this.birthdate);
    }
}
