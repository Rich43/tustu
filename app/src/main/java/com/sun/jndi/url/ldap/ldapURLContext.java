package com.sun.jndi.url.ldap;

import com.sun.jndi.ldap.LdapURL;
import com.sun.jndi.toolkit.url.GenericURLDirContext;
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
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.spi.ResolveResult;

/* loaded from: rt.jar:com/sun/jndi/url/ldap/ldapURLContext.class */
public final class ldapURLContext extends GenericURLDirContext {
    ldapURLContext(Hashtable<?, ?> hashtable) {
        super(hashtable);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext
    protected ResolveResult getRootURLContext(String str, Hashtable<?, ?> hashtable) throws NamingException {
        return ldapURLContextFactory.getUsingURLIgnoreRootDN(str, hashtable);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext
    protected Name getURLSuffix(String str, String str2) throws NamingException {
        LdapURL ldapURL = new LdapURL(str2);
        String dn = ldapURL.getDN() != null ? ldapURL.getDN() : "";
        CompositeName compositeName = new CompositeName();
        if (!"".equals(dn)) {
            compositeName.add(dn);
        }
        return compositeName;
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public Object lookup(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.lookup(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public Object lookup(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.lookup(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void bind(String str, Object obj) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        super.bind(str, obj);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void bind(Name name, Object obj) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        super.bind(name, obj);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void rebind(String str, Object obj) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        super.rebind(str, obj);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void rebind(Name name, Object obj) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        super.rebind(name, obj);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void unbind(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        super.unbind(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void unbind(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        super.unbind(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void rename(String str, String str2) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        if (LdapURL.hasQueryComponents(str2)) {
            throw new InvalidNameException(str2);
        }
        super.rename(str, str2);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void rename(Name name, Name name2) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        if (LdapURL.hasQueryComponents(name2.get(0))) {
            throw new InvalidNameException(name2.toString());
        }
        super.rename(name, name2);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public NamingEnumeration<NameClassPair> list(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.list(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.list(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public NamingEnumeration<Binding> listBindings(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.listBindings(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.listBindings(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void destroySubcontext(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        super.destroySubcontext(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public void destroySubcontext(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        super.destroySubcontext(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public Context createSubcontext(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.createSubcontext(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public Context createSubcontext(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.createSubcontext(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public Object lookupLink(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.lookupLink(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public Object lookupLink(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.lookupLink(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public NameParser getNameParser(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.getNameParser(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public NameParser getNameParser(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.getNameParser(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public String composeName(String str, String str2) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        if (LdapURL.hasQueryComponents(str2)) {
            throw new InvalidNameException(str2);
        }
        return super.composeName(str, str2);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext, javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        if (LdapURL.hasQueryComponents(name2.get(0))) {
            throw new InvalidNameException(name2.toString());
        }
        return super.composeName(name, name2);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public Attributes getAttributes(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.getAttributes(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public Attributes getAttributes(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.getAttributes(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public Attributes getAttributes(String str, String[] strArr) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.getAttributes(str, strArr);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public Attributes getAttributes(Name name, String[] strArr) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.getAttributes(name, strArr);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public void modifyAttributes(String str, int i2, Attributes attributes) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        super.modifyAttributes(str, i2, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public void modifyAttributes(Name name, int i2, Attributes attributes) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        super.modifyAttributes(name, i2, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public void modifyAttributes(String str, ModificationItem[] modificationItemArr) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        super.modifyAttributes(str, modificationItemArr);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public void modifyAttributes(Name name, ModificationItem[] modificationItemArr) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        super.modifyAttributes(name, modificationItemArr);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public void bind(String str, Object obj, Attributes attributes) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        super.bind(str, obj, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public void bind(Name name, Object obj, Attributes attributes) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        super.bind(name, obj, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public void rebind(String str, Object obj, Attributes attributes) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        super.rebind(str, obj, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public void rebind(Name name, Object obj, Attributes attributes) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        super.rebind(name, obj, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public DirContext createSubcontext(String str, Attributes attributes) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.createSubcontext(str, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public DirContext createSubcontext(Name name, Attributes attributes) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.createSubcontext(name, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public DirContext getSchema(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.getSchema(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public DirContext getSchema(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.getSchema(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(String str) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            throw new InvalidNameException(str);
        }
        return super.getSchemaClassDefinition(str);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.getSchemaClassDefinition(name);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            return searchUsingURL(str);
        }
        return super.search(str, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes) throws NamingException {
        if (name.size() == 1) {
            return search(name.get(0), attributes);
        }
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.search(name, attributes);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes, String[] strArr) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            return searchUsingURL(str);
        }
        return super.search(str, attributes, strArr);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes, String[] strArr) throws NamingException {
        if (name.size() == 1) {
            return search(name.get(0), attributes, strArr);
        }
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.search(name, attributes, strArr);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, SearchControls searchControls) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            return searchUsingURL(str);
        }
        return super.search(str, str2, searchControls);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, SearchControls searchControls) throws NamingException {
        if (name.size() == 1) {
            return search(name.get(0), str, searchControls);
        }
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.search(name, str, searchControls);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, Object[] objArr, SearchControls searchControls) throws NamingException {
        if (LdapURL.hasQueryComponents(str)) {
            return searchUsingURL(str);
        }
        return super.search(str, str2, objArr, searchControls);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLDirContext, javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, Object[] objArr, SearchControls searchControls) throws NamingException {
        if (name.size() == 1) {
            return search(name.get(0), str, objArr, searchControls);
        }
        if (LdapURL.hasQueryComponents(name.get(0))) {
            throw new InvalidNameException(name.toString());
        }
        return super.search(name, str, objArr, searchControls);
    }

    private NamingEnumeration<SearchResult> searchUsingURL(String str) throws NamingException {
        LdapURL ldapURL = new LdapURL(str);
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = dirContext.search(rootURLContext.getRemainingName(), setFilterUsingURL(ldapURL), setSearchControlsUsingURL(ldapURL));
            dirContext.close();
            return namingEnumerationSearch;
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    private static String setFilterUsingURL(LdapURL ldapURL) {
        String filter = ldapURL.getFilter();
        if (filter == null) {
            filter = "(objectClass=*)";
        }
        return filter;
    }

    private static SearchControls setSearchControlsUsingURL(LdapURL ldapURL) {
        SearchControls searchControls = new SearchControls();
        String scope = ldapURL.getScope();
        String attributes = ldapURL.getAttributes();
        if (scope == null) {
            searchControls.setSearchScope(0);
        } else if (scope.equals("sub")) {
            searchControls.setSearchScope(2);
        } else if (scope.equals("one")) {
            searchControls.setSearchScope(1);
        } else if (scope.equals("base")) {
            searchControls.setSearchScope(0);
        }
        if (attributes == null) {
            searchControls.setReturningAttributes(null);
        } else {
            StringTokenizer stringTokenizer = new StringTokenizer(attributes, ",");
            int iCountTokens = stringTokenizer.countTokens();
            String[] strArr = new String[iCountTokens];
            for (int i2 = 0; i2 < iCountTokens; i2++) {
                strArr[i2] = stringTokenizer.nextToken();
            }
            searchControls.setReturningAttributes(strArr);
        }
        return searchControls;
    }
}
