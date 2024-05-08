package com.mqdemo.service;

import org.springframework.web.multipart.MultipartFile;

public interface BaiduAIService {

    String actionOcr(MultipartFile multipartFile);
}
