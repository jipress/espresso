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
        if (this.responseCors != null && this.responseCors.allowedOrigins != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, String.join(",", this.responseCors.allowedOrigins));
        if (this.responseCors != null && this.responseCors.allowedHeaders != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, String.join(",", this.responseCors.allowedHeaders));
        if (this.responseCors != null && this.responseCors.allowedMethods != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_ALLOW_METHODS_HEADER, String.join(",", this.responseCors.allowedMethods));
        if (this.responseCors != null && this.responseCors.preflightMaxAge != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_MAX_AGE_HEADER, Long.toString(this.responseCors.preflightMaxAge));
        if (this.responseCors != null && this.responseCors.allowedCredentials != null)
            cors.put(CorsOptions.Option.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, Boolean.toString(this.responseCors.allowedCredentials));
        if (this.responseCors != null && this.responseCors.allowedTimingOrigins != null)
            cors.put(CorsOptions.ALLOWED_TIMING_ORIGINS_PARAM, String.join(",", this.responseCors.allowedTimingOrigins));
        if (this.responseCors != null && this.responseCors.exposedHeaders != null)
            cors.put(CorsOptions.EXPOSED_HEADERS_PARAM, String.join(",", this.responseCors.exposedHeaders));

        //return configured options
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

        String[] allowedOrigins;
        String[] allowedMethods;
        String[] allowedHeaders;
        Long preflightMaxAge;
        Boolean allowedCredentials;
        String[] allowedTimingOrigins;
        String[] exposedHeaders;

        public ResponseCors(String[] allowedOrigins, String[] allowedMethods, String[] allowedHeaders, Long preflightMaxAge, Boolean allowedCredentials, String[] allowedTimingOrigins, String[] exposedHeaders) {
            this.allowedOrigins = allowedOrigins;
            this.allowedMethods = allowedMethods;
            this.allowedHeaders = allowedHeaders;
            this.preflightMaxAge = preflightMaxAge;
            this.allowedCredentials = allowedCredentials;
            this.allowedTimingOrigins = allowedTimingOrigins;
            this.exposedHeaders = exposedHeaders;
        }
    }

    public static class ResponseCorsBuilder {

        private String[] allowedOrigins;
        private String[] allowedMethods;
        private String[] allowedHeaders;
        private Long preflightMaxAge;
        private Boolean allowedCredentials;
        private String[] allowedTimingOrigins;
        private String[] exposedHeaders;

        public ResponseCorsBuilder allowedOrigins(String... origins) {
            this.allowedOrigins = origins;
            return this;
        }

        public ResponseCorsBuilder allowedMethods(String... methods) {
            this.allowedMethods = methods;
            return this;
        }

        public ResponseCorsBuilder allowedHeaders(String... headers) {
            this.allowedHeaders = headers;
            return this;
        }

        public ResponseCorsBuilder exposedHeaders(String... headers) {
            this.exposedHeaders = headers;
            return this;
        }

        public ResponseCorsBuilder allowedTimingOrigins(String... origins) {
            this.allowedTimingOrigins = origins;
            return this;
        }

        public ResponseCorsBuilder preflightMaxAge(Long maxAge) {
            this.preflightMaxAge = maxAge;
            return this;
        }

        public ResponseCorsBuilder allowedCredentials(Boolean allow) {
            this.allowedCredentials = allow;
            return this;
        }

        public ResponseCors build() {
            return new ResponseCors(allowedOrigins, allowedMethods, allowedHeaders, preflightMaxAge, allowedCredentials, allowedTimingOrigins, exposedHeaders);
        }
    }
}
