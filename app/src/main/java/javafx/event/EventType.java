package javafx.event;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import javafx.event.Event;

/* loaded from: jfxrt.jar:javafx/event/EventType.class */
public final class EventType<T extends Event> implements Serializable {
    public static final EventType<Event> ROOT = new EventType<>("EVENT", (EventType) null);
    private WeakHashMap<EventType<? extends T>, Void> subTypes;
    private final EventType<? super T> superType;
    private final String name;

    @Deprecated
    public EventType() {
        this(ROOT, (String) null);
    }

    public EventType(String name) {
        this(ROOT, name);
    }

    public EventType(EventType<? super T> superType) {
        this(superType, (String) null);
    }

    public EventType(EventType<? super T> superType, String name) {
        if (superType == null) {
            throw new NullPointerException("Event super type must not be null!");
        }
        this.superType = superType;
        this.name = name;
        superType.register(this);
    }

    EventType(String name, EventType<? super T> superType) {
        this.superType = superType;
        this.name = name;
        if (superType != null) {
            if (superType.subTypes != null) {
                Iterator i2 = superType.subTypes.keySet().iterator();
                while (i2.hasNext()) {
                    EventType<? extends T> t2 = i2.next();
                    if ((name == null && t2.name == null) || (name != null && name.equals(t2.name))) {
                        i2.remove();
                    }
                }
            }
            superType.register(this);
        }
    }

    public final EventType<? super T> getSuperType() {
        return this.superType;
    }

    public final String getName() {
        return this.name;
    }

    public String toString() {
        return this.name != null ? this.name : super.toString();
    }

    private void register(EventType<? extends T> subType) {
        if (this.subTypes == null) {
            this.subTypes = new WeakHashMap<>();
        }
        for (EventType<? extends T> t2 : this.subTypes.keySet()) {
            if ((t2.name == null && subType.name == null) || (t2.name != null && t2.name.equals(subType.name))) {
                throw new IllegalArgumentException("EventType \"" + ((Object) subType) + "\"with parent \"" + ((Object) subType.getSuperType()) + "\" already exists");
            }
        }
        this.subTypes.put(subType, null);
    }

    private Object writeReplace() throws ObjectStreamException {
        Deque<String> path = new LinkedList<>();
        EventType eventType = this;
        while (true) {
            EventType eventType2 = eventType;
            if (eventType2 != ROOT) {
                path.addFirst(eventType2.name);
                eventType = eventType2.superType;
            } else {
                return new EventTypeSerialization(new ArrayList(path));
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/event/EventType$EventTypeSerialization.class */
    static class EventTypeSerialization implements Serializable {
        private List<String> path;

        public EventTypeSerialization(List<String> path) {
            this.path = path;
        }

        private Object readResolve() throws ObjectStreamException {
            EventType t2 = EventType.ROOT;
            for (int i2 = 0; i2 < this.path.size(); i2++) {
                String p2 = this.path.get(i2);
                if (t2.subTypes != null) {
                    EventType s2 = findSubType(t2.subTypes.keySet(), p2);
                    if (s2 == null) {
                        throw new InvalidObjectException("Cannot find event type \"" + p2 + "\" (of " + ((Object) t2) + ")");
                    }
                    t2 = s2;
                } else {
                    throw new InvalidObjectException("Cannot find event type \"" + p2 + "\" (of " + ((Object) t2) + ")");
                }
            }
            return t2;
        }

        private EventType findSubType(Set<EventType> subTypes, String name) {
            for (EventType t2 : subTypes) {
                if ((t2.name == null && name == null) || (t2.name != null && t2.name.equals(name))) {
                    return t2;
                }
            }
            return null;
        }
    }
}
