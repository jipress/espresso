package com.akilisha.espresso.jett.application;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PathUtilsTest {

    @Test
    void pathToRegex() {
        String regex1 = PathUtils.pathToRegex("^/commits/(\\w+)(?:\\.\\.(\\w+))?$");
        assertThat(regex1).isEqualTo("^/commits/(\\w+)(?:\\.\\.(\\w+))?$");

        String regex2 = PathUtils.pathToRegex("/commits/:from..:to");
        assertThat(regex2).isEqualTo("/commits/([\\w-]+)..([\\w-]+)");
    }

    @Test
    void pathToRegexMap() {
        List<String> paths = List.of(
                "^/commits/(\\w+)(?:\\.\\.(\\w+))?$",
                "/series/:title/episode/:num/actors",
                "/shop/aile/bananas",
                "/shop/aile/bananas",
                "/commits/:from..:to",
                "/flights/:airport/:depart-:arrive/:gate");
        Map<String, String> pathMap = PathUtils.pathToRegexMap(paths);
        assertThat(pathMap).hasSize(5);
        assertThat(pathMap.get(paths.get(0))).isEqualTo("^/commits/(\\w+)(?:\\.\\.(\\w+))?$");
        assertThat(pathMap.get(paths.get(1))).isEqualTo("/series/([\\w-]+)/episode/([\\w-]+)/actors");
        assertThat(pathMap.get(paths.get(2))).isEqualTo("/shop/aile/bananas");
        assertThat(pathMap.get(paths.get(3))).isEqualTo("/shop/aile/bananas");
        assertThat(pathMap.get(paths.get(4))).isEqualTo("/commits/([\\w-]+)..([\\w-]+)");
        assertThat(pathMap.get(paths.get(5))).isEqualTo("/flights/([\\w-]+)/([\\w-]+)-([\\w-]+)/([\\w-]+)");
    }

    @Test
    void longestPathPrefix() {
        List<String> paths = List.of(
                "^/commits/(\\w+)(?:\\.\\.(\\w+))?$",
                "/series/:title/episode/:num/actors",
                "/shop/:aile/bananas",
                "/shop/:aile/bananas",
                "/commits/:from..:to",
                "/flights/:airport/:depart-:arrive/:gate");
        Set<String> longest = PathUtils.longestPathPrefix(paths);
        assertThat(longest).hasSize(5);
        assertThat(longest).contains(
                "/series/:title/episode/:num/actors",
                "/commits/:from..:to",
                "^/commits",
                "/shop/:aile/bananas",
                "/flights/:airport/:depart-:arrive/:gate");
    }

    @Test
    void extractPathVariables() {
        Map<String, String> params1 = PathUtils.extractPathVariables(
                "^/commits/(\\w+)(?:\\.\\.(\\w+))?$",
                "/commits/71dbb9c..4c084f9");
        assertThat(params1).hasSize(2);
        assertThat(params1.get("0")).isEqualTo("71dbb9c");
        assertThat(params1.get("1")).isEqualTo("4c084f9");

        Map<String, String> params2 = PathUtils.extractPathVariables(
                "^/commits/(\\w+)(?:\\.\\.(\\w+))?$",
                "/commits/71dbb9c");
        assertThat(params2).hasSize(2);
        assertThat(params2.get("0")).isEqualTo("71dbb9c");
        assertThat(params2.get("1")).isNull();

        Map<String, String> params3 = PathUtils.extractPathVariables(
                "/flights/:airport/:depart-:arrive/:gate",
                "/flights/ord/chicago-atlanta/D20");
        assertThat(params3).hasSize(4);
        assertThat(params3.get("airport")).isEqualTo("ord");
        assertThat(params3.get("depart")).isEqualTo("chicago");
        assertThat(params3.get("arrive")).isEqualTo("atlanta");
        assertThat(params3.get("gate")).isEqualTo("D20");
    }

    @Test
    void extractQueryVariables() {
        String query = "math=20&history=30&science=30&social=30&math=40";
        Map<String, List<String>> params = PathUtils.extractQueryVariables(query);
        assertThat(params).hasSize(4);
        assertThat(params.get("math")).hasSize(2);
        assertThat(params.get("social").get(0)).isEqualTo("30");
    }
}