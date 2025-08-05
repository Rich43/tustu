package javax.naming.ldap;

import java.util.EventObject;

/* loaded from: rt.jar:javax/naming/ldap/UnsolicitedNotificationEvent.class */
public class UnsolicitedNotificationEvent extends EventObject {
    private UnsolicitedNotification notice;
    private static final long serialVersionUID = -2382603380799883705L;

    public UnsolicitedNotificationEvent(Object obj, UnsolicitedNotification unsolicitedNotification) {
        super(obj);
        this.notice = unsolicitedNotification;
    }

    public UnsolicitedNotification getNotification() {
        return this.notice;
    }

    public void dispatch(UnsolicitedNotificationListener unsolicitedNotificationListener) {
        unsolicitedNotificationListener.notificationReceived(this);
    }
}
