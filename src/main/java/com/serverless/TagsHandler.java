package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.serverless.api.ApiGatewayResponse;
import com.serverless.api.Request;
import com.serverless.bl.services.InputService;
import com.serverless.bl.services.TagsService;
import com.serverless.utils.TagsInjector;
import com.serverless.utils.exceptions.DatasourceException;
import com.serverless.utils.exceptions.InvalidInputException;
import org.apache.log4j.Logger;

import java.util.Map;

import static com.serverless.api.ResponseStatus.BAD_REQUEST;
import static com.serverless.api.ResponseStatus.SERVER_ERROR;

public class TagsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Injector INJECTOR = Guice.createInjector(new TagsInjector());

    private static final Logger LOG = Logger.getLogger(TagsHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            Request request = INJECTOR.getInstance(InputService.class).getInputParams(input);

            JsonObject tags = getTagsService().getTags(request);

            LOG.info(":handleRequest: received=" + input + " result=" + tags);

            return ApiGatewayResponse.builder()
                    .setObjectBody(tags)
                    .build();
        } catch (InvalidInputException e) {
            LOG.error(":handleRequest: " + e.getMessage(), e);
            return ApiGatewayResponse.builder()
                    .setStatusCode(BAD_REQUEST.getStatus())
                    .setObjectBody(getErrMsg(e.getMessage()))
                    .build();
        } catch (DatasourceException e) {
            LOG.error(":handleRequest: " + e.getMessage(), e);
            return ApiGatewayResponse.builder()
                    .setStatusCode(SERVER_ERROR.getStatus())
                    .setObjectBody(getErrMsg(e.getMessage()))
                    .build();
        } catch (Exception e) {
            String errMsg = "failed to retrieve tags content";
            LOG.error(":handleRequest: " + errMsg, e);
            return ApiGatewayResponse.builder()
                    .setStatusCode(SERVER_ERROR.getStatus())
                    .setObjectBody(getErrMsg(errMsg))
                    .build();
        }
    }

    TagsService getTagsService() {
        return INJECTOR.getInstance(TagsService.class);
    }

    JsonObject getErrMsg(String errMsg) {
        JsonObject response = new JsonObject();
        response.addProperty("errMsg", errMsg);
        return response;
    }
}
