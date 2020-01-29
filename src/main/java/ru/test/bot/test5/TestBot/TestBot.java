package ru.test.bot.test5.TestBot;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.util.ByteString;
import com.fasterxml.jackson.core.JsonProcessingException;
import ru.test.bot.test5.HttpClient.AkkaTestClient;

import java.util.Optional;
import java.util.Scanner;

public class TestBot {
    public static void main(String[] args) throws JsonProcessingException {
        ActorSystem actorSystem = ActorSystem.create("client");

        AkkaTestClient client = new AkkaTestClient(actorSystem, Materializer.createMaterializer(actorSystem));
        Scanner in = new Scanner(System.in);
        System.out.println("Введите ваш вопрос");
        String sentence = in.nextLine();
        System.out.print("Выш вопрос: " + sentence);

        client.apiRequest(sentence, success -> success.entity().
                getDataBytes().
                runFold(ByteString.emptyByteString(), ByteString::concat, client.getMaterializer())
                .handle((byteString, f) -> {
                    if (f == null) {
                        System.out.println("Result: " + byteString.utf8String());
                    } else {
                        System.out.println("Error: " + byteString.utf8String());
                    }
                    return NotUsed.getInstance();
                })
        ).whenComplete((success, throwable) -> client.close());
    }
}
