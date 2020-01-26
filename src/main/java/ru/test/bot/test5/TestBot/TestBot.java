package ru.test.bot.test5.TestBot;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import ru.test.bot.test5.HttpClient.AkkaTestClient;

public class TestBot {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("client");

        AkkaTestClient client = new AkkaTestClient(actorSystem, ActorMaterializer.create(actorSystem));

        if (args.length == 0) {
            System.err.println("Enter your question please!");
            System.exit(0);
        }
    }
}
