package com.six.rocketsrepo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
class Rocket {
    @EqualsAndHashCode.Exclude
    Long id;
    @EqualsAndHashCode.Include
    String name;
    @EqualsAndHashCode.Exclude
    RocketStatus status;
    @EqualsAndHashCode.Exclude
    String missionName;

    Rocket(String name) {
        this.id = System.currentTimeMillis();
        this.name = name;
        this.status = RocketStatus.ON_GROUND;
    }

    void launchFor(String missionName) {
        this.missionName = missionName;
        this.status = RocketStatus.IN_SPACE;
    }

    void sendToRepair() {
        this.status = RocketStatus.IN_REPAIR;
    }

    void releaseFromRepair() {
        this.status = RocketStatus.IN_SPACE;
    }

    void releaseFromMission() {
        this.missionName = null;
        if (RocketStatus.IN_REPAIR != this.status) {
            this.status = RocketStatus.ON_GROUND;
        }
    }
}
