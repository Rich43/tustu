package com.sun.jndi.cosnaming;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/* loaded from: rt.jar:com/sun/jndi/cosnaming/CNCtxFactory.class */
public class CNCtxFactory implements InitialContextFactory {
    @Override // javax.naming.spi.InitialContextFactory
    public Context getInitialContext(Hashtable<?, ?> hashtable) throws NamingException {
        return new CNCtx(hashtable);
    }
}
