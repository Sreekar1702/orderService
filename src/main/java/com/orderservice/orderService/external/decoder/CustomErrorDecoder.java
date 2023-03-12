package com.orderservice.orderService.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.orderService.exception.CustomException;
import com.orderservice.orderService.external.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.ObjectError;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("::{}",response.request().url());
        log.info("::{}",response.request().headers());
        try {
            ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(),
                    ErrorResponse.class);
            return new CustomException(errorResponse.getMessage(),errorResponse.getErrorCode(),response.status());
        } catch (IOException e) {
            throw new CustomException("Internal Server Error","Internal_Server_ERROR",500);
        }
    }
}
