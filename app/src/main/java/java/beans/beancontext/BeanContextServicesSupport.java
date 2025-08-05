package java.beans.beancontext;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.beans.beancontext.BeanContextSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TooManyListenersException;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextServicesSupport.class */
public class BeanContextServicesSupport extends BeanContextSupport implements BeanContextServices {
    private static final long serialVersionUID = -8494482757288719206L;
    protected transient HashMap services;
    protected transient int serializable;
    protected transient BCSSProxyServiceProvider proxy;
    protected transient ArrayList bcsListeners;

    public BeanContextServicesSupport(BeanContextServices beanContextServices, Locale locale, boolean z2, boolean z3) {
        super(beanContextServices, locale, z2, z3);
        this.serializable = 0;
    }

    public BeanContextServicesSupport(BeanContextServices beanContextServices, Locale locale, boolean z2) {
        this(beanContextServices, locale, z2, true);
    }

    public BeanContextServicesSupport(BeanContextServices beanContextServices, Locale locale) {
        this(beanContextServices, locale, false, true);
    }

    public BeanContextServicesSupport(BeanContextServices beanContextServices) {
        this(beanContextServices, null, false, true);
    }

    public BeanContextServicesSupport() {
        this(null, null, false, true);
    }

    @Override // java.beans.beancontext.BeanContextSupport
    public void initialize() {
        super.initialize();
        this.services = new HashMap(this.serializable + 1);
        this.bcsListeners = new ArrayList(1);
    }

    public BeanContextServices getBeanContextServicesPeer() {
        return (BeanContextServices) getBeanContextChildPeer();
    }

    /* loaded from: rt.jar:java/beans/beancontext/BeanContextServicesSupport$BCSSChild.class */
    protected class BCSSChild extends BeanContextSupport.BCSChild {
        private static final long serialVersionUID = -3263851306889194873L;
        private transient HashMap serviceClasses;
        private transient HashMap serviceRequestors;

        /* loaded from: rt.jar:java/beans/beancontext/BeanContextServicesSupport$BCSSChild$BCSSCServiceClassRef.class */
        class BCSSCServiceClassRef {
            Class serviceClass;
            BeanContextServiceProvider serviceProvider;
            int serviceRefs;
            BeanContextServiceProvider delegateProvider;
            int delegateRefs;
            HashMap requestors = new HashMap(1);

            BCSSCServiceClassRef(Class cls, BeanContextServiceProvider beanContextServiceProvider, boolean z2) {
                this.serviceClass = cls;
                if (z2) {
                    this.delegateProvider = beanContextServiceProvider;
                } else {
                    this.serviceProvider = beanContextServiceProvider;
                }
            }

            void addRequestor(Object obj, BeanContextServiceRevokedListener beanContextServiceRevokedListener) throws TooManyListenersException {
                BeanContextServiceRevokedListener beanContextServiceRevokedListener2 = (BeanContextServiceRevokedListener) this.requestors.get(obj);
                if (beanContextServiceRevokedListener2 != null && !beanContextServiceRevokedListener2.equals(beanContextServiceRevokedListener)) {
                    throw new TooManyListenersException();
                }
                this.requestors.put(obj, beanContextServiceRevokedListener);
            }

            void removeRequestor(Object obj) {
                this.requestors.remove(obj);
            }

            void verifyRequestor(Object obj, BeanContextServiceRevokedListener beanContextServiceRevokedListener) throws TooManyListenersException {
                BeanContextServiceRevokedListener beanContextServiceRevokedListener2 = (BeanContextServiceRevokedListener) this.requestors.get(obj);
                if (beanContextServiceRevokedListener2 != null && !beanContextServiceRevokedListener2.equals(beanContextServiceRevokedListener)) {
                    throw new TooManyListenersException();
                }
            }

