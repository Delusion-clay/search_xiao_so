package com.it.search.springcloud.service;

import com.it.search.springcloud.es.jest.jestservice.JestService;
import com.it.search.springcloud.es.jest.jestservice.ResultParse;
import io.searchbox.client.JestClient;
import io.searchbox.core.SearchResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-06-09 8:19
 */
@Service
public class EsService {
    public List<Map<String,Object>> search(
            String indexName,
            String pageNumber,
                                           String pageSize){
        List<Map<String,Object>> list = new ArrayList<>();
        JestClient jestClient = null;

        Integer pageNumber_num  = Integer.valueOf(pageNumber);
        Integer pageSize_num  = Integer.valueOf(pageSize);

        try {
            jestClient = JestService.getJestClient();
            SearchResult search = JestService.search(jestClient,
                    indexName,
                    pageNumber_num,
                    pageSize_num);
            list = ResultParse.parseSearchResultOnly(search);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JestService.closeJestClient(jestClient);
        }
        return list;


    }


    public List<Map<String,Object>> searchAll( String indexName,
                                                 String typeName,
                                                 String keyWord,
                                                 String pageNumber,
                                                 String pageSize){

        List<Map<String,Object>> list = new ArrayList<>();
        JestClient jestClient = null;

        Integer pageNumber_num  = Integer.valueOf(pageNumber);
        Integer pageSize_num  = Integer.valueOf(pageSize);

        try {
            jestClient = JestService.getJestClient();
            SearchResult search = JestService.searchAll(jestClient,
                    indexName,
                    typeName,
                    keyWord,
                    pageNumber_num,
                    pageSize_num);
            list = ResultParse.parseSearchResultOnly(search);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JestService.closeJestClient(jestClient);
        }
        return list;

    }

    public List<Map<String, Object>> searchByWord(String keyWord, String pageNumber, String pageSize) {
        List<Map<String,Object>> list = new ArrayList<>();
        JestClient jestClient = null;

        Integer pageNumber_num  = Integer.valueOf(pageNumber);
        Integer pageSize_num  = Integer.valueOf(pageSize);

        try {
            jestClient = JestService.getJestClient();
            SearchResult search = JestService.searchByWord(jestClient,
                    keyWord,
                    pageNumber_num,
                    pageSize_num);
            list = ResultParse.parseSearchResultOnly(search);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JestService.closeJestClient(jestClient);
        }
        return list;
    }
}
