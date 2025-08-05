package javax.security.auth.login;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Provider;
import java.security.Security;
import java.util.Objects;
import javax.security.auth.AuthPermission;
import sun.security.jca.GetInstance;

/* loaded from: rt.jar:javax/security/auth/login/Configuration.class */
public abstract class Configuration {
    private static Configuration configuration;
    private final AccessControlContext acc = AccessController.getContext();

    /* loaded from: rt.jar:javax/security/auth/login/Configuration$Parameters.class */
    public interface Parameters {
    }

    public abstract AppConfigurationEntry[] getAppConfigurationEntry(String str);

    private static void checkPermission(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AuthPermission("createLoginConfiguration." + str));
        }
    }

    protected Configuration() {
    }

    public static Configuration getConfiguration() {
        Configuration configuration2;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AuthPermission("getLoginConfiguration"));
        }
        synchronized (Configuration.class) {
            if (configuration == null) {
                String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.security.auth.login.Configuration.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public String run() {
                        return Security.getProperty("login.configuration.provider");
                    }
                });
                if (str == null) {
                    str = "sun.security.provider.ConfigFile";
                }
                try {
                    final String str2 = str;
                    Configuration configuration3 = (Configuration) AccessController.doPrivileged(new PrivilegedExceptionAction<Configuration>() { // from class: javax.security.auth.login.Configuration.2
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Configuration run() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
                            return (Configuration) Class.forName(str2, false, Thread.currentThread().getContextClassLoader()).asSubclass(Configuration.class).newInstance();
                        }
                    });
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: javax.security.auth.login.Configuration.3
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Void run() {
                            Configuration.setConfiguration(Configuration.this);
                            return null;
                        }
                    }, (AccessControlContext) Objects.requireNonNull(configuration3.acc));
                } catch (PrivilegedActionException e2) {
                    Exception exception = e2.getException();
                    if (exception instanceof InstantiationException) {
                        throw ((SecurityException) new SecurityException("Configuration error:" + exception.getCause().getMessage() + "\n").initCause(exception.getCause()));
                    }
                    throw ((SecurityException) new SecurityException("Configuration error: " + exception.toString() + "\n").initCause(exception));
                }
            }
            configuration2 = configuration;
        }
        return configuration2;
    }

    public static void setConfiguration(Configuration configuration2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AuthPermission("setLoginConfiguration"));
        }
        configuration = configuration2;
    }

    public static Configuration getInstance(String str, Parameters parameters) throws NoSuchAlgorithmException {
        checkPermission(str);
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("Configuration", (Class<?>) ConfigurationSpi.class, str, parameters);
            return new ConfigDelegate((ConfigurationSpi) getInstance.impl, getInstance.provider, str, parameters);
        } catch (NoSuchAlgorithmException e2) {
            return handleException(e2);
        }
    }

    public static Configuration getInstance(String str, Parameters parameters, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        }
        checkPermission(str);
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("Configuration", (Class<?>) ConfigurationSpi.class, str, parameters, str2);
            return new ConfigDelegate((ConfigurationSpi) getInstance.impl, getInstance.provider, str, parameters);
        } catch (NoSuchAlgorithmException e2) {
            return handleException(e2);
        }
    }

    public static Configuration getInstance(String str, Parameters parameters, Provider provider) throws NoSuchAlgorithmException {
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        checkPermission(str);
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("Configuration", (Class<?>) ConfigurationSpi.class, str, parameters, provider);
            return new ConfigDelegate((ConfigurationSpi) getInstance.impl, getInstance.provider, str, parameters);
        } catch (NoSuchAlgorithmException e2) {
            return handleException(e2);
        }
    }

    private static Configuration handleException(NoSuchAlgorithmException noSuchAlgorithmException) throws NoSuchAlgorithmException {
        Throwable cause = noSuchAlgorithmException.getCause();
        if (cause instanceof IllegalArgumentException) {
            throw ((IllegalArgumentException) cause);
        }
        throw noSuchAlgorithmException;
    }

    public Provider getProvider() {
        return null;
    }

    public String getType() {
        return null;
    }

    public Parameters getParameters() {
        return null;
    }

    public void refresh() {
    }

    /* loaded from: rt.jar:javax/security/auth/login/Configuration$ConfigDelegate.class */
    private static class ConfigDelegate extends Configuration {
        private ConfigurationSpi spi;

        /* renamed from: p, reason: collision with root package name */
        private Provider f12799p;
        private String type;
        private Parameters params;

        private ConfigDelegate(ConfigurationSpi configurationSpi, Provider provider, String str, Parameters parameters) {
            this.spi = configurationSpi;
            this.f12799p = provider;
            this.type = str;
            this.params = parameters;
        }

        @Override // javax.security.auth.login.Configuration
        public String getType() {
            return this.type;
        }

        @Override // javax.security.auth.login.Configuration
        public Parameters getParameters() {
            return this.params;
        }

        @Override // javax.security.auth.login.Configuration
        public Provider getProvider() {
            return this.f12799p;
        }

        @Override // javax.security.auth.login.Configuration
        public AppConfigurationEntry[] getAppConfigurationEntry(String str) {
            return this.spi.engineGetAppConfigurationEntry(str);
        }

        @Override // javax.security.auth.login.Configuration
        public void refresh() {
            this.spi.engineRefresh();
        }
    }
}
