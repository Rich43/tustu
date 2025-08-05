package com.sun.jndi.toolkit.url;

import java.net.MalformedURLException;
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
import javax.naming.OperationNotSupportedException;
import javax.naming.spi.NamingManager;
import javax.naming.spi.ResolveResult;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/jndi/toolkit/url/GenericURLContext.class */
public abstract class GenericURLContext implements Context {
    protected Hashtable<String, Object> myEnv;

    protected abstract ResolveResult getRootURLContext(String str, Hashtable<?, ?> hashtable) throws NamingException;

    public GenericURLContext(Hashtable<?, ?> hashtable) {
        this.myEnv = null;
        this.myEnv = (Hashtable) (hashtable == null ? null : hashtable.clone());
    }

    @Override // javax.naming.Context
    public void close() throws NamingException {
        this.myEnv = null;
    }

    @Override // javax.naming.Context
    public String getNameInNamespace() throws NamingException {
        return "";
    }

    protected Name getURLSuffix(String str, String str2) throws NamingException {
        String strSubstring = str2.substring(str.length());
        if (strSubstring.length() == 0) {
            return new CompositeName();
        }
        if (strSubstring.charAt(0) == '/') {
            strSubstring = strSubstring.substring(1);
        }
        try {
            return new CompositeName().add(UrlUtil.decode(strSubstring));
        } catch (MalformedURLException e2) {
            throw new InvalidNameException(e2.getMessage());
        }
    }

    protected String getURLPrefix(String str) throws NamingException {
        int length;
        int iIndexOf = str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        if (iIndexOf < 0) {
            throw new OperationNotSupportedException("Invalid URL: " + str);
        }
        int length2 = iIndexOf + 1;
        if (str.startsWith("//", length2)) {
            int i2 = length2 + 2;
            int iIndexOf2 = str.indexOf(47, i2);
            int iIndexOf3 = str.indexOf(63, i2);
            int iIndexOf4 = str.indexOf(35, i2);
            if (iIndexOf4 > -1 && iIndexOf3 > iIndexOf4) {
                iIndexOf3 = -1;
            }
            if (iIndexOf4 > -1 && iIndexOf2 > iIndexOf4) {
                iIndexOf2 = -1;
            }
            if (iIndexOf3 > -1 && iIndexOf2 > iIndexOf3) {
                iIndexOf2 = -1;
            }
            if (iIndexOf2 > -1) {
                length = iIndexOf2;
            } else if (iIndexOf3 > -1) {
                length = iIndexOf3;
            } else {
                length = iIndexOf4 > -1 ? iIndexOf4 : str.length();
            }
            int i3 = length;
            if (i3 >= 0) {
                length2 = i3;
            } else {
                length2 = str.length();
            }
        }
        return str.substring(0, length2);
    }

    protected boolean urlEquals(String str, String str2) {
        return str.equals(str2);
    }

    protected Context getContinuationContext(Name name) throws NamingException {
        Object objLookup = lookup(name.get(0));
        CannotProceedException cannotProceedException = new CannotProceedException();
        cannotProceedException.setResolvedObj(objLookup);
        cannotProceedException.setEnvironment(this.myEnv);
        return NamingManager.getContinuationContext(cannotProceedException);
    }

