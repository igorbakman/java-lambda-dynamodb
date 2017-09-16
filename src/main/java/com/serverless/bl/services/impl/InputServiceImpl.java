package com.serverless.bl.services.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.serverless.TagsHandler;
import com.serverless.api.Request;
import com.serverless.bl.services.InputService;
import com.serverless.utils.exceptions.InvalidInputException;
import org.apache.log4j.Logger;

import java.util.Map;


public class InputServiceImpl implements InputService {

    private static final Logger LOG = Logger.getLogger(TagsHandler.class);


    @Override
    public Request getInputParams(Map<String, Object> input) {
        LOG.debug(":getInputParams: starting with input=" + input);
        Request request = new Request(getBrandId(input), getPathParam(input, "language").getAsString());
        LOG.info(":getInputParams: extracted " + request);
        return request;
    }

    private int getBrandId(Map<String, Object> input) {
        try {
            return getPathParam(input, "brand_id").getAsInt();
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidInputException("invalid input - brand_id MUST be a number", e);
        }
    }

    private JsonElement getPathParam(Map<String, Object> input, String param) {
        try {
            JsonObject pathParameters = new JsonParser().parse(input.get("pathParameters").toString()).getAsJsonObject();
            return pathParameters.get(param);
        } catch (JsonSyntaxException e) {
            throw new InvalidInputException("internal server error - failed to parse input", e);
        }
    }

}
