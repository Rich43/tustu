package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.directory.Attributes;

/* loaded from: rt.jar:javax/naming/spi/DirObjectFactory.class */
public interface DirObjectFactory extends ObjectFactory {
    Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable, Attributes attributes) throws Exception;
}
