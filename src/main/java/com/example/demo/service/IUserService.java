package com.example.demo.service;

import com.example.demo.pojo.User;

import java.util.List;

public interface IUserService {

    public List<User> getAllUser();

    public User getUserById(int userId);

    public User getUserByName(String username);

    public int insertUser(User user);

    public int deleteUserById(int id);

    public int updateUser(User user);

}
