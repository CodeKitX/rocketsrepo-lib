package com.six.rocketsrepo;

import java.util.List;
import java.util.Optional;

public interface MissionRepository {
    void addMission(Mission mission);

    Optional<Mission> findMissionByName(String missionName);

    List<Mission> getMissions();

}
