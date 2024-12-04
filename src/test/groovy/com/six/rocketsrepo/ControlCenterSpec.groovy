package com.six.rocketsrepo

import com.six.rocketsrepo.exceptions.MissionAlreadyExistsException
import com.six.rocketsrepo.exceptions.RocketAlreadyAssignedException
import com.six.rocketsrepo.exceptions.RocketDoesNotExistException
import spock.lang.Specification
import spock.lang.Subject

import com.six.rocketsrepo.exceptions.RocketAlreadyExistsException

class ControlCenterSpec extends Specification {

    public static final String FALCON_9 = "Falcon 9"
    public static final String RED_DRAGON = "Red Dragon"
    public static final String MISSION_1 = "Mission 1"
    public static final String MISSION_2 = "Mission 2"

    def rocketRepo = new RocketRepositoryLocal()
    def missionRepo = new MissionRepositoryLocal()

    @Subject
    ControlCenter controlCenter = new ControlCenter(rocketRepo, missionRepo)

    def setup() {
        rocketRepo.getRockets().clear()
        missionRepo.getMissions().clear()
    }

    def "should add a rocket to repo"() {
        given:
        def rocketsCount = rocketRepo.getRockets().size()

        when:
        controlCenter.addRocket(FALCON_9)

        then:
        rocketRepo.getRockets().size() == rocketsCount + 1
    }

    def "should not duplicate rockets in repo"() {
        given:
        controlCenter.addRocket(FALCON_9)

        when:
        controlCenter.addRocket(FALCON_9)

        then:
        thrown(RocketAlreadyExistsException)

    }

    def "should add a new mission"() {
        given:
        def missionsCount = missionRepo.getMissions().size()

        when:
        controlCenter.scheduleMission(MISSION_1)

        then:
        missionRepo.getMissions().size() == missionsCount + 1
    }

    def "should not duplicate missions"() {
        given:
        controlCenter.scheduleMission(MISSION_1)

        when:
        controlCenter.scheduleMission(MISSION_1)

        then:
        thrown(MissionAlreadyExistsException)
    }

    def "should assign a rocket to a mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        def mission = missionRepo.findMissionByName(MISSION_1).orElseThrow()
        def rocketsCount = mission.assignedRocketsCount

        when:
        controlCenter.assignRocketToMission(FALCON_9, MISSION_1)

