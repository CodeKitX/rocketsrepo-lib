package com.six.rocketsrepo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum RocketStatus {
    ON_GROUND("On ground"),
    IN_SPACE("In space"),
    IN_REPAIR("In repair");

    private final String status;
}
