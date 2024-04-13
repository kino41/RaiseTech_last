package com.information.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> insert(@RequestBody UserRequest userRequest, UriComponentsBuilder uriBuilder) {
        User user = userService.insert(userRequest.convertToUser());
        URI location = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        UserResponse body = new UserResponse("user created");
        return ResponseEntity.created(location).body(body);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Integer id, @RequestBody User updateUser) {
        userService.update(id, updateUser.getName(), updateUser.getBirthdate());
        UserResponse response = new UserResponse("user updated");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
