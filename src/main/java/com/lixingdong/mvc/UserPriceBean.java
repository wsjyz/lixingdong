package com.lixingdong.mvc;

import com.lixingdong.domain.UserPrice;

/**
 * Created by dam on 14-5-7.
 */
public class UserPriceBean {

    private String currentUserId;

    private UserPrice highestPrice;

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public UserPrice getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(UserPrice highestPrice) {
        this.highestPrice = highestPrice;
    }
}
