package javax.management;

import java.util.Vector;

/* loaded from: rt.jar:javax/management/AttributeChangeNotificationFilter.class */
public class AttributeChangeNotificationFilter implements NotificationFilter {
    private static final long serialVersionUID = -6347317584796410029L;
    private Vector<String> enabledAttributes = new Vector<>();

    @Override // javax.management.NotificationFilter
    public synchronized boolean isNotificationEnabled(Notification notification) {
        String type = notification.getType();
        if (type == null || !type.equals(AttributeChangeNotification.ATTRIBUTE_CHANGE) || !(notification instanceof AttributeChangeNotification)) {
            return false;
        }
        return this.enabledAttributes.contains(((AttributeChangeNotification) notification).getAttributeName());
    }

    public synchronized void enableAttribute(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("The name cannot be null.");
        }
        if (!this.enabledAttributes.contains(str)) {
            this.enabledAttributes.addElement(str);
        }
    }

    public synchronized void disableAttribute(String str) {
        this.enabledAttributes.removeElement(str);
    }

    public synchronized void disableAllAttributes() {
        this.enabledAttributes.removeAllElements();
    }

    public synchronized Vector<String> getEnabledAttributes() {
        return this.enabledAttributes;
    }
}
