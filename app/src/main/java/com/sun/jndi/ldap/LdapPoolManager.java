package com.sun.jndi.ldap;

import com.sun.jndi.ldap.pool.Pool;
import com.sun.jndi.ldap.pool.PoolCleaner;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.pobjects.graphics.Separation;
import sun.misc.InnocuousThread;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapPoolManager.class */
public final class LdapPoolManager {
    private static final String DEBUG = "com.sun.jndi.ldap.connect.pool.debug";
    public static final boolean debug = "all".equalsIgnoreCase(getProperty(DEBUG, null));
    public static final boolean trace;
    private static final String POOL_AUTH = "com.sun.jndi.ldap.connect.pool.authentication";
    private static final String POOL_PROTOCOL = "com.sun.jndi.ldap.connect.pool.protocol";
    private static final String MAX_POOL_SIZE = "com.sun.jndi.ldap.connect.pool.maxsize";
    private static final String PREF_POOL_SIZE = "com.sun.jndi.ldap.connect.pool.prefsize";
    private static final String INIT_POOL_SIZE = "com.sun.jndi.ldap.connect.pool.initsize";
    private static final String POOL_TIMEOUT = "com.sun.jndi.ldap.connect.pool.timeout";
    private static final String SASL_CALLBACK = "java.naming.security.sasl.callback";
    private static final int DEFAULT_MAX_POOL_SIZE = 0;
    private static final int DEFAULT_PREF_POOL_SIZE = 0;
    private static final int DEFAULT_INIT_POOL_SIZE = 1;
    private static final int DEFAULT_TIMEOUT = 0;
    private static final String DEFAULT_AUTH_MECHS = "none simple";
    private static final String DEFAULT_PROTOCOLS = "plain";
    private static final int NONE = 0;
    private static final int SIMPLE = 1;
    private static final int DIGEST = 2;
    private static final long idleTimeout;
    private static final int maxSize;
    private static final int prefSize;
    private static final int initSize;
    private static boolean supportPlainProtocol;
    private static boolean supportSslProtocol;
    private static final Pool[] pools;

