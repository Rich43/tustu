package javax.naming.ldap;

import javax.naming.event.NamingListener;

/* loaded from: rt.jar:javax/naming/ldap/UnsolicitedNotificationListener.class */
public interface UnsolicitedNotificationListener extends NamingListener {
    void notificationReceived(UnsolicitedNotificationEvent unsolicitedNotificationEvent);
}
