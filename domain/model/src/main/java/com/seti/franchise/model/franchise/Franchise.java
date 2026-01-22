package com.seti.franchise.model.franchise;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class Franchise {

    String id;
    String name;

}
