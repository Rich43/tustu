package com.sun.org.glassfish.external.probe.provider;

import java.util.Iterator;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/glassfish/external/probe/provider/StatsProviderManager.class */
public class StatsProviderManager {
    static StatsProviderManagerDelegate spmd;
    static Vector<StatsProviderInfo> toBeRegistered = new Vector<>();

    private StatsProviderManager() {
    }

    public static boolean register(String configElement, PluginPoint pp, String subTreeRoot, Object statsProvider) {
        return register(pp, configElement, subTreeRoot, statsProvider, (String) null);
    }

    public static boolean register(PluginPoint pp, String configElement, String subTreeRoot, Object statsProvider, String invokerId) {
        StatsProviderInfo spInfo = new StatsProviderInfo(configElement, pp, subTreeRoot, statsProvider, invokerId);
        return registerStatsProvider(spInfo);
    }

    public static boolean register(String configElement, PluginPoint pp, String subTreeRoot, Object statsProvider, String configLevelStr) {
        return register(configElement, pp, subTreeRoot, statsProvider, configLevelStr, null);
    }

    public static boolean register(String configElement, PluginPoint pp, String subTreeRoot, Object statsProvider, String configLevelStr, String invokerId) {
        StatsProviderInfo spInfo = new StatsProviderInfo(configElement, pp, subTreeRoot, statsProvider, invokerId);
        spInfo.setConfigLevel(configLevelStr);
        return registerStatsProvider(spInfo);
    }

    private static boolean registerStatsProvider(StatsProviderInfo spInfo) {
        if (spmd == null) {
            toBeRegistered.add(spInfo);
            return false;
        }
        spmd.register(spInfo);
        return true;
    }

    public static boolean unregister(Object statsProvider) {
        if (spmd == null) {
            Iterator<StatsProviderInfo> it = toBeRegistered.iterator();
            while (it.hasNext()) {
                StatsProviderInfo spInfo = it.next();
                if (spInfo.getStatsProvider() == statsProvider) {
                    toBeRegistered.remove(spInfo);
                    return false;
                }
            }
            return false;
        }
        spmd.unregister(statsProvider);
        return true;
    }

    public static boolean hasListeners(String probeStr) {
        if (spmd == null) {
            return false;
        }
        return spmd.hasListeners(probeStr);
    }

    public static void setStatsProviderManagerDelegate(StatsProviderManagerDelegate lspmd) {
        if (lspmd == null) {
            return;
        }
        spmd = lspmd;
        Iterator<StatsProviderInfo> it = toBeRegistered.iterator();
        while (it.hasNext()) {
            StatsProviderInfo spInfo = it.next();
            spmd.register(spInfo);
        }
        toBeRegistered.clear();
    }
}
