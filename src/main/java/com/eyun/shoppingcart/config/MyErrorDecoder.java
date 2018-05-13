package com.eyun.shoppingcart.config;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyErrorDecoder implements ErrorDecoder {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = null;
        try {
            exception =  FeignException.errorStatus(methodKey, response);
            if (400 <= response.status() || response.status() < 500){
                exception = new HystrixBadRequestException("request exception wrapper", exception);
            }else{
                logger.error(exception.getMessage(), exception);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return exception;
    }
}
