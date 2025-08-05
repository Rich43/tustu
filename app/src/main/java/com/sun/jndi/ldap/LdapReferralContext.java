package com.sun.jndi.ldap;

import com.sun.jndi.toolkit.dir.SearchFilter;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.ExtendedRequest;
import javax.naming.ldap.ExtendedResponse;
import javax.naming.ldap.LdapContext;
import javax.naming.spi.NamingManager;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapReferralContext.class */
final class LdapReferralContext implements DirContext, LdapContext {
    private DirContext refCtx;
    private LdapReferralException refEx;
    private boolean skipThisReferral;
    private NamingException previousEx;
    private Name urlName = null;
    private String urlAttrs = null;
    private String urlScope = null;
    private String urlFilter = null;
    private int hopCount = 1;

    LdapReferralContext(LdapReferralException ldapReferralException, Hashtable<?, ?> hashtable, Control[] controlArr, Control[] controlArr2, String str, boolean z2, int i2) throws NamingException {
        this.refCtx = null;
        this.refEx = null;
        this.skipThisReferral = false;
        this.previousEx = null;
        this.refEx = ldapReferralException;
        this.skipThisReferral = z2;
        if (z2) {
            return;
        }
        if (hashtable != null) {
            hashtable = (Hashtable) hashtable.clone();
            if (controlArr == null) {
                hashtable.remove("java.naming.ldap.control.connect");
            }
        } else if (controlArr != null) {
            hashtable = new Hashtable<>(5);
        }
        if (controlArr != null) {
            Object obj = new Control[controlArr.length];
            System.arraycopy(controlArr, 0, obj, 0, controlArr.length);
            hashtable.put("java.naming.ldap.control.connect", obj);
        }
        while (true) {
            try {
                String nextReferral = this.refEx.getNextReferral();
                if (nextReferral == null) {
                    if (this.previousEx != null) {
                        throw ((NamingException) this.previousEx.fillInStackTrace());
                    }
                    throw new NamingException("Illegal encoding: referral is empty");
                }
                try {
                    Object objectInstance = NamingManager.getObjectInstance(new Reference("javax.naming.directory.DirContext", new StringRefAddr("URL", nextReferral)), null, null, hashtable);
                    if (objectInstance instanceof DirContext) {
                        this.refCtx = (DirContext) objectInstance;
                        if ((this.refCtx instanceof LdapContext) && controlArr2 != null) {
                            ((LdapContext) this.refCtx).setRequestControls(controlArr2);
                        }
                        initDefaults(nextReferral, str);
                        return;
                    }
                    NotContextException notContextException = new NotContextException("Cannot create context for: " + nextReferral);
                    notContextException.setRemainingName(new CompositeName().add(str));
                    throw notContextException;
                } catch (NamingException e2) {
                    if (i2 == 2) {
                        throw e2;
                    }
                    this.previousEx = e2;
                } catch (Exception e3) {
                    NamingException namingException = new NamingException("problem generating object using object factory");
                    namingException.setRootCause(e3);
                    throw namingException;
                }
            } catch (LdapReferralException e4) {
                if (i2 == 2) {
                    throw e4;
                }
                this.refEx = e4;
            }
        }
    }

    private void initDefaults(String str, String str2) throws NamingException {
        String dn;
        String str3;
        try {
            LdapURL ldapURL = new LdapURL(str);
            dn = ldapURL.getDN();
            this.urlAttrs = ldapURL.getAttributes();
            this.urlScope = ldapURL.getScope();
            this.urlFilter = ldapURL.getFilter();
        } catch (NamingException e2) {
            dn = str;
            this.urlFilter = null;
            this.urlScope = null;
            this.urlAttrs = null;
        }
        if (dn == null) {
            str3 = str2;
        } else {
            str3 = "";
        }
        if (str3 == null) {
            this.urlName = null;
        } else {
            this.urlName = str3.equals("") ? new CompositeName() : new CompositeName().add(str3);
        }
    }

    @Override // javax.naming.Context
    public void close() throws NamingException {
        if (this.refCtx != null) {
            this.refCtx.close();
            this.refCtx = null;
        }
        this.refEx = null;
    }

    void setHopCount(int i2) {
        this.hopCount = i2;
        if (this.refCtx != null && (this.refCtx instanceof LdapCtx)) {
            ((LdapCtx) this.refCtx).setHopCount(i2);
        }
    }

    @Override // javax.naming.Context
    public Object lookup(String str) throws NamingException {
        return lookup(toName(str));
    }

