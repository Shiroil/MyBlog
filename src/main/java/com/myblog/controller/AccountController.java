package com.myblog.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.common.dto.LoginDto;
import com.myblog.common.lang.Result;
import com.myblog.entity.Users;
import com.myblog.service.UsersService;
import com.myblog.util.JwtUtils;
import com.myblog.util.UuidUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    @Autowired
    UsersService usersService;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){
        Users user = usersService.getOne(new QueryWrapper<Users>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");
        if(!user.getPassword().equals(loginDto.getPassword())){
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(Long.parseLong(user.getId()));
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        return Result.success(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .map());
    }


    @PostMapping("/register")
    public Result register(@Validated @RequestBody LoginDto loginDto){
        Users user = usersService.getOne(new QueryWrapper<Users>().eq("username", loginDto.getUsername()));
        Assert.isNull(user, "用户名已存在");
        Users user_register = new Users();
        user_register.setUsername(loginDto.getUsername());
        user_register.setPassword(loginDto.getPassword());

        user_register.setPower("1");
        usersService.save(user_register);
        return Result.success("注册成功");
    }

    @RequiresAuthentication
    @RequestMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.success("注销成功");
    }
}
