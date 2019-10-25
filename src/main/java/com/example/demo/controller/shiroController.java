package com.example.demo.controller;

import com.example.demo.pojo.User;
import com.example.demo.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class shiroController {
    @Resource
    private IUserService userService;

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserInfoById(@PathVariable String username) {
        Map<String, Object> map = new HashMap<>(16);
        User user = userService.getUserByName(username);
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("creatTime", user.getCreatTime());
        map.put("permission", user.getPermission());
        return map;
    }

    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public Object toLogin() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", 200);
        map.put("msg", "未登录");
        return map;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestParam String username, String password) {
        //  获取Subject
        Subject subject = SecurityUtils.getSubject();
//        System.out.println(subject.isPermitted("admin"));
        //  封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //  执行登录方法
//        return token;
        try {
            subject.login(token);
            return "redirect:/index";
        } catch (UnknownAccountException e) {
            //  登录失败：用户名不存在
            return "用户名不存在";
        } catch (IncorrectCredentialsException e) {
            //  登录失败：密码错误
            return "密码错误";
        }
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public Object index() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", 100);
        map.put("msg", "您登录成功");
        return map;
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public String logout() {
        return "logout";
    }

    @RequestMapping(value = "/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @RequestMapping(value = "/noAuth", method = RequestMethod.GET)
    @ResponseBody
    public Object noAuth() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", 200);
        map.put("msg", "您没有该授权");
        return map;
    }


}
