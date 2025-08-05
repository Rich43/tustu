package javax.management;

import java.util.EventListener;

/* loaded from: rt.jar:javax/management/NotificationListener.class */
public interface NotificationListener extends EventListener {
    void handleNotification(Notification notification, Object obj);
}
