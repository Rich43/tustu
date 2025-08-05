package com.sun.jndi.toolkit.ctx;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Binding;
import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.naming.spi.ResolveResult;
import javax.naming.spi.Resolver;

/* loaded from: rt.jar:com/sun/jndi/toolkit/ctx/PartialCompositeContext.class */
public abstract class PartialCompositeContext implements Context, Resolver {
    protected static final int _PARTIAL = 1;
    protected static final int _COMPONENT = 2;
    protected static final int _ATOMIC = 3;
    protected int _contextType = 1;
    static final CompositeName _EMPTY_NAME = new CompositeName();
    static CompositeName _NNS_NAME;

    protected abstract ResolveResult p_resolveToClass(Name name, Class<?> cls, Continuation continuation) throws NamingException;

    protected abstract Object p_lookup(Name name, Continuation continuation) throws NamingException;

    protected abstract Object p_lookupLink(Name name, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<NameClassPair> p_list(Name name, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<Binding> p_listBindings(Name name, Continuation continuation) throws NamingException;

    protected abstract void p_bind(Name name, Object obj, Continuation continuation) throws NamingException;

    protected abstract void p_rebind(Name name, Object obj, Continuation continuation) throws NamingException;

    protected abstract void p_unbind(Name name, Continuation continuation) throws NamingException;

    protected abstract void p_destroySubcontext(Name name, Continuation continuation) throws NamingException;

    protected abstract Context p_createSubcontext(Name name, Continuation continuation) throws NamingException;

    protected abstract void p_rename(Name name, Name name2, Continuation continuation) throws NamingException;

    protected abstract NameParser p_getNameParser(Name name, Continuation continuation) throws NamingException;

    static {
        try {
            _NNS_NAME = new CompositeName("/");
        } catch (InvalidNameException e2) {
        }
    }

    protected PartialCompositeContext() {
    }

    protected Hashtable<?, ?> p_getEnvironment() throws NamingException {
        return getEnvironment();
    }

    @Override // javax.naming.spi.Resolver
    public ResolveResult resolveToClass(String str, Class<? extends Context> cls) throws NamingException {
        return resolveToClass(new CompositeName(str), cls);
    }

    @Override // javax.naming.spi.Resolver
    public ResolveResult resolveToClass(Name name, Class<? extends Context> cls) throws NamingException {
        ResolveResult resolveResultResolveToClass;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            resolveResultResolveToClass = p_resolveToClass(name, cls, continuation);
            while (continuation.isContinue()) {
                resolveResultResolveToClass = getPCContext(continuation).p_resolveToClass(continuation.getRemainingName(), cls, continuation);
            }
        } catch (CannotProceedException e2) {
            Context continuationContext = NamingManager.getContinuationContext(e2);
            if (!(continuationContext instanceof Resolver)) {
                throw e2;
            }
            resolveResultResolveToClass = ((Resolver) continuationContext).resolveToClass(e2.getRemainingName(), cls);
        }
        return resolveResultResolveToClass;
    }

    @Override // javax.naming.Context
    public Object lookup(String str) throws NamingException {
        return lookup(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public Object lookup(Name name) throws NamingException {
        Object objLookup;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            objLookup = p_lookup(name, continuation);
            while (continuation.isContinue()) {
                objLookup = getPCContext(continuation).p_lookup(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            objLookup = NamingManager.getContinuationContext(e2).lookup(e2.getRemainingName());
        }
        return objLookup;
    }

    @Override // javax.naming.Context
    public void bind(String str, Object obj) throws NamingException {
        bind(new CompositeName(str), obj);
    }

    @Override // javax.naming.Context
    public void bind(Name name, Object obj) throws NamingException {
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            p_bind(name, obj, continuation);
            while (continuation.isContinue()) {
                getPCContext(continuation).p_bind(continuation.getRemainingName(), obj, continuation);
            }
        } catch (CannotProceedException e2) {
            NamingManager.getContinuationContext(e2).bind(e2.getRemainingName(), obj);
        }
    }

    @Override // javax.naming.Context
    public void rebind(String str, Object obj) throws NamingException {
        rebind(new CompositeName(str), obj);
    }

    @Override // javax.naming.Context
    public void rebind(Name name, Object obj) throws NamingException {
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            p_rebind(name, obj, continuation);
            while (continuation.isContinue()) {
                getPCContext(continuation).p_rebind(continuation.getRemainingName(), obj, continuation);
            }
        } catch (CannotProceedException e2) {
            NamingManager.getContinuationContext(e2).rebind(e2.getRemainingName(), obj);
        }
    }

    @Override // javax.naming.Context
    public void unbind(String str) throws NamingException {
        unbind(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public void unbind(Name name) throws NamingException {
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            p_unbind(name, continuation);
            while (continuation.isContinue()) {
                getPCContext(continuation).p_unbind(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            NamingManager.getContinuationContext(e2).unbind(e2.getRemainingName());
        }
    }

    @Override // javax.naming.Context
    public void rename(String str, String str2) throws NamingException {
        rename(new CompositeName(str), new CompositeName(str2));
    }

    @Override // javax.naming.Context
    public void rename(Name name, Name name2) throws NamingException {
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            p_rename(name, name2, continuation);
            while (continuation.isContinue()) {
                getPCContext(continuation).p_rename(continuation.getRemainingName(), name2, continuation);
            }
        } catch (CannotProceedException e2) {
            Context continuationContext = NamingManager.getContinuationContext(e2);
            if (e2.getRemainingNewName() != null) {
                name2 = e2.getRemainingNewName();
            }
            continuationContext.rename(e2.getRemainingName(), name2);
        }
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(String str) throws NamingException {
        return list(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        NamingEnumeration<NameClassPair> list;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            list = p_list(name, continuation);
            while (continuation.isContinue()) {
                list = getPCContext(continuation).p_list(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            list = NamingManager.getContinuationContext(e2).list(e2.getRemainingName());
        }
        return list;
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(String str) throws NamingException {
        return listBindings(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        NamingEnumeration<Binding> namingEnumerationListBindings;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            namingEnumerationListBindings = p_listBindings(name, continuation);
            while (continuation.isContinue()) {
                namingEnumerationListBindings = getPCContext(continuation).p_listBindings(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            namingEnumerationListBindings = NamingManager.getContinuationContext(e2).listBindings(e2.getRemainingName());
        }
        return namingEnumerationListBindings;
    }

    @Override // javax.naming.Context
    public void destroySubcontext(String str) throws NamingException {
        destroySubcontext(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public void destroySubcontext(Name name) throws NamingException {
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            p_destroySubcontext(name, continuation);
            while (continuation.isContinue()) {
                getPCContext(continuation).p_destroySubcontext(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            NamingManager.getContinuationContext(e2).destroySubcontext(e2.getRemainingName());
        }
    }

    @Override // javax.naming.Context
    public Context createSubcontext(String str) throws NamingException {
        return createSubcontext(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public Context createSubcontext(Name name) throws NamingException {
        Context contextCreateSubcontext;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            contextCreateSubcontext = p_createSubcontext(name, continuation);
            while (continuation.isContinue()) {
                contextCreateSubcontext = getPCContext(continuation).p_createSubcontext(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            contextCreateSubcontext = NamingManager.getContinuationContext(e2).createSubcontext(e2.getRemainingName());
        }
        return contextCreateSubcontext;
    }

    @Override // javax.naming.Context
    public Object lookupLink(String str) throws NamingException {
        return lookupLink(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public Object lookupLink(Name name) throws NamingException {
        Object objLookupLink;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            objLookupLink = p_lookupLink(name, continuation);
            while (continuation.isContinue()) {
                objLookupLink = getPCContext(continuation).p_lookupLink(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            objLookupLink = NamingManager.getContinuationContext(e2).lookupLink(e2.getRemainingName());
        }
        return objLookupLink;
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(String str) throws NamingException {
        return getNameParser(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(Name name) throws NamingException {
        NameParser nameParser;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            nameParser = p_getNameParser(name, continuation);
            while (continuation.isContinue()) {
                nameParser = getPCContext(continuation).p_getNameParser(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            nameParser = NamingManager.getContinuationContext(e2).getNameParser(e2.getRemainingName());
        }
        return nameParser;
    }

    @Override // javax.naming.Context
    public String composeName(String str, String str2) throws NamingException {
        return composeName(new CompositeName(str), new CompositeName(str2)).toString();
    }

    @Override // javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        Name name3 = (Name) name2.clone();
        if (name == null) {
            return name3;
        }
        name3.addAll(name);
        String str = (String) p_getEnvironment().get("java.naming.provider.compose.elideEmpty");
        if (str == null || !str.equalsIgnoreCase("true")) {
            return name3;
        }
        int size = name2.size();
        if (!allEmpty(name2) && !allEmpty(name)) {
            if (name3.get(size - 1).equals("")) {
                name3.remove(size - 1);
            } else if (name3.get(size).equals("")) {
                name3.remove(size);
            }
        }
        return name3;
    }

    protected static boolean allEmpty(Name name) {
        Enumeration<String> all = name.getAll();
        while (all.hasMoreElements()) {
            if (!all.nextElement2().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    protected static PartialCompositeContext getPCContext(Continuation continuation) throws NamingException {
        Object resolvedObj = continuation.getResolvedObj();
        if (resolvedObj instanceof PartialCompositeContext) {
            return (PartialCompositeContext) resolvedObj;
        }
        throw continuation.fillInException(new CannotProceedException());
    }
}
