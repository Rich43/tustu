package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/spi/InitialContextFactory.class */
public interface InitialContextFactory {
    Context getInitialContext(Hashtable<?, ?> hashtable) throws NamingException;
}
