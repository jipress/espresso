package com.akilisha.espresso.api.application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppSettingsTest {

    @Test
    void check_number_of_settings_available() {
        assertThat(AppSettings.Setting.values()).hasSize(20);
    }

    @Test
    void check_that_new_instance_contains_default_values() {
        AppSettings settings = new AppSettings();
        assertThat(settings).hasSize(12);
    }

    @Test
    void verify_that_updating_a_property_returns_updated_values() {
        AppSettings settings = new AppSettings();
        assertThat(settings.get(AppSettings.Setting.ENV)).isEqualTo("development");
        settings.put(AppSettings.Setting.ENV, "production");
        assertThat(settings.get(AppSettings.Setting.ENV)).isEqualTo("production");
    }
}