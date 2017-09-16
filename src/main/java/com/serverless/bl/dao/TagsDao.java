package com.serverless.bl.dao;

import com.serverless.api.Request;
import com.serverless.bl.TagsResponse;

public interface TagsDao {

    TagsResponse getTags(Request request);

}
