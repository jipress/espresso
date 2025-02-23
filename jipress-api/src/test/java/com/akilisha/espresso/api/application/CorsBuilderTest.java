package com.akilisha.espresso.api.application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CorsBuilderTest {

    @Test
    void new_build_produces_empty_cors_options() {
        CorsOptions options = CorsBuilder.newBuilder()
                .request(CorsBuilder.RequestCorsBuilder::build)
                .response(CorsBuilder.ResponseCorsBuilder::build)
                .build();
        assertThat(options).isEmpty();
    }

    @Test
    void new_build_produces_with_request_builder_options_produces_2_options() {
        CorsOptions options = CorsBuilder.newBuilder()
                .request(bld -> bld
                        .requestHeaders("some headers")
                        .requestMethods("some methods")
                        .build())
                .response(CorsBuilder.ResponseCorsBuilder::build)
                .build();
        assertThat(options).hasSize(2);
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_REQUEST_HEADERS_HEADER)).isEqualTo("some headers");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_REQUEST_METHOD_HEADER)).isEqualTo("some methods");
    }

    @Test
    void new_build_produces_with_response_builder_options_produces_8_options() {
        CorsOptions options = CorsBuilder.newBuilder()
                .request(CorsBuilder.RequestCorsBuilder::build)
                .response(bld -> bld
                        .allowedMethods("PUT")
                        .allowedHeaders("Content-Type")
                        .allowedCredentials(true)
                        .preflightMaxAge(3000L)
                        .allowedOrigins("*")
                        .exposedHeaders("GET")
                        .allowedTimingOrigins("*")
                        .build())
                .build();
        assertThat(options).hasSize(7);
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_ALLOW_HEADERS_HEADER)).isEqualTo("Content-Type");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_ALLOW_METHODS_HEADER)).isEqualTo("PUT");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER)).isEqualTo("true");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER)).isEqualTo("*");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_MAX_AGE_HEADER)).isEqualTo("3000");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER)).isEqualTo("GET");
        assertThat(options.get(CorsOptions.Option.TIMING_ALLOW_ORIGIN_HEADER)).isEqualTo("*");
    }
}