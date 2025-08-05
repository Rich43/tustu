package com.sun.corba.se.impl.resolver;

import com.sun.corba.se.spi.orbutil.closure.Closure;
import com.sun.corba.se.spi.resolver.LocalResolver;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/resolver/LocalResolverImpl.class */
public class LocalResolverImpl implements LocalResolver {
    Map nameToClosure = new HashMap();

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public synchronized Object resolve(String str) {
        Closure closure = (Closure) this.nameToClosure.get(str);
        if (closure == null) {
            return null;
        }
        return (Object) closure.evaluate();
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public synchronized Set list() {
        return this.nameToClosure.keySet();
    }

    @Override // com.sun.corba.se.spi.resolver.LocalResolver
    public synchronized void register(String str, Closure closure) {
        this.nameToClosure.put(str, closure);
    }
}
