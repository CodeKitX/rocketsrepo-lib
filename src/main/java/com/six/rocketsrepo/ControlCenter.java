package com.six.rocketsrepo;

import com.six.rocketsrepo.exceptions.RocketDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ControlCenter {
    private final RocketRepository rocketRepository;
    private final MissionRepository missionRepository;

    void addRocket(String rocketName) {
        Rocket rocket = new Rocket(rocketName);
        rocketRepository.addRocket(rocket);
    }

    void scheduleMission(String missionName) {
        Mission mission = new Mission(missionName);
        missionRepository.addMission(mission);

    }

    void assignRocketToMission(String rocketName, String missionName) {

    }

    void assignRocketsToMission(List<String> rocketNames, String missionName) {

    }

    void sendRocketToRepair(String rocketName) {

    }

    void releaseRocketFromRepair(String rocketName) {

    }

    void endMission(String missionName) {

    }

    List<Mission> missionStatusReport() {
        return null;
    }

}
