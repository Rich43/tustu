package javax.naming;

import java.util.Enumeration;

/* loaded from: rt.jar:javax/naming/NamingEnumeration.class */
public interface NamingEnumeration<T> extends Enumeration<T> {
    T next() throws NamingException;

    boolean hasMore() throws NamingException;

    void close() throws NamingException;
}
