
package com.it.search.es.jest.jestservice;

import com.google.gson.GsonBuilder;
import com.it.search.utils.FileCommon;
import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.params.Parameters;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class JestService {

    private static Logger LOG = LoggerFactory.getLogger(JestService.class);

    public static void main(String[] args) throws Exception {

        JestService.testscroll("wechat_20091106","wechat_20091106");
        /*JestClient jestClient = null;
        Map<String, Long> stringLongMap = null;
        try {
            jestClient = JestService.getJestClient();
            SearchResult aggregation = JestService.aggregation(jestClient, "wechat", "wechat", "collect_time");
            stringLongMap = ResultParse.parseAggregation(aggregation);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JestService.closeJestClient(jestClient);
        }
        System.out.println(stringLongMap);*/
    }

    public static void testscroll(String indexName,String typeName){
        JestClient jestClient = JestService.getJestClient();

        BoolQueryBuilder boolbuilder = QueryBuilders.boolQuery();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(3);
        searchSourceBuilder.query(boolbuilder);

        Search search = new Search
                .Builder(searchSourceBuilder.toString())
                .addIndex(indexName)
                .addType(typeName)
                .setParameter(Parameters.SCROLL, "10m")
                .build();

        try {
            int sum = 0, loop = 0;
            JestResult result = jestClient.execute(search);

            //??????????????????????????????????????????
            while(result.isSucceeded() && result.getSourceAsObjectList(Map.class).size() > 0) {
                List<Map> record = result.getSourceAsObjectList(Map.class);
                sum += record.size();
                System.out.println("currentLoopCount???" + (++loop) + ", queryResult???" + record.size() + ", total???" + sum);
                //????????????ID
                String scrollId = result.getJsonObject().get("_scroll_id").getAsString();
                SearchScroll scroll = new SearchScroll.Builder(scrollId, "10m").build();
                result = jestClient.execute(scroll);
                System.out.println(result.getJsonObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *  HTTP??????  9200??????
     * ??????JestClient??????
     * @return
     */
    public static JestClient getJestClient() {

        //????????????JestClientFactory ?????????
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://had-12:9200/")
                .gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create())
                .connTimeout(1500)
                .readTimeout(3000)
                .multiThreaded(true)
                .build());
        return factory.getObject();
    }





/*


/**
     * ??????????????????????????????
     * @param jestClient
     * @param indexName
     * @param typeName
     * @return
     * @throws Exception
     */

    public static Long count(JestClient jestClient,
                             String indexName,
                             String typeName) throws Exception {
        Count count = new Count.Builder().addIndex(indexName).addType(typeName).build();
        CountResult results = jestClient.execute(count);
        return results.getCount().longValue();
    }


     /**
     * ??????????????????
     * @param jestClient
     * @param indexName
     * @param typeName
     * @param field
     * @return
     * @throws Exception
     */
    public static SearchResult  aggregation(JestClient jestClient, String indexName, String typeName, String field) throws Exception {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //????????????API
        AggregationBuilder group1 = AggregationBuilders.terms("group1").field(field);
        //AggregationBuilder group2 = group1.subAggregation(AggregationBuilders.terms("group2").field(""));
        searchSourceBuilder.aggregation(group1);
        searchSourceBuilder.size(0);
        System.out.println(searchSourceBuilder.toString());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(indexName)
                .addType(typeName).build();
        SearchResult result = jestClient.execute(search);
        return result;
    }


    //????????????
    public static SearchResult search(
            JestClient jestClient,
            String indexName,
            String typeName,
            String field,
            String fieldValue,
            String sortField,
            String sortValue,
            int pageNumber,
            int pageSize,
            String[] includes) {
        //?????????????????????  ???????????????????????????
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // ?????????????????????
        searchSourceBuilder.fetchSource(includes,new String[0]);
        //???????????????
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(StringUtils.isEmpty(field)){
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }else{
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery(field,fieldValue));
        }

        searchSourceBuilder.query(boolQueryBuilder);
        //????????????
        //?????????????????????
        searchSourceBuilder.from((pageNumber-1)*pageSize);
        searchSourceBuilder.size(pageSize);

        //????????????
        if("desc".equals(sortValue)){
            searchSourceBuilder.sort(sortField,SortOrder.DESC);
        }else{
            searchSourceBuilder.sort(sortField,SortOrder.ASC);
        }

        System.out.println("sql =====" + searchSourceBuilder.toString());

        //???????????????????????????
        Search.Builder builder = new Search.Builder(searchSourceBuilder.toString());
        //??????indexName typeName
        if(StringUtils.isNotBlank(indexName)){
            builder.addIndex(indexName);
        }
        if(StringUtils.isNotBlank(typeName)){
            builder.addType(typeName);
        }

        Search build = builder.build();
        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(build);
        } catch (IOException e) {
            LOG.error("????????????",e);
        }
        return searchResult;
    }






    /**
     *
     * @param jestClient
     * @param indexName
     * @param typeName
     * @param field
     * @param fieldValue
     * @param sortField
     * @param sortValue
     * @param pageNumber
     * @param pageSize
     * @return
     */
    //????????????
    public static SearchResult search(
            JestClient jestClient,
            String indexName,
            String typeName,
            String field,
            String fieldValue,
            String sortField,
            String sortValue,
            int pageNumber,
            int pageSize) {
        //?????????????????????  ???????????????????????????
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //??????bool????????? ????????????????????????
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //??????????????????
        if(StringUtils.isEmpty(field)){
            //????????????
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }else{
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field,fieldValue));
        }

        searchSourceBuilder.query(boolQueryBuilder);
        //????????????
        //?????????????????????
        searchSourceBuilder.from((pageNumber-1)*pageSize);
        searchSourceBuilder.size(pageSize);
        //????????????
        if("desc".equals(sortValue)){
            searchSourceBuilder.sort(sortField,SortOrder.DESC);
        }else{
            searchSourceBuilder.sort(sortField,SortOrder.ASC);
        }
        System.out.println("sql =====" + searchSourceBuilder.toString());
        //???????????????????????????  ????????????????????????
        Search.Builder builder = new Search.Builder(searchSourceBuilder.toString());

        //??????indexName typeName
        if(StringUtils.isNotBlank(indexName)){
            builder.addIndex(indexName);
        }
        if(StringUtils.isNotBlank(typeName)){
            builder.addType(typeName);
        }

        Search build = builder.build();
        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(build);
        } catch (IOException e) {
            LOG.error("????????????",e);
        }
        return searchResult;
    }


    /**
     * ????????????????????????
     *
     * @param jestClient
     * @param indexName
     * @return
     * @throws Exception
     */
    public static boolean indexExists(JestClient jestClient, String indexName) {
        JestResult result = null;
        try {
            Action action = new IndicesExists.Builder(indexName).build();
            result = jestClient.execute(action);
        } catch (IOException e) {
            LOG.error(null, e);
        }
        return result.isSucceeded();
    }


    /**
     * ????????????
     *
     * @param jestClient
     * @param indexName
     * @return
     * @throws Exception
     */
    public static boolean createIndex(JestClient jestClient, String indexName) throws Exception {

        if (!JestService.indexExists(jestClient, indexName)) {
            JestResult jr = jestClient.execute(new CreateIndex.Builder(indexName).build());
            return jr.isSucceeded();
        } else {
            LOG.info("?????????????????????");
            return false;
        }
    }

    public static boolean createIndexWithSettingsMapAndMappingsString(JestClient jestClient, String indexName, String type, String path) throws Exception {

        // String mappingJson = "{\"type1\": {\"_source\":{\"enabled\":false},\"properties\":{\"field1\":{\"type\":\"keyword\"}}}}";
        String mappingJson = FileCommon.getAbstractPath(path);
        String realMappingJson = "{" + type + ":" + mappingJson + "}";
        System.out.println(realMappingJson);
        CreateIndex createIndex = new CreateIndex.Builder(indexName)
                .mappings(realMappingJson)
                .build();
        JestResult jr = jestClient.execute(createIndex);
        return jr.isSucceeded();
    }


    /**
     * Put??????
     *
     * @param jestClient
     * @param indexName
     * @param typeName
     * @param source
     * @return
     * @throws Exception
     */
    public static boolean createIndexMapping(JestClient jestClient, String indexName, String typeName, String source) throws Exception {

        PutMapping putMapping = new PutMapping.Builder(indexName, typeName, source).build();
        JestResult jr = jestClient.execute(putMapping);
        return jr.isSucceeded();
    }

    /**
     * Get??????
     *
     * @param jestClient
     * @param indexName
     * @param typeName
     * @return
     * @throws Exception
     */
    public static String getIndexMapping(JestClient jestClient, String indexName, String typeName) throws Exception {

        GetMapping getMapping = new GetMapping.Builder().addIndex(indexName).addType(typeName).build();
        JestResult jr = jestClient.execute(getMapping);
        return jr.getJsonString();
    }

    /**
     * ????????????
     *
     * @param jestClient
     * @param indexName
     * @param typeName
     * @return
     * @throws Exception
     */
    public static boolean index(JestClient jestClient, String indexName, String typeName, String idField, List<Map<String, Object>> listMaps) throws Exception {

        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(indexName).defaultType(typeName);
        for (Map<String, Object> map : listMaps) {
            if (map != null && map.containsKey(idField)) {
                Object o = map.get(idField);
                Index index = new Index.Builder(map).id(map.get(idField).toString()).build();
                bulk.addAction(index);
            }
        }
        BulkResult br = jestClient.execute(bulk.build());
        return br.isSucceeded();
    }


    /**
     * ????????????
     *
     * @param jestClient
     * @param indexName
     * @param typeName
     * @return
     * @throws Exception
     */
    public static boolean indexString(JestClient jestClient, String indexName, String typeName, String idField, List<Map<String, String>> listMaps) throws Exception {
        if (listMaps != null && listMaps.size() > 0) {
            Bulk.Builder bulk = new Bulk.Builder().defaultIndex(indexName).defaultType(typeName);
            for (Map<String, String> map : listMaps) {
                if (map != null && map.containsKey(idField)) {
                    Index index = new Index.Builder(map).id(map.get(idField)).build();
                    bulk.addAction(index);
                }
            }
            BulkResult br = jestClient.execute(bulk.build());
            return br.isSucceeded();
        } else {
            return false;
        }

    }

    /**
     * ????????????
     *
     * @param jestClient
     * @param indexName
     * @param typeName
     * @return
     * @throws Exception
     */
    public static boolean indexOne(JestClient jestClient, String indexName, String typeName, String id, Map<String, Object> map) {
        Index.Builder builder = new Index.Builder(map);
        builder.id(id);
        builder.refresh(true);
        Index index = builder.index(indexName).type(typeName).build();
        try {
            JestResult result = jestClient.execute(index);
            if (result != null && !result.isSucceeded()) {
                throw new RuntimeException(result.getErrorMessage() + "????????????????????????!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * ????????????
     *
     * @param jestClient
     * @param indexName
     * @param typeName
     * @param query
     * @return
     * @throws Exception
     */
    public static SearchResult search(JestClient jestClient, String indexName, String typeName, String query) throws Exception {

        Search search = new Search.Builder(query)
                .addIndex(indexName)
                .addType(typeName)
                .build();
        return jestClient.execute(search);
    }







    /**
     * Get??????
     *
     * @param jestClient
     * @param indexName
     * @param typeName
     * @param id
     * @return
     * @throws Exception
     */
    public static JestResult get(JestClient jestClient, String indexName, String typeName, String id) throws Exception {

        Get get = new Get.Builder(indexName, id).type(typeName).build();
        return jestClient.execute(get);
    }


    /**
     * Delete??????
     *
     * @param jestClient
     * @param indexName
     * @return
     * @throws Exception
     */
    public boolean delete(JestClient jestClient, String indexName) throws Exception {

        JestResult jr = jestClient.execute(new DeleteIndex.Builder(indexName).build());
        return jr.isSucceeded();
    }

    /**
     * Delete??????
     *
     * @param jestClient
     * @param indexName
     * @param typeName
     * @param id
     * @return
     * @throws Exception
     */
    public static boolean delete(JestClient jestClient, String indexName, String typeName, String id) throws Exception {

        DocumentResult dr = jestClient.execute(new Delete.Builder(id).index(indexName).type(typeName).build());
        return dr.isSucceeded();
    }

    /**
     * ??????JestClient?????????
     *
     * @param jestClient
     * @throws Exception
     */
    public static void closeJestClient(JestClient jestClient) {

        if (jestClient != null) {
            jestClient.shutdownClient();
        }
    }


    public static String query = "{\n" +
            "  \"size\": 1,\n" +
            "  \"query\": {\n" +
            "     \"match\": {\n" +
            "       \"taskexcuteid\": \"89899143\"\n" +
            "     }\n" +
            "  },\n" +
            "  \"aggs\": {\n" +
            "    \"count\": {\n" +
            "      \"terms\": {\n" +
            "        \"field\": \"source.keyword\"\n" +
            "      },\n" +
            "      \"aggs\": {\n" +
            "        \"sum_price\": {\n" +
            "          \"sum\": {\n" +
            "            \"field\": \"taskprice\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"sum_wordcount\": {\n" +
            "          \"sum\": {\n" +
            "            \"field\": \"taskwordcount\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"avg_taskprice\": {\n" +
            "          \"avg\": {\n" +
            "            \"field\": \"taskprice\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
