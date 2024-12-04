package com.six.rocketsrepo;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
class MissionRepositoryLocal implements MissionRepository {
    List<Mission> missionsDb = new ArrayList<>();

    @Override
    public void addMission(Mission mission) {
        log.debug("Adding a mission {}", mission);
    }

    @Override
    public Optional<Mission> findMissionByName(String missionName) {
        return Optional.empty();
    }

    @Override
    public List<Mission> getMissions() {
        return missionsDb;
    }
}
