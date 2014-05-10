package com.lixingdong.domain;

/**
 * Created by dam on 14-5-6.
 */
public class UserPrice {

    private String userId;
    private String goodsId;
    private long userPrice;
    private boolean isDeal;
    private String dealTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public long getUserPrice() {
        return userPrice;
    }

    public void setUserPrice(long userPrice) {
        this.userPrice = userPrice;
    }

    public boolean isDeal() {
        return isDeal;
    }

    public void setDeal(boolean isDeal) {
        this.isDeal = isDeal;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }
}
