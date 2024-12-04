package com.six.rocketsrepo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
class Mission {
    @EqualsAndHashCode.Exclude
    Long id;
    @EqualsAndHashCode.Include
    String name;
    @EqualsAndHashCode.Exclude
    MissionStatus status;
    @EqualsAndHashCode.Exclude
    Set<Rocket> assignedRockets = new HashSet<>();

    Mission(String name) {
        this.id = System.currentTimeMillis();
        this.name = name;
        this.status = MissionStatus.SCHEDULED;
    }

    void addRocket(Rocket rocket) {
        assignedRockets.add(rocket);
        this.start();
        rocket.launchFor(this.name);
    }

    boolean hasRocket(String rocketName) {
        return assignedRockets.stream()
                .anyMatch(rocket -> rocket.getName().equals(rocketName));
    }

    boolean canBeStarted() {
        return assignedRockets.stream()
                .allMatch(rocket -> rocket.getStatus() == RocketStatus.IN_SPACE);
    }

    int getAssignedRocketsCount() {
        return assignedRockets.size();
    }

    void start() {
        this.status = MissionStatus.IN_PROGRESS;
    }

    void suspend() {
        this.status = MissionStatus.PENDING;
    }

    void end() {
        assignedRockets.forEach(Rocket::releaseFromMission);
        this.status = MissionStatus.ENDED;
    }
}