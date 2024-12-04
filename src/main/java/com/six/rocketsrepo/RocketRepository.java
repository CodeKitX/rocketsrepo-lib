package com.six.rocketsrepo;

import java.util.List;
import java.util.Optional;

public interface RocketRepository {
    void addRocket(Rocket rocket);

    Optional<Rocket> findRocketByName(String rocketName);

    List<Rocket> getRockets();

}
