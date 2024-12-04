package com.six.rocketsrepo;

import com.six.rocketsrepo.exceptions.ControlCenterException;
import com.six.rocketsrepo.exceptions.MissionDoesNotExistException;
import com.six.rocketsrepo.exceptions.RocketAlreadyAssignedException;
import com.six.rocketsrepo.exceptions.RocketDoesNotExistException;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ControlCenter {
    private final RocketRepository rocketRepository;
    private final MissionRepository missionRepository;

    void addRocket(String rocketName) {
        if (rocketName == null || rocketName.isBlank()) {
            throw new ControlCenterException("Rocket name cannot be null or empty");
        }

        Rocket rocket = new Rocket(rocketName);
        rocketRepository.addRocket(rocket);
    }

    void scheduleMission(String missionName) {
        if (missionName == null || missionName.isBlank()) {
            throw new ControlCenterException("Mission name cannot be null or empty");
        }

        Mission mission = new Mission(missionName);
        missionRepository.addMission(mission);
    }

    void assignRocketToMission(String rocketName, String missionName) {
        Rocket rocket = getRocketOrThrow(rocketName);
        throwIfRocketNotAssignable(rocket);

        Mission mission = getMissionOrThrow(missionName);

        mission.addRocket(rocket);
    }

    void assignRocketsToMission(List<String> rocketNames, String missionName) {
        rocketNames.stream()
                .map(this::getRocketOrThrow)
                .forEach(rocket -> {
                    throwIfRocketNotAssignable(rocket);
                    Mission mission = getMissionOrThrow(missionName);
                    mission.addRocket(rocket);
                });
    }

    void sendRocketToRepair(String rocketName) {
        Rocket rocket = getRocketOrThrow(rocketName);
        getMissionOrThrow(rocket.getMissionName()).suspend();
        rocket.sendToRepair();
    }

    void releaseRocketFromRepair(String rocketName) {
        Rocket rocket = getRocketOrThrow(rocketName);
        rocket.releaseFromRepair();

        Mission mission = getMissionOrThrow(rocket.getMissionName());
        if (mission.canBeStarted()) {
            mission.start();
        }
    }

    void endMission(String missionName) {
        getMissionOrThrow(missionName).end();
    }

    void missionStatusReport() {
        List<Mission> missionsSorted = missionRepository.getMissions().stream()
                .sorted(Comparator.comparingInt(Mission::getAssignedRocketsCount).reversed()
                        .thenComparing(Comparator.comparing(Mission::getName).reversed()))
                .toList();

        missionsSorted.forEach(mission -> {
            System.out.printf("->%s - %s - Dragons: %d%n",
                    mission.getName(),
                    mission.getStatus().getStatus(),
                    mission.getAssignedRocketsCount());
            mission.getAssignedRockets().forEach(rocket -> {
                System.out.printf("---> %s - %s%n",
                        rocket.getName(),
                        rocket.getStatus().getStatus());
            });
        });
    }

    private Mission getMissionOrThrow(String missionName) {
        if (missionName == null || missionName.isBlank()) {
            throw new ControlCenterException("Mission name cannot be null or empty");
        }
        return missionRepository.findMissionByName(missionName)
                .orElseThrow(() -> new MissionDoesNotExistException(missionName));
    }

    private static void throwIfRocketNotAssignable(Rocket rocket) {
        if (rocket == null) {
            throw new ControlCenterException("Rocket cannot be null");
        }

        if (RocketStatus.ON_GROUND != rocket.getStatus()) {
            throw new RocketAlreadyAssignedException("Rocket assigned to a mission");
        }
    }

    private Rocket getRocketOrThrow(String rocketName) {
        if (rocketName == null || rocketName.isBlank()) {
            throw new ControlCenterException("Rocket name cannot be null or empty");
        }

        return rocketRepository.findRocketByName(rocketName)
                .orElseThrow(() -> new RocketDoesNotExistException(rocketName));
    }

}
