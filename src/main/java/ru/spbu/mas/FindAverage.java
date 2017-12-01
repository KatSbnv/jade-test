package ru.spbu.mas;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
public class FindAverage extends TickerBehaviour {
    private final DefaultAgent agent;
    private ArrayList<AID> linkedAgents;
    private ArrayList<Double> messages = new ArrayList<Double>();
    private double number;

    FindAverage(DefaultAgent agent) {
        super(agent, 1000);
        this.agent = agent;
        this.linkedAgents = agent.getLinkedAgents();
        this.number = agent.getNumber();
    }

    @Override
    public void onTick() {

        for (AID receiverAID : linkedAgents)
            sendMessage(receiverAID);

        sleepAgent();
        readMessages();
        checkTheEnd();
        updateState();

        if (getTickCount() % 10 == 0)
            System.out.println("Agent #" + agent.getAID().getLocalName() + "; Iteration #" + getTickCount());

    }

    private void sendMessage(AID receiverAID) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(receiverAID);
        msg.setContent(Double.toString(number));
        agent.send(msg);
    }

    private void readMessages() {
        ACLMessage msg;
        while ((msg = agent.receive()) != null) {
            double neighborNumber = Double.parseDouble(msg.getContent());
            messages.add(neighborNumber);
        }
    }

    private void updateState() {
        double alpha = 0.1;
        for (Double neighborNumber : messages) {
            // Let b is equal to 1
            number += alpha * (neighborNumber - number);
        }
        messages.clear();
    }

    private void checkTheEnd() {
        double epsilon = 0.01;
        int stopCriterion = 0;
        for (Double neighborNumber : messages) {
            if (Math.abs(number - neighborNumber) <= epsilon)
                stopCriterion++;
        }

        if (stopCriterion == messages.size()) {
            System.out.println("Agent #" + agent.getAID().getLocalName() + "; number: " + number);
            this.stop();
        }
    }

    private void sleepAgent() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
