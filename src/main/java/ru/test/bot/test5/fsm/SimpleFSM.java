package ru.test.bot.test5.fsm;


import akka.actor.AbstractFSM;
import ru.test.bot.test5.fsm.api.Data;
import ru.test.bot.test5.fsm.api.WaitingOff;
import ru.test.bot.test5.fsm.api.WaitingOn;
import ru.test.bot.test5.fsm.api.SimpleState;

public class SimpleFSM extends AbstractFSM<SimpleState, Data> {
    {
        // fsm body
        startWith(WaitingState.WAITING_STATE, new SomeData());

        // transitions

        // from Off to On
        when(WaitingState.WAITING_STATE,
                matchEvent(WaitingOn.class, SomeData.class,
                        (eventWaitingOn, uninitialized) ->
                                goTo(SearchState.SEARCH_STATE)
                                        .using(new SomeData())
                                        .replying(SearchState.SEARCH_STATE)
                )
        );

        // from On to Off
        when(SearchState.SEARCH_STATE,
                matchEvent(WaitingOff.class, SomeData.class,
                        (eventWaitingOff, uninitialized) ->
                                goTo(WaitingState.WAITING_STATE)
                                        .using(new SomeData())
                                        .replying(WaitingState.WAITING_STATE)
                )
        );

        onTermination(
                matchStop(Normal(),
                        (state, data) -> {
                            goTo(WaitingState.WAITING_STATE)
                                    .using(new SomeData());
                        }).
                        stop(Shutdown(),
                                (state, data) -> {
                                    goTo(WaitingState.WAITING_STATE)
                                            .using(new SomeData());
                                }).
                        stop(Failure.class,
                                (reason, state, data) -> {
                                    goTo(WaitingState.WAITING_STATE)
                                            .using(new SomeData());
                                })
        );

        initialize();

    }
}