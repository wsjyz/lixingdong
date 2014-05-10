package com.lixingdong.service.bean;

import com.lixingdong.domain.Goods;
import com.lixingdong.domain.UserPrice;

/**
 * Created by dam on 14-5-8.
 */
public class HistoryBean {

    private Goods goods;
    private UserPrice userPrice;
    private String result;
    private String isFinish;

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public UserPrice getUserPrice() {
        return userPrice;
    }

    public void setUserPrice(UserPrice userPrice) {
        this.userPrice = userPrice;
    }
}
