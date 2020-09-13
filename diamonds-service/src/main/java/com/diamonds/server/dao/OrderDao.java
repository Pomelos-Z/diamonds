package com.diamonds.server.dao;

import com.diamonds.server.domin.OrderInfo;
import com.diamonds.server.domin.SecKillingOrderInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDao {

    @Select("select * from sec_killing_order where user_id=#{userPhone} and goods_id=#{goodsId}")
    public SecKillingOrderInfo getSecKillingOrderByUserIdGoodsId(@Param("userNickName") String userPhone, @Param("goodsId") String goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into sec_killing_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    public int insertSecKillingOrder(SecKillingOrderInfo orderInfo);

    @Select("select * from order_info where order_sn = #{orderSn}")
    public OrderInfo getOrderBySn(@Param("orderSn") String orderSn);

    @Select("select * from order_info where status=#{status} and create_Date<=#{createDate}")
    public List<OrderInfo> selectOrdersByStatusCreateTime(@Param("status") Integer status, @Param("createDate") String createDate);

    @Update("update order_info set status=0 where id=#{id}")
    public int closeOrderByOrderInfo();
}
