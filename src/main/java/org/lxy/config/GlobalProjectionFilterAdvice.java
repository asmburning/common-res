package org.lxy.config;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@ControllerAdvice
public class GlobalProjectionFilterAdvice implements ResponseBodyAdvice<Object> {
  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    List<Annotation> annotations = Arrays.asList(returnType.getMethodAnnotations());
    for (Annotation annotation : annotations) {
      if (annotation.annotationType().equals(ProjectionFilter.class)) {
        return true;
      }
    }
    return false;
//    return annotations.stream().anyMatch(annotation -> annotation.annotationType().equals(ProjectionFilter.class));
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
    // Identify keys we are interested in.
    ProjectionFilter annotation = returnType.getMethodAnnotation(ProjectionFilter.class);
    String possibleFilter = annotation.name();

    HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

    String parameterValue = null;
    Enumeration<String> parameterNames = servletRequest.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String parameterName = parameterNames.nextElement();
      if (possibleFilter.contains(parameterName)) {
        parameterValue = servletRequest.getParameter(parameterName);
        break;
      }
    }

    MappingJacksonValue container = getOrCreateContainer(body);
    if (null == parameterValue) {
      container.setFilters(new SimpleFilterProvider().addFilter(Constant.FILTER_SYMBOL,
          SimpleBeanPropertyFilter.SerializeExceptFilter.serializeAllExcept()));
    } else {
      Assert.notNull(parameterValue);
      String[] parameterValues = parameterValue.split(Constant.SPLIT_SYMBOL);
      container.setFilters(new SimpleFilterProvider().addFilter(Constant.FILTER_SYMBOL,
          SimpleBeanPropertyFilter.SerializeExceptFilter.filterOutAllExcept(parameterValues)));
    }

    return container;
  }

  /**
   * Wrap the body in a {@link MappingJacksonValue} value container (for providing
   * additional serialization instructions) or simply cast it if already wrapped.
   */
  protected MappingJacksonValue getOrCreateContainer(Object body) {
    return body instanceof MappingJacksonValue ? (MappingJacksonValue) body : new MappingJacksonValue(body);
  }
}
