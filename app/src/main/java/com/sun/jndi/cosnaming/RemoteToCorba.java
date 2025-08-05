package com.sun.jndi.cosnaming;

import com.sun.jndi.toolkit.corba.CorbaUtils;
import java.rmi.Remote;
import java.util.Hashtable;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.StateFactory;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/jndi/cosnaming/RemoteToCorba.class */
public class RemoteToCorba implements StateFactory {
    @Override // javax.naming.spi.StateFactory
    public Object getStateToBind(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        if (!(obj instanceof Object) && (obj instanceof Remote)) {
            try {
                return CorbaUtils.remoteToCorba((Remote) obj, ((CNCtx) context)._orb);
            } catch (ClassNotFoundException e2) {
                throw new ConfigurationException("javax.rmi packages not available");
            }
        }
        return null;
    }
}
