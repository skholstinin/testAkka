package ru.skholstinin.chatbot.fsm.messages;

public class MessageToConsole {
    private final String message;
    private final String sender;

    public MessageToConsole(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
