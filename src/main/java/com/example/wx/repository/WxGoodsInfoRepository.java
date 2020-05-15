package com.example.wx.repository;

import com.example.wx.bean.WxGoodsInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WxGoodsInfoRepository extends JpaRepository<WxGoodsInfo, String> {

    List<WxGoodsInfo> findAllByOOrderByCreateTimeDesc();
}
