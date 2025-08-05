package javax.swing.event;

import javax.swing.text.Document;
import javax.swing.text.Element;

/* loaded from: rt.jar:javax/swing/event/DocumentEvent.class */
public interface DocumentEvent {

    /* loaded from: rt.jar:javax/swing/event/DocumentEvent$ElementChange.class */
    public interface ElementChange {
        Element getElement();

        int getIndex();

        Element[] getChildrenRemoved();

        Element[] getChildrenAdded();
    }

    int getOffset();

    int getLength();

    Document getDocument();

    EventType getType();

    ElementChange getChange(Element element);

    /* loaded from: rt.jar:javax/swing/event/DocumentEvent$EventType.class */
    public static final class EventType {
        public static final EventType INSERT = new EventType("INSERT");
        public static final EventType REMOVE = new EventType("REMOVE");
        public static final EventType CHANGE = new EventType("CHANGE");
        private String typeString;

        private EventType(String str) {
            this.typeString = str;
        }

        public String toString() {
            return this.typeString;
        }
    }
}
