package com.sun.jmx.mbeanserver;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.JMX;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/DescriptorCache.class */
public class DescriptorCache {
    private static final DescriptorCache instance = new DescriptorCache();
    private final WeakHashMap<ImmutableDescriptor, WeakReference<ImmutableDescriptor>> map = new WeakHashMap<>();

    private DescriptorCache() {
    }

    static DescriptorCache getInstance() {
        return instance;
    }

    public static DescriptorCache getInstance(JMX jmx) {
        if (jmx != null) {
            return instance;
        }
        return null;
    }

    public ImmutableDescriptor get(ImmutableDescriptor immutableDescriptor) {
        WeakReference<ImmutableDescriptor> weakReference = this.map.get(immutableDescriptor);
        ImmutableDescriptor immutableDescriptor2 = weakReference == null ? null : weakReference.get();
        if (immutableDescriptor2 != null) {
            return immutableDescriptor2;
        }
        this.map.put(immutableDescriptor, new WeakReference<>(immutableDescriptor));
        return immutableDescriptor;
    }

    public ImmutableDescriptor union(Descriptor... descriptorArr) {
        return get(ImmutableDescriptor.union(descriptorArr));
    }
}
