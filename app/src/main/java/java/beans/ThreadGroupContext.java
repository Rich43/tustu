package java.beans;

import com.sun.beans.finder.BeanInfoFinder;
import com.sun.beans.finder.PropertyEditorFinder;
import java.awt.GraphicsEnvironment;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: rt.jar:java/beans/ThreadGroupContext.class */
final class ThreadGroupContext {
    private static final WeakIdentityMap<ThreadGroupContext> contexts = new WeakIdentityMap<ThreadGroupContext>() { // from class: java.beans.ThreadGroupContext.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.beans.WeakIdentityMap
        public ThreadGroupContext create(Object obj) {
            return new ThreadGroupContext();
        }
    };
    private volatile boolean isDesignTime;
    private volatile Boolean isGuiAvailable;
    private Map<Class<?>, BeanInfo> beanInfoCache;
    private BeanInfoFinder beanInfoFinder;
    private PropertyEditorFinder propertyEditorFinder;

    static ThreadGroupContext getContext() {
        return contexts.get(Thread.currentThread().getThreadGroup());
    }

    private ThreadGroupContext() {
    }

    boolean isDesignTime() {
        return this.isDesignTime;
    }

    void setDesignTime(boolean z2) {
        this.isDesignTime = z2;
    }

    boolean isGuiAvailable() {
        Boolean bool = this.isGuiAvailable;
        if (bool != null) {
            return bool.booleanValue();
        }
        return !GraphicsEnvironment.isHeadless();
    }

    void setGuiAvailable(boolean z2) {
        this.isGuiAvailable = Boolean.valueOf(z2);
    }

    BeanInfo getBeanInfo(Class<?> cls) {
        if (this.beanInfoCache != null) {
            return this.beanInfoCache.get(cls);
        }
        return null;
    }

    BeanInfo putBeanInfo(Class<?> cls, BeanInfo beanInfo) {
        if (this.beanInfoCache == null) {
            this.beanInfoCache = new WeakHashMap();
        }
        return this.beanInfoCache.put(cls, beanInfo);
    }

    void removeBeanInfo(Class<?> cls) {
        if (this.beanInfoCache != null) {
            this.beanInfoCache.remove(cls);
        }
    }

    void clearBeanInfoCache() {
        if (this.beanInfoCache != null) {
            this.beanInfoCache.clear();
        }
    }

    synchronized BeanInfoFinder getBeanInfoFinder() {
        if (this.beanInfoFinder == null) {
            this.beanInfoFinder = new BeanInfoFinder();
        }
        return this.beanInfoFinder;
    }

    synchronized PropertyEditorFinder getPropertyEditorFinder() {
        if (this.propertyEditorFinder == null) {
            this.propertyEditorFinder = new PropertyEditorFinder();
        }
        return this.propertyEditorFinder;
    }
}
