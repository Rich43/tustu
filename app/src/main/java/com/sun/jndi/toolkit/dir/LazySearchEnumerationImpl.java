package com.sun.jndi.toolkit.dir;

import java.util.Hashtable;
import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.spi.DirectoryManager;

/* loaded from: rt.jar:com/sun/jndi/toolkit/dir/LazySearchEnumerationImpl.class */
public final class LazySearchEnumerationImpl implements NamingEnumeration<SearchResult> {
    private NamingEnumeration<Binding> candidates;
    private SearchResult nextMatch;
    private SearchControls cons;
    private AttrFilter filter;
    private Context context;
    private Hashtable<String, Object> env;
    private boolean useFactory;

    public LazySearchEnumerationImpl(NamingEnumeration<Binding> namingEnumeration, AttrFilter attrFilter, SearchControls searchControls) throws NamingException {
        this.nextMatch = null;
        this.useFactory = true;
        this.candidates = namingEnumeration;
        this.filter = attrFilter;
        if (searchControls == null) {
            this.cons = new SearchControls();
        } else {
            this.cons = searchControls;
        }
    }

    public LazySearchEnumerationImpl(NamingEnumeration<Binding> namingEnumeration, AttrFilter attrFilter, SearchControls searchControls, Context context, Hashtable<String, Object> hashtable, boolean z2) throws NamingException {
        this.nextMatch = null;
        this.useFactory = true;
        this.candidates = namingEnumeration;
        this.filter = attrFilter;
        this.env = (Hashtable) (hashtable == null ? null : hashtable.clone());
        this.context = context;
        this.useFactory = z2;
        if (searchControls == null) {
            this.cons = new SearchControls();
        } else {
            this.cons = searchControls;
        }
    }

    public LazySearchEnumerationImpl(NamingEnumeration<Binding> namingEnumeration, AttrFilter attrFilter, SearchControls searchControls, Context context, Hashtable<String, Object> hashtable) throws NamingException {
        this(namingEnumeration, attrFilter, searchControls, context, hashtable, true);
    }

    @Override // javax.naming.NamingEnumeration
    public boolean hasMore() throws NamingException {
        return findNextMatch(false) != null;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        try {
            return hasMore();
        } catch (NamingException e2) {
            return false;
        }
    }

    @Override // java.util.Enumeration
    /* renamed from: nextElement */
    public SearchResult nextElement2() {
        try {
            return findNextMatch(true);
        } catch (NamingException e2) {
            throw new NoSuchElementException(e2.toString());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javax.naming.NamingEnumeration
    public SearchResult next() throws NamingException {
        return findNextMatch(true);
    }

    @Override // javax.naming.NamingEnumeration
    public void close() throws NamingException {
        if (this.candidates != null) {
            this.candidates.close();
        }
    }

    private SearchResult findNextMatch(boolean z2) throws NamingException {
        if (this.nextMatch != null) {
            SearchResult searchResult = this.nextMatch;
            if (z2) {
                this.nextMatch = null;
            }
            return searchResult;
        }
        while (this.candidates.hasMore()) {
            Binding next = this.candidates.next();
            Object object = next.getObject();
            if (object instanceof DirContext) {
                Attributes attributes = ((DirContext) object).getAttributes("");
                if (this.filter.check(attributes)) {
                    if (!this.cons.getReturningObjFlag()) {
                        object = null;
                    } else if (this.useFactory) {
                        try {
                            object = DirectoryManager.getObjectInstance(object, this.context != null ? new CompositeName(next.getName()) : null, this.context, this.env, attributes);
                        } catch (NamingException e2) {
                            throw e2;
                        } catch (Exception e3) {
                            NamingException namingException = new NamingException("problem generating object using object factory");
                            namingException.setRootCause(e3);
                            throw namingException;
                        }
                    }
                    SearchResult searchResult2 = new SearchResult(next.getName(), next.getClassName(), object, SearchFilter.selectAttributes(attributes, this.cons.getReturningAttributes()), true);
                    if (!z2) {
                        this.nextMatch = searchResult2;
                    }
                    return searchResult2;
                }
            }
        }
        return null;
    }
}
