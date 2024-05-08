package com.mqdemo.controller;

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
    public String actionOcr(MultipartFile file){
        return this.baiduAIService.actionOcr(file);
    }


}
