package com.sun.jmx.mbeanserver;

import com.sun.jmx.defaults.JmxProperties;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.logging.Level;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanPermission;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;
import javax.management.RuntimeOperationsException;
import sun.reflect.misc.ConstructorUtil;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanInstantiator.class */
public class MBeanInstantiator {
    private final ModifiableClassLoaderRepository clr;
    private static final Map<String, Class<?>> primitiveClasses = Util.newMap();

    MBeanInstantiator(ModifiableClassLoaderRepository modifiableClassLoaderRepository) {
        this.clr = modifiableClassLoaderRepository;
    }

    public void testCreation(Class<?> cls) throws NotCompliantMBeanException {
        Introspector.testCreation(cls);
    }

    public Class<?> findClassWithDefaultLoaderRepository(String str) throws ClassNotFoundException, ReflectionException {
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("The class name cannot be null"), "Exception occurred during object instantiation");
        }
        ReflectUtil.checkPackageAccess(str);
        try {
            if (this.clr == null) {
                throw new ClassNotFoundException(str);
            }
            return this.clr.loadClass(str);
        } catch (ClassNotFoundException e2) {
            throw new ReflectionException(e2, "The MBean class could not be loaded by the default loader repository");
        }
    }

    public Class<?> findClass(String str, ClassLoader classLoader) throws ReflectionException {
        return loadClass(str, classLoader);
    }

    public Class<?> findClass(String str, ObjectName objectName) throws InstanceNotFoundException, ReflectionException {
        ClassLoader classLoader;
        if (objectName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException(), "Null loader passed in parameter");
        }
        synchronized (this) {
            classLoader = getClassLoader(objectName);
        }
        if (classLoader == null) {
            throw new InstanceNotFoundException("The loader named " + ((Object) objectName) + " is not registered in the MBeanServer");
        }
        return findClass(str, classLoader);
    }

    public Class<?>[] findSignatureClasses(String[] strArr, ClassLoader classLoader) throws ReflectionException {
        if (strArr == null) {
            return null;
        }
        int length = strArr.length;
        Class<?>[] clsArr = new Class[length];
        if (length == 0) {
            return clsArr;
        }
        for (int i2 = 0; i2 < length; i2++) {
            try {
                Class<?> cls = primitiveClasses.get(strArr[i2]);
                if (cls != null) {
                    clsArr[i2] = cls;
                } else {
                    ReflectUtil.checkPackageAccess(strArr[i2]);
                    if (classLoader != null) {
                        clsArr[i2] = Class.forName(strArr[i2], false, classLoader);
                    } else {
                        clsArr[i2] = findClass(strArr[i2], getClass().getClassLoader());
                    }
                }
            } catch (ClassNotFoundException e2) {
                if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanInstantiator.class.getName(), "findSignatureClasses", "The parameter class could not be found", (Throwable) e2);
                }
                throw new ReflectionException(e2, "The parameter class could not be found");
            } catch (RuntimeException e3) {
                if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanInstantiator.class.getName(), "findSignatureClasses", "Unexpected exception", (Throwable) e3);
                }
                throw e3;
            }
        }
        return clsArr;
    }

    public Object instantiate(Class<?> cls) throws MBeanException, SecurityException, ReflectionException {
        checkMBeanPermission(cls, (String) null, (ObjectName) null, "instantiate");
        Constructor<?> constructorFindConstructor = findConstructor(cls, null);
        if (constructorFindConstructor == null) {
            throw new ReflectionException(new NoSuchMethodException("No such constructor"));
        }
        try {
            ReflectUtil.checkPackageAccess(cls);
            ensureClassAccess(cls);
            return constructorFindConstructor.newInstance(new Object[0]);
        } catch (IllegalAccessException e2) {
            throw new ReflectionException(e2, "Exception thrown trying to invoke the MBean's empty constructor");
        } catch (IllegalArgumentException e3) {
            throw new ReflectionException(e3, "Exception thrown trying to invoke the MBean's empty constructor");
        } catch (InstantiationException e4) {
            throw new ReflectionException(e4, "Exception thrown trying to invoke the MBean's empty constructor");
        } catch (NoSuchMethodError e5) {
            throw new ReflectionException(new NoSuchMethodException("No constructor"), "No such constructor");
        } catch (InvocationTargetException e6) {
            Throwable targetException = e6.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw new RuntimeMBeanException((RuntimeException) targetException, "RuntimeException thrown in the MBean's empty constructor");
            }
            if (targetException instanceof Error) {
                throw new RuntimeErrorException((Error) targetException, "Error thrown in the MBean's empty constructor");
            }
            throw new MBeanException((Exception) targetException, "Exception thrown in the MBean's empty constructor");
        }
    }

    public Object instantiate(Class<?> cls, Object[] objArr, String[] strArr, ClassLoader classLoader) throws MBeanException, SecurityException, ReflectionException {
        checkMBeanPermission(cls, (String) null, (ObjectName) null, "instantiate");
        try {
            Constructor<?> constructorFindConstructor = findConstructor(cls, strArr == null ? null : findSignatureClasses(strArr, cls.getClassLoader()));
            if (constructorFindConstructor == null) {
                throw new ReflectionException(new NoSuchMethodException("No such constructor"));
            }
            try {
                ReflectUtil.checkPackageAccess(cls);
                ensureClassAccess(cls);
                return constructorFindConstructor.newInstance(objArr);
            } catch (IllegalAccessException e2) {
                throw new ReflectionException(e2, "Exception thrown trying to invoke the MBean's constructor");
            } catch (InstantiationException e3) {
                throw new ReflectionException(e3, "Exception thrown trying to invoke the MBean's constructor");
            } catch (NoSuchMethodError e4) {
                throw new ReflectionException(new NoSuchMethodException("No such constructor found"), "No such constructor");
            } catch (InvocationTargetException e5) {
                Throwable targetException = e5.getTargetException();
                if (targetException instanceof RuntimeException) {
                    throw new RuntimeMBeanException((RuntimeException) targetException, "RuntimeException thrown in the MBean's constructor");
                }
                if (targetException instanceof Error) {
                    throw new RuntimeErrorException((Error) targetException, "Error thrown in the MBean's constructor");
                }
                throw new MBeanException((Exception) targetException, "Exception thrown in the MBean's constructor");
            }
        } catch (IllegalArgumentException e6) {
            throw new ReflectionException(e6, "The constructor parameter classes could not be loaded");
        }
    }

    public ObjectInputStream deserialize(ClassLoader classLoader, byte[] bArr) throws OperationsException {
        if (bArr == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException(), "Null data passed in parameter");
        }
        if (bArr.length == 0) {
            throw new RuntimeOperationsException(new IllegalArgumentException(), "Empty data passed in parameter");
        }
        try {
            return new ObjectInputStreamWithLoader(new ByteArrayInputStream(bArr), classLoader);
        } catch (IOException e2) {
            throw new OperationsException("An IOException occurred trying to de-serialize the data");
        }
    }

    public ObjectInputStream deserialize(String str, ObjectName objectName, byte[] bArr, ClassLoader classLoader) throws OperationsException, ClassNotFoundException, ReflectionException {
        Class<?> cls;
        if (bArr == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException(), "Null data passed in parameter");
        }
        if (bArr.length == 0) {
            throw new RuntimeOperationsException(new IllegalArgumentException(), "Empty data passed in parameter");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException(), "Null className passed in parameter");
        }
        ReflectUtil.checkPackageAccess(str);
        if (objectName == null) {
            cls = findClass(str, classLoader);
        } else {
            try {
                ClassLoader classLoader2 = getClassLoader(objectName);
                if (classLoader2 == null) {
                    throw new ClassNotFoundException(str);
                }
                cls = Class.forName(str, false, classLoader2);
            } catch (ClassNotFoundException e2) {
                throw new ReflectionException(e2, "The MBean class could not be loaded by the " + objectName.toString() + " class loader");
            }
        }
        try {
            return new ObjectInputStreamWithLoader(new ByteArrayInputStream(bArr), cls.getClassLoader());
        } catch (IOException e3) {
            throw new OperationsException("An IOException occurred trying to de-serialize the data");
        }
    }

    public Object instantiate(String str) throws MBeanException, ReflectionException {
        return instantiate(str, (Object[]) null, (String[]) null, (ClassLoader) null);
    }

    public Object instantiate(String str, ObjectName objectName, ClassLoader classLoader) throws MBeanException, InstanceNotFoundException, ReflectionException {
        return instantiate(str, objectName, (Object[]) null, (String[]) null, classLoader);
    }

    public Object instantiate(String str, Object[] objArr, String[] strArr, ClassLoader classLoader) throws MBeanException, ReflectionException {
        return instantiate(findClassWithDefaultLoaderRepository(str), objArr, strArr, classLoader);
    }

    public Object instantiate(String str, ObjectName objectName, Object[] objArr, String[] strArr, ClassLoader classLoader) throws MBeanException, InstanceNotFoundException, ReflectionException {
        Class<?> clsFindClass;
        if (objectName == null) {
            clsFindClass = findClass(str, classLoader);
        } else {
            clsFindClass = findClass(str, objectName);
        }
        return instantiate(clsFindClass, objArr, strArr, classLoader);
    }

    public ModifiableClassLoaderRepository getClassLoaderRepository() throws SecurityException {
        checkMBeanPermission((String) null, (String) null, (ObjectName) null, "getClassLoaderRepository");
        return this.clr;
    }

    static Class<?> loadClass(String str, ClassLoader classLoader) throws ReflectionException {
        Class<?> cls;
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("The class name cannot be null"), "Exception occurred during object instantiation");
        }
        ReflectUtil.checkPackageAccess(str);
        if (classLoader == null) {
            try {
                classLoader = MBeanInstantiator.class.getClassLoader();
            } catch (ClassNotFoundException e2) {
                throw new ReflectionException(e2, "The MBean class could not be loaded");
            }
        }
        if (classLoader != null) {
            cls = Class.forName(str, false, classLoader);
        } else {
            cls = Class.forName(str);
        }
        return cls;
    }

    static Class<?>[] loadSignatureClasses(String[] strArr, ClassLoader classLoader) throws ReflectionException {
        if (strArr == null) {
            return null;
        }
        ClassLoader classLoader2 = classLoader == null ? MBeanInstantiator.class.getClassLoader() : classLoader;
        int length = strArr.length;
        Class<?>[] clsArr = new Class[length];
        if (length == 0) {
            return clsArr;
        }
        for (int i2 = 0; i2 < length; i2++) {
            try {
                Class<?> cls = primitiveClasses.get(strArr[i2]);
                if (cls != null) {
                    clsArr[i2] = cls;
                } else {
                    ReflectUtil.checkPackageAccess(strArr[i2]);
                    clsArr[i2] = Class.forName(strArr[i2], false, classLoader2);
                }
            } catch (ClassNotFoundException e2) {
                if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanInstantiator.class.getName(), "findSignatureClasses", "The parameter class could not be found", (Throwable) e2);
                }
                throw new ReflectionException(e2, "The parameter class could not be found");
            } catch (RuntimeException e3) {
                if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanInstantiator.class.getName(), "findSignatureClasses", "Unexpected exception", (Throwable) e3);
                }
                throw e3;
            }
        }
        return clsArr;
    }

    private Constructor<?> findConstructor(Class<?> cls, Class<?>[] clsArr) {
        try {
            return ConstructorUtil.getConstructor(cls, clsArr);
        } catch (Exception e2) {
            return null;
        }
    }

    static {
        for (Class<?> cls : new Class[]{Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Character.TYPE, Boolean.TYPE}) {
            primitiveClasses.put(cls.getName(), cls);
        }
    }

    private static void checkMBeanPermission(Class<?> cls, String str, ObjectName objectName, String str2) throws SecurityException {
        if (cls != null) {
            checkMBeanPermission(cls.getName(), str, objectName, str2);
        }
    }

    private static void checkMBeanPermission(String str, String str2, ObjectName objectName, String str3) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new MBeanPermission(str, str2, objectName, str3));
        }
    }

    private static void ensureClassAccess(Class cls) throws IllegalAccessException {
        if (!Modifier.isPublic(cls.getModifiers())) {
            throw new IllegalAccessException("Class is not public and can't be instantiated");
        }
    }

    private ClassLoader getClassLoader(final ObjectName objectName) {
        if (this.clr == null) {
            return null;
        }
        Permissions permissions = new Permissions();
        permissions.add(new MBeanPermission("*", null, objectName, "getClassLoader"));
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.jmx.mbeanserver.MBeanInstantiator.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return MBeanInstantiator.this.clr.getClassLoader(objectName);
            }
        }, new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissions)}));
    }
}
