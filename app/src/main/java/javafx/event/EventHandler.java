package javafx.event;

import java.util.EventListener;
import javafx.event.Event;

@FunctionalInterface
/* loaded from: jfxrt.jar:javafx/event/EventHandler.class */
public interface EventHandler<T extends Event> extends EventListener {
    void handle(T t2);
}
