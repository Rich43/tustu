package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.Binding;
import javax.naming.CannotProceedException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/spi/ContinuationContext.class */
class ContinuationContext implements Context, Resolver {
    protected CannotProceedException cpe;
    protected Hashtable<?, ?> env;
    protected Context contCtx = null;

    protected ContinuationContext(CannotProceedException cannotProceedException, Hashtable<?, ?> hashtable) {
        this.cpe = cannotProceedException;
        this.env = hashtable;
    }

    protected Context getTargetContext() throws NamingException {
        if (this.contCtx == null) {
            if (this.cpe.getResolvedObj() == null) {
                throw ((NamingException) this.cpe.fillInStackTrace());
            }
            this.contCtx = NamingManager.getContext(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
            if (this.contCtx == null) {
                throw ((NamingException) this.cpe.fillInStackTrace());
            }
        }
        return this.contCtx;
    }

    @Override // javax.naming.Context
    public Object lookup(Name name) throws NamingException {
        return getTargetContext().lookup(name);
    }

    @Override // javax.naming.Context
    public Object lookup(String str) throws NamingException {
        return getTargetContext().lookup(str);
    }

    @Override // javax.naming.Context
    public void bind(Name name, Object obj) throws NamingException {
        getTargetContext().bind(name, obj);
    }

    @Override // javax.naming.Context
    public void bind(String str, Object obj) throws NamingException {
        getTargetContext().bind(str, obj);
    }

    @Override // javax.naming.Context
    public void rebind(Name name, Object obj) throws NamingException {
        getTargetContext().rebind(name, obj);
    }

    @Override // javax.naming.Context
    public void rebind(String str, Object obj) throws NamingException {
        getTargetContext().rebind(str, obj);
    }

    @Override // javax.naming.Context
    public void unbind(Name name) throws NamingException {
        getTargetContext().unbind(name);
    }

    @Override // javax.naming.Context
    public void unbind(String str) throws NamingException {
        getTargetContext().unbind(str);
    }

    @Override // javax.naming.Context
    public void rename(Name name, Name name2) throws NamingException {
        getTargetContext().rename(name, name2);
    }

    @Override // javax.naming.Context
    public void rename(String str, String str2) throws NamingException {
        getTargetContext().rename(str, str2);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return getTargetContext().list(name);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(String str) throws NamingException {
        return getTargetContext().list(str);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return getTargetContext().listBindings(name);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(String str) throws NamingException {
        return getTargetContext().listBindings(str);
    }

    @Override // javax.naming.Context
    public void destroySubcontext(Name name) throws NamingException {
        getTargetContext().destroySubcontext(name);
    }

    @Override // javax.naming.Context
    public void destroySubcontext(String str) throws NamingException {
        getTargetContext().destroySubcontext(str);
    }

    @Override // javax.naming.Context
    public Context createSubcontext(Name name) throws NamingException {
        return getTargetContext().createSubcontext(name);
    }

    @Override // javax.naming.Context
    public Context createSubcontext(String str) throws NamingException {
        return getTargetContext().createSubcontext(str);
    }

    @Override // javax.naming.Context
    public Object lookupLink(Name name) throws NamingException {
        return getTargetContext().lookupLink(name);
    }

    @Override // javax.naming.Context
    public Object lookupLink(String str) throws NamingException {
        return getTargetContext().lookupLink(str);
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(Name name) throws NamingException {
        return getTargetContext().getNameParser(name);
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(String str) throws NamingException {
        return getTargetContext().getNameParser(str);
    }

    @Override // javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        return getTargetContext().composeName(name, name2);
    }

    @Override // javax.naming.Context
    public String composeName(String str, String str2) throws NamingException {
        return getTargetContext().composeName(str, str2);
    }

    @Override // javax.naming.Context
    public Object addToEnvironment(String str, Object obj) throws NamingException {
        return getTargetContext().addToEnvironment(str, obj);
    }

    @Override // javax.naming.Context
    public Object removeFromEnvironment(String str) throws NamingException {
        return getTargetContext().removeFromEnvironment(str);
    }

    @Override // javax.naming.Context
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return getTargetContext().getEnvironment();
    }

    @Override // javax.naming.Context
    public String getNameInNamespace() throws NamingException {
        return getTargetContext().getNameInNamespace();
    }

    @Override // javax.naming.spi.Resolver
    public ResolveResult resolveToClass(Name name, Class<? extends Context> cls) throws NamingException {
        if (this.cpe.getResolvedObj() == null) {
            throw ((NamingException) this.cpe.fillInStackTrace());
        }
        Resolver resolver = NamingManager.getResolver(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
        if (resolver == null) {
            throw ((NamingException) this.cpe.fillInStackTrace());
        }
        return resolver.resolveToClass(name, cls);
    }

    @Override // javax.naming.spi.Resolver
    public ResolveResult resolveToClass(String str, Class<? extends Context> cls) throws NamingException {
        if (this.cpe.getResolvedObj() == null) {
            throw ((NamingException) this.cpe.fillInStackTrace());
        }
        Resolver resolver = NamingManager.getResolver(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
        if (resolver == null) {
            throw ((NamingException) this.cpe.fillInStackTrace());
        }
        return resolver.resolveToClass(str, cls);
    }

    @Override // javax.naming.Context
    public void close() throws NamingException {
        this.cpe = null;
        this.env = null;
        if (this.contCtx != null) {
            this.contCtx.close();
            this.contCtx = null;
        }
    }
}
