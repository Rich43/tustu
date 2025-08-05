package com.sun.jndi.dns;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* compiled from: DnsContext.java */
/* loaded from: rt.jar:com/sun/jndi/dns/BaseNameClassPairEnumeration.class */
abstract class BaseNameClassPairEnumeration<T> implements NamingEnumeration<T> {
    protected Enumeration<NameNode> nodes;
    protected DnsContext ctx;

    @Override // javax.naming.NamingEnumeration
    public abstract T next() throws NamingException;

    BaseNameClassPairEnumeration(DnsContext dnsContext, Hashtable<String, NameNode> hashtable) {
        this.ctx = dnsContext;
        this.nodes = hashtable != null ? hashtable.elements() : null;
    }

    @Override // javax.naming.NamingEnumeration
    public final void close() {
        this.nodes = null;
        this.ctx = null;
    }

    @Override // javax.naming.NamingEnumeration
    public final boolean hasMore() {
        boolean z2 = this.nodes != null && this.nodes.hasMoreElements();
        if (!z2) {
            close();
        }
        return z2;
    }

    @Override // java.util.Enumeration
    public final boolean hasMoreElements() {
        return hasMore();
    }

    @Override // java.util.Enumeration
    /* renamed from: nextElement */
    public final T nextElement2() {
        try {
            return next();
        } catch (NamingException e2) {
            NoSuchElementException noSuchElementException = new NoSuchElementException();
            noSuchElementException.initCause(e2);
            throw noSuchElementException;
        }
    }
}
