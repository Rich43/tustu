package com.sun.jmx.remote.security;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Util;
import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import javax.management.remote.JMXPrincipal;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import sun.management.jmxremote.ConnectorBootstrap;

/* loaded from: rt.jar:com/sun/jmx/remote/security/FileLoginModule.class */
public class FileLoginModule implements LoginModule {
    private static final String USERNAME_KEY = "javax.security.auth.login.name";
    private static final String PASSWORD_KEY = "javax.security.auth.login.password";
    private boolean useFirstPass = false;
    private boolean tryFirstPass = false;
    private boolean storePass = false;
    private boolean clearPass = false;
    private boolean succeeded = false;
    private boolean commitSucceeded = false;
    private String username;
    private char[] password;
    private JMXPrincipal user;
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, Object> sharedState;
    private Map<String, ?> options;
    private String passwordFile;
    private String passwordFileDisplayName;
    private boolean userSuppliedPasswordFile;
    private boolean hasJavaHomePermission;
    private Properties userCredentials;
    private static final String DEFAULT_PASSWORD_FILE_NAME = ((String) AccessController.doPrivileged(new GetPropertyAction("java.home"))) + File.separatorChar + "lib" + File.separatorChar + "management" + File.separatorChar + ConnectorBootstrap.DefaultValues.PASSWORD_FILE_NAME;
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "FileLoginModule");

    @Override // javax.security.auth.spi.LoginModule
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> map, Map<String, ?> map2) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = (Map) Util.cast(map);
        this.options = map2;
        this.tryFirstPass = "true".equalsIgnoreCase((String) map2.get("tryFirstPass"));
        this.useFirstPass = "true".equalsIgnoreCase((String) map2.get("useFirstPass"));
        this.storePass = "true".equalsIgnoreCase((String) map2.get("storePass"));
        this.clearPass = "true".equalsIgnoreCase((String) map2.get("clearPass"));
        this.passwordFile = (String) map2.get("passwordFile");
        this.passwordFileDisplayName = this.passwordFile;
        this.userSuppliedPasswordFile = true;
        if (this.passwordFile == null) {
            this.passwordFile = DEFAULT_PASSWORD_FILE_NAME;
            this.userSuppliedPasswordFile = false;
            try {
                System.getProperty("java.home");
                this.hasJavaHomePermission = true;
                this.passwordFileDisplayName = this.passwordFile;
            } catch (SecurityException e2) {
                this.hasJavaHomePermission = false;
                this.passwordFileDisplayName = ConnectorBootstrap.DefaultValues.PASSWORD_FILE_NAME;
            }
        }
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean login() throws LoginException {
        try {
            loadPasswordFile();
            if (this.userCredentials == null) {
                throw new LoginException("Error: unable to locate the users' credentials.");
            }
            if (logger.debugOn()) {
                logger.debug("login", "Using password file: " + this.passwordFileDisplayName);
            }
            if (this.tryFirstPass) {
                try {
                    attemptAuthentication(true);
                    this.succeeded = true;
                    if (logger.debugOn()) {
                        logger.debug("login", "Authentication using cached password has succeeded");
                        return true;
                    }
                    return true;
                } catch (LoginException e2) {
                    cleanState();
                    logger.debug("login", "Authentication using cached password has failed");
                }
            } else if (this.useFirstPass) {
                try {
                    attemptAuthentication(true);
                    this.succeeded = true;
                    if (logger.debugOn()) {
                        logger.debug("login", "Authentication using cached password has succeeded");
                        return true;
                    }
                    return true;
                } catch (LoginException e3) {
                    cleanState();
                    logger.debug("login", "Authentication using cached password has failed");
                    throw e3;
                }
            }
            if (logger.debugOn()) {
                logger.debug("login", "Acquiring password");
            }
            try {
                attemptAuthentication(false);
                this.succeeded = true;
                if (logger.debugOn()) {
                    logger.debug("login", "Authentication has succeeded");
                    return true;
                }
                return true;
            } catch (LoginException e4) {
                cleanState();
                logger.debug("login", "Authentication has failed");
                throw e4;
            }
        } catch (IOException e5) {
            throw ((LoginException) EnvHelp.initCause(new LoginException("Error: unable to load the password file: " + this.passwordFileDisplayName), e5));
        }
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean commit() throws LoginException {
        if (!this.succeeded) {
            return false;
        }
        if (this.subject.isReadOnly()) {
            cleanState();
            throw new LoginException("Subject is read-only");
        }
        if (!this.subject.getPrincipals().contains(this.user)) {
            this.subject.getPrincipals().add(this.user);
        }
        if (logger.debugOn()) {
            logger.debug("commit", "Authentication has completed successfully");
        }
        cleanState();
        this.commitSucceeded = true;
        return true;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean abort() throws LoginException {
        if (logger.debugOn()) {
            logger.debug("abort", "Authentication has not completed successfully");
        }
        if (!this.succeeded) {
            return false;
        }
        if (this.succeeded && !this.commitSucceeded) {
            this.succeeded = false;
            cleanState();
            this.user = null;
            return true;
        }
        logout();
        return true;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean logout() throws LoginException {
        if (this.subject.isReadOnly()) {
            cleanState();
            throw new LoginException("Subject is read-only");
        }
        this.subject.getPrincipals().remove(this.user);
        cleanState();
        this.succeeded = false;
        this.commitSucceeded = false;
        this.user = null;
        if (logger.debugOn()) {
            logger.debug("logout", "Subject is being logged out");
            return true;
        }
        return true;
    }

    private void attemptAuthentication(boolean z2) throws LoginException {
        getUsernamePassword(z2);
        String property = this.userCredentials.getProperty(this.username);
        if (property == null || !property.equals(new String(this.password))) {
            if (logger.debugOn()) {
                logger.debug("login", "Invalid username or password");
            }
            throw new FailedLoginException("Invalid username or password");
        }
        if (this.storePass && !this.sharedState.containsKey(USERNAME_KEY) && !this.sharedState.containsKey(PASSWORD_KEY)) {
            this.sharedState.put(USERNAME_KEY, this.username);
            this.sharedState.put(PASSWORD_KEY, this.password);
        }
        this.user = new JMXPrincipal(this.username);
        if (logger.debugOn()) {
            logger.debug("login", "User '" + this.username + "' successfully validated");
        }
    }

    private void loadPasswordFile() throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.passwordFile);
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                try {
                    this.userCredentials = new Properties();
                    this.userCredentials.load(bufferedInputStream);
                    bufferedInputStream.close();
                } catch (Throwable th) {
                    bufferedInputStream.close();
                    throw th;
                }
            } finally {
                fileInputStream.close();
            }
        } catch (SecurityException e2) {
            if (this.userSuppliedPasswordFile || this.hasJavaHomePermission) {
                throw e2;
            }
            AccessControlException accessControlException = new AccessControlException("access denied " + new FilePermission(this.passwordFileDisplayName, "read").toString());
            accessControlException.setStackTrace(e2.getStackTrace());
            throw accessControlException;
        }
    }

    private void getUsernamePassword(boolean z2) throws LoginException {
        if (z2) {
            this.username = (String) this.sharedState.get(USERNAME_KEY);
            this.password = (char[]) this.sharedState.get(PASSWORD_KEY);
            return;
        }
        if (this.callbackHandler == null) {
            throw new LoginException("Error: no CallbackHandler available to garner authentication information from the user");
        }
        Callback[] callbackArr = {new NameCallback("username"), new PasswordCallback("password", false)};
        try {
            this.callbackHandler.handle(callbackArr);
            this.username = ((NameCallback) callbackArr[0]).getName();
            char[] password = ((PasswordCallback) callbackArr[1]).getPassword();
            this.password = new char[password.length];
            System.arraycopy(password, 0, this.password, 0, password.length);
            ((PasswordCallback) callbackArr[1]).clearPassword();
        } catch (IOException e2) {
            throw ((LoginException) EnvHelp.initCause(new LoginException(e2.toString()), e2));
        } catch (UnsupportedCallbackException e3) {
            throw ((LoginException) EnvHelp.initCause(new LoginException("Error: " + e3.getCallback().toString() + " not available to garner authentication information from the user"), e3));
        }
    }

    private void cleanState() {
        this.username = null;
        if (this.password != null) {
            Arrays.fill(this.password, ' ');
            this.password = null;
        }
        if (this.clearPass) {
            this.sharedState.remove(USERNAME_KEY);
            this.sharedState.remove(PASSWORD_KEY);
        }
    }
}
