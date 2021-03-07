package com.example.wx.repository;

import com.example.wx.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {
    List<User> findAllByUserNameAndPassword(String userName,String password);
    List<User> findAllByUserName(String userName);
    List<User> findAllById(String id);
}
