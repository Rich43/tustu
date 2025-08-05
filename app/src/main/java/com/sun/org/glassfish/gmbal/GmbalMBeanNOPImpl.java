package com.sun.org.glassfish.gmbal;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ReflectionException;

/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/GmbalMBeanNOPImpl.class */
public class GmbalMBeanNOPImpl implements GmbalMBean {
    @Override // javax.management.DynamicMBean
    public Object getAttribute(String attribute) throws MBeanException, AttributeNotFoundException, ReflectionException {
        return null;
    }

    @Override // javax.management.DynamicMBean
    public void setAttribute(Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, ReflectionException {
    }

    @Override // javax.management.DynamicMBean
    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    @Override // javax.management.DynamicMBean
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    @Override // javax.management.DynamicMBean
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        return null;
    }

    @Override // javax.management.DynamicMBean
    public MBeanInfo getMBeanInfo() {
        return null;
    }

    @Override // javax.management.NotificationEmitter
    public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws ListenerNotFoundException {
    }

    @Override // javax.management.NotificationBroadcaster
    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
    }

    @Override // javax.management.NotificationBroadcaster
    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
    }

    @Override // javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[0];
    }
}
