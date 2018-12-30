package com.springcloud.eshop.data.link.server.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springcloud.eshop.common.server.support.cache.JedisUtil;
import com.springcloud.eshop.common.server.support.utils.ServerResponse;
import com.springcloud.eshop.data.link.server.service.feign.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@Api(description = "直连服务相关的接口")
public class DataLinkController {
    @Autowired
    private JedisUtil.Strings redisTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation("直连服务调用的接口")
    @GetMapping("/product")
    public String getProduct(String productId) {
        //TODO 这里可以先从ecache缓存中获取数据
//        ServerResponse response = restTemplate.getForObject("http://192.168.1.125:8774?productId=" + productId, ServerResponse.class);
//        return response.getData().toString();

        //1. 根据商品id获取数据
        log.info("直连服务调用的接口");
        Object obj = redisTemplate.get("eshop:dynamic:product:" + productId);
        //2. 如果数据为空, 通过feign 远程调用服务接口
        if (obj == null || "".equals(JSON.toJSONString(obj))) {
            ServerResponse response = productService.findByProductId(productId);
            String data = JSON.toJSONString(response.getData());
            JSONObject jsonData = JSONObject.parseObject(data);
            if (jsonData != null && jsonData.size() != 0) {
                //查询商品属性
                ServerResponse propertyResponse
                        = productService.findProductPropertyByProductId(productId);
                if (propertyResponse != null) {
                    jsonData.put("productProperty", JSONObject.parseObject(JSON.toJSONString(propertyResponse.getData())));
                }
                //查询商品规格
                ServerResponse specificationResponse
                        = productService.findProductSpecificationByProductId(productId);
                if (specificationResponse != null) {
                    jsonData.put("productSpecification", JSONObject.parseObject(JSON.toJSONString(specificationResponse.getData())));
                }
                //3. 查询到数据后存储到redis缓存中并返回结果
                redisTemplate.set("eshop:dynamic:product:" + productId, jsonData.toJSONString());
                return jsonData.toJSONString();
            }
            return "";
        }
        //4  如果数据不为空直接返回数据
        return JSON.toJSONString(obj);
    }
}
