package java.beans.beancontext;

import java.awt.Component;
import java.awt.Container;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.Visibility;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextSupport.class */
public class BeanContextSupport extends BeanContextChildSupport implements BeanContext, Serializable, PropertyChangeListener, VetoableChangeListener {
    static final long serialVersionUID = -4879613978649577204L;
    protected transient HashMap children;
    private int serializable;
    protected transient ArrayList bcmListeners;
    protected Locale locale;
    protected boolean okToUseGui;
    protected boolean designTime;
    private transient PropertyChangeListener childPCL;
    private transient VetoableChangeListener childVCL;
    private transient boolean serializing;

    public BeanContextSupport(BeanContext beanContext, Locale locale, boolean z2, boolean z3) {
        super(beanContext);
        this.serializable = 0;
        this.locale = locale != null ? locale : Locale.getDefault();
        this.designTime = z2;
        this.okToUseGui = z3;
        initialize();
    }

    public BeanContextSupport(BeanContext beanContext, Locale locale, boolean z2) {
        this(beanContext, locale, z2, true);
    }

    public BeanContextSupport(BeanContext beanContext, Locale locale) {
        this(beanContext, locale, false, true);
    }

    public BeanContextSupport(BeanContext beanContext) {
        this(beanContext, null, false, true);
    }

    public BeanContextSupport() {
        this(null, null, false, true);
    }

    public BeanContext getBeanContextPeer() {
        return (BeanContext) getBeanContextChildPeer();
    }

    @Override // java.beans.beancontext.BeanContext
    public Object instantiateChild(String str) throws IOException, ClassNotFoundException {
        BeanContext beanContextPeer = getBeanContextPeer();
        return Beans.instantiate(beanContextPeer.getClass().getClassLoader(), str, beanContextPeer);
    }

    @Override // java.util.Collection, java.util.Set
    public int size() {
        int size;
        synchronized (this.children) {
            size = this.children.size();
        }
        return size;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        boolean zIsEmpty;
        synchronized (this.children) {
            zIsEmpty = this.children.isEmpty();
        }
        return zIsEmpty;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        boolean zContainsKey;
        synchronized (this.children) {
            zContainsKey = this.children.containsKey(obj);
        }
        return zContainsKey;
    }

    public boolean containsKey(Object obj) {
        boolean zContainsKey;
        synchronized (this.children) {
            zContainsKey = this.children.containsKey(obj);
        }
        return zContainsKey;
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator iterator() {
        BCSIterator bCSIterator;
        synchronized (this.children) {
            bCSIterator = new BCSIterator(this.children.keySet().iterator());
        }
        return bCSIterator;
    }

    @Override // java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] array;
        synchronized (this.children) {
            array = this.children.keySet().toArray();
        }
        return array;
    }

    @Override // java.util.Collection
    public Object[] toArray(Object[] objArr) {
        Object[] array;
        synchronized (this.children) {
            array = this.children.keySet().toArray(objArr);
        }
        return array;
    }

    /* loaded from: rt.jar:java/beans/beancontext/BeanContextSupport$BCSIterator.class */
    protected static final class BCSIterator implements Iterator {
        private Iterator src;

        BCSIterator(Iterator it) {
            this.src = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.src.hasNext();
        }

        @Override // java.util.Iterator
        public Object next() {
            return this.src.next();
        }

        @Override // java.util.Iterator
        public void remove() {
        }
    }

    /* loaded from: rt.jar:java/beans/beancontext/BeanContextSupport$BCSChild.class */
    protected class BCSChild implements Serializable {
        private static final long serialVersionUID = -5815286101609939109L;
        private Object child;
        private Object proxyPeer;
        private transient boolean removePending;

        BCSChild(Object obj, Object obj2) {
            this.child = obj;
            this.proxyPeer = obj2;
        }

        Object getChild() {
            return this.child;
        }

        void setRemovePending(boolean z2) {
            this.removePending = z2;
        }

        boolean isRemovePending() {
            return this.removePending;
        }

        boolean isProxyPeer() {
            return this.proxyPeer != null;
        }

        Object getProxyPeer() {
            return this.proxyPeer;
        }
    }

    protected BCSChild createBCSChild(Object obj, Object obj2) {
        return new BCSChild(obj, obj2);
    }

