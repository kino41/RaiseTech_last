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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
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
        verify(userMapper).findById(1);
    }

    @Test
    void 存在しないユーザーのIDを指定したときに例外を返すこと() {
        doReturn(Optional.empty()).when(userMapper).findById(0);
        assertThrows(UserNotFoundException.class, () -> userService.findById(0));
    }

    @Test
    void 新規のユーザーをIDと紐付けて正常に登録できること() {
        User newUser = new User(4, "hayashi", "1992/03/09");
        userService.insert(newUser);
        verify(userMapper).insert(newUser);
    }

    @Test
    void 存在するユーザーのIDを指定したときに正常にユーザーを更新できること() {
        User existingUser = new User(1, "suzuki", "1973/07/22");
        User updateUser = new User(1, "sato", "1988/04/18");
        doReturn(Optional.of(existingUser)).when(userMapper).findById(1);
        doNothing().when(userMapper).update(updateUser);
        User actual = userService.update(1, "sato", "1988/04/18");
        verify(userMapper).findById(1);
        verify(userMapper).update(updateUser);
        assertThat(actual).isEqualTo(updateUser);
    }

    @Test
    void 存在しないユーザーのIDを指定したときに更新が行われず例外を返すこと() {
        doReturn(Optional.empty()).when(userMapper).findById(0);
        assertThrows(UserNotFoundException.class, () -> userService.update(0, "sato", "1988/04/18"));
        verify(userMapper, never()).update(any(User.class));
        verify(userMapper).findById(0);
    }

    @Test
    void 存在するユーザーのIDを指定したときに正常にユーザーを削除できること() {
        doReturn(Optional.of(new User(1, "suzuki", "1973/07/22"))).when(userMapper).findById(1);
        User actual = userService.findById(1);
        assertThat(actual).isEqualTo(new User(1, "suzuki", "1973/07/22"));
        doReturn(1).when(userMapper).deleteById(1);
        userService.delete(1);
        verify(userMapper).deleteById(1);
    }

    @Test
    void 存在しないユーザーのIDを指定したときに削除が行われず例外を返すこと() {
        doReturn(Optional.empty()).when(userMapper).findById(0);
        assertThrows(UserNotFoundException.class, () -> userService.delete(0));
        verify(userMapper, never()).deleteById(0);
        verify(userMapper).findById(0);
    }
}
