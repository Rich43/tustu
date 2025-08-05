package javax.naming.event;

import java.util.EventObject;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/event/NamingExceptionEvent.class */
public class NamingExceptionEvent extends EventObject {
    private NamingException exception;
    private static final long serialVersionUID = -4877678086134736336L;

    public NamingExceptionEvent(EventContext eventContext, NamingException namingException) {
        super(eventContext);
        this.exception = namingException;
    }

    public NamingException getException() {
        return this.exception;
    }

    public EventContext getEventContext() {
        return (EventContext) getSource();
    }

    public void dispatch(NamingListener namingListener) {
        namingListener.namingExceptionThrown(this);
    }
}
