package com.sun.jmx.mbeanserver;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.defaults.ServiceName;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/Repository.class */
public class Repository {
    private final Map<String, Map<String, NamedObject>> domainTb;
    private volatile int nbElements;
    private final String domain;
    private final ReentrantReadWriteLock lock;

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/Repository$RegistrationContext.class */
    public interface RegistrationContext {
        void registering();

        void unregistered();
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/Repository$ObjectNamePattern.class */
    private static final class ObjectNamePattern {
        private final String[] keys;
        private final String[] values;
        private final String properties;
        private final boolean isPropertyListPattern;
        private final boolean isPropertyValuePattern;
        public final ObjectName pattern;

        public ObjectNamePattern(ObjectName objectName) {
            this(objectName.isPropertyListPattern(), objectName.isPropertyValuePattern(), objectName.getCanonicalKeyPropertyListString(), objectName.getKeyPropertyList(), objectName);
        }

        ObjectNamePattern(boolean z2, boolean z3, String str, Map<String, String> map, ObjectName objectName) {
            this.isPropertyListPattern = z2;
            this.isPropertyValuePattern = z3;
            this.properties = str;
            int size = map.size();
            this.keys = new String[size];
            this.values = new String[size];
            int i2 = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                this.keys[i2] = entry.getKey();
                this.values[i2] = entry.getValue();
                i2++;
            }
            this.pattern = objectName;
        }

        public boolean matchKeys(ObjectName objectName) {
            if (this.isPropertyValuePattern && !this.isPropertyListPattern && objectName.getKeyPropertyList().size() != this.keys.length) {
                return false;
            }
            if (this.isPropertyValuePattern || this.isPropertyListPattern) {
                for (int length = this.keys.length - 1; length >= 0; length--) {
                    String keyProperty = objectName.getKeyProperty(this.keys[length]);
                    if (keyProperty == null) {
                        return false;
                    }
                    if (this.isPropertyValuePattern && this.pattern.isPropertyValuePattern(this.keys[length])) {
                        if (!Util.wildmatch(keyProperty, this.values[length])) {
                            return false;
                        }
                    } else if (!keyProperty.equals(this.values[length])) {
                        return false;
                    }
                }
                return true;
            }
            return objectName.getCanonicalKeyPropertyListString().equals(this.properties);
        }
    }

    private void addAllMatching(Map<String, NamedObject> map, Set<NamedObject> set, ObjectNamePattern objectNamePattern) {
        synchronized (map) {
            for (NamedObject namedObject : map.values()) {
                if (objectNamePattern.matchKeys(namedObject.getName())) {
                    set.add(namedObject);
                }
            }
        }
    }

    private void addNewDomMoi(DynamicMBean dynamicMBean, String str, ObjectName objectName, RegistrationContext registrationContext) {
        HashMap map = new HashMap();
        addMoiToTb(dynamicMBean, objectName, objectName.getCanonicalKeyPropertyListString(), map, registrationContext);
        this.domainTb.put(str, map);
        this.nbElements++;
    }

    private void registering(RegistrationContext registrationContext) {
        if (registrationContext == null) {
            return;
        }
        try {
            registrationContext.registering();
        } catch (RuntimeOperationsException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw new RuntimeOperationsException(e3);
        }
    }

    private void unregistering(RegistrationContext registrationContext, ObjectName objectName) {
        if (registrationContext == null) {
            return;
        }
        try {
            registrationContext.unregistered();
        } catch (Exception e2) {
            JmxProperties.MBEANSERVER_LOGGER.log(Level.FINE, "Unexpected exception while unregistering " + ((Object) objectName), (Throwable) e2);
        }
    }

    private void addMoiToTb(DynamicMBean dynamicMBean, ObjectName objectName, String str, Map<String, NamedObject> map, RegistrationContext registrationContext) {
        registering(registrationContext);
        map.put(str, new NamedObject(objectName, dynamicMBean));
    }

    private NamedObject retrieveNamedObject(ObjectName objectName) {
        if (objectName.isPattern()) {
            return null;
        }
        String strIntern = objectName.getDomain().intern();
        if (strIntern.length() == 0) {
            strIntern = this.domain;
        }
        Map<String, NamedObject> map = this.domainTb.get(strIntern);
        if (map == null) {
            return null;
        }
        return map.get(objectName.getCanonicalKeyPropertyListString());
    }

    public Repository(String str) {
        this(str, true);
    }

    public Repository(String str, boolean z2) {
        this.nbElements = 0;
        this.lock = new ReentrantReadWriteLock(z2);
        this.domainTb = new HashMap(5);
        if (str != null && str.length() != 0) {
            this.domain = str.intern();
        } else {
            this.domain = ServiceName.DOMAIN;
        }
        this.domainTb.put(this.domain, new HashMap());
    }

