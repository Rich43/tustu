package com.sun.security.auth.module;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.RefreshFailedException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.kerberos.KeyTab;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import jdk.Exported;
import sun.misc.HexDumpEncoder;
import sun.security.jgss.krb5.Krb5Util;
import sun.security.krb5.Config;
import sun.security.krb5.Credentials;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KerberosSecrets;
import sun.security.krb5.KrbAsReqBuilder;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/module/Krb5LoginModule.class */
public class Krb5LoginModule implements LoginModule {
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, Object> sharedState;
    private Map<String, ?> options;
    private String username;
    private static final String NAME = "javax.security.auth.login.name";
    private static final String PWD = "javax.security.auth.login.password";
    private static final ResourceBundle rb = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: com.sun.security.auth.module.Krb5LoginModule.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ResourceBundle run2() {
            return ResourceBundle.getBundle("sun.security.util.AuthResources");
        }
    });
    private boolean debug = false;
    private boolean storeKey = false;
    private boolean doNotPrompt = false;
    private boolean useTicketCache = false;
    private boolean useKeyTab = false;
    private String ticketCacheName = null;
    private String keyTabName = null;
    private String princName = null;
    private boolean useFirstPass = false;
    private boolean tryFirstPass = false;
    private boolean storePass = false;
    private boolean clearPass = false;
    private boolean refreshKrb5Config = false;
    private boolean renewTGT = false;
    private boolean isInitiator = true;
    private boolean succeeded = false;
    private boolean commitSucceeded = false;
    private EncryptionKey[] encKeys = null;
    KeyTab ktab = null;
    private Credentials cred = null;
    private PrincipalName principal = null;
    private KerberosPrincipal kerbClientPrinc = null;
    private KerberosTicket kerbTicket = null;
    private KerberosKey[] kerbKeys = null;
    private StringBuffer krb5PrincName = null;
    private boolean unboundServer = false;
    private char[] password = null;

    @Override // javax.security.auth.spi.LoginModule
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> map, Map<String, ?> map2) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = map;
        this.options = map2;
        this.debug = "true".equalsIgnoreCase((String) map2.get(TransformerFactoryImpl.DEBUG));
        this.storeKey = "true".equalsIgnoreCase((String) map2.get("storeKey"));
        this.doNotPrompt = "true".equalsIgnoreCase((String) map2.get("doNotPrompt"));
        this.useTicketCache = "true".equalsIgnoreCase((String) map2.get("useTicketCache"));
        this.useKeyTab = "true".equalsIgnoreCase((String) map2.get("useKeyTab"));
        this.ticketCacheName = (String) map2.get("ticketCache");
        this.keyTabName = (String) map2.get("keyTab");
        if (this.keyTabName != null) {
            this.keyTabName = sun.security.krb5.internal.ktab.KeyTab.normalize(this.keyTabName);
        }
        this.princName = (String) map2.get("principal");
        this.refreshKrb5Config = "true".equalsIgnoreCase((String) map2.get("refreshKrb5Config"));
        this.renewTGT = "true".equalsIgnoreCase((String) map2.get("renewTGT"));
        String str = (String) map2.get("isInitiator");
        if (str != null) {
            this.isInitiator = "true".equalsIgnoreCase(str);
        }
        this.tryFirstPass = "true".equalsIgnoreCase((String) map2.get("tryFirstPass"));
        this.useFirstPass = "true".equalsIgnoreCase((String) map2.get("useFirstPass"));
        this.storePass = "true".equalsIgnoreCase((String) map2.get("storePass"));
        this.clearPass = "true".equalsIgnoreCase((String) map2.get("clearPass"));
        if (this.debug) {
            System.out.print("Debug is  " + this.debug + " storeKey " + this.storeKey + " useTicketCache " + this.useTicketCache + " useKeyTab " + this.useKeyTab + " doNotPrompt " + this.doNotPrompt + " ticketCache is " + this.ticketCacheName + " isInitiator " + this.isInitiator + " KeyTab is " + this.keyTabName + " refreshKrb5Config is " + this.refreshKrb5Config + " principal is " + this.princName + " tryFirstPass is " + this.tryFirstPass + " useFirstPass is " + this.useFirstPass + " storePass is " + this.storePass + " clearPass is " + this.clearPass + "\n");
        }
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean login() throws LoginException {
        if (this.refreshKrb5Config) {
            try {
                if (this.debug) {
                    System.out.println("Refreshing Kerberos configuration");
                }
                Config.refresh();
            } catch (KrbException e2) {
                LoginException loginException = new LoginException(e2.getMessage());
                loginException.initCause(e2);
                throw loginException;
            }
        }
        String property = System.getProperty("sun.security.krb5.principal");
        if (property != null) {
            this.krb5PrincName = new StringBuffer(property);
        } else if (this.princName != null) {
            this.krb5PrincName = new StringBuffer(this.princName);
        }
        validateConfiguration();
        if (this.krb5PrincName != null && this.krb5PrincName.toString().equals("*")) {
            this.unboundServer = true;
        }
        if (this.tryFirstPass) {
            try {
                attemptAuthentication(true);
                if (this.debug) {
                    System.out.println("\t\t[Krb5LoginModule] authentication succeeded");
                }
                this.succeeded = true;
                cleanState();
                return true;
            } catch (LoginException e3) {
                cleanState();
                if (this.debug) {
                    System.out.println("\t\t[Krb5LoginModule] tryFirstPass failed with:" + e3.getMessage());
                }
            }
        } else if (this.useFirstPass) {
            try {
                attemptAuthentication(true);
                this.succeeded = true;
                cleanState();
                return true;
            } catch (LoginException e4) {
                if (this.debug) {
                    System.out.println("\t\t[Krb5LoginModule] authentication failed \n" + e4.getMessage());
                }
                this.succeeded = false;
                cleanState();
                throw e4;
            }
        }
        try {
            attemptAuthentication(false);
            this.succeeded = true;
            cleanState();
            return true;
        } catch (LoginException e5) {
            if (this.debug) {
                System.out.println("\t\t[Krb5LoginModule] authentication failed \n" + e5.getMessage());
            }
            this.succeeded = false;
            cleanState();
            throw e5;
        }
    }

    private void attemptAuthentication(boolean z2) throws LoginException {
        KrbAsReqBuilder krbAsReqBuilder;
        KeyTab unboundInstance;
        KeyTab keyTab;
        PrincipalName client;
        Credentials credentialsRenewCredentials;
        if (this.krb5PrincName != null) {
            try {
                this.principal = new PrincipalName(this.krb5PrincName.toString(), 1);
            } catch (KrbException e2) {
                LoginException loginException = new LoginException(e2.getMessage());
                loginException.initCause(e2);
                throw loginException;
            }
        }
        try {
            if (this.useTicketCache) {
                if (this.debug) {
                    System.out.println("Acquire TGT from Cache");
                }
                this.cred = Credentials.acquireTGTFromCache(this.principal, this.ticketCacheName);
                if (this.cred != null) {
                    if (this.renewTGT && isOld(this.cred) && (credentialsRenewCredentials = renewCredentials(this.cred)) != null) {
                        credentialsRenewCredentials.setProxy(this.cred.getProxy());
                        this.cred = credentialsRenewCredentials;
                    }
                    if (!isCurrent(this.cred)) {
                        this.cred = null;
                        if (this.debug) {
                            System.out.println("Credentials are no longer valid");
                        }
                    }
                }
                if (this.cred != null && this.principal == null) {
                    if (this.cred.getProxy() != null) {
                        client = this.cred.getProxy().getClient();
                    } else {
                        client = this.cred.getClient();
                    }
                    this.principal = client;
                }
                if (this.debug) {
                    System.out.println("Principal is " + ((Object) this.principal));
                    if (this.cred == null) {
                        System.out.println("null credentials from Ticket Cache");
                    }
                }
            }
            if (this.cred == null) {
                if (this.principal == null) {
                    promptForName(z2);
                    this.principal = new PrincipalName(this.krb5PrincName.toString(), 1);
                }
                if (this.useKeyTab) {
                    if (!this.unboundServer) {
                        KerberosPrincipal kerberosPrincipal = new KerberosPrincipal(this.principal.getName());
                        if (this.keyTabName == null) {
                            keyTab = KeyTab.getInstance(kerberosPrincipal);
                        } else {
                            keyTab = KeyTab.getInstance(kerberosPrincipal, new File(this.keyTabName));
                        }
                        this.ktab = keyTab;
                    } else {
                        if (this.keyTabName == null) {
                            unboundInstance = KeyTab.getUnboundInstance();
                        } else {
                            unboundInstance = KeyTab.getUnboundInstance(new File(this.keyTabName));
                        }
                        this.ktab = unboundInstance;
                    }
                    if (this.isInitiator && Krb5Util.keysFromJavaxKeyTab(this.ktab, this.principal).length == 0) {
                        this.ktab = null;
                        if (this.debug) {
                            System.out.println("Key for the principal " + ((Object) this.principal) + " not available in " + (this.keyTabName == null ? "default key tab" : this.keyTabName));
                        }
                    }
                }
                if (this.ktab == null) {
                    promptForPass(z2);
                    krbAsReqBuilder = new KrbAsReqBuilder(this.principal, this.password);
                    if (this.isInitiator) {
                        this.cred = krbAsReqBuilder.action().getCreds();
                    }
                    if (this.storeKey) {
                        this.encKeys = krbAsReqBuilder.getKeys(this.isInitiator);
                    }
                } else {
                    krbAsReqBuilder = new KrbAsReqBuilder(this.principal, this.ktab);
                    if (this.isInitiator) {
                        this.cred = krbAsReqBuilder.action().getCreds();
                    }
                }
                krbAsReqBuilder.destroy();
                if (this.debug) {
                    System.out.println("principal is " + ((Object) this.principal));
                    HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
                    if (this.ktab != null) {
                        System.out.println("Will use keytab");
                    } else if (this.storeKey) {
                        for (int i2 = 0; i2 < this.encKeys.length; i2++) {
                            System.out.println("EncryptionKey: keyType=" + this.encKeys[i2].getEType() + " keyBytes (hex dump)=" + hexDumpEncoder.encodeBuffer(this.encKeys[i2].getBytes()));
                        }
                    }
                }
                if (this.isInitiator && this.cred == null) {
                    throw new LoginException("TGT Can not be obtained from the KDC ");
                }
            }
        } catch (IOException e3) {
            LoginException loginException2 = new LoginException(e3.getMessage());
            loginException2.initCause(e3);
            throw loginException2;
        } catch (KrbException e4) {
            LoginException loginException3 = new LoginException(e4.getMessage());
            loginException3.initCause(e4);
            throw loginException3;
        }
    }

    private void promptForName(boolean z2) throws LoginException {
        this.krb5PrincName = new StringBuffer("");
        if (z2) {
            this.username = (String) this.sharedState.get(NAME);
            if (this.debug) {
                System.out.println("username from shared state is " + this.username + "\n");
            }
            if (this.username == null) {
                System.out.println("username from shared state is null\n");
                throw new LoginException("Username can not be obtained from sharedstate ");
            }
            if (this.debug) {
                System.out.println("username from shared state is " + this.username + "\n");
            }
            if (this.username != null && this.username.length() > 0) {
                this.krb5PrincName.insert(0, this.username);
                return;
            }
        }
        if (this.doNotPrompt) {
            throw new LoginException("Unable to obtain Principal Name for authentication ");
        }
        if (this.callbackHandler == null) {
            throw new LoginException("No CallbackHandler available to garner authentication information from the user");
        }
        try {
            String property = System.getProperty("user.name");
            Callback[] callbackArr = {new NameCallback(new MessageFormat(rb.getString("Kerberos.username.defUsername.")).format(new Object[]{property}))};
            this.callbackHandler.handle(callbackArr);
            this.username = ((NameCallback) callbackArr[0]).getName();
            if (this.username == null || this.username.length() == 0) {
                this.username = property;
            }
            this.krb5PrincName.insert(0, this.username);
        } catch (IOException e2) {
            throw new LoginException(e2.getMessage());
        } catch (UnsupportedCallbackException e3) {
            throw new LoginException(e3.getMessage() + " not available to garner  authentication information  from the user");
        }
    }

    private void promptForPass(boolean z2) throws LoginException {
        if (z2) {
            this.password = (char[]) this.sharedState.get(PWD);
            if (this.password == null) {
                if (this.debug) {
                    System.out.println("Password from shared state is null");
                }
                throw new LoginException("Password can not be obtained from sharedstate ");
            }
            if (this.debug) {
                System.out.println("password is " + new String(this.password));
                return;
            }
            return;
        }
        if (this.doNotPrompt) {
            throw new LoginException("Unable to obtain password from user\n");
        }
        if (this.callbackHandler == null) {
            throw new LoginException("No CallbackHandler available to garner authentication information from the user");
        }
        try {
            Callback[] callbackArr = {new PasswordCallback(new MessageFormat(rb.getString("Kerberos.password.for.username.")).format(new Object[]{this.krb5PrincName.toString()}), false)};
            this.callbackHandler.handle(callbackArr);
            char[] password = ((PasswordCallback) callbackArr[0]).getPassword();
            if (password == null) {
                throw new LoginException("No password provided");
            }
            this.password = new char[password.length];
            System.arraycopy(password, 0, this.password, 0, password.length);
            ((PasswordCallback) callbackArr[0]).clearPassword();
            for (int i2 = 0; i2 < password.length; i2++) {
                password[i2] = ' ';
            }
            if (this.debug) {
                System.out.println("\t\t[Krb5LoginModule] user entered username: " + ((Object) this.krb5PrincName));
                System.out.println();
            }
        } catch (IOException e2) {
            throw new LoginException(e2.getMessage());
        } catch (UnsupportedCallbackException e3) {
            throw new LoginException(e3.getMessage() + " not available to garner  authentication information from the user");
        }
    }

    private void validateConfiguration() throws LoginException {
        if (this.doNotPrompt && !this.useTicketCache && !this.useKeyTab && !this.tryFirstPass && !this.useFirstPass) {
            throw new LoginException("Configuration Error - either doNotPrompt should be  false or at least one of useTicketCache,  useKeyTab, tryFirstPass and useFirstPass should be true");
        }
        if (this.ticketCacheName != null && !this.useTicketCache) {
            throw new LoginException("Configuration Error  - useTicketCache should be set to true to use the ticket cache" + this.ticketCacheName);
        }
        if ((this.keyTabName != null) & (!this.useKeyTab)) {
            throw new LoginException("Configuration Error - useKeyTab should be set to true to use the keytab" + this.keyTabName);
        }
        if (this.storeKey && this.doNotPrompt && !this.useKeyTab && !this.tryFirstPass && !this.useFirstPass) {
            throw new LoginException("Configuration Error - either doNotPrompt should be set to  false or at least one of tryFirstPass, useFirstPass or useKeyTab must be set to true for storeKey option");
        }
        if (this.renewTGT && !this.useTicketCache) {
            throw new LoginException("Configuration Error - either useTicketCache should be  true or renewTGT should be false");
        }
        if (this.krb5PrincName != null && this.krb5PrincName.toString().equals("*") && this.isInitiator) {
            throw new LoginException("Configuration Error - principal cannot be * when isInitiator is true");
        }
    }

    private static boolean isCurrent(Credentials credentials) {
        Date endTime = credentials.getEndTime();
        return endTime == null || System.currentTimeMillis() <= endTime.getTime();
    }

    private static boolean isOld(Credentials credentials) {
        Date endTime = credentials.getEndTime();
        if (endTime != null) {
            Date authTime = credentials.getAuthTime();
            long jCurrentTimeMillis = System.currentTimeMillis();
            return authTime != null ? jCurrentTimeMillis - authTime.getTime() > endTime.getTime() - jCurrentTimeMillis : jCurrentTimeMillis <= endTime.getTime() - 7200000;
        }
        return false;
    }

    private Credentials renewCredentials(Credentials credentials) throws RefreshFailedException {
        Credentials credentialsRenew;
        try {
        } catch (Exception e2) {
            credentialsRenew = null;
            if (this.debug) {
                System.out.println("Ticket could not be renewed : " + e2.getMessage());
            }
        }
        if (!credentials.isRenewable()) {
            throw new RefreshFailedException("This ticket is not renewable");
        }
        if (credentials.getRenewTill() == null) {
            return credentials;
        }
        if (System.currentTimeMillis() > this.cred.getRenewTill().getTime()) {
            throw new RefreshFailedException("This ticket is past its last renewal time.");
        }
        credentialsRenew = credentials.renew();
        if (this.debug) {
            System.out.println("Renewed Kerberos Ticket");
        }
        return credentialsRenew;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean commit() throws LoginException {
        if (!this.succeeded) {
            return false;
        }
        if (this.isInitiator && this.cred == null) {
            this.succeeded = false;
            throw new LoginException("Null Client Credential");
        }
        if (this.subject.isReadOnly()) {
            cleanKerberosCred();
            throw new LoginException("Subject is Readonly");
        }
        Set<Object> privateCredentials = this.subject.getPrivateCredentials();
        Set<Principal> principals = this.subject.getPrincipals();
        this.kerbClientPrinc = new KerberosPrincipal(this.principal.getName());
        if (this.isInitiator) {
            this.kerbTicket = Krb5Util.credsToTicket(this.cred);
            if (this.cred.getProxy() != null) {
                KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketSetProxy(this.kerbTicket, Krb5Util.credsToTicket(this.cred.getProxy()));
            }
        }
        if (this.storeKey && this.encKeys != null) {
            if (this.encKeys.length == 0) {
                this.succeeded = false;
                throw new LoginException("Null Server Key ");
            }
            this.kerbKeys = new KerberosKey[this.encKeys.length];
            for (int i2 = 0; i2 < this.encKeys.length; i2++) {
                Integer keyVersionNumber = this.encKeys[i2].getKeyVersionNumber();
                this.kerbKeys[i2] = new KerberosKey(this.kerbClientPrinc, this.encKeys[i2].getBytes(), this.encKeys[i2].getEType(), keyVersionNumber == null ? 0 : keyVersionNumber.intValue());
            }
        }
        if (!this.unboundServer && !principals.contains(this.kerbClientPrinc)) {
            principals.add(this.kerbClientPrinc);
        }
        if (this.kerbTicket != null && !privateCredentials.contains(this.kerbTicket)) {
            privateCredentials.add(this.kerbTicket);
        }
        if (this.storeKey) {
            if (this.encKeys == null) {
                if (this.ktab != null) {
                    if (!privateCredentials.contains(this.ktab)) {
                        privateCredentials.add(this.ktab);
                    }
                } else {
                    this.succeeded = false;
                    throw new LoginException("No key to store");
                }
            } else {
                for (int i3 = 0; i3 < this.kerbKeys.length; i3++) {
                    if (!privateCredentials.contains(this.kerbKeys[i3])) {
                        privateCredentials.add(this.kerbKeys[i3]);
                    }
                    this.encKeys[i3].destroy();
                    this.encKeys[i3] = null;
                    if (this.debug) {
                        System.out.println("Added server's key" + ((Object) this.kerbKeys[i3]));
                        System.out.println("\t\t[Krb5LoginModule] added Krb5Principal  " + this.kerbClientPrinc.toString() + " to Subject");
                    }
                }
            }
        }
        this.commitSucceeded = true;
        if (this.debug) {
            System.out.println("Commit Succeeded \n");
            return true;
        }
        return true;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean abort() throws LoginException {
        if (!this.succeeded) {
            return false;
        }
        if (this.succeeded && !this.commitSucceeded) {
            this.succeeded = false;
            cleanKerberosCred();
            return true;
        }
        logout();
        return true;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean logout() throws LoginException {
        if (this.debug) {
            System.out.println("\t\t[Krb5LoginModule]: Entering logout");
        }
        if (this.subject.isReadOnly()) {
            cleanKerberosCred();
            throw new LoginException("Subject is Readonly");
        }
        this.subject.getPrincipals().remove(this.kerbClientPrinc);
        Iterator<Object> it = this.subject.getPrivateCredentials().iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if ((next instanceof KerberosTicket) || (next instanceof KerberosKey) || (next instanceof KeyTab)) {
                it.remove();
            }
        }
        cleanKerberosCred();
        this.succeeded = false;
        this.commitSucceeded = false;
        if (this.debug) {
            System.out.println("\t\t[Krb5LoginModule]: logged out Subject");
            return true;
        }
        return true;
    }

    private void cleanKerberosCred() throws LoginException {
        try {
            if (this.kerbTicket != null) {
                this.kerbTicket.destroy();
            }
            if (this.kerbKeys != null) {
                for (int i2 = 0; i2 < this.kerbKeys.length; i2++) {
                    this.kerbKeys[i2].destroy();
                }
            }
            this.kerbTicket = null;
            this.kerbKeys = null;
            this.kerbClientPrinc = null;
        } catch (DestroyFailedException e2) {
            throw new LoginException("Destroy Failed on Kerberos Private Credentials");
        }
    }

    private void cleanState() {
        if (this.succeeded) {
            if (this.storePass && !this.sharedState.containsKey(NAME) && !this.sharedState.containsKey(PWD)) {
                this.sharedState.put(NAME, this.username);
                this.sharedState.put(PWD, this.password);
            }
        } else {
            this.encKeys = null;
            this.ktab = null;
            this.principal = null;
        }
        this.username = null;
        this.password = null;
        if (this.krb5PrincName != null && this.krb5PrincName.length() != 0) {
            this.krb5PrincName.delete(0, this.krb5PrincName.length());
        }
        this.krb5PrincName = null;
        if (this.clearPass) {
            this.sharedState.remove(NAME);
            this.sharedState.remove(PWD);
        }
    }
}
