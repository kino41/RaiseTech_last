package com.information.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> findAll() {
        return userMapper.getAll();
    }

    public List<User> findById(int id) {
        return userMapper.getById(id);
    }

    public User insert(String name, String birthdate) {
        User newUser = User.createUser(name, birthdate);
        userMapper.insert(newUser);
        return newUser;
    }
}
