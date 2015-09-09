package com.hewei.oschina.utils;

import com.hewei.oschina.pojos.OsChinaPojo;
import com.hewei.spider.utils.JsonUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hewei.oschina.constants.OsChinaConstants.*;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  16:09
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class OsChinaEsUtils {

    public static final Logger LOG = LoggerFactory.getLogger(OsChinaEsUtils.class);

    private static class HEWEI {

        private static final Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", ES_CLUSTER_NAME).put("client.transport.sniff", true).build();

        private static final Client CLIENT = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(ES_IP, ES_PORT));
    }

    public static Client getClient() {
        return HEWEI.CLIENT;
    }

    public static IndicesExistsResponse existIndex(Client client, String indexName) {
        return client.admin().indices().prepareExists(indexName).get();
    }

    public static void createIndex() {
        Client client = getClient();
        if (!existIndex(client, ES_INDEX_NAME).isExists()) {
            try {
                Settings.Builder builder = ImmutableSettings.settingsBuilder()
                        .put("index.number_of_shards", ES_NUMBER_OF_SHARDS)
                        .put("index.number_of_replicas", ES_NUMBER_OF_REPLICAS);
                client.admin().indices().prepareCreate(ES_INDEX_NAME).setSettings(builder).get();
            } catch (IndexAlreadyExistsException ignored) {//并发
            }
        }
    }

	public static IndexResponse add(String esType, OsChinaPojo data) {
		return getClient().prepareIndex().setIndex(ES_INDEX_NAME).setType(esType).setSource(JsonUtils.toJson(data)).get();
	}
}
