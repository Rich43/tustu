package javax.naming;

import java.util.Hashtable;

/* loaded from: rt.jar:javax/naming/Context.class */
public interface Context {
    public static final String INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial";
    public static final String OBJECT_FACTORIES = "java.naming.factory.object";
    public static final String STATE_FACTORIES = "java.naming.factory.state";
    public static final String URL_PKG_PREFIXES = "java.naming.factory.url.pkgs";
    public static final String PROVIDER_URL = "java.naming.provider.url";
    public static final String DNS_URL = "java.naming.dns.url";
    public static final String AUTHORITATIVE = "java.naming.authoritative";
    public static final String BATCHSIZE = "java.naming.batchsize";
    public static final String REFERRAL = "java.naming.referral";
    public static final String SECURITY_PROTOCOL = "java.naming.security.protocol";
    public static final String SECURITY_AUTHENTICATION = "java.naming.security.authentication";
    public static final String SECURITY_PRINCIPAL = "java.naming.security.principal";
    public static final String SECURITY_CREDENTIALS = "java.naming.security.credentials";
    public static final String LANGUAGE = "java.naming.language";
    public static final String APPLET = "java.naming.applet";

    Object lookup(Name name) throws NamingException;

    Object lookup(String str) throws NamingException;

    void bind(Name name, Object obj) throws NamingException;

    void bind(String str, Object obj) throws NamingException;

    void rebind(Name name, Object obj) throws NamingException;

    void rebind(String str, Object obj) throws NamingException;

    void unbind(Name name) throws NamingException;

    void unbind(String str) throws NamingException;

    void rename(Name name, Name name2) throws NamingException;

    void rename(String str, String str2) throws NamingException;

    NamingEnumeration<NameClassPair> list(Name name) throws NamingException;

    NamingEnumeration<NameClassPair> list(String str) throws NamingException;

    NamingEnumeration<Binding> listBindings(Name name) throws NamingException;

    NamingEnumeration<Binding> listBindings(String str) throws NamingException;

    void destroySubcontext(Name name) throws NamingException;

    void destroySubcontext(String str) throws NamingException;

    Context createSubcontext(Name name) throws NamingException;

    Context createSubcontext(String str) throws NamingException;

    Object lookupLink(Name name) throws NamingException;

    Object lookupLink(String str) throws NamingException;

    NameParser getNameParser(Name name) throws NamingException;

    NameParser getNameParser(String str) throws NamingException;

    Name composeName(Name name, Name name2) throws NamingException;

    String composeName(String str, String str2) throws NamingException;

    Object addToEnvironment(String str, Object obj) throws NamingException;

    Object removeFromEnvironment(String str) throws NamingException;

    Hashtable<?, ?> getEnvironment() throws NamingException;

    void close() throws NamingException;

    String getNameInNamespace() throws NamingException;
}
