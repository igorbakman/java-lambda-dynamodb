package com.serverless;

import com.google.gson.JsonObject;
import com.serverless.api.ApiGatewayResponse;
import com.serverless.api.Request;
import com.serverless.bl.services.TagsService;
import com.serverless.utils.exceptions.DatasourceException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.serverless.api.ResponseStatus.BAD_REQUEST;
import static com.serverless.api.ResponseStatus.SERVER_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TagsHandlerTest {

    private TagsHandler classUnderTest;
    private Map<String, Object> input;
    private TagsService tagsService;
    private JsonObject tags;
    private String lang;
    private int brandId;
    private Request request;

    @Before
    public void setUp() {
        classUnderTest = new TagsHandlerMock();
        tagsService = mock(TagsService.class);
        lang = "de";
        brandId = 12;

        tags = new JsonObject();
        tags.addProperty("tag_1", "bla bla tag 1");
        tags.addProperty("tag_2", "bla bla tag 2");
        tags.addProperty("tag_3", "bla bla tag 3");

        request = new Request(brandId, lang);
        when(tagsService.getTags(request)).thenReturn(tags);

        input = new HashMap<>();
        input.put("pathParameters", "{brand_id=" + brandId + ", language=" + lang + "}");
    }

    @Test
    public void getTags_success() {
        ApiGatewayResponse actual = classUnderTest.handleRequest(input, null);
        ApiGatewayResponse expected = ApiGatewayResponse.builder().setObjectBody(tags).build();
        assertResponse(actual, expected);
    }

    @Test
    public void getTags_invalidInput_null() {
        ApiGatewayResponse actual = classUnderTest.handleRequest(null, null);
        ApiGatewayResponse expected = ApiGatewayResponse.builder().setStatusCode(BAD_REQUEST.getStatus()).setObjectBody(classUnderTest.getErrMsg("invalid input - brand_id MUST be a number")).build();
        assertResponse(actual, expected);
    }

    @Test
    public void getTags_invalidInput_wrongBrandId() {
        input.put("pathParameters", "{brand_id=" + brandId + "a, language=" + lang + "}");
        ApiGatewayResponse actual = classUnderTest.handleRequest(input, null);
        ApiGatewayResponse expected = ApiGatewayResponse.builder().setStatusCode(BAD_REQUEST.getStatus()).setObjectBody(classUnderTest.getErrMsg("invalid input - brand_id MUST be a number")).build();
        assertResponse(actual, expected);
    }

    @Test
    public void getTags_invalidInput_noLang() {
        input.put("pathParameters", "{brand_id=" + brandId);
        ApiGatewayResponse actual = classUnderTest.handleRequest(input, null);
        ApiGatewayResponse expected = ApiGatewayResponse.builder().setStatusCode(BAD_REQUEST.getStatus()).setObjectBody(classUnderTest.getErrMsg("internal server error - failed to parse input")).build();
        assertResponse(actual, expected);
    }

    @Test
    public void getTags_noTags() {
        when(tagsService.getTags(request)).thenThrow(new DatasourceException("failed to get TAGS from DB"));
        ApiGatewayResponse actual = classUnderTest.handleRequest(input, null);
        ApiGatewayResponse expected = ApiGatewayResponse.builder().setStatusCode(SERVER_ERROR.getStatus()).setObjectBody(classUnderTest.getErrMsg("failed to get TAGS from DB")).build();
        assertResponse(actual, expected);
    }

    @Test
    public void getTags_returnNullTags() {
        when(tagsService.getTags(request)).thenReturn(null);
        ApiGatewayResponse actual = classUnderTest.handleRequest(input, null);
        ApiGatewayResponse expected = ApiGatewayResponse.builder().setStatusCode(SERVER_ERROR.getStatus()).setObjectBody(classUnderTest.getErrMsg("failed to retrieve tags content")).build();
        assertResponse(actual, expected);
    }


    private void assertResponse(ApiGatewayResponse actual, ApiGatewayResponse expected) {
        assertThat(actual.getBody(), equalTo(expected.getBody()));
        assertThat(actual.getStatusCode(), equalTo(expected.getStatusCode()));
        assertThat(actual.getHeaders(), equalTo(expected.getHeaders()));
    }

    private class TagsHandlerMock extends TagsHandler {
        @Override
        TagsService getTagsService() {
            return tagsService;
        }
    }

}