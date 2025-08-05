package com.sun.beans.finder;

import java.beans.PersistenceDelegate;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/beans/finder/PersistenceDelegateFinder.class */
public final class PersistenceDelegateFinder extends InstanceFinder<PersistenceDelegate> {
    private final Map<Class<?>, PersistenceDelegate> registry;

    @Override // com.sun.beans.finder.InstanceFinder
    public /* bridge */ /* synthetic */ PersistenceDelegate find(Class cls) {
        return find((Class<?>) cls);
    }

    @Override // com.sun.beans.finder.InstanceFinder
    public /* bridge */ /* synthetic */ void setPackages(String[] strArr) {
        super.setPackages(strArr);
    }

    @Override // com.sun.beans.finder.InstanceFinder
    public /* bridge */ /* synthetic */ String[] getPackages() {
        return super.getPackages();
    }

    public PersistenceDelegateFinder() {
        super(PersistenceDelegate.class, true, "PersistenceDelegate", new String[0]);
        this.registry = new HashMap();
    }

    public void register(Class<?> cls, PersistenceDelegate persistenceDelegate) {
        synchronized (this.registry) {
            if (persistenceDelegate != null) {
                this.registry.put(cls, persistenceDelegate);
            } else {
                this.registry.remove(cls);
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.beans.finder.InstanceFinder
    public PersistenceDelegate find(Class<?> cls) {
        PersistenceDelegate persistenceDelegate;
        synchronized (this.registry) {
            persistenceDelegate = this.registry.get(cls);
        }
        return persistenceDelegate != null ? persistenceDelegate : (PersistenceDelegate) super.find(cls);
    }
}
