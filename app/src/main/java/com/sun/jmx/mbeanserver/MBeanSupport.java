package com.sun.jmx.mbeanserver;

import java.util.Iterator;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanSupport.class */
public abstract class MBeanSupport<M> implements DynamicMBean2, MBeanRegistration {
    private final MBeanInfo mbeanInfo;
    private final Object resource;
    private final PerInterface<M> perInterface;

    abstract MBeanIntrospector<M> getMBeanIntrospector();

    abstract Object getCookie();

    public abstract void register(MBeanServer mBeanServer, ObjectName objectName) throws Exception;

    public abstract void unregister();

    <T> MBeanSupport(T t2, Class<T> cls) throws NotCompliantMBeanException {
        if (cls == null) {
            throw new NotCompliantMBeanException("Null MBean interface");
        }
        if (!cls.isInstance(t2)) {
            throw new NotCompliantMBeanException("Resource class " + t2.getClass().getName() + " is not an instance of " + cls.getName());
        }
        ReflectUtil.checkPackageAccess((Class<?>) cls);
        this.resource = t2;
        MBeanIntrospector<M> mBeanIntrospector = getMBeanIntrospector();
        this.perInterface = mBeanIntrospector.getPerInterface(cls);
        this.mbeanInfo = mBeanIntrospector.getMBeanInfo(t2, this.perInterface);
    }

    public final boolean isMXBean() {
        return this.perInterface.isMXBean();
    }

    @Override // javax.management.MBeanRegistration
    public final ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        if (this.resource instanceof MBeanRegistration) {
            objectName = ((MBeanRegistration) this.resource).preRegister(mBeanServer, objectName);
        }
        return objectName;
    }

    @Override // com.sun.jmx.mbeanserver.DynamicMBean2
    public final void preRegister2(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        register(mBeanServer, objectName);
    }

    @Override // com.sun.jmx.mbeanserver.DynamicMBean2
    public final void registerFailed() {
        unregister();
    }

    @Override // javax.management.MBeanRegistration
    public final void postRegister(Boolean bool) {
        if (this.resource instanceof MBeanRegistration) {
            ((MBeanRegistration) this.resource).postRegister(bool);
        }
    }

    @Override // javax.management.MBeanRegistration
    public final void preDeregister() throws Exception {
        if (this.resource instanceof MBeanRegistration) {
            ((MBeanRegistration) this.resource).preDeregister();
        }
    }

    @Override // javax.management.MBeanRegistration
    public final void postDeregister() {
        try {
            unregister();
        } finally {
            if (this.resource instanceof MBeanRegistration) {
                ((MBeanRegistration) this.resource).postDeregister();
            }
        }
    }

    @Override // javax.management.DynamicMBean
    public final Object getAttribute(String str) throws MBeanException, AttributeNotFoundException, ReflectionException {
        return this.perInterface.getAttribute(this.resource, str, getCookie());
    }

    @Override // javax.management.DynamicMBean
    public final AttributeList getAttributes(String[] strArr) {
        AttributeList attributeList = new AttributeList(strArr.length);
        for (String str : strArr) {
            try {
                attributeList.add(new Attribute(str, getAttribute(str)));
            } catch (Exception e2) {
            }
        }
        return attributeList;
    }

    @Override // javax.management.DynamicMBean
    public final void setAttribute(Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, ReflectionException {
        this.perInterface.setAttribute(this.resource, attribute.getName(), attribute.getValue(), getCookie());
    }

    @Override // javax.management.DynamicMBean
    public final AttributeList setAttributes(AttributeList attributeList) {
        AttributeList attributeList2 = new AttributeList(attributeList.size());
        Iterator<Object> it = attributeList.iterator();
        while (it.hasNext()) {
            Attribute attribute = (Attribute) it.next();
            try {
                setAttribute(attribute);
                attributeList2.add(new Attribute(attribute.getName(), attribute.getValue()));
            } catch (Exception e2) {
            }
        }
        return attributeList2;
    }

    @Override // javax.management.DynamicMBean
    public final Object invoke(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException {
        return this.perInterface.invoke(this.resource, str, objArr, strArr, getCookie());
    }

    @Override // javax.management.DynamicMBean
    public MBeanInfo getMBeanInfo() {
        return this.mbeanInfo;
    }

    @Override // com.sun.jmx.mbeanserver.DynamicMBean2
    public final String getClassName() {
        return this.resource.getClass().getName();
    }

    @Override // com.sun.jmx.mbeanserver.DynamicMBean2
    public final Object getResource() {
        return this.resource;
    }

    public final Class<?> getMBeanInterface() {
        return this.perInterface.getMBeanInterface();
    }
}
