package com.lixingdong.service;

import com.lixingdong.domain.Goods;
import com.lixingdong.domain.UserPrice;

import java.util.List;

/**
 * Created by dam on 14-5-6.
 */
public interface GoodsService {

    void addGoods(Goods goods);

    Goods getGoodsById(String goodsId);

    UserPrice getGoodsHighestPrice(String goodsId);

    List<Goods> findGoodsList();
}
