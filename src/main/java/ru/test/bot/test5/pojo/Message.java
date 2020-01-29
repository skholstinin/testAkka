package ru.test.bot.test5.pojo;


import java.util.ArrayList;
import java.util.List;

public class Message {
    private List<String> context = new ArrayList<>();

    public Message() {
    }

    public List<String> getContext() {
        return context;
    }

    public void addMessage(String message) {
        context.add(message);
    }

    public void setContext(List<String> context) {
        this.context = context;
    }
}
