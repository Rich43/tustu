package javax.naming.event;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/event/EventContext.class */
public interface EventContext extends Context {
    public static final int OBJECT_SCOPE = 0;
    public static final int ONELEVEL_SCOPE = 1;
    public static final int SUBTREE_SCOPE = 2;

    void addNamingListener(Name name, int i2, NamingListener namingListener) throws NamingException;

    void addNamingListener(String str, int i2, NamingListener namingListener) throws NamingException;

    void removeNamingListener(NamingListener namingListener) throws NamingException;

    boolean targetMustExist() throws NamingException;
}
