package com.example.webclient.app;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import lombok.val;

public class RequestResponseCacheFilter extends OncePerRequestFilter {
	
	Logger log = LoggerFactory.getLogger(RequestResponseCacheFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            beforeRequest(request, response);
            filterChain.doFilter(request, response);
        }
        finally {
            afterRequest(request, response);
            response.copyBodyToResponse();
        }
    }

    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
//        if (log.isInfoEnabled()) {
//            logRequestHeader(request, request.getRemoteAddr() + "|>");
//        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
//        if (log.isInfoEnabled()) {
//            logRequestBody(request, request.getRemoteAddr() + "|>");
//            logResponse(response, request.getRemoteAddr() + "|<");
//        }
    }

//    private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix) {
//        val queryString = request.getQueryString();
//        if (queryString == null) {
//            log.info("{} {} {}", prefix, request.getMethod(), request.getRequestURI());
//        } else {
//            log.info("{} {} {}?{}", prefix, request.getMethod(), request.getRequestURI(), queryString);
//        }
//        Collections.list(request.getHeaderNames()).forEach(headerName ->
//            Collections.list(request.getHeaders(headerName)).forEach(headerValue ->
//                log.info("{} {}: {}", prefix, headerName, headerValue)));
//        log.info("{}", prefix);
//    }
//
//    private static void logRequestBody(ContentCachingRequestWrapper request, String prefix) {
//        val content = request.getContentAsByteArray();
//        if (content.length > 0) {
//            logContent(content, request.getContentType(), request.getCharacterEncoding(), prefix);
//        }
//    }
//
//    private static void logResponse(ContentCachingResponseWrapper response, String prefix) {
//        val status = response.getStatus();
//        log.info("{} {} {}", prefix, status, HttpStatus.valueOf(status).getReasonPhrase());
//        response.getHeaderNames().forEach(headerName ->
//            response.getHeaders(headerName).forEach(headerValue ->
//                log.info("{} {}: {}", prefix, headerName, headerValue)));
//        log.info("{}", prefix);
//        val content = response.getContentAsByteArray();
//        if (content.length > 0) {
//            logContent(content, response.getContentType(), response.getCharacterEncoding(), prefix);
//        }
//    }
//
//    private static void logContent(byte[] content, String contentType, String contentEncoding, String prefix) {
//        val mediaType = MediaType.valueOf(contentType);
//        val visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
//        if (visible) {
//            try {
//                val contentString = new String(content, contentEncoding);
//                Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> log.info("{} {}", prefix, line));
//            } catch (UnsupportedEncodingException e) {
//                log.info("{} [{} bytes content]", prefix, content.length);
//            }
//        } else {
//            log.info("{} [{} bytes content]", prefix, content.length);
//        }
//    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}
