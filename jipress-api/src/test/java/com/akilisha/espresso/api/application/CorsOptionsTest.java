package com.akilisha.espresso.api.application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CorsOptionsTest {

    private static CorsOptions getCorsOptions() {
        CorsOptions options = new CorsOptions();
        options.put(CorsOptions.ALLOWED_CREDENTIALS_PARAM, "allowed credentials");
        options.put(CorsOptions.ALLOWED_HEADERS_PARAM, "allowed headers");
        options.put(CorsOptions.ALLOWED_METHODS_PARAM, "allowed methods");
        options.put(CorsOptions.ALLOWED_ORIGINS_PARAM, "allowed origin");
        options.put(CorsOptions.ALLOWED_TIMING_ORIGINS_PARAM, "allowed timing");
        options.put(CorsOptions.PREFLIGHT_MAX_AGE_PARAM, "allowed max age");
        options.put(CorsOptions.EXPOSED_HEADERS_PARAM, "exposed headers");
        return options;
    }

    @Test
    void check_number_of_options_available() {
        assertThat(CorsOptions.Option.values()).hasSize(9);
    }

    @Test
    void check_that_new_instance_contains_no_default_values() {
        CorsOptions options = new CorsOptions();
        assertThat(options).hasSize(0);
    }

    @Test
    void check_that_wide_open_instance_contains_default_values() {
        CorsOptions options = CorsOptions.wideOpen();
        assertThat(options).hasSize(7);
    }

    @Test
    void check_that_using_property_names_sets_correct_values() {
        CorsOptions options = getCorsOptions();

        assertThat(options).hasSize(7);
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER)).isEqualTo("allowed origin");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER)).isEqualTo("allowed credentials");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_ALLOW_METHODS_HEADER)).isEqualTo("allowed methods");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_ALLOW_HEADERS_HEADER)).isEqualTo("allowed headers");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_MAX_AGE_HEADER)).isEqualTo("allowed max age");
        assertThat(options.get(CorsOptions.Option.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER)).isEqualTo("exposed headers");
        assertThat(options.get(CorsOptions.Option.TIMING_ALLOW_ORIGIN_HEADER)).isEqualTo("allowed timing");
    }
}