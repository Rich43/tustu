package com.sun.javafx.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.event.Event;
import javafx.event.EventDispatcher;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:com/sun/javafx/event/EventRedirector.class */
public class EventRedirector extends BasicEventDispatcher {
    private final Object eventSource;
    private final List<EventDispatcher> eventDispatchers = new CopyOnWriteArrayList();
    private final EventDispatchChainImpl eventDispatchChain = new EventDispatchChainImpl();

    public EventRedirector(Object eventSource) {
        this.eventSource = eventSource;
    }

    protected void handleRedirectedEvent(Object eventSource, Event event) {
    }

    public final void addEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatchers.add(eventDispatcher);
    }

    public final void removeEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatchers.remove(eventDispatcher);
    }

    @Override // com.sun.javafx.event.BasicEventDispatcher
    public final Event dispatchCapturingEvent(Event event) {
        EventType<?> eventType = event.getEventType();
        if (eventType == DirectEvent.DIRECT) {
            event = ((DirectEvent) event).getOriginalEvent();
        } else {
            redirectEvent(event);
            if (eventType == RedirectedEvent.REDIRECTED) {
                handleRedirectedEvent(event.getSource(), ((RedirectedEvent) event).getOriginalEvent());
            }
        }
        return event;
    }

    private void redirectEvent(Event event) {
        if (!this.eventDispatchers.isEmpty()) {
            RedirectedEvent redirectedEvent = event.getEventType() == RedirectedEvent.REDIRECTED ? (RedirectedEvent) event : new RedirectedEvent(event, this.eventSource, null);
            for (EventDispatcher eventDispatcher : this.eventDispatchers) {
                this.eventDispatchChain.reset();
                eventDispatcher.dispatchEvent(redirectedEvent, this.eventDispatchChain);
            }
        }
    }
}
