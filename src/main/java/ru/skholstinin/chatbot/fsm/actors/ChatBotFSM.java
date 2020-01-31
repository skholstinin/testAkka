package ru.skholstinin.chatbot.fsm.actors;

import akka.NotUsed;
import akka.actor.AbstractFSM;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.stream.Materializer;
import akka.util.ByteString;
import com.fasterxml.jackson.core.JsonProcessingException;
import ru.skholstinin.chatbot.fsm.state.ChatState;
import ru.skholstinin.chatbot.httpclient.AkkaTestClient;
import ru.skholstinin.chatbot.fsm.models.UninitializedData;
import ru.skholstinin.chatbot.fsm.messages.MessageFromConsole;
import ru.skholstinin.chatbot.fsm.messages.MessageToApi;
import ru.skholstinin.chatbot.fsm.messages.MessageToConsole;
import ru.skholstinin.chatbot.fsm.models.Data;

public class ChatBotFSM extends AbstractFSM<ChatState, Data> {
    private LoggingAdapter loger = Logging.getLogger(getContext().getSystem(), this);

    {
        startWith(ChatState.WAITING, UninitializedData.Uninitialized);

        when(ChatState.WAITING,
                matchEvent(MessageFromConsole.class, Data.class,
                        (msgObject, data) -> {
                            processMessageFromConsole(getSender(), msgObject.getMessage());
                            return goTo(ChatState.SEARCH).using(data);
                        }
                )
        );
        when(ChatState.SEARCH,
                matchEvent(MessageToApi.class, Data.class,
                        (msgObject, data) -> {
                            processMessageToApi(getSender(), msgObject.getMessage());
                            return goTo(ChatState.ANSWERING).using(data);
                        }

                )
        );

        when(ChatState.ANSWERING,
                matchEvent(MessageToConsole.class, Data.class,
                        (msgObject, data) -> {
                            processMessageToConsole(getSender(), msgObject.getMessage());
                            return goTo(ChatState.WAITING).using(data);
                        }
                )

        );
        initialize();
    }


    private void processMessageFromConsole(ActorRef sender, String message) {
        sender.tell(new MessageToApi(message), sender);
        sendRequestToApi(message);
        loger.info("processMessageFromConsole");
    }

    private void processMessageFromApi(ActorRef sender, String message) {
        loger.info("processMessageFromApi");
    }

    private void processMessageToConsole(ActorRef sender, String message) {
        System.out.println(message);//TODO попробовать заменить вывод в конслоь на сообщение другому актору, в котором будет вывод в консоль
        loger.info("processMessageToConsole");
    }

    private void processMessageToApi(ActorRef sender, String message) {
        sender.tell(new MessageToConsole(message, sender.path().name()), sender);
        loger.info("processMessageToApi");
    }

    private void sendRequestToApi(String message) {
        ActorSystem actorSystem = ActorSystem.create("client");
        AkkaTestClient client = new AkkaTestClient(actorSystem, Materializer.createMaterializer(actorSystem));
        try {
            client.apiRequest(message, success -> success.entity().
                    getDataBytes().
                    runFold(ByteString.emptyByteString(), ByteString::concat, client.getMaterializer())
                    .handle((byteString, f) -> {
                        if (f == null) {
                            System.out.println(byteString.utf8String());
                            System.out.println("Ask next question? Y/n [Y]");//TODO попробовать заменить вывод в конслоь на сообщение другому актору, в котором будет вывод в консоль
                        } else {
                            System.out.println("Error: " + byteString.utf8String());
                        }
                        return NotUsed.getInstance();
                    })
            ).whenComplete((success, throwable) -> client.close());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
