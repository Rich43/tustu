package javax.naming.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/naming/event/NamingListener.class */
public interface NamingListener extends EventListener {
    void namingExceptionThrown(NamingExceptionEvent namingExceptionEvent);
}
