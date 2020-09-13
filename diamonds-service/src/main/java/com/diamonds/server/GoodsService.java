package com.diamonds.server;

import com.diamonds.server.domin.GoodsInfo;

import java.util.List;

public interface GoodsService {
    List<GoodsInfo> getSecGoodsList();
}
