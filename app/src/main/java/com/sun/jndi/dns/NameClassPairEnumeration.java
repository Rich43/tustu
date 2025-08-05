package com.sun.jndi.dns;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* compiled from: DnsContext.java */
/* loaded from: rt.jar:com/sun/jndi/dns/NameClassPairEnumeration.class */
final class NameClassPairEnumeration extends BaseNameClassPairEnumeration<NameClassPair> implements NamingEnumeration<NameClassPair> {
    NameClassPairEnumeration(DnsContext dnsContext, Hashtable<String, NameNode> hashtable) {
        super(dnsContext, hashtable);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.jndi.dns.BaseNameClassPairEnumeration, javax.naming.NamingEnumeration
    public NameClassPair next() throws NamingException {
        if (!hasMore()) {
            throw new NoSuchElementException();
        }
        NameNode nameNodeNextElement2 = this.nodes.nextElement2();
        String str = (nameNodeNextElement2.isZoneCut() || nameNodeNextElement2.getChildren() != null) ? "javax.naming.directory.DirContext" : Constants.OBJECT_CLASS;
        Name nameAdd = new CompositeName().add(new DnsName().add(nameNodeNextElement2.getLabel()).toString());
        NameClassPair nameClassPair = new NameClassPair(nameAdd.toString(), str);
        nameClassPair.setNameInNamespace(this.ctx.fullyQualify(nameAdd).toString());
        return nameClassPair;
    }
}
