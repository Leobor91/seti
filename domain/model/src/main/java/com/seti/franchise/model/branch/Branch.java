package com.seti.franchise.model.branch;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Branch {

    String id;
    String franchiseId;
    String name;
}
