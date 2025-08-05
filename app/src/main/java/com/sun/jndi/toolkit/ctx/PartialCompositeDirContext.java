package com.sun.jndi.toolkit.ctx;

import javax.naming.Binding;
import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.spi.DirectoryManager;

/* loaded from: rt.jar:com/sun/jndi/toolkit/ctx/PartialCompositeDirContext.class */
public abstract class PartialCompositeDirContext extends AtomicContext implements DirContext {
    protected abstract Attributes p_getAttributes(Name name, String[] strArr, Continuation continuation) throws NamingException;

    protected abstract void p_modifyAttributes(Name name, int i2, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract void p_modifyAttributes(Name name, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException;

    protected abstract void p_bind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract void p_rebind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract DirContext p_createSubcontext(Name name, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<SearchResult> p_search(Name name, Attributes attributes, String[] strArr, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<SearchResult> p_search(Name name, String str, SearchControls searchControls, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<SearchResult> p_search(Name name, String str, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException;

    protected abstract DirContext p_getSchema(Name name, Continuation continuation) throws NamingException;

    protected abstract DirContext p_getSchemaClassDefinition(Name name, Continuation continuation) throws NamingException;

    protected PartialCompositeDirContext() {
        this._contextType = 1;
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str) throws NamingException {
        return getAttributes(str, (String[]) null);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name) throws NamingException {
        return getAttributes(name, (String[]) null);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str, String[] strArr) throws NamingException {
        return getAttributes(new CompositeName(str), strArr);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name, String[] strArr) throws NamingException {
        Attributes attributes;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            attributes = p_getAttributes(name, strArr, continuation);
            while (continuation.isContinue()) {
                attributes = getPCDirContext(continuation).p_getAttributes(continuation.getRemainingName(), strArr, continuation);
            }
        } catch (CannotProceedException e2) {
            attributes = DirectoryManager.getContinuationDirContext(e2).getAttributes(e2.getRemainingName(), strArr);
        }
        return attributes;
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, int i2, Attributes attributes) throws NamingException {
        modifyAttributes(new CompositeName(str), i2, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, int i2, Attributes attributes) throws NamingException {
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            p_modifyAttributes(name, i2, attributes, continuation);
            while (continuation.isContinue()) {
                getPCDirContext(continuation).p_modifyAttributes(continuation.getRemainingName(), i2, attributes, continuation);
            }
        } catch (CannotProceedException e2) {
            DirectoryManager.getContinuationDirContext(e2).modifyAttributes(e2.getRemainingName(), i2, attributes);
        }
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, ModificationItem[] modificationItemArr) throws NamingException {
        modifyAttributes(new CompositeName(str), modificationItemArr);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, ModificationItem[] modificationItemArr) throws NamingException {
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            p_modifyAttributes(name, modificationItemArr, continuation);
            while (continuation.isContinue()) {
                getPCDirContext(continuation).p_modifyAttributes(continuation.getRemainingName(), modificationItemArr, continuation);
            }
        } catch (CannotProceedException e2) {
            DirectoryManager.getContinuationDirContext(e2).modifyAttributes(e2.getRemainingName(), modificationItemArr);
        }
    }

    @Override // javax.naming.directory.DirContext
    public void bind(String str, Object obj, Attributes attributes) throws NamingException {
        bind(new CompositeName(str), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void bind(Name name, Object obj, Attributes attributes) throws NamingException {
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            p_bind(name, obj, attributes, continuation);
            while (continuation.isContinue()) {
                getPCDirContext(continuation).p_bind(continuation.getRemainingName(), obj, attributes, continuation);
            }
        } catch (CannotProceedException e2) {
            DirectoryManager.getContinuationDirContext(e2).bind(e2.getRemainingName(), obj, attributes);
        }
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(String str, Object obj, Attributes attributes) throws NamingException {
        rebind(new CompositeName(str), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(Name name, Object obj, Attributes attributes) throws NamingException {
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            p_rebind(name, obj, attributes, continuation);
            while (continuation.isContinue()) {
                getPCDirContext(continuation).p_rebind(continuation.getRemainingName(), obj, attributes, continuation);
            }
        } catch (CannotProceedException e2) {
            DirectoryManager.getContinuationDirContext(e2).rebind(e2.getRemainingName(), obj, attributes);
        }
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(String str, Attributes attributes) throws NamingException {
        return createSubcontext(new CompositeName(str), attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(Name name, Attributes attributes) throws NamingException {
        DirContext dirContextCreateSubcontext;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            dirContextCreateSubcontext = p_createSubcontext(name, attributes, continuation);
            while (continuation.isContinue()) {
                dirContextCreateSubcontext = getPCDirContext(continuation).p_createSubcontext(continuation.getRemainingName(), attributes, continuation);
            }
        } catch (CannotProceedException e2) {
            dirContextCreateSubcontext = DirectoryManager.getContinuationDirContext(e2).createSubcontext(e2.getRemainingName(), attributes);
        }
        return dirContextCreateSubcontext;
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes) throws NamingException {
        return search(str, attributes, (String[]) null);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes) throws NamingException {
        return search(name, attributes, (String[]) null);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes, String[] strArr) throws NamingException {
        return search(new CompositeName(str), attributes, strArr);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes, String[] strArr) throws NamingException {
        NamingEnumeration<SearchResult> namingEnumerationSearch;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            namingEnumerationSearch = p_search(name, attributes, strArr, continuation);
            while (continuation.isContinue()) {
                namingEnumerationSearch = getPCDirContext(continuation).p_search(continuation.getRemainingName(), attributes, strArr, continuation);
            }
        } catch (CannotProceedException e2) {
            namingEnumerationSearch = DirectoryManager.getContinuationDirContext(e2).search(e2.getRemainingName(), attributes, strArr);
        }
        return namingEnumerationSearch;
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, SearchControls searchControls) throws NamingException {
        return search(new CompositeName(str), str2, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, SearchControls searchControls) throws NamingException {
        NamingEnumeration<SearchResult> namingEnumerationSearch;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            namingEnumerationSearch = p_search(name, str, searchControls, continuation);
            while (continuation.isContinue()) {
                namingEnumerationSearch = getPCDirContext(continuation).p_search(continuation.getRemainingName(), str, searchControls, continuation);
            }
        } catch (CannotProceedException e2) {
            namingEnumerationSearch = DirectoryManager.getContinuationDirContext(e2).search(e2.getRemainingName(), str, searchControls);
        }
        return namingEnumerationSearch;
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, Object[] objArr, SearchControls searchControls) throws NamingException {
        return search(new CompositeName(str), str2, objArr, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, Object[] objArr, SearchControls searchControls) throws NamingException {
        NamingEnumeration<SearchResult> namingEnumerationSearch;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            namingEnumerationSearch = p_search(name, str, objArr, searchControls, continuation);
            while (continuation.isContinue()) {
                namingEnumerationSearch = getPCDirContext(continuation).p_search(continuation.getRemainingName(), str, objArr, searchControls, continuation);
            }
        } catch (CannotProceedException e2) {
            namingEnumerationSearch = DirectoryManager.getContinuationDirContext(e2).search(e2.getRemainingName(), str, objArr, searchControls);
        }
        return namingEnumerationSearch;
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(String str) throws NamingException {
        return getSchema(new CompositeName(str));
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(Name name) throws NamingException {
        DirContext schema;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            schema = p_getSchema(name, continuation);
            while (continuation.isContinue()) {
                schema = getPCDirContext(continuation).p_getSchema(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            schema = DirectoryManager.getContinuationDirContext(e2).getSchema(e2.getRemainingName());
        }
        return schema;
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(String str) throws NamingException {
        return getSchemaClassDefinition(new CompositeName(str));
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        DirContext schemaClassDefinition;
        Continuation continuation = new Continuation(name, p_getEnvironment());
        try {
            schemaClassDefinition = p_getSchemaClassDefinition(name, continuation);
            while (continuation.isContinue()) {
                schemaClassDefinition = getPCDirContext(continuation).p_getSchemaClassDefinition(continuation.getRemainingName(), continuation);
            }
        } catch (CannotProceedException e2) {
            schemaClassDefinition = DirectoryManager.getContinuationDirContext(e2).getSchemaClassDefinition(e2.getRemainingName());
        }
        return schemaClassDefinition;
    }

    protected static PartialCompositeDirContext getPCDirContext(Continuation continuation) throws NamingException {
        PartialCompositeContext pCContext = PartialCompositeContext.getPCContext(continuation);
        if (!(pCContext instanceof PartialCompositeDirContext)) {
            throw continuation.fillInException(new NotContextException("Resolved object is not a DirContext."));
        }
        return (PartialCompositeDirContext) pCContext;
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected StringHeadTail c_parseComponent(String str, Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected Object a_lookup(String str, Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected Object a_lookupLink(String str, Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected NamingEnumeration<NameClassPair> a_list(Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected NamingEnumeration<Binding> a_listBindings(Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected void a_bind(String str, Object obj, Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected void a_rebind(String str, Object obj, Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected void a_unbind(String str, Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected void a_destroySubcontext(String str, Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected Context a_createSubcontext(String str, Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected void a_rename(String str, Name name, Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext
    protected NameParser a_getNameParser(Continuation continuation) throws NamingException {
        throw continuation.fillInException(new OperationNotSupportedException());
    }
}
