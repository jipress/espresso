package com.akilisha.espresso.demos.client;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.component.LifeCycle;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CookieClient {

    public static void main(String[] args) throws Exception {
        // Think of an HttpClient instance as a browser instance.
        HttpClient httpClient = new HttpClient();
        // Configure HttpClient, for example:
        httpClient.setFollowRedirects(false);
        // Start HttpClient.
        httpClient.start();
        handleRequest(httpClient);
        new Thread(() -> LifeCycle.stop(httpClient)).start();
    }

    public static void handleRequest(HttpClient httpClient) throws ExecutionException, InterruptedException, TimeoutException {
        // create cookie
        HttpCookie cookie = new HttpCookie("foo", "bar");
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(TimeUnit.DAYS.toSeconds(1));

        // add to cookie store
        CookieStore cookieStore = httpClient.getCookieStore();
        cookieStore.add(URI.create("http://localhost:3031"), cookie);

        // view cookies in the store
        List<HttpCookie> cookies = cookieStore.get(URI.create("http://localhost:3031/time"));
        System.out.printf("cookies -> %s\n", cookies);

        // fire off request having cookies
        ContentResponse response = httpClient.newRequest("http://localhost:3031/time")
                .cookie(cookie)
                .send();
        System.out.printf("response -> %s\n", response.getContentAsString());
    }
}
