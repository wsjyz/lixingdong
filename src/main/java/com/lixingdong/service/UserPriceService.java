package com.lixingdong.service;

import com.lixingdong.domain.UserPrice;
import com.lixingdong.service.bean.HistoryBean;

import java.util.List;

/**
 * Created by dam on 14-5-6.
 */
public interface UserPriceService {

    void addUserPrice(UserPrice userPrice);

    List<HistoryBean> getUserHistory(String userId);

    void deletePrice(String userId,String goodsId);
}
