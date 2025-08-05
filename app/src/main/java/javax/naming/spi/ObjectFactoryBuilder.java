package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/spi/ObjectFactoryBuilder.class */
public interface ObjectFactoryBuilder {
    ObjectFactory createObjectFactory(Object obj, Hashtable<?, ?> hashtable) throws NamingException;
}
