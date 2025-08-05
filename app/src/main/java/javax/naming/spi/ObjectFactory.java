package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;

/* loaded from: rt.jar:javax/naming/spi/ObjectFactory.class */
public interface ObjectFactory {
    Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws Exception;
}