    static {
        trace = debug || "fine".equalsIgnoreCase(getProperty(DEBUG, null));
        supportPlainProtocol = false;
        supportSslProtocol = false;
        pools = new Pool[3];
        maxSize = getInteger(MAX_POOL_SIZE, 0);
        prefSize = getInteger(PREF_POOL_SIZE, 0);
        initSize = getInteger(INIT_POOL_SIZE, 1);
        idleTimeout = getLong(POOL_TIMEOUT, 0L);
        StringTokenizer stringTokenizer = new StringTokenizer(getProperty(POOL_AUTH, DEFAULT_AUTH_MECHS));
        int iCountTokens = stringTokenizer.countTokens();
        for (int i2 = 0; i2 < iCountTokens; i2++) {
            String lowerCase = stringTokenizer.nextToken().toLowerCase(Locale.ENGLISH);
            if (lowerCase.equals("anonymous")) {
                lowerCase = Separation.COLORANT_NONE;
            }
            int iFindPool = findPool(lowerCase);
            if (iFindPool >= 0 && pools[iFindPool] == null) {
                pools[iFindPool] = new Pool(initSize, prefSize, maxSize);
            }
        }
        StringTokenizer stringTokenizer2 = new StringTokenizer(getProperty(POOL_PROTOCOL, DEFAULT_PROTOCOLS));
        int iCountTokens2 = stringTokenizer2.countTokens();
        for (int i3 = 0; i3 < iCountTokens2; i3++) {
            String strNextToken = stringTokenizer2.nextToken();
            if (DEFAULT_PROTOCOLS.equalsIgnoreCase(strNextToken)) {
                supportPlainProtocol = true;
            } else if ("ssl".equalsIgnoreCase(strNextToken)) {
                supportSslProtocol = true;
            }
        }
        if (idleTimeout > 0) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.jndi.ldap.LdapPoolManager.1
                static final /* synthetic */ boolean $assertionsDisabled;

                static {
                    $assertionsDisabled = !LdapPoolManager.class.desiredAssertionStatus();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    Thread threadNewSystemThread = InnocuousThread.newSystemThread("LDAP PoolCleaner", new PoolCleaner(LdapPoolManager.idleTimeout, LdapPoolManager.pools));
                    if (!$assertionsDisabled && threadNewSystemThread.getContextClassLoader() != null) {
                        throw new AssertionError();
                    }
                    threadNewSystemThread.setDaemon(true);
                    threadNewSystemThread.start();
                    return null;
                }
            });
        }
        if (debug) {
            showStats(System.err);
        }
    }

    private LdapPoolManager() {
    }

    private static int findPool(String str) {
        if (Separation.COLORANT_NONE.equalsIgnoreCase(str)) {
            return 0;
        }
        if ("simple".equalsIgnoreCase(str)) {
            return 1;
        }
        if ("digest-md5".equalsIgnoreCase(str)) {
            return 2;
        }
        return -1;
    }

    static boolean isPoolingAllowed(String str, OutputStream outputStream, String str2, String str3, Hashtable<?, ?> hashtable) throws NamingException {
        if ((outputStream != null && !debug) || ((str3 == null && !supportPlainProtocol) || ("ssl".equalsIgnoreCase(str3) && !supportSslProtocol))) {
            d("Pooling disallowed due to tracing or unsupported pooling of protocol");
            return false;
        }
        boolean z2 = false;
        if (str != null && !str.equals("javax.net.ssl.SSLSocketFactory")) {
            try {
                for (Class<?> cls : Obj.helper.loadClass(str).getInterfaces()) {
                    if (cls.getCanonicalName().equals("java.util.Comparator")) {
                        z2 = true;
                    }
                }
                if (!z2) {
                    return false;
                }
            } catch (Exception e2) {
                CommunicationException communicationException = new CommunicationException("Loading the socket factory");
                communicationException.setRootCause(e2);
                throw communicationException;
            }
        }
        int iFindPool = findPool(str2);
        if (iFindPool < 0 || pools[iFindPool] == null) {
            d("authmech not found: ", str2);
            return false;
        }
        d("using authmech: ", str2);
        switch (iFindPool) {
            case 0:
            case 1:
                return true;
            case 2:
                return hashtable == null || hashtable.get(SASL_CALLBACK) == null;
            default:
                return false;
        }
    }

    static LdapClient getLdapClient(String str, int i2, String str2, int i3, int i4, OutputStream outputStream, int i5, String str3, Control[] controlArr, String str4, String str5, Object obj, Hashtable<?, ?> hashtable) throws NamingException {
        Pool pool;
        ClientId digestClientId = null;
        int iFindPool = findPool(str3);
        if (iFindPool < 0 || (pool = pools[iFindPool]) == null) {
            throw new IllegalArgumentException("Attempting to use pooling for an unsupported mechanism: " + str3);
        }
        switch (iFindPool) {
            case 0:
                digestClientId = new ClientId(i5, str, i2, str4, controlArr, outputStream, str2);
                break;
            case 1:
                digestClientId = new SimpleClientId(i5, str, i2, str4, controlArr, outputStream, str2, str5, obj);
                break;
            case 2:
                digestClientId = new DigestClientId(i5, str, i2, str4, controlArr, outputStream, str2, str5, obj, hashtable);
                break;
        }
        return (LdapClient) pool.getPooledConnection(digestClientId, i3, new LdapClientFactory(str, i2, str2, i3, i4, outputStream));
    }

    public static void showStats(PrintStream printStream) {
        printStream.println("***** start *****");
        printStream.println("idle timeout: " + idleTimeout);
        printStream.println("maximum pool size: " + maxSize);
        printStream.println("preferred pool size: " + prefSize);
        printStream.println("initial pool size: " + initSize);
        printStream.println("protocol types: " + (supportPlainProtocol ? "plain " : "") + (supportSslProtocol ? "ssl" : ""));
        printStream.println("authentication types: " + (pools[0] != null ? "none " : "") + (pools[1] != null ? "simple " : "") + (pools[2] != null ? "DIGEST-MD5 " : ""));
        int i2 = 0;
        while (i2 < pools.length) {
            if (pools[i2] != null) {
                printStream.println((i2 == 0 ? "anonymous pools" : i2 == 1 ? "simple auth pools" : i2 == 2 ? "digest pools" : "") + CallSiteDescriptor.TOKEN_DELIMITER);
                pools[i2].showStats(printStream);
            }
            i2++;
        }
        printStream.println("***** end *****");
    }

    public static void expire(long j2) {
        for (int i2 = 0; i2 < pools.length; i2++) {
            if (pools[i2] != null) {
                pools[i2].expire(j2);
            }
        }
    }

    private static void d(String str) {
        if (debug) {
            System.err.println("LdapPoolManager: " + str);
        }
    }

    private static void d(String str, String str2) {
        if (debug) {
            System.err.println("LdapPoolManager: " + str + str2);
        }
    }

    private static final String getProperty(final String str, final String str2) {
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.jndi.ldap.LdapPoolManager.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                try {
                    return System.getProperty(str, str2);
                } catch (SecurityException e2) {
                    return str2;
                }
            }
        });
    }

    private static final int getInteger(final String str, final int i2) {
        return ((Integer) AccessController.doPrivileged(new PrivilegedAction<Integer>() { // from class: com.sun.jndi.ldap.LdapPoolManager.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Integer run2() {
                try {
                    return Integer.getInteger(str, i2);
                } catch (SecurityException e2) {
                    return new Integer(i2);
                }
            }
        })).intValue();
    }

    private static final long getLong(final String str, final long j2) {
        return ((Long) AccessController.doPrivileged(new PrivilegedAction<Long>() { // from class: com.sun.jndi.ldap.LdapPoolManager.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Long run2() {
                try {
                    return Long.getLong(str, j2);
                } catch (SecurityException e2) {
                    return new Long(j2);
                }
            }
        })).longValue();
    }
}
