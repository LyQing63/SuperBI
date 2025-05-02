package io.lyqing64.github.superbi.config;

import io.lyqing64.github.superbi.common.Response;
import io.lyqing64.github.superbi.enums.BusinessCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 如果返回值已经是 Response 类型，则直接返回
        if (body instanceof Response) {
            return body;
        }
        // 如果返回值是 BusinessCode 枚举，则转换为 Response 对象
        if (body instanceof BusinessCode) {
            BusinessCode businessCode = (BusinessCode) body;
            return Response.error(businessCode);
        }
        // 其他情况，包装为成功的 Response 对象
        return Response.success(body);
    }
}