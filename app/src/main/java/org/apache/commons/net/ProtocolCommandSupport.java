package org.apache.commons.net;

import java.io.Serializable;
import java.util.EventListener;
import java.util.Iterator;
import org.apache.commons.net.util.ListenerList;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ProtocolCommandSupport.class */
public class ProtocolCommandSupport implements Serializable {
    private static final long serialVersionUID = -8017692739988399978L;
    private final Object __source;
    private final ListenerList __listeners = new ListenerList();

    public ProtocolCommandSupport(Object source) {
        this.__source = source;
    }

    public void fireCommandSent(String command, String message) {
        ProtocolCommandEvent event = new ProtocolCommandEvent(this.__source, command, message);
        Iterator i$ = this.__listeners.iterator();
        while (i$.hasNext()) {
            EventListener listener = i$.next();
            ((ProtocolCommandListener) listener).protocolCommandSent(event);
        }
    }

    public void fireReplyReceived(int replyCode, String message) {
        ProtocolCommandEvent event = new ProtocolCommandEvent(this.__source, replyCode, message);
        Iterator i$ = this.__listeners.iterator();
        while (i$.hasNext()) {
            EventListener listener = i$.next();
            ((ProtocolCommandListener) listener).protocolReplyReceived(event);
        }
    }

    public void addProtocolCommandListener(ProtocolCommandListener listener) {
        this.__listeners.addListener(listener);
    }

    public void removeProtocolCommandListener(ProtocolCommandListener listener) {
        this.__listeners.removeListener(listener);
    }

    public int getListenerCount() {
        return this.__listeners.getListenerCount();
    }
}
