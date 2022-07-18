package com.hmall.search;

import com.alibaba.fastjson.JSON;
import com.hmall.common.dto.PageDTO;
import com.hmall.search.doc.ItemDoc;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class SearchApplicationTests {

    /*private static final String ITEM_MAPPING = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"long\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"long\"\n" +
            "      },\n" +
            "      \"stock\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"image\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"category\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"brand\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"spec\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"sold\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"commentCount\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"isAD\":{\n" +
            "        \"type\": \"boolean\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";*/

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    //import org.junit.jupiter.api.Test;
    //这个类必须是这个，不然拿不到容器中的对象，我emo了...
    @Test
    public void test() throws IOException {
        GetIndexRequest request = new GetIndexRequest("heima");
        System.out.println(client);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
        client.close();
    }

    /*//创建商品索引库
    @Test
    public void createIndex() throws IOException {
        System.out.println("开始商品创建索引库...");
        //1.初始化请求对象
        CreateIndexRequest request = new CreateIndexRequest("item");
        //2.组织请求参数（DSL） request.source()：表示设置请求的参数
        request.source(ITEM_MAPPING, XContentType.JSON);
        //3.发送请求    :   client.indices()获取操作索引的对象，create表示创建索引
        client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println("创建索引库成功");
    }

    @Autowired
    private ItemClient itemClient;

    //导入数据
    @Test
    public void importDataToItemIndex() throws IOException {
        System.out.println("开始导入数据...");
        int page = 1;
        int size = 500;
        while (true){
            PageDTO<Item> itemPageDTO = itemClient.queryItemByPage(page, size);
            List<Item> list = itemPageDTO.getList();
            if (list.size()<=0){
                System.out.println("导入数据完成");
                break;
            }
            BulkRequest request = new BulkRequest();
            for (Item item : list) {
                if (item.getStatus()==2){
                    continue;
                }
                ItemDoc itemDoc = new ItemDoc(item);
                String docJson = JSON.toJSONString(itemDoc);
                //把每个请求添加到桶处理请求里面
                request.add(new IndexRequest("item")
                        .id(itemDoc.getId().toString())
                        //The number of object passed must be even but was [1]------
                        //参数不能写反，emo.........
                        //.source(XContentType.JSON,docJson)
                        .source(docJson,XContentType.JSON)
                );
            }
            client.bulk(request,RequestOptions.DEFAULT);
            page++;
        }
    }*/
}
