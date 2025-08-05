package com.sun.naming.internal;

import java.util.List;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/naming/internal/FactoryEnumeration.class */
public final class FactoryEnumeration {
    private List<NamedWeakReference<Object>> factories;
    private int posn = 0;
    private ClassLoader loader;

    FactoryEnumeration(List<NamedWeakReference<Object>> list, ClassLoader classLoader) {
        this.factories = list;
        this.loader = classLoader;
    }

    public Object next() throws NamingException {
        synchronized (this.factories) {
            List<NamedWeakReference<Object>> list = this.factories;
            int i2 = this.posn;
            this.posn = i2 + 1;
            NamedWeakReference<Object> namedWeakReference = list.get(i2);
            Object cls = namedWeakReference.get();
            if (cls != null && !(cls instanceof Class)) {
                return cls;
            }
            String name = namedWeakReference.getName();
            if (cls == null) {
                try {
                    cls = Class.forName(name, true, this.loader);
                    cls = ((Class) cls).newInstance();
                    this.factories.set(this.posn - 1, new NamedWeakReference<>(cls, name));
                    return cls;
                } catch (ClassNotFoundException e2) {
                    NamingException namingException = new NamingException("No longer able to load " + name);
                    namingException.setRootCause(e2);
                    throw namingException;
                } catch (IllegalAccessException e3) {
                    NamingException namingException2 = new NamingException("Cannot access " + cls);
                    namingException2.setRootCause(e3);
                    throw namingException2;
                } catch (InstantiationException e4) {
                    NamingException namingException3 = new NamingException("Cannot instantiate " + cls);
                    namingException3.setRootCause(e4);
                    throw namingException3;
                }
            }
            cls = ((Class) cls).newInstance();
            this.factories.set(this.posn - 1, new NamedWeakReference<>(cls, name));
            return cls;
        }
    }

    public boolean hasMore() {
        boolean z2;
        synchronized (this.factories) {
            z2 = this.posn < this.factories.size();
        }
        return z2;
    }
}
