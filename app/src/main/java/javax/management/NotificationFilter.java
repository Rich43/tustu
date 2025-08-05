package javax.management;

import java.io.Serializable;

/* loaded from: rt.jar:javax/management/NotificationFilter.class */
public interface NotificationFilter extends Serializable {
    boolean isNotificationEnabled(Notification notification);
}
