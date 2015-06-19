package com.hewei.spider.es;

import com.hewei.spider.pojos.StorageData;
import com.hewei.spider.utils.JsonUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;

import java.io.IOException;
import java.util.Map;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  16:34
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class ESUtils {

    private static final String INDEX_NAME = "star_index";

    private static final String TYPE_NAME = "star";

    private static final String CLUSTER_NAME = "hewei1";

    private static final String IP = "172.18.2.40";

    private static final int port = 9300;


    public static Client getClient() {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", CLUSTER_NAME).put("client.transport.sniff", true).build();
        return new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(IP, port));
    }

    public static IndicesExistsResponse existIndex(Client client) {
        return client.admin().indices().prepareExists(INDEX_NAME).execute().actionGet();
    }

    public static void createMapping(Client client) {

        XContentBuilder mapping;

        try {
            mapping = XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject(TYPE_NAME)
//                            .startObject("_timestamp")
//                                .field("enabled","true")
//                                .field("format","yyyy-MM-dd HH:mm:ss")
//                            .endObject()
                            .startObject("properties")
                                .startObject("id")
                                    .field("type", "long")
                                    .field("index", "not_analyzed")
                                .endObject()
                                .startObject("createTime")
                                    .field("type", "String")
                                    .field("index", "not_analyzed")
                                    .field("format", "yyyy-MM-dd HH:mm:ss")
                                .endObject()
                                .startObject("name")
                                    .field("type", "string")
                                    .field("indexAnalyzer", "ik")
                                    .field("searchAnalyzer", "ik")
                                    .field("index","analyzed")
                                .endObject()
                                .startObject("url")
                                    .field("type", "string")
                                    .field("indexAnalyzer", "ik")
                                    .field("searchAnalyzer", "ik")
                                    .field("index", "analyzed")
                                .endObject()
                                .startObject("searchText")
                                    .field("type", "string")
                                    .field("indexAnalyzer", "ik")
                                    .field("searchAnalyzer", "ik")
                                    .field("index", "analyzed")
                                .endObject()
                                .startObject("desc")
                                    .field("type", "string")
//                                    .field("index", "not_analyzed")
                                    .field("index", "no")
                                    .field("include_in_all", "false")
                                .endObject()
                                .startObject("experience")
                                    .field("type", "string")
                                    .field("index", "no")
                                    .field("include_in_all", "false")
                                .endObject()
                                .startObject("originalHtml")
                                    .field("type", "string")
                                    .field("index", "no")
                                    .field("include_in_all", "false")
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PutMappingRequest mappingRequest = Requests.putMappingRequest(INDEX_NAME).type(TYPE_NAME).source(mapping);

        client.admin().indices().putMapping(mappingRequest).actionGet();
    }

    public static void createIndex(Client client) {
        if (!existIndex(client).isExists()) {
            client.admin().indices().prepareCreate(INDEX_NAME).execute().actionGet();
        }
    }

    public static void dropIndex(Client client) {
        if (existIndex(client).isExists()) {
            client.admin().indices().prepareDelete(INDEX_NAME).execute().actionGet();
        }
    }

    public static IndexResponse add(Client client, StorageData data) {
        IndexResponse indexResponse = client.prepareIndex().setIndex(INDEX_NAME).setType(TYPE_NAME).
                setSource(JsonUtils.toJson(data)).setId(String.valueOf(data.getId())).execute().actionGet();
        System.out.println("添加信息,isCreated=" + indexResponse.isCreated());
        return indexResponse;
    }

    public static void search(Client client,String searchField,String searchText) {
        //        BoolQueryBuilder query = QueryBuilders.boolQuery();
        //        query.must(QueryBuilders.termQuery("description", "打开"));
        QueryBuilder query = QueryBuilders.queryStringQuery(searchField+":"+searchText);
//        .setFetchSource(new String[]{"name","url"},null)
        SearchResponse response = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(query).addHighlightedField(searchField).setHighlighterPreTags("<span style=\"color:red\">").setHighlighterPostTags("</span>").setFrom(0).setSize(5).execute().actionGet();
        SearchHits shs = response.getHits();
        for (SearchHit hit : shs) {
            System.out.println("搜索到的信息:" + hit.getSourceAsString());
            StorageData data = JsonUtils.parse(hit.getSourceAsString(), StorageData.class);
            //获取对应的高亮域
            Map<String, HighlightField> result = hit.highlightFields();
            //从设定的高亮域中取得指定域
            HighlightField titleField = result.get(searchField);
            //取得定义的高亮标签
            Text[] descriptionTexts = titleField.fragments();
            //为title串值增加自定义的高亮标签
            String description = "";
            for (Text text : descriptionTexts) {
                description += text;
            }
            //将追加了高亮标签的串值重新填充到对应的对象
//            data.setDescription(description);
            //打印高亮标签追加完成后的实体对象
//            System.out.println(article.getDescription());
            System.out.println(description);
        }
    }

    public static void main(String[] args) {
        try (Client client = ESUtils.getClient()) {
            dropIndex(client);
//            ESUtils.createIndex(client);
//            ESUtils.createMapping(client);
//            StorageData data = new StorageData();
//            data.setId(1L);
//            data.setCreateTime(new Date());
//            data.setUrl("www.baidu.com");
//            data.setName("贺伟");
//            data.setDesc("贺伟来打酱油的");
//            data.setExperience("贺伟专业来数云打酱油的");
//            data.setOriginalHtml("<div>贺伟就是来哈哈哈打酱油的,怎么滴</div>");
//            data.setSearchText("贺伟啊");
//            add(client, data);
//            search(client, "name", "贺伟");
//            dropIndex(client);
        }
    }

}
