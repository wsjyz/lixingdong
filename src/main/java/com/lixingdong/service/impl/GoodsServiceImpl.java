package com.lixingdong.service.impl;

import com.alibaba.fastjson.JSON;
import com.lixingdong.domain.Goods;
import com.lixingdong.domain.UserPrice;
import com.lixingdong.service.GoodsService;
import com.lixingdong.util.ConfigurationFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.support.collections.RedisCollection;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by dam on 14-5-6.
 */
@Service("GoodsService")
public class GoodsServiceImpl implements GoodsService{

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ConfigurationFile confBean;

    @Override
    public void addGoods(Goods goods) {
        if(goods != null){
            redisTemplate.boundZSetOps(confBean.getGoodsKey()).add(goods.getGoodsId(),new Date().getTime());
            String goodsStr = JSON.toJSONString(goods);
            redisTemplate.boundValueOps(goods.getGoodsId()).set(goodsStr);
        }
    }

    @Override
    public Goods getGoodsById(String goodsId) {
        if(StringUtils.isNotBlank(goodsId)){
            String goodsStr = redisTemplate.boundValueOps(goodsId).get();
            if(StringUtils.isNotBlank(goodsStr)){
                Goods goods = JSON.parseObject(goodsStr, Goods.class);
                return goods;
            }
        }
        return null;
    }

    @Override
    public UserPrice getGoodsHighestPrice(String goodsId) {
        UserPrice userPrice = new UserPrice();
        if(StringUtils.isNotBlank(goodsId)){
            Set<ZSetOperations.TypedTuple<String>> set
                    = redisTemplate.boundZSetOps(goodsId+"-price").reverseRangeWithScores(0,1);
            Iterator<ZSetOperations.TypedTuple<String>> itor = set.iterator();
            if(itor.hasNext()){

                ZSetOperations.TypedTuple<String> typedTuple = itor.next();
                userPrice.setUserPrice(typedTuple.getScore().longValue());
                userPrice.setUserId(typedTuple.getValue());
            }

        }
        return userPrice;
    }

    @Override
    public List<Goods> findGoodsList() {
        List<String> idList = new ArrayList<String>();
        List<Goods> goodsList = new ArrayList<Goods>();
        //先取出ID
        Set<ZSetOperations.TypedTuple<String>> set =
                redisTemplate.boundZSetOps(confBean.getGoodsKey()).reverseRangeWithScores(0,-1);
        Iterator<ZSetOperations.TypedTuple<String>> itor = set.iterator();
        while(itor.hasNext()){
            ZSetOperations.TypedTuple<String> typedTuple = itor.next();
            idList.add(typedTuple.getValue());
        }
        for(String goodsId:idList){
            String goodsStr = redisTemplate.boundValueOps(goodsId).get();
            Goods goods = JSON.parseObject(goodsStr,Goods.class);
            goodsList.add(goods);
        }
        return goodsList;
    }
}
