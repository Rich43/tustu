package javax.security.sasl;

import java.security.AccessController;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.callback.CallbackHandler;

/* loaded from: rt.jar:javax/security/sasl/Sasl.class */
public class Sasl {
    private static List<String> disabledMechanisms = new ArrayList();
    private static final String SASL_LOGGER_NAME = "javax.security.sasl";
    private static final Logger logger;
    public static final String QOP = "javax.security.sasl.qop";
    public static final String STRENGTH = "javax.security.sasl.strength";
    public static final String SERVER_AUTH = "javax.security.sasl.server.authentication";
    public static final String BOUND_SERVER_NAME = "javax.security.sasl.bound.server.name";
    public static final String MAX_BUFFER = "javax.security.sasl.maxbuffer";
    public static final String RAW_SEND_SIZE = "javax.security.sasl.rawsendsize";
    public static final String REUSE = "javax.security.sasl.reuse";
    public static final String POLICY_NOPLAINTEXT = "javax.security.sasl.policy.noplaintext";
    public static final String POLICY_NOACTIVE = "javax.security.sasl.policy.noactive";
    public static final String POLICY_NODICTIONARY = "javax.security.sasl.policy.nodictionary";
    public static final String POLICY_NOANONYMOUS = "javax.security.sasl.policy.noanonymous";
    public static final String POLICY_FORWARD_SECRECY = "javax.security.sasl.policy.forward";
    public static final String POLICY_PASS_CREDENTIALS = "javax.security.sasl.policy.credentials";
    public static final String CREDENTIALS = "javax.security.sasl.credentials";

    static {
        String str = (String) AccessController.doPrivileged(() -> {
            return Security.getProperty("jdk.sasl.disabledMechanisms");
        });
        if (str != null) {
            for (String str2 : str.split("\\s*,\\s*")) {
                if (!str2.isEmpty()) {
                    disabledMechanisms.add(str2);
                }
            }
        }
        logger = Logger.getLogger(SASL_LOGGER_NAME);
    }

    private Sasl() {
    }

    public static SaslClient createSaslClient(String[] strArr, String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        SaslClientFactory saslClientFactory;
        SaslClient saslClientCreateSaslClient;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str4 = strArr[i2];
            if (str4 == null) {
                throw new NullPointerException("Mechanism name cannot be null");
            }
            if (str4.length() != 0) {
                if (isDisabled(str4)) {
                    logger.log(Level.FINE, "Disabled " + str4 + " mechanism ignored");
                } else {
                    String str5 = "SaslClientFactory." + str4;
                    Provider[] providers = Security.getProviders(str5);
                    for (int i3 = 0; providers != null && i3 < providers.length; i3++) {
                        String property = providers[i3].getProperty(str5);
                        if (property != null && (saslClientFactory = (SaslClientFactory) loadFactory(providers[i3], property)) != null && (saslClientCreateSaslClient = saslClientFactory.createSaslClient(new String[]{strArr[i2]}, str, str2, str3, map, callbackHandler)) != null) {
                            return saslClientCreateSaslClient;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static Object loadFactory(Provider provider, String str) throws SaslException {
        try {
            return Class.forName(str, true, provider.getClass().getClassLoader()).newInstance();
        } catch (ClassNotFoundException e2) {
            throw new SaslException("Cannot load class " + str, e2);
        } catch (IllegalAccessException e3) {
            throw new SaslException("Cannot access class " + str, e3);
        } catch (InstantiationException e4) {
            throw new SaslException("Cannot instantiate class " + str, e4);
        } catch (SecurityException e5) {
            throw new SaslException("Cannot access class " + str, e5);
        }
    }

    public static SaslServer createSaslServer(String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        SaslServer saslServerCreateSaslServer;
        if (str == null) {
            throw new NullPointerException("Mechanism name cannot be null");
        }
        if (str.length() == 0) {
            return null;
        }
        if (isDisabled(str)) {
            logger.log(Level.FINE, "Disabled " + str + " mechanism ignored");
            return null;
        }
        String str4 = "SaslServerFactory." + str;
        Provider[] providers = Security.getProviders(str4);
        for (int i2 = 0; providers != null && i2 < providers.length; i2++) {
            String property = providers[i2].getProperty(str4);
            if (property == null) {
                throw new SaslException("Provider does not support " + str4);
            }
            SaslServerFactory saslServerFactory = (SaslServerFactory) loadFactory(providers[i2], property);
            if (saslServerFactory != null && (saslServerCreateSaslServer = saslServerFactory.createSaslServer(str, str2, str3, map, callbackHandler)) != null) {
                return saslServerCreateSaslServer;
            }
        }
        return null;
    }

    public static Enumeration<SaslClientFactory> getSaslClientFactories() {
        final Iterator<Object> it = getFactories("SaslClientFactory").iterator();
        return new Enumeration<SaslClientFactory>() { // from class: javax.security.sasl.Sasl.1
            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public SaslClientFactory nextElement2() {
                return (SaslClientFactory) it.next();
            }
        };
    }

    public static Enumeration<SaslServerFactory> getSaslServerFactories() {
        final Iterator<Object> it = getFactories("SaslServerFactory").iterator();
        return new Enumeration<SaslServerFactory>() { // from class: javax.security.sasl.Sasl.2
            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public SaslServerFactory nextElement2() {
                return (SaslServerFactory) it.next();
            }
        };
    }

    private static Set<Object> getFactories(String str) {
        HashSet hashSet = new HashSet();
        if (str == null || str.length() == 0 || str.endsWith(".")) {
            return hashSet;
        }
        Provider[] providers = Security.getProviders();
        HashSet hashSet2 = new HashSet();
        for (int i2 = 0; i2 < providers.length; i2++) {
            hashSet2.clear();
            Enumeration<Object> enumerationKeys = providers[i2].keys();
            while (enumerationKeys.hasMoreElements()) {
                String str2 = (String) enumerationKeys.nextElement2();
                if (str2.startsWith(str) && str2.indexOf(" ") < 0) {
                    String property = providers[i2].getProperty(str2);
                    if (!hashSet2.contains(property)) {
                        hashSet2.add(property);
                        try {
                            Object objLoadFactory = loadFactory(providers[i2], property);
                            if (objLoadFactory != null) {
                                hashSet.add(objLoadFactory);
                            }
                        } catch (Exception e2) {
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableSet(hashSet);
    }

    private static boolean isDisabled(String str) {
        return disabledMechanisms.contains(str);
    }
}
