package com.six.rocketsrepo;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
class RocketRepositoryLocal implements RocketRepository {
    List<Rocket> rocketsDb = new ArrayList<>();

    @Override
    public void addRocket(Rocket rocket) {
        log.debug("Adding a rocket {}", rocket);
    }

    @Override
    public Optional<Rocket> findRocketByName(String rocketName) {
        return Optional.empty();
    }

    @Override
    public List<Rocket> getRockets() {
        return rocketsDb;
    }
}
