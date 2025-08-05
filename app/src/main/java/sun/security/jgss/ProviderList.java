package sun.security.jgss;

import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import sun.security.action.GetPropertyAction;
import sun.security.jgss.spi.MechanismFactory;
import sun.security.jgss.wrapper.NativeGSSFactory;
import sun.security.jgss.wrapper.SunNativeProvider;

/* loaded from: rt.jar:sun/security/jgss/ProviderList.class */
public final class ProviderList {
    private static final String PROV_PROP_PREFIX = "GssApiMechanism.";
    private static final int PROV_PROP_PREFIX_LEN = PROV_PROP_PREFIX.length();
    private static final String SPI_MECH_FACTORY_TYPE = "sun.security.jgss.spi.MechanismFactory";
    private static final String DEFAULT_MECH_PROP = "sun.security.jgss.mechanism";
    public static final Oid DEFAULT_MECH_OID;
    private ArrayList<PreferencesEntry> preferences = new ArrayList<>(5);
    private HashMap<PreferencesEntry, MechanismFactory> factories = new HashMap<>(5);
    private HashSet<Oid> mechs = new HashSet<>(5);
    private final GSSCaller caller;

    static {
        Oid oidCreateOid = null;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(DEFAULT_MECH_PROP));
        if (str != null) {
            oidCreateOid = GSSUtil.createOid(str);
        }
        DEFAULT_MECH_OID = oidCreateOid == null ? GSSUtil.GSS_KRB5_MECH_OID : oidCreateOid;
    }

    public ProviderList(GSSCaller gSSCaller, boolean z2) {
        Provider[] providers;
        this.caller = gSSCaller;
        if (z2) {
            providers = new Provider[]{new SunNativeProvider()};
        } else {
            providers = Security.getProviders();
        }
        for (Provider provider : providers) {
            try {
                addProviderAtEnd(provider, null);
            } catch (GSSException e2) {
                GSSUtil.debug("Error in adding provider " + provider.getName() + ": " + ((Object) e2));
            }
        }
    }

    private boolean isMechFactoryProperty(String str) {
        return str.startsWith(PROV_PROP_PREFIX) || str.regionMatches(true, 0, PROV_PROP_PREFIX, 0, PROV_PROP_PREFIX_LEN);
    }

    private Oid getOidFromMechFactoryProperty(String str) throws GSSException {
        return new Oid(str.substring(PROV_PROP_PREFIX_LEN));
    }

    public synchronized MechanismFactory getMechFactory(Oid oid) throws GSSException {
        if (oid == null) {
            oid = DEFAULT_MECH_OID;
        }
        return getMechFactory(oid, (Provider) null);
    }

    public synchronized MechanismFactory getMechFactory(Oid oid, Provider provider) throws GSSException {
        MechanismFactory mechFactory;
        if (oid == null) {
            oid = DEFAULT_MECH_OID;
        }
        if (provider == null) {
            Iterator<PreferencesEntry> it = this.preferences.iterator();
            while (it.hasNext()) {
                PreferencesEntry next = it.next();
                if (next.impliesMechanism(oid) && (mechFactory = getMechFactory(next, oid)) != null) {
                    return mechFactory;
                }
            }
            throw new GSSExceptionImpl(2, oid);
        }
        return getMechFactory(new PreferencesEntry(provider, oid), oid);
    }

    private MechanismFactory getMechFactory(PreferencesEntry preferencesEntry, Oid oid) throws GSSException {
        Provider provider = preferencesEntry.getProvider();
        PreferencesEntry preferencesEntry2 = new PreferencesEntry(provider, oid);
        MechanismFactory mechFactoryImpl = this.factories.get(preferencesEntry2);
        if (mechFactoryImpl == null) {
            String property = provider.getProperty(PROV_PROP_PREFIX + oid.toString());
            if (property != null) {
                mechFactoryImpl = getMechFactoryImpl(provider, property, oid, this.caller);
                this.factories.put(preferencesEntry2, mechFactoryImpl);
            } else if (preferencesEntry.getOid() != null) {
                throw new GSSExceptionImpl(2, "Provider " + provider.getName() + " does not support mechanism " + ((Object) oid));
            }
        }
        return mechFactoryImpl;
    }

    private static MechanismFactory getMechFactoryImpl(Provider provider, String str, Oid oid, GSSCaller gSSCaller) throws GSSException {
        Class<?> cls;
        try {
            Class<?> cls2 = Class.forName(SPI_MECH_FACTORY_TYPE);
            ClassLoader classLoader = provider.getClass().getClassLoader();
            if (classLoader != null) {
                cls = classLoader.loadClass(str);
            } else {
                cls = Class.forName(str);
            }
            if (cls2.isAssignableFrom(cls)) {
                MechanismFactory mechanismFactory = (MechanismFactory) cls.getConstructor(GSSCaller.class).newInstance(gSSCaller);
                if (mechanismFactory instanceof NativeGSSFactory) {
                    ((NativeGSSFactory) mechanismFactory).setMech(oid);
                }
                return mechanismFactory;
            }
            throw createGSSException(provider, str, "is not a sun.security.jgss.spi.MechanismFactory", null);
        } catch (ClassNotFoundException e2) {
            throw createGSSException(provider, str, "cannot be created", e2);
        } catch (IllegalAccessException e3) {
            throw createGSSException(provider, str, "cannot be created", e3);
        } catch (InstantiationException e4) {
            throw createGSSException(provider, str, "cannot be created", e4);
        } catch (NoSuchMethodException e5) {
            throw createGSSException(provider, str, "cannot be created", e5);
        } catch (SecurityException e6) {
            throw createGSSException(provider, str, "cannot be created", e6);
        } catch (InvocationTargetException e7) {
            throw createGSSException(provider, str, "cannot be created", e7);
        }
    }

    private static GSSException createGSSException(Provider provider, String str, String str2, Exception exc) {
        return new GSSExceptionImpl(2, (str + " configured by " + provider.getName() + " for GSS-API Mechanism Factory ") + str2, exc);
    }

    public Oid[] getMechs() {
        return (Oid[]) this.mechs.toArray(new Oid[0]);
    }

    public synchronized void addProviderAtFront(Provider provider, Oid oid) throws GSSException {
        boolean zAddAllMechsFromProvider;
        PreferencesEntry preferencesEntry = new PreferencesEntry(provider, oid);
        Iterator<PreferencesEntry> it = this.preferences.iterator();
        while (it.hasNext()) {
            if (preferencesEntry.implies(it.next())) {
                it.remove();
            }
        }
        if (oid == null) {
            zAddAllMechsFromProvider = addAllMechsFromProvider(provider);
        } else {
            String string = oid.toString();
            if (provider.getProperty(PROV_PROP_PREFIX + string) == null) {
                throw new GSSExceptionImpl(2, "Provider " + provider.getName() + " does not support " + string);
            }
            this.mechs.add(oid);
            zAddAllMechsFromProvider = true;
        }
        if (zAddAllMechsFromProvider) {
            this.preferences.add(0, preferencesEntry);
        }
    }

    public synchronized void addProviderAtEnd(Provider provider, Oid oid) throws GSSException {
        boolean zAddAllMechsFromProvider;
        PreferencesEntry preferencesEntry = new PreferencesEntry(provider, oid);
        Iterator<PreferencesEntry> it = this.preferences.iterator();
        while (it.hasNext()) {
            if (it.next().implies(preferencesEntry)) {
                return;
            }
        }
        if (oid == null) {
            zAddAllMechsFromProvider = addAllMechsFromProvider(provider);
        } else {
            String string = oid.toString();
            if (provider.getProperty(PROV_PROP_PREFIX + string) == null) {
                throw new GSSExceptionImpl(2, "Provider " + provider.getName() + " does not support " + string);
            }
            this.mechs.add(oid);
            zAddAllMechsFromProvider = true;
        }
        if (zAddAllMechsFromProvider) {
            this.preferences.add(preferencesEntry);
        }
    }

    private boolean addAllMechsFromProvider(Provider provider) {
        boolean z2 = false;
        Enumeration<Object> enumerationKeys = provider.keys();
        while (enumerationKeys.hasMoreElements()) {
            String str = (String) enumerationKeys.nextElement2();
            if (isMechFactoryProperty(str)) {
                try {
                    this.mechs.add(getOidFromMechFactoryProperty(str));
                    z2 = true;
                } catch (GSSException e2) {
                    GSSUtil.debug("Ignore the invalid property " + str + " from provider " + provider.getName());
                }
            }
        }
        return z2;
    }

    /* loaded from: rt.jar:sun/security/jgss/ProviderList$PreferencesEntry.class */
    private static final class PreferencesEntry {

        /* renamed from: p, reason: collision with root package name */
        private Provider f13610p;
        private Oid oid;

        PreferencesEntry(Provider provider, Oid oid) {
            this.f13610p = provider;
            this.oid = oid;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PreferencesEntry)) {
                return false;
            }
            PreferencesEntry preferencesEntry = (PreferencesEntry) obj;
            if (this.f13610p.getName().equals(preferencesEntry.f13610p.getName())) {
                if (this.oid == null || preferencesEntry.oid == null) {
                    return this.oid == null && preferencesEntry.oid == null;
                }
                return this.oid.equals(preferencesEntry.oid);
            }
            return false;
        }

        public int hashCode() {
            int iHashCode = (37 * 17) + this.f13610p.getName().hashCode();
            if (this.oid != null) {
                iHashCode = (37 * iHashCode) + this.oid.hashCode();
            }
            return iHashCode;
        }

        boolean implies(Object obj) {
            if (obj instanceof PreferencesEntry) {
                PreferencesEntry preferencesEntry = (PreferencesEntry) obj;
                return equals(preferencesEntry) || (this.f13610p.getName().equals(preferencesEntry.f13610p.getName()) && this.oid == null);
            }
            return false;
        }

        Provider getProvider() {
            return this.f13610p;
        }

        Oid getOid() {
            return this.oid;
        }

        boolean impliesMechanism(Oid oid) {
            return this.oid == null || this.oid.equals(oid);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer("<");
            stringBuffer.append(this.f13610p.getName());
            stringBuffer.append(", ");
            stringBuffer.append((Object) this.oid);
            stringBuffer.append(">");
            return stringBuffer.toString();
        }
    }
}
