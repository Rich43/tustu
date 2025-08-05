package com.sun.jndi.ldap;

import com.sun.jndi.toolkit.ctx.Continuation;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Vector;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.Control;
import javax.naming.spi.DirectoryManager;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapBindingEnumeration.class */
final class LdapBindingEnumeration extends AbstractLdapNamingEnumeration<Binding> {
    private final AccessControlContext acc;

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration
    protected /* bridge */ /* synthetic */ NameClassPair createItem(String str, Attributes attributes, Vector vector) throws NamingException {
        return createItem(str, attributes, (Vector<Control>) vector);
    }

    LdapBindingEnumeration(LdapCtx ldapCtx, LdapResult ldapResult, Name name, Continuation continuation) throws NamingException {
        super(ldapCtx, ldapResult, name, continuation);
        this.acc = AccessController.getContext();
    }

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration
    protected Binding createItem(String str, final Attributes attributes, Vector<Control> vector) throws NamingException {
        Binding binding;
        Object objDoPrivileged = null;
        String atom = getAtom(str);
        if (attributes.get(Obj.JAVA_ATTRIBUTES[2]) != null) {
            try {
                objDoPrivileged = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: com.sun.jndi.ldap.LdapBindingEnumeration.1
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() throws NamingException {
                        return Obj.decodeObject(attributes);
                    }
                }, this.acc);
            } catch (PrivilegedActionException e2) {
                throw ((NamingException) e2.getException());
            }
        }
        if (objDoPrivileged == null) {
            objDoPrivileged = new LdapCtx(this.homeCtx, str);
        }
        CompositeName compositeName = new CompositeName();
        compositeName.add(atom);
        try {
            Object objectInstance = DirectoryManager.getObjectInstance(objDoPrivileged, compositeName, this.homeCtx, this.homeCtx.envprops, attributes);
            if (vector != null) {
                binding = new BindingWithControls(compositeName.toString(), objectInstance, this.homeCtx.convertControls(vector));
            } else {
                binding = new Binding(compositeName.toString(), objectInstance);
            }
            binding.setNameInNamespace(str);
            return binding;
        } catch (NamingException e3) {
            throw e3;
        } catch (Exception e4) {
            NamingException namingException = new NamingException("problem generating object using object factory");
            namingException.setRootCause(e4);
            throw namingException;
        }
    }

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration
    protected AbstractLdapNamingEnumeration<? extends NameClassPair> getReferredResults(LdapReferralContext ldapReferralContext) throws NamingException {
        return (AbstractLdapNamingEnumeration) ldapReferralContext.listBindings(this.listArg);
    }
}
