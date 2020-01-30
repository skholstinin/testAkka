package ru.skholstinin.chatbot.fsm.messages;


public class MessageFromApi {
    private final String message;
    private final String sender;

    public MessageFromApi(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public MessageFromApi(String message) {
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
