package com.serverless.bl.services;

import com.serverless.api.Request;

import java.util.Map;

public interface InputService {

    Request getInputParams(Map<String, Object> input);

}
