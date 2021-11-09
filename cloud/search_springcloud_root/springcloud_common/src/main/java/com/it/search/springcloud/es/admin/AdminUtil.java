package com.it.search.springcloud.es.admin;

import com.it.search.springcloud.common.utils.FileCommon;
import com.it.search.springcloud.es.client.ESclientUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

public class AdminUtil {

    public static boolean buildIndexAndMapping(String index,
                                               String type,
                                               String path,
                                               int shards,
                                               int replicas){
        boolean flag = true;

        try {
            TransportClient client = ESclientUtil.getClient();
            //创建索引
            boolean indices = AdminUtil.createIndices(client, index, shards, replicas);
            if(indices){
                //索引创建成功，创建mapping

                String json = FileCommon.getAbstractPath(path);
                flag = AdminUtil.addMapping(client, index, type, json);
            }else{
                flag = false;
                System.out.println(("索引" + index + "创建失败"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return flag;
    }




    /**
     * 索引是否存在
     * @param client
     * @param index
     * @return
     */
    public static boolean indexExists(TransportClient client,String index){
        boolean tag = true;
        try {
            IndicesExistsResponse indicesExistsResponse = client.admin().indices().prepareExists(index).execute().actionGet();
            tag = indicesExistsResponse.isExists();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tag;
    }

    /**
     *
     * @param client  客户端
     * @param index   索引名
     * @param shards  分片数
     * @param replicas 副本数
     * @return
     */
    public static boolean createIndices(TransportClient client,
                                        String index,
                                        int shards,
                                        int replicas){
        CreateIndexResponse createIndexResponse = null;
        if(!AdminUtil.indexExists(client,index)){
            createIndexResponse = client.admin().indices().prepareCreate(index)
                    .setSettings(Settings.builder()
                            .put("index.number_of_shards", shards)
                            .put("index.number_of_replicas", replicas)
                    ).get();
        }
        return createIndexResponse.isAcknowledged();
    }


    /**
     * 添加mapping
     * @param client
     * @param index
     * @param type
     * @param jsonString
     * @return
     */
    public static boolean addMapping(TransportClient client,
                                     String index,
                                     String type,
                                     String jsonString){
        PutMappingResponse putMappingResponse = client.admin().indices().preparePutMapping(index)
                .setType(type)
                .setSource(jsonString, XContentType.JSON)
                .get();
        return putMappingResponse.isAcknowledged();
    }


}
