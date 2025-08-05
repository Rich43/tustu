package com.sun.jndi.dns;

import java.util.Hashtable;
import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.DirectoryManager;

/* compiled from: DnsContext.java */
/* loaded from: rt.jar:com/sun/jndi/dns/BindingEnumeration.class */
final class BindingEnumeration extends BaseNameClassPairEnumeration<Binding> implements NamingEnumeration<Binding> {
    BindingEnumeration(DnsContext dnsContext, Hashtable<String, NameNode> hashtable) {
        super(dnsContext, hashtable);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.jndi.dns.BaseNameClassPairEnumeration, javax.naming.NamingEnumeration
    public Binding next() throws NamingException {
        if (!hasMore()) {
            throw new NoSuchElementException();
        }
        Name nameAdd = new DnsName().add(this.nodes.nextElement2().getLabel());
        Name nameAdd2 = new CompositeName().add(nameAdd.toString());
        String string = nameAdd2.toString();
        DnsContext dnsContext = new DnsContext(this.ctx, this.ctx.fullyQualify(nameAdd));
        try {
            Binding binding = new Binding(string, DirectoryManager.getObjectInstance(dnsContext, nameAdd2, this.ctx, dnsContext.environment, null));
            binding.setNameInNamespace(this.ctx.fullyQualify(nameAdd2).toString());
            return binding;
        } catch (Exception e2) {
            NamingException namingException = new NamingException("Problem generating object using object factory");
            namingException.setRootCause(e2);
            throw namingException;
        }
    }
}
