package com.sun.jndi.url.rmi;

import java.util.Hashtable;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

/* loaded from: rt.jar:com/sun/jndi/url/rmi/rmiURLContextFactory.class */
public class rmiURLContextFactory implements ObjectFactory {
    @Override // javax.naming.spi.ObjectFactory
    public Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        if (obj == null) {
            return new rmiURLContext(hashtable);
        }
        if (obj instanceof String) {
            return getUsingURL((String) obj, hashtable);
        }
        if (obj instanceof String[]) {
            return getUsingURLs((String[]) obj, hashtable);
        }
        throw new ConfigurationException("rmiURLContextFactory.getObjectInstance: argument must be an RMI URL String or an array of them");
    }

    private static Object getUsingURL(String str, Hashtable<?, ?> hashtable) throws NamingException {
        rmiURLContext rmiurlcontext = new rmiURLContext(hashtable);
        try {
            Object objLookup = rmiurlcontext.lookup(str);
            rmiurlcontext.close();
            return objLookup;
        } catch (Throwable th) {
            rmiurlcontext.close();
            throw th;
        }
    }

    private static Object getUsingURLs(String[] strArr, Hashtable<?, ?> hashtable) throws NamingException {
        if (strArr.length == 0) {
            throw new ConfigurationException("rmiURLContextFactory: empty URL array");
        }
        rmiURLContext rmiurlcontext = new rmiURLContext(hashtable);
        NamingException namingException = null;
        for (String str : strArr) {
            try {
                try {
                    return rmiurlcontext.lookup(str);
                } catch (NamingException e2) {
                    namingException = e2;
                }
            } finally {
                rmiurlcontext.close();
            }
        }
        throw namingException;
    }
}
