package com.wemade.board.common.utils;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.util.UriEncoder;

import com.wemade.board.framework.exception.BaseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * rest 통신을 위한 utils
 * 
 * @author : dev_tony85
 * @Date : 2023. 9. 4.
 * @Description :
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestUtil {

    private final RestTemplate restTemplate;

    /**
     * http get호출
     *
     * @param url
     * @param reqParam : nullable
     * @param resType
     * @return
     * 
     */
    public <R> R callGet(String url, MultiValueMap<String, String> reqParam, Class<R> resType) {
        return callGet(url, null, reqParam, resType);
    }

    /**
     * http get호출
     *
     * @param url
     * @param headers  : nullable
     * @param reqParam : nullable
     * @param resType
     * @return
     * 
     */
    public <R> R callGet(String url, HttpHeaders headers, MultiValueMap<String, String> reqParam, Class<R> resType) {

        String apiURL = UriComponentsBuilder.fromHttpUrl(url).queryParams(reqParam).build().toUriString();
        return callRest(apiURL, HttpMethod.GET, headers, null, resType);
    }

    /**
     * http post 호출
     *
     * @param url
     * @param reqParam : nullable
     * @param resType
     * @return
     * 
     */
    public <P, R> R callPost(String url, P reqParam, Class<R> resType) {
        return callRest(url, HttpMethod.POST, null, reqParam, resType);
    }

    /**
     * http post 호출
     *
     * @param url
     * @param headers  : nullable
     * @param reqParam : nullable
     * @param resType
     * @return
     * 
     */
    public <P, R> R callPost(String url, HttpHeaders headers, P reqParam, Class<R> resType) {
        return callRest(url, HttpMethod.POST, headers, reqParam, resType);
    }

    /**
     * rest call
     *
     * @param url
     * @param method
     * @param reqParam : nullable
     * @param resType
     * @return
     * 
     */
    public <P, R> R callRest(String url, HttpMethod method, P reqParam, Class<R> resType) {
        return callRest(url, method, null, reqParam, resType);
    }

    /**
     * rest call
     *
     * @param url
     * @param method
     * @param headers  : nullable
     * @param reqParam : nullable
     * @param resType
     * @return
     * 
     */
    public <P, R> R callRest(String url, HttpMethod method, HttpHeaders headers, P reqParam, Class<R> resType) {
        ResponseEntity<R> responseEntity = null;

        long requestTime = 0;
        long responseTime = 0;
        try {

            // header 셋팅
            if (headers == null) {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
            }

            // request 셋팅
            HttpEntity<P> request = null;
            if (reqParam != null) {
                request = new HttpEntity<P>(reqParam, headers);
            } else {
                request = new HttpEntity<>(headers);
            }

            // http 호출
            requestTime = System.currentTimeMillis(); // 요청 시작 시간
            responseEntity = restTemplate.exchange(URI.create(UriEncoder.encode(url)), method, request, resType); // http
                                                                                                                  // 요청
            responseTime = System.currentTimeMillis(); // 응답 시간

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error(e.toString());
            throw new BaseException(140);
        } catch (HttpServerErrorException e) {
            log.error(e.toString());
            throw new BaseException(141);
        } catch (UnknownHttpStatusCodeException e) {
            log.error(e.toString());
            throw new BaseException(142);
        } finally {
            log.info("==> REQUEST [{}, {}, *param : {}]", url, method, reqParam);

            if (responseEntity != null) {
                log.info("<== RESPONSE code : [{}], time(mills) : [{}]", responseEntity.getStatusCode(),
                        (responseTime - requestTime));
            }
        }
    }

}
