package com.lixingdong.mvc;

import com.lixingdong.domain.Goods;
import com.lixingdong.domain.UserPrice;
import com.lixingdong.service.GoodsService;
import com.lixingdong.service.UserPriceService;
import com.lixingdong.service.bean.HistoryBean;
import com.lixingdong.util.CookieUtil;
import com.lixingdong.util.UUIDGen;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dam on 14-5-6.
 */
@Controller
@RequestMapping("/user-price")
public class UserPriceController {

    @Autowired
    private UserPriceService userPriceService;
    @Autowired
    private GoodsService goodsService;

    @ResponseBody
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public UserPriceBean addUserPrice(UserPrice userPrice,HttpServletResponse response){
        UserPriceBean userPriceBean = new UserPriceBean();
        String userId = userPrice.getUserId();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dealTime = sdf.format(new Date());
        userPrice.setDealTime(dealTime);
        userPriceService.addUserPrice(userPrice);
        UserPrice highestPrice = goodsService.getGoodsHighestPrice(userPrice.getGoodsId());

        userPriceBean.setCurrentUserId(userId);
        userPriceBean.setHighestPrice(highestPrice);

        CookieUtil.addCookie(response,"userIdCookie",userId);

        return userPriceBean;
    }
    @RequestMapping(value = "/tohistory", method = RequestMethod.GET)
    public String toHistory(HttpServletRequest request,Model model) {
        String userId = CookieUtil.findCookie(request,"userIdCookie");
        model.addAttribute("userId",userId);
        return "history";
    }
    @ResponseBody
    @RequestMapping(value = "/history-list/{userId}")
    public List<HistoryBean> getHistory(@PathVariable String userId){
        List<HistoryBean> historyBeanList = new ArrayList<HistoryBean>();
        if(StringUtils.isNotBlank(userId)){
            historyBeanList = userPriceService.getUserHistory(userId);
        }
        return historyBeanList;
    }
    @ResponseBody
    @RequestMapping(value = "/manage/delete/{userId}/{goodsId}")
    public String delete(@PathVariable String userId,@PathVariable String goodsId){
        String result = "error";
        if(StringUtils.isNotBlank(userId)){
            userPriceService.deletePrice(userId,goodsId);
            result = "success";
        }
        return result;
    }
}
