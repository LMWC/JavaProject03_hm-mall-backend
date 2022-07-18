package com.hmall.search.web;

import com.hmall.common.dto.PageDTO;
import com.hmall.search.doc.ItemDoc;
import com.hmall.search.dto.RequestParams;
import com.hmall.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;
    //private RestHighLevelClient client;

    //搜索栏自动补全
    @GetMapping("/suggestion")
    public List<String> suggestion(@RequestParam("key") String key) throws IOException {
        return searchService.suggestion(key);
    }


    //过滤项聚合
    @PostMapping("/filters")
    public Map<String,List<String>> filters(@RequestBody RequestParams requestParams) throws IOException {
        return searchService.aggregationByCondition(requestParams);
    }

    //搜索商品
    @PostMapping("/list")
    public PageDTO<ItemDoc> list(@RequestBody RequestParams requestParams) throws IOException {
        log.info("搜索商品前端获取的参数:{}",requestParams.toString());
        return searchService.selectByCondition(requestParams);
    }



}
