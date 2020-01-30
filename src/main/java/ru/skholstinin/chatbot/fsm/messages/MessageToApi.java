package ru.skholstinin.chatbot.fsm.messages;

public class MessageToApi {
    private final String message;
    private final String sender;

    public MessageToApi(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public MessageToApi(String message) {
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
