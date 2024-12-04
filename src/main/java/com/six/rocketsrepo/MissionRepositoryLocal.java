package com.six.rocketsrepo;

import com.six.rocketsrepo.exceptions.MissionAlreadyExistsException;
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
        findMissionByName(mission.getName()).ifPresentOrElse(
                m -> {
                    throw new MissionAlreadyExistsException(mission.getName());
                },
                () -> missionsDb.add(mission)
        );
    }

    @Override
    public Optional<Mission> findMissionByName(String missionName) {
        return missionsDb.stream()
                .filter(m -> m.getName().equals(missionName))
                .findFirst();
    }

    @Override
    public List<Mission> getMissions() {
        return missionsDb;
    }
}
