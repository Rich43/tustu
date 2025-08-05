package com.sun.org.glassfish.external.probe.provider;

/* loaded from: rt.jar:com/sun/org/glassfish/external/probe/provider/StatsProviderManagerDelegate.class */
public interface StatsProviderManagerDelegate {
    void register(StatsProviderInfo statsProviderInfo);

    void unregister(Object obj);

    boolean hasListeners(String str);
}
