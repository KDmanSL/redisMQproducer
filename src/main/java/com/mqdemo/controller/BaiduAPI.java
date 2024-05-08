package com.mqdemo.controller;

import cn.hutool.core.lang.UUID;
import com.mqdemo.dto.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.mqdemo.service.BaiduAIService;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class BaiduAPI {

    @Resource
    private BaiduAIService baiduAIService;

    @PostMapping("/actionOcr")
    @ResponseBody
    public Result actionOcr(@RequestParam("file") MultipartFile file){
        String uuid1 = UUID.randomUUID().toString(true);
        return baiduAIService.actionOcr(file,uuid1);
    }

}
