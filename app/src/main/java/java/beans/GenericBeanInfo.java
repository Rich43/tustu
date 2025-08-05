package java.beans;

import java.awt.Image;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/* compiled from: Introspector.java */
/* loaded from: rt.jar:java/beans/GenericBeanInfo.class */
class GenericBeanInfo extends SimpleBeanInfo {
    private BeanDescriptor beanDescriptor;
    private EventSetDescriptor[] events;
    private int defaultEvent;
    private PropertyDescriptor[] properties;
    private int defaultProperty;
    private MethodDescriptor[] methods;
    private Reference<BeanInfo> targetBeanInfoRef;

    public GenericBeanInfo(BeanDescriptor beanDescriptor, EventSetDescriptor[] eventSetDescriptorArr, int i2, PropertyDescriptor[] propertyDescriptorArr, int i3, MethodDescriptor[] methodDescriptorArr, BeanInfo beanInfo) {
        this.beanDescriptor = beanDescriptor;
        this.events = eventSetDescriptorArr;
        this.defaultEvent = i2;
        this.properties = propertyDescriptorArr;
        this.defaultProperty = i3;
        this.methods = methodDescriptorArr;
        this.targetBeanInfoRef = beanInfo != null ? new SoftReference(beanInfo) : null;
    }

    GenericBeanInfo(GenericBeanInfo genericBeanInfo) {
        this.beanDescriptor = new BeanDescriptor(genericBeanInfo.beanDescriptor);
        if (genericBeanInfo.events != null) {
            int length = genericBeanInfo.events.length;
            this.events = new EventSetDescriptor[length];
            for (int i2 = 0; i2 < length; i2++) {
                this.events[i2] = new EventSetDescriptor(genericBeanInfo.events[i2]);
            }
        }
        this.defaultEvent = genericBeanInfo.defaultEvent;
        if (genericBeanInfo.properties != null) {
            int length2 = genericBeanInfo.properties.length;
            this.properties = new PropertyDescriptor[length2];
            for (int i3 = 0; i3 < length2; i3++) {
                PropertyDescriptor propertyDescriptor = genericBeanInfo.properties[i3];
                if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
                    this.properties[i3] = new IndexedPropertyDescriptor((IndexedPropertyDescriptor) propertyDescriptor);
                } else {
                    this.properties[i3] = new PropertyDescriptor(propertyDescriptor);
                }
            }
        }
        this.defaultProperty = genericBeanInfo.defaultProperty;
        if (genericBeanInfo.methods != null) {
            int length3 = genericBeanInfo.methods.length;
            this.methods = new MethodDescriptor[length3];
            for (int i4 = 0; i4 < length3; i4++) {
                this.methods[i4] = new MethodDescriptor(genericBeanInfo.methods[i4]);
            }
        }
        this.targetBeanInfoRef = genericBeanInfo.targetBeanInfoRef;
    }

    @Override // java.beans.SimpleBeanInfo, java.beans.BeanInfo
    public PropertyDescriptor[] getPropertyDescriptors() {
        return this.properties;
    }

    @Override // java.beans.SimpleBeanInfo, java.beans.BeanInfo
    public int getDefaultPropertyIndex() {
        return this.defaultProperty;
    }

    @Override // java.beans.SimpleBeanInfo, java.beans.BeanInfo
    public EventSetDescriptor[] getEventSetDescriptors() {
        return this.events;
    }

    @Override // java.beans.SimpleBeanInfo, java.beans.BeanInfo
    public int getDefaultEventIndex() {
        return this.defaultEvent;
    }

    @Override // java.beans.SimpleBeanInfo, java.beans.BeanInfo
    public MethodDescriptor[] getMethodDescriptors() {
        return this.methods;
    }

    @Override // java.beans.SimpleBeanInfo, java.beans.BeanInfo
    public BeanDescriptor getBeanDescriptor() {
        return this.beanDescriptor;
    }

    @Override // java.beans.SimpleBeanInfo, java.beans.BeanInfo
    public Image getIcon(int i2) {
        BeanInfo targetBeanInfo = getTargetBeanInfo();
        if (targetBeanInfo != null) {
            return targetBeanInfo.getIcon(i2);
        }
        return super.getIcon(i2);
    }

    private BeanInfo getTargetBeanInfo() {
        if (this.targetBeanInfoRef == null) {
            return null;
        }
        BeanInfo beanInfoFind = this.targetBeanInfoRef.get();
        if (beanInfoFind == null) {
            beanInfoFind = ThreadGroupContext.getContext().getBeanInfoFinder().find(this.beanDescriptor.getBeanClass());
            if (beanInfoFind != null) {
                this.targetBeanInfoRef = new SoftReference(beanInfoFind);
            }
        }
        return beanInfoFind;
    }
}
