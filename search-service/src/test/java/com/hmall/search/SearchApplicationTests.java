package com.hmall.search;

import com.alibaba.fastjson.JSON;
import com.hmall.common.client.ItemClient;
import com.hmall.common.dto.Item;
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

    private static final String ITEM_MAPPING = "{\n" +
            "  \"settings\": {\n" +
            "    \"analysis\": {\n" +
            "      \"analyzer\": {\n" +
            "        \"text_anlyzer\": {\n" +
            "          \"tokenizer\": \"ik_max_word\",\n" +
            "          \"filter\": \"py\"\n" +
            "        },\n" +
            "        \"completion_analyzer\": {\n" +
            "          \"tokenizer\": \"keyword\",\n" +
            "          \"filter\": \"py\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"filter\": {\n" +
            "        \"py\": {\n" +
            "          \"type\": \"pinyin\",\n" +
            "          \"keep_full_pinyin\": false,\n" +
            "          \"keep_joined_full_pinyin\": true,\n" +
            "          \"keep_original\": true,\n" +
            "          \"limit_first_letter_length\": 16,\n" +
            "          \"remove_duplicated_term\": true,\n" +
            "          \"none_chinese_pinyin_tokenize\": false\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
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
            "}";

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    //import org.junit.jupiter.api.Test;
    //??????????????????????????????????????????????????????????????????emo???...
    @Test
    public void test() throws IOException {
        GetIndexRequest request = new GetIndexRequest("heima");
        System.out.println(client);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
        client.close();
    }

    //?????????????????????
    @Test
    public void createIndex() throws IOException {
        System.out.println("???????????????????????????...");
        //1.?????????????????????
        CreateIndexRequest request = new CreateIndexRequest("item");
        //2.?????????????????????DSL??? request.source()??????????????????????????????
        request.source(ITEM_MAPPING, XContentType.JSON);
        //3.????????????    :   client.indices()??????????????????????????????create??????????????????
        client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println("?????????????????????");
    }

    @Autowired
    private ItemClient itemClient;

    //????????????
    @Test
    public void importDataToItemIndex() throws IOException {
        System.out.println("??????????????????...");
        int page = 1;
        int size = 500;
        while (true){
            PageDTO<Item> itemPageDTO = itemClient.queryItemByPage(page, size);
            List<Item> list = itemPageDTO.getList();
            if (list.size()<=0){
                System.out.println("??????????????????");
                break;
            }
            BulkRequest request = new BulkRequest();
            for (Item item : list) {
                if (item.getStatus()==2){
                    continue;
                }
                ItemDoc itemDoc = new ItemDoc(item);
                String docJson = JSON.toJSONString(itemDoc);
                //?????????????????????????????????????????????
                request.add(new IndexRequest("item")
                        .id(itemDoc.getId().toString())
                        //The number of object passed must be even but was [1]------
                        //?????????????????????emo.........
                        //.source(XContentType.JSON,docJson)
                        .source(docJson,XContentType.JSON)
                );
            }
            client.bulk(request,RequestOptions.DEFAULT);
            page++;
        }
    }
}
