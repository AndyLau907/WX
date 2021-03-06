package com.example.wx.repository;

import com.example.wx.bean.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDataRepository extends JpaRepository<UserData,String> {
    List<UserData> findAllByIsActiveAndUserId(int isActive,String userId);
}
