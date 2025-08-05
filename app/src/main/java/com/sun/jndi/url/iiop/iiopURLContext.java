package com.sun.jndi.url.iiop;

import com.sun.jndi.cosnaming.CorbanameUrl;
import com.sun.jndi.cosnaming.IiopUrl;
import com.sun.jndi.toolkit.url.GenericURLContext;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ResolveResult;

/* loaded from: rt.jar:com/sun/jndi/url/iiop/iiopURLContext.class */
public class iiopURLContext extends GenericURLContext {
    iiopURLContext(Hashtable<?, ?> hashtable) {
        super(hashtable);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext
    protected ResolveResult getRootURLContext(String str, Hashtable<?, ?> hashtable) throws NamingException {
        return iiopURLContextFactory.getUsingURLIgnoreRest(str, hashtable);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext
    protected Name getURLSuffix(String str, String str2) throws MalformedURLException, NamingException {
        try {
            if (str2.startsWith("iiop://") || str2.startsWith("iiopname://")) {
                return new IiopUrl(str2).getCosName();
            }
            if (str2.startsWith("corbaname:")) {
                return new CorbanameUrl(str2).getCosName();
            }
            throw new MalformedURLException("Not a valid URL: " + str2);
        } catch (MalformedURLException e2) {
            throw new InvalidNameException(e2.getMessage());
        }
    }
}
