package ru.test.bot.test5.TestBot;

import akka.actor.AbstractActor;
import akka.actor.UntypedAbstractActor;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AkkaBot extends UntypedAbstractActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public void onReceive(Object message) throws Throwable, Throwable {
        if (message instanceof String) {
            String str = (String) message;
            if (str.equals("Y") || str.equals("y")) {

            }
            logger.info("AkkBot have receive message \"{}\"", str);
            System.out.println("Ask next question? Y/n [Y]");
            getSender().tell("Ask next question? Y/n [Y]", this.sender());
        }
    }

    private void onPostStop() {
        System.out.println("actor stopped");
    }
//    @Override
//    public Receive createReceive() {
//        return receiveBuilder()
//                .match(
//                        String.class,
//                        s -> {
//                            log.info("Received String message: {}", s);
//                        })
//                .matchAny(o -> log.info("received unknown message"))
//                .build();
//    }
}
