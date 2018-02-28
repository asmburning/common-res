package org.lxy.config;

import org.lxy.common.ApiResult;
import org.lxy.common.BusinessException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

public abstract class AbstractControllerExceptionHandler {

    protected ApiResult handle(BusinessException e, WebRequest request) {
        return buildExceptionResponse(e.getCode(), e.getMessage(),
                request);
    }

    protected String catchRestfulPath(WebRequest request) {
        String restOfTheUrl = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return restOfTheUrl;
    }

    protected ApiResult buildExceptionResponse(String errorCode, String errorMessage,
                                               WebRequest request) {
        ApiResult apiResult = new ApiResult();
        apiResult.setTimestamp(System.currentTimeMillis());
        apiResult.setCode(errorCode);
        apiResult.setMessage(errorMessage);
        apiResult.setPath(catchRestfulPath(request));
        return apiResult;
    }
}
