package com.information.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users")
    List<User> getAll();

    @Select("SELECT * FROM users WHERE id = #{id}")
    List<User> getId(int id);
}
