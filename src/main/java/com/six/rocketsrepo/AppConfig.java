package com.six.rocketsrepo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ControlCenter controlCenter() {
        return new ControlCenter(new RocketRepositoryLocal(), new MissionRepositoryLocal());
    }
}
