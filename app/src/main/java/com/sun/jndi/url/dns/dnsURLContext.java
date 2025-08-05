package com.sun.jndi.url.dns;

import com.sun.jndi.dns.DnsContextFactory;
import com.sun.jndi.dns.DnsUrl;
import com.sun.jndi.toolkit.url.GenericURLDirContext;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.spi.ResolveResult;

/* loaded from: rt.jar:com/sun/jndi/url/dns/dnsURLContext.class */
public class dnsURLContext extends GenericURLDirContext {
    public dnsURLContext(Hashtable<?, ?> hashtable) {
        super(hashtable);
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext
    protected ResolveResult getRootURLContext(String str, Hashtable<?, ?> hashtable) throws NamingException {
        try {
            DnsUrl dnsUrl = new DnsUrl(str);
            return new ResolveResult(DnsContextFactory.getContext(".", new DnsUrl[]{dnsUrl}, hashtable), new CompositeName().add(dnsUrl.getDomain()));
        } catch (MalformedURLException e2) {
            throw new InvalidNameException(e2.getMessage());
        }
    }
}
