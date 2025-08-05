package com.sun.jndi.rmi.registry;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.NoSuchElementException;
import javax.naming.CompositeName;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* compiled from: RegistryContext.java */
/* loaded from: rt.jar:com/sun/jndi/rmi/registry/NameClassPairEnumeration.class */
class NameClassPairEnumeration implements NamingEnumeration<NameClassPair> {
    private final String[] names;
    private int nextName = 0;

    NameClassPairEnumeration(String[] strArr) {
        this.names = strArr;
    }

    @Override // javax.naming.NamingEnumeration
    public boolean hasMore() {
        return this.nextName < this.names.length;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javax.naming.NamingEnumeration
    public NameClassPair next() throws NamingException {
        if (!hasMore()) {
            throw new NoSuchElementException();
        }
        String[] strArr = this.names;
        int i2 = this.nextName;
        this.nextName = i2 + 1;
        String str = strArr[i2];
        NameClassPair nameClassPair = new NameClassPair(new CompositeName().add(str).toString(), Constants.OBJECT_CLASS);
        nameClassPair.setNameInNamespace(str);
        return nameClassPair;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return hasMore();
    }

    @Override // java.util.Enumeration
    public NameClassPair nextElement() {
        try {
            return next();
        } catch (NamingException e2) {
            throw new NoSuchElementException("javax.naming.NamingException was thrown");
        }
    }

    @Override // javax.naming.NamingEnumeration
    public void close() {
        this.nextName = this.names.length;
    }
}