            void verifyAndMaybeSetProvider(BeanContextServiceProvider beanContextServiceProvider, boolean z2) {
                BeanContextServiceProvider beanContextServiceProvider2;
                if (z2) {
                    beanContextServiceProvider2 = this.delegateProvider;
                    if (beanContextServiceProvider2 == null || beanContextServiceProvider == null) {
                        this.delegateProvider = beanContextServiceProvider;
                        return;
                    }
                } else {
                    beanContextServiceProvider2 = this.serviceProvider;
                    if (beanContextServiceProvider2 == null || beanContextServiceProvider == null) {
                        this.serviceProvider = beanContextServiceProvider;
                        return;
                    }
                }
                if (!beanContextServiceProvider2.equals(beanContextServiceProvider)) {
                    throw new UnsupportedOperationException("existing service reference obtained from different BeanContextServiceProvider not supported");
                }
            }

            Iterator cloneOfEntries() {
                return ((HashMap) this.requestors.clone()).entrySet().iterator();
            }

            Iterator entries() {
                return this.requestors.entrySet().iterator();
            }

            boolean isEmpty() {
                return this.requestors.isEmpty();
            }

            Class getServiceClass() {
                return this.serviceClass;
            }

            BeanContextServiceProvider getServiceProvider() {
                return this.serviceProvider;
            }

            BeanContextServiceProvider getDelegateProvider() {
                return this.delegateProvider;
            }

            boolean isDelegated() {
                return this.delegateProvider != null;
            }

            void addRef(boolean z2) {
                if (z2) {
                    this.delegateRefs++;
                } else {
                    this.serviceRefs++;
                }
            }

            void releaseRef(boolean z2) {
                if (z2) {
                    int i2 = this.delegateRefs - 1;
                    this.delegateRefs = i2;
                    if (i2 == 0) {
                        this.delegateProvider = null;
                        return;
                    }
                    return;
                }
                int i3 = this.serviceRefs - 1;
                this.serviceRefs = i3;
                if (i3 <= 0) {
                    this.serviceProvider = null;
                }
            }

            int getRefs() {
                return this.serviceRefs + this.delegateRefs;
            }

            int getDelegateRefs() {
                return this.delegateRefs;
            }

            int getServiceRefs() {
                return this.serviceRefs;
            }
        }

        /* loaded from: rt.jar:java/beans/beancontext/BeanContextServicesSupport$BCSSChild$BCSSCServiceRef.class */
        class BCSSCServiceRef {
            BCSSCServiceClassRef serviceClassRef;
            int refCnt = 1;
            boolean delegated;

            BCSSCServiceRef(BCSSCServiceClassRef bCSSCServiceClassRef, boolean z2) {
                this.delegated = false;
                this.serviceClassRef = bCSSCServiceClassRef;
                this.delegated = z2;
            }

            void addRef() {
                this.refCnt++;
            }

            int release() {
                int i2 = this.refCnt - 1;
                this.refCnt = i2;
                return i2;
            }

            BCSSCServiceClassRef getServiceClassRef() {
                return this.serviceClassRef;
            }

            boolean isDelegated() {
                return this.delegated;
            }
        }

        BCSSChild(Object obj, Object obj2) {
            super(obj, obj2);
        }

        synchronized void usingService(Object obj, Object obj2, Class cls, BeanContextServiceProvider beanContextServiceProvider, boolean z2, BeanContextServiceRevokedListener beanContextServiceRevokedListener) throws UnsupportedOperationException, TooManyListenersException {
            BCSSCServiceClassRef bCSSCServiceClassRef = null;
            if (this.serviceClasses == null) {
                this.serviceClasses = new HashMap(1);
            } else {
                bCSSCServiceClassRef = (BCSSCServiceClassRef) this.serviceClasses.get(cls);
            }
            if (bCSSCServiceClassRef == null) {
                bCSSCServiceClassRef = new BCSSCServiceClassRef(cls, beanContextServiceProvider, z2);
                this.serviceClasses.put(cls, bCSSCServiceClassRef);
            } else {
                bCSSCServiceClassRef.verifyAndMaybeSetProvider(beanContextServiceProvider, z2);
                bCSSCServiceClassRef.verifyRequestor(obj, beanContextServiceRevokedListener);
            }
            bCSSCServiceClassRef.addRequestor(obj, beanContextServiceRevokedListener);
            bCSSCServiceClassRef.addRef(z2);
            BCSSCServiceRef bCSSCServiceRef = null;
            Map map = null;
            if (this.serviceRequestors == null) {
                this.serviceRequestors = new HashMap(1);
            } else {
                map = (Map) this.serviceRequestors.get(obj);
            }
            if (map == null) {
                map = new HashMap(1);
                this.serviceRequestors.put(obj, map);
            } else {
                bCSSCServiceRef = (BCSSCServiceRef) map.get(obj2);
            }
            if (bCSSCServiceRef == null) {
                map.put(obj2, new BCSSCServiceRef(bCSSCServiceClassRef, z2));
            } else {
                bCSSCServiceRef.addRef();
            }
        }

