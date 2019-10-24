package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getAllUser(){
        return this.userMapper.selectAllUser();
    }

    @Override
    public User getUserById(int userId){
        return this.userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public User getUserByName(String username){
        return this.userMapper.selectByName(username);
    }

    @Override
    public int insertUser(User user) {
        return this.userMapper.insertSelective(user);
    }

    @Override
    public int deleteUserById(int id) {
        return this.userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateUser(User user) {
        return this.userMapper.updateByPrimaryKeySelective(user);
    }
}
