package com.sun.corba.se.impl.resolver;

import com.sun.corba.se.spi.orbutil.closure.Closure;
import com.sun.corba.se.spi.resolver.LocalResolver;
import com.sun.corba.se.spi.resolver.Resolver;
import java.util.Set;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/resolver/SplitLocalResolverImpl.class */
public class SplitLocalResolverImpl implements LocalResolver {
    private Resolver resolver;
    private LocalResolver localResolver;

    public SplitLocalResolverImpl(Resolver resolver, LocalResolver localResolver) {
        this.resolver = resolver;
        this.localResolver = localResolver;
    }

    @Override // com.sun.corba.se.spi.resolver.LocalResolver
    public void register(String str, Closure closure) {
        this.localResolver.register(str, closure);
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Object resolve(String str) {
        return this.resolver.resolve(str);
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Set list() {
        return this.resolver.list();
    }
}