    @Override // java.util.Collection, java.util.List
    public boolean add(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
        if (this.children.containsKey(obj)) {
            return false;
        }
        synchronized (BeanContext.globalHierarchyLock) {
            if (this.children.containsKey(obj)) {
                return false;
            }
            if (!validatePendingAdd(obj)) {
                throw new IllegalStateException();
            }
            BeanContextChild childBeanContextChild = getChildBeanContextChild(obj);
            BeanContextChild beanContextProxy = null;
            synchronized (obj) {
                if (obj instanceof BeanContextProxy) {
                    beanContextProxy = ((BeanContextProxy) obj).getBeanContextProxy();
                    if (beanContextProxy == null) {
                        throw new NullPointerException("BeanContextPeer.getBeanContextProxy()");
                    }
                }
                BCSChild bCSChildCreateBCSChild = createBCSChild(obj, beanContextProxy);
                BCSChild bCSChild = null;
                synchronized (this.children) {
                    this.children.put(obj, bCSChildCreateBCSChild);
                    if (beanContextProxy != null) {
                        BCSChild bCSChildCreateBCSChild2 = createBCSChild(beanContextProxy, obj);
                        bCSChild = bCSChildCreateBCSChild2;
                        this.children.put(beanContextProxy, bCSChildCreateBCSChild2);
                    }
                }
                if (childBeanContextChild != null) {
                    synchronized (childBeanContextChild) {
                        try {
                            childBeanContextChild.setBeanContext(getBeanContextPeer());
                            childBeanContextChild.addPropertyChangeListener("beanContext", this.childPCL);
                            childBeanContextChild.addVetoableChangeListener("beanContext", this.childVCL);
                        } catch (PropertyVetoException e2) {
                            synchronized (this.children) {
                                this.children.remove(obj);
                                if (beanContextProxy != null) {
                                    this.children.remove(beanContextProxy);
                                }
                                throw new IllegalStateException();
                            }
                        }
                    }
                }
                Visibility childVisibility = getChildVisibility(obj);
                if (childVisibility != null) {
                    if (this.okToUseGui) {
                        childVisibility.okToUseGui();
                    } else {
                        childVisibility.dontUseGui();
                    }
                }
                if (getChildSerializable(obj) != null) {
                    this.serializable++;
                }
                childJustAddedHook(obj, bCSChildCreateBCSChild);
                if (beanContextProxy != null) {
                    Visibility childVisibility2 = getChildVisibility(beanContextProxy);
                    if (childVisibility2 != null) {
                        if (this.okToUseGui) {
                            childVisibility2.okToUseGui();
                        } else {
                            childVisibility2.dontUseGui();
                        }
                    }
                    if (getChildSerializable(beanContextProxy) != null) {
                        this.serializable++;
                    }
                    childJustAddedHook(beanContextProxy, bCSChild);
                }
            }
            fireChildrenAdded(new BeanContextMembershipEvent(getBeanContextPeer(), beanContextProxy == null ? new Object[]{obj} : new Object[]{obj, beanContextProxy}));
            return true;
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return remove(obj, true);
    }

    protected boolean remove(Object obj, boolean z2) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
        synchronized (BeanContext.globalHierarchyLock) {
            if (!containsKey(obj)) {
                return false;
            }
            if (!validatePendingRemove(obj)) {
                throw new IllegalStateException();
            }
            BCSChild bCSChild = (BCSChild) this.children.get(obj);
            BCSChild bCSChild2 = null;
            Object obj2 = null;
            synchronized (obj) {
                if (z2) {
                    BeanContextChild childBeanContextChild = getChildBeanContextChild(obj);
                    if (childBeanContextChild != null) {
                        synchronized (childBeanContextChild) {
                            childBeanContextChild.removePropertyChangeListener("beanContext", this.childPCL);
                            childBeanContextChild.removeVetoableChangeListener("beanContext", this.childVCL);
                            try {
                                childBeanContextChild.setBeanContext(null);
                            } catch (PropertyVetoException e2) {
                                childBeanContextChild.addPropertyChangeListener("beanContext", this.childPCL);
                                childBeanContextChild.addVetoableChangeListener("beanContext", this.childVCL);
                                throw new IllegalStateException();
                            }
                        }
                    }
                }
                synchronized (this.children) {
                    this.children.remove(obj);
                    if (bCSChild.isProxyPeer()) {
                        HashMap map = this.children;
                        Object proxyPeer = bCSChild.getProxyPeer();
                        obj2 = proxyPeer;
                        bCSChild2 = (BCSChild) map.get(proxyPeer);
                        this.children.remove(obj2);
                    }
                }
                if (getChildSerializable(obj) != null) {
                    this.serializable--;
                }
                childJustRemovedHook(obj, bCSChild);
                if (obj2 != null) {
                    if (getChildSerializable(obj2) != null) {
                        this.serializable--;
                    }
                    childJustRemovedHook(obj2, bCSChild2);
                }
            }
            fireChildrenRemoved(new BeanContextMembershipEvent(getBeanContextPeer(), obj2 == null ? new Object[]{obj} : new Object[]{obj, obj2}));
            return true;
        }
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection collection) {
        synchronized (this.children) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override // java.util.Collection
    public boolean addAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection, java.util.List
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.beans.beancontext.BeanContext
    public void addBeanContextMembershipListener(BeanContextMembershipListener beanContextMembershipListener) {
        if (beanContextMembershipListener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (this.bcmListeners) {
            if (this.bcmListeners.contains(beanContextMembershipListener)) {
                return;
            }
            this.bcmListeners.add(beanContextMembershipListener);
        }
    }

    @Override // java.beans.beancontext.BeanContext
    public void removeBeanContextMembershipListener(BeanContextMembershipListener beanContextMembershipListener) {
        if (beanContextMembershipListener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (this.bcmListeners) {
            if (this.bcmListeners.contains(beanContextMembershipListener)) {
                this.bcmListeners.remove(beanContextMembershipListener);
            }
        }
    }

    @Override // java.beans.beancontext.BeanContext
    public InputStream getResourceAsStream(String str, BeanContextChild beanContextChild) {
        if (str == null) {
            throw new NullPointerException("name");
        }
        if (beanContextChild == null) {
            throw new NullPointerException("bcc");
        }
        if (containsKey(beanContextChild)) {
            ClassLoader classLoader = beanContextChild.getClass().getClassLoader();
            return classLoader != null ? classLoader.getResourceAsStream(str) : ClassLoader.getSystemResourceAsStream(str);
        }
        throw new IllegalArgumentException("Not a valid child");
    }

    @Override // java.beans.beancontext.BeanContext
    public URL getResource(String str, BeanContextChild beanContextChild) {
        if (str == null) {
            throw new NullPointerException("name");
        }
        if (beanContextChild == null) {
            throw new NullPointerException("bcc");
        }
        if (containsKey(beanContextChild)) {
            ClassLoader classLoader = beanContextChild.getClass().getClassLoader();
            return classLoader != null ? classLoader.getResource(str) : ClassLoader.getSystemResource(str);
        }
        throw new IllegalArgumentException("Not a valid child");
    }

    @Override // java.beans.DesignMode
    public synchronized void setDesignTime(boolean z2) {
        if (this.designTime != z2) {
            this.designTime = z2;
            firePropertyChange("designMode", Boolean.valueOf(!z2), Boolean.valueOf(z2));
        }
    }

    @Override // java.beans.DesignMode
    public synchronized boolean isDesignTime() {
        return this.designTime;
    }

    public synchronized void setLocale(Locale locale) throws PropertyVetoException {
        if (this.locale != null && !this.locale.equals(locale) && locale != null) {
            Locale locale2 = this.locale;
            fireVetoableChange("locale", locale2, locale);
            this.locale = locale;
            firePropertyChange("locale", locale2, locale);
        }
    }

    public synchronized Locale getLocale() {
        return this.locale;
    }

    @Override // java.beans.Visibility
    public synchronized boolean needsGui() {
        BeanContext beanContextPeer = getBeanContextPeer();
        if (beanContextPeer != this) {
            if (beanContextPeer instanceof Visibility) {
                return beanContextPeer.needsGui();
            }
            if ((beanContextPeer instanceof Container) || (beanContextPeer instanceof Component)) {
                return true;
            }
        }
        synchronized (this.children) {
            for (Object obj : this.children.keySet()) {
                try {
                    return ((Visibility) obj).needsGui();
                } catch (ClassCastException e2) {
                    if ((obj instanceof Container) || (obj instanceof Component)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override // java.beans.Visibility
    public synchronized void dontUseGui() {
        if (this.okToUseGui) {
            this.okToUseGui = false;
            synchronized (this.children) {
                Iterator it = this.children.keySet().iterator();
                while (it.hasNext()) {
                    Visibility childVisibility = getChildVisibility(it.next());
                    if (childVisibility != null) {
                        childVisibility.dontUseGui();
                    }
                }
            }
        }
    }

    @Override // java.beans.Visibility
    public synchronized void okToUseGui() {
        if (!this.okToUseGui) {
            this.okToUseGui = true;
            synchronized (this.children) {
                Iterator it = this.children.keySet().iterator();
                while (it.hasNext()) {
                    Visibility childVisibility = getChildVisibility(it.next());
                    if (childVisibility != null) {
                        childVisibility.okToUseGui();
                    }
                }
            }
        }
    }

    @Override // java.beans.Visibility
    public boolean avoidingGui() {
        return !this.okToUseGui && needsGui();
    }

    public boolean isSerializing() {
        return this.serializing;
    }

    protected Iterator bcsChildren() {
        Iterator it;
        synchronized (this.children) {
            it = this.children.values().iterator();
        }
        return it;
    }

    protected void bcsPreSerializationHook(ObjectOutputStream objectOutputStream) throws IOException {
    }

    protected void bcsPreDeserializationHook(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
    }

    protected void childDeserializedHook(Object obj, BCSChild bCSChild) {
        synchronized (this.children) {
            this.children.put(obj, bCSChild);
        }
    }

    protected final void serialize(ObjectOutputStream objectOutputStream, Collection collection) throws IOException {
        int i2 = 0;
        Object[] array = collection.toArray();
        for (int i3 = 0; i3 < array.length; i3++) {
            if (array[i3] instanceof Serializable) {
                i2++;
            } else {
                array[i3] = null;
            }
        }
        objectOutputStream.writeInt(i2);
        int i4 = 0;
        while (i2 > 0) {
            Object obj = array[i4];
            if (obj != null) {
                objectOutputStream.writeObject(obj);
                i2--;
            }
            i4++;
        }
    }

    protected final void deserialize(ObjectInputStream objectInputStream, Collection collection) throws IOException, ClassNotFoundException {
        int i2 = objectInputStream.readInt();
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 > 0) {
                collection.add(objectInputStream.readObject());
            } else {
                return;
            }
        }
    }

    public final void writeChildren(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.serializable <= 0) {
            return;
        }
        boolean z2 = this.serializing;
        this.serializing = true;
        int i2 = 0;
        synchronized (this.children) {
            Iterator it = this.children.entrySet().iterator();
            while (it.hasNext() && i2 < this.serializable) {
                Map.Entry entry = (Map.Entry) it.next();
                if (entry.getKey() instanceof Serializable) {
                    try {
                        objectOutputStream.writeObject(entry.getKey());
                        objectOutputStream.writeObject(entry.getValue());
                        i2++;
                    } catch (IOException e2) {
                        this.serializing = z2;
                        throw e2;
                    }
                }
            }
        }
        this.serializing = z2;
        if (i2 != this.serializable) {
            throw new IOException("wrote different number of children than expected");
        }
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        this.serializing = true;
        synchronized (BeanContext.globalHierarchyLock) {
            try {
                objectOutputStream.defaultWriteObject();
                bcsPreSerializationHook(objectOutputStream);
                if (this.serializable > 0 && equals(getBeanContextPeer())) {
                    writeChildren(objectOutputStream);
                }
                serialize(objectOutputStream, this.bcmListeners);
                this.serializing = false;
            } catch (Throwable th) {
                this.serializing = false;
                throw th;
            }
        }
    }

    public final void readChildren(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int i2 = this.serializable;
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 > 0) {
                Object object = objectInputStream.readObject();
                BCSChild bCSChild = (BCSChild) objectInputStream.readObject();
                synchronized (object) {
                    BeanContextChild beanContextChild = null;
                    try {
                        beanContextChild = (BeanContextChild) object;
                    } catch (ClassCastException e2) {
                    }
                    if (beanContextChild != null) {
                        try {
                            beanContextChild.setBeanContext(getBeanContextPeer());
                            beanContextChild.addPropertyChangeListener("beanContext", this.childPCL);
                            beanContextChild.addVetoableChangeListener("beanContext", this.childVCL);
                        } catch (PropertyVetoException e3) {
                        }
                    }
                    childDeserializedHook(object, bCSChild);
                }
            } else {
                return;
            }
        }
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        synchronized (BeanContext.globalHierarchyLock) {
            objectInputStream.defaultReadObject();
            initialize();
            bcsPreDeserializationHook(objectInputStream);
            if (this.serializable > 0 && equals(getBeanContextPeer())) {
                readChildren(objectInputStream);
            }
            ArrayList arrayList = new ArrayList(1);
            this.bcmListeners = arrayList;
            deserialize(objectInputStream, arrayList);
        }
    }

    @Override // java.beans.VetoableChangeListener
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        String propertyName = propertyChangeEvent.getPropertyName();
        Object source = propertyChangeEvent.getSource();
        synchronized (this.children) {
            if ("beanContext".equals(propertyName) && containsKey(source) && !getBeanContextPeer().equals(propertyChangeEvent.getNewValue())) {
                if (!validatePendingRemove(source)) {
                    throw new PropertyVetoException("current BeanContext vetoes setBeanContext()", propertyChangeEvent);
                }
                ((BCSChild) this.children.get(source)).setRemovePending(true);
            }
        }
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        Object source = propertyChangeEvent.getSource();
        synchronized (this.children) {
            if ("beanContext".equals(propertyName) && containsKey(source) && ((BCSChild) this.children.get(source)).isRemovePending()) {
                BeanContext beanContextPeer = getBeanContextPeer();
                if (beanContextPeer.equals(propertyChangeEvent.getOldValue()) && !beanContextPeer.equals(propertyChangeEvent.getNewValue())) {
                    remove(source, false);
                } else {
                    ((BCSChild) this.children.get(source)).setRemovePending(false);
                }
            }
        }
    }

    protected boolean validatePendingAdd(Object obj) {
        return true;
    }

    protected boolean validatePendingRemove(Object obj) {
        return true;
    }

    protected void childJustAddedHook(Object obj, BCSChild bCSChild) {
    }

    protected void childJustRemovedHook(Object obj, BCSChild bCSChild) {
    }

    protected static final Visibility getChildVisibility(Object obj) {
        try {
            return (Visibility) obj;
        } catch (ClassCastException e2) {
            return null;
        }
    }

    protected static final Serializable getChildSerializable(Object obj) {
        try {
            return (Serializable) obj;
        } catch (ClassCastException e2) {
            return null;
        }
    }

    protected static final PropertyChangeListener getChildPropertyChangeListener(Object obj) {
        try {
            return (PropertyChangeListener) obj;
        } catch (ClassCastException e2) {
            return null;
        }
    }

    protected static final VetoableChangeListener getChildVetoableChangeListener(Object obj) {
        try {
            return (VetoableChangeListener) obj;
        } catch (ClassCastException e2) {
            return null;
        }
    }

    protected static final BeanContextMembershipListener getChildBeanContextMembershipListener(Object obj) {
        try {
            return (BeanContextMembershipListener) obj;
        } catch (ClassCastException e2) {
            return null;
        }
    }

    protected static final BeanContextChild getChildBeanContextChild(Object obj) {
        try {
            BeanContextChild beanContextChild = (BeanContextChild) obj;
            if ((obj instanceof BeanContextChild) && (obj instanceof BeanContextProxy)) {
                throw new IllegalArgumentException("child cannot implement both BeanContextChild and BeanContextProxy");
            }
            return beanContextChild;
        } catch (ClassCastException e2) {
            try {
                return ((BeanContextProxy) obj).getBeanContextProxy();
            } catch (ClassCastException e3) {
                return null;
            }
        }
    }

    protected final void fireChildrenAdded(BeanContextMembershipEvent beanContextMembershipEvent) {
        Object[] array;
        synchronized (this.bcmListeners) {
            array = this.bcmListeners.toArray();
        }
        for (Object obj : array) {
            ((BeanContextMembershipListener) obj).childrenAdded(beanContextMembershipEvent);
        }
    }

    protected final void fireChildrenRemoved(BeanContextMembershipEvent beanContextMembershipEvent) {
        Object[] array;
        synchronized (this.bcmListeners) {
            array = this.bcmListeners.toArray();
        }
        for (Object obj : array) {
            ((BeanContextMembershipListener) obj).childrenRemoved(beanContextMembershipEvent);
        }
    }

    protected synchronized void initialize() {
        this.children = new HashMap(this.serializable + 1);
        this.bcmListeners = new ArrayList(1);
        this.childPCL = new PropertyChangeListener() { // from class: java.beans.beancontext.BeanContextSupport.1
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                BeanContextSupport.this.propertyChange(propertyChangeEvent);
            }
        };
        this.childVCL = new VetoableChangeListener() { // from class: java.beans.beancontext.BeanContextSupport.2
            @Override // java.beans.VetoableChangeListener
            public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
                BeanContextSupport.this.vetoableChange(propertyChangeEvent);
            }
        };
    }

    protected final Object[] copyChildren() {
        Object[] array;
        synchronized (this.children) {
            array = this.children.keySet().toArray();
        }
        return array;
    }

    protected static final boolean classEquals(Class cls, Class cls2) {
        return cls.equals(cls2) || cls.getName().equals(cls2.getName());
    }
}
