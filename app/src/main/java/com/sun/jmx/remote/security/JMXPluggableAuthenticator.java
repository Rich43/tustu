package com.sun.jmx.remote.security;

import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.management.remote.JMXAuthenticator;
import javax.security.auth.AuthPermission;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/* loaded from: rt.jar:com/sun/jmx/remote/security/JMXPluggableAuthenticator.class */
public final class JMXPluggableAuthenticator implements JMXAuthenticator {
    private LoginContext loginContext;
    private String username;
    private String password;
    private static final String LOGIN_CONFIG_PROP = "jmx.remote.x.login.config";
    private static final String PASSWORD_FILE_PROP = "jmx.remote.x.password.file";
    private static final String LOGIN_CONFIG_NAME = "JMXPluggableAuthenticator";
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", LOGIN_CONFIG_NAME);

    public JMXPluggableAuthenticator(Map<?, ?> map) throws LoginException, SecurityException {
        String str = null;
        String str2 = null;
        if (map != null) {
            str = (String) map.get(LOGIN_CONFIG_PROP);
            str2 = (String) map.get(PASSWORD_FILE_PROP);
        }
        try {
            if (str != null) {
                this.loginContext = new LoginContext(str, new JMXCallbackHandler());
            } else {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkPermission(new AuthPermission("createLoginContext.JMXPluggableAuthenticator"));
                }
                final String str3 = str2;
                try {
                    this.loginContext = (LoginContext) AccessController.doPrivileged(new PrivilegedExceptionAction<LoginContext>() { // from class: com.sun.jmx.remote.security.JMXPluggableAuthenticator.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public LoginContext run() throws LoginException {
                            return new LoginContext(JMXPluggableAuthenticator.LOGIN_CONFIG_NAME, null, new JMXCallbackHandler(), new FileLoginConfig(str3));
                        }
                    });
                } catch (PrivilegedActionException e2) {
                    throw ((LoginException) e2.getException());
                }
            }
        } catch (SecurityException e3) {
            authenticationFailure("authenticate", e3);
        } catch (LoginException e4) {
            authenticationFailure("authenticate", e4);
        }
    }

    @Override // javax.management.remote.JMXAuthenticator
    public Subject authenticate(Object obj) throws SecurityException {
        if (!(obj instanceof String[])) {
            if (obj == null) {
                authenticationFailure("authenticate", "Credentials required");
            }
            authenticationFailure("authenticate", "Credentials should be String[] instead of " + obj.getClass().getName());
        }
        String[] strArr = (String[]) obj;
        if (strArr.length != 2) {
            authenticationFailure("authenticate", "Credentials should have 2 elements not " + strArr.length);
        }
        this.username = strArr[0];
        this.password = strArr[1];
        if (this.username == null || this.password == null) {
            authenticationFailure("authenticate", "Username or password is null");
        }
        try {
            this.loginContext.login();
            final Subject subject = this.loginContext.getSubject();
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.jmx.remote.security.JMXPluggableAuthenticator.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    subject.setReadOnly();
                    return null;
                }
            });
            return subject;
        } catch (LoginException e2) {
            authenticationFailure("authenticate", e2);
            return null;
        }
    }

    private static void authenticationFailure(String str, String str2) throws SecurityException {
        String str3 = "Authentication failed! " + str2;
        SecurityException securityException = new SecurityException(str3);
        logException(str, str3, securityException);
        throw securityException;
    }

    private static void authenticationFailure(String str, Exception exc) throws SecurityException {
        String message;
        SecurityException securityException;
        if (exc instanceof SecurityException) {
            message = exc.getMessage();
            securityException = (SecurityException) exc;
        } else {
            message = "Authentication failed! " + exc.getMessage();
            SecurityException securityException2 = new SecurityException(message);
            EnvHelp.initCause(securityException2, exc);
            securityException = securityException2;
        }
        logException(str, message, securityException);
        throw securityException;
    }

    private static void logException(String str, String str2, Exception exc) {
        if (logger.traceOn()) {
            logger.trace(str, str2);
        }
        if (logger.debugOn()) {
            logger.debug(str, exc);
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/security/JMXPluggableAuthenticator$JMXCallbackHandler.class */
    private final class JMXCallbackHandler implements CallbackHandler {
        private JMXCallbackHandler() {
        }

        @Override // javax.security.auth.callback.CallbackHandler
        public void handle(Callback[] callbackArr) throws UnsupportedCallbackException, IOException {
            for (int i2 = 0; i2 < callbackArr.length; i2++) {
                if (callbackArr[i2] instanceof NameCallback) {
                    ((NameCallback) callbackArr[i2]).setName(JMXPluggableAuthenticator.this.username);
                } else if (callbackArr[i2] instanceof PasswordCallback) {
                    ((PasswordCallback) callbackArr[i2]).setPassword(JMXPluggableAuthenticator.this.password.toCharArray());
                } else {
                    throw new UnsupportedCallbackException(callbackArr[i2], "Unrecognized Callback");
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/security/JMXPluggableAuthenticator$FileLoginConfig.class */
    private static class FileLoginConfig extends Configuration {
        private AppConfigurationEntry[] entries;
        private static final String FILE_LOGIN_MODULE = FileLoginModule.class.getName();
        private static final String PASSWORD_FILE_OPTION = "passwordFile";

        public FileLoginConfig(String str) {
            Map mapEmptyMap;
            if (str != null) {
                mapEmptyMap = new HashMap(1);
                mapEmptyMap.put(PASSWORD_FILE_OPTION, str);
            } else {
                mapEmptyMap = Collections.emptyMap();
            }
            this.entries = new AppConfigurationEntry[]{new AppConfigurationEntry(FILE_LOGIN_MODULE, AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, mapEmptyMap)};
        }

        @Override // javax.security.auth.login.Configuration
        public AppConfigurationEntry[] getAppConfigurationEntry(String str) {
            if (str.equals(JMXPluggableAuthenticator.LOGIN_CONFIG_NAME)) {
                return this.entries;
            }
            return null;
        }

        @Override // javax.security.auth.login.Configuration
        public void refresh() {
        }
    }
}
