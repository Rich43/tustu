package javax.security.auth.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Security;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.AuthPermission;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import sun.security.util.Debug;
import sun.security.util.PendingException;
import sun.security.util.ResourcesMgr;

/* loaded from: rt.jar:javax/security/auth/login/LoginContext.class */
public class LoginContext {
    private static final String INIT_METHOD = "initialize";
    private static final String LOGIN_METHOD = "login";
    private static final String COMMIT_METHOD = "commit";
    private static final String ABORT_METHOD = "abort";
    private static final String LOGOUT_METHOD = "logout";
    private static final String OTHER = "other";
    private static final String DEFAULT_HANDLER = "auth.login.defaultCallbackHandler";
    private Subject subject;
    private boolean subjectProvided;
    private boolean loginSucceeded;
    private CallbackHandler callbackHandler;
    private Map<String, ?> state;
    private Configuration config;
    private AccessControlContext creatorAcc;
    private ModuleInfo[] moduleStack;
    private ClassLoader contextClassLoader;
    private int moduleIndex;
    private LoginException firstError;
    private LoginException firstRequiredError;
    private boolean success;
    private static final Class<?>[] PARAMS = new Class[0];
    private static final Debug debug = Debug.getInstance("logincontext", "\t[LoginContext]");

    private void init(String str) throws LoginException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && this.creatorAcc == null) {
            securityManager.checkPermission(new AuthPermission("createLoginContext." + str));
        }
        if (str == null) {
            throw new LoginException(ResourcesMgr.getString("Invalid.null.input.name"));
        }
        if (this.config == null) {
            this.config = (Configuration) AccessController.doPrivileged(new PrivilegedAction<Configuration>() { // from class: javax.security.auth.login.LoginContext.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Configuration run2() {
                    return Configuration.getConfiguration();
                }
            });
        }
        AppConfigurationEntry[] appConfigurationEntry = this.config.getAppConfigurationEntry(str);
        if (appConfigurationEntry == null) {
            if (securityManager != null && this.creatorAcc == null) {
                securityManager.checkPermission(new AuthPermission("createLoginContext.other"));
            }
            appConfigurationEntry = this.config.getAppConfigurationEntry("other");
            if (appConfigurationEntry == null) {
                throw new LoginException(new MessageFormat(ResourcesMgr.getString("No.LoginModules.configured.for.name")).format(new Object[]{str}));
            }
        }
        this.moduleStack = new ModuleInfo[appConfigurationEntry.length];
        for (int i2 = 0; i2 < appConfigurationEntry.length; i2++) {
            this.moduleStack[i2] = new ModuleInfo(new AppConfigurationEntry(appConfigurationEntry[i2].getLoginModuleName(), appConfigurationEntry[i2].getControlFlag(), appConfigurationEntry[i2].getOptions()), null);
        }
        this.contextClassLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: javax.security.auth.login.LoginContext.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader == null) {
                    contextClassLoader = ClassLoader.getSystemClassLoader();
                }
                return contextClassLoader;
            }
        });
    }

    private void loadDefaultCallbackHandler() throws LoginException {
        try {
            final ClassLoader classLoader = this.contextClassLoader;
            this.callbackHandler = (CallbackHandler) AccessController.doPrivileged(new PrivilegedExceptionAction<CallbackHandler>() { // from class: javax.security.auth.login.LoginContext.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public CallbackHandler run() throws Exception {
                    String property = Security.getProperty(LoginContext.DEFAULT_HANDLER);
                    if (property == null || property.length() == 0) {
                        return null;
                    }
                    return (CallbackHandler) Class.forName(property, true, classLoader).asSubclass(CallbackHandler.class).newInstance();
                }
            });
            if (this.callbackHandler != null && this.creatorAcc == null) {
                this.callbackHandler = new SecureCallbackHandler(AccessController.getContext(), this.callbackHandler);
            }
        } catch (PrivilegedActionException e2) {
            throw new LoginException(e2.getException().toString());
        }
    }

    public LoginContext(String str) throws LoginException {
        this.subject = null;
        this.subjectProvided = false;
        this.loginSucceeded = false;
        this.state = new HashMap();
        this.creatorAcc = null;
        this.contextClassLoader = null;
        this.moduleIndex = 0;
        this.firstError = null;
        this.firstRequiredError = null;
        this.success = false;
        init(str);
        loadDefaultCallbackHandler();
    }

    public LoginContext(String str, Subject subject) throws LoginException {
        this.subject = null;
        this.subjectProvided = false;
        this.loginSucceeded = false;
        this.state = new HashMap();
        this.creatorAcc = null;
        this.contextClassLoader = null;
        this.moduleIndex = 0;
        this.firstError = null;
        this.firstRequiredError = null;
        this.success = false;
        init(str);
        if (subject == null) {
            throw new LoginException(ResourcesMgr.getString("invalid.null.Subject.provided"));
        }
        this.subject = subject;
        this.subjectProvided = true;
        loadDefaultCallbackHandler();
    }

    public LoginContext(String str, CallbackHandler callbackHandler) throws LoginException {
        this.subject = null;
        this.subjectProvided = false;
        this.loginSucceeded = false;
        this.state = new HashMap();
        this.creatorAcc = null;
        this.contextClassLoader = null;
        this.moduleIndex = 0;
        this.firstError = null;
        this.firstRequiredError = null;
        this.success = false;
        init(str);
        if (callbackHandler == null) {
            throw new LoginException(ResourcesMgr.getString("invalid.null.CallbackHandler.provided"));
        }
        this.callbackHandler = new SecureCallbackHandler(AccessController.getContext(), callbackHandler);
    }

    public LoginContext(String str, Subject subject, CallbackHandler callbackHandler) throws LoginException {
        this(str, subject);
        if (callbackHandler == null) {
            throw new LoginException(ResourcesMgr.getString("invalid.null.CallbackHandler.provided"));
        }
        this.callbackHandler = new SecureCallbackHandler(AccessController.getContext(), callbackHandler);
    }

    public LoginContext(String str, Subject subject, CallbackHandler callbackHandler, Configuration configuration) throws LoginException {
        this.subject = null;
        this.subjectProvided = false;
        this.loginSucceeded = false;
        this.state = new HashMap();
        this.creatorAcc = null;
        this.contextClassLoader = null;
        this.moduleIndex = 0;
        this.firstError = null;
        this.firstRequiredError = null;
        this.success = false;
        this.config = configuration;
        if (configuration != null) {
            this.creatorAcc = AccessController.getContext();
        }
        init(str);
        if (subject != null) {
            this.subject = subject;
            this.subjectProvided = true;
        }
        if (callbackHandler == null) {
            loadDefaultCallbackHandler();
        } else if (this.creatorAcc == null) {
            this.callbackHandler = new SecureCallbackHandler(AccessController.getContext(), callbackHandler);
        } else {
            this.callbackHandler = callbackHandler;
        }
    }

    public void login() throws LoginException {
        this.loginSucceeded = false;
        if (this.subject == null) {
            this.subject = new Subject();
        }
        try {
            invokePriv(LOGIN_METHOD);
            invokePriv(COMMIT_METHOD);
            this.loginSucceeded = true;
        } catch (LoginException e2) {
            try {
                invokePriv(ABORT_METHOD);
                throw e2;
            } catch (LoginException e3) {
                throw e2;
            }
        }
    }

    public void logout() throws LoginException {
        if (this.subject == null) {
            throw new LoginException(ResourcesMgr.getString("null.subject.logout.called.before.login"));
        }
        invokePriv(LOGOUT_METHOD);
    }

    public Subject getSubject() {
        if (!this.loginSucceeded && !this.subjectProvided) {
            return null;
        }
        return this.subject;
    }

    private void clearState() {
        this.moduleIndex = 0;
        this.firstError = null;
        this.firstRequiredError = null;
        this.success = false;
    }

    private void throwException(LoginException loginException, LoginException loginException2) throws LoginException {
        clearState();
        throw (loginException != null ? loginException : loginException2);
    }

    private void invokePriv(final String str) throws LoginException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: javax.security.auth.login.LoginContext.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws LoginException, SecurityException, IllegalArgumentException {
                    LoginContext.this.invoke(str);
                    return null;
                }
            }, this.creatorAcc);
        } catch (PrivilegedActionException e2) {
            throw ((LoginException) e2.getException());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invoke(String str) throws LoginException, SecurityException, IllegalArgumentException {
        LoginException loginException;
        Method[] methods;
        int i2 = this.moduleIndex;
        while (i2 < this.moduleStack.length) {
            try {
                if (this.moduleStack[i2].module != null) {
                    methods = this.moduleStack[i2].module.getClass().getMethods();
                } else {
                    this.moduleStack[i2].module = Class.forName(this.moduleStack[i2].entry.getLoginModuleName(), true, this.contextClassLoader).getConstructor(PARAMS).newInstance(new Object[0]);
                    methods = this.moduleStack[i2].module.getClass().getMethods();
                    int i3 = 0;
                    while (i3 < methods.length && !methods[i3].getName().equals("initialize")) {
                        i3++;
                    }
                    methods[i3].invoke(this.moduleStack[i2].module, this.subject, this.callbackHandler, this.state, this.moduleStack[i2].entry.getOptions());
                }
                int i4 = 0;
                while (i4 < methods.length && !methods[i4].getName().equals(str)) {
                    i4++;
                }
                if (((Boolean) methods[i4].invoke(this.moduleStack[i2].module, new Object[0])).booleanValue()) {
                    if (!str.equals(ABORT_METHOD) && !str.equals(LOGOUT_METHOD) && this.moduleStack[i2].entry.getControlFlag() == AppConfigurationEntry.LoginModuleControlFlag.SUFFICIENT && this.firstRequiredError == null) {
                        clearState();
                        if (debug != null) {
                            debug.println(str + " SUFFICIENT success");
                            return;
                        }
                        return;
                    }
                    if (debug != null) {
                        debug.println(str + " success");
                    }
                    this.success = true;
                } else if (debug != null) {
                    debug.println(str + " ignored");
                }
            } catch (ClassNotFoundException e2) {
                throwException(null, new LoginException(ResourcesMgr.getString("unable.to.find.LoginModule.class.") + e2.getMessage()));
            } catch (IllegalAccessException e3) {
                throwException(null, new LoginException(ResourcesMgr.getString("unable.to.access.LoginModule.") + e3.getMessage()));
            } catch (InstantiationException e4) {
                throwException(null, new LoginException(ResourcesMgr.getString("unable.to.instantiate.LoginModule.") + e4.getMessage()));
            } catch (NoSuchMethodException e5) {
                throwException(null, new LoginException(new MessageFormat(ResourcesMgr.getString("unable.to.instantiate.LoginModule.module.because.it.does.not.provide.a.no.argument.constructor")).format(new Object[]{this.moduleStack[i2].entry.getLoginModuleName()})));
            } catch (InvocationTargetException e6) {
                if ((e6.getCause() instanceof PendingException) && str.equals(LOGIN_METHOD)) {
                    throw ((PendingException) e6.getCause());
                }
                if (e6.getCause() instanceof LoginException) {
                    loginException = (LoginException) e6.getCause();
                } else if (e6.getCause() instanceof SecurityException) {
                    loginException = new LoginException("Security Exception");
                    loginException.initCause(new SecurityException());
                    if (debug != null) {
                        debug.println("original security exception with detail msg replaced by new exception with empty detail msg");
                        debug.println("original security exception: " + e6.getCause().toString());
                    }
                } else {
                    StringWriter stringWriter = new StringWriter();
                    e6.getCause().printStackTrace(new PrintWriter(stringWriter));
                    stringWriter.flush();
                    loginException = new LoginException(stringWriter.toString());
                }
                if (this.moduleStack[i2].entry.getControlFlag() == AppConfigurationEntry.LoginModuleControlFlag.REQUISITE) {
                    if (debug != null) {
                        debug.println(str + " REQUISITE failure");
                    }
                    if (!str.equals(ABORT_METHOD) && !str.equals(LOGOUT_METHOD)) {
                        throwException(this.firstRequiredError, loginException);
                    } else if (this.firstRequiredError == null) {
                        this.firstRequiredError = loginException;
                    }
                } else if (this.moduleStack[i2].entry.getControlFlag() == AppConfigurationEntry.LoginModuleControlFlag.REQUIRED) {
                    if (debug != null) {
                        debug.println(str + " REQUIRED failure");
                    }
                    if (this.firstRequiredError == null) {
                        this.firstRequiredError = loginException;
                    }
                } else {
                    if (debug != null) {
                        debug.println(str + " OPTIONAL failure");
                    }
                    if (this.firstError == null) {
                        this.firstError = loginException;
                    }
                }
            }
            i2++;
            this.moduleIndex++;
        }
        if (this.firstRequiredError != null) {
            throwException(this.firstRequiredError, null);
            return;
        }
        if (!this.success && this.firstError != null) {
            throwException(this.firstError, null);
        } else if (this.success) {
            clearState();
        } else {
            throwException(new LoginException(ResourcesMgr.getString("Login.Failure.all.modules.ignored")), null);
        }
    }

    /* loaded from: rt.jar:javax/security/auth/login/LoginContext$SecureCallbackHandler.class */
    private static class SecureCallbackHandler implements CallbackHandler {
        private final AccessControlContext acc;
        private final CallbackHandler ch;

        SecureCallbackHandler(AccessControlContext accessControlContext, CallbackHandler callbackHandler) {
            this.acc = accessControlContext;
            this.ch = callbackHandler;
        }

        @Override // javax.security.auth.callback.CallbackHandler
        public void handle(final Callback[] callbackArr) throws UnsupportedCallbackException, IOException {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: javax.security.auth.login.LoginContext.SecureCallbackHandler.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Void run() throws UnsupportedCallbackException, IOException {
                        SecureCallbackHandler.this.ch.handle(callbackArr);
                        return null;
                    }
                }, this.acc);
            } catch (PrivilegedActionException e2) {
                if (e2.getException() instanceof IOException) {
                    throw ((IOException) e2.getException());
                }
                throw ((UnsupportedCallbackException) e2.getException());
            }
        }
    }

    /* loaded from: rt.jar:javax/security/auth/login/LoginContext$ModuleInfo.class */
    private static class ModuleInfo {
        AppConfigurationEntry entry;
        Object module;

        ModuleInfo(AppConfigurationEntry appConfigurationEntry, Object obj) {
            this.entry = appConfigurationEntry;
            this.module = obj;
        }
    }
}