    @Override // javax.naming.Context
    public Object lookup(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            Object objLookup = context.lookup(rootURLContext.getRemainingName());
            context.close();
            return objLookup;
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public Object lookup(Name name) throws NamingException {
        if (name.size() == 1) {
            return lookup(name.get(0));
        }
        Context continuationContext = getContinuationContext(name);
        try {
            Object objLookup = continuationContext.lookup(name.getSuffix(1));
            continuationContext.close();
            return objLookup;
        } catch (Throwable th) {
            continuationContext.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public void bind(String str, Object obj) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            context.bind(rootURLContext.getRemainingName(), obj);
            context.close();
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public void bind(Name name, Object obj) throws NamingException {
        if (name.size() == 1) {
            bind(name.get(0), obj);
            return;
        }
        Context continuationContext = getContinuationContext(name);
        try {
            continuationContext.bind(name.getSuffix(1), obj);
            continuationContext.close();
        } catch (Throwable th) {
            continuationContext.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public void rebind(String str, Object obj) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            context.rebind(rootURLContext.getRemainingName(), obj);
            context.close();
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public void rebind(Name name, Object obj) throws NamingException {
        if (name.size() == 1) {
            rebind(name.get(0), obj);
            return;
        }
        Context continuationContext = getContinuationContext(name);
        try {
            continuationContext.rebind(name.getSuffix(1), obj);
            continuationContext.close();
        } catch (Throwable th) {
            continuationContext.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public void unbind(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            context.unbind(rootURLContext.getRemainingName());
            context.close();
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public void unbind(Name name) throws NamingException {
        if (name.size() == 1) {
            unbind(name.get(0));
            return;
        }
        Context continuationContext = getContinuationContext(name);
        try {
            continuationContext.unbind(name.getSuffix(1));
        } finally {
            continuationContext.close();
        }
    }

    @Override // javax.naming.Context
    public void rename(String str, String str2) throws NamingException {
        String uRLPrefix = getURLPrefix(str);
        String uRLPrefix2 = getURLPrefix(str2);
        if (!urlEquals(uRLPrefix, uRLPrefix2)) {
            throw new OperationNotSupportedException("Renaming using different URL prefixes not supported : " + str + " " + str2);
        }
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            context.rename(rootURLContext.getRemainingName(), getURLSuffix(uRLPrefix2, str2));
            context.close();
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public void rename(Name name, Name name2) throws NamingException {
        if (name.size() == 1) {
            if (name2.size() != 1) {
                throw new OperationNotSupportedException("Renaming to a Name with more components not supported: " + ((Object) name2));
            }
            rename(name.get(0), name2.get(0));
        } else {
            if (!urlEquals(name.get(0), name2.get(0))) {
                throw new OperationNotSupportedException("Renaming using different URLs as first components not supported: " + ((Object) name) + " " + ((Object) name2));
            }
            Context continuationContext = getContinuationContext(name);
            try {
                continuationContext.rename(name.getSuffix(1), name2.getSuffix(1));
                continuationContext.close();
            } catch (Throwable th) {
                continuationContext.close();
                throw th;
            }
        }
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            NamingEnumeration<NameClassPair> list = context.list(rootURLContext.getRemainingName());
            context.close();
            return list;
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        if (name.size() == 1) {
            return list(name.get(0));
        }
        Context continuationContext = getContinuationContext(name);
        try {
            NamingEnumeration<NameClassPair> list = continuationContext.list(name.getSuffix(1));
            continuationContext.close();
            return list;
        } catch (Throwable th) {
            continuationContext.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            NamingEnumeration<Binding> namingEnumerationListBindings = context.listBindings(rootURLContext.getRemainingName());
            context.close();
            return namingEnumerationListBindings;
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        if (name.size() == 1) {
            return listBindings(name.get(0));
        }
        Context continuationContext = getContinuationContext(name);
        try {
            NamingEnumeration<Binding> namingEnumerationListBindings = continuationContext.listBindings(name.getSuffix(1));
            continuationContext.close();
            return namingEnumerationListBindings;
        } catch (Throwable th) {
            continuationContext.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public void destroySubcontext(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            context.destroySubcontext(rootURLContext.getRemainingName());
            context.close();
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public void destroySubcontext(Name name) throws NamingException {
        if (name.size() == 1) {
            destroySubcontext(name.get(0));
            return;
        }
        Context continuationContext = getContinuationContext(name);
        try {
            continuationContext.destroySubcontext(name.getSuffix(1));
        } finally {
            continuationContext.close();
        }
    }

    @Override // javax.naming.Context
    public Context createSubcontext(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            Context contextCreateSubcontext = context.createSubcontext(rootURLContext.getRemainingName());
            context.close();
            return contextCreateSubcontext;
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public Context createSubcontext(Name name) throws NamingException {
        if (name.size() == 1) {
            return createSubcontext(name.get(0));
        }
        Context continuationContext = getContinuationContext(name);
        try {
            Context contextCreateSubcontext = continuationContext.createSubcontext(name.getSuffix(1));
            continuationContext.close();
            return contextCreateSubcontext;
        } catch (Throwable th) {
            continuationContext.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public Object lookupLink(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            Object objLookupLink = context.lookupLink(rootURLContext.getRemainingName());
            context.close();
            return objLookupLink;
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public Object lookupLink(Name name) throws NamingException {
        if (name.size() == 1) {
            return lookupLink(name.get(0));
        }
        Context continuationContext = getContinuationContext(name);
        try {
            Object objLookupLink = continuationContext.lookupLink(name.getSuffix(1));
            continuationContext.close();
            return objLookupLink;
        } catch (Throwable th) {
            continuationContext.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        Context context = (Context) rootURLContext.getResolvedObj();
        try {
            NameParser nameParser = context.getNameParser(rootURLContext.getRemainingName());
            context.close();
            return nameParser;
        } catch (Throwable th) {
            context.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(Name name) throws NamingException {
        if (name.size() == 1) {
            return getNameParser(name.get(0));
        }
        Context continuationContext = getContinuationContext(name);
        try {
            NameParser nameParser = continuationContext.getNameParser(name.getSuffix(1));
            continuationContext.close();
            return nameParser;
        } catch (Throwable th) {
            continuationContext.close();
            throw th;
        }
    }

    @Override // javax.naming.Context
    public String composeName(String str, String str2) throws NamingException {
        if (str2.equals("")) {
            return str;
        }
        if (str.equals("")) {
            return str2;
        }
        return str2 + "/" + str;
    }

    @Override // javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        Name name3 = (Name) name2.clone();
        name3.addAll(name);
        return name3;
    }

    @Override // javax.naming.Context
    public Object removeFromEnvironment(String str) throws NamingException {
        if (this.myEnv == null) {
            return null;
        }
        return this.myEnv.remove(str);
    }

    @Override // javax.naming.Context
    public Object addToEnvironment(String str, Object obj) throws NamingException {
        if (this.myEnv == null) {
            this.myEnv = new Hashtable<>(11, 0.75f);
        }
        return this.myEnv.put(str, obj);
    }

    @Override // javax.naming.Context
    public Hashtable<String, Object> getEnvironment() throws NamingException {
        if (this.myEnv == null) {
            return new Hashtable<>(5, 0.75f);
        }
        return (Hashtable) this.myEnv.clone();
    }
}
