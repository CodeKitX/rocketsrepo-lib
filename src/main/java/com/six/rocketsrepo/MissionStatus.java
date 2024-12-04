package com.six.rocketsrepo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum MissionStatus {
    SCHEDULED("Scheduled"),
    PENDING("Pending"),
    IN_PROGRESS("In progress"),
    ENDED("Ended");

    private final String status;
}
