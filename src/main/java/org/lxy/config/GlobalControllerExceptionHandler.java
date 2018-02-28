package org.lxy.config;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(basePackages = "org.lxy")
public class GlobalControllerExceptionHandler extends AbstractControllerExceptionHandler {
}
