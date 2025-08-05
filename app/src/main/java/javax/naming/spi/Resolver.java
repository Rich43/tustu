package javax.naming.spi;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/spi/Resolver.class */
public interface Resolver {
    ResolveResult resolveToClass(Name name, Class<? extends Context> cls) throws NamingException;

    ResolveResult resolveToClass(String str, Class<? extends Context> cls) throws NamingException;
}
