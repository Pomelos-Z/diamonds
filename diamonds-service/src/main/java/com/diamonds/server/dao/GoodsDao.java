package com.diamonds.server.dao;

import com.diamonds.server.domin.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.price from sec_killing_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsInfo> getSecGoodsList();
}
