package sun.net;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;

/* loaded from: rt.jar:sun/net/InetAddressCachePolicy.class */
public final class InetAddressCachePolicy {
    private static final String cachePolicyProp = "networkaddress.cache.ttl";
    private static final String cachePolicyPropFallback = "sun.net.inetaddr.ttl";
    private static final String negativeCachePolicyProp = "networkaddress.cache.negative.ttl";
    private static final String negativeCachePolicyPropFallback = "sun.net.inetaddr.negative.ttl";
    public static final int FOREVER = -1;
    public static final int NEVER = 0;
    public static final int DEFAULT_POSITIVE = 30;
    private static volatile int cachePolicy;
    private static volatile int negativeCachePolicy;
    private static boolean propertySet;
    private static boolean propertyNegativeSet;

    static {
        cachePolicy = -1;
        negativeCachePolicy = 0;
        Integer num = (Integer) AccessController.doPrivileged(new PrivilegedAction<Integer>() { // from class: sun.net.InetAddressCachePolicy.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Integer run2() {
                try {
                    String property = Security.getProperty(InetAddressCachePolicy.cachePolicyProp);
                    if (property != null) {
                        return Integer.valueOf(property);
                    }
                } catch (NumberFormatException e2) {
                }
                try {
                    String property2 = System.getProperty(InetAddressCachePolicy.cachePolicyPropFallback);
                    if (property2 != null) {
                        return Integer.decode(property2);
                    }
                    return null;
                } catch (NumberFormatException e3) {
                    return null;
                }
            }
        });
        if (num != null) {
            cachePolicy = num.intValue() < 0 ? -1 : num.intValue();
            propertySet = true;
        } else if (System.getSecurityManager() == null) {
            cachePolicy = 30;
        }
        Integer num2 = (Integer) AccessController.doPrivileged(new PrivilegedAction<Integer>() { // from class: sun.net.InetAddressCachePolicy.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Integer run2() {
                try {
                    String property = Security.getProperty(InetAddressCachePolicy.negativeCachePolicyProp);
                    if (property != null) {
                        return Integer.valueOf(property);
                    }
                } catch (NumberFormatException e2) {
                }
                try {
                    String property2 = System.getProperty(InetAddressCachePolicy.negativeCachePolicyPropFallback);
                    if (property2 != null) {
                        return Integer.decode(property2);
                    }
                    return null;
                } catch (NumberFormatException e3) {
                    return null;
                }
            }
        });
        if (num2 != null) {
            negativeCachePolicy = num2.intValue() < 0 ? -1 : num2.intValue();
            propertyNegativeSet = true;
        }
    }

    public static int get() {
        return cachePolicy;
    }

    public static int getNegative() {
        return negativeCachePolicy;
    }

    public static synchronized void setIfNotSet(int i2) {
        if (!propertySet) {
            checkValue(i2, cachePolicy);
            cachePolicy = i2;
        }
    }

    public static void setNegativeIfNotSet(int i2) {
        if (!propertyNegativeSet) {
            negativeCachePolicy = i2 < 0 ? -1 : i2;
        }
    }

    private static void checkValue(int i2, int i3) {
        if (i2 == -1) {
            return;
        }
        if (i3 == -1 || i2 < i3 || i2 < -1) {
            throw new SecurityException("can't make InetAddress cache more lax");
        }
    }
}
