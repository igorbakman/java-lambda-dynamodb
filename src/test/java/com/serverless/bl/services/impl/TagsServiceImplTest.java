package com.serverless.bl.services.impl;

import com.google.gson.JsonObject;
import com.serverless.api.Request;
import com.serverless.bl.TagsResponse;
import com.serverless.bl.dao.TagsDao;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TagsServiceImplTest {

    private TagsServiceImpl classUnderTest;
    private TagsDao dao;
    private Request request;
    private TagsResponse tagsResponse;
    private JsonObject brandTags;
    private JsonObject defaultTags;

    @Before
    public void setUp() {
        dao = mock(TagsDao.class);

        request = new Request(512, "ja");
        tagsResponse = generateTags();

        classUnderTest = new TagsServiceImpl();
        classUnderTest.setDao(dao);

        when(dao.getTags(request)).thenReturn(tagsResponse);
    }

    private TagsResponse generateTags() {
        brandTags = new JsonObject();
        brandTags.addProperty("tag_1", "bla bla brand 93 tag 1");
        brandTags.addProperty("tag_3", "bla bla brand 93 tag 3");
        defaultTags = new JsonObject();
        defaultTags.addProperty("tag_1", "bla bla tag 1");
        defaultTags.addProperty("tag_2", "bla bla tag 2");
        defaultTags.addProperty("tag_3", "bla bla tag 3");
        defaultTags.addProperty("tag_4", "bla bla tag 4");
        return new TagsResponse(brandTags.toString(), defaultTags.toString());
    }

    @Test
    public void getTags_replaceTag() {
        JsonObject expected = new JsonObject();
        expected.addProperty("tag_2", "bla bla tag 2");
        expected.addProperty("tag_4", "bla bla tag 4");
        expected.addProperty("tag_1", "bla bla brand 93 tag 1");
        expected.addProperty("tag_3", "bla bla brand 93 tag 3");

        JsonObject actual = classUnderTest.getTags(request);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void getTags_noReplacement() {
        tagsResponse = new TagsResponse(null, defaultTags.toString());
        when(dao.getTags(request)).thenReturn(tagsResponse);
        JsonObject actual = classUnderTest.getTags(request);
        assertThat(actual, equalTo(defaultTags));
    }

    @Test
    public void getTags_skipInvalidBrandTags() {
        tagsResponse = new TagsResponse(brandTags + "}", defaultTags.toString());
        when(dao.getTags(request)).thenReturn(tagsResponse);
        JsonObject actual = classUnderTest.getTags(request);
        assertThat(actual, equalTo(defaultTags));
    }

}