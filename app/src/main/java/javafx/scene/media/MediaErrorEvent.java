package javafx.scene.media;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:javafx/scene/media/MediaErrorEvent.class */
public class MediaErrorEvent extends Event {
    private static final long serialVersionUID = 20121107;
    public static final EventType<MediaErrorEvent> MEDIA_ERROR = new EventType<>(Event.ANY, "Media Error Event");
    private MediaException error;

    MediaErrorEvent(Object source, EventTarget target, MediaException error) {
        super(source, target, MEDIA_ERROR);
        if (error == null) {
            throw new IllegalArgumentException("error == null!");
        }
        this.error = error;
    }

    public MediaException getMediaError() {
        return this.error;
    }

    @Override // java.util.EventObject
    public String toString() {
        return super.toString() + ": source " + getSource() + "; target " + ((Object) getTarget()) + "; error " + ((Object) this.error);
    }

    @Override // javafx.event.Event
    public MediaErrorEvent copyFor(Object newSource, EventTarget newTarget) {
        return (MediaErrorEvent) super.copyFor(newSource, newTarget);
    }

    @Override // javafx.event.Event
    public EventType<MediaErrorEvent> getEventType() {
        return super.getEventType();
    }
}
