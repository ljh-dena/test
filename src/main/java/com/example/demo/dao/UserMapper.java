package com.example.demo.dao;

import com.example.demo.annotation.ChooseDataSource;
import com.example.demo.dataSource.AllDatasource;
import com.example.demo.pojo.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    @ChooseDataSource(dataSource = AllDatasource.SECOND)
    User selectByName(String username);

    @ChooseDataSource(dataSource = AllDatasource.SECOND)
    List<User> selectAllUser();

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}