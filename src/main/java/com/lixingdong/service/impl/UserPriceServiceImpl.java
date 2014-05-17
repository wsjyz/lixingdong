package com.lixingdong.service.impl;

import com.alibaba.fastjson.JSON;
import com.lixingdong.domain.Goods;
import com.lixingdong.domain.UserPrice;
import com.lixingdong.service.GoodsService;
import com.lixingdong.service.UserPriceService;
import com.lixingdong.service.bean.HistoryBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dam on 14-5-6.
 */
@Service("UserPriceService")
public class UserPriceServiceImpl implements UserPriceService {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private GoodsService goodsService;

    @Override
    public void addUserPrice(UserPrice userPrice) {
        if(userPrice != null){
            redisTemplate.boundZSetOps(userPrice.getGoodsId()+"-price").
                    add(userPrice.getUserId(), userPrice.getUserPrice());
            String userPriceStr = JSON.toJSONString(userPrice);
            redisTemplate.boundListOps(userPrice.getUserId()+"-price").leftPush(userPriceStr);
        }
    }

    @Override
    public List<HistoryBean> getUserHistory(String userId) {
        List<HistoryBean> beanList = new ArrayList<HistoryBean>();
        List<String> priceList = redisTemplate.boundListOps(userId+"-price").range(0,-1);

        return (List<HistoryBean>)CollectionUtils.collect(priceList,new Transformer() {
            @Override
            public Object transform(Object o) {
                UserPrice userPrice = JSON.parseObject(o.toString(), UserPrice.class);
                String goodsStr = redisTemplate.boundValueOps(userPrice.getGoodsId()).get();
                Goods goods = JSON.parseObject(goodsStr,Goods.class);
                HistoryBean historyBean = new HistoryBean();
                historyBean.setGoods(goods);
                historyBean.setUserPrice(userPrice);
                String isFinish = timeIsFinish(goods.getFinishTime());
                historyBean.setIsFinish(isFinish);
                if(isFinish.equals("FINISH")){
                   UserPrice highestPrice = goodsService.getGoodsHighestPrice(goods.getGoodsId());
                   if(highestPrice.getUserId().equals(userPrice.getUserId())){
                        historyBean.setResult("恭喜！竞拍成功");
                   }else{
                       historyBean.setResult("竞拍未成功");
                   }
                }else{
                    historyBean.setResult("竞拍未结束");
                }
                return historyBean;
            }
        });
    }

    @Override
    public void deletePrice(String userId,String goodsId) {
        redisTemplate.boundZSetOps(goodsId +"-price").remove(userId);
        redisTemplate.boundListOps(userId+"-price").remove(1,goodsId);
    }

    private String timeIsFinish(String finishTimeStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date finishDate = sdf.parse(finishTimeStr);
            Calendar finishCal = Calendar.getInstance();
            finishCal.setTime(finishDate);

            Calendar currentCal = Calendar.getInstance();
            if(currentCal.after(finishCal)){
                return "FINISH";
            }else{
                return "AUCTIONING";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
