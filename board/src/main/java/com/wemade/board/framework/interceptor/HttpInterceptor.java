/***************************************************
 * Copyright(c) 2023-2024 WEMADE right reserved.
 *
 * Revision History
 * Author : hubert
 * Date : Fri Aug 11 2023
 * Description :
 *
 ****************************************************/
package com.wemade.board.framework.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.wemade.board.common.constant.ComnConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestUrl = request.getRequestURI();

        log.warn("  ** request uri : {}", requestUrl);
        log.warn("  ** remote addr : {}", request.getRemoteAddr());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = String.valueOf(headerNames.nextElement());
            String headerVal = request.getHeader(headerName);
            log.warn("  **header[{}] ==> {}", headerName, headerVal);
        }

        // ==========================================================
        // URI PATTREN CHECK
        // ==========================================================
        if (requestUrl.matches("(\\S)*/api/player/(\\S)*")) {
            // check header info
            String clientIp = request.getHeader(ComnConstants.CLIENT_IP);

            log.info("==> Client ip {}", clientIp);
            if (StringUtils.isEmpty(clientIp))
                clientIp = "";

            request.setAttribute(ComnConstants.CLIENT_IP, clientIp);
        }

        return true;
    }

}