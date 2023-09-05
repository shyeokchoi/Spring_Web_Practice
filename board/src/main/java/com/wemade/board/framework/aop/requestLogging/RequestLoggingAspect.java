package com.wemade.board.framework.aop.requestLogging;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class RequestLoggingAspect {

    private final Gson gson = new Gson();

    //private final CmLogHistService cmLogHistoryService;

    // @Pointcut("within(com.wemade.pconsole.controller..*)")
    // @Pointcut("execution(* com.wemade.pconsole..*Service.*(..))")
    @Pointcut("@annotation(com.wemade.board.framework.aop.requestLogging.annotation.HistoryRegister)")
    public void onRequest() {
    }

    @Pointcut("@annotation(com.wemade.board.framework.aop.requestLogging.annotation.InsHistoryRegister)")
    public void onInsRequest() {
    }

    @Pointcut("@annotation(com.wemade.board.framework.aop.requestLogging.annotation.UdateHistoryRegister)")
    public void onUdateRequest() {
    }

    @Around("com.wemade.board.framework.aop.requestLogging.RequestLoggingAspect.onRequest()")
    public Object doCommonLogging(ProceedingJoinPoint pjp) throws Throwable {
        return doLogging(pjp, HistoryEnum.insert);
    }

    @Around("com.wemade.board.framework.aop.requestLogging.RequestLoggingAspect.onInsRequest()")
    public Object doInsLogging(ProceedingJoinPoint pjp) throws Throwable {
        return doLogging(pjp, HistoryEnum.insert);
    }

    @Around("com.wemade.board.framework.aop.requestLogging.RequestLoggingAspect.onUdateRequest())")
    public Object doUdateLogging(ProceedingJoinPoint pjp) throws Throwable {
        return doLogging(pjp, HistoryEnum.update);
    }
    
    /**
     * 대상 메소드 호출 이력 저장
     * @param pjp
     * @param histType
     * @return
     * @throws Throwable
     */
    private Object doLogging(ProceedingJoinPoint pjp, HistoryEnum histType) throws Throwable {
        Map<String, Object> paramMap = null;
        String paramStr = null;
        try {
            // DB저장용 문자열 생성
            try {
                paramMap = makeParams(pjp);
                paramStr = gson.toJson(paramMap); 
            } catch(Exception e) {
                String uuid = UUID.randomUUID().toString();
                log.error("[{}]",uuid, e);
                paramStr = "json parsing error. * uuid : " + uuid;  // 파싱에러가 발생해도 일단 logging은 한다. 
            }

            // 대상 메소드 실행
            Object obj = pjp.proceed(pjp.getArgs());
            
            // DB 저장
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            insHistoryInfo(paramStr, request, histType);
            
            return obj;
        } finally { // 로깅용 객체에 대한 레퍼런스 즉시 해제처리.
            paramMap = null;
            paramStr = null;
        }
    }

    /**
     * 메소드의 파라미터 추출해서 Map으로 리턴
     * @param joinPoint
     * @return
     */
    private Map<String, Object> makeParams(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            if (args[i] instanceof MultipartRequest || args[i] instanceof MultipartFile
                    || args[i] instanceof ServletRequest || args[i] instanceof ServletResponse) {
                continue;
            }

            params.put(parameterNames[i], args[i]);
        }
        return params;
    }

    /**
     * DB저장
     * @param param
     * @param request
     * @param hisType
     */
    private void insHistoryInfo(String param, HttpServletRequest request, HistoryEnum hisType) {
        CmLogHistDTO cmLogHistoryDTO = null;
        try {
            cmLogHistoryDTO = new CmLogHistDTO();
            cmLogHistoryDTO.setType(hisType);
            //cmLogHistoryDTO.setUserId(SessionUtil.getUserId()); // TODO : 요청자의 아이디를 셋팅해야함.
            cmLogHistoryDTO.setPath(request.getRequestURI());
            cmLogHistoryDTO.setHistory(param);
            cmLogHistoryDTO.setIp(request.getRemoteAddr());

            //cmLogHistoryService.insCmLogHistory(cmLogHistoryDTO);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            cmLogHistoryDTO = null;
        }
    }

}
