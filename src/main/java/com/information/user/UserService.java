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
        Optional<User> optionalUser = userMapper.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.updateUser(name, birthdate);
            userMapper.update(user);
            return user;
        } else {
            throw new UserNotFoundException("user not found");
        }
    }

    public void delete(Integer id) {
        if (!userMapper.findById(id).isPresent()) {
            throw new UserNotFoundException("user not found");
        }
        int affectedRows = userMapper.deleteById(id);
        if (affectedRows <= 0) {
            throw new UserNotFoundException("user not found");
        }
    }
}
