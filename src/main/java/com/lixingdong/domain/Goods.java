package com.lixingdong.domain;

/**
 * Created by dam on 14-5-6.
 */
public class Goods {

    private String goodsId;
    private String goodsName;
    private String goodsUrl;
    private String area;
    private long followerCounts;
    private String status;
    private long reservePrice;//保留价
    private long highestPrice;//最高价
    private String addTime;//添加时间
    private String finishTime;//竞拍结束时间
    private String marketPrice;//市场价
    private String description;//物品描述

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getFollowerCounts() {
        return followerCounts;
    }

    public void setFollowerCounts(long followerCounts) {
        this.followerCounts = followerCounts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(long reservePrice) {
        this.reservePrice = reservePrice;
    }

    public long getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(long highestPrice) {
        this.highestPrice = highestPrice;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
