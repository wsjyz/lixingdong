package com.lixingdong.service;

import com.lixingdong.domain.Goods;
import com.lixingdong.domain.UserPrice;

import java.util.List;

/**
 * Created by dam on 14-5-6.
 */
public interface GoodsService {

    Goods saveGoods(Goods goods);

    void removeGoods(String goodsId);

    Goods getGoodsById(String goodsId);

    UserPrice getGoodsHighestPrice(String goodsId);

    List<Goods> findGoodsList(long start,long end);

    List<UserPrice> findGoodsPriceList(String goodsId);

    int findGoodsCount();
}
