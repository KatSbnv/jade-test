package ru.spbu.mas;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.HashMap;

public class MainController {
    private static final int numberOfAgents = 5;
    void initAgents() {
// Retrieve the singleton instance of the JADE Runtime
        Runtime rt = Runtime.instance();

        //Create a container to host the Default Agent
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        //p.setParameter(Profile.MAIN_PORT, "10098");
        p.setParameter(Profile.GUI, "false");
        ContainerController cc = rt.createMainContainer(p);

        HashMap<Integer, String> neighbors = new HashMap <Integer, String>();
        neighbors.put(1, "2, 4, 3");
        neighbors.put(2, "1, 3");
        neighbors.put(3, "1, 2, 4");
        neighbors.put(4, "1, 3, 5");
        neighbors.put(5,  "4");

//Create a container to host the Default Agent
        try {
            for (int i = 1; i <= MainController.numberOfAgents; i++) {
                AgentController agent = cc.createNewAgent(Integer.toString(i), "ru.spbu.mas.DefaultAgent",
                        new Object[]{neighbors.get(i)});
                agent.start();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

}
