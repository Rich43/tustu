package java.time.zone;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: rt.jar:java/time/zone/ZoneRulesProvider.class */
public abstract class ZoneRulesProvider {
    private static final CopyOnWriteArrayList<ZoneRulesProvider> PROVIDERS = new CopyOnWriteArrayList<>();
    private static final ConcurrentMap<String, ZoneRulesProvider> ZONES = new ConcurrentHashMap(512, 0.75f, 2);

    protected abstract Set<String> provideZoneIds();

    protected abstract ZoneRules provideRules(String str, boolean z2);

    protected abstract NavigableMap<String, ZoneRules> provideVersions(String str);

    static {
        final ArrayList arrayList = new ArrayList();
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.time.zone.ZoneRulesProvider.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                String property = System.getProperty("java.time.zone.DefaultZoneRulesProvider");
                if (property != null) {
                    try {
                        ZoneRulesProvider zoneRulesProvider = (ZoneRulesProvider) ZoneRulesProvider.class.cast(Class.forName(property, true, ClassLoader.getSystemClassLoader()).newInstance());
                        ZoneRulesProvider.registerProvider(zoneRulesProvider);
                        arrayList.add(zoneRulesProvider);
                        return null;
                    } catch (Exception e2) {
                        throw new Error(e2);
                    }
                }
                ZoneRulesProvider.registerProvider(new TzdbZoneRulesProvider());
                return null;
            }
        });
        Iterator it = ServiceLoader.load(ZoneRulesProvider.class, ClassLoader.getSystemClassLoader()).iterator();
        while (it.hasNext()) {
            try {
                ZoneRulesProvider zoneRulesProvider = (ZoneRulesProvider) it.next();
                boolean z2 = false;
                Iterator<E> it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    if (((ZoneRulesProvider) it2.next()).getClass() == zoneRulesProvider.getClass()) {
                        z2 = true;
                    }
                }
                if (!z2) {
                    registerProvider0(zoneRulesProvider);
                    arrayList.add(zoneRulesProvider);
                }
            } catch (ServiceConfigurationError e2) {
                if (!(e2.getCause() instanceof SecurityException)) {
                    throw e2;
                }
            }
        }
        PROVIDERS.addAll(arrayList);
    }

    public static Set<String> getAvailableZoneIds() {
        return new HashSet(ZONES.keySet());
    }

    public static ZoneRules getRules(String str, boolean z2) {
        Objects.requireNonNull(str, "zoneId");
        return getProvider(str).provideRules(str, z2);
    }

    public static NavigableMap<String, ZoneRules> getVersions(String str) {
        Objects.requireNonNull(str, "zoneId");
        return getProvider(str).provideVersions(str);
    }

    private static ZoneRulesProvider getProvider(String str) {
        ZoneRulesProvider zoneRulesProvider = ZONES.get(str);
        if (zoneRulesProvider == null) {
            if (ZONES.isEmpty()) {
                throw new ZoneRulesException("No time-zone data files registered");
            }
            throw new ZoneRulesException("Unknown time-zone ID: " + str);
        }
        return zoneRulesProvider;
    }

    public static void registerProvider(ZoneRulesProvider zoneRulesProvider) {
        Objects.requireNonNull(zoneRulesProvider, "provider");
        registerProvider0(zoneRulesProvider);
        PROVIDERS.add(zoneRulesProvider);
    }

    private static void registerProvider0(ZoneRulesProvider zoneRulesProvider) {
        for (String str : zoneRulesProvider.provideZoneIds()) {
            Objects.requireNonNull(str, "zoneId");
            if (ZONES.putIfAbsent(str, zoneRulesProvider) != null) {
                throw new ZoneRulesException("Unable to register zone as one already registered with that ID: " + str + ", currently loading from provider: " + ((Object) zoneRulesProvider));
            }
        }
    }

    public static boolean refresh() {
        boolean zProvideRefresh = false;
        Iterator<ZoneRulesProvider> it = PROVIDERS.iterator();
        while (it.hasNext()) {
            zProvideRefresh |= it.next().provideRefresh();
        }
        return zProvideRefresh;
    }

    protected ZoneRulesProvider() {
    }

    protected boolean provideRefresh() {
        return false;
    }
}
