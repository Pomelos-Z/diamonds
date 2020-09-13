package com.diamonds.server.impl;


import com.diamonds.server.GoodsService;
import com.diamonds.server.dao.GoodsDao;
import com.diamonds.server.domin.GoodsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public List<GoodsInfo> getSecGoodsList() {
        return goodsDao.getSecGoodsList();
    }
}
