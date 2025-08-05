package javafx.event;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.event.Event;

/* loaded from: jfxrt.jar:javafx/event/WeakEventHandler.class */
public final class WeakEventHandler<T extends Event> implements EventHandler<T> {
    private final WeakReference<EventHandler<T>> weakRef;

    public WeakEventHandler(@NamedArg("eventHandler") EventHandler<T> eventHandler) {
        this.weakRef = new WeakReference<>(eventHandler);
    }

    public boolean wasGarbageCollected() {
        return this.weakRef.get() == null;
    }

    @Override // javafx.event.EventHandler
    public void handle(T event) {
        EventHandler<T> eventHandler = this.weakRef.get();
        if (eventHandler != null) {
            eventHandler.handle(event);
        }
    }

    void clear() {
        this.weakRef.clear();
    }
}