    public String[] getDomains() {
        this.lock.readLock().lock();
        try {
            ArrayList arrayList = new ArrayList(this.domainTb.size());
            for (Map.Entry<String, Map<String, NamedObject>> entry : this.domainTb.entrySet()) {
                Map<String, NamedObject> value = entry.getValue();
                if (value != null && value.size() != 0) {
                    arrayList.add(entry.getKey());
                }
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void addMBean(DynamicMBean dynamicMBean, ObjectName objectName, RegistrationContext registrationContext) throws InstanceAlreadyExistsException {
        boolean z2;
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, Repository.class.getName(), "addMBean", "name = " + ((Object) objectName));
        }
        String strIntern = objectName.getDomain().intern();
        if (strIntern.length() == 0) {
            objectName = Util.newObjectName(this.domain + objectName.toString());
        }
        if (strIntern == this.domain) {
            z2 = true;
            strIntern = this.domain;
        } else {
            z2 = false;
        }
        if (objectName.isPattern()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Repository: cannot add mbean for pattern name " + objectName.toString()));
        }
        this.lock.writeLock().lock();
        if (!z2) {
            try {
                if (strIntern.equals("JMImplementation") && this.domainTb.containsKey("JMImplementation")) {
                    throw new RuntimeOperationsException(new IllegalArgumentException("Repository: domain name cannot be JMImplementation"));
                }
            } catch (Throwable th) {
                this.lock.writeLock().unlock();
                throw th;
            }
        }
        Map<String, NamedObject> map = this.domainTb.get(strIntern);
        if (map == null) {
            addNewDomMoi(dynamicMBean, strIntern, objectName, registrationContext);
            this.lock.writeLock().unlock();
            return;
        }
        String canonicalKeyPropertyListString = objectName.getCanonicalKeyPropertyListString();
        if (map.get(canonicalKeyPropertyListString) != null) {
            throw new InstanceAlreadyExistsException(objectName.toString());
        }
        this.nbElements++;
        addMoiToTb(dynamicMBean, objectName, canonicalKeyPropertyListString, map, registrationContext);
        this.lock.writeLock().unlock();
    }

    public boolean contains(ObjectName objectName) {
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, Repository.class.getName(), Keywords.FUNC_CONTAINS_STRING, " name = " + ((Object) objectName));
        }
        this.lock.readLock().lock();
        try {
            return retrieveNamedObject(objectName) != null;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public DynamicMBean retrieve(ObjectName objectName) {
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, Repository.class.getName(), "retrieve", "name = " + ((Object) objectName));
        }
        this.lock.readLock().lock();
        try {
            NamedObject namedObjectRetrieveNamedObject = retrieveNamedObject(objectName);
            if (namedObjectRetrieveNamedObject == null) {
                return null;
            }
            DynamicMBean object = namedObjectRetrieveNamedObject.getObject();
            this.lock.readLock().unlock();
            return object;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public Set<NamedObject> query(ObjectName objectName, QueryExp queryExp) {
        HashSet hashSet = new HashSet();
        ObjectName objectName2 = (objectName == null || objectName.getCanonicalName().length() == 0 || objectName.equals(ObjectName.WILDCARD)) ? ObjectName.WILDCARD : objectName;
        this.lock.readLock().lock();
        try {
            if (!objectName2.isPattern()) {
                NamedObject namedObjectRetrieveNamedObject = retrieveNamedObject(objectName2);
                if (namedObjectRetrieveNamedObject != null) {
                    hashSet.add(namedObjectRetrieveNamedObject);
                }
                return hashSet;
            }
            if (objectName2 == ObjectName.WILDCARD) {
                Iterator<Map<String, NamedObject>> it = this.domainTb.values().iterator();
                while (it.hasNext()) {
                    hashSet.addAll(it.next().values());
                }
                this.lock.readLock().unlock();
                return hashSet;
            }
            boolean z2 = objectName2.getCanonicalKeyPropertyListString().length() == 0;
            ObjectNamePattern objectNamePattern = z2 ? null : new ObjectNamePattern(objectName2);
            if (objectName2.getDomain().length() == 0) {
                Map<String, NamedObject> map = this.domainTb.get(this.domain);
                if (z2) {
                    hashSet.addAll(map.values());
                } else {
                    addAllMatching(map, hashSet, objectNamePattern);
                }
                this.lock.readLock().unlock();
                return hashSet;
            }
            if (!objectName2.isDomainPattern()) {
                Map<String, NamedObject> map2 = this.domainTb.get(objectName2.getDomain());
                if (map2 == null) {
                    Set<NamedObject> setEmptySet = Collections.emptySet();
                    this.lock.readLock().unlock();
                    return setEmptySet;
                }
                if (z2) {
                    hashSet.addAll(map2.values());
                } else {
                    addAllMatching(map2, hashSet, objectNamePattern);
                }
                this.lock.readLock().unlock();
                return hashSet;
            }
            String domain = objectName2.getDomain();
            for (String str : this.domainTb.keySet()) {
                if (Util.wildmatch(str, domain)) {
                    Map<String, NamedObject> map3 = this.domainTb.get(str);
                    if (z2) {
                        hashSet.addAll(map3.values());
                    } else {
                        addAllMatching(map3, hashSet, objectNamePattern);
                    }
                }
            }
            this.lock.readLock().unlock();
            return hashSet;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void remove(ObjectName objectName, RegistrationContext registrationContext) throws InstanceNotFoundException {
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, Repository.class.getName(), "remove", "name = " + ((Object) objectName));
        }
        String strIntern = objectName.getDomain().intern();
        if (strIntern.length() == 0) {
            strIntern = this.domain;
        }
        this.lock.writeLock().lock();
        try {
            Map<String, NamedObject> map = this.domainTb.get(strIntern);
            if (map == null) {
                throw new InstanceNotFoundException(objectName.toString());
            }
            if (map.remove(objectName.getCanonicalKeyPropertyListString()) == null) {
                throw new InstanceNotFoundException(objectName.toString());
            }
            this.nbElements--;
            if (map.isEmpty()) {
                this.domainTb.remove(strIntern);
                if (strIntern == this.domain) {
                    this.domainTb.put(this.domain, new HashMap());
                }
            }
            unregistering(registrationContext, objectName);
            this.lock.writeLock().unlock();
        } catch (Throwable th) {
            this.lock.writeLock().unlock();
            throw th;
        }
    }

    public Integer getCount() {
        return Integer.valueOf(this.nbElements);
    }

    public String getDefaultDomain() {
        return this.domain;
    }
}
