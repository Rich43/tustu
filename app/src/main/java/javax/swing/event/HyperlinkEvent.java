package javax.swing.event;

import java.awt.event.InputEvent;
import java.net.URL;
import java.util.EventObject;
import javax.swing.text.Element;

/* loaded from: rt.jar:javax/swing/event/HyperlinkEvent.class */
public class HyperlinkEvent extends EventObject {
    private EventType type;

    /* renamed from: u, reason: collision with root package name */
    private URL f12817u;
    private String desc;
    private Element sourceElement;
    private InputEvent inputEvent;

    public HyperlinkEvent(Object obj, EventType eventType, URL url) {
        this(obj, eventType, url, null);
    }

    public HyperlinkEvent(Object obj, EventType eventType, URL url, String str) {
        this(obj, eventType, url, str, null);
    }

    public HyperlinkEvent(Object obj, EventType eventType, URL url, String str, Element element) {
        super(obj);
        this.type = eventType;
        this.f12817u = url;
        this.desc = str;
        this.sourceElement = element;
    }

    public HyperlinkEvent(Object obj, EventType eventType, URL url, String str, Element element, InputEvent inputEvent) {
        super(obj);
        this.type = eventType;
        this.f12817u = url;
        this.desc = str;
        this.sourceElement = element;
        this.inputEvent = inputEvent;
    }

    public EventType getEventType() {
        return this.type;
    }

    public String getDescription() {
        return this.desc;
    }

    public URL getURL() {
        return this.f12817u;
    }

    public Element getSourceElement() {
        return this.sourceElement;
    }

    public InputEvent getInputEvent() {
        return this.inputEvent;
    }

    /* loaded from: rt.jar:javax/swing/event/HyperlinkEvent$EventType.class */
    public static final class EventType {
        public static final EventType ENTERED = new EventType("ENTERED");
        public static final EventType EXITED = new EventType("EXITED");
        public static final EventType ACTIVATED = new EventType("ACTIVATED");
        private String typeString;

        private EventType(String str) {
            this.typeString = str;
        }

        public String toString() {
            return this.typeString;
        }
    }
}
