package sun.security.jca;

import java.security.Provider;

/* loaded from: rt.jar:sun/security/jca/Providers.class */
public class Providers {
    private static volatile int threadListsUsed;
    private static volatile ProviderList providerList;
    private static final ThreadLocal<ProviderList> threadLists = new InheritableThreadLocal();
    private static final String BACKUP_PROVIDER_CLASSNAME = "sun.security.provider.VerificationProvider";
    private static final String[] jarVerificationProviders = {"sun.security.provider.Sun", "sun.security.rsa.SunRsaSign", "sun.security.ec.SunEC", "com.sun.crypto.provider.SunJCE", BACKUP_PROVIDER_CLASSNAME};

    static {
        providerList = ProviderList.EMPTY;
        providerList = ProviderList.fromSecurityProperties();
    }

    private Providers() {
    }

    public static Provider getSunProvider() {
        try {
            return (Provider) Class.forName(jarVerificationProviders[0]).newInstance();
        } catch (Exception e2) {
            try {
                return (Provider) Class.forName(BACKUP_PROVIDER_CLASSNAME).newInstance();
            } catch (Exception e3) {
                throw new RuntimeException("Sun provider not found", e2);
            }
        }
    }

    public static Object startJarVerification() {
        return beginThreadProviderList(getProviderList().getJarList(jarVerificationProviders));
    }

    public static void stopJarVerification(Object obj) {
        endThreadProviderList((ProviderList) obj);
    }

    public static ProviderList getProviderList() {
        ProviderList threadProviderList = getThreadProviderList();
        if (threadProviderList == null) {
            threadProviderList = getSystemProviderList();
        }
        return threadProviderList;
    }

    public static void setProviderList(ProviderList providerList2) {
        if (getThreadProviderList() == null) {
            setSystemProviderList(providerList2);
        } else {
            changeThreadProviderList(providerList2);
        }
    }

    public static ProviderList getFullProviderList() {
        synchronized (Providers.class) {
            ProviderList threadProviderList = getThreadProviderList();
            if (threadProviderList != null) {
                ProviderList providerListRemoveInvalid = threadProviderList.removeInvalid();
                if (providerListRemoveInvalid != threadProviderList) {
                    changeThreadProviderList(providerListRemoveInvalid);
                    threadProviderList = providerListRemoveInvalid;
                }
                return threadProviderList;
            }
            ProviderList systemProviderList = getSystemProviderList();
            ProviderList providerListRemoveInvalid2 = systemProviderList.removeInvalid();
            if (providerListRemoveInvalid2 != systemProviderList) {
                setSystemProviderList(providerListRemoveInvalid2);
                systemProviderList = providerListRemoveInvalid2;
            }
            return systemProviderList;
        }
    }

    private static ProviderList getSystemProviderList() {
        return providerList;
    }

    private static void setSystemProviderList(ProviderList providerList2) {
        providerList = providerList2;
    }

    public static ProviderList getThreadProviderList() {
        if (threadListsUsed == 0) {
            return null;
        }
        return threadLists.get();
    }

    private static void changeThreadProviderList(ProviderList providerList2) {
        threadLists.set(providerList2);
    }

    public static synchronized ProviderList beginThreadProviderList(ProviderList providerList2) {
        if (ProviderList.debug != null) {
            ProviderList.debug.println("ThreadLocal providers: " + ((Object) providerList2));
        }
        ProviderList providerList3 = threadLists.get();
        threadListsUsed++;
        threadLists.set(providerList2);
        return providerList3;
    }

    public static synchronized void endThreadProviderList(ProviderList providerList2) {
        if (providerList2 == null) {
            if (ProviderList.debug != null) {
                ProviderList.debug.println("Disabling ThreadLocal providers");
            }
            threadLists.remove();
        } else {
            if (ProviderList.debug != null) {
                ProviderList.debug.println("Restoring previous ThreadLocal providers: " + ((Object) providerList2));
            }
            threadLists.set(providerList2);
        }
        threadListsUsed--;
    }
}
