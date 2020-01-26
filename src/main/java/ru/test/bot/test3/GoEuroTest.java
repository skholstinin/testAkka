package ru.test.bot.test3;

import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.stream.ActorMaterializer;
import ru.test.bot.test3.helpers.CsvHelper;
import ru.test.bot.test3.models.City;

import java.util.Arrays;
import java.util.Optional;

public class GoEuroTest {

    public static void main(String[] args) {

        ActorSystem actorSystem = ActorSystem.create("client");

        AkkaHttpJavaClient client = new AkkaHttpJavaClient(actorSystem, ActorMaterializer.create(actorSystem));

        if (args.length == 0) {
            System.err.println("Enter a city name!");
            System.exit(0);
        } else {
            String cityName = args[0];
            System.out.println(cityName);
            if (cityName != null && !cityName.isEmpty()) {
                client.requestLevelFutureBased(Optional.of(cityName), success ->
                        Jackson.unmarshaller(City[].class)
                                .unmarshall(success.entity(),
                                        client.getSystem().dispatcher(),
                                        client.getMaterializer())
                                .thenApply(Arrays::asList))
                        .thenAccept(cities -> new CsvHelper().writeToCSV(cities, cityName))
                        .whenComplete((success, throwable) -> client.close());
            } else {
                System.err.println("Enter a city name!");
                System.exit(0);
            }
        }
    }

}
