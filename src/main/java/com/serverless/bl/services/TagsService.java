package com.serverless.bl.services;

import com.google.gson.JsonObject;
import com.serverless.api.Request;

public interface TagsService {

    JsonObject getTags(Request request);

}
