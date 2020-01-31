package ru.skholstinin.chatbot.httpclient;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.*;
import akka.stream.Materializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.skholstinin.chatbot.pojo.Message;
import ru.skholstinin.chatbot.settings.Settings;
import ru.skholstinin.chatbot.settings.SettingsImpl;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class AkkaTestClient {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final ActorSystem system;
    private final Materializer materializer;
    private final SettingsImpl settings;

    public AkkaTestClient(ActorSystem sys, Materializer mat) {
        system = sys;
        materializer = mat;
        settings = Settings.SettingsProvider.get(system);
    }

    public Materializer getMaterializer() {
        return materializer;
    }

    public void close() {
        Http.get(system).shutdownAllConnectionPools().whenComplete((s, f) -> system.terminate());
    }

    private String getApiUri() {
        return settings.URI;
    }

    public <U> CompletionStage<U> apiRequest(String message, Function<HttpResponse, CompletionStage<U>> responseHandler) throws JsonProcessingException {
        Message body = new Message();
        body.addMessage(message);
        return Http.get(system)
                .singleRequest(HttpRequest.POST(getApiUri())
                        .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, objectMapper.writeValueAsString(body))), materializer)
                .thenComposeAsync(responseHandler);

    }

}
