package com.sun.jndi.cosnaming;

import com.sun.jndi.toolkit.corba.CorbaUtils;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import org.omg.CosNaming.BindingIterator;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;

/* loaded from: rt.jar:com/sun/jndi/cosnaming/CNBindingEnumeration.class */
final class CNBindingEnumeration implements NamingEnumeration<Binding> {
    private static final int DEFAULT_BATCHSIZE = 100;
    private BindingListHolder _bindingList;
    private BindingIterator _bindingIter;
    private int counter;
    private int batchsize;
    private CNCtx _ctx;
    private Hashtable<?, ?> _env;
    private boolean more;
    private boolean isLookedUpCtx;

    CNBindingEnumeration(CNCtx cNCtx, boolean z2, Hashtable<?, ?> hashtable) {
        this.batchsize = 100;
        this.more = false;
        this.isLookedUpCtx = false;
        String str = hashtable != null ? (String) hashtable.get(Context.BATCHSIZE) : null;
        if (str != null) {
            try {
                this.batchsize = Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("Batch size not numeric: " + str);
            }
        }
        this._ctx = cNCtx;
        this._ctx.incEnumCount();
        this.isLookedUpCtx = z2;
        this._env = hashtable;
        this._bindingList = new BindingListHolder();
        BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
        this._ctx._nc.list(0, this._bindingList, bindingIteratorHolder);
        this._bindingIter = bindingIteratorHolder.value;
        if (this._bindingIter != null) {
            this.more = this._bindingIter.next_n(this.batchsize, this._bindingList);
        } else {
            this.more = false;
        }
        this.counter = 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javax.naming.NamingEnumeration
    public Binding next() throws NamingException {
        if (this.more && this.counter >= this._bindingList.value.length) {
            getMore();
        }
        if (this.more && this.counter < this._bindingList.value.length) {
            org.omg.CosNaming.Binding binding = this._bindingList.value[this.counter];
            this.counter++;
            return mapBinding(binding);
        }
        throw new NoSuchElementException();
    }

    @Override // javax.naming.NamingEnumeration
    public boolean hasMore() throws NamingException {
        if (this.more) {
            return this.counter < this._bindingList.value.length || getMore();
        }
        return false;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        try {
            return hasMore();
        } catch (NamingException e2) {
            return false;
        }
    }

    @Override // java.util.Enumeration
    /* renamed from: nextElement */
    public Binding nextElement2() {
        try {
            return next();
        } catch (NamingException e2) {
            throw new NoSuchElementException();
        }
    }

    @Override // javax.naming.NamingEnumeration
    public void close() throws NamingException {
        this.more = false;
        if (this._bindingIter != null) {
            this._bindingIter.destroy();
            this._bindingIter = null;
        }
        if (this._ctx != null) {
            this._ctx.decEnumCount();
            if (this.isLookedUpCtx) {
                this._ctx.close();
            }
            this._ctx = null;
        }
    }

    protected void finalize() {
        try {
            close();
        } catch (NamingException e2) {
        }
    }

    private boolean getMore() throws NamingException {
        try {
            this.more = this._bindingIter.next_n(this.batchsize, this._bindingList);
            this.counter = 0;
            return this.more;
        } catch (Exception e2) {
            this.more = false;
            NamingException namingException = new NamingException("Problem getting binding list");
            namingException.setRootCause(e2);
            throw namingException;
        }
    }

    private Binding mapBinding(org.omg.CosNaming.Binding binding) throws NamingException {
        Object objCallResolve = this._ctx.callResolve(binding.binding_name);
        Name nameCosNameToName = CNNameParser.cosNameToName(binding.binding_name);
        try {
            if (CorbaUtils.isObjectFactoryTrusted(objCallResolve)) {
                objCallResolve = NamingManager.getObjectInstance(objCallResolve, nameCosNameToName, this._ctx, this._env);
            }
            Binding binding2 = new Binding(nameCosNameToName.toString(), objCallResolve);
            binding2.setNameInNamespace(CNNameParser.cosNameToInsString(this._ctx.makeFullName(binding.binding_name)));
            return binding2;
        } catch (NamingException e2) {
            throw e2;
        } catch (Exception e3) {
            NamingException namingException = new NamingException("problem generating object using object factory");
            namingException.setRootCause(e3);
            throw namingException;
        }
    }
}
