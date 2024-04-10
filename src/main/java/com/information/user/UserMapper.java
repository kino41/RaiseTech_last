package com.information.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users")
    List<User> getAll();

    @Select("SELECT * FROM users WHERE id = #{id}")
    Optional<User> getById(int id);

    @Insert("INSERT INTO users (name, birthdate) VALUES(#{name}, #{birthdate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Update("UPDATE users SET name = #{name}, birthdate = #{birthdate} WHERE id = #{id}")
    void update(User user);

    @Select("SELECT * FROM users WHERE id = #{id}")
    Optional<User> findById(int id);
}
