package ru.skholstinin.chatbot.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestKit;
import org.junit.Before;
import org.junit.Test;
import ru.skholstinin.chatbot.fsm.actors.ChatBotFSM;
import ru.skholstinin.chatbot.fsm.messages.MessageFromConsole;
import ru.skholstinin.chatbot.fsm.messages.MessageToApi;
import ru.skholstinin.chatbot.fsm.state.ChatState;

public class TestChatBotFSM {

    @Test
    public void test() {
        ActorSystem system = ActorSystem.create("chatBotFSMTest");
        try {
            new TestKit(system) {
                {
                    ActorRef testBot = system.actorOf(Props.create(ChatBotFSM.class));
                    testBot.tell(
                            new MessageFromConsole("Got question"),
                            testActor()
                    );
                    expectMsgClass(MessageToApi.class);
                }
            };
        } finally {
            system.terminate();
        }
    }
}
