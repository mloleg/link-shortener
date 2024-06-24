package ru.mloleg.linkshortener.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoggingFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        log.info("Request: {} {}", request.getMethod(), request.getRequestURI());

        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);

        try {
            chain.doFilter(request, wrapper);
            String responseBody = new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

            log.info("Response: {} {} {}", request.getMethod(), request.getRequestURI(), responseBody);
        } finally {
            wrapper.copyBodyToResponse();
        }
    }
}
