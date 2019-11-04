package com.example.demo.controller;

import com.example.demo.dataSource.DynamicDataSourceContextHolder;
import com.example.demo.pojo.User;
import com.example.demo.service.IUserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;

@RestController
public class myControllerTest {

    @Autowired
    @Resource
    private IUserService userService;

    @RequestMapping(value = "/hello")
    @ResponseBody
    public  String myTest(HttpServletRequest request){
        System.out.println("hello");
        return "hello !";
    }

    @RequestMapping(value = "/insertUser")
    @ResponseBody
    public  String insertUser(HttpServletRequest request) {
        User user = new User();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        user.setUsername(username);
        user.setPassword(password);
        int i = userService.insertUser(user);
        if (i == 1) {
            System.out.println("Insert user success!");
            return "Insert user success!";
        }else {
            System.out.println("Insert user failed!");
            return "Insert user failed!";
        }
    }

    @RequestMapping(value = "/deletetUser")
    @ResponseBody
    public  String deleteUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        int i = userService.deleteUserById(id);
        if (i == 1) {
            System.out.println("Delete user success!");
            return "Delete user success!";
        }else {
            System.out.println("Delete user failed!");
            return "Delete user failed!";
        }
    }

    @RequestMapping(value = "/updateUser")
    @ResponseBody
    public  String updateUser(HttpServletRequest request) {
        User user = new User();
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        int i = userService.updateUser(user);
        if (i == 1) {
            System.out.println("Update user success!");
            return "Update user success!";
        }else {
            System.out.println("Update user failed!");
            return "Update user failed!";
        }
    }

    @RequestMapping(value = "/showUsers")
    @ResponseBody
    public  List<User> showUsers(HttpServletResponse response) throws JSONException {
        List<User> users = userService.getAllUser();
        JSONObject rspJson = new JSONObject();
        rspJson.put("data", users);
        System.out.println(rspJson);
        return users;
    }

    @RequestMapping(value = "/selectUser")
    @ResponseBody
    public  User selectUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = userService.getUserById(id);
        String result = "id:" + user.getId() + "  username: " + user.getUsername() + "  creatTime: " + user.getCreatTime();
        System.out.println(user);
        return user;
    }
}
