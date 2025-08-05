package com.sun.jmx.interceptor;

import java.io.ObjectInputStream;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;

/* loaded from: rt.jar:com/sun/jmx/interceptor/MBeanServerInterceptor.class */
public interface MBeanServerInterceptor extends MBeanServer {
    @Override // javax.management.MBeanServer
    Object instantiate(String str) throws MBeanException, ReflectionException;

    @Override // javax.management.MBeanServer
    Object instantiate(String str, ObjectName objectName) throws MBeanException, InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServer
    Object instantiate(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException;

    @Override // javax.management.MBeanServer
    Object instantiate(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServer
    @Deprecated
    ObjectInputStream deserialize(ObjectName objectName, byte[] bArr) throws OperationsException;

    @Override // javax.management.MBeanServer
    @Deprecated
    ObjectInputStream deserialize(String str, byte[] bArr) throws OperationsException, ReflectionException;

    @Override // javax.management.MBeanServer
    @Deprecated
    ObjectInputStream deserialize(String str, ObjectName objectName, byte[] bArr) throws OperationsException, ReflectionException;

    @Override // javax.management.MBeanServer
    ClassLoaderRepository getClassLoaderRepository();
}
