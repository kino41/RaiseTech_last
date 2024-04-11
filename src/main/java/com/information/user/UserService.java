package com.information.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> findAll() {
        return userMapper.getAll();
    }

    public User findById(int id) {
        Optional<User> user = userMapper.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("user not found");
        }
    }

    public User insert(String name, String birthdate) {
        User newUser = User.createUser(name, birthdate);
        userMapper.insert(newUser);
        return newUser;
    }

    public User insert(User user) {
        userMapper.insert(user);
        return user;
    }

    public User update(Integer id, String name, String birthdate) {
        Optional<User> user = userMapper.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("user not found");
        }

    }
}