        synchronized void releaseService(Object obj, Object obj2) {
            Map map;
            BCSSCServiceRef bCSSCServiceRef;
            if (this.serviceRequestors == null || (map = (Map) this.serviceRequestors.get(obj)) == null || (bCSSCServiceRef = (BCSSCServiceRef) map.get(obj2)) == null) {
                return;
            }
            BCSSCServiceClassRef serviceClassRef = bCSSCServiceRef.getServiceClassRef();
            boolean zIsDelegated = bCSSCServiceRef.isDelegated();
            (zIsDelegated ? serviceClassRef.getDelegateProvider() : serviceClassRef.getServiceProvider()).releaseService(BeanContextServicesSupport.this.getBeanContextServicesPeer(), obj, obj2);
            serviceClassRef.releaseRef(zIsDelegated);
            serviceClassRef.removeRequestor(obj);
            if (bCSSCServiceRef.release() == 0) {
                map.remove(obj2);
                if (map.isEmpty()) {
                    this.serviceRequestors.remove(obj);
                    serviceClassRef.removeRequestor(obj);
                }
                if (this.serviceRequestors.isEmpty()) {
                    this.serviceRequestors = null;
                }
                if (serviceClassRef.isEmpty()) {
                    this.serviceClasses.remove(serviceClassRef.getServiceClass());
                }
                if (this.serviceClasses.isEmpty()) {
                    this.serviceClasses = null;
                }
            }
        }

        synchronized void revokeService(Class cls, boolean z2, boolean z3) {
            BCSSCServiceClassRef bCSSCServiceClassRef;
            if (this.serviceClasses == null || (bCSSCServiceClassRef = (BCSSCServiceClassRef) this.serviceClasses.get(cls)) == null) {
                return;
            }
            Iterator itCloneOfEntries = bCSSCServiceClassRef.cloneOfEntries();
            BeanContextServiceRevokedEvent beanContextServiceRevokedEvent = new BeanContextServiceRevokedEvent(BeanContextServicesSupport.this.getBeanContextServicesPeer(), cls, z3);
            boolean z4 = false;
            while (itCloneOfEntries.hasNext() && this.serviceRequestors != null) {
                Map.Entry entry = (Map.Entry) itCloneOfEntries.next();
                BeanContextServiceRevokedListener beanContextServiceRevokedListener = (BeanContextServiceRevokedListener) entry.getValue();
                if (z3) {
                    Object key = entry.getKey();
                    Map map = (Map) this.serviceRequestors.get(key);
                    if (map != null) {
                        Iterator it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            BCSSCServiceRef bCSSCServiceRef = (BCSSCServiceRef) ((Map.Entry) it.next()).getValue();
                            if (bCSSCServiceRef.getServiceClassRef().equals(bCSSCServiceClassRef) && z2 == bCSSCServiceRef.isDelegated()) {
                                it.remove();
                            }
                        }
                        boolean zIsEmpty = map.isEmpty();
                        z4 = zIsEmpty;
                        if (zIsEmpty) {
                            this.serviceRequestors.remove(key);
                        }
                    }
                    if (z4) {
                        bCSSCServiceClassRef.removeRequestor(key);
                    }
                }
                beanContextServiceRevokedListener.serviceRevoked(beanContextServiceRevokedEvent);
            }
            if (z3 && this.serviceClasses != null) {
                if (bCSSCServiceClassRef.isEmpty()) {
                    this.serviceClasses.remove(cls);
                }
                if (this.serviceClasses.isEmpty()) {
                    this.serviceClasses = null;
                }
            }
            if (this.serviceRequestors != null && this.serviceRequestors.isEmpty()) {
                this.serviceRequestors = null;
            }
        }

