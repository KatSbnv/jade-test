package ru.spbu.mas;

import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.Random;

public class DefaultAgent extends Agent {
    private ArrayList<AID> linkedAgents = new ArrayList<AID>();
    private double number;

    @Override
    protected void setup() {
        int id = Integer.parseInt(getAID().getLocalName());
        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            String[] neighbors = args[0].toString().split(", ");
            for (String neighbor : neighbors) {
                AID uid = new AID(neighbor, AID.ISLOCALNAME);
                linkedAgents.add(uid);
            }
        }

        int min = 5, max = 30;
        number = new Random().nextInt(max - min) + min;

        System.out.println("Agent #" + id + "; number: " + number + "; neighbors: " + linkedAgents.toString());

        addBehaviour(new FindAverage(this));
    }

    double getNumber() {
        return number;
    }

    ArrayList<AID> getLinkedAgents() {
        return linkedAgents;
    }
}
