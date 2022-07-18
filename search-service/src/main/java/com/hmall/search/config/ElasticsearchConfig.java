package com.hmall.search.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//2.配置RestHighLevelClient对象到容器
@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        HttpHost httpHost = new HttpHost("127.0.0.1",9200,"http");
        RestClientBuilder builder = RestClient.builder(HttpHost.create(httpHost.toHostString()));
        //设置保活策略
        builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultIOReactorConfig(
                                IOReactorConfig.custom()
                                                .setSoKeepAlive(true)
                                                .build()
                        )
        );
        return new RestHighLevelClient(builder);
    }
}
