package com.sun.security.auth.module;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.security.auth.LdapPrincipal;
import com.sun.security.auth.UserPrincipal;
import java.io.IOException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
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
import org.apache.commons.math3.geometry.VectorFormat;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/module/LdapLoginModule.class */
public class LdapLoginModule implements LoginModule {
    private static final String USERNAME_KEY = "javax.security.auth.login.name";
    private static final String PASSWORD_KEY = "javax.security.auth.login.password";
    private static final String USER_PROVIDER = "userProvider";
    private static final String USER_FILTER = "userFilter";
    private static final String AUTHC_IDENTITY = "authIdentity";
    private static final String AUTHZ_IDENTITY = "authzIdentity";
    private static final String USERNAME_TOKEN = "{USERNAME}";
    private String userProvider;
    private String userFilter;
    private String authcIdentity;
    private String authzIdentity;
    private String username;
    private char[] password;
    private LdapPrincipal ldapPrincipal;
    private UserPrincipal userPrincipal;
    private UserPrincipal authzPrincipal;
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, Object> sharedState;
    private Map<String, ?> options;
    private LdapContext ctx;
    private Hashtable<String, Object> ldapEnvironment;
    private static final ResourceBundle rb = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: com.sun.security.auth.module.LdapLoginModule.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ResourceBundle run2() {
            return ResourceBundle.getBundle("sun.security.util.AuthResources");
        }
    });
    private static final Pattern USERNAME_PATTERN = Pattern.compile("\\{USERNAME\\}");
    private String authzIdentityAttr = null;
    private boolean useSSL = true;
    private boolean authFirst = false;
    private boolean authOnly = false;
    private boolean useFirstPass = false;
    private boolean tryFirstPass = false;
    private boolean storePass = false;
    private boolean clearPass = false;
    private boolean debug = false;
    private boolean succeeded = false;
    private boolean commitSucceeded = false;
    private Matcher identityMatcher = null;
    private Matcher filterMatcher = null;
    private SearchControls constraints = null;

    @Override // javax.security.auth.spi.LoginModule
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> map, Map<String, ?> map2) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = map;
        this.options = map2;
        this.ldapEnvironment = new Hashtable<>(9);
        this.ldapEnvironment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        for (String str : map2.keySet()) {
            if (str.indexOf(".") > -1) {
                this.ldapEnvironment.put(str, map2.get(str));
            }
        }
        this.userProvider = (String) map2.get(USER_PROVIDER);
        if (this.userProvider != null) {
            this.ldapEnvironment.put(Context.PROVIDER_URL, this.userProvider);
        }
        this.authcIdentity = (String) map2.get(AUTHC_IDENTITY);
        if (this.authcIdentity != null && this.authcIdentity.indexOf(USERNAME_TOKEN) != -1) {
            this.identityMatcher = USERNAME_PATTERN.matcher(this.authcIdentity);
        }
        this.userFilter = (String) map2.get(USER_FILTER);
        if (this.userFilter != null) {
            if (this.userFilter.indexOf(USERNAME_TOKEN) != -1) {
                this.filterMatcher = USERNAME_PATTERN.matcher(this.userFilter);
            }
            this.constraints = new SearchControls();
            this.constraints.setSearchScope(2);
            this.constraints.setReturningAttributes(new String[0]);
        }
        this.authzIdentity = (String) map2.get(AUTHZ_IDENTITY);
        if (this.authzIdentity != null && this.authzIdentity.startsWith(VectorFormat.DEFAULT_PREFIX) && this.authzIdentity.endsWith("}")) {
            if (this.constraints != null) {
                this.authzIdentityAttr = this.authzIdentity.substring(1, this.authzIdentity.length() - 1);
                this.constraints.setReturningAttributes(new String[]{this.authzIdentityAttr});
            }
            this.authzIdentity = null;
        }
        if (this.authcIdentity != null) {
            if (this.userFilter != null) {
                this.authFirst = true;
            } else {
                this.authOnly = true;
            }
        }
        if ("false".equalsIgnoreCase((String) map2.get("useSSL"))) {
            this.useSSL = false;
            this.ldapEnvironment.remove(Context.SECURITY_PROTOCOL);
        } else {
            this.ldapEnvironment.put(Context.SECURITY_PROTOCOL, "ssl");
        }
        this.tryFirstPass = "true".equalsIgnoreCase((String) map2.get("tryFirstPass"));
        this.useFirstPass = "true".equalsIgnoreCase((String) map2.get("useFirstPass"));
        this.storePass = "true".equalsIgnoreCase((String) map2.get("storePass"));
        this.clearPass = "true".equalsIgnoreCase((String) map2.get("clearPass"));
        this.debug = "true".equalsIgnoreCase((String) map2.get(TransformerFactoryImpl.DEBUG));
        if (this.debug) {
            if (this.authFirst) {
                System.out.println("\t\t[LdapLoginModule] authentication-first mode; " + (this.useSSL ? "SSL enabled" : "SSL disabled"));
            } else if (this.authOnly) {
                System.out.println("\t\t[LdapLoginModule] authentication-only mode; " + (this.useSSL ? "SSL enabled" : "SSL disabled"));
            } else {
                System.out.println("\t\t[LdapLoginModule] search-first mode; " + (this.useSSL ? "SSL enabled" : "SSL disabled"));
            }
        }
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean login() throws LoginException {
        if (this.userProvider == null) {
            throw new LoginException("Unable to locate the LDAP directory service");
        }
        if (this.debug) {
            System.out.println("\t\t[LdapLoginModule] user provider: " + this.userProvider);
        }
        if (this.tryFirstPass) {
            try {
                attemptAuthentication(true);
                this.succeeded = true;
                if (this.debug) {
                    System.out.println("\t\t[LdapLoginModule] tryFirstPass succeeded");
                    return true;
                }
                return true;
            } catch (LoginException e2) {
                cleanState();
                if (this.debug) {
                    System.out.println("\t\t[LdapLoginModule] tryFirstPass failed: " + e2.toString());
                }
            }
        } else if (this.useFirstPass) {
            try {
                attemptAuthentication(true);
                this.succeeded = true;
                if (this.debug) {
                    System.out.println("\t\t[LdapLoginModule] useFirstPass succeeded");
                    return true;
                }
                return true;
            } catch (LoginException e3) {
                cleanState();
                if (this.debug) {
                    System.out.println("\t\t[LdapLoginModule] useFirstPass failed");
                }
                throw e3;
            }
        }
        try {
            attemptAuthentication(false);
            this.succeeded = true;
            if (this.debug) {
                System.out.println("\t\t[LdapLoginModule] authentication succeeded");
                return true;
            }
            return true;
        } catch (LoginException e4) {
            cleanState();
            if (this.debug) {
                System.out.println("\t\t[LdapLoginModule] authentication failed");
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
            throw new LoginException("Subject is read-only");
        }
        Set<Principal> principals = this.subject.getPrincipals();
        if (!principals.contains(this.ldapPrincipal)) {
            principals.add(this.ldapPrincipal);
        }
        if (this.debug) {
            System.out.println("\t\t[LdapLoginModule] added LdapPrincipal \"" + ((Object) this.ldapPrincipal) + "\" to Subject");
        }
        if (!principals.contains(this.userPrincipal)) {
            principals.add(this.userPrincipal);
        }
        if (this.debug) {
            System.out.println("\t\t[LdapLoginModule] added UserPrincipal \"" + ((Object) this.userPrincipal) + "\" to Subject");
        }
        if (this.authzPrincipal != null && !principals.contains(this.authzPrincipal)) {
            principals.add(this.authzPrincipal);
            if (this.debug) {
                System.out.println("\t\t[LdapLoginModule] added UserPrincipal \"" + ((Object) this.authzPrincipal) + "\" to Subject");
            }
        }
        cleanState();
        this.commitSucceeded = true;
        return true;
    }

    @Override // javax.security.auth.spi.LoginModule
    public boolean abort() throws LoginException {
        if (this.debug) {
            System.out.println("\t\t[LdapLoginModule] aborted authentication");
        }
        if (!this.succeeded) {
            return false;
        }
        if (this.succeeded && !this.commitSucceeded) {
            this.succeeded = false;
            cleanState();
            this.ldapPrincipal = null;
            this.userPrincipal = null;
            this.authzPrincipal = null;
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
        Set<Principal> principals = this.subject.getPrincipals();
        principals.remove(this.ldapPrincipal);
        principals.remove(this.userPrincipal);
        if (this.authzIdentity != null) {
            principals.remove(this.authzPrincipal);
        }
        cleanState();
        this.succeeded = false;
        this.commitSucceeded = false;
        this.ldapPrincipal = null;
        this.userPrincipal = null;
        this.authzPrincipal = null;
        if (this.debug) {
            System.out.println("\t\t[LdapLoginModule] logged out Subject");
            return true;
        }
        return true;
    }

    private void attemptAuthentication(boolean z2) throws LoginException {
        String strFindUserDN;
        getUsernamePassword(z2);
        if (this.password == null || this.password.length == 0) {
            throw new FailedLoginException("No password was supplied");
        }
        if (this.authFirst || this.authOnly) {
            String strReplaceUsernameToken = replaceUsernameToken(this.identityMatcher, this.authcIdentity, this.username);
            this.ldapEnvironment.put(Context.SECURITY_CREDENTIALS, this.password);
            this.ldapEnvironment.put(Context.SECURITY_PRINCIPAL, strReplaceUsernameToken);
            if (this.debug) {
                System.out.println("\t\t[LdapLoginModule] attempting to authenticate user: " + this.username);
            }
            try {
                this.ctx = new InitialLdapContext(this.ldapEnvironment, null);
                if (this.userFilter != null) {
                    strFindUserDN = findUserDN(this.ctx);
                } else {
                    strFindUserDN = strReplaceUsernameToken;
                }
            } catch (NamingException e2) {
                throw ((LoginException) new FailedLoginException("Cannot bind to LDAP server").initCause(e2));
            }
        } else {
            try {
                this.ctx = new InitialLdapContext(this.ldapEnvironment, null);
                strFindUserDN = findUserDN(this.ctx);
                try {
                    this.ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
                    this.ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, strFindUserDN);
                    this.ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, this.password);
                    if (this.debug) {
                        System.out.println("\t\t[LdapLoginModule] attempting to authenticate user: " + this.username);
                    }
                    this.ctx.reconnect(null);
                } catch (NamingException e3) {
                    throw ((LoginException) new FailedLoginException("Cannot bind to LDAP server").initCause(e3));
                }
            } catch (NamingException e4) {
                throw ((LoginException) new FailedLoginException("Cannot connect to LDAP server").initCause(e4));
            }
        }
        if (this.storePass && !this.sharedState.containsKey(USERNAME_KEY) && !this.sharedState.containsKey(PASSWORD_KEY)) {
            this.sharedState.put(USERNAME_KEY, this.username);
            this.sharedState.put(PASSWORD_KEY, this.password);
        }
        this.userPrincipal = new UserPrincipal(this.username);
        if (this.authzIdentity != null) {
            this.authzPrincipal = new UserPrincipal(this.authzIdentity);
        }
        try {
            this.ldapPrincipal = new LdapPrincipal(strFindUserDN);
        } catch (InvalidNameException e5) {
            if (this.debug) {
                System.out.println("\t\t[LdapLoginModule] cannot create LdapPrincipal: bad DN");
            }
            throw ((LoginException) new FailedLoginException("Cannot create LdapPrincipal").initCause(e5));
        }
    }

    private String findUserDN(LdapContext ldapContext) throws LoginException {
        Attribute attribute;
        String nameInNamespace = "";
        if (this.userFilter != null) {
            if (this.debug) {
                System.out.println("\t\t[LdapLoginModule] searching for entry belonging to user: " + this.username);
            }
            try {
                NamingEnumeration<SearchResult> namingEnumerationSearch = ldapContext.search("", replaceUsernameToken(this.filterMatcher, this.userFilter, escapeUsernameChars()), this.constraints);
                if (namingEnumerationSearch.hasMore()) {
                    SearchResult next = namingEnumerationSearch.next();
                    nameInNamespace = next.getNameInNamespace();
                    if (this.debug) {
                        System.out.println("\t\t[LdapLoginModule] found entry: " + nameInNamespace);
                    }
                    if (this.authzIdentityAttr != null && (attribute = next.getAttributes().get(this.authzIdentityAttr)) != null) {
                        Object obj = attribute.get();
                        if (obj instanceof String) {
                            this.authzIdentity = (String) obj;
                        }
                    }
                    namingEnumerationSearch.close();
                } else if (this.debug) {
                    System.out.println("\t\t[LdapLoginModule] user's entry not found");
                }
            } catch (NamingException e2) {
            }
            if (nameInNamespace.equals("")) {
                throw new FailedLoginException("Cannot find user's LDAP entry");
            }
            return nameInNamespace;
        }
        if (this.debug) {
            System.out.println("\t\t[LdapLoginModule] cannot search for entry belonging to user: " + this.username);
        }
        throw new FailedLoginException("Cannot find user's LDAP entry");
    }

    private String escapeUsernameChars() {
        int length = this.username.length();
        StringBuilder sb = new StringBuilder(length + 16);
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = this.username.charAt(i2);
            switch (cCharAt) {
                case 0:
                    sb.append("\\\\00");
                    break;
                case '(':
                    sb.append("\\\\28");
                    break;
                case ')':
                    sb.append("\\\\29");
                    break;
                case '*':
                    sb.append("\\\\2A");
                    break;
                case '\\':
                    sb.append("\\\\5C");
                    break;
                default:
                    sb.append(cCharAt);
                    break;
            }
        }
        return sb.toString();
    }

    private String replaceUsernameToken(Matcher matcher, String str, String str2) {
        return matcher != null ? matcher.replaceAll(str2) : str;
    }

    private void getUsernamePassword(boolean z2) throws LoginException {
        if (z2) {
            this.username = (String) this.sharedState.get(USERNAME_KEY);
            this.password = (char[]) this.sharedState.get(PASSWORD_KEY);
            return;
        }
        if (this.callbackHandler == null) {
            throw new LoginException("No CallbackHandler available to acquire authentication information from the user");
        }
        Callback[] callbackArr = {new NameCallback(rb.getString("username.")), new PasswordCallback(rb.getString("password."), false)};
        try {
            this.callbackHandler.handle(callbackArr);
            this.username = ((NameCallback) callbackArr[0]).getName();
            char[] password = ((PasswordCallback) callbackArr[1]).getPassword();
            this.password = new char[password.length];
            System.arraycopy(password, 0, this.password, 0, password.length);
            ((PasswordCallback) callbackArr[1]).clearPassword();
        } catch (IOException e2) {
            throw new LoginException(e2.toString());
        } catch (UnsupportedCallbackException e3) {
            throw new LoginException("Error: " + e3.getCallback().toString() + " not available to acquire authentication information from the user");
        }
    }

    private void cleanState() {
        this.username = null;
        if (this.password != null) {
            Arrays.fill(this.password, ' ');
            this.password = null;
        }
        try {
            if (this.ctx != null) {
                this.ctx.close();
            }
        } catch (NamingException e2) {
        }
        this.ctx = null;
        if (this.clearPass) {
            this.sharedState.remove(USERNAME_KEY);
            this.sharedState.remove(PASSWORD_KEY);
        }
    }
}
