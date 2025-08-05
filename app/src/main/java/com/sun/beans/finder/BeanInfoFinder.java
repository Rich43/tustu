package com.sun.beans.finder;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/beans/finder/BeanInfoFinder.class */
public final class BeanInfoFinder extends InstanceFinder<BeanInfo> {
    private static final String DEFAULT = "sun.beans.infos";
    private static final String DEFAULT_NEW = "com.sun.beans.infos";

    @Override // com.sun.beans.finder.InstanceFinder
    protected /* bridge */ /* synthetic */ BeanInfo instantiate(Class cls, String str, String str2) {
        return instantiate((Class<?>) cls, str, str2);
    }

    @Override // com.sun.beans.finder.InstanceFinder
    public /* bridge */ /* synthetic */ void setPackages(String[] strArr) {
        super.setPackages(strArr);
    }

    @Override // com.sun.beans.finder.InstanceFinder
    public /* bridge */ /* synthetic */ String[] getPackages() {
        return super.getPackages();
    }

    public BeanInfoFinder() {
        super(BeanInfo.class, true, "BeanInfo", DEFAULT);
    }

    private static boolean isValid(Class<?> cls, Method method) {
        return method != null && method.getDeclaringClass().isAssignableFrom(cls);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.beans.finder.InstanceFinder
    protected BeanInfo instantiate(Class<?> cls, String str, String str2) {
        if (DEFAULT.equals(str)) {
            str = DEFAULT_NEW;
        }
        BeanInfo beanInfo = (!DEFAULT_NEW.equals(str) || "ComponentBeanInfo".equals(str2)) ? (BeanInfo) super.instantiate(cls, str, str2) : null;
        if (beanInfo != null) {
            BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
            if (beanDescriptor != null) {
                if (cls.equals(beanDescriptor.getBeanClass())) {
                    return beanInfo;
                }
                return null;
            }
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null) {
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    if (readMethod == null) {
                        readMethod = propertyDescriptor.getWriteMethod();
                    }
                    if (isValid(cls, readMethod)) {
                        return beanInfo;
                    }
                }
                return null;
            }
            MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
            if (methodDescriptors != null) {
                for (MethodDescriptor methodDescriptor : methodDescriptors) {
                    if (isValid(cls, methodDescriptor.getMethod())) {
                        return beanInfo;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }
}
