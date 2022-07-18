package com.hmall.search.service;

import com.hmall.common.dto.PageDTO;
import com.hmall.search.doc.ItemDoc;
import com.hmall.search.dto.RequestParams;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchService {
    PageDTO<ItemDoc> selectByCondition(RequestParams requestParams) throws IOException;

    Map<String, List<String>> aggregationByCondition(RequestParams requestParams) throws IOException;

    List<String> suggestion(String key) throws IOException;

    boolean insertById(Long id);

    boolean deleteById(Long id);
}
