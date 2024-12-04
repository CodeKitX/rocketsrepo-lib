package com.six.rocketsrepo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ControlCenter controlCenter = context.getBean(ControlCenter.class);


        // Adding rockets
        controlCenter.addRocket("Dragon 1");
        controlCenter.addRocket("Dragon 2");
        controlCenter.addRocket("Red Dragon");
        controlCenter.addRocket("Dragon XL");
        controlCenter.addRocket("Falcon Heavy");


        // Adding missions
        controlCenter.scheduleMission("Mars");
        controlCenter.scheduleMission("Luna1");
        controlCenter.scheduleMission("Double Landing");
        controlCenter.scheduleMission("Transit");
        controlCenter.scheduleMission("Luna2");
        controlCenter.scheduleMission("Vertical Landing");

        // Assigning rockets to missions
        controlCenter.assignRocketToMission("Dragon 1", "Luna1");
        controlCenter.assignRocketToMission("Dragon 2", "Luna1");
        controlCenter.assignRocketToMission("Red Dragon", "Transit");
        controlCenter.assignRocketToMission("Dragon XL", "Transit");
        controlCenter.assignRocketToMission("Falcon Heavy", "Transit");


        controlCenter.endMission("Vertical Landing");
        controlCenter.endMission("Double Landing");
        controlCenter.sendRocketToRepair("Dragon 1");

        // Generating mission status report
        controlCenter.missionStatusReport();
    }
}

