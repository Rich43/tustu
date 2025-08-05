package javax.naming.event;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

/* loaded from: rt.jar:javax/naming/event/EventDirContext.class */
public interface EventDirContext extends EventContext, DirContext {
    void addNamingListener(Name name, String str, SearchControls searchControls, NamingListener namingListener) throws NamingException;

    void addNamingListener(String str, String str2, SearchControls searchControls, NamingListener namingListener) throws NamingException;

    void addNamingListener(Name name, String str, Object[] objArr, SearchControls searchControls, NamingListener namingListener) throws NamingException;

    void addNamingListener(String str, String str2, Object[] objArr, SearchControls searchControls, NamingListener namingListener) throws NamingException;
}
