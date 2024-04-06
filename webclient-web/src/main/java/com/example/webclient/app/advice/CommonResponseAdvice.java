package com.example.webclient.app.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.webclient.app.sample1.Sample1ResponseResource;

@RestControllerAdvice
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * beforeBodyWrite()を実装することで、業務ロジック後の共通的な処理が行える。
     */
    @Override
    public Object beforeBodyWrite(
    		Object body, MethodParameter returnType, MediaType selectedContentType,
	          Class<? extends HttpMessageConverter<?>> selectedConverterType, 
	        		  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Sample1ResponseResource) {
            CommonResponseResource commonResponseResource = new CommonResponseResource();
            commonResponseResource.setMessage("This is a common message");
            commonResponseResource.setBizResponse(body);
            return commonResponseResource;
        }
        return body;
    }
}
