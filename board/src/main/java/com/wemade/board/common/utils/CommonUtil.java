/***************************************************
 * Copyright(c) 2022-2023 WEMADE right reserved.
 *
 * Revision History
 * Author : hubert
 * Date : Fri Feb 03 2023
 * Description :
 *
 ****************************************************/
package com.wemade.board.common.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.wemade.board.common.DTO.Direction;
import com.wemade.board.common.constant.FrkConstants;
import com.wemade.board.framework.base.BasePagingParam;
import com.wemade.board.framework.exception.BaseException;

public class CommonUtil {

    private CommonUtil() {
        throw new IllegalStateException("CommonUtil class");
    }

    public static String timeStamp() {
        Calendar cal = Calendar.getInstance();
        String s = String.format("%04d%02d%02d%02d%02d%02d", cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        return s;
    }

    public static String getDateStr(Date date) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
            return fmt.format(date);
        } catch(NullPointerException e) {
        	return "";
        } catch(IllegalArgumentException e) {
        	return "";
        }
    }

    private static double deg2rad(double deg) {
        return ( deg * Math.PI / 180.0 );
    }

    private static double rad2deg(double rad) {
        return ( rad * 180.0 / Math.PI );
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344 * 1000;
        return dist; // meter
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
        if ( !( ra instanceof ServletRequestAttributes ) ) {
            throw new IllegalStateException("Current request is not a servlet request");
        }
        return ( (ServletRequestAttributes) ra ).getRequest();
    }

    public static HttpServletResponse getResponse() {
    	RequestAttributes ra = RequestContextHolder.getRequestAttributes();
    	if ( !( ra instanceof ServletRequestAttributes ) ) {
            throw new IllegalStateException("Current request is not a servlet response");
        }
        return ( (ServletRequestAttributes) ra ).getResponse();
    }

    public static String getHeaderOrCookie(String name) throws UnsupportedEncodingException {
        return getHeaderOrCookie(getRequest(), name);
    }

    public static String getHeaderOrCookie(HttpServletRequest request, String name)
            throws UnsupportedEncodingException {
        String value = request.getHeader(name);
        if ( value == null || value.isEmpty() ) {
            Cookie cookie = WebUtils.getCookie(request, name);
            if ( cookie != null ) {
                value = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
            }
        }
        return value;
    }

    public static String getClassName(Object object) {

    	String className = "";

    	if(object != null) {
    		className = CommonUtil.getClassName(object.getClass());
    	}

    	return className;
    }

    @SuppressWarnings("rawtypes")
    public static String getClassName(Class clz) {

    	int pos = clz.getName().indexOf("$$");
        if (pos ==-1) {
            pos = clz.getName().indexOf("@");
        }
        if (pos == -1) {
            return clz.getName();
        } else {
            return clz.getName().substring(0, pos);
        }
    }

    @SuppressWarnings("rawtypes")
    public static String getClassSimpleName(Object object) {
    	Class clz = object.getClass();
    	int pos = clz.getName().indexOf("$$");
    	if (pos ==-1) {
    		pos = clz.getName().indexOf("@");
    	}
    	if (pos == -1) {
    		return clz.getSimpleName();
    	} else {
    		return clz.getName().substring(0, pos);
    	}
    }

    public static Map<String, Object> makeMapByParam(MultipartHttpServletRequest multiRequest) {
        String name = "";
        String value = "";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Enumeration<String> params = multiRequest.getParameterNames();
        while(params.hasMoreElements()){
            name = (String)params.nextElement();
            value = multiRequest.getParameter(name);
            paramMap.put(name, value);
        }

        return paramMap;
    }

    public static <T> T convertToValueObject(Map<String, Object> map, Class<T> type) {
        try {
            Objects.requireNonNull(type, "Class cannot be null");
            T instance = type.getConstructor().newInstance();

            if (map == null || map.isEmpty()) {
                return instance;
            }

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Field[] fields = type.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    String name = field.getName();

                    boolean isSameType = entry.getValue().getClass().equals(field.getType());
                    boolean isSameName = entry.getKey().equals(name);

                    if (isSameType && isSameName) {
                        field.set(instance, map.get(name));
                        break;
                    }
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String genRanNumber(int len) {

        int createNum = 0;
        String resultNum = "";

        try {
            SecureRandom rand = SecureRandom.getInstanceStrong();

            for(int i=0; i<len; i++) {
                createNum = rand.nextInt(9);
                resultNum += createNum+"";
            }
        } catch (NoSuchAlgorithmException ex) {

        }

        return resultNum;
    }

    public static HashMap<String, Object> convertMap(MultipartHttpServletRequest multiRequest) {
        HashMap<String, Object> hmap = new HashMap<String, Object>();
        String key;

        Enumeration<String> iter = multiRequest.getParameterNames();

        while (iter.hasMoreElements()) {
            key = (String) iter.nextElement();
            if (multiRequest.getParameterValues(key).length > 1) {
                hmap.put(key, multiRequest.getParameterValues(key));
            } else {
                hmap.put(key, multiRequest.getParameter(key));
            }
        }
        return hmap;
    }

    public static String getExtension(String fileName) {
        if(StringUtils.isEmpty(fileName)) return null;

        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        return ext;
    }

    /**
     * 객체의 필드 복사 처리.
     * @param <E>
     * @param <D>
     * @param dto
     * @param resType
     * @return
     */
    public static <E, D> E copyProperties(D dto, Class<E> resType){
        if( dto == null || resType == null ) {
            return null;
        }
        E returnEntity = null;
        try {
            returnEntity = resType.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(dto, returnEntity);
        return returnEntity;
    }
    
    /**
     * 페이지 리스트의 rowNumber계산을 위한 시작번호 생성
     * @param totalCount
     * @param BasePagingParam
     * @return
     */
    public static long genStartNoForPagingResult(long totalCount, BasePagingParam pagingParam) {
        long startNo;
        if(pagingParam.getDirection() == Direction.DESC) {
            startNo = totalCount;
            startNo = startNo - ((pagingParam.getCurrPage() - 1) * pagingParam.getPageSize());
            
        } else if(pagingParam.getDirection() == Direction.ASC) {
            startNo = 1;
            startNo = startNo + ((pagingParam.getCurrPage() - 1) * pagingParam.getPageSize());
        } else {
            throw new BaseException("Invalid Direction. direction : " + pagingParam.getDirection(), FrkConstants.CD_INTERNAL_ERR);
        }
        return startNo;
    }
    
}
