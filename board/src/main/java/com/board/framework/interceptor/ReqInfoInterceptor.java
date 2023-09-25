package com.board.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.board.constant.RequestAttributeKeys;
import com.board.dto.common.ReqInfoDTO;

@Component
public class ReqInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        String userAgent = request.getHeader("User-Agent");

        String ipAddr = request.getHeader("X-FORWARDED-FOR");
        if (ipAddr == null) {
            ipAddr = request.getRemoteAddr();
        }

        ReqInfoDTO reqInfoDTO = new ReqInfoDTO(ipAddr, userAgent);

        request.setAttribute(RequestAttributeKeys.REQ_INFO, reqInfoDTO);

        return true;
    }
}
