package com.lixingdong.service.impl;

import com.alibaba.fastjson.JSON;
import com.lixingdong.domain.Goods;
import com.lixingdong.domain.UserPrice;
import com.lixingdong.service.GoodsService;
import com.lixingdong.util.ConfigurationFile;
import com.lixingdong.util.UUIDGen;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
    public Goods saveGoods(Goods goods) {
        if(goods != null){
            if(StringUtils.isBlank(goods.getGoodsId())){
                goods.setGoodsId(UUIDGen.genShortPK());
                redisTemplate.boundZSetOps(confBean.getGoodsKey()).add(goods.getGoodsId(),new Date().getTime());
            }
            String goodsStr = JSON.toJSONString(goods);
            redisTemplate.boundValueOps(goods.getGoodsId()).set(goodsStr);
            return goods;
        }else{
            return null;
        }
    }
    public void increaseFlowerCounts(Goods goods,int step){
        AtomicLong counts = new AtomicLong(goods.getFollowerCounts());
        goods.setFollowerCounts(counts.addAndGet(step));
        saveGoods(goods);
    }

    @Override
    public void removeGoods(String goodsId) {
        int result = redisTemplate.boundZSetOps(confBean.getGoodsKey()).remove(goodsId).intValue();
        if(redisTemplate.hasKey(goodsId)){
            redisTemplate.delete(goodsId);
        }
        //删除文件
        Path filePath = Paths.get(confBean.getFileUploadPath()+"/"+goodsId+".jpg");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
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
    public List<Goods> findGoodsList(long start,long end) {
        List<String> idList = new ArrayList<String>();
        List<Goods> goodsList = new ArrayList<Goods>();
        //先取出ID
        Set<ZSetOperations.TypedTuple<String>> set =
                redisTemplate.boundZSetOps(confBean.getGoodsKey()).reverseRangeWithScores(start,end);
        Iterator<ZSetOperations.TypedTuple<String>> itor = set.iterator();
        while(itor.hasNext()){
            ZSetOperations.TypedTuple<String> typedTuple = itor.next();
            idList.add(typedTuple.getValue());
        }
        List<String> goodsStrList = redisTemplate.opsForValue().multiGet(idList);
        goodsList = (List<Goods>)CollectionUtils.collect(goodsStrList,new Transformer() {
            @Override
            public Object transform(Object o) {
                Goods goods = JSON.parseObject(o.toString(),Goods.class);
                return goods;
            }
        });
        return goodsList;
    }

    @Override
    public List<UserPrice> findGoodsPriceList(String goodsId) {
        List<UserPrice> priceList = new ArrayList<UserPrice>();
        Set<ZSetOperations.TypedTuple<String>> set =
                redisTemplate.boundZSetOps(goodsId+"-price").reverseRangeWithScores(0,-1);
        Iterator<ZSetOperations.TypedTuple<String>> itor = set.iterator();
        while(itor.hasNext()){
            ZSetOperations.TypedTuple<String> typedTuple = itor.next();
            UserPrice userPrice = new UserPrice();
            userPrice.setUserId(typedTuple.getValue());
            userPrice.setUserPrice(typedTuple.getScore().longValue());
            priceList.add(userPrice);
        }
        return priceList;
    }

    @Override
    public int findGoodsCount() {
        return redisTemplate.boundZSetOps(confBean.getGoodsKey()).size().intValue();
    }

}
