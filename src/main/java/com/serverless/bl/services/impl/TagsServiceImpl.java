package com.serverless.bl.services.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.serverless.api.Request;
import com.serverless.bl.TagsResponse;
import com.serverless.bl.dao.TagsDao;
import com.serverless.bl.services.TagsService;
import org.apache.log4j.Logger;

public class TagsServiceImpl implements TagsService {

    private static final Logger LOG = Logger.getLogger(TagsServiceImpl.class);

    private TagsDao dao;

    @Inject
    void setDao(TagsDao dao) {
        this.dao = dao;
    }

    @Override
    public JsonObject getTags(Request request) {
        TagsResponse tagsResponse = dao.getTags(request);
        LOG.debug(":getTags: retrieved from DB " + tagsResponse);

        if (tagsResponse.getBrandTags() == null) {
            return getDefaultTags(tagsResponse);
        }

        return mergeTags(tagsResponse);
    }

    private JsonObject mergeTags(TagsResponse tagsResponse) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject defaultTags = parser.parse(tagsResponse.getDefaultTags()).getAsJsonObject();
            JsonObject brandTags = parser.parse(tagsResponse.getBrandTags()).getAsJsonObject();

            brandTags.entrySet().forEach(entry -> defaultTags.add(entry.getKey(), entry.getValue()));

            return defaultTags;
        } catch (JsonSyntaxException e) {
            LOG.error(":mergeTags: failed to merge common tags with brand tags, using only common. JsonSyntaxException=" + e.getMessage());
            return getDefaultTags(tagsResponse);
        }
    }

    private JsonObject getDefaultTags(TagsResponse tagsResponse) {
        return new JsonParser().parse(tagsResponse.getDefaultTags()).getAsJsonObject();
    }

}
