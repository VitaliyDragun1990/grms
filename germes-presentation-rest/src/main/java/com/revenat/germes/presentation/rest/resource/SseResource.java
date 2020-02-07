package com.revenat.germes.presentation.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Demonstrating server side event (SSE)
 *
 * @author Vitaliy Dragun
 */
@Path("sse")
@Singleton
public class SseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SseResource.class);

    private final AtomicBoolean terminationFlag = new AtomicBoolean();

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("time")
    public void currentTime(@Context final SseEventSink eventSink, @Context final Sse sse) {
        new Thread(() -> {
           while (!terminationFlag.get()) {
               final OutboundSseEvent sseEvent = sse.newEventBuilder()
                       .data(String.class, LocalTime.now().toString())
                       .build();
               eventSink.send(sseEvent);
               try {
                   Thread.sleep(1000);
               } catch (final InterruptedException e) {
                   LOGGER.warn(e.getMessage());
                   terminationFlag.set(true);
                   return;
               }
           }
           eventSink.close();
        }).start();
    }

    @PreDestroy
    void terminate() {
        terminationFlag.set(true);
    }
}
