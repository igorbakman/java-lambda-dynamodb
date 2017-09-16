package com.serverless.bl;

public class TagsResponse {

    private final String brandTags;
    private final String defaultTags;

    public TagsResponse(String brandTags, String defaultTags) {
        this.brandTags = brandTags;
        this.defaultTags = defaultTags;
    }

    public String getBrandTags() {
        return brandTags;
    }

    public String getDefaultTags() {
        return defaultTags;
    }

    @Override
    public String toString() {
        return "TagsResponse{" +
                "brandTags=" + brandTags +
                ", defaultTags=" + defaultTags +
                '}';
    }

}
