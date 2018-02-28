package org.lxy.config;

import org.lxy.common.ApiResult;
import org.lxy.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Locale;

@ControllerAdvice(basePackages = "org.lxy")
public class PlatformExceptionHandler extends GlobalControllerExceptionHandler {

    private static final String COMMON_EXCEPTION_CODE = "00500";
    private static final String BAD_REQUEST_EXCEPTION_CODE = "00400";
    private static final String METHOD_NOT_ALLOWED_EXCEPTION_CODE = "00405";

    @Autowired
    protected MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public ApiResult handleMethodArgumentNotValidException(Exception e, WebRequest request) {
        return buildExceptionResponse(BAD_REQUEST_EXCEPTION_CODE, e.getMessage(), request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ApiResult handleBindException(BindException e, WebRequest request) {
        return buildExceptionResponse(BAD_REQUEST_EXCEPTION_CODE, getBindExceptionMessage(e.getBindingResult()), request);
    }

    private String getBindExceptionMessage(BindingResult bindingResult) {
        final Locale locale = LocaleContextHolder.getLocale();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder msg = new StringBuilder("Arguments invalid:");
        for (FieldError fieldError : fieldErrors) {
            msg.append(fieldError.getField()).append("=>").append(messageSource.getMessage(fieldError, locale)).append("/n");
        }
        return msg.toString();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ApiResult handleUserAuthorizationException(BusinessException e, WebRequest request) {
        return handle(e, request);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) // 405
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ApiResult handleHttpRequestMethodNotSupportedException(Exception e, WebRequest request) {
        return buildExceptionResponse(METHOD_NOT_ALLOWED_EXCEPTION_CODE, e.getMessage(), request);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult handleException(Exception e, WebRequest request) {
        return buildExceptionResponse(COMMON_EXCEPTION_CODE, e.getMessage(), request);
    }

}
