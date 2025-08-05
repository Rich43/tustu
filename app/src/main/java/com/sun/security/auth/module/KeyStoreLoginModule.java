package com.sun.security.auth.module;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.security.auth.x500.X500Principal;
import javax.security.auth.x500.X500PrivateCredential;
import jdk.Exported;
import org.icepdf.core.util.PdfOps;
import sun.security.util.Password;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/module/KeyStoreLoginModule.class */
public class KeyStoreLoginModule implements LoginModule {
    private static final int UNINITIALIZED = 0;
    private static final int INITIALIZED = 1;
    private static final int AUTHENTICATED = 2;
    private static final int LOGGED_IN = 3;
    private static final int PROTECTED_PATH = 0;
    private static final int TOKEN = 1;
    private static final int NORMAL = 2;
    private static final String NONE = "NONE";
    private static final String P11KEYSTORE = "PKCS11";
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, Object> sharedState;
    private Map<String, ?> options;
    private char[] keyStorePassword;
    private char[] privateKeyPassword;
    private KeyStore keyStore;
    private String keyStoreURL;
    private String keyStoreType;
    private String keyStoreProvider;
    private String keyStoreAlias;
    private String keyStorePasswordURL;
    private String privateKeyPasswordURL;
    private boolean debug;
    private X500Principal principal;
    private Certificate[] fromKeyStore;
    private X500PrivateCredential privateCredential;
    private static final ResourceBundle rb = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: com.sun.security.auth.module.KeyStoreLoginModule.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ResourceBundle run2() {
            return ResourceBundle.getBundle("sun.security.util.AuthResources");
        }
    });
    private static final TextOutputCallback bannerCallback = new TextOutputCallback(0, rb.getString("Please.enter.keystore.information"));
    private final ConfirmationCallback confirmationCallback = new ConfirmationCallback(0, 2, 3);
    private CertPath certP = null;
    private int status = 0;
    private boolean nullStream = false;
    private boolean token = false;
    private boolean protectedPath = false;

    @Override // javax.security.auth.spi.LoginModule
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> map, Map<String, ?> map2) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = map;
        this.options = map2;
        processOptions();
        this.status = 1;
    }

    private void processOptions() {
        this.keyStoreURL = (String) this.options.get("keyStoreURL");
        if (this.keyStoreURL == null) {
            this.keyStoreURL = "file:" + System.getProperty("user.home").replace(File.separatorChar, '/') + "/.keystore";
        } else if (NONE.equals(this.keyStoreURL)) {
            this.nullStream = true;
        }
        this.keyStoreType = (String) this.options.get("keyStoreType");
        if (this.keyStoreType == null) {
            this.keyStoreType = KeyStore.getDefaultType();
        }
        if (P11KEYSTORE.equalsIgnoreCase(this.keyStoreType)) {
            this.token = true;
        }
        this.keyStoreProvider = (String) this.options.get("keyStoreProvider");
        this.keyStoreAlias = (String) this.options.get("keyStoreAlias");
        this.keyStorePasswordURL = (String) this.options.get("keyStorePasswordURL");
        this.privateKeyPasswordURL = (String) this.options.get("privateKeyPasswordURL");
        this.protectedPath = "true".equalsIgnoreCase((String) this.options.get("protected"));
        this.debug = "true".equalsIgnoreCase((String) this.options.get(TransformerFactoryImpl.DEBUG));
        if (this.debug) {
            debugPrint(null);
            debugPrint("keyStoreURL=" + this.keyStoreURL);
            debugPrint("keyStoreType=" + this.keyStoreType);
            debugPrint("keyStoreProvider=" + this.keyStoreProvider);
            debugPrint("keyStoreAlias=" + this.keyStoreAlias);
            debugPrint("keyStorePasswordURL=" + this.keyStorePasswordURL);
            debugPrint("privateKeyPasswordURL=" + this.privateKeyPasswordURL);
            debugPrint("protectedPath=" + this.protectedPath);
            debugPrint(null);
        }
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean login() throws LoginException {
        switch (this.status) {
            case 0:
            default:
                throw new LoginException("The login module is not initialized");
            case 1:
            case 2:
                if (this.token && !this.nullStream) {
                    throw new LoginException("if keyStoreType is PKCS11 then keyStoreURL must be NONE");
                }
                if (this.token && this.privateKeyPasswordURL != null) {
                    throw new LoginException("if keyStoreType is PKCS11 then privateKeyPasswordURL must not be specified");
                }
                if (this.protectedPath && (this.keyStorePasswordURL != null || this.privateKeyPasswordURL != null)) {
                    throw new LoginException("if protected is true then keyStorePasswordURL and privateKeyPasswordURL must not be specified");
                }
                if (this.protectedPath) {
                    getAliasAndPasswords(0);
                } else if (this.token) {
                    getAliasAndPasswords(1);
                } else {
                    getAliasAndPasswords(2);
                }
                try {
                    getKeyStoreInfo();
                    this.status = 2;
                    return true;
                } finally {
                    if (this.privateKeyPassword != null && this.privateKeyPassword != this.keyStorePassword) {
                        Arrays.fill(this.privateKeyPassword, (char) 0);
                        this.privateKeyPassword = null;
                    }
                    if (this.keyStorePassword != null) {
                        Arrays.fill(this.keyStorePassword, (char) 0);
                        this.keyStorePassword = null;
                    }
                }
            case 3:
                return true;
        }
    }

    private void getAliasAndPasswords(int i2) throws LoginException {
        NameCallback nameCallback;
        if (this.callbackHandler == null) {
            switch (i2) {
                case 0:
                    checkAlias();
                    break;
                case 1:
                    checkAlias();
                    checkStorePass();
                    break;
                case 2:
                    checkAlias();
                    checkStorePass();
                    checkKeyPass();
                    break;
            }
        } else {
            if (this.keyStoreAlias == null || this.keyStoreAlias.length() == 0) {
                nameCallback = new NameCallback(rb.getString("Keystore.alias."));
            } else {
                nameCallback = new NameCallback(rb.getString("Keystore.alias."), this.keyStoreAlias);
            }
            PasswordCallback passwordCallback = null;
            PasswordCallback passwordCallback2 = null;
            switch (i2) {
                case 2:
                    passwordCallback2 = new PasswordCallback(rb.getString("Private.key.password.optional."), false);
                case 1:
                    passwordCallback = new PasswordCallback(rb.getString("Keystore.password."), false);
                    break;
            }
            prompt(nameCallback, passwordCallback, passwordCallback2);
        }
        if (this.debug) {
            debugPrint("alias=" + this.keyStoreAlias);
        }
    }

    private void checkAlias() throws LoginException {
        if (this.keyStoreAlias == null) {
            throw new LoginException("Need to specify an alias option to use KeyStoreLoginModule non-interactively.");
        }
    }

    private void checkStorePass() throws LoginException {
        if (this.keyStorePasswordURL == null) {
            throw new LoginException("Need to specify keyStorePasswordURL option to use KeyStoreLoginModule non-interactively.");
        }
        InputStream inputStreamOpenStream = null;
        try {
            try {
                inputStreamOpenStream = new URL(this.keyStorePasswordURL).openStream();
                this.keyStorePassword = Password.readPassword(inputStreamOpenStream);
                if (inputStreamOpenStream != null) {
                    try {
                        inputStreamOpenStream.close();
                    } catch (IOException e2) {
                        LoginException loginException = new LoginException("Problem closing the keystore password stream");
                        loginException.initCause(e2);
                        throw loginException;
                    }
                }
            } catch (Throwable th) {
                if (inputStreamOpenStream != null) {
                    try {
                        inputStreamOpenStream.close();
                    } catch (IOException e3) {
                        LoginException loginException2 = new LoginException("Problem closing the keystore password stream");
                        loginException2.initCause(e3);
                        throw loginException2;
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            LoginException loginException3 = new LoginException("Problem accessing keystore password \"" + this.keyStorePasswordURL + PdfOps.DOUBLE_QUOTE__TOKEN);
            loginException3.initCause(e4);
            throw loginException3;
        }
    }

    private void checkKeyPass() throws LoginException {
        if (this.privateKeyPasswordURL == null) {
            this.privateKeyPassword = this.keyStorePassword;
            return;
        }
        InputStream inputStreamOpenStream = null;
        try {
            try {
                inputStreamOpenStream = new URL(this.privateKeyPasswordURL).openStream();
                this.privateKeyPassword = Password.readPassword(inputStreamOpenStream);
                if (inputStreamOpenStream != null) {
                    try {
                        inputStreamOpenStream.close();
                    } catch (IOException e2) {
                        LoginException loginException = new LoginException("Problem closing the private key password stream");
                        loginException.initCause(e2);
                        throw loginException;
                    }
                }
            } catch (Throwable th) {
                if (inputStreamOpenStream != null) {
                    try {
                        inputStreamOpenStream.close();
                    } catch (IOException e3) {
                        LoginException loginException2 = new LoginException("Problem closing the private key password stream");
                        loginException2.initCause(e3);
                        throw loginException2;
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            LoginException loginException3 = new LoginException("Problem accessing private key password \"" + this.privateKeyPasswordURL + PdfOps.DOUBLE_QUOTE__TOKEN);
            loginException3.initCause(e4);
            throw loginException3;
        }
    }

    private void prompt(NameCallback nameCallback, PasswordCallback passwordCallback, PasswordCallback passwordCallback2) throws LoginException {
        if (passwordCallback == null) {
            try {
                this.callbackHandler.handle(new Callback[]{bannerCallback, nameCallback, this.confirmationCallback});
                if (this.confirmationCallback.getSelectedIndex() == 2) {
                    throw new LoginException("Login cancelled");
                }
                saveAlias(nameCallback);
                return;
            } catch (IOException e2) {
                LoginException loginException = new LoginException("Problem retrieving keystore alias");
                loginException.initCause(e2);
                throw loginException;
            } catch (UnsupportedCallbackException e3) {
                throw new LoginException("Error: " + e3.getCallback().toString() + " is not available to retrieve authentication  information from the user");
            }
        }
        if (passwordCallback2 == null) {
            try {
                this.callbackHandler.handle(new Callback[]{bannerCallback, nameCallback, passwordCallback, this.confirmationCallback});
                if (this.confirmationCallback.getSelectedIndex() == 2) {
                    throw new LoginException("Login cancelled");
                }
                saveAlias(nameCallback);
                saveStorePass(passwordCallback);
                return;
            } catch (IOException e4) {
                LoginException loginException2 = new LoginException("Problem retrieving keystore alias and password");
                loginException2.initCause(e4);
                throw loginException2;
            } catch (UnsupportedCallbackException e5) {
                throw new LoginException("Error: " + e5.getCallback().toString() + " is not available to retrieve authentication  information from the user");
            }
        }
        try {
            this.callbackHandler.handle(new Callback[]{bannerCallback, nameCallback, passwordCallback, passwordCallback2, this.confirmationCallback});
            if (this.confirmationCallback.getSelectedIndex() == 2) {
                throw new LoginException("Login cancelled");
            }
            saveAlias(nameCallback);
            saveStorePass(passwordCallback);
            saveKeyPass(passwordCallback2);
        } catch (IOException e6) {
            LoginException loginException3 = new LoginException("Problem retrieving keystore alias and passwords");
            loginException3.initCause(e6);
            throw loginException3;
        } catch (UnsupportedCallbackException e7) {
            throw new LoginException("Error: " + e7.getCallback().toString() + " is not available to retrieve authentication  information from the user");
        }
    }

    private void saveAlias(NameCallback nameCallback) {
        this.keyStoreAlias = nameCallback.getName();
    }

    private void saveStorePass(PasswordCallback passwordCallback) {
        this.keyStorePassword = passwordCallback.getPassword();
        if (this.keyStorePassword == null) {
            this.keyStorePassword = new char[0];
        }
        passwordCallback.clearPassword();
    }

    private void saveKeyPass(PasswordCallback passwordCallback) {
        this.privateKeyPassword = passwordCallback.getPassword();
        if (this.privateKeyPassword == null || this.privateKeyPassword.length == 0) {
            this.privateKeyPassword = this.keyStorePassword;
        }
        passwordCallback.clearPassword();
    }

    private void getKeyStoreInfo() throws LoginException {
        try {
            if (this.keyStoreProvider == null) {
                this.keyStore = KeyStore.getInstance(this.keyStoreType);
            } else {
                this.keyStore = KeyStore.getInstance(this.keyStoreType, this.keyStoreProvider);
            }
            InputStream inputStreamOpenStream = null;
            try {
                try {
                    try {
                        if (this.nullStream) {
                            this.keyStore.load(null, this.keyStorePassword);
                        } else {
                            inputStreamOpenStream = new URL(this.keyStoreURL).openStream();
                            this.keyStore.load(inputStreamOpenStream, this.keyStorePassword);
                        }
                        if (inputStreamOpenStream != null) {
                            try {
                                inputStreamOpenStream.close();
                            } catch (IOException e2) {
                                LoginException loginException = new LoginException("Error initializing keystore");
                                loginException.initCause(e2);
                                throw loginException;
                            }
                        }
                        try {
                            this.fromKeyStore = this.keyStore.getCertificateChain(this.keyStoreAlias);
                            if (this.fromKeyStore == null || this.fromKeyStore.length == 0 || !(this.fromKeyStore[0] instanceof X509Certificate)) {
                                throw new FailedLoginException("Unable to find X.509 certificate chain in keystore");
                            }
                            LinkedList linkedList = new LinkedList();
                            for (int i2 = 0; i2 < this.fromKeyStore.length; i2++) {
                                linkedList.add(this.fromKeyStore[i2]);
                            }
                            this.certP = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID).generateCertPath(linkedList);
                            try {
                                X509Certificate x509Certificate = (X509Certificate) this.fromKeyStore[0];
                                this.principal = new X500Principal(x509Certificate.getSubjectDN().getName());
                                Key key = this.keyStore.getKey(this.keyStoreAlias, this.privateKeyPassword);
                                if (key == null || !(key instanceof PrivateKey)) {
                                    throw new FailedLoginException("Unable to recover key from keystore");
                                }
                                this.privateCredential = new X500PrivateCredential(x509Certificate, (PrivateKey) key, this.keyStoreAlias);
                                if (this.debug) {
                                    debugPrint("principal=" + ((Object) this.principal) + "\n certificate=" + ((Object) this.privateCredential.getCertificate()) + "\n alias =" + this.privateCredential.getAlias());
                                }
                            } catch (KeyStoreException e3) {
                                LoginException loginException2 = new LoginException("Error using keystore");
                                loginException2.initCause(e3);
                                throw loginException2;
                            } catch (NoSuchAlgorithmException e4) {
                                LoginException loginException3 = new LoginException("Error using keystore");
                                loginException3.initCause(e4);
                                throw loginException3;
                            } catch (UnrecoverableKeyException e5) {
                                FailedLoginException failedLoginException = new FailedLoginException("Unable to recover key from keystore");
                                failedLoginException.initCause(e5);
                                throw failedLoginException;
                            }
                        } catch (KeyStoreException e6) {
                            LoginException loginException4 = new LoginException("Error using keystore");
                            loginException4.initCause(e6);
                            throw loginException4;
                        } catch (CertificateException e7) {
                            LoginException loginException5 = new LoginException("Error: X.509 Certificate type unavailable");
                            loginException5.initCause(e7);
                            throw loginException5;
                        }
                    } catch (MalformedURLException e8) {
                        LoginException loginException6 = new LoginException("Incorrect keyStoreURL option");
                        loginException6.initCause(e8);
                        throw loginException6;
                    } catch (GeneralSecurityException e9) {
                        LoginException loginException7 = new LoginException("Error initializing keystore");
                        loginException7.initCause(e9);
                        throw loginException7;
                    }
                } catch (IOException e10) {
                    LoginException loginException8 = new LoginException("Error initializing keystore");
                    loginException8.initCause(e10);
                    throw loginException8;
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        inputStreamOpenStream.close();
                    } catch (IOException e11) {
                        LoginException loginException9 = new LoginException("Error initializing keystore");
                        loginException9.initCause(e11);
                        throw loginException9;
                    }
                }
                throw th;
            }
        } catch (KeyStoreException e12) {
            LoginException loginException10 = new LoginException("The specified keystore type was not available");
            loginException10.initCause(e12);
            throw loginException10;
        } catch (NoSuchProviderException e13) {
            LoginException loginException11 = new LoginException("The specified keystore provider was not available");
            loginException11.initCause(e13);
            throw loginException11;
        }
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean commit() throws LoginException {
        switch (this.status) {
            case 0:
            default:
                throw new LoginException("The login module is not initialized");
            case 1:
                logoutInternal();
                throw new LoginException("Authentication failed");
            case 2:
                if (commitInternal()) {
                    return true;
                }
                logoutInternal();
                throw new LoginException("Unable to retrieve certificates");
            case 3:
                return true;
        }
    }

    private boolean commitInternal() throws LoginException {
        if (this.subject.isReadOnly()) {
            throw new LoginException("Subject is set readonly");
        }
        this.subject.getPrincipals().add(this.principal);
        this.subject.getPublicCredentials().add(this.certP);
        this.subject.getPrivateCredentials().add(this.privateCredential);
        this.status = 3;
        return true;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean abort() throws LoginException {
        switch (this.status) {
            case 2:
                logoutInternal();
                break;
            case 3:
                logoutInternal();
                break;
        }
        return true;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean logout() throws LoginException {
        if (this.debug) {
            debugPrint("Entering logout " + this.status);
        }
        switch (this.status) {
            case 0:
                throw new LoginException("The login module is not initialized");
            case 1:
            case 2:
            default:
                return false;
            case 3:
                logoutInternal();
                return true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x00f2, code lost:
    
        throw new javax.security.auth.login.LoginException("Unable to remove Principal (X500Principal ) and public credential (certificatepath) from read-only Subject");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void logoutInternal() throws javax.security.auth.login.LoginException {
        /*
            Method dump skipped, instructions count: 342
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.security.auth.module.KeyStoreLoginModule.logoutInternal():void");
    }

    private void debugPrint(String str) {
        if (str == null) {
            System.err.println();
        } else {
            System.err.println("Debug KeyStoreLoginModule: " + str);
        }
    }
}
