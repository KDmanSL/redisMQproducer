package com.mqdemo.service;

import com.mqdemo.dto.Result;
import org.springframework.web.multipart.MultipartFile;

public interface BaiduAIService {

    Result actionOcr(MultipartFile multipartFile,String uuid);
}
