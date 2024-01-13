package com.akilisha.espresso.api.application;

import java.util.EnumMap;

public class CorsOptions extends EnumMap<CorsOptions.Option, String> {

    public static final String ALLOWED_ORIGINS_PARAM = "allowedOrigins";
    public static final String ALLOWED_METHODS_PARAM = "allowedMethods";
    public static final String ALLOWED_HEADERS_PARAM = "allowedHeaders";
    public static final String PREFLIGHT_MAX_AGE_PARAM = "preflightMaxAge";
    public static final String ALLOWED_CREDENTIALS_PARAM = "allowedCredentials";
    public static final String EXPOSED_HEADERS_PARAM = "exposedHeaders";
    public static final String ALLOWED_TIMING_ORIGINS_PARAM = "allowedTimingOrigins";

    CorsOptions() {
        super(Option.class);
    }

    public static CorsOptions wideOpen() {
        return CorsBuilder.newBuilder()
                .response(res -> res
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
                        .allowedCredentials(true)
                        .preflightMaxAge(1209600L)
                        .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "Options", "Cache-Control", "X-Requested-With")
                        .exposedHeaders("x-forward-to")
                        .allowedTimingOrigins("*")
                        .build())
                .build();
    }

    public void put(String name, String value) {
        switch (name) {
            case ALLOWED_ORIGINS_PARAM:
                this.put(Option.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, value);
                break;
            case ALLOWED_METHODS_PARAM:
                this.put(Option.ACCESS_CONTROL_ALLOW_METHODS_HEADER, value);
                break;
            case ALLOWED_HEADERS_PARAM:
                this.put(Option.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, value);
                break;
            case ALLOWED_CREDENTIALS_PARAM:
                this.put(Option.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, value);
                break;
            case EXPOSED_HEADERS_PARAM:
                this.put(Option.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER, value);
                break;
            case PREFLIGHT_MAX_AGE_PARAM:
                this.put(Option.ACCESS_CONTROL_MAX_AGE_HEADER, value);
                break;
            case ALLOWED_TIMING_ORIGINS_PARAM:
                this.put(Option.TIMING_ALLOW_ORIGIN_HEADER, value);
            default:
                // do nothing;
                break;
        }
    }

    public enum Option {
        // Request headers
        ACCESS_CONTROL_REQUEST_METHOD_HEADER("Access-Control-Request-Method"),
        ACCESS_CONTROL_REQUEST_HEADERS_HEADER("Access-Control-Request-Headers"),
        // Response headers
        ACCESS_CONTROL_ALLOW_ORIGIN_HEADER("Access-Control-Allow-Origin"),
        ACCESS_CONTROL_ALLOW_METHODS_HEADER("Access-Control-Allow-Methods"),
        ACCESS_CONTROL_ALLOW_HEADERS_HEADER("Access-Control-Allow-Headers"),
        ACCESS_CONTROL_MAX_AGE_HEADER("Access-Control-Max-Age"),
        ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER("Access-Control-Allow-Credentials"),
        ACCESS_CONTROL_EXPOSE_HEADERS_HEADER("Access-Control-Expose-Headers"),
        TIMING_ALLOW_ORIGIN_HEADER("Timing-Allow-Origin");

        public final String name;

        Option(String name) {
            this.name = name;
        }

        public static Option option(String name) {
            for (Option opt : values()) {
                if (opt.name.equals(name)) {
                    return opt;
                }
            }
            throw new RuntimeException(String.format("Cors option '%s' is not a known option", name));
        }
    }
}
