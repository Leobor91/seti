package com.seti.franchise.restconsumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String id;
    private String name;
    private String branchId;
    private Long stock;
}
