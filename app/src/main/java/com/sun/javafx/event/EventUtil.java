package com.sun.javafx.event;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;

/* loaded from: jfxrt.jar:com/sun/javafx/event/EventUtil.class */
public final class EventUtil {
    private static final EventDispatchChainImpl eventDispatchChain = new EventDispatchChainImpl();
    private static final AtomicBoolean eventDispatchChainInUse = new AtomicBoolean();

    public static Event fireEvent(EventTarget eventTarget, Event event) {
        if (event.getTarget() != eventTarget) {
            event = event.copyFor(event.getSource(), eventTarget);
        }
        if (eventDispatchChainInUse.getAndSet(true)) {
            return fireEventImpl(new EventDispatchChainImpl(), eventTarget, event);
        }
        try {
            Event eventFireEventImpl = fireEventImpl(eventDispatchChain, eventTarget, event);
            eventDispatchChain.reset();
            eventDispatchChainInUse.set(false);
            return eventFireEventImpl;
        } catch (Throwable th) {
            eventDispatchChain.reset();
            eventDispatchChainInUse.set(false);
            throw th;
        }
    }

    public static Event fireEvent(Event event, EventTarget... eventTargets) {
        return fireEventImpl(new EventDispatchTreeImpl(), new CompositeEventTargetImpl(eventTargets), event);
    }

    private static Event fireEventImpl(EventDispatchChain eventDispatchChain2, EventTarget eventTarget, Event event) {
        EventDispatchChain targetDispatchChain = eventTarget.buildEventDispatchChain(eventDispatchChain2);
        return targetDispatchChain.dispatchEvent(event);
    }
}
