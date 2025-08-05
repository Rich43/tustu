package com.sun.jmx.interceptor;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.DynamicMBean2;
import com.sun.jmx.mbeanserver.Introspector;
import com.sun.jmx.mbeanserver.MBeanInstantiator;
import com.sun.jmx.mbeanserver.ModifiableClassLoaderRepository;
import com.sun.jmx.mbeanserver.NamedObject;
import com.sun.jmx.mbeanserver.Repository;
import com.sun.jmx.mbeanserver.Util;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.JMRuntimeException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanPermission;
import javax.management.MBeanRegistration;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.MBeanTrustPermission;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryEval;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;
import javax.management.RuntimeOperationsException;
import javax.management.loading.ClassLoaderRepository;

/* loaded from: rt.jar:com/sun/jmx/interceptor/DefaultMBeanServerInterceptor.class */
public class DefaultMBeanServerInterceptor implements MBeanServerInterceptor {
    private final transient MBeanInstantiator instantiator;
    private transient MBeanServer server;
    private final transient MBeanServerDelegate delegate;
    private final transient Repository repository;
    private final String domain;
    private final transient WeakHashMap<ListenerWrapper, WeakReference<ListenerWrapper>> listenerWrappers = new WeakHashMap<>();
    private final Set<ObjectName> beingUnregistered = new HashSet();

    /* loaded from: rt.jar:com/sun/jmx/interceptor/DefaultMBeanServerInterceptor$ResourceContext.class */
    private interface ResourceContext extends Repository.RegistrationContext {
        public static final ResourceContext NONE = new ResourceContext() { // from class: com.sun.jmx.interceptor.DefaultMBeanServerInterceptor.ResourceContext.1
            @Override // com.sun.jmx.interceptor.DefaultMBeanServerInterceptor.ResourceContext
            public void done() {
            }

            @Override // com.sun.jmx.mbeanserver.Repository.RegistrationContext
            public void registering() {
            }

            @Override // com.sun.jmx.mbeanserver.Repository.RegistrationContext
            public void unregistered() {
            }
        };

        void done();
    }

