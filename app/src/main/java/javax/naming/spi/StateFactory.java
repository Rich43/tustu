package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/spi/StateFactory.class */
public interface StateFactory {
    Object getStateToBind(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException;
}
