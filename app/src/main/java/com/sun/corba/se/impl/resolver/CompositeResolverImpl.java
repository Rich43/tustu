package com.sun.corba.se.impl.resolver;

import com.sun.corba.se.spi.resolver.Resolver;
import java.util.HashSet;
import java.util.Set;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/resolver/CompositeResolverImpl.class */
public class CompositeResolverImpl implements Resolver {
    private Resolver first;
    private Resolver second;

    public CompositeResolverImpl(Resolver resolver, Resolver resolver2) {
        this.first = resolver;
        this.second = resolver2;
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Object resolve(String str) {
        Object objectResolve = this.first.resolve(str);
        if (objectResolve == null) {
            objectResolve = this.second.resolve(str);
        }
        return objectResolve;
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Set list() {
        HashSet hashSet = new HashSet();
        hashSet.addAll(this.first.list());
        hashSet.addAll(this.second.list());
        return hashSet;
    }
}
