package com.example.wx.repository;

import com.example.wx.bean.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftRepository extends JpaRepository<Gift,String> {
    List<Gift>  findAllById(String id);
}