        void cleanupReferences() {
            if (this.serviceRequestors == null) {
                return;
            }
            Iterator it = this.serviceRequestors.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                Iterator it2 = ((Map) entry.getValue()).entrySet().iterator();
                it.remove();
                while (it2.hasNext()) {
                    Map.Entry entry2 = (Map.Entry) it2.next();
                    Object key2 = entry2.getKey();
                    BCSSCServiceRef bCSSCServiceRef = (BCSSCServiceRef) entry2.getValue();
                    BCSSCServiceClassRef serviceClassRef = bCSSCServiceRef.getServiceClassRef();
                    BeanContextServiceProvider delegateProvider = bCSSCServiceRef.isDelegated() ? serviceClassRef.getDelegateProvider() : serviceClassRef.getServiceProvider();
                    serviceClassRef.removeRequestor(key);
                    it2.remove();
                    while (bCSSCServiceRef.release() >= 0) {
                        delegateProvider.releaseService(BeanContextServicesSupport.this.getBeanContextServicesPeer(), key, key2);
                    }
                }
            }
            this.serviceRequestors = null;
            this.serviceClasses = null;
        }

        void revokeAllDelegatedServicesNow() {
            if (this.serviceClasses == null) {
                return;
            }
            Iterator it = new HashSet(this.serviceClasses.values()).iterator();
            while (it.hasNext()) {
                BCSSCServiceClassRef bCSSCServiceClassRef = (BCSSCServiceClassRef) it.next();
                if (bCSSCServiceClassRef.isDelegated()) {
                    Iterator itCloneOfEntries = bCSSCServiceClassRef.cloneOfEntries();
                    BeanContextServiceRevokedEvent beanContextServiceRevokedEvent = new BeanContextServiceRevokedEvent(BeanContextServicesSupport.this.getBeanContextServicesPeer(), bCSSCServiceClassRef.getServiceClass(), true);
                    boolean z2 = false;
                    while (itCloneOfEntries.hasNext()) {
                        Map.Entry entry = (Map.Entry) itCloneOfEntries.next();
                        BeanContextServiceRevokedListener beanContextServiceRevokedListener = (BeanContextServiceRevokedListener) entry.getValue();
                        Object key = entry.getKey();
                        Map map = (Map) this.serviceRequestors.get(key);
                        if (map != null) {
                            Iterator it2 = map.entrySet().iterator();
                            while (it2.hasNext()) {
                                BCSSCServiceRef bCSSCServiceRef = (BCSSCServiceRef) ((Map.Entry) it2.next()).getValue();
                                if (bCSSCServiceRef.getServiceClassRef().equals(bCSSCServiceClassRef) && bCSSCServiceRef.isDelegated()) {
                                    it2.remove();
                                }
                            }
                            boolean zIsEmpty = map.isEmpty();
                            z2 = zIsEmpty;
                            if (zIsEmpty) {
                                this.serviceRequestors.remove(key);
                            }
                        }
                        if (z2) {
                            bCSSCServiceClassRef.removeRequestor(key);
                        }
                        beanContextServiceRevokedListener.serviceRevoked(beanContextServiceRevokedEvent);
                        if (bCSSCServiceClassRef.isEmpty()) {
                            this.serviceClasses.remove(bCSSCServiceClassRef.getServiceClass());
                        }
                    }
                }
            }
            if (this.serviceClasses.isEmpty()) {
                this.serviceClasses = null;
            }
            if (this.serviceRequestors != null && this.serviceRequestors.isEmpty()) {
                this.serviceRequestors = null;
            }
        }
    }

    @Override // java.beans.beancontext.BeanContextSupport
    protected BeanContextSupport.BCSChild createBCSChild(Object obj, Object obj2) {
        return new BCSSChild(obj, obj2);
    }

    /* loaded from: rt.jar:java/beans/beancontext/BeanContextServicesSupport$BCSSServiceProvider.class */
    protected static class BCSSServiceProvider implements Serializable {
        private static final long serialVersionUID = 861278251667444782L;
        protected BeanContextServiceProvider serviceProvider;

        BCSSServiceProvider(Class cls, BeanContextServiceProvider beanContextServiceProvider) {
            this.serviceProvider = beanContextServiceProvider;
        }

        protected BeanContextServiceProvider getServiceProvider() {
            return this.serviceProvider;
        }
    }

    protected BCSSServiceProvider createBCSSServiceProvider(Class cls, BeanContextServiceProvider beanContextServiceProvider) {
        return new BCSSServiceProvider(cls, beanContextServiceProvider);
    }

    @Override // java.beans.beancontext.BeanContextServices
    public void addBeanContextServicesListener(BeanContextServicesListener beanContextServicesListener) {
        if (beanContextServicesListener == null) {
            throw new NullPointerException("bcsl");
        }
        synchronized (this.bcsListeners) {
            if (this.bcsListeners.contains(beanContextServicesListener)) {
                return;
            }
            this.bcsListeners.add(beanContextServicesListener);
        }
    }

    @Override // java.beans.beancontext.BeanContextServices
    public void removeBeanContextServicesListener(BeanContextServicesListener beanContextServicesListener) {
        if (beanContextServicesListener == null) {
            throw new NullPointerException("bcsl");
        }
        synchronized (this.bcsListeners) {
            if (this.bcsListeners.contains(beanContextServicesListener)) {
                this.bcsListeners.remove(beanContextServicesListener);
            }
        }
    }

    @Override // java.beans.beancontext.BeanContextServices
    public boolean addService(Class cls, BeanContextServiceProvider beanContextServiceProvider) {
        return addService(cls, beanContextServiceProvider, true);
    }

    protected boolean addService(Class cls, BeanContextServiceProvider beanContextServiceProvider, boolean z2) {
        if (cls == null) {
            throw new NullPointerException("serviceClass");
        }
        if (beanContextServiceProvider == null) {
            throw new NullPointerException("bcsp");
        }
        synchronized (BeanContext.globalHierarchyLock) {
            if (this.services.containsKey(cls)) {
                return false;
            }
            this.services.put(cls, createBCSSServiceProvider(cls, beanContextServiceProvider));
            if (beanContextServiceProvider instanceof Serializable) {
                this.serializable++;
            }
            if (!z2) {
                return true;
            }
            BeanContextServiceAvailableEvent beanContextServiceAvailableEvent = new BeanContextServiceAvailableEvent(getBeanContextServicesPeer(), cls);
            fireServiceAdded(beanContextServiceAvailableEvent);
            synchronized (this.children) {
                for (Object obj : this.children.keySet()) {
                    if (obj instanceof BeanContextServices) {
                        ((BeanContextServicesListener) obj).serviceAvailable(beanContextServiceAvailableEvent);
                    }
                }
            }
            return true;
        }
    }

    @Override // java.beans.beancontext.BeanContextServices
    public void revokeService(Class cls, BeanContextServiceProvider beanContextServiceProvider, boolean z2) {
        if (cls == null) {
            throw new NullPointerException("serviceClass");
        }
        if (beanContextServiceProvider == null) {
            throw new NullPointerException("bcsp");
        }
        synchronized (BeanContext.globalHierarchyLock) {
            if (this.services.containsKey(cls)) {
                if (!((BCSSServiceProvider) this.services.get(cls)).getServiceProvider().equals(beanContextServiceProvider)) {
                    throw new IllegalArgumentException("service provider mismatch");
                }
                this.services.remove(cls);
                if (beanContextServiceProvider instanceof Serializable) {
                    this.serializable--;
                }
                Iterator itBcsChildren = bcsChildren();
                while (itBcsChildren.hasNext()) {
                    ((BCSSChild) itBcsChildren.next()).revokeService(cls, false, z2);
                }
                fireServiceRevoked(cls, z2);
            }
        }
    }

    @Override // java.beans.beancontext.BeanContextServices
    public synchronized boolean hasService(Class cls) {
        if (cls == null) {
            throw new NullPointerException("serviceClass");
        }
        synchronized (BeanContext.globalHierarchyLock) {
            if (this.services.containsKey(cls)) {
                return true;
            }
            try {
                BeanContextServices beanContextServices = (BeanContextServices) getBeanContext();
                return beanContextServices == null ? false : beanContextServices.hasService(cls);
            } catch (ClassCastException e2) {
                return false;
            }
        }
    }

    /* loaded from: rt.jar:java/beans/beancontext/BeanContextServicesSupport$BCSSProxyServiceProvider.class */
    protected class BCSSProxyServiceProvider implements BeanContextServiceProvider, BeanContextServiceRevokedListener {
        private BeanContextServices nestingCtxt;

        BCSSProxyServiceProvider(BeanContextServices beanContextServices) {
            this.nestingCtxt = beanContextServices;
        }

        @Override // java.beans.beancontext.BeanContextServiceProvider
        public Object getService(BeanContextServices beanContextServices, Object obj, Class cls, Object obj2) {
            try {
                return this.nestingCtxt.getService(beanContextServices, obj, cls, obj2, this);
            } catch (TooManyListenersException e2) {
                return null;
            }
        }

        @Override // java.beans.beancontext.BeanContextServiceProvider
        public void releaseService(BeanContextServices beanContextServices, Object obj, Object obj2) {
            this.nestingCtxt.releaseService(beanContextServices, obj, obj2);
        }

        @Override // java.beans.beancontext.BeanContextServiceProvider
        public Iterator getCurrentServiceSelectors(BeanContextServices beanContextServices, Class cls) {
            return this.nestingCtxt.getCurrentServiceSelectors(cls);
        }

        @Override // java.beans.beancontext.BeanContextServiceRevokedListener
        public void serviceRevoked(BeanContextServiceRevokedEvent beanContextServiceRevokedEvent) {
            Iterator itBcsChildren = BeanContextServicesSupport.this.bcsChildren();
            while (itBcsChildren.hasNext()) {
                ((BCSSChild) itBcsChildren.next()).revokeService(beanContextServiceRevokedEvent.getServiceClass(), true, beanContextServiceRevokedEvent.isCurrentServiceInvalidNow());
            }
        }
    }

    @Override // java.beans.beancontext.BeanContextServices
    public Object getService(BeanContextChild beanContextChild, Object obj, Class cls, Object obj2, BeanContextServiceRevokedListener beanContextServiceRevokedListener) throws TooManyListenersException {
        BCSSChild bCSSChild;
        Object service;
        BeanContextServiceProvider serviceProvider;
        Object service2;
        if (beanContextChild == null) {
            throw new NullPointerException("child");
        }
        if (cls == null) {
            throw new NullPointerException("serviceClass");
        }
        if (obj == null) {
            throw new NullPointerException("requestor");
        }
        if (beanContextServiceRevokedListener == null) {
            throw new NullPointerException("bcsrl");
        }
        BeanContextServices beanContextServicesPeer = getBeanContextServicesPeer();
        synchronized (BeanContext.globalHierarchyLock) {
            synchronized (this.children) {
                bCSSChild = (BCSSChild) this.children.get(beanContextChild);
            }
            if (bCSSChild == null) {
                throw new IllegalArgumentException("not a child of this context");
            }
            BCSSServiceProvider bCSSServiceProvider = (BCSSServiceProvider) this.services.get(cls);
            if (bCSSServiceProvider != null && (service2 = (serviceProvider = bCSSServiceProvider.getServiceProvider()).getService(beanContextServicesPeer, obj, cls, obj2)) != null) {
                try {
                    bCSSChild.usingService(obj, service2, cls, serviceProvider, false, beanContextServiceRevokedListener);
                    return service2;
                } catch (UnsupportedOperationException e2) {
                    serviceProvider.releaseService(beanContextServicesPeer, obj, service2);
                    throw e2;
                } catch (TooManyListenersException e3) {
                    serviceProvider.releaseService(beanContextServicesPeer, obj, service2);
                    throw e3;
                }
            }
            if (this.proxy != null && (service = this.proxy.getService(beanContextServicesPeer, obj, cls, obj2)) != null) {
                try {
                    bCSSChild.usingService(obj, service, cls, this.proxy, true, beanContextServiceRevokedListener);
                    return service;
                } catch (UnsupportedOperationException e4) {
                    this.proxy.releaseService(beanContextServicesPeer, obj, service);
                    throw e4;
                } catch (TooManyListenersException e5) {
                    this.proxy.releaseService(beanContextServicesPeer, obj, service);
                    throw e5;
                }
            }
            return null;
        }
    }

    @Override // java.beans.beancontext.BeanContextServices
    public void releaseService(BeanContextChild beanContextChild, Object obj, Object obj2) {
        BCSSChild bCSSChild;
        if (beanContextChild == null) {
            throw new NullPointerException("child");
        }
        if (obj == null) {
            throw new NullPointerException("requestor");
        }
        if (obj2 == null) {
            throw new NullPointerException(DeploymentDescriptorParser.ATTR_SERVICE);
        }
        synchronized (BeanContext.globalHierarchyLock) {
            synchronized (this.children) {
                bCSSChild = (BCSSChild) this.children.get(beanContextChild);
            }
            if (bCSSChild != null) {
                bCSSChild.releaseService(obj, obj2);
            } else {
                throw new IllegalArgumentException("child actual is not a child of this BeanContext");
            }
        }
    }

    @Override // java.beans.beancontext.BeanContextServices
    public Iterator getCurrentServiceClasses() {
        return new BeanContextSupport.BCSIterator(this.services.keySet().iterator());
    }

    @Override // java.beans.beancontext.BeanContextServices
    public Iterator getCurrentServiceSelectors(Class cls) {
        BCSSServiceProvider bCSSServiceProvider = (BCSSServiceProvider) this.services.get(cls);
        if (bCSSServiceProvider != null) {
            return new BeanContextSupport.BCSIterator(bCSSServiceProvider.getServiceProvider().getCurrentServiceSelectors(getBeanContextServicesPeer(), cls));
        }
        return null;
    }

    @Override // java.beans.beancontext.BeanContextChildSupport, java.beans.beancontext.BeanContextServicesListener
    public void serviceAvailable(BeanContextServiceAvailableEvent beanContextServiceAvailableEvent) {
        synchronized (BeanContext.globalHierarchyLock) {
            if (this.services.containsKey(beanContextServiceAvailableEvent.getServiceClass())) {
                return;
            }
            fireServiceAdded(beanContextServiceAvailableEvent);
            synchronized (this.children) {
            }
            for (Object obj : this.children.keySet()) {
                if (obj instanceof BeanContextServices) {
                    ((BeanContextServicesListener) obj).serviceAvailable(beanContextServiceAvailableEvent);
                }
            }
        }
    }

    @Override // java.beans.beancontext.BeanContextChildSupport, java.beans.beancontext.BeanContextServiceRevokedListener
    public void serviceRevoked(BeanContextServiceRevokedEvent beanContextServiceRevokedEvent) {
        synchronized (BeanContext.globalHierarchyLock) {
            if (this.services.containsKey(beanContextServiceRevokedEvent.getServiceClass())) {
                return;
            }
            fireServiceRevoked(beanContextServiceRevokedEvent);
            synchronized (this.children) {
            }
            for (Object obj : this.children.keySet()) {
                if (obj instanceof BeanContextServices) {
                    ((BeanContextServicesListener) obj).serviceRevoked(beanContextServiceRevokedEvent);
                }
            }
        }
    }

    protected static final BeanContextServicesListener getChildBeanContextServicesListener(Object obj) {
        try {
            return (BeanContextServicesListener) obj;
        } catch (ClassCastException e2) {
            return null;
        }
    }

    @Override // java.beans.beancontext.BeanContextSupport
    protected void childJustRemovedHook(Object obj, BeanContextSupport.BCSChild bCSChild) {
        ((BCSSChild) bCSChild).cleanupReferences();
    }

    @Override // java.beans.beancontext.BeanContextChildSupport
    protected synchronized void releaseBeanContextResources() {
        super.releaseBeanContextResources();
        synchronized (this.children) {
            if (this.children.isEmpty()) {
                return;
            }
            for (Object obj : this.children.values().toArray()) {
                ((BCSSChild) obj).revokeAllDelegatedServicesNow();
            }
            this.proxy = null;
        }
    }

    @Override // java.beans.beancontext.BeanContextChildSupport
    protected synchronized void initializeBeanContextResources() {
        super.initializeBeanContextResources();
        BeanContext beanContext = getBeanContext();
        if (beanContext == null) {
            return;
        }
        try {
            this.proxy = new BCSSProxyServiceProvider((BeanContextServices) beanContext);
        } catch (ClassCastException e2) {
        }
    }

    protected final void fireServiceAdded(Class cls) {
        fireServiceAdded(new BeanContextServiceAvailableEvent(getBeanContextServicesPeer(), cls));
    }

    protected final void fireServiceAdded(BeanContextServiceAvailableEvent beanContextServiceAvailableEvent) {
        Object[] array;
        synchronized (this.bcsListeners) {
            array = this.bcsListeners.toArray();
        }
        for (Object obj : array) {
            ((BeanContextServicesListener) obj).serviceAvailable(beanContextServiceAvailableEvent);
        }
    }

    protected final void fireServiceRevoked(BeanContextServiceRevokedEvent beanContextServiceRevokedEvent) {
        Object[] array;
        synchronized (this.bcsListeners) {
            array = this.bcsListeners.toArray();
        }
        for (Object obj : array) {
            ((BeanContextServiceRevokedListener) obj).serviceRevoked(beanContextServiceRevokedEvent);
        }
    }

    protected final void fireServiceRevoked(Class cls, boolean z2) {
        Object[] array;
        BeanContextServiceRevokedEvent beanContextServiceRevokedEvent = new BeanContextServiceRevokedEvent(getBeanContextServicesPeer(), cls, z2);
        synchronized (this.bcsListeners) {
            array = this.bcsListeners.toArray();
        }
        for (Object obj : array) {
            ((BeanContextServicesListener) obj).serviceRevoked(beanContextServiceRevokedEvent);
        }
    }

    @Override // java.beans.beancontext.BeanContextSupport
    protected synchronized void bcsPreSerializationHook(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(this.serializable);
        if (this.serializable <= 0) {
            return;
        }
        int i2 = 0;
        Iterator it = this.services.entrySet().iterator();
        while (it.hasNext() && i2 < this.serializable) {
            Map.Entry entry = (Map.Entry) it.next();
            try {
                BCSSServiceProvider bCSSServiceProvider = (BCSSServiceProvider) entry.getValue();
                if (bCSSServiceProvider.getServiceProvider() instanceof Serializable) {
                    objectOutputStream.writeObject(entry.getKey());
                    objectOutputStream.writeObject(bCSSServiceProvider);
                    i2++;
                }
            } catch (ClassCastException e2) {
            }
        }
        if (i2 != this.serializable) {
            throw new IOException("wrote different number of service providers than expected");
        }
    }

    @Override // java.beans.beancontext.BeanContextSupport
    protected synchronized void bcsPreDeserializationHook(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.serializable = objectInputStream.readInt();
        for (int i2 = this.serializable; i2 > 0; i2--) {
            this.services.put(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        serialize(objectOutputStream, this.bcsListeners);
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        deserialize(objectInputStream, this.bcsListeners);
    }
}
