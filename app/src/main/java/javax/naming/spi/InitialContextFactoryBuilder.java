package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/spi/InitialContextFactoryBuilder.class */
public interface InitialContextFactoryBuilder {
    InitialContextFactory createInitialContextFactory(Hashtable<?, ?> hashtable) throws NamingException;
}
