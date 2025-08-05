package javax.naming.event;

/* loaded from: rt.jar:javax/naming/event/NamespaceChangeListener.class */
public interface NamespaceChangeListener extends NamingListener {
    void objectAdded(NamingEvent namingEvent);

    void objectRemoved(NamingEvent namingEvent);

    void objectRenamed(NamingEvent namingEvent);
}
