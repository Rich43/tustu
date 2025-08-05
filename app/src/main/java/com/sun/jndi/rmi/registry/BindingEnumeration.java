package com.sun.jndi.rmi.registry;

import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* compiled from: RegistryContext.java */
/* loaded from: rt.jar:com/sun/jndi/rmi/registry/BindingEnumeration.class */
class BindingEnumeration implements NamingEnumeration<Binding> {
    private RegistryContext ctx;
    private final String[] names;
    private int nextName = 0;

    BindingEnumeration(RegistryContext registryContext, String[] strArr) {
        this.ctx = new RegistryContext(registryContext);
        this.names = strArr;
    }

    protected void finalize() {
        this.ctx.close();
    }

    @Override // javax.naming.NamingEnumeration
    public boolean hasMore() {
        if (this.nextName >= this.names.length) {
            this.ctx.close();
        }
        return this.nextName < this.names.length;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javax.naming.NamingEnumeration
    public Binding next() throws NamingException {
        if (!hasMore()) {
            throw new NoSuchElementException();
        }
        String[] strArr = this.names;
        int i2 = this.nextName;
        this.nextName = i2 + 1;
        Name nameAdd = new CompositeName().add(strArr[i2]);
        Object objLookup = this.ctx.lookup(nameAdd);
        String string = nameAdd.toString();
        Binding binding = new Binding(string, objLookup);
        binding.setNameInNamespace(string);
        return binding;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return hasMore();
    }

    @Override // java.util.Enumeration
    /* renamed from: nextElement */
    public Binding nextElement2() {
        try {
            return next();
        } catch (NamingException e2) {
            throw new NoSuchElementException("javax.naming.NamingException was thrown");
        }
    }

    @Override // javax.naming.NamingEnumeration
    public void close() {
        finalize();
    }
}
