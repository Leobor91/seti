package com.seti.franchise.restconsumer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@Builder
public class ApiResponseDto<T>{

    int status;

    String message;

    T data;
}
