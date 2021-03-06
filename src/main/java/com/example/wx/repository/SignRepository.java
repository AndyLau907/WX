package com.example.wx.repository;

import com.example.wx.bean.Sign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SignRepository  extends JpaRepository<Sign,String> {
    List<Sign> findAllBySignDateAndAndUserId(Date signDate,String userId);
    List<Sign> findAllByUserDataId(String userDataId);
}
