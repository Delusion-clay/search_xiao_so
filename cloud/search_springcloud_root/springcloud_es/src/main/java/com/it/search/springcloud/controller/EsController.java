package com.it.search.springcloud.controller;

import com.it.search.springcloud.service.EsService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-06-09 8:21
 */
@Controller //控制器，转发器
@RequestMapping("/es")
@Api(value = "ES查询")
public class EsController {
    @Resource
    private EsService esService;

    @ResponseBody
    @RequestMapping(value = "/searchAll" ,method = RequestMethod.POST)
    public List<Map<String,Object>> search(
            @RequestParam(name="indexName") String indexName,
            @RequestParam(name="pageNumber") String pageNumber,
            @RequestParam(name="pageSize") String pageSize
    ){
        return esService.search(indexName,pageNumber,pageSize);
    }

    @ResponseBody
    @RequestMapping(value = "/searchByWord",method = {RequestMethod.POST})
    public List<Map<String,Object>> searchByWord(
            @RequestParam(name="keyWord") String keyWord,
            @RequestParam(name="pageNumber") String pageNumber,
            @RequestParam(name="pageSize") String pageSize){
        return esService.searchByWord(keyWord, pageNumber, pageSize);
    }
}
