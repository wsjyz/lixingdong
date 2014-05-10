package com.lixingdong.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by dam on 14-5-8.
 */
@Component("ConfBean")
public class ConfigurationFile {
    //竞拍品在redis中的ZSET KEY
    @Value("${goods.key}")
    private String goodsKey;
    //图片预览前缀
    @Value("${pic.url}")
    private String imgUrlPrefix;
    //图片上传路径
    @Value("${pic.upload.path}")
    private String fileUploadPath;

    public String getGoodsKey() {
        return goodsKey;
    }

    public String getImgUrlPrefix() {
        return imgUrlPrefix;
    }

    public String getFileUploadPath() {
        return fileUploadPath;
    }
}
