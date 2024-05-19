package com.information.user.service;

import com.information.user.User;
import com.information.user.UserMapper;
import com.information.user.UserNotFoundException;
import com.information.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;

    @Test
    void 全てのユーザーが取得できること() {
        List<User> users = List.of(
                new User(1, "suzuki", "1973/07/22"),
                new User(2, "kato", "1960/03/15"),
                new User(3, "tanaka", "1991/12/08")
        );
        doReturn(users).when(userMapper).getAll();
        List<User> actual = userService.findAll();
        assertThat(actual).isEqualTo(users);
        verify(userMapper).getAll();
    }

    @Test
    void 存在するユーザーのIDを指定したときに正常にユーザーを取得できること() {
        doReturn(Optional.of(new User(1, "suzuki", "1973/07/22"))).when(userMapper).findById(1);
        User actual = userService.findById(1);
        assertThat(actual).isEqualTo(new User(1, "suzuki", "1973/07/22"));
    }

    @Test
    void 存在しないユーザーのIDを指定したときに例外を返すこと() {
        doReturn(Optional.empty()).when(userMapper).findById(0);
        assertThrows(UserNotFoundException.class, () -> userService.findById(0));
    }
}
