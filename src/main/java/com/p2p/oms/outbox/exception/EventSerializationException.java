package com.p2p.oms.outbox.exception;

import java.util.function.Supplier;

public class EventSerializationException extends RuntimeException {

    public EventSerializationException(String message) {
        super("EVENT_SERIALIZATION_FAILED" + message);
    }

    public static Supplier<EventSerializationException> of(String message) {
        return () -> new EventSerializationException(message);
    }

}