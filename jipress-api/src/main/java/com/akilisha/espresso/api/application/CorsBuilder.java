package com.akilisha.espresso.api.application;

import java.util.function.Function;

public class CorsBuilder {

    private RequestCors requestCors;
    private ResponseCors responseCors;

    private CorsBuilder() {
    }

    public static CorsBuilder newBuilder() {
        return new CorsBuilder();
    }

    public CorsBuilder request(Function<RequestCorsBuilder, RequestCors> builder) {
        this.requestCors = builder.apply(new RequestCorsBuilder());
        return this;
    }

    public CorsBuilder response(Function<ResponseCorsBuilder, ResponseCors> builder) {
        this.responseCors = builder.apply(new ResponseCorsBuilder());
        return this;
    }

    public CorsOptions build() {
        CorsOptions cors = new CorsOptions();
        //request headers
        if (this.requestCors != null && this.requestCors.requestMethod != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_REQUEST_METHOD_HEADER, String.join(",", this.requestCors.requestMethod));
        if (this.requestCors != null && this.requestCors.requestHeaders != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_REQUEST_HEADERS_HEADER, String.join(",", this.requestCors.requestHeaders));

        //response headers
        if (this.responseCors != null && this.responseCors.allowOrigin != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, this.responseCors.allowOrigin);
        if (this.responseCors != null && this.responseCors.allowHeaders != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, String.join(",", this.responseCors.allowHeaders));
        if (this.responseCors != null && this.responseCors.allowMethod != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_ALLOW_METHOD_HEADER, String.join(",", this.responseCors.allowMethod));
        if (this.responseCors != null && this.responseCors.maxAge != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_MAX_AGE_HEADER, Long.toString(this.responseCors.maxAge));
        if (this.responseCors != null && this.responseCors.allowCredentials != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, Boolean.toString(this.responseCors.allowCredentials));
        return cors;
    }

    public static class RequestCors {

        String[] requestMethod;
        String[] requestHeaders;

        public RequestCors(String[] requestMethod, String[] requestHeaders) {
            this.requestMethod = requestMethod;
            this.requestHeaders = requestHeaders;
        }
    }

    public static class RequestCorsBuilder {
        private String[] requestMethods;
        private String[] requestHeaders;

        public RequestCorsBuilder requestMethods(String... methods) {
            this.requestMethods = methods;
            return this;
        }

        public RequestCorsBuilder requestHeaders(String... headers) {
            this.requestHeaders = headers;
            return this;
        }

        public RequestCors build() {
            return new RequestCors(requestMethods, requestHeaders);
        }
    }

    public static class ResponseCors {

        String allowOrigin;
        String[] allowMethod;
        String[] allowHeaders;
        Long maxAge;
        Boolean allowCredentials;

        public ResponseCors(String allowOrigin, String[] allowMethod, String[] allowHeaders,
                            Long maxAge, Boolean allowCredentials) {
            this.allowOrigin = allowOrigin;
            this.allowMethod = allowMethod;
            this.allowHeaders = allowHeaders;
            this.maxAge = maxAge;
            this.allowCredentials = allowCredentials;
        }
    }

    public static class ResponseCorsBuilder {

        private String allowOrigin;
        private String[] allowMethods;
        private String[] allowHeaders;
        private Long maxAge;
        private Boolean allowCredentials;

        public ResponseCorsBuilder allowOrigin(String origin) {
            this.allowOrigin = origin;
            return this;
        }

        public ResponseCorsBuilder allowMethods(String... methods) {
            this.allowMethods = methods;
            return this;
        }

        public ResponseCorsBuilder allowHeaders(String... headers) {
            this.allowHeaders = headers;
            return this;
        }

        public ResponseCorsBuilder maxAge(Long age) {
            this.maxAge = age;
            return this;
        }

        public ResponseCorsBuilder allowCredentials(Boolean allow) {
            this.allowCredentials = allow;
            return this;
        }

        public ResponseCors build() {
            return new ResponseCors(allowOrigin, allowMethods, allowHeaders, maxAge, allowCredentials);
        }
    }
}
