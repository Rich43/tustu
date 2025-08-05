package javax.management;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* loaded from: rt.jar:javax/management/NotificationFilterSupport.class */
public class NotificationFilterSupport implements NotificationFilter {
    private static final long serialVersionUID = 6579080007561786969L;
    private List<String> enabledTypes = new Vector();

    @Override // javax.management.NotificationFilter
    public synchronized boolean isNotificationEnabled(Notification notification) {
        String type = notification.getType();
        if (type == null) {
            return false;
        }
        try {
            Iterator<String> it = this.enabledTypes.iterator();
            while (it.hasNext()) {
                if (type.startsWith(it.next())) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public synchronized void enableType(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("The prefix cannot be null.");
        }
        if (!this.enabledTypes.contains(str)) {
            this.enabledTypes.add(str);
        }
    }

    public synchronized void disableType(String str) {
        this.enabledTypes.remove(str);
    }

    public synchronized void disableAllTypes() {
        this.enabledTypes.clear();
    }

    public synchronized Vector<String> getEnabledTypes() {
        return (Vector) this.enabledTypes;
    }
}
