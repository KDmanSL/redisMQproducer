package com.mqdemo.service.impl;

import com.baidu.aip.ocr.AipOcr;
import com.mqdemo.service.BaiduAIService;
import com.mqdemo.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Service
public class BaiduAIServiceImpl implements BaiduAIService {
    @Override
    public String actionOcr(MultipartFile multipartFile) {
        AipOcr client = new AipOcr(SystemConstants.APP_ID
                , SystemConstants.API_KEY, SystemConstants.SECRET_KEY);
        HashMap<String, String> options = new HashMap<String, String>(4);
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("probability", "true");

        // 参数为二进制数组
        byte[] buf = new byte[0];
        try {
            buf = multipartFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取文件字节数据异常，{}",e.getMessage());
        }
        JSONObject res = client.basicGeneral(buf, options);
        String jsonData = "";
        try {
            jsonData = res.toString(2);
        } catch (JSONException e) {
            log.error("获取json数据异常，{}",e.getMessage());
        }
        return jsonData;
    }

}
