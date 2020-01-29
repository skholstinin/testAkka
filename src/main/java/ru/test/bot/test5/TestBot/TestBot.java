package ru.test.bot.test5.TestBot;

import akka.actor.ActorSystem;
import akka.stream.Materializer;
import ru.test.bot.test5.HttpClient.AkkaTestClient;

public class TestBot {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("client");

        AkkaTestClient client = new AkkaTestClient(actorSystem, Materializer.createMaterializer(actorSystem));

        if (args.length == 0) {
            System.err.println("Enter your question please!");
            System.exit(0);
        }
    }
}
