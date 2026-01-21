package com.seti.franchise.model.product;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Product {

    String id;
    String branchId;
    String name;
    Long stock;

}
