package com.sun.jndi.ldap;

import com.sun.jndi.toolkit.ctx.Continuation;
import java.util.Vector;
import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.Control;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapNamingEnumeration.class */
final class LdapNamingEnumeration extends AbstractLdapNamingEnumeration<NameClassPair> {
    private static final String defaultClassName = DirContext.class.getName();

    LdapNamingEnumeration(LdapCtx ldapCtx, LdapResult ldapResult, Name name, Continuation continuation) throws NamingException {
        super(ldapCtx, ldapResult, name, continuation);
    }

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration
    protected NameClassPair createItem(String str, Attributes attributes, Vector<Control> vector) throws NamingException {
        String str2;
        NameClassPair nameClassPair;
        Attribute attribute = attributes.get(Obj.JAVA_ATTRIBUTES[2]);
        if (attribute != null) {
            str2 = (String) attribute.get();
        } else {
            str2 = defaultClassName;
        }
        CompositeName compositeName = new CompositeName();
        compositeName.add(getAtom(str));
        if (vector != null) {
            nameClassPair = new NameClassPairWithControls(compositeName.toString(), str2, this.homeCtx.convertControls(vector));
        } else {
            nameClassPair = new NameClassPair(compositeName.toString(), str2);
        }
        nameClassPair.setNameInNamespace(str);
        return nameClassPair;
    }

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration
    protected AbstractLdapNamingEnumeration<? extends NameClassPair> getReferredResults(LdapReferralContext ldapReferralContext) throws NamingException {
        return (AbstractLdapNamingEnumeration) ldapReferralContext.list(this.listArg);
    }
}
