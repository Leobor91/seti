package com.seti.franchise.mongo.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document
public class ProductDocument {

    @Id
    private String id;
    private String branchId;
    private String name;
    private Long stock;

}
