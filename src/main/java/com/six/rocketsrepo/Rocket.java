package com.six.rocketsrepo;

import lombok.Data;

@Data
class Rocket {
    Long id;
    String name;
    RocketStatus status;

    Rocket(String name) {
        this.id = System.currentTimeMillis();
        this.name = name;
        this.status = RocketStatus.ON_GROUND;
    }
}
