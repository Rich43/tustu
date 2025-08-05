package javax.naming.ldap;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

/* loaded from: rt.jar:javax/naming/ldap/LdapContext.class */
public interface LdapContext extends DirContext {
    public static final String CONTROL_FACTORIES = "java.naming.factory.control";

    ExtendedResponse extendedOperation(ExtendedRequest extendedRequest) throws NamingException;

    LdapContext newInstance(Control[] controlArr) throws NamingException;

    void reconnect(Control[] controlArr) throws NamingException;

    Control[] getConnectControls() throws NamingException;

    void setRequestControls(Control[] controlArr) throws NamingException;

    Control[] getRequestControls() throws NamingException;

    Control[] getResponseControls() throws NamingException;
}
