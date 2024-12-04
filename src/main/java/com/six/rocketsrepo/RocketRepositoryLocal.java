package com.six.rocketsrepo;

import com.six.rocketsrepo.exceptions.RocketAlreadyExistsException;
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
        findRocketByName(rocket.getName()).ifPresentOrElse(
                r -> {
                    throw new RocketAlreadyExistsException(rocket.getName());
                },
                () -> rocketsDb.add(rocket)
        );
    }

    @Override
    public Optional<Rocket> findRocketByName(String rocketName) {
        return rocketsDb.stream()
                .filter(r -> r.getName().equals(rocketName))
                .findFirst();
    }

    @Override
    public List<Rocket> getRockets() {
        return rocketsDb;
    }
}
