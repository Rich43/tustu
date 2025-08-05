package com.sun.corba.se.impl.resolver;

import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orb.StringPair;
import com.sun.corba.se.spi.resolver.Resolver;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/resolver/ORBInitRefResolverImpl.class */
public class ORBInitRefResolverImpl implements Resolver {
    Operation urlHandler;
    Map orbInitRefTable = new HashMap();

    public ORBInitRefResolverImpl(Operation operation, StringPair[] stringPairArr) {
        this.urlHandler = operation;
        for (StringPair stringPair : stringPairArr) {
            this.orbInitRefTable.put(stringPair.getFirst(), stringPair.getSecond());
        }
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Object resolve(String str) {
        String str2 = (String) this.orbInitRefTable.get(str);
        if (str2 == null) {
            return null;
        }
        return (Object) this.urlHandler.operate(str2);
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Set list() {
        return this.orbInitRefTable.keySet();
    }
}
