package com.six.rocketsrepo;

import lombok.Data;

import java.util.Set;

@Data
class Mission {
    Long id;
    String name;
    MissionStatus status;
    Set<Rocket> assignedRockets;

    Mission(String name) {
        this.id = System.currentTimeMillis();
        this.name = name;
        this.status = MissionStatus.SCHEDULED;
    }

    boolean hasRocket(String rocketName) {
        return assignedRockets.stream().anyMatch(rocket -> rocket.getName().equals(rocketName));
    }

    int getAssignedRocketsCount() {
        return assignedRockets.size();
    }
}