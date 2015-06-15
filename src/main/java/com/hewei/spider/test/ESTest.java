package com.hewei.spider.test;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
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
import java.util.Date;
import java.util.Map;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/6/15  16:25
 * @desc
 */
public class ESTest {

    public static final String INDEX_NAME = "esindex";

    public static final String TYPE_NAME = "file";

    public static void main(String[] args) {
        try (Client client = getClient()) {
            createIndex(client);
            createMapping(client);
            crud(client);
            search(client);
            dropIndex(client);
        }
    }

    public static void search(Client client) {
        //        BoolQueryBuilder query = QueryBuilders.boolQuery();
        //        query.must(QueryBuilders.termQuery("description", "打开"));
        QueryBuilder query = QueryBuilders.queryStringQuery("description:正式");
        SearchResponse response = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(query).addHighlightedField("description").setHighlighterPreTags("<span style=\"color:red\">").setHighlighterPostTags("</span>").setFrom(0).setSize(60).execute().actionGet();
        SearchHits shs = response.getHits();
        for (SearchHit hit : shs) {
            System.out.println("搜索到的信息:" + hit.getSourceAsString());
            Article article = Utils.readMessage(hit.getSourceAsString());
            //获取对应的高亮域
            Map<String, HighlightField> result = hit.highlightFields();
            //从设定的高亮域中取得指定域
            HighlightField titleField = result.get("description");
            //取得定义的高亮标签
            Text[] descriptionTexts = titleField.fragments();
            //为title串值增加自定义的高亮标签
            String description = "";
            for (Text text : descriptionTexts) {
                description += text;
            }
            //将追加了高亮标签的串值重新填充到对应的对象
            article.setDescription(description);
            //打印高亮标签追加完成后的实体对象
            System.out.println(article.getDescription());
        }
    }

    public static void crud(Client client) {
        Article article = new Article(1, new Date(), "对应issue已经有人提出，并且已经pr了。 但是目前还没有发布正式版，在打开的四个issue已经有人再催");
        add(client, article);
        get(client, article);
        update(client, article);
//        delete(client, article);
    }

    private static GetResponse update(Client client, Article article) {

        GetResponse getResponse = get(client, article);
        System.out.println("更新前版本=" + getResponse.getVersion());

        article.setCreateDate(new Date());
        article.setDescription(article.getDescription() + "---hewei");

        UpdateResponse updateResponse = client.prepareUpdate().setIndex(INDEX_NAME).setType(TYPE_NAME).setDoc(Utils.toJson(article)).setId(String.valueOf(article.getId())).execute().actionGet();

        System.out.println("更新信息，isCreated=" + updateResponse.isCreated());

        getResponse = get(client, article);
        System.out.println("更新后版本=" + getResponse.getVersion());

        return getResponse;
    }

    private static DeleteResponse delete(Client client, Article article) {
        DeleteResponse delResponse = client.prepareDelete().setIndex(INDEX_NAME).setType(TYPE_NAME).setId(String.valueOf(article.getId())).execute().actionGet();
        System.out.println("删除信息=" + delResponse.isFound());
        return delResponse;
    }

    private static GetResponse get(Client client, Article article) {
        GetResponse getResponse = client.prepareGet().setIndex(INDEX_NAME).setType(TYPE_NAME).setId(String.valueOf(article.getId())).execute().actionGet();
        System.out.println("获得数据=" + getResponse.getSourceAsString());
        return getResponse;
    }

    private static IndexResponse add(Client client, Article article) {
        IndexResponse indexResponse = client.prepareIndex().setIndex(INDEX_NAME).setType(TYPE_NAME).setSource(Utils.toJson(article)).setId(String.valueOf(article.getId())).execute().actionGet();
        System.out.println("添加信息,isCreated=" + indexResponse.isCreated());
        return indexResponse;
    }

    public static void createMapping(Client client) {

        XContentBuilder mapping;

        try {
            mapping = XContentFactory.jsonBuilder().startObject().startObject(TYPE_NAME).startObject("properties").startObject("description").field("type", "string").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
                    //.field("term_vector","with_positions_offsets")更快的高亮
                    //.field("include_in_all", "true")不包含在_all索引中
                    .startObject("createDate").field("type", "date").endObject().startObject("id").field("type", "integer").endObject().endObject().endObject().endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PutMappingRequest mappingRequest = Requests.putMappingRequest(INDEX_NAME).type(TYPE_NAME).source(mapping);

        client.admin().indices().putMapping(mappingRequest).actionGet();
    }

    public static IndicesExistsResponse existIndex(Client client) {
        return client.admin().indices().prepareExists(INDEX_NAME).execute().actionGet();
    }

    public static void dropIndex(Client client) {
        if (existIndex(client).isExists()) {
            client.admin().indices().prepareDelete(INDEX_NAME).execute().actionGet();
        }
    }

    public static void createIndex(Client client) {
        if (!existIndex(client).isExists()) {
            client.admin().indices().prepareCreate(INDEX_NAME).execute().actionGet();
        }
    }

    public static Client getClient() {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "hewei_test").put("client.transport.sniff", true).build();
        return new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("172.18.2.35", 9300));
    }
}
