package java.beans;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageProducer;
import java.net.URL;

/* loaded from: rt.jar:java/beans/SimpleBeanInfo.class */
public class SimpleBeanInfo implements BeanInfo {
    @Override // java.beans.BeanInfo
    public BeanDescriptor getBeanDescriptor() {
        return null;
    }

    @Override // java.beans.BeanInfo
    public PropertyDescriptor[] getPropertyDescriptors() {
        return null;
    }

    @Override // java.beans.BeanInfo
    public int getDefaultPropertyIndex() {
        return -1;
    }

    @Override // java.beans.BeanInfo
    public EventSetDescriptor[] getEventSetDescriptors() {
        return null;
    }

    @Override // java.beans.BeanInfo
    public int getDefaultEventIndex() {
        return -1;
    }

    @Override // java.beans.BeanInfo
    public MethodDescriptor[] getMethodDescriptors() {
        return null;
    }

    @Override // java.beans.BeanInfo
    public BeanInfo[] getAdditionalBeanInfo() {
        return null;
    }

    @Override // java.beans.BeanInfo
    public Image getIcon(int i2) {
        return null;
    }

    public Image loadImage(String str) {
        ImageProducer imageProducer;
        try {
            URL resource = getClass().getResource(str);
            if (resource != null && (imageProducer = (ImageProducer) resource.getContent()) != null) {
                return Toolkit.getDefaultToolkit().createImage(imageProducer);
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }
}
