package javax.naming;

import com.sun.naming.internal.ResourceManager;
import java.util.Hashtable;
import javax.naming.spi.NamingManager;

/* loaded from: rt.jar:javax/naming/InitialContext.class */
public class InitialContext implements Context {
    protected Hashtable<Object, Object> myProps = null;
    protected Context defaultInitCtx = null;
    protected boolean gotDefault = false;

    protected InitialContext(boolean z2) throws NamingException {
        if (!z2) {
            init(null);
        }
    }

    public InitialContext() throws NamingException {
        init(null);
    }

    public InitialContext(Hashtable<?, ?> hashtable) throws NamingException {
        init(hashtable != null ? (Hashtable) hashtable.clone() : hashtable);
    }

    protected void init(Hashtable<?, ?> hashtable) throws NamingException {
        this.myProps = ResourceManager.getInitialEnvironment(hashtable);
        if (this.myProps.get(Context.INITIAL_CONTEXT_FACTORY) != null) {
            getDefaultInitCtx();
        }
    }

    public static <T> T doLookup(Name name) throws NamingException {
        return (T) new InitialContext().lookup(name);
    }

    public static <T> T doLookup(String str) throws NamingException {
        return (T) new InitialContext().lookup(str);
    }

    private static String getURLScheme(String str) {
        int iIndexOf = str.indexOf(58);
        int iIndexOf2 = str.indexOf(47);
        if (iIndexOf <= 0) {
            return null;
        }
        if (iIndexOf2 == -1 || iIndexOf < iIndexOf2) {
            return str.substring(0, iIndexOf);
        }
        return null;
    }

    protected Context getDefaultInitCtx() throws NamingException {
        if (!this.gotDefault) {
            this.defaultInitCtx = NamingManager.getInitialContext(this.myProps);
            this.gotDefault = true;
        }
        if (this.defaultInitCtx == null) {
            throw new NoInitialContextException();
        }
        return this.defaultInitCtx;
    }

    protected Context getURLOrDefaultInitCtx(String str) throws NamingException {
        Context uRLContext;
        if (NamingManager.hasInitialContextFactoryBuilder()) {
            return getDefaultInitCtx();
        }
        String uRLScheme = getURLScheme(str);
        if (uRLScheme != null && (uRLContext = NamingManager.getURLContext(uRLScheme, this.myProps)) != null) {
            return uRLContext;
        }
        return getDefaultInitCtx();
    }

    protected Context getURLOrDefaultInitCtx(Name name) throws NamingException {
        String uRLScheme;
        Context uRLContext;
        if (NamingManager.hasInitialContextFactoryBuilder()) {
            return getDefaultInitCtx();
        }
        if (name.size() > 0 && (uRLScheme = getURLScheme(name.get(0))) != null && (uRLContext = NamingManager.getURLContext(uRLScheme, this.myProps)) != null) {
            return uRLContext;
        }
        return getDefaultInitCtx();
    }

    @Override // javax.naming.Context
    public Object lookup(String str) throws NamingException {
        return getURLOrDefaultInitCtx(str).lookup(str);
    }

    @Override // javax.naming.Context
    public Object lookup(Name name) throws NamingException {
        return getURLOrDefaultInitCtx(name).lookup(name);
    }

    @Override // javax.naming.Context
    public void bind(String str, Object obj) throws NamingException {
        getURLOrDefaultInitCtx(str).bind(str, obj);
    }

    @Override // javax.naming.Context
    public void bind(Name name, Object obj) throws NamingException {
        getURLOrDefaultInitCtx(name).bind(name, obj);
    }

    @Override // javax.naming.Context
    public void rebind(String str, Object obj) throws NamingException {
        getURLOrDefaultInitCtx(str).rebind(str, obj);
    }

    @Override // javax.naming.Context
    public void rebind(Name name, Object obj) throws NamingException {
        getURLOrDefaultInitCtx(name).rebind(name, obj);
    }

    @Override // javax.naming.Context
    public void unbind(String str) throws NamingException {
        getURLOrDefaultInitCtx(str).unbind(str);
    }

    @Override // javax.naming.Context
    public void unbind(Name name) throws NamingException {
        getURLOrDefaultInitCtx(name).unbind(name);
    }

    @Override // javax.naming.Context
    public void rename(String str, String str2) throws NamingException {
        getURLOrDefaultInitCtx(str).rename(str, str2);
    }

    @Override // javax.naming.Context
    public void rename(Name name, Name name2) throws NamingException {
        getURLOrDefaultInitCtx(name).rename(name, name2);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(String str) throws NamingException {
        return getURLOrDefaultInitCtx(str).list(str);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return getURLOrDefaultInitCtx(name).list(name);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(String str) throws NamingException {
        return getURLOrDefaultInitCtx(str).listBindings(str);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return getURLOrDefaultInitCtx(name).listBindings(name);
    }

    @Override // javax.naming.Context
    public void destroySubcontext(String str) throws NamingException {
        getURLOrDefaultInitCtx(str).destroySubcontext(str);
    }

    @Override // javax.naming.Context
    public void destroySubcontext(Name name) throws NamingException {
        getURLOrDefaultInitCtx(name).destroySubcontext(name);
    }

    @Override // javax.naming.Context
    public Context createSubcontext(String str) throws NamingException {
        return getURLOrDefaultInitCtx(str).createSubcontext(str);
    }

    @Override // javax.naming.Context
    public Context createSubcontext(Name name) throws NamingException {
        return getURLOrDefaultInitCtx(name).createSubcontext(name);
    }

    @Override // javax.naming.Context
    public Object lookupLink(String str) throws NamingException {
        return getURLOrDefaultInitCtx(str).lookupLink(str);
    }

    @Override // javax.naming.Context
    public Object lookupLink(Name name) throws NamingException {
        return getURLOrDefaultInitCtx(name).lookupLink(name);
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(String str) throws NamingException {
        return getURLOrDefaultInitCtx(str).getNameParser(str);
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(Name name) throws NamingException {
        return getURLOrDefaultInitCtx(name).getNameParser(name);
    }

    @Override // javax.naming.Context
    public String composeName(String str, String str2) throws NamingException {
        return str;
    }

    @Override // javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        return (Name) name.clone();
    }

    @Override // javax.naming.Context
    public Object addToEnvironment(String str, Object obj) throws NamingException {
        this.myProps.put(str, obj);
        return getDefaultInitCtx().addToEnvironment(str, obj);
    }

    @Override // javax.naming.Context
    public Object removeFromEnvironment(String str) throws NamingException {
        this.myProps.remove(str);
        return getDefaultInitCtx().removeFromEnvironment(str);
    }

    @Override // javax.naming.Context
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return getDefaultInitCtx().getEnvironment();
    }

    @Override // javax.naming.Context
    public void close() throws NamingException {
        this.myProps = null;
        if (this.defaultInitCtx != null) {
            this.defaultInitCtx.close();
            this.defaultInitCtx = null;
        }
        this.gotDefault = false;
    }

    @Override // javax.naming.Context
    public String getNameInNamespace() throws NamingException {
        return getDefaultInitCtx().getNameInNamespace();
    }
}