        then:
        def updatedMission = missionRepo.findMissionByName(MISSION_1).orElseThrow()
        updatedMission.hasRocket(FALCON_9)
        updatedMission.assignedRocketsCount == rocketsCount + 1
    }

    def "should not assign a rocket assigned to same mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.assignRocketToMission(FALCON_9, MISSION_1)

        when:
        controlCenter.assignRocketToMission(FALCON_9, MISSION_1)

        then:
        thrown(RocketAlreadyAssignedException)
    }

    def "should not assign a rocket assigned to other mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.scheduleMission(MISSION_2)
        controlCenter.addRocket(FALCON_9)
        controlCenter.assignRocketToMission(FALCON_9, MISSION_1)

        when:
        controlCenter.assignRocketToMission(FALCON_9, MISSION_2)

        then:
        thrown(RocketAlreadyAssignedException)
    }

    def "should not assign a non existing rocket assigned to mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)

        when:
        controlCenter.assignRocketToMission(FALCON_9, MISSION_1)

        then:
        thrown(RocketDoesNotExistException)
    }


    def "should change rocket status after rocket is assigned to mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)

        when:
        controlCenter.assignRocketToMission(FALCON_9, MISSION_1)

        then:
        rocketRepo.findRocketByName(FALCON_9)
                .orElseThrow()
                .getStatus() == RocketStatus.IN_SPACE
    }

    def "should assign multiple rockets to mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON)
        def rockets = [FALCON_9, RED_DRAGON]
        def mission = missionRepo.findMissionByName(MISSION_1).orElseThrow()
        def rocketsCount = mission.assignedRocketsCount

        when:
        controlCenter.assignRocketsToMission(rockets, MISSION_1)

        then:
        def updatedMission = missionRepo.findMissionByName(MISSION_1).orElseThrow()
        updatedMission.hasRocket(FALCON_9)
        updatedMission.hasRocket(RED_DRAGON)
        updatedMission.assignedRocketsCount == rocketsCount + rockets.size()
    }

    def "should not assign any rocket if one of the rockets is already assigned to same mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON)
        controlCenter.assignRocketToMission(FALCON_9, MISSION_1)
        def rockets = [FALCON_9, RED_DRAGON]

        when:
        controlCenter.assignRocketsToMission(rockets, MISSION_1)

        then:
        thrown(RocketAlreadyAssignedException)
    }

    def "should not assign any rocket if one of the rockets is already assigned to other mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.scheduleMission(MISSION_2)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON)
        controlCenter.assignRocketToMission(FALCON_9, MISSION_1)
        def rockets = [FALCON_9, RED_DRAGON]

        when:
        controlCenter.assignRocketsToMission(rockets, MISSION_2)

        then:
        thrown(RocketAlreadyAssignedException)
    }

    def "should change rockets statuses after rockets are assigned to mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON)
        def rockets = [FALCON_9, RED_DRAGON]

        when:
        controlCenter.assignRocketsToMission(rockets, MISSION_1)

        then:
        rocketRepo.findRocketByName(FALCON_9)
                .orElseThrow()
                .getStatus() == RocketStatus.IN_SPACE
        rocketRepo.findRocketByName(RED_DRAGON)
                .orElseThrow()
                .getStatus() == RocketStatus.IN_SPACE
    }

    def "should start mission when rockets assigned"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON);
        def rockets = [FALCON_9, RED_DRAGON]

        when:
        controlCenter.assignRocketsToMission(rockets, MISSION_1)

        then:
        missionRepo.findMissionByName(MISSION_1)
                .orElseThrow()
                .getStatus() == MissionStatus.IN_PROGRESS
    }

    def "should suspend mission when rocket is sent to repair"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON);
        def rockets = [FALCON_9, RED_DRAGON]
        controlCenter.assignRocketsToMission(rockets, MISSION_1)

        when:
        controlCenter.sendRocketToRepair(FALCON_9)

        then:
        missionRepo.findMissionByName(MISSION_1)
                .orElseThrow()
                .getStatus() == MissionStatus.PENDING

    }

    def "should continue mission after all rockets are repaired"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON);
        def rockets = [FALCON_9, RED_DRAGON]
        controlCenter.assignRocketsToMission(rockets, MISSION_1)
        controlCenter.sendRocketToRepair(FALCON_9)

        when:
        controlCenter.releaseRocketFromRepair(FALCON_9)

        then:
        missionRepo.findMissionByName(MISSION_1)
                .orElseThrow()
                .getStatus() == MissionStatus.IN_PROGRESS
    }

    def "should end mission"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON);
        def rockets = [FALCON_9, RED_DRAGON]
        controlCenter.assignRocketsToMission(rockets, MISSION_1)

        when:
        controlCenter.endMission(MISSION_1)

        then:
        missionRepo.findMissionByName(MISSION_1)
                .orElseThrow()
                .getStatus() == MissionStatus.ENDED
    }

    def "should release all rockets in space after mission has ended"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON);
        def rockets = [FALCON_9, RED_DRAGON]
        controlCenter.assignRocketsToMission(rockets, MISSION_1)

        when:
        controlCenter.endMission(MISSION_1)

        then:
        def rocket1 = rocketRepo.findRocketByName(FALCON_9).orElseThrow()
        rocket1.getStatus() == RocketStatus.ON_GROUND
        rocket1.getMissionName() == null
        def rocket2 = rocketRepo.findRocketByName(RED_DRAGON).orElseThrow()
        rocket2.getStatus() == RocketStatus.ON_GROUND
        rocket2.getMissionName() == null
    }

    def "should keep a rocket in repair after mission has ended"() {
        given:
        controlCenter.scheduleMission(MISSION_1)
        controlCenter.addRocket(FALCON_9)
        controlCenter.addRocket(RED_DRAGON);
        def rockets = [FALCON_9, RED_DRAGON]
        controlCenter.assignRocketsToMission(rockets, MISSION_1)
        controlCenter.sendRocketToRepair(FALCON_9)

        when:
        controlCenter.endMission(MISSION_1)

        then:
        def rocket1 = rocketRepo.findRocketByName(FALCON_9).orElseThrow()
        rocket1.getStatus() == RocketStatus.IN_REPAIR
        rocket1.getMissionName() == null
        def rocket2 = rocketRepo.findRocketByName(RED_DRAGON).orElseThrow()
        rocket2.getStatus() == RocketStatus.ON_GROUND
        rocket2.getMissionName() == null
    }
}