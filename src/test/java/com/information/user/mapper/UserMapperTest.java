package com.information.user.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.information.user.User;
import com.information.user.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 全てのユーザーが取得できること() {
        List<User> users = userMapper.getAll();
        assertThat(users)
                .hasSize(3)
                .contains(
                        new User(1, "suzuki", "1973/07/22"),
                        new User(2, "kato", "1960/03/15"),
                        new User(3, "tanaka", "1991/12/08")
                );
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 存在するユーザーのIDを指定したときにユーザーを取得できること() {
        Optional<User> user = userMapper.findById(1);
        assertThat(user).contains(new User(1, "suzuki", "1973/07/22"));
    }

    @Test
    @Transactional
    void 存在しないユーザーのIDを指定したときにOptionalEmptyが返されること() {
        Optional<User> user = userMapper.findById(0);
        assertThat(user).isEmpty();
    }
}
