package com.mqdemo.service.impl;

import cn.hutool.core.lang.UUID;
import com.baidu.aip.ocr.AipOcr;
import com.mqdemo.dto.Result;
import com.mqdemo.service.BaiduAIService;
import com.mqdemo.utils.GetIPlocalUtil;
import com.mqdemo.utils.SystemConstants;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

import static com.mqdemo.utils.SystemConstants.MQ_NAME_SERVER;

@Slf4j
@Service
public class BaiduAIServiceImpl implements BaiduAIService {
    @Value("${web.local-host}")
    private String localHost;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("mqadd.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @Override
    public Result actionOcr(MultipartFile multipartFile, String uuid) {

        String host = localHost;

        if (multipartFile == null) {
            return Result.fail("图片不能为空");
        }

        // 处理图片保存到本地Resources-images路径
        String fileUrl = null;
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String storedFileName = UUID.randomUUID().toString(true) + fileExtension;
            Path destinationFile = Paths.get("src/main/resources/images", storedFileName).toAbsolutePath().normalize();
            if (!destinationFile.getParent().toFile().exists()) {
                destinationFile.getParent().toFile().mkdirs();
            }
            multipartFile.transferTo(destinationFile);

            fileUrl = "http://"+host+":8082/images/"+storedFileName;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件存取异常，{}", e.getMessage());
        }


        Map<String, String> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("file_url", fileUrl);
        //stringRedisTemplate.opsForStream().add(MQ_NAME_SERVER, map);
        // 执行lua脚本，添加消息到MQ
        stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                uuid, fileUrl, host
        );
        log.info("MQ添加成功", uuid);
        return Result.ok(map);
    }

}
