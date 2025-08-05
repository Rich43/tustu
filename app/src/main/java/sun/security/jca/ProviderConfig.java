package sun.security.jca;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.ProviderException;
import javafx.fxml.FXMLLoader;
import sun.security.util.Debug;
import sun.security.util.PropertyExpander;

/* loaded from: rt.jar:sun/security/jca/ProviderConfig.class */
final class ProviderConfig {
    private static final String P11_SOL_NAME = "sun.security.pkcs11.SunPKCS11";
    private static final String P11_SOL_ARG = "${java.home}/lib/security/sunpkcs11-solaris.cfg";
    private static final int MAX_LOAD_TRIES = 30;
    private final String className;
    private final String argument;
    private int tries;
    private volatile Provider provider;
    private boolean isLoading;
    private static final Debug debug = Debug.getInstance("jca", "ProviderConfig");
    private static final Class[] CL_STRING = {String.class};

    ProviderConfig(String str, String str2) {
        if (str.equals(P11_SOL_NAME) && str2.equals(P11_SOL_ARG)) {
            checkSunPKCS11Solaris();
        }
        this.className = str;
        this.argument = expand(str2);
    }

    ProviderConfig(String str) {
        this(str, "");
    }

    ProviderConfig(Provider provider) {
        this.className = provider.getClass().getName();
        this.argument = "";
        this.provider = provider;
    }

    private void checkSunPKCS11Solaris() {
        if (((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.security.jca.ProviderConfig.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                if (!new File("/usr/lib/libpkcs11.so").exists()) {
                    return Boolean.FALSE;
                }
                if ("false".equalsIgnoreCase(System.getProperty("sun.security.pkcs11.enable-solaris"))) {
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            }
        })) == Boolean.FALSE) {
            this.tries = 30;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasArgument() {
        return this.argument.length() != 0;
    }

    private boolean shouldLoad() {
        return this.tries < 30;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableLoad() {
        this.tries = 30;
    }

    boolean isLoaded() {
        return this.provider != null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProviderConfig)) {
            return false;
        }
        ProviderConfig providerConfig = (ProviderConfig) obj;
        return this.className.equals(providerConfig.className) && this.argument.equals(providerConfig.argument);
    }

    public int hashCode() {
        return this.className.hashCode() + this.argument.hashCode();
    }

    public String toString() {
        if (hasArgument()) {
            return this.className + "('" + this.argument + "')";
        }
        return this.className;
    }

    synchronized Provider getProvider() {
        Provider provider = this.provider;
        if (provider != null) {
            return provider;
        }
        if (!shouldLoad()) {
            return null;
        }
        if (this.isLoading) {
            if (debug != null) {
                debug.println("Recursion loading provider: " + ((Object) this));
                new Exception("Call trace").printStackTrace();
                return null;
            }
            return null;
        }
        try {
            this.isLoading = true;
            this.tries++;
            Provider providerDoLoadProvider = doLoadProvider();
            this.provider = providerDoLoadProvider;
            return providerDoLoadProvider;
        } finally {
            this.isLoading = false;
        }
    }

    private Provider doLoadProvider() {
        return (Provider) AccessController.doPrivileged(new PrivilegedAction<Provider>() { // from class: sun.security.jca.ProviderConfig.2
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v25, types: [java.lang.Throwable] */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Provider run2() {
                Exception cause;
                Class<?> cls;
                Object objNewInstance;
                if (ProviderConfig.debug != null) {
                    ProviderConfig.debug.println("Loading provider: " + ((Object) ProviderConfig.this));
                }
                try {
                    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                    if (systemClassLoader == null) {
                        cls = Class.forName(ProviderConfig.this.className);
                    } else {
                        cls = systemClassLoader.loadClass(ProviderConfig.this.className);
                    }
                    if (!ProviderConfig.this.hasArgument()) {
                        objNewInstance = cls.newInstance();
                    } else {
                        objNewInstance = cls.getConstructor(ProviderConfig.CL_STRING).newInstance(ProviderConfig.this.argument);
                    }
                    if (objNewInstance instanceof Provider) {
                        if (ProviderConfig.debug != null) {
                            ProviderConfig.debug.println("Loaded provider " + objNewInstance);
                        }
                        return (Provider) objNewInstance;
                    }
                    if (ProviderConfig.debug != null) {
                        ProviderConfig.debug.println(ProviderConfig.this.className + " is not a provider");
                    }
                    ProviderConfig.this.disableLoad();
                    return null;
                } catch (Exception e2) {
                    if (e2 instanceof InvocationTargetException) {
                        cause = ((InvocationTargetException) e2).getCause();
                    } else {
                        cause = e2;
                    }
                    if (ProviderConfig.debug != null) {
                        ProviderConfig.debug.println("Error loading provider " + ((Object) ProviderConfig.this));
                        cause.printStackTrace();
                    }
                    if (cause instanceof ProviderException) {
                        throw ((ProviderException) cause);
                    }
                    if (cause instanceof UnsupportedOperationException) {
                        ProviderConfig.this.disableLoad();
                        return null;
                    }
                    return null;
                } catch (ExceptionInInitializerError e3) {
                    if (ProviderConfig.debug != null) {
                        ProviderConfig.debug.println("Error loading provider " + ((Object) ProviderConfig.this));
                        e3.printStackTrace();
                    }
                    ProviderConfig.this.disableLoad();
                    return null;
                }
            }
        });
    }

    private static String expand(final String str) {
        if (!str.contains(FXMLLoader.BINDING_EXPRESSION_PREFIX)) {
            return str;
        }
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.jca.ProviderConfig.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                try {
                    return PropertyExpander.expand(str);
                } catch (GeneralSecurityException e2) {
                    throw new ProviderException(e2);
                }
            }
        });
    }
}
