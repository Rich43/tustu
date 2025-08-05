package javax.naming.ldap;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.NotContextException;
import javax.naming.directory.InitialDirContext;

/* loaded from: rt.jar:javax/naming/ldap/InitialLdapContext.class */
public class InitialLdapContext extends InitialDirContext implements LdapContext {
    private static final String BIND_CONTROLS_PROPERTY = "java.naming.ldap.control.connect";

    public InitialLdapContext() throws NamingException {
        super((Hashtable<?, ?>) null);
    }

    public InitialLdapContext(Hashtable<?, ?> hashtable, Control[] controlArr) throws NamingException {
        super(true);
        Hashtable<?, ?> hashtable2 = hashtable == null ? new Hashtable<>(11) : (Hashtable) hashtable.clone();
        if (controlArr != null) {
            Object obj = new Control[controlArr.length];
            System.arraycopy(controlArr, 0, obj, 0, controlArr.length);
            hashtable2.put(BIND_CONTROLS_PROPERTY, obj);
        }
        hashtable2.put("java.naming.ldap.version", "3");
        init(hashtable2);
    }

    private LdapContext getDefaultLdapInitCtx() throws NamingException {
        Context defaultInitCtx = getDefaultInitCtx();
        if (!(defaultInitCtx instanceof LdapContext)) {
            if (defaultInitCtx == null) {
                throw new NoInitialContextException();
            }
            throw new NotContextException("Not an instance of LdapContext");
        }
        return (LdapContext) defaultInitCtx;
    }

    @Override // javax.naming.ldap.LdapContext
    public ExtendedResponse extendedOperation(ExtendedRequest extendedRequest) throws NamingException {
        return getDefaultLdapInitCtx().extendedOperation(extendedRequest);
    }

    @Override // javax.naming.ldap.LdapContext
    public LdapContext newInstance(Control[] controlArr) throws NamingException {
        return getDefaultLdapInitCtx().newInstance(controlArr);
    }

    @Override // javax.naming.ldap.LdapContext
    public void reconnect(Control[] controlArr) throws NamingException {
        getDefaultLdapInitCtx().reconnect(controlArr);
    }

    @Override // javax.naming.ldap.LdapContext
    public Control[] getConnectControls() throws NamingException {
        return getDefaultLdapInitCtx().getConnectControls();
    }

    @Override // javax.naming.ldap.LdapContext
    public void setRequestControls(Control[] controlArr) throws NamingException {
        getDefaultLdapInitCtx().setRequestControls(controlArr);
    }

    @Override // javax.naming.ldap.LdapContext
    public Control[] getRequestControls() throws NamingException {
        return getDefaultLdapInitCtx().getRequestControls();
    }

    @Override // javax.naming.ldap.LdapContext
    public Control[] getResponseControls() throws NamingException {
        return getDefaultLdapInitCtx().getResponseControls();
    }
}