    public DefaultMBeanServerInterceptor(MBeanServer mBeanServer, MBeanServerDelegate mBeanServerDelegate, MBeanInstantiator mBeanInstantiator, Repository repository) {
        this.server = null;
        if (mBeanServer == null) {
            throw new IllegalArgumentException("outer MBeanServer cannot be null");
        }
        if (mBeanServerDelegate == null) {
            throw new IllegalArgumentException("MBeanServerDelegate cannot be null");
        }
        if (mBeanInstantiator == null) {
            throw new IllegalArgumentException("MBeanInstantiator cannot be null");
        }
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.server = mBeanServer;
        this.delegate = mBeanServerDelegate;
        this.instantiator = mBeanInstantiator;
        this.repository = repository;
        this.domain = repository.getDefaultDomain();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, ReflectionException {
        return createMBean(str, objectName, (Object[]) null, (String[]) null);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstanceNotFoundException, ReflectionException {
        return createMBean(str, objectName, objectName2, (Object[]) null, (String[]) null);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, ReflectionException {
        try {
            return createMBean(str, objectName, null, true, objArr, strArr);
        } catch (InstanceNotFoundException e2) {
            throw ((IllegalArgumentException) EnvHelp.initCause(new IllegalArgumentException("Unexpected exception: " + ((Object) e2)), e2));
        }
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstanceNotFoundException, ReflectionException {
        return createMBean(str, objectName, objectName2, false, objArr, strArr);
    }

    private ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, boolean z2, Object[] objArr, String[] strArr) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, ClassNotFoundException, SecurityException, InstanceNotFoundException, ReflectionException {
        Class<?> clsFindClass;
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("The class name cannot be null"), "Exception occurred during MBean creation");
        }
        if (objectName != null) {
            if (objectName.isPattern()) {
                throw new RuntimeOperationsException(new IllegalArgumentException("Invalid name->" + objectName.toString()), "Exception occurred during MBean creation");
            }
            objectName = nonDefaultDomain(objectName);
        }
        checkMBeanPermission(str, (String) null, (ObjectName) null, "instantiate");
        checkMBeanPermission(str, (String) null, objectName, "registerMBean");
        if (z2) {
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "createMBean", "ClassName = " + str + ", ObjectName = " + ((Object) objectName));
            }
            clsFindClass = this.instantiator.findClassWithDefaultLoaderRepository(str);
        } else if (objectName2 == null) {
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "createMBean", "ClassName = " + str + ", ObjectName = " + ((Object) objectName) + ", Loader name = null");
            }
            clsFindClass = this.instantiator.findClass(str, this.server.getClass().getClassLoader());
        } else {
            ObjectName objectNameNonDefaultDomain = nonDefaultDomain(objectName2);
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "createMBean", "ClassName = " + str + ", ObjectName = " + ((Object) objectName) + ", Loader name = " + ((Object) objectNameNonDefaultDomain));
            }
            clsFindClass = this.instantiator.findClass(str, objectNameNonDefaultDomain);
        }
        checkMBeanTrustPermission(clsFindClass);
        Introspector.testCreation(clsFindClass);
        Introspector.checkCompliance(clsFindClass);
        Object objInstantiate = this.instantiator.instantiate(clsFindClass, objArr, strArr, this.server.getClass().getClassLoader());
        return registerObject(getNewMBeanClassName(objInstantiate), objInstantiate, objectName);
    }

    @Override // javax.management.MBeanServer
    public ObjectInstance registerMBean(Object obj, ObjectName objectName) throws InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException, SecurityException {
        Class<?> cls = obj.getClass();
        Introspector.checkCompliance(cls);
        String newMBeanClassName = getNewMBeanClassName(obj);
        checkMBeanPermission(newMBeanClassName, (String) null, objectName, "registerMBean");
        checkMBeanTrustPermission(cls);
        return registerObject(newMBeanClassName, obj, objectName);
    }

    private static String getNewMBeanClassName(Object obj) throws NotCompliantMBeanException {
        if (obj instanceof DynamicMBean) {
            try {
                String className = ((DynamicMBean) obj).getMBeanInfo().getClassName();
                if (className == null) {
                    throw new NotCompliantMBeanException("MBeanInfo has null class name");
                }
                return className;
            } catch (Exception e2) {
                NotCompliantMBeanException notCompliantMBeanException = new NotCompliantMBeanException("Bad getMBeanInfo()");
                notCompliantMBeanException.initCause(e2);
                throw notCompliantMBeanException;
            }
        }
        return obj.getClass().getName();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void unregisterMBean(ObjectName objectName) throws MBeanRegistrationException, InstanceNotFoundException {
        if (objectName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Object name cannot be null"), "Exception occurred trying to unregister the MBean");
        }
        ObjectName objectNameNonDefaultDomain = nonDefaultDomain(objectName);
        synchronized (this.beingUnregistered) {
            while (this.beingUnregistered.contains(objectNameNonDefaultDomain)) {
                try {
                    this.beingUnregistered.wait();
                } catch (InterruptedException e2) {
                    throw new MBeanRegistrationException(e2, e2.toString());
                }
            }
            this.beingUnregistered.add(objectNameNonDefaultDomain);
        }
        try {
            exclusiveUnregisterMBean(objectNameNonDefaultDomain);
            synchronized (this.beingUnregistered) {
                this.beingUnregistered.remove(objectNameNonDefaultDomain);
                this.beingUnregistered.notifyAll();
            }
        } catch (Throwable th) {
            synchronized (this.beingUnregistered) {
                this.beingUnregistered.remove(objectNameNonDefaultDomain);
                this.beingUnregistered.notifyAll();
                throw th;
            }
        }
    }

    private void exclusiveUnregisterMBean(ObjectName objectName) throws MBeanRegistrationException, InstanceNotFoundException {
        DynamicMBean mBean = getMBean(objectName);
        checkMBeanPermission(mBean, (String) null, objectName, "unregisterMBean");
        if (mBean instanceof MBeanRegistration) {
            preDeregisterInvoke((MBeanRegistration) mBean);
        }
        ResourceContext resourceContextUnregisterFromRepository = unregisterFromRepository(getResource(mBean), mBean, objectName);
        try {
            if (mBean instanceof MBeanRegistration) {
                postDeregisterInvoke(objectName, (MBeanRegistration) mBean);
            }
        } finally {
            resourceContextUnregisterFromRepository.done();
        }
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public ObjectInstance getObjectInstance(ObjectName objectName) throws InstanceNotFoundException {
        ObjectName objectNameNonDefaultDomain = nonDefaultDomain(objectName);
        DynamicMBean mBean = getMBean(objectNameNonDefaultDomain);
        checkMBeanPermission(mBean, (String) null, objectNameNonDefaultDomain, "getObjectInstance");
        return new ObjectInstance(objectNameNonDefaultDomain, getClassName(mBean));
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Set<ObjectInstance> queryMBeans(ObjectName objectName, QueryExp queryExp) {
        if (System.getSecurityManager() != null) {
            checkMBeanPermission((String) null, (String) null, (ObjectName) null, "queryMBeans");
            Set<ObjectInstance> setQueryMBeansImpl = queryMBeansImpl(objectName, null);
            HashSet hashSet = new HashSet(setQueryMBeansImpl.size());
            for (ObjectInstance objectInstance : setQueryMBeansImpl) {
                try {
                    checkMBeanPermission(objectInstance.getClassName(), (String) null, objectInstance.getObjectName(), "queryMBeans");
                    hashSet.add(objectInstance);
                } catch (SecurityException e2) {
                }
            }
            return filterListOfObjectInstances(hashSet, queryExp);
        }
        return queryMBeansImpl(objectName, queryExp);
    }

    private Set<ObjectInstance> queryMBeansImpl(ObjectName objectName, QueryExp queryExp) {
        return objectInstancesFromFilteredNamedObjects(this.repository.query(objectName, queryExp), queryExp);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Set<ObjectName> queryNames(ObjectName objectName, QueryExp queryExp) {
        Set<ObjectName> setQueryNamesImpl;
        if (System.getSecurityManager() != null) {
            checkMBeanPermission((String) null, (String) null, (ObjectName) null, "queryNames");
            Set<ObjectInstance> setQueryMBeansImpl = queryMBeansImpl(objectName, null);
            HashSet hashSet = new HashSet(setQueryMBeansImpl.size());
            for (ObjectInstance objectInstance : setQueryMBeansImpl) {
                try {
                    checkMBeanPermission(objectInstance.getClassName(), (String) null, objectInstance.getObjectName(), "queryNames");
                    hashSet.add(objectInstance);
                } catch (SecurityException e2) {
                }
            }
            Set<ObjectInstance> setFilterListOfObjectInstances = filterListOfObjectInstances(hashSet, queryExp);
            setQueryNamesImpl = new HashSet(setFilterListOfObjectInstances.size());
            Iterator<ObjectInstance> it = setFilterListOfObjectInstances.iterator();
            while (it.hasNext()) {
                setQueryNamesImpl.add(it.next().getObjectName());
            }
        } else {
            setQueryNamesImpl = queryNamesImpl(objectName, queryExp);
        }
        return setQueryNamesImpl;
    }

    private Set<ObjectName> queryNamesImpl(ObjectName objectName, QueryExp queryExp) {
        return objectNamesFromFilteredNamedObjects(this.repository.query(objectName, queryExp), queryExp);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public boolean isRegistered(ObjectName objectName) {
        if (objectName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Object name cannot be null"), "Object name cannot be null");
        }
        return this.repository.contains(nonDefaultDomain(objectName));
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public String[] getDomains() {
        if (System.getSecurityManager() != null) {
            checkMBeanPermission((String) null, (String) null, (ObjectName) null, "getDomains");
            String[] domains = this.repository.getDomains();
            ArrayList arrayList = new ArrayList(domains.length);
            for (int i2 = 0; i2 < domains.length; i2++) {
                try {
                    checkMBeanPermission((String) null, (String) null, Util.newObjectName(domains[i2] + ":x=x"), "getDomains");
                    arrayList.add(domains[i2]);
                } catch (SecurityException e2) {
                }
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        return this.repository.getDomains();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Integer getMBeanCount() {
        return this.repository.getCount();
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Object getAttribute(ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
        if (objectName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Object name cannot be null"), "Exception occurred trying to invoke the getter on the MBean");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Exception occurred trying to invoke the getter on the MBean");
        }
        ObjectName objectNameNonDefaultDomain = nonDefaultDomain(objectName);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "getAttribute", "Attribute = " + str + ", ObjectName = " + ((Object) objectNameNonDefaultDomain));
        }
        DynamicMBean mBean = getMBean(objectNameNonDefaultDomain);
        checkMBeanPermission(mBean, str, objectNameNonDefaultDomain, "getAttribute");
        try {
            return mBean.getAttribute(str);
        } catch (AttributeNotFoundException e2) {
            throw e2;
        } catch (Throwable th) {
            rethrowMaybeMBeanException(th);
            throw new AssertionError();
        }
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public AttributeList getAttributes(ObjectName objectName, String[] strArr) throws InstanceNotFoundException, ReflectionException {
        String[] strArr2;
        if (objectName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName name cannot be null"), "Exception occurred trying to invoke the getter on the MBean");
        }
        if (strArr == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attributes cannot be null"), "Exception occurred trying to invoke the getter on the MBean");
        }
        ObjectName objectNameNonDefaultDomain = nonDefaultDomain(objectName);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "getAttributes", "ObjectName = " + ((Object) objectNameNonDefaultDomain));
        }
        DynamicMBean mBean = getMBean(objectNameNonDefaultDomain);
        if (System.getSecurityManager() == null) {
            strArr2 = strArr;
        } else {
            String className = getClassName(mBean);
            checkMBeanPermission(className, (String) null, objectNameNonDefaultDomain, "getAttribute");
            ArrayList arrayList = new ArrayList(strArr.length);
            for (String str : strArr) {
                try {
                    checkMBeanPermission(className, str, objectNameNonDefaultDomain, "getAttribute");
                    arrayList.add(str);
                } catch (SecurityException e2) {
                }
            }
            strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        try {
            return mBean.getAttributes(strArr2);
        } catch (Throwable th) {
            rethrow(th);
            throw new AssertionError();
        }
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void setAttribute(ObjectName objectName, Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
        if (objectName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName name cannot be null"), "Exception occurred trying to invoke the setter on the MBean");
        }
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Exception occurred trying to invoke the setter on the MBean");
        }
        ObjectName objectNameNonDefaultDomain = nonDefaultDomain(objectName);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "setAttribute", "ObjectName = " + ((Object) objectNameNonDefaultDomain) + ", Attribute = " + attribute.getName());
        }
        DynamicMBean mBean = getMBean(objectNameNonDefaultDomain);
        checkMBeanPermission(mBean, attribute.getName(), objectNameNonDefaultDomain, "setAttribute");
        try {
            mBean.setAttribute(attribute);
        } catch (AttributeNotFoundException e2) {
            throw e2;
        } catch (InvalidAttributeValueException e3) {
            throw e3;
        } catch (Throwable th) {
            rethrowMaybeMBeanException(th);
            throw new AssertionError();
        }
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws InstanceNotFoundException, ReflectionException {
        AttributeList attributeList2;
        if (objectName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName name cannot be null"), "Exception occurred trying to invoke the setter on the MBean");
        }
        if (attributeList == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList  cannot be null"), "Exception occurred trying to invoke the setter on the MBean");
        }
        ObjectName objectNameNonDefaultDomain = nonDefaultDomain(objectName);
        DynamicMBean mBean = getMBean(objectNameNonDefaultDomain);
        if (System.getSecurityManager() == null) {
            attributeList2 = attributeList;
        } else {
            String className = getClassName(mBean);
            checkMBeanPermission(className, (String) null, objectNameNonDefaultDomain, "setAttribute");
            attributeList2 = new AttributeList(attributeList.size());
            for (Attribute attribute : attributeList.asList()) {
                try {
                    checkMBeanPermission(className, attribute.getName(), objectNameNonDefaultDomain, "setAttribute");
                    attributeList2.add(attribute);
                } catch (SecurityException e2) {
                }
            }
        }
        try {
            return mBean.setAttributes(attributeList2);
        } catch (Throwable th) {
            rethrow(th);
            throw new AssertionError();
        }
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public Object invoke(ObjectName objectName, String str, Object[] objArr, String[] strArr) throws MBeanException, InstanceNotFoundException, ReflectionException {
        ObjectName objectNameNonDefaultDomain = nonDefaultDomain(objectName);
        DynamicMBean mBean = getMBean(objectNameNonDefaultDomain);
        checkMBeanPermission(mBean, str, objectNameNonDefaultDomain, "invoke");
        try {
            return mBean.invoke(str, objArr, strArr);
        } catch (Throwable th) {
            rethrowMaybeMBeanException(th);
            throw new AssertionError();
        }
    }

    private static void rethrow(Throwable th) throws ReflectionException {
        try {
            throw th;
        } catch (Error e2) {
            throw new RuntimeErrorException(e2, e2.toString());
        } catch (RuntimeException e3) {
            throw new RuntimeMBeanException(e3, e3.toString());
        } catch (ReflectionException e4) {
            throw e4;
        } catch (RuntimeErrorException e5) {
            throw e5;
        } catch (RuntimeOperationsException e6) {
            throw e6;
        } catch (Throwable th2) {
            throw new RuntimeException("Unexpected exception", th2);
        }
    }

    private static void rethrowMaybeMBeanException(Throwable th) throws MBeanException, ReflectionException {
        if (th instanceof MBeanException) {
            throw ((MBeanException) th);
        }
        rethrow(th);
    }

    private ObjectInstance registerObject(String str, Object obj, ObjectName objectName) throws InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
        if (obj == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Cannot add null object"), "Exception occurred trying to register the MBean");
        }
        return registerDynamicMBean(str, Introspector.makeDynamicMBean(obj), objectName);
    }

    private ObjectInstance registerDynamicMBean(String str, DynamicMBean dynamicMBean, ObjectName objectName) throws InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
        ObjectName objectNameNonDefaultDomain = nonDefaultDomain(objectName);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "registerMBean", "ObjectName = " + ((Object) objectNameNonDefaultDomain));
        }
        ObjectName objectNamePreRegister = preRegister(dynamicMBean, this.server, objectNameNonDefaultDomain);
        boolean z2 = false;
        ResourceContext resourceContext = null;
        try {
            if (dynamicMBean instanceof DynamicMBean2) {
                try {
                    ((DynamicMBean2) dynamicMBean).preRegister2(this.server, objectNamePreRegister);
                    z2 = true;
                } catch (Exception e2) {
                    if (e2 instanceof RuntimeException) {
                        throw ((RuntimeException) e2);
                    }
                    if (e2 instanceof InstanceAlreadyExistsException) {
                        throw ((InstanceAlreadyExistsException) e2);
                    }
                    throw new RuntimeException(e2);
                }
            }
            if (objectNamePreRegister != objectNameNonDefaultDomain && objectNamePreRegister != null) {
                objectNamePreRegister = ObjectName.getInstance(nonDefaultDomain(objectNamePreRegister));
            }
            checkMBeanPermission(str, (String) null, objectNamePreRegister, "registerMBean");
            if (objectNamePreRegister == null) {
                throw new RuntimeOperationsException(new IllegalArgumentException("No object name specified"), "Exception occurred trying to register the MBean");
            }
            ResourceContext resourceContextRegisterWithRepository = registerWithRepository(getResource(dynamicMBean), dynamicMBean, objectNamePreRegister);
            try {
                postRegister(objectNamePreRegister, dynamicMBean, true, false);
                if (1 != 0 && resourceContextRegisterWithRepository != null) {
                    resourceContextRegisterWithRepository.done();
                }
                return new ObjectInstance(objectNamePreRegister, str);
            } catch (Throwable th) {
                if (1 != 0 && resourceContextRegisterWithRepository != null) {
                    resourceContextRegisterWithRepository.done();
                }
                throw th;
            }
        } catch (Throwable th2) {
            try {
                postRegister(objectNamePreRegister, dynamicMBean, false, z2);
                if (0 != 0 && 0 != 0) {
                    resourceContext.done();
                }
                throw th2;
            } catch (Throwable th3) {
                if (0 != 0 && 0 != 0) {
                    resourceContext.done();
                }
                throw th3;
            }
        }
    }

    private static void throwMBeanRegistrationException(Throwable th, String str) throws MBeanRegistrationException {
        if (th instanceof RuntimeException) {
            throw new RuntimeMBeanException((RuntimeException) th, "RuntimeException thrown " + str);
        }
        if (th instanceof Error) {
            throw new RuntimeErrorException((Error) th, "Error thrown " + str);
        }
        if (th instanceof MBeanRegistrationException) {
            throw ((MBeanRegistrationException) th);
        }
        if (th instanceof Exception) {
            throw new MBeanRegistrationException((Exception) th, "Exception thrown " + str);
        }
        throw new RuntimeException(th);
    }

    private static ObjectName preRegister(DynamicMBean dynamicMBean, MBeanServer mBeanServer, ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException {
        ObjectName objectNamePreRegister = null;
        try {
            if (dynamicMBean instanceof MBeanRegistration) {
                objectNamePreRegister = ((MBeanRegistration) dynamicMBean).preRegister(mBeanServer, objectName);
            }
        } catch (Throwable th) {
            throwMBeanRegistrationException(th, "in preRegister method");
        }
        return objectNamePreRegister != null ? objectNamePreRegister : objectName;
    }

    private static void postRegister(ObjectName objectName, DynamicMBean dynamicMBean, boolean z2, boolean z3) {
        if (z3 && (dynamicMBean instanceof DynamicMBean2)) {
            ((DynamicMBean2) dynamicMBean).registerFailed();
        }
        try {
            if (dynamicMBean instanceof MBeanRegistration) {
                ((MBeanRegistration) dynamicMBean).postRegister(Boolean.valueOf(z2));
            }
        } catch (Error e2) {
            JmxProperties.MBEANSERVER_LOGGER.fine("While registering MBean [" + ((Object) objectName) + "]: Error thrown by postRegister: rethrowing <" + ((Object) e2) + ">, but keeping the MBean registered");
            throw new RuntimeErrorException(e2, "Error thrown in postRegister method: rethrowing <" + ((Object) e2) + ">, but keeping the MBean registered");
        } catch (RuntimeException e3) {
            JmxProperties.MBEANSERVER_LOGGER.fine("While registering MBean [" + ((Object) objectName) + "]: Exception thrown by postRegister: rethrowing <" + ((Object) e3) + ">, but keeping the MBean registered");
            throw new RuntimeMBeanException(e3, "RuntimeException thrown in postRegister method: rethrowing <" + ((Object) e3) + ">, but keeping the MBean registered");
        }
    }

    private static void preDeregisterInvoke(MBeanRegistration mBeanRegistration) throws MBeanRegistrationException {
        try {
            mBeanRegistration.preDeregister();
        } catch (Throwable th) {
            throwMBeanRegistrationException(th, "in preDeregister method");
        }
    }

    private static void postDeregisterInvoke(ObjectName objectName, MBeanRegistration mBeanRegistration) {
        try {
            mBeanRegistration.postDeregister();
        } catch (Error e2) {
            JmxProperties.MBEANSERVER_LOGGER.fine("While unregistering MBean [" + ((Object) objectName) + "]: Error thrown by postDeregister: rethrowing <" + ((Object) e2) + ">, although the MBean is succesfully unregistered");
            throw new RuntimeErrorException(e2, "Error thrown in postDeregister method: rethrowing <" + ((Object) e2) + ">, although the MBean is sucessfully unregistered");
        } catch (RuntimeException e3) {
            JmxProperties.MBEANSERVER_LOGGER.fine("While unregistering MBean [" + ((Object) objectName) + "]: Exception thrown by postDeregister: rethrowing <" + ((Object) e3) + ">, although the MBean is succesfully unregistered");
            throw new RuntimeMBeanException(e3, "RuntimeException thrown in postDeregister method: rethrowing <" + ((Object) e3) + ">, although the MBean is sucessfully unregistered");
        }
    }

    private DynamicMBean getMBean(ObjectName objectName) throws InstanceNotFoundException {
        if (objectName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Object name cannot be null"), "Exception occurred trying to get an MBean");
        }
        DynamicMBean dynamicMBeanRetrieve = this.repository.retrieve(objectName);
        if (dynamicMBeanRetrieve == null) {
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "getMBean", ((Object) objectName) + " : Found no object");
            }
            throw new InstanceNotFoundException(objectName.toString());
        }
        return dynamicMBeanRetrieve;
    }

    private static Object getResource(DynamicMBean dynamicMBean) {
        if (dynamicMBean instanceof DynamicMBean2) {
            return ((DynamicMBean2) dynamicMBean).getResource();
        }
        return dynamicMBean;
    }

    private ObjectName nonDefaultDomain(ObjectName objectName) {
        if (objectName == null || objectName.getDomain().length() > 0) {
            return objectName;
        }
        return Util.newObjectName(this.domain + ((Object) objectName));
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public String getDefaultDomain() {
        return this.domain;
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void addNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws IllegalArgumentException, InstanceNotFoundException {
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "addNotificationListener", "ObjectName = " + ((Object) objectName));
        }
        DynamicMBean mBean = getMBean(objectName);
        checkMBeanPermission(mBean, (String) null, objectName, "addNotificationListener");
        NotificationBroadcaster notificationBroadcaster = getNotificationBroadcaster(objectName, mBean, NotificationBroadcaster.class);
        if (notificationListener == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Null listener"), "Null listener");
        }
        notificationBroadcaster.addNotificationListener(getListenerWrapper(notificationListener, objectName, mBean, true), notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void addNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException {
        Object resource = getResource(getMBean(objectName2));
        if (!(resource instanceof NotificationListener)) {
            throw new RuntimeOperationsException(new IllegalArgumentException(objectName2.getCanonicalName()), "The MBean " + objectName2.getCanonicalName() + "does not implement the NotificationListener interface");
        }
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "addNotificationListener", "ObjectName = " + ((Object) objectName) + ", Listener = " + ((Object) objectName2));
        }
        this.server.addNotificationListener(objectName, (NotificationListener) resource, notificationFilter, obj);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener) throws ListenerNotFoundException, InstanceNotFoundException {
        removeNotificationListener(objectName, notificationListener, null, null, true);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, InstanceNotFoundException {
        removeNotificationListener(objectName, notificationListener, notificationFilter, obj, false);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2) throws ListenerNotFoundException, InstanceNotFoundException {
        NotificationListener listener = getListener(objectName2);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "removeNotificationListener", "ObjectName = " + ((Object) objectName) + ", Listener = " + ((Object) objectName2));
        }
        this.server.removeNotificationListener(objectName, listener);
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, InstanceNotFoundException {
        NotificationListener listener = getListener(objectName2);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "removeNotificationListener", "ObjectName = " + ((Object) objectName) + ", Listener = " + ((Object) objectName2));
        }
        this.server.removeNotificationListener(objectName, listener, notificationFilter, obj);
    }

    private NotificationListener getListener(ObjectName objectName) throws ListenerNotFoundException {
        try {
            Object resource = getResource(getMBean(objectName));
            if (!(resource instanceof NotificationListener)) {
                throw new RuntimeOperationsException(new IllegalArgumentException(objectName.getCanonicalName()), "MBean " + objectName.getCanonicalName() + " does not implement " + NotificationListener.class.getName());
            }
            return (NotificationListener) resource;
        } catch (InstanceNotFoundException e2) {
            throw ((ListenerNotFoundException) EnvHelp.initCause(new ListenerNotFoundException(e2.getMessage()), e2));
        }
    }

    private void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj, boolean z2) throws ListenerNotFoundException, InstanceNotFoundException {
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "removeNotificationListener", "ObjectName = " + ((Object) objectName));
        }
        DynamicMBean mBean = getMBean(objectName);
        checkMBeanPermission(mBean, (String) null, objectName, "removeNotificationListener");
        NotificationBroadcaster notificationBroadcaster = getNotificationBroadcaster(objectName, mBean, z2 ? NotificationBroadcaster.class : NotificationEmitter.class);
        NotificationListener listenerWrapper = getListenerWrapper(notificationListener, objectName, mBean, false);
        if (listenerWrapper == null) {
            throw new ListenerNotFoundException("Unknown listener");
        }
        if (z2) {
            notificationBroadcaster.removeNotificationListener(listenerWrapper);
        } else {
            ((NotificationEmitter) notificationBroadcaster).removeNotificationListener(listenerWrapper, notificationFilter, obj);
        }
    }

    private static <T extends NotificationBroadcaster> T getNotificationBroadcaster(ObjectName objectName, Object obj, Class<T> cls) {
        if (cls.isInstance(obj)) {
            return cls.cast(obj);
        }
        if (obj instanceof DynamicMBean2) {
            obj = ((DynamicMBean2) obj).getResource();
        }
        if (cls.isInstance(obj)) {
            return cls.cast(obj);
        }
        throw new RuntimeOperationsException(new IllegalArgumentException(objectName.getCanonicalName()), "MBean " + objectName.getCanonicalName() + " does not implement " + cls.getName());
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public MBeanInfo getMBeanInfo(ObjectName objectName) throws IntrospectionException, InstanceNotFoundException, ReflectionException {
        try {
            MBeanInfo mBeanInfo = getMBean(objectName).getMBeanInfo();
            if (mBeanInfo == null) {
                throw new JMRuntimeException("MBean " + ((Object) objectName) + "has no MBeanInfo");
            }
            checkMBeanPermission(mBeanInfo.getClassName(), (String) null, objectName, "getMBeanInfo");
            return mBeanInfo;
        } catch (Error e2) {
            throw new RuntimeErrorException(e2, "getMBeanInfo threw Error");
        } catch (RuntimeErrorException e3) {
            throw e3;
        } catch (RuntimeMBeanException e4) {
            throw e4;
        } catch (RuntimeException e5) {
            throw new RuntimeMBeanException(e5, "getMBeanInfo threw RuntimeException");
        }
    }

    @Override // javax.management.MBeanServer, javax.management.MBeanServerConnection
    public boolean isInstanceOf(ObjectName objectName, String str) throws InstanceNotFoundException {
        String name;
        DynamicMBean mBean = getMBean(objectName);
        checkMBeanPermission(mBean, (String) null, objectName, "isInstanceOf");
        try {
            Object resource = getResource(mBean);
            if (resource instanceof DynamicMBean) {
                name = getClassName((DynamicMBean) resource);
            } else {
                name = resource.getClass().getName();
            }
            String str2 = name;
            if (str2.equals(str)) {
                return true;
            }
            ClassLoader classLoader = resource.getClass().getClassLoader();
            Class<?> cls = Class.forName(str, false, classLoader);
            if (cls.isInstance(resource)) {
                return true;
            }
            return cls.isAssignableFrom(Class.forName(str2, false, classLoader));
        } catch (Exception e2) {
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, DefaultMBeanServerInterceptor.class.getName(), "isInstanceOf", "Exception calling isInstanceOf", (Throwable) e2);
                return false;
            }
            return false;
        }
    }

    @Override // javax.management.MBeanServer
    public ClassLoader getClassLoaderFor(ObjectName objectName) throws InstanceNotFoundException {
        DynamicMBean mBean = getMBean(objectName);
        checkMBeanPermission(mBean, (String) null, objectName, "getClassLoaderFor");
        return getResource(mBean).getClass().getClassLoader();
    }

    @Override // javax.management.MBeanServer
    public ClassLoader getClassLoader(ObjectName objectName) throws InstanceNotFoundException {
        if (objectName == null) {
            checkMBeanPermission((String) null, (String) null, (ObjectName) null, "getClassLoader");
            return this.server.getClass().getClassLoader();
        }
        DynamicMBean mBean = getMBean(objectName);
        checkMBeanPermission(mBean, (String) null, objectName, "getClassLoader");
        Object resource = getResource(mBean);
        if (!(resource instanceof ClassLoader)) {
            throw new InstanceNotFoundException(objectName.toString() + " is not a classloader");
        }
        return (ClassLoader) resource;
    }

    private void sendNotification(String str, ObjectName objectName) {
        MBeanServerNotification mBeanServerNotification = new MBeanServerNotification(str, MBeanServerDelegate.DELEGATE_NAME, 0L, objectName);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "sendNotification", str + " " + ((Object) objectName));
        }
        this.delegate.sendNotification(mBeanServerNotification);
    }

    private Set<ObjectName> objectNamesFromFilteredNamedObjects(Set<NamedObject> set, QueryExp queryExp) {
        boolean zApply;
        HashSet hashSet = new HashSet();
        if (queryExp == null) {
            Iterator<NamedObject> it = set.iterator();
            while (it.hasNext()) {
                hashSet.add(it.next().getName());
            }
        } else {
            MBeanServer mBeanServer = QueryEval.getMBeanServer();
            queryExp.setMBeanServer(this.server);
            try {
                for (NamedObject namedObject : set) {
                    try {
                        zApply = queryExp.apply(namedObject.getName());
                    } catch (Exception e2) {
                        zApply = false;
                    }
                    if (zApply) {
                        hashSet.add(namedObject.getName());
                    }
                }
            } finally {
                queryExp.setMBeanServer(mBeanServer);
            }
        }
        return hashSet;
    }

    private Set<ObjectInstance> objectInstancesFromFilteredNamedObjects(Set<NamedObject> set, QueryExp queryExp) {
        boolean zApply;
        HashSet hashSet = new HashSet();
        if (queryExp == null) {
            for (NamedObject namedObject : set) {
                hashSet.add(new ObjectInstance(namedObject.getName(), safeGetClassName(namedObject.getObject())));
            }
        } else {
            MBeanServer mBeanServer = QueryEval.getMBeanServer();
            queryExp.setMBeanServer(this.server);
            try {
                for (NamedObject namedObject2 : set) {
                    DynamicMBean object = namedObject2.getObject();
                    try {
                        zApply = queryExp.apply(namedObject2.getName());
                    } catch (Exception e2) {
                        zApply = false;
                    }
                    if (zApply) {
                        hashSet.add(new ObjectInstance(namedObject2.getName(), safeGetClassName(object)));
                    }
                }
            } finally {
                queryExp.setMBeanServer(mBeanServer);
            }
        }
        return hashSet;
    }

    private static String safeGetClassName(DynamicMBean dynamicMBean) {
        try {
            return getClassName(dynamicMBean);
        } catch (Exception e2) {
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, DefaultMBeanServerInterceptor.class.getName(), "safeGetClassName", "Exception getting MBean class name", (Throwable) e2);
                return null;
            }
            return null;
        }
    }

    private Set<ObjectInstance> filterListOfObjectInstances(Set<ObjectInstance> set, QueryExp queryExp) {
        boolean zApply;
        if (queryExp == null) {
            return set;
        }
        HashSet hashSet = new HashSet();
        for (ObjectInstance objectInstance : set) {
            MBeanServer mBeanServer = QueryEval.getMBeanServer();
            queryExp.setMBeanServer(this.server);
            try {
                zApply = queryExp.apply(objectInstance.getObjectName());
                queryExp.setMBeanServer(mBeanServer);
            } catch (Exception e2) {
                zApply = false;
                queryExp.setMBeanServer(mBeanServer);
            } catch (Throwable th) {
                queryExp.setMBeanServer(mBeanServer);
                throw th;
            }
            if (zApply) {
                hashSet.add(objectInstance);
            }
        }
        return hashSet;
    }

    private NotificationListener getListenerWrapper(NotificationListener notificationListener, ObjectName objectName, DynamicMBean dynamicMBean, boolean z2) {
        ListenerWrapper listenerWrapper;
        ListenerWrapper listenerWrapper2 = new ListenerWrapper(notificationListener, objectName, getResource(dynamicMBean));
        synchronized (this.listenerWrappers) {
            WeakReference<ListenerWrapper> weakReference = this.listenerWrappers.get(listenerWrapper2);
            if (weakReference != null && (listenerWrapper = weakReference.get()) != null) {
                return listenerWrapper;
            }
            if (z2) {
                this.listenerWrappers.put(listenerWrapper2, new WeakReference<>(listenerWrapper2));
                return listenerWrapper2;
            }
            return null;
        }
    }

    @Override // com.sun.jmx.interceptor.MBeanServerInterceptor, javax.management.MBeanServer
    public Object instantiate(String str) throws MBeanException, ReflectionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.jmx.interceptor.MBeanServerInterceptor, javax.management.MBeanServer
    public Object instantiate(String str, ObjectName objectName) throws MBeanException, InstanceNotFoundException, ReflectionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.jmx.interceptor.MBeanServerInterceptor, javax.management.MBeanServer
    public Object instantiate(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.jmx.interceptor.MBeanServerInterceptor, javax.management.MBeanServer
    public Object instantiate(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws MBeanException, InstanceNotFoundException, ReflectionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.jmx.interceptor.MBeanServerInterceptor, javax.management.MBeanServer
    public ObjectInputStream deserialize(ObjectName objectName, byte[] bArr) throws OperationsException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.jmx.interceptor.MBeanServerInterceptor, javax.management.MBeanServer
    public ObjectInputStream deserialize(String str, byte[] bArr) throws OperationsException, ReflectionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.jmx.interceptor.MBeanServerInterceptor, javax.management.MBeanServer
    public ObjectInputStream deserialize(String str, ObjectName objectName, byte[] bArr) throws OperationsException, ReflectionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.jmx.interceptor.MBeanServerInterceptor, javax.management.MBeanServer
    public ClassLoaderRepository getClassLoaderRepository() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* loaded from: rt.jar:com/sun/jmx/interceptor/DefaultMBeanServerInterceptor$ListenerWrapper.class */
    private static class ListenerWrapper implements NotificationListener {
        private NotificationListener listener;
        private ObjectName name;
        private Object mbean;

        ListenerWrapper(NotificationListener notificationListener, ObjectName objectName, Object obj) {
            this.listener = notificationListener;
            this.name = objectName;
            this.mbean = obj;
        }

        @Override // javax.management.NotificationListener
        public void handleNotification(Notification notification, Object obj) {
            if (notification != null && notification.getSource() == this.mbean) {
                notification.setSource(this.name);
            }
            this.listener.handleNotification(notification, obj);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ListenerWrapper)) {
                return false;
            }
            ListenerWrapper listenerWrapper = (ListenerWrapper) obj;
            return listenerWrapper.listener == this.listener && listenerWrapper.mbean == this.mbean && listenerWrapper.name.equals(this.name);
        }

        public int hashCode() {
            return System.identityHashCode(this.listener) ^ System.identityHashCode(this.mbean);
        }
    }

    private static String getClassName(DynamicMBean dynamicMBean) {
        if (dynamicMBean instanceof DynamicMBean2) {
            return ((DynamicMBean2) dynamicMBean).getClassName();
        }
        return dynamicMBean.getMBeanInfo().getClassName();
    }

    private static void checkMBeanPermission(DynamicMBean dynamicMBean, String str, ObjectName objectName, String str2) {
        if (System.getSecurityManager() != null) {
            checkMBeanPermission(safeGetClassName(dynamicMBean), str, objectName, str2);
        }
    }

    private static void checkMBeanPermission(String str, String str2, ObjectName objectName, String str3) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new MBeanPermission(str, str2, objectName, str3));
        }
    }

    private static void checkMBeanTrustPermission(final Class<?> cls) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new MBeanTrustPermission("register"), new AccessControlContext(new ProtectionDomain[]{(ProtectionDomain) AccessController.doPrivileged(new PrivilegedAction<ProtectionDomain>() { // from class: com.sun.jmx.interceptor.DefaultMBeanServerInterceptor.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public ProtectionDomain run2() {
                    return cls.getProtectionDomain();
                }
            })}));
        }
    }

    private ResourceContext registerWithRepository(Object obj, DynamicMBean dynamicMBean, ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException {
        ResourceContext resourceContextMakeResourceContextFor = makeResourceContextFor(obj, objectName);
        this.repository.addMBean(dynamicMBean, objectName, resourceContextMakeResourceContextFor);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "addObject", "Send create notification of object " + objectName.getCanonicalName());
        }
        sendNotification(MBeanServerNotification.REGISTRATION_NOTIFICATION, objectName);
        return resourceContextMakeResourceContextFor;
    }

    private ResourceContext unregisterFromRepository(Object obj, DynamicMBean dynamicMBean, ObjectName objectName) throws InstanceNotFoundException {
        ResourceContext resourceContextMakeResourceContextFor = makeResourceContextFor(obj, objectName);
        this.repository.remove(objectName, resourceContextMakeResourceContextFor);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "unregisterMBean", "Send delete notification of object " + objectName.getCanonicalName());
        }
        sendNotification(MBeanServerNotification.UNREGISTRATION_NOTIFICATION, objectName);
        return resourceContextMakeResourceContextFor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addClassLoader(ClassLoader classLoader, ObjectName objectName) {
        ModifiableClassLoaderRepository instantiatorCLR = getInstantiatorCLR();
        if (instantiatorCLR == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Dynamic addition of class loaders is not supported"), "Exception occurred trying to register the MBean as a class loader");
        }
        instantiatorCLR.addClassLoader(objectName, classLoader);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeClassLoader(ClassLoader classLoader, ObjectName objectName) {
        ModifiableClassLoaderRepository instantiatorCLR;
        if (classLoader != this.server.getClass().getClassLoader() && (instantiatorCLR = getInstantiatorCLR()) != null) {
            instantiatorCLR.removeClassLoader(objectName);
        }
    }

    private ResourceContext createClassLoaderContext(final ClassLoader classLoader, final ObjectName objectName) {
        return new ResourceContext() { // from class: com.sun.jmx.interceptor.DefaultMBeanServerInterceptor.2
            @Override // com.sun.jmx.mbeanserver.Repository.RegistrationContext
            public void registering() {
                DefaultMBeanServerInterceptor.this.addClassLoader(classLoader, objectName);
            }

            @Override // com.sun.jmx.mbeanserver.Repository.RegistrationContext
            public void unregistered() {
                DefaultMBeanServerInterceptor.this.removeClassLoader(classLoader, objectName);
            }

            @Override // com.sun.jmx.interceptor.DefaultMBeanServerInterceptor.ResourceContext
            public void done() {
            }
        };
    }

    private ResourceContext makeResourceContextFor(Object obj, ObjectName objectName) {
        if (obj instanceof ClassLoader) {
            return createClassLoaderContext((ClassLoader) obj, objectName);
        }
        return ResourceContext.NONE;
    }

    private ModifiableClassLoaderRepository getInstantiatorCLR() {
        return (ModifiableClassLoaderRepository) AccessController.doPrivileged(new PrivilegedAction<ModifiableClassLoaderRepository>() { // from class: com.sun.jmx.interceptor.DefaultMBeanServerInterceptor.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ModifiableClassLoaderRepository run2() {
                if (DefaultMBeanServerInterceptor.this.instantiator != null) {
                    return DefaultMBeanServerInterceptor.this.instantiator.getClassLoaderRepository();
                }
                return null;
            }
        });
    }
}
