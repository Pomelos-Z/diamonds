package com.diamonds.server.request;

import java.util.List;

public class OrderRequest {
    // 或者采用redis来记录用户购物车所选择的商品信息
    private List<String> goodsIdList;

    public List<String> getGoodsIdList() {
        return goodsIdList;
    }

    public void setGoodsIdList(List<String> goodsIdList) {
        this.goodsIdList = goodsIdList;
    }
}
