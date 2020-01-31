package ru.skholstinin.chatbot.testbot;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.fasterxml.jackson.core.JsonProcessingException;
import ru.skholstinin.chatbot.fsm.actors.ChatBotFSM;
import ru.skholstinin.chatbot.fsm.messages.MessageFromConsole;

import java.util.Scanner;

public class TestBot {
    public static void main(String[] args) throws JsonProcessingException {
        ActorSystem system = ActorSystem.create("AkkaBotSystem");
        final ActorRef chatBotFSM = system.actorOf(Props.create(ChatBotFSM.class));


        System.out.println("Input your question, please");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String inputStr = sc.nextLine();
            if (inputStr.equals("exit")) break;
            if (!(inputStr.equals("Y") || inputStr.equals("y"))) {
//                akkabot.tell(inputStr, ActorRef.noSender());
                chatBotFSM.tell(new MessageFromConsole(inputStr, chatBotFSM.path().name()), chatBotFSM);
            } else {
                System.out.println("Input your next question, please");
            }
        }
        system.terminate();


    }
}