    @Override // javax.naming.Context
    public Object lookup(Name name) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.lookup(overrideName(name));
    }

    @Override // javax.naming.Context
    public void bind(String str, Object obj) throws NamingException {
        bind(toName(str), obj);
    }

    @Override // javax.naming.Context
    public void bind(Name name, Object obj) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        this.refCtx.bind(overrideName(name), obj);
    }

    @Override // javax.naming.Context
    public void rebind(String str, Object obj) throws NamingException {
        rebind(toName(str), obj);
    }

    @Override // javax.naming.Context
    public void rebind(Name name, Object obj) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        this.refCtx.rebind(overrideName(name), obj);
    }

    @Override // javax.naming.Context
    public void unbind(String str) throws NamingException {
        unbind(toName(str));
    }

    @Override // javax.naming.Context
    public void unbind(Name name) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        this.refCtx.unbind(overrideName(name));
    }

    @Override // javax.naming.Context
    public void rename(String str, String str2) throws NamingException {
        rename(toName(str), toName(str2));
    }

    @Override // javax.naming.Context
    public void rename(Name name, Name name2) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        this.refCtx.rename(overrideName(name), toName(this.refEx.getNewRdn()));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(String str) throws NamingException {
        return list(toName(str));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        NamingEnumeration<NameClassPair> list;
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        try {
            if (this.urlScope != null && this.urlScope.equals("base")) {
                SearchControls searchControls = new SearchControls();
                searchControls.setReturningObjFlag(true);
                searchControls.setSearchScope(0);
                list = this.refCtx.search(overrideName(name), "(objectclass=*)", searchControls);
            } else {
                list = this.refCtx.list(overrideName(name));
            }
            this.refEx.setNameResolved(true);
            ((ReferralEnumeration) list).appendUnprocessedReferrals(this.refEx);
            return list;
        } catch (LdapReferralException e2) {
            e2.appendUnprocessedReferrals(this.refEx);
            throw ((NamingException) e2.fillInStackTrace());
        } catch (NamingException e3) {
            if (this.refEx != null && !this.refEx.hasMoreReferrals()) {
                this.refEx.setNamingException(e3);
            }
            if (this.refEx != null && (this.refEx.hasMoreReferrals() || this.refEx.hasMoreReferralExceptions())) {
                throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
            }
            throw e3;
        }
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(String str) throws NamingException {
        return listBindings(toName(str));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        NamingEnumeration<Binding> namingEnumerationListBindings;
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        try {
            if (this.urlScope != null && this.urlScope.equals("base")) {
                SearchControls searchControls = new SearchControls();
                searchControls.setReturningObjFlag(true);
                searchControls.setSearchScope(0);
                namingEnumerationListBindings = this.refCtx.search(overrideName(name), "(objectclass=*)", searchControls);
            } else {
                namingEnumerationListBindings = this.refCtx.listBindings(overrideName(name));
            }
            this.refEx.setNameResolved(true);
            ((ReferralEnumeration) namingEnumerationListBindings).appendUnprocessedReferrals(this.refEx);
            return namingEnumerationListBindings;
        } catch (LdapReferralException e2) {
            e2.appendUnprocessedReferrals(this.refEx);
            throw ((NamingException) e2.fillInStackTrace());
        } catch (NamingException e3) {
            if (this.refEx != null && !this.refEx.hasMoreReferrals()) {
                this.refEx.setNamingException(e3);
            }
            if (this.refEx != null && (this.refEx.hasMoreReferrals() || this.refEx.hasMoreReferralExceptions())) {
                throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
            }
            throw e3;
        }
    }

    @Override // javax.naming.Context
    public void destroySubcontext(String str) throws NamingException {
        destroySubcontext(toName(str));
    }

    @Override // javax.naming.Context
    public void destroySubcontext(Name name) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        this.refCtx.destroySubcontext(overrideName(name));
    }

    @Override // javax.naming.Context
    public Context createSubcontext(String str) throws NamingException {
        return createSubcontext(toName(str));
    }

    @Override // javax.naming.Context
    public Context createSubcontext(Name name) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.createSubcontext(overrideName(name));
    }

    @Override // javax.naming.Context
    public Object lookupLink(String str) throws NamingException {
        return lookupLink(toName(str));
    }

    @Override // javax.naming.Context
    public Object lookupLink(Name name) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.lookupLink(overrideName(name));
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(String str) throws NamingException {
        return getNameParser(toName(str));
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(Name name) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.getNameParser(overrideName(name));
    }

    @Override // javax.naming.Context
    public String composeName(String str, String str2) throws NamingException {
        return composeName(toName(str), toName(str2)).toString();
    }

    @Override // javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.composeName(name, name2);
    }

    @Override // javax.naming.Context
    public Object addToEnvironment(String str, Object obj) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.addToEnvironment(str, obj);
    }

    @Override // javax.naming.Context
    public Object removeFromEnvironment(String str) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.removeFromEnvironment(str);
    }

    @Override // javax.naming.Context
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.getEnvironment();
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str) throws NamingException {
        return getAttributes(toName(str));
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.getAttributes(overrideName(name));
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str, String[] strArr) throws NamingException {
        return getAttributes(toName(str), strArr);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name, String[] strArr) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.getAttributes(overrideName(name), strArr);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, int i2, Attributes attributes) throws NamingException {
        modifyAttributes(toName(str), i2, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, int i2, Attributes attributes) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        this.refCtx.modifyAttributes(overrideName(name), i2, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, ModificationItem[] modificationItemArr) throws NamingException {
        modifyAttributes(toName(str), modificationItemArr);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, ModificationItem[] modificationItemArr) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        this.refCtx.modifyAttributes(overrideName(name), modificationItemArr);
    }

    @Override // javax.naming.directory.DirContext
    public void bind(String str, Object obj, Attributes attributes) throws NamingException {
        bind(toName(str), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void bind(Name name, Object obj, Attributes attributes) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        this.refCtx.bind(overrideName(name), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(String str, Object obj, Attributes attributes) throws NamingException {
        rebind(toName(str), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(Name name, Object obj, Attributes attributes) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        this.refCtx.rebind(overrideName(name), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(String str, Attributes attributes) throws NamingException {
        return createSubcontext(toName(str), attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(Name name, Attributes attributes) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.createSubcontext(overrideName(name), attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(String str) throws NamingException {
        return getSchema(toName(str));
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(Name name) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.getSchema(overrideName(name));
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(String str) throws NamingException {
        return getSchemaClassDefinition(toName(str));
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return this.refCtx.getSchemaClassDefinition(overrideName(name));
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes) throws NamingException {
        return search(toName(str), SearchFilter.format(attributes), new SearchControls());
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes) throws NamingException {
        return search(name, SearchFilter.format(attributes), new SearchControls());
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes, String[] strArr) throws NamingException {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(strArr);
        return search(toName(str), SearchFilter.format(attributes), searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes, String[] strArr) throws NamingException {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(strArr);
        return search(name, SearchFilter.format(attributes), searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, SearchControls searchControls) throws NamingException {
        return search(toName(str), str2, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, SearchControls searchControls) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = this.refCtx.search(overrideName(name), overrideFilter(str), overrideAttributesAndScope(searchControls));
            this.refEx.setNameResolved(true);
            ((ReferralEnumeration) namingEnumerationSearch).appendUnprocessedReferrals(this.refEx);
            return namingEnumerationSearch;
        } catch (LdapReferralException e2) {
            e2.appendUnprocessedReferrals(this.refEx);
            throw ((NamingException) e2.fillInStackTrace());
        } catch (NamingException e3) {
            if (this.refEx != null && !this.refEx.hasMoreReferrals()) {
                this.refEx.setNamingException(e3);
            }
            if (this.refEx != null && (this.refEx.hasMoreReferrals() || this.refEx.hasMoreReferralExceptions())) {
                throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
            }
            throw e3;
        }
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, Object[] objArr, SearchControls searchControls) throws NamingException {
        return search(toName(str), str2, objArr, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, Object[] objArr, SearchControls searchControls) throws NamingException {
        NamingEnumeration<SearchResult> namingEnumerationSearch;
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        try {
            if (this.urlFilter != null) {
                namingEnumerationSearch = this.refCtx.search(overrideName(name), this.urlFilter, overrideAttributesAndScope(searchControls));
            } else {
                namingEnumerationSearch = this.refCtx.search(overrideName(name), str, objArr, overrideAttributesAndScope(searchControls));
            }
            this.refEx.setNameResolved(true);
            ((ReferralEnumeration) namingEnumerationSearch).appendUnprocessedReferrals(this.refEx);
            return namingEnumerationSearch;
        } catch (LdapReferralException e2) {
            e2.appendUnprocessedReferrals(this.refEx);
            throw ((NamingException) e2.fillInStackTrace());
        } catch (NamingException e3) {
            if (this.refEx != null && !this.refEx.hasMoreReferrals()) {
                this.refEx.setNamingException(e3);
            }
            if (this.refEx != null && (this.refEx.hasMoreReferrals() || this.refEx.hasMoreReferralExceptions())) {
                throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
            }
            throw e3;
        }
    }

    @Override // javax.naming.Context
    public String getNameInNamespace() throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        return (this.urlName == null || this.urlName.isEmpty()) ? "" : this.urlName.get(0);
    }

    @Override // javax.naming.ldap.LdapContext
    public ExtendedResponse extendedOperation(ExtendedRequest extendedRequest) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        if (!(this.refCtx instanceof LdapContext)) {
            throw new NotContextException("Referral context not an instance of LdapContext");
        }
        return ((LdapContext) this.refCtx).extendedOperation(extendedRequest);
    }

    @Override // javax.naming.ldap.LdapContext
    public LdapContext newInstance(Control[] controlArr) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        if (!(this.refCtx instanceof LdapContext)) {
            throw new NotContextException("Referral context not an instance of LdapContext");
        }
        return ((LdapContext) this.refCtx).newInstance(controlArr);
    }

    @Override // javax.naming.ldap.LdapContext
    public void reconnect(Control[] controlArr) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        if (!(this.refCtx instanceof LdapContext)) {
            throw new NotContextException("Referral context not an instance of LdapContext");
        }
        ((LdapContext) this.refCtx).reconnect(controlArr);
    }

    @Override // javax.naming.ldap.LdapContext
    public Control[] getConnectControls() throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        if (!(this.refCtx instanceof LdapContext)) {
            throw new NotContextException("Referral context not an instance of LdapContext");
        }
        return ((LdapContext) this.refCtx).getConnectControls();
    }

    @Override // javax.naming.ldap.LdapContext
    public void setRequestControls(Control[] controlArr) throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        if (!(this.refCtx instanceof LdapContext)) {
            throw new NotContextException("Referral context not an instance of LdapContext");
        }
        ((LdapContext) this.refCtx).setRequestControls(controlArr);
    }

    @Override // javax.naming.ldap.LdapContext
    public Control[] getRequestControls() throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        if (!(this.refCtx instanceof LdapContext)) {
            throw new NotContextException("Referral context not an instance of LdapContext");
        }
        return ((LdapContext) this.refCtx).getRequestControls();
    }

    @Override // javax.naming.ldap.LdapContext
    public Control[] getResponseControls() throws NamingException {
        if (this.skipThisReferral) {
            throw ((NamingException) this.refEx.appendUnprocessedReferrals(null).fillInStackTrace());
        }
        if (!(this.refCtx instanceof LdapContext)) {
            throw new NotContextException("Referral context not an instance of LdapContext");
        }
        return ((LdapContext) this.refCtx).getResponseControls();
    }

    private Name toName(String str) throws InvalidNameException {
        return str.equals("") ? new CompositeName() : new CompositeName().add(str);
    }

    private Name overrideName(Name name) throws InvalidNameException {
        return this.urlName == null ? name : this.urlName;
    }

    private SearchControls overrideAttributesAndScope(SearchControls searchControls) {
        if (this.urlScope != null || this.urlAttrs != null) {
            SearchControls searchControls2 = new SearchControls(searchControls.getSearchScope(), searchControls.getCountLimit(), searchControls.getTimeLimit(), searchControls.getReturningAttributes(), searchControls.getReturningObjFlag(), searchControls.getDerefLinkFlag());
            if (this.urlScope != null) {
                if (this.urlScope.equals("base")) {
                    searchControls2.setSearchScope(0);
                } else if (this.urlScope.equals("one")) {
                    searchControls2.setSearchScope(1);
                } else if (this.urlScope.equals("sub")) {
                    searchControls2.setSearchScope(2);
                }
            }
            if (this.urlAttrs != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(this.urlAttrs, ",");
                int iCountTokens = stringTokenizer.countTokens();
                String[] strArr = new String[iCountTokens];
                for (int i2 = 0; i2 < iCountTokens; i2++) {
                    strArr[i2] = stringTokenizer.nextToken();
                }
                searchControls2.setReturningAttributes(strArr);
            }
            return searchControls2;
        }
        return searchControls;
    }

    private String overrideFilter(String str) {
        return this.urlFilter == null ? str : this.urlFilter;
    }
}
