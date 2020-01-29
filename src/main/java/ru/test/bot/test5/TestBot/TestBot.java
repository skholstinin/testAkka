package ru.test.bot.test5.TestBot;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.stream.Materializer;
import akka.util.ByteString;
import com.fasterxml.jackson.core.JsonProcessingException;
import ru.test.bot.test5.HttpClient.AkkaTestClient;

import java.awt.image.Kernel;
import java.util.Optional;
import java.util.Scanner;

public class TestBot {
    public static void main(String[] args) throws JsonProcessingException {
        ActorSystem system = ActorSystem.create("AkkaBotSystem");
        final ActorRef akkabotConsoleReceiver = system.actorOf(Props.create(AkkaBot.class), "akkabotConsoleReceiver");
        final ActorRef akkabotConsoleSender = system.actorOf(Props.create(AkkaBot.class), "akkabotConsoleSender");

        System.out.println("Введите ваш вопрос");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String inputStr = sc.nextLine();
            if (inputStr.equals("exit")) break;
            akkabotConsoleSender.tell(inputStr, akkabotConsoleReceiver);
        }
        system.terminate();



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
