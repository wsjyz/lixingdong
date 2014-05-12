package com.lixingdong.mvc;

import com.lixingdong.domain.Goods;
import com.lixingdong.domain.PageModel;
import com.lixingdong.domain.UserPrice;
import com.lixingdong.service.GoodsService;
import com.lixingdong.util.ConfigurationFile;
import com.lixingdong.util.UUIDGen;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dam on 14-5-6.
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ConfigurationFile confBean;

    @RequestMapping(value = "/toadd")
    public String toAddGoods(@RequestParam String goodsId,Model model) {
        Goods goods = new Goods();
        if(StringUtils.isNotBlank(goodsId)){
            goods = goodsService.getGoodsById(goodsId);
        }
        model.addAttribute("goods",goods);
        return "addgoods";
    }

    @ResponseBody
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addGoods(MultipartHttpServletRequest request){
        String result = "success";
        String goodsId = request.getParameter("goodsId");
        Goods g = new Goods();
        g.setGoodsId(goodsId);
        g.setArea(request.getParameter("area"));
        g.setGoodsName(request.getParameter("goodsName"));
        g.setReservePrice(Long.parseLong(request.getParameter("reservePrice")));
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        g.setAddTime(sdf.format(new Date().getTime()));
        g.setFinishTime(request.getParameter("finishTime"));
        g.setDescription(request.getParameter("description"));
        g.setMarketPrice(request.getParameter("marketPrice"));
        Goods newGoods = goodsService.saveGoods(g);
        if(newGoods != null){
            String path = confBean.getFileUploadPath();
            MultipartFile file = request.getFile("goodsUrlFile");
            String originalFilename = file.getOriginalFilename();
            String fileSuffix = originalFilename.substring(originalFilename.indexOf("."),originalFilename.length());
            File targetFile = new File(path, newGoods.getGoodsId()+fileSuffix);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            try {
                file.transferTo(targetFile);
            } catch (IOException e) {
                result = e.getMessage();
                e.printStackTrace();
            }
        }else{
            result = "创建数据时出错";
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/manage/delete/{goodsId}")
    public String deleteGoods(@PathVariable String goodsId){
        String result = "success";
        goodsService.removeGoods(goodsId);
        return result;
    }
    @RequestMapping(value = "/view/{goodsId}")
    public String getGoodsById(@PathVariable String goodsId,Model model,HttpServletRequest request){
        Goods goods = goodsService.getGoodsById(goodsId);
        //修改状态
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date finishDate = sdf.parse(goods.getFinishTime());
            Calendar finishCal = Calendar.getInstance();
            finishCal.setTime(finishDate);

            Calendar currentCal = Calendar.getInstance();
            if(currentCal.after(finishCal)){
                goods.setStatus("FINISH");
            }else{
                goods.setStatus("AUCTIONING");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.addAttribute("goods",goods);

//        String userId = CookieUtil.findCookie(request,"userIdCookie");
//        model.addAttribute("userId",userId);

        String imgPrefix = confBean.getImgUrlPrefix();
        model.addAttribute("urlPrefix",imgPrefix);
        return "goodsinfo";
    }


    @RequestMapping(value = "/to-list")
    public String getAuctionGoodsList(Model model){
        String imgPrefix = confBean.getImgUrlPrefix();
        model.addAttribute("urlPrefix",imgPrefix);
        return "goodslist";
    }
    @ResponseBody
    @RequestMapping(value = "/get-list")
    public List<Goods> getGoodsList(@RequestParam long start,@RequestParam long end){
        List<Goods> goodsList = goodsService.findGoodsList(start,end);
        return goodsList;
    }
    @RequestMapping(value = "/manage/to-list")
    public String toManageList(Model model){
        return "/manage/goods/list";
    }
    @ResponseBody
    @RequestMapping(value = "/manage/get-list")
    public PageModel getGoodsPage(PageModel ptFromPage,@RequestParam long start,@RequestParam long end){

        List<Goods> goodsList = goodsService.findGoodsList(start,end);
        int count = goodsService.findGoodsCount();
        PageModel pt = new PageModel();
        pt.setsEcho(ptFromPage.getsEcho());
        pt.setiTotalRecords(count);
        pt.setiTotalDisplayRecords(count);
        pt.setAaData(goodsList);
        pt.setiDisplayLength(count);
        return pt;
    }
    @ResponseBody
    @RequestMapping(value = "/get-highest-price/{goodsId}")
    public UserPrice getHighestPrice(@PathVariable String goodsId){
        UserPrice userPrice = goodsService.getGoodsHighestPrice(goodsId);
        return userPrice;
    }
    @RequestMapping(value = "/manage/to-price-list/{goodsId}")
    public String toPriceList(@PathVariable String goodsId,Model model){
        model.addAttribute("goodsId",goodsId);
        return "/manage/goods/pricelist";
    }
    @ResponseBody
    @RequestMapping(value = "/manage/get-price-list/{goodsId}")
    public List<UserPrice> getGoodsPriceList(@PathVariable String goodsId){
        List<UserPrice> priceList = goodsService.findGoodsPriceList(goodsId);
        return priceList;
    }
}
