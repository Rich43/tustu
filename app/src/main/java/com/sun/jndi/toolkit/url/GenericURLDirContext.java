package com.sun.jndi.toolkit.url;

import java.util.Hashtable;
import javax.naming.CannotProceedException;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.spi.DirectoryManager;
import javax.naming.spi.ResolveResult;

/* loaded from: rt.jar:com/sun/jndi/toolkit/url/GenericURLDirContext.class */
public abstract class GenericURLDirContext extends GenericURLContext implements DirContext {
    protected GenericURLDirContext(Hashtable<?, ?> hashtable) {
        super(hashtable);
    }

    protected DirContext getContinuationDirContext(Name name) throws NamingException {
        Object objLookup = lookup(name.get(0));
        CannotProceedException cannotProceedException = new CannotProceedException();
        cannotProceedException.setResolvedObj(objLookup);
        cannotProceedException.setEnvironment(this.myEnv);
        return DirectoryManager.getContinuationDirContext(cannotProceedException);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            Attributes attributes = dirContext.getAttributes(rootURLContext.getRemainingName());
            dirContext.close();
            return attributes;
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name) throws NamingException {
        if (name.size() == 1) {
            return getAttributes(name.get(0));
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            Attributes attributes = continuationDirContext.getAttributes(name.getSuffix(1));
            continuationDirContext.close();
            return attributes;
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str, String[] strArr) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            Attributes attributes = dirContext.getAttributes(rootURLContext.getRemainingName(), strArr);
            dirContext.close();
            return attributes;
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name, String[] strArr) throws NamingException {
        if (name.size() == 1) {
            return getAttributes(name.get(0), strArr);
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            Attributes attributes = continuationDirContext.getAttributes(name.getSuffix(1), strArr);
            continuationDirContext.close();
            return attributes;
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, int i2, Attributes attributes) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            dirContext.modifyAttributes(rootURLContext.getRemainingName(), i2, attributes);
            dirContext.close();
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, int i2, Attributes attributes) throws NamingException {
        if (name.size() == 1) {
            modifyAttributes(name.get(0), i2, attributes);
            return;
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            continuationDirContext.modifyAttributes(name.getSuffix(1), i2, attributes);
            continuationDirContext.close();
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, ModificationItem[] modificationItemArr) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            dirContext.modifyAttributes(rootURLContext.getRemainingName(), modificationItemArr);
            dirContext.close();
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, ModificationItem[] modificationItemArr) throws NamingException {
        if (name.size() == 1) {
            modifyAttributes(name.get(0), modificationItemArr);
            return;
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            continuationDirContext.modifyAttributes(name.getSuffix(1), modificationItemArr);
            continuationDirContext.close();
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public void bind(String str, Object obj, Attributes attributes) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            dirContext.bind(rootURLContext.getRemainingName(), obj, attributes);
            dirContext.close();
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public void bind(Name name, Object obj, Attributes attributes) throws NamingException {
        if (name.size() == 1) {
            bind(name.get(0), obj, attributes);
            return;
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            continuationDirContext.bind(name.getSuffix(1), obj, attributes);
            continuationDirContext.close();
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(String str, Object obj, Attributes attributes) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            dirContext.rebind(rootURLContext.getRemainingName(), obj, attributes);
            dirContext.close();
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(Name name, Object obj, Attributes attributes) throws NamingException {
        if (name.size() == 1) {
            rebind(name.get(0), obj, attributes);
            return;
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            continuationDirContext.rebind(name.getSuffix(1), obj, attributes);
            continuationDirContext.close();
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(String str, Attributes attributes) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            DirContext dirContextCreateSubcontext = dirContext.createSubcontext(rootURLContext.getRemainingName(), attributes);
            dirContext.close();
            return dirContextCreateSubcontext;
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(Name name, Attributes attributes) throws NamingException {
        if (name.size() == 1) {
            return createSubcontext(name.get(0), attributes);
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            DirContext dirContextCreateSubcontext = continuationDirContext.createSubcontext(name.getSuffix(1), attributes);
            continuationDirContext.close();
            return dirContextCreateSubcontext;
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        return ((DirContext) rootURLContext.getResolvedObj()).getSchema(rootURLContext.getRemainingName());
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(Name name) throws NamingException {
        if (name.size() == 1) {
            return getSchema(name.get(0));
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            DirContext schema = continuationDirContext.getSchema(name.getSuffix(1));
            continuationDirContext.close();
            return schema;
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(String str) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            DirContext schemaClassDefinition = dirContext.getSchemaClassDefinition(rootURLContext.getRemainingName());
            dirContext.close();
            return schemaClassDefinition;
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        if (name.size() == 1) {
            return getSchemaClassDefinition(name.get(0));
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            DirContext schemaClassDefinition = continuationDirContext.getSchemaClassDefinition(name.getSuffix(1));
            continuationDirContext.close();
            return schemaClassDefinition;
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = dirContext.search(rootURLContext.getRemainingName(), attributes);
            dirContext.close();
            return namingEnumerationSearch;
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes) throws NamingException {
        if (name.size() == 1) {
            return search(name.get(0), attributes);
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = continuationDirContext.search(name.getSuffix(1), attributes);
            continuationDirContext.close();
            return namingEnumerationSearch;
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes, String[] strArr) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = dirContext.search(rootURLContext.getRemainingName(), attributes, strArr);
            dirContext.close();
            return namingEnumerationSearch;
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes, String[] strArr) throws NamingException {
        if (name.size() == 1) {
            return search(name.get(0), attributes, strArr);
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = continuationDirContext.search(name.getSuffix(1), attributes, strArr);
            continuationDirContext.close();
            return namingEnumerationSearch;
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, SearchControls searchControls) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = dirContext.search(rootURLContext.getRemainingName(), str2, searchControls);
            dirContext.close();
            return namingEnumerationSearch;
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, SearchControls searchControls) throws NamingException {
        if (name.size() == 1) {
            return search(name.get(0), str, searchControls);
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = continuationDirContext.search(name.getSuffix(1), str, searchControls);
            continuationDirContext.close();
            return namingEnumerationSearch;
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, Object[] objArr, SearchControls searchControls) throws NamingException {
        ResolveResult rootURLContext = getRootURLContext(str, this.myEnv);
        DirContext dirContext = (DirContext) rootURLContext.getResolvedObj();
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = dirContext.search(rootURLContext.getRemainingName(), str2, objArr, searchControls);
            dirContext.close();
            return namingEnumerationSearch;
        } catch (Throwable th) {
            dirContext.close();
            throw th;
        }
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, Object[] objArr, SearchControls searchControls) throws NamingException {
        if (name.size() == 1) {
            return search(name.get(0), str, objArr, searchControls);
        }
        DirContext continuationDirContext = getContinuationDirContext(name);
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearch = continuationDirContext.search(name.getSuffix(1), str, objArr, searchControls);
            continuationDirContext.close();
            return namingEnumerationSearch;
        } catch (Throwable th) {
            continuationDirContext.close();
            throw th;
        }
    }
}
