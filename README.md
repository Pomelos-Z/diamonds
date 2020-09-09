# diamonds

基于分布式的模块之一

### user模块
登录 退出 拦截器校验
- 多点登录记录
- MD5(MD5(pwd) + salt)密码加密
- JWT token验证
- redis缓存用户信息

### order模块
普通的下单模块 非秒杀高并发级别 

`策略模式`获取用户会员折扣信息 配置`线程池`+`观察者模式`处理邮件和短信业务
```java
public OrderResponse order(OrderRequest request) {
    // 事务处理 自定义事务处理配置 注解
    // 1、获取当前下单用户
    UserInfoCache currentUserInfo = webManager.getCurrentUserInfo();
    // 2、生成订单编号
    String orderSN = orderSnGen.generate(currentUserInfo.getUserId());
    // 3、保存订单基本信息数据

    // 4、获取商品数据
    List<String> goodsIdList = request.getGoodsIdList();
    // 5、遍历商品下单
    //    获取用户折扣 -> 判断库存 -> 减少库存 增加销量 -> 保存订单
    String discount = vipDiscountMap.get(currentUserInfo.getVipLevel()).getDiscount();
    goodsIdList.forEach(goodsId -> {
        // 乐观锁 or 队列 秒杀系列的这里暂不考虑 秒杀系列在另外的controller中处理
    });
    // 6、成功后的邮件or推送or短信
    applicationContext.publishEvent(new OrderEvent(this, "message", currentUserInfo));

    OrderResponse response = new OrderResponse();
    response.setSuccess(true);
    return response;
}
```

### 秒杀模块(doing)
针对秒杀活动的模块

考虑到秒杀活动的特殊性，应该将其单独拉出来作为服务单独部署，这样秒杀活动不会影响到我的其他业务的正常进行

此处先暂时写在这里

目前构思阶段：

- 首先考虑的是限流 此处采用自定义注解 + 拦截器 + redis实现校验 

- service Bean预加载时读取商品信息及库存到redis中 `InitializingBean`

- ThreadLocal放置当前用户信息

- 商品预下单redis分布式锁锁住购买的当前商品 
```java
String orderId = null;
try {
    orderId = redisDistributedLockManager.execute(StrUtil.format(CacheKeyConstant.SEC_KILLING_ORDER_LOCK_KEY, goodsId),
            300L, 1000L, () -> orderPerHandle(goodsId, userPhone));
} catch (LockException e) {
    throw new SecKillingException(SecKillingExceptionEnum.SEC_KILLING_FAIL);
}
```

- 然后暂未实现的有：
    - 秒杀页面应尽量简单 减少不必要的资源加载
    - 秒杀页面作为静态资源缓存 redis缓存html
    - 商品详情页面也做缓存
    - 对于抢到的放在mq里慢慢消化
    - 数据库订单id采用雪花算法得到分布式自增id
