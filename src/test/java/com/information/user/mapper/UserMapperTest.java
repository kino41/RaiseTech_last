package com.information.user.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
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

import static com.information.user.User.createUser;
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

    @Test
    @DataSet(value = "datasets/users.yml")
    @ExpectedDataSet(value = "datasets/insertUsers.yml", ignoreCols = "id")
    @Transactional
    void 新規のユーザーの名前と生年月日を登録できること() {
        User user = createUser("hayashi", "1992/03/09");
        userMapper.insert(user);

        Integer userId = user.getId();

        Optional<User> insertedUser = userMapper.findById(userId);
        assertThat(insertedUser).isPresent();
        assertThat(insertedUser.get().getName()).isEqualTo("hayashi");
        assertThat(insertedUser.get().getBirthdate()).isEqualTo("1992/03/09");
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @ExpectedDataSet(value = "datasets/updateUsers.yml")
    @Transactional
    void 存在するユーザーのIDを指定したときにユーザーを更新できること() {
        Optional<User> optionalUser = userMapper.findById(1);
        assertThat(optionalUser).isPresent();
        User existingUser = optionalUser.get();
        assertThat(existingUser.getName()).isNotEqualTo("sato");
        assertThat(existingUser.getBirthdate()).isNotEqualTo("1988/04/18");

        User user = new User(1, "sato", "1988/04/18");
        userMapper.update(user);

        Optional<User> updateOptionalUser = userMapper.findById(1);
        assertThat(updateOptionalUser).isPresent();
        User updatedUser = updateOptionalUser.get();
        assertThat(updatedUser.getName()).isEqualTo("sato");
        assertThat(updatedUser.getBirthdate()).isEqualTo("1988/04/18");
    }

    @Test
    @Transactional
    void 存在しないユーザーのIDを指定したときに更新が行われないこと() {
        Optional<User> user = userMapper.findById(0);
        assertThat(user).isEmpty();

        userMapper.update(new User(0, "sato", "1988/04/18"));

        Optional<User> updateUser = userMapper.findById(0);
        assertThat(updateUser).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @ExpectedDataSet(value = "datasets/deleteUsers.yml")
    @Transactional
    void 存在するユーザーのIDを指定したときにユーザーを削除できること() {
        Optional<User> optionalUser = userMapper.findById(1);
        assertThat(optionalUser).isPresent();

        User user = optionalUser.get();
        int deletedRowCount = userMapper.deleteById(user.getId());
        assertThat(deletedRowCount).isEqualTo(1);

        Optional<User> deleteOptionalUser = userMapper.findById(user.getId());
        assertThat(deleteOptionalUser).isEmpty();
    }

    @Test
    @Transactional
    void 存在しないユーザーのIDを指定したときに削除が行われないこと() {
        Optional<User> user = userMapper.findById(0);
        assertThat(user).isEmpty();

        int deletedRowCount = userMapper.deleteById(0);
        assertThat(deletedRowCount).isZero();
    }
}
