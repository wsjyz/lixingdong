package com.lixingdong.mvc;

import com.lixingdong.util.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
@RequestMapping("/")
public class AuctionController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuctionController.class);

    // 自定义 token
    private String TOKEN = "weixin";

	@RequestMapping(value = "/")
	public String printWelcome(ModelMap model,@RequestParam("signature") String signature,
                               @RequestParam("echostr") String echostr,
                               @RequestParam("timestamp") String timestamp,
                               @RequestParam("nonce") String nonce) {
        String[] str = { TOKEN, timestamp, nonce };
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        String digest = DigestUtils.SHA1(bigStr).toLowerCase();
        LOGGER.debug("signature:"+signature+"--"+digest+"--"+digest.equals(signature));
        // 确认请求来至微信
        if (digest.equals(signature)) {
            return echostr;
        }
        return echostr;
	}

}