package com.example.webclient.app.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomInterceptor implements HandlerInterceptor  {

	
    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

    	wrapResponse(response);
    	
    	return true;
	}
    
    private ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }	
	
    @Override
    public void postHandle(
    		HttpServletRequest request, HttpServletResponse response,
    		Object handler, ModelAndView modelAndView) throws Exception {
    	
//        // 共通の情報を追加する処理
//        Map<String, Object> additionalData = new HashMap<>();
//        additionalData.put("key", "value");
//
//        // レスポンスボディに共通の情報を追加
//        ObjectMapper objectMapper = new ObjectMapper();
//        String additionalDataJson = objectMapper.writeValueAsString(additionalData);
//        response.getWriter().write(additionalDataJson);
//        
//        ContentCachingResponseWrapper.class.cast(response).copyBodyToResponse();
    }	
}
