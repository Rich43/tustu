package com.sun.jndi.url.dns;

import java.util.Hashtable;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

/* loaded from: rt.jar:com/sun/jndi/url/dns/dnsURLContextFactory.class */
public class dnsURLContextFactory implements ObjectFactory {
    @Override // javax.naming.spi.ObjectFactory
    public Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        if (obj == null) {
            return new dnsURLContext(hashtable);
        }
        if (obj instanceof String) {
            return getUsingURL((String) obj, hashtable);
        }
        if (obj instanceof String[]) {
            return getUsingURLs((String[]) obj, hashtable);
        }
        throw new ConfigurationException("dnsURLContextFactory.getObjectInstance: argument must be a DNS URL String or an array of them");
    }

    private static Object getUsingURL(String str, Hashtable<?, ?> hashtable) throws NamingException {
        dnsURLContext dnsurlcontext = new dnsURLContext(hashtable);
        try {
            Object objLookup = dnsurlcontext.lookup(str);
            dnsurlcontext.close();
            return objLookup;
        } catch (Throwable th) {
            dnsurlcontext.close();
            throw th;
        }
    }

    private static Object getUsingURLs(String[] strArr, Hashtable<?, ?> hashtable) throws NamingException {
        if (strArr.length == 0) {
            throw new ConfigurationException("dnsURLContextFactory: empty URL array");
        }
        dnsURLContext dnsurlcontext = new dnsURLContext(hashtable);
        NamingException namingException = null;
        for (String str : strArr) {
            try {
                try {
                    return dnsurlcontext.lookup(str);
                } catch (NamingException e2) {
                    namingException = e2;
                }
            } finally {
                dnsurlcontext.close();
            }
        }
        throw namingException;
    }
}
