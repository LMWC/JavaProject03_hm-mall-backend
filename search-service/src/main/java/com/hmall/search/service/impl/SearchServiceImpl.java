package com.hmall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.hmall.common.client.ItemClient;
import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import com.hmall.search.doc.ItemDoc;
import com.hmall.search.dto.RequestParams;
import com.hmall.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RestHighLevelClient client;

    //查询
    @Override
    public PageDTO<ItemDoc> selectByCondition(RequestParams params) throws IOException {
        SearchRequest request = new SearchRequest("item");

        //=========增加组合查询条件booleanQuery(params)========
        //====此处是BoolQueryBuilder,不是BooleanQueryBuilder
        BoolQueryBuilder queryBuilder = booleanQuery(params);
        //1.基础参数
        request.source().query(queryBuilder);

        //2.分页
        request.source().from((params.getPage()-1)*params.getSize());
        request.source().size(params.getSize());

        //3.排序
        String sortBy = params.getSortBy();
        if ("sold".equals(sortBy)){
            request.source().sort("sold", SortOrder.DESC);
        }else if("price".equals(sortBy)){
            request.source().sort("price",SortOrder.DESC);
        }
        //4.设置算分函数
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(
                queryBuilder,
                new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                QueryBuilders.termQuery("isAD", true),
                                ScoreFunctionBuilders.weightFactorFunction(100)
                        )
                }
        );
        //5.设置高亮
        request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return handleSelectResponse(response);
    }

    //聚合
    @Override
    public Map<String, List<String>> aggregationByCondition(RequestParams params) throws IOException {
        SearchRequest request = new SearchRequest("item");
        //把其它的一些条件都拿过来
        request.source().query(booleanQuery(params));
        //不需要查询原始数据
        request.source().size(0);
        //设置聚合====terms:设置聚合种类，需要指定自定义名称=====field：设置聚合的字段名=====size:设置聚合的结果数量
        //对分类聚合=========聚合直接操作，不需要判断语句，emo....
        request.source().aggregation(AggregationBuilders.terms("categoryAggs").field("category").size(20));
        //对品牌聚合
        request.source().aggregation(AggregationBuilders.terms("brandAggs").field("brand").size(20));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return handleAggResponse(response);
    }

    @Autowired
    private ItemClient itemClient;

    @Override
    public boolean insertById(Long id) {
        Item item = itemClient.queryItemById(id);
        ItemDoc itemDoc = new ItemDoc(item);
        String jsonItemDoc = JSON.toJSONString(itemDoc);
        IndexRequest request = new IndexRequest("item").id(id.toString());
        request.source(jsonItemDoc, XContentType.JSON);
        IndexResponse response = null;
        try {
            response = client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response != null && "created".equals(response.getResult().getLowercase());
    }

    @Override
    public boolean deleteById(Long id) {
        DeleteRequest request = new DeleteRequest("item");
        request.id(id.toString());
        DeleteResponse response = null;
        try {
            response = client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response != null && "deleted".equals(response.getResult().getLowercase());
    }

    //组合查询
    private BoolQueryBuilder booleanQuery(RequestParams params) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();

        //对主搜索条件过滤
        if (StringUtils.isEmpty(params.getKey())){
            queryBuilder.must(QueryBuilders.matchAllQuery());
        }else {
            queryBuilder.must(QueryBuilders.matchQuery("all",params.getKey()));
        }
        //对分类过滤
        if (StringUtils.isNotEmpty(params.getCategory())){
            queryBuilder.filter(QueryBuilders.termQuery("category",params.getCategory()));
        }
        //对品牌过滤
        if (StringUtils.isNotEmpty(params.getBrand())){
            queryBuilder.filter(QueryBuilders.termQuery("brand",params.getBrand()));
        }
        //对价格过滤
        if (params.getMinPrice()!=null&&params.getMaxPrice()!=null){
            queryBuilder.filter(QueryBuilders.rangeQuery("price")
                    .gte(params.getMinPrice()*100).lte(params.getMaxPrice()*100));
        }
        return queryBuilder;
    }

    //处理查询的结果
    private PageDTO<ItemDoc> handleSelectResponse(SearchResponse response) {
        log.info("查询的结果:{}",response);
        ArrayList<ItemDoc> list = new ArrayList<>();
        SearchHits hits = response.getHits();
        log.info("查询的总数量:{}",hits.getTotalHits().value);
        SearchHit[] hitsData = hits.getHits();
        for (SearchHit doc : hitsData) {
            String source = doc.getSourceAsString();
            //这里如果需要解析的话，确保实体类有无参构造或者自己全参数的有参构造，又又又emo了...
            ItemDoc itemDoc = JSON.parseObject(source, ItemDoc.class);
            list.add(itemDoc);
            //置换高亮字段
            Map<String, HighlightField> map = doc.getHighlightFields();
            if (map!=null&&map.size()>0){
                HighlightField name = map.get("name");
                String value = name.getFragments()[0].toString();
                itemDoc.setName(value);
            }
        }
        return new PageDTO<>(hits.getTotalHits().value,list);
    }

    //处理聚合的结果
    private Map<String, List<String>> handleAggResponse(SearchResponse response) {
        log.info("聚合的结果:{}",response);
        Map<String, List<String>> map = new HashMap<>();
        ArrayList<String> categoryList = new ArrayList<>();
        ArrayList<String> brandList = new ArrayList<>();

        //aggregations与hits同层
        Aggregations aggregations = response.getAggregations();
        //根据名称获取聚合结果，必须转换成对应的具体类型才可以获取对应的数据
        //Aggregation cityAggs1 = aggregations.get("cityAggs");
        Terms categoryAggs = aggregations.get("categoryAggs");
        Terms brandAggs = aggregations.get("brandAggs");
        //获取分类所有的桶
        List<? extends Terms.Bucket> categoryAggsBuckets = categoryAggs.getBuckets();
        for (Terms.Bucket bucket : categoryAggsBuckets) {
            Object key = bucket.getKey();
            categoryList.add(key.toString());
        }
        //获取品牌所有的桶
        List<? extends Terms.Bucket> brandAggsBuckets = brandAggs.getBuckets();
        for (Terms.Bucket bucket : brandAggsBuckets) {
            Object key = bucket.getKey();
            brandList.add(key.toString());
        }
        map.put("category",categoryList);
        map.put("brand",brandList);
        return map;
    }

    //自动补全
    /*@Override
    public List<String> suggestion(String key) {

        try {
            SearchRequest request = new SearchRequest("item");
            request.source().suggest(
                    new SuggestBuilder().addSuggestion("mysuggest",
                            SuggestBuilders.completionSuggestion("suggestion").prefix(key)
                                    .skipDuplicates(true).size(20))
            );
            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
            //解析
            CompletionSuggestion mysuggest = search.getSuggest().getSuggestion("mysuggest");
            List<CompletionSuggestion.Entry.Option> options = mysuggest.getOptions();
            List<String> list = new ArrayList<>();
            for (CompletionSuggestion.Entry.Option option : options) {
                Text text = option.getText();
                list.add(text.toString());
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
    @Override
    public List<String> suggestion(String prefix) {

        //new一个新的SearchRequest对象
        SearchRequest request = new SearchRequest("item");

        //打印参数看看
        System.out.println("prefix = " + prefix);
        List<String> list = null;

        try {
            //1、准备DSL
            request.source().suggest(
                    new SuggestBuilder().addSuggestion(
                            "suggestions",
                            SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(prefix)
                            .skipDuplicates(true)
                            .size(10)
                    )
            );

            //3.发起请求
            SearchResponse response = client.search(request,RequestOptions.DEFAULT);

            //4.解析结果
            Suggest suggest = response.getSuggest();

            //4.1.根据补全查询名称，获取补全结果
            CompletionSuggestion suggestions = suggest.getSuggestion("suggestions");

            //4.2.获取options
            List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();

            //4.3.遍历
            list = new ArrayList<>(options.size());
            for (CompletionSuggestion.Entry.Option option : options) {
                String text = option.getText().toString();
                list.add(text);
            }

            //打印结果看看
            System.out.println(list);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}
