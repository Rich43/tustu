package javafx.scene.web;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:javafx/scene/web/WebEvent.class */
public final class WebEvent<T> extends Event {
    public static final EventType<WebEvent> ANY = new EventType<>(Event.ANY, "WEB");
    public static final EventType<WebEvent> RESIZED = new EventType<>(ANY, "WEB_RESIZED");
    public static final EventType<WebEvent> STATUS_CHANGED = new EventType<>(ANY, "WEB_STATUS_CHANGED");
    public static final EventType<WebEvent> VISIBILITY_CHANGED = new EventType<>(ANY, "WEB_VISIBILITY_CHANGED");
    public static final EventType<WebEvent> ALERT = new EventType<>(ANY, "WEB_ALERT");
    private final T data;

    public WebEvent(@NamedArg("source") Object source, @NamedArg("type") EventType<WebEvent> type, @NamedArg("data") T data) {
        super(source, null, type);
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    @Override // java.util.EventObject
    public String toString() {
        return String.format("WebEvent [source = %s, eventType = %s, data = %s]", getSource(), getEventType(), getData());
    }
}
