package com.akilisha.espresso.jett.handler;

import com.akilisha.espresso.api.application.CorsOptions;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.annotation.ManagedObject;

import java.io.IOException;

import static com.akilisha.espresso.api.application.CorsOptions.Option.*;

@ManagedObject("Apply requested CORS headers in the response")
@RequiredArgsConstructor
@Slf4j
public class CorsHandler extends HandlerWrapper {

    final CorsOptions options;

    private static boolean isPreflightRequest(HttpServletRequest request) {
        return request.getHeader("Origin") != null && request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("Origin header - {}", request.getHeader("Origin"));
        log.info("Request method - {}", request.getMethod());

        // these are is necessary to allow cross-origin requests
        if (options.get(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER) != null) {
            response.addHeader(CorsOptions.Option.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER.name, options.get(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER));
        }

        if (options.get(ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER) != null) {
            response.addHeader(CorsOptions.Option.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER.name, options.get(ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER));
        }

        if (options.get(ACCESS_CONTROL_ALLOW_HEADERS_HEADER) != null) {
            response.addHeader(CorsOptions.Option.ACCESS_CONTROL_ALLOW_HEADERS_HEADER.name, options.get(ACCESS_CONTROL_ALLOW_HEADERS_HEADER));
        }

        if (options.get(ACCESS_CONTROL_MAX_AGE_HEADER) != null) {
            response.addHeader(CorsOptions.Option.ACCESS_CONTROL_MAX_AGE_HEADER.name, options.get(ACCESS_CONTROL_MAX_AGE_HEADER));
        }

        if (options.get(ACCESS_CONTROL_EXPOSE_HEADERS_HEADER) != null) {
            response.addHeader(CorsOptions.Option.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER.name, options.get(ACCESS_CONTROL_EXPOSE_HEADERS_HEADER));
        }

        if (options.get(TIMING_ALLOW_ORIGIN_HEADER) != null) {
            response.addHeader(CorsOptions.Option.TIMING_ALLOW_ORIGIN_HEADER.name, options.get(TIMING_ALLOW_ORIGIN_HEADER));
        }

        // this is necessary when pre-flighting to allow cross-origin requests
        if (isPreflightRequest(request)) {
            if (options.get(ACCESS_CONTROL_ALLOW_METHOD_HEADER) != null) {
                response.addHeader(CorsOptions.Option.ACCESS_CONTROL_ALLOW_METHOD_HEADER.name, options.get(ACCESS_CONTROL_ALLOW_METHOD_HEADER));
            }

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            baseRequest.setHandled(true);
            return;
        }

        if (_handler != null)
            _handler.handle(target, baseRequest, request, response);
    }
}

