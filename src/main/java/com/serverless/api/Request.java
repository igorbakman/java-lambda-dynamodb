package com.serverless.api;

public class Request {

    private final int brandId;
    private final String language;

    public Request(int brandId, String language) {
        this.brandId = brandId;
        this.language = language;
    }

    public int getBrandId() {
        return brandId;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "Request{" +
                "brandId=" + brandId +
                ", language='" + language + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        return brandId == request.brandId && language.equals(request.language);
    }

    @Override
    public int hashCode() {
        int result = brandId;
        result = 31 * result + language.hashCode();
        return result;
    }
}
