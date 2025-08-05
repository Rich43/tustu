package com.sun.jndi.url.iiop;

import com.sun.jndi.cosnaming.CNCtx;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;
import javax.naming.spi.ResolveResult;

/* loaded from: rt.jar:com/sun/jndi/url/iiop/iiopURLContextFactory.class */
public class iiopURLContextFactory implements ObjectFactory {
    @Override // javax.naming.spi.ObjectFactory
    public Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws Exception {
        if (obj == null) {
            return new iiopURLContext(hashtable);
        }
        if (obj instanceof String) {
            return getUsingURL((String) obj, hashtable);
        }
        if (obj instanceof String[]) {
            return getUsingURLs((String[]) obj, hashtable);
        }
        throw new IllegalArgumentException("iiopURLContextFactory.getObjectInstance: argument must be a URL String or array of URLs");
    }

    static ResolveResult getUsingURLIgnoreRest(String str, Hashtable<?, ?> hashtable) throws NamingException {
        return CNCtx.createUsingURL(str, hashtable);
    }

    private static Object getUsingURL(String str, Hashtable<?, ?> hashtable) throws NamingException {
        ResolveResult usingURLIgnoreRest = getUsingURLIgnoreRest(str, hashtable);
        Context context = (Context) usingURLIgnoreRest.getResolvedObj();
        try {
            Object objLookup = context.lookup(usingURLIgnoreRest.getRemainingName());
            context.close();
            return objLookup;
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    private static Object getUsingURLs(String[] strArr, Hashtable<?, ?> hashtable) {
        Object usingURL;
        for (String str : strArr) {
            try {
                usingURL = getUsingURL(str, hashtable);
            } catch (NamingException e2) {
            }
            if (usingURL != null) {
                return usingURL;
            }
        }
        return null;
    }
}
