package com.sun.jndi.ldap;

import com.sun.jndi.ldap.LdapCtx;
import com.sun.jndi.toolkit.ctx.Continuation;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Vector;
import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.spi.DirectoryManager;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapSearchEnumeration.class */
final class LdapSearchEnumeration extends AbstractLdapNamingEnumeration<SearchResult> {
    private Name startName;
    private LdapCtx.SearchArgs searchArgs;
    private final AccessControlContext acc;

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration
    protected /* bridge */ /* synthetic */ NameClassPair createItem(String str, Attributes attributes, Vector vector) throws NamingException {
        return createItem(str, attributes, (Vector<Control>) vector);
    }

    LdapSearchEnumeration(LdapCtx ldapCtx, LdapResult ldapResult, String str, LdapCtx.SearchArgs searchArgs, Continuation continuation) throws NamingException {
        super(ldapCtx, ldapResult, searchArgs.name, continuation);
        this.searchArgs = null;
        this.acc = AccessController.getContext();
        this.startName = new javax.naming.ldap.LdapName(str);
        this.searchArgs = searchArgs;
    }

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration
    protected SearchResult createItem(String str, final Attributes attributes, Vector<Control> vector) throws NamingException {
        String string;
        String string2;
        SearchResult searchResult;
        LdapCtx ldapCtx;
        Object objDoPrivileged = null;
        boolean z2 = true;
        try {
            javax.naming.ldap.LdapName ldapName = new javax.naming.ldap.LdapName(str);
            if (this.startName != null && ldapName.startsWith(this.startName)) {
                string = ldapName.getSuffix(this.startName.size()).toString();
                string2 = ldapName.getSuffix(this.homeCtx.currentParsedDN.size()).toString();
            } else {
                z2 = false;
                String urlString = LdapURL.toUrlString(this.homeCtx.hostname, this.homeCtx.port_number, str, this.homeCtx.hasLdapsScheme);
                string = urlString;
                string2 = urlString;
            }
        } catch (NamingException e2) {
            z2 = false;
            String urlString2 = LdapURL.toUrlString(this.homeCtx.hostname, this.homeCtx.port_number, str, this.homeCtx.hasLdapsScheme);
            string = urlString2;
            string2 = urlString2;
        }
        CompositeName compositeName = new CompositeName();
        if (!string.equals("")) {
            compositeName.add(string);
        }
        CompositeName compositeName2 = new CompositeName();
        if (!string2.equals("")) {
            compositeName2.add(string2);
        }
        this.homeCtx.setParents(attributes, compositeName2);
        if (this.searchArgs.cons.getReturningObjFlag()) {
            if (attributes.get(Obj.JAVA_ATTRIBUTES[2]) != null) {
                try {
                    objDoPrivileged = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: com.sun.jndi.ldap.LdapSearchEnumeration.1
                        @Override // java.security.PrivilegedExceptionAction
                        public Object run() throws NamingException {
                            return Obj.decodeObject(attributes);
                        }
                    }, this.acc);
                } catch (PrivilegedActionException e3) {
                    throw ((NamingException) e3.getException());
                }
            }
            if (objDoPrivileged == null) {
                objDoPrivileged = new LdapCtx(this.homeCtx, str);
            }
            Object obj = objDoPrivileged;
            if (z2) {
                try {
                    ldapCtx = this.homeCtx;
                } catch (NamingException e4) {
                    throw e4;
                } catch (Exception e5) {
                    NamingException namingException = new NamingException("problem generating object using object factory");
                    namingException.setRootCause(e5);
                    throw namingException;
                }
            } else {
                ldapCtx = null;
            }
            objDoPrivileged = DirectoryManager.getObjectInstance(obj, compositeName2, ldapCtx, this.homeCtx.envprops, attributes);
            String[] strArr = this.searchArgs.reqAttrs;
            if (strArr != null) {
                BasicAttributes basicAttributes = new BasicAttributes(true);
                for (String str2 : strArr) {
                    basicAttributes.put(str2, null);
                }
                for (int i2 = 0; i2 < Obj.JAVA_ATTRIBUTES.length; i2++) {
                    if (basicAttributes.get(Obj.JAVA_ATTRIBUTES[i2]) == null) {
                        attributes.remove(Obj.JAVA_ATTRIBUTES[i2]);
                    }
                }
            }
        }
        if (vector != null) {
            searchResult = new SearchResultWithControls(z2 ? compositeName.toString() : string, objDoPrivileged, attributes, z2, this.homeCtx.convertControls(vector));
        } else {
            searchResult = new SearchResult(z2 ? compositeName.toString() : string, objDoPrivileged, attributes, z2);
        }
        searchResult.setNameInNamespace(str);
        return searchResult;
    }

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration, com.sun.jndi.ldap.ReferralEnumeration
    public void appendUnprocessedReferrals(LdapReferralException ldapReferralException) {
        this.startName = null;
        super.appendUnprocessedReferrals(ldapReferralException);
    }

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration
    protected AbstractLdapNamingEnumeration<? extends NameClassPair> getReferredResults(LdapReferralContext ldapReferralContext) throws NamingException {
        return (AbstractLdapNamingEnumeration) ldapReferralContext.search(this.searchArgs.name, this.searchArgs.filter, this.searchArgs.cons);
    }

    @Override // com.sun.jndi.ldap.AbstractLdapNamingEnumeration
    protected void update(AbstractLdapNamingEnumeration<? extends NameClassPair> abstractLdapNamingEnumeration) {
        super.update(abstractLdapNamingEnumeration);
        this.startName = ((LdapSearchEnumeration) abstractLdapNamingEnumeration).startName;
    }

    void setStartName(Name name) {
        this.startName = name;
    }
}
