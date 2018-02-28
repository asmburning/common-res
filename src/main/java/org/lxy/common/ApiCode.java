package org.lxy.common;

import lombok.Getter;


@Getter
public enum ApiCode {

    OK("200", "OK"),
    TOKEN_EXPIRE("30001", "登录已过期"),
    HTTP_INNER("30010", "内部服务HTTP不通"),
    INVALID_PARAM("30020", "参数错误"),;

    String code;
    String desc;

    ApiCode(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

}
