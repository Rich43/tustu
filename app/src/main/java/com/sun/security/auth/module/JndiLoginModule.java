package com.sun.security.auth.module;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.security.auth.UnixNumericGroupPrincipal;
import com.sun.security.auth.UnixNumericUserPrincipal;
import com.sun.security.auth.UnixPrincipal;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import jdk.Exported;
import jdk.internal.dynalink.CallSiteDescriptor;
import net.lingala.zip4j.util.InternalZipConstants;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/module/JndiLoginModule.class */
public class JndiLoginModule implements LoginModule {
    private static final ResourceBundle rb = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: com.sun.security.auth.module.JndiLoginModule.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ResourceBundle run2() {
            return ResourceBundle.getBundle("sun.security.util.AuthResources");
        }
    });
    private String userProvider;
    private String groupProvider;
    private String username;
    private char[] password;
    DirContext ctx;
    private UnixPrincipal userPrincipal;
    private UnixNumericUserPrincipal UIDPrincipal;
    private UnixNumericGroupPrincipal GIDPrincipal;
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, Object> sharedState;
    private Map<String, ?> options;
    private static final String CRYPT = "{crypt}";
    private static final String USER_PWD = "userPassword";
    private static final String USER_UID = "uidNumber";
    private static final String USER_GID = "gidNumber";
    private static final String GROUP_ID = "gidNumber";
    private static final String NAME = "javax.security.auth.login.name";
    private static final String PWD = "javax.security.auth.login.password";
    public final String USER_PROVIDER = "user.provider.url";
    public final String GROUP_PROVIDER = "group.provider.url";
    private boolean debug = false;
    private boolean strongDebug = false;
    private boolean useFirstPass = false;
    private boolean tryFirstPass = false;
    private boolean storePass = false;
    private boolean clearPass = false;
    private boolean succeeded = false;
    private boolean commitSucceeded = false;
    private LinkedList<UnixNumericGroupPrincipal> supplementaryGroups = new LinkedList<>();

    @Override // javax.security.auth.spi.LoginModule
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> map, Map<String, ?> map2) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = map;
        this.options = map2;
        this.debug = "true".equalsIgnoreCase((String) map2.get(TransformerFactoryImpl.DEBUG));
        this.strongDebug = "true".equalsIgnoreCase((String) map2.get("strongDebug"));
        this.userProvider = (String) map2.get("user.provider.url");
        this.groupProvider = (String) map2.get("group.provider.url");
        this.tryFirstPass = "true".equalsIgnoreCase((String) map2.get("tryFirstPass"));
        this.useFirstPass = "true".equalsIgnoreCase((String) map2.get("useFirstPass"));
        this.storePass = "true".equalsIgnoreCase((String) map2.get("storePass"));
        this.clearPass = "true".equalsIgnoreCase((String) map2.get("clearPass"));
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean login() throws LoginException {
        if (this.userProvider == null) {
            throw new LoginException("Error: Unable to locate JNDI user provider");
        }
        if (this.groupProvider == null) {
            throw new LoginException("Error: Unable to locate JNDI group provider");
        }
        if (this.debug) {
            System.out.println("\t\t[JndiLoginModule] user provider: " + this.userProvider);
            System.out.println("\t\t[JndiLoginModule] group provider: " + this.groupProvider);
        }
        if (this.tryFirstPass) {
            try {
                attemptAuthentication(true);
                this.succeeded = true;
                if (this.debug) {
                    System.out.println("\t\t[JndiLoginModule] tryFirstPass succeeded");
                    return true;
                }
                return true;
            } catch (LoginException e2) {
                cleanState();
                if (this.debug) {
                    System.out.println("\t\t[JndiLoginModule] tryFirstPass failed with:" + e2.toString());
                }
            }
        } else if (this.useFirstPass) {
            try {
                attemptAuthentication(true);
                this.succeeded = true;
                if (this.debug) {
                    System.out.println("\t\t[JndiLoginModule] useFirstPass succeeded");
                    return true;
                }
                return true;
            } catch (LoginException e3) {
                cleanState();
                if (this.debug) {
                    System.out.println("\t\t[JndiLoginModule] useFirstPass failed");
                }
                throw e3;
            }
        }
        try {
            attemptAuthentication(false);
            this.succeeded = true;
            if (this.debug) {
                System.out.println("\t\t[JndiLoginModule] regular authentication succeeded");
                return true;
            }
            return true;
        } catch (LoginException e4) {
            cleanState();
            if (this.debug) {
                System.out.println("\t\t[JndiLoginModule] regular authentication failed");
            }
            throw e4;
        }
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean commit() throws LoginException {
        if (!this.succeeded) {
            return false;
        }
        if (this.subject.isReadOnly()) {
            cleanState();
            throw new LoginException("Subject is Readonly");
        }
        if (!this.subject.getPrincipals().contains(this.userPrincipal)) {
            this.subject.getPrincipals().add(this.userPrincipal);
        }
        if (!this.subject.getPrincipals().contains(this.UIDPrincipal)) {
            this.subject.getPrincipals().add(this.UIDPrincipal);
        }
        if (!this.subject.getPrincipals().contains(this.GIDPrincipal)) {
            this.subject.getPrincipals().add(this.GIDPrincipal);
        }
        for (int i2 = 0; i2 < this.supplementaryGroups.size(); i2++) {
            if (!this.subject.getPrincipals().contains(this.supplementaryGroups.get(i2))) {
                this.subject.getPrincipals().add(this.supplementaryGroups.get(i2));
            }
        }
        if (this.debug) {
            System.out.println("\t\t[JndiLoginModule]: added UnixPrincipal,");
            System.out.println("\t\t\t\tUnixNumericUserPrincipal,");
            System.out.println("\t\t\t\tUnixNumericGroupPrincipal(s),");
            System.out.println("\t\t\t to Subject");
        }
        cleanState();
        this.commitSucceeded = true;
        return true;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean abort() throws LoginException {
        if (this.debug) {
            System.out.println("\t\t[JndiLoginModule]: aborted authentication failed");
        }
        if (!this.succeeded) {
            return false;
        }
        if (this.succeeded && !this.commitSucceeded) {
            this.succeeded = false;
            cleanState();
            this.userPrincipal = null;
            this.UIDPrincipal = null;
            this.GIDPrincipal = null;
            this.supplementaryGroups = new LinkedList<>();
            return true;
        }
        logout();
        return true;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean logout() throws LoginException {
        if (this.subject.isReadOnly()) {
            cleanState();
            throw new LoginException("Subject is Readonly");
        }
        this.subject.getPrincipals().remove(this.userPrincipal);
        this.subject.getPrincipals().remove(this.UIDPrincipal);
        this.subject.getPrincipals().remove(this.GIDPrincipal);
        for (int i2 = 0; i2 < this.supplementaryGroups.size(); i2++) {
            this.subject.getPrincipals().remove(this.supplementaryGroups.get(i2));
        }
        cleanState();
        this.succeeded = false;
        this.commitSucceeded = false;
        this.userPrincipal = null;
        this.UIDPrincipal = null;
        this.GIDPrincipal = null;
        this.supplementaryGroups = new LinkedList<>();
        if (this.debug) {
            System.out.println("\t\t[JndiLoginModule]: logged out Subject");
            return true;
        }
        return true;
    }

    private void attemptAuthentication(boolean z2) throws LoginException {
        getUsernamePassword(z2);
        try {
            InitialContext initialContext = new InitialContext();
            this.ctx = (DirContext) initialContext.lookup(this.userProvider);
            NamingEnumeration<SearchResult> namingEnumerationSearch = this.ctx.search("", "(uid=" + this.username + ")", new SearchControls());
            if (namingEnumerationSearch.hasMore()) {
                Attributes attributes = namingEnumerationSearch.next().getAttributes();
                if (verifyPassword(new String((byte[]) attributes.get(USER_PWD).get(), InternalZipConstants.CHARSET_UTF8).substring(CRYPT.length()), new String(this.password))) {
                    if (this.debug) {
                        System.out.println("\t\t[JndiLoginModule] attemptAuthentication() succeeded");
                    }
                    if (this.storePass && !this.sharedState.containsKey(NAME) && !this.sharedState.containsKey(PWD)) {
                        this.sharedState.put(NAME, this.username);
                        this.sharedState.put(PWD, this.password);
                    }
                    this.userPrincipal = new UnixPrincipal(this.username);
                    String str = (String) attributes.get(USER_UID).get();
                    this.UIDPrincipal = new UnixNumericUserPrincipal(str);
                    if (this.debug && str != null) {
                        System.out.println("\t\t[JndiLoginModule] user: '" + this.username + "' has UID: " + str);
                    }
                    String str2 = (String) attributes.get("gidNumber").get();
                    this.GIDPrincipal = new UnixNumericGroupPrincipal(str2, true);
                    if (this.debug && str2 != null) {
                        System.out.println("\t\t[JndiLoginModule] user: '" + this.username + "' has GID: " + str2);
                    }
                    this.ctx = (DirContext) initialContext.lookup(this.groupProvider);
                    NamingEnumeration<SearchResult> namingEnumerationSearch2 = this.ctx.search("", new BasicAttributes("memberUid", this.username));
                    while (namingEnumerationSearch2.hasMore()) {
                        String str3 = (String) namingEnumerationSearch2.next().getAttributes().get("gidNumber").get();
                        if (!str2.equals(str3)) {
                            this.supplementaryGroups.add(new UnixNumericGroupPrincipal(str3, false));
                            if (this.debug && str3 != null) {
                                System.out.println("\t\t[JndiLoginModule] user: '" + this.username + "' has Supplementary Group: " + str3);
                            }
                        }
                    }
                    return;
                }
                if (this.debug) {
                    System.out.println("\t\t[JndiLoginModule] attemptAuthentication() failed");
                }
                throw new FailedLoginException("Login incorrect");
            }
            if (this.debug) {
                System.out.println("\t\t[JndiLoginModule]: User not found");
            }
            throw new FailedLoginException("User not found");
        } catch (UnsupportedEncodingException e2) {
            if (this.debug) {
                System.out.println("\t\t[JndiLoginModule]:  password incorrectly encoded");
                e2.printStackTrace();
            }
            throw new LoginException("Login failure due to incorrect password encoding in the password database");
        } catch (NamingException e3) {
            if (this.debug) {
                System.out.println("\t\t[JndiLoginModule]:  User not found");
                e3.printStackTrace();
            }
            throw new FailedLoginException("User not found");
        }
    }

    private void getUsernamePassword(boolean z2) throws LoginException {
        if (z2) {
            this.username = (String) this.sharedState.get(NAME);
            this.password = (char[]) this.sharedState.get(PWD);
            return;
        }
        if (this.callbackHandler == null) {
            throw new LoginException("Error: no CallbackHandler available to garner authentication information from the user");
        }
        String strSubstring = this.userProvider.substring(0, this.userProvider.indexOf(CallSiteDescriptor.TOKEN_DELIMITER));
        Callback[] callbackArr = {new NameCallback(strSubstring + " " + rb.getString("username.")), new PasswordCallback(strSubstring + " " + rb.getString("password."), false)};
        try {
            this.callbackHandler.handle(callbackArr);
            this.username = ((NameCallback) callbackArr[0]).getName();
            char[] password = ((PasswordCallback) callbackArr[1]).getPassword();
            this.password = new char[password.length];
            System.arraycopy(password, 0, this.password, 0, password.length);
            ((PasswordCallback) callbackArr[1]).clearPassword();
            if (this.strongDebug) {
                System.out.println("\t\t[JndiLoginModule] user entered username: " + this.username);
                System.out.print("\t\t[JndiLoginModule] user entered password: ");
                for (int i2 = 0; i2 < this.password.length; i2++) {
                    System.out.print(this.password[i2]);
                }
                System.out.println();
            }
        } catch (IOException e2) {
            throw new LoginException(e2.toString());
        } catch (UnsupportedCallbackException e3) {
            throw new LoginException("Error: " + e3.getCallback().toString() + " not available to garner authentication information from the user");
        }
    }

    private boolean verifyPassword(String str, String str2) {
        if (str == null) {
            return false;
        }
        Crypt crypt = new Crypt();
        try {
            byte[] bytes = str.getBytes(InternalZipConstants.CHARSET_UTF8);
            byte[] bArrCrypt = crypt.crypt(str2.getBytes(InternalZipConstants.CHARSET_UTF8), bytes);
            if (bArrCrypt.length != bytes.length) {
                return false;
            }
            for (int i2 = 0; i2 < bArrCrypt.length; i2++) {
                if (bytes[i2] != bArrCrypt[i2]) {
                    return false;
                }
            }
            return true;
        } catch (UnsupportedEncodingException e2) {
            return false;
        }
    }

    private void cleanState() {
        this.username = null;
        if (this.password != null) {
            for (int i2 = 0; i2 < this.password.length; i2++) {
                this.password[i2] = ' ';
            }
            this.password = null;
        }
        this.ctx = null;
        if (this.clearPass) {
            this.sharedState.remove(NAME);
            this.sharedState.remove(PWD);
        }
    }
}
