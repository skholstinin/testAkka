package ru.skholstinin.chatbot.fsm.models;

import akka.actor.ActorRef;

public final class ConsoleData implements Data {

    private ActorRef sender;

    public ConsoleData(ActorRef sender) {
        this.sender = sender;
    }

    public ActorRef getSender() {
        return sender;
    }
}
