package javafx.event;

/* loaded from: jfxrt.jar:javafx/event/EventDispatchChain.class */
public interface EventDispatchChain {
    EventDispatchChain append(EventDispatcher eventDispatcher);

    EventDispatchChain prepend(EventDispatcher eventDispatcher);

    Event dispatchEvent(Event event);
}
