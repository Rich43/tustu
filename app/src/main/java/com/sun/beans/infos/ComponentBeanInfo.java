package com.sun.beans.infos;

import java.awt.Component;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import jdk.jfr.Enabled;

/* loaded from: rt.jar:com/sun/beans/infos/ComponentBeanInfo.class */
public class ComponentBeanInfo extends SimpleBeanInfo {
    private static final Class<Component> beanClass = Component.class;

    @Override // java.beans.SimpleBeanInfo, java.beans.BeanInfo
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor("name", beanClass);
            PropertyDescriptor propertyDescriptor2 = new PropertyDescriptor("background", beanClass);
            PropertyDescriptor propertyDescriptor3 = new PropertyDescriptor("foreground", beanClass);
            PropertyDescriptor propertyDescriptor4 = new PropertyDescriptor("font", beanClass);
            PropertyDescriptor propertyDescriptor5 = new PropertyDescriptor(Enabled.NAME, beanClass);
            PropertyDescriptor propertyDescriptor6 = new PropertyDescriptor("visible", beanClass);
            PropertyDescriptor propertyDescriptor7 = new PropertyDescriptor("focusable", beanClass);
            propertyDescriptor5.setExpert(true);
            propertyDescriptor6.setHidden(true);
            propertyDescriptor2.setBound(true);
            propertyDescriptor3.setBound(true);
            propertyDescriptor4.setBound(true);
            propertyDescriptor7.setBound(true);
            return new PropertyDescriptor[]{propertyDescriptor, propertyDescriptor2, propertyDescriptor3, propertyDescriptor4, propertyDescriptor5, propertyDescriptor6, propertyDescriptor7};
        } catch (IntrospectionException e2) {
            throw new Error(e2.toString());
        }
    }
}
