package javax.net.ssl;

import java.util.EventObject;

/* loaded from: rt.jar:javax/net/ssl/SSLSessionBindingEvent.class */
public class SSLSessionBindingEvent extends EventObject {
    private static final long serialVersionUID = 3989172637106345L;
    private String name;

    public SSLSessionBindingEvent(SSLSession sSLSession, String str) {
        super(sSLSession);
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public SSLSession getSession() {
        return (SSLSession) getSource();
    }
}
