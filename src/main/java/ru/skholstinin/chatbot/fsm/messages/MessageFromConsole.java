package ru.skholstinin.chatbot.fsm.messages;

public class MessageFromConsole {
    private final String message;
    private final String sender;

    public MessageFromConsole(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public MessageFromConsole(String message) {
        this.message = message;
        this.sender = "";
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
