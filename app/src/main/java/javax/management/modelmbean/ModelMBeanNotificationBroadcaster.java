package javax.management.modelmbean;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationListener;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:javax/management/modelmbean/ModelMBeanNotificationBroadcaster.class */
public interface ModelMBeanNotificationBroadcaster extends NotificationBroadcaster {
    void sendNotification(Notification notification) throws MBeanException, RuntimeOperationsException;

    void sendNotification(String str) throws MBeanException, RuntimeOperationsException;

    void sendAttributeChangeNotification(AttributeChangeNotification attributeChangeNotification) throws MBeanException, RuntimeOperationsException;

    void sendAttributeChangeNotification(Attribute attribute, Attribute attribute2) throws MBeanException, RuntimeOperationsException;

    void addAttributeChangeNotificationListener(NotificationListener notificationListener, String str, Object obj) throws MBeanException, IllegalArgumentException, RuntimeOperationsException;

    void removeAttributeChangeNotificationListener(NotificationListener notificationListener, String str) throws MBeanException, ListenerNotFoundException, RuntimeOperationsException;
}
