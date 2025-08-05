package com.sun.jndi.toolkit.dir;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/* loaded from: rt.jar:com/sun/jndi/toolkit/dir/DirSearch.class */
public class DirSearch {
    public static NamingEnumeration<SearchResult> search(DirContext dirContext, Attributes attributes, String[] strArr) throws NamingException {
        return new LazySearchEnumerationImpl(new ContextEnumerator(dirContext, 1), new ContainmentFilter(attributes), new SearchControls(1, 0L, 0, strArr, false, false));
    }

    public static NamingEnumeration<SearchResult> search(DirContext dirContext, String str, SearchControls searchControls) throws NamingException {
        if (searchControls == null) {
            searchControls = new SearchControls();
        }
        return new LazySearchEnumerationImpl(new ContextEnumerator(dirContext, searchControls.getSearchScope()), new SearchFilter(str), searchControls);
    }

    public static NamingEnumeration<SearchResult> search(DirContext dirContext, String str, Object[] objArr, SearchControls searchControls) throws NamingException {
        return search(dirContext, SearchFilter.format(str, objArr), searchControls);
    }
}
