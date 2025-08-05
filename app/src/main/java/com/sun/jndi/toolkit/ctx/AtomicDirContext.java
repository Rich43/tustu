package com.sun.jndi.toolkit.ctx;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/* loaded from: rt.jar:com/sun/jndi/toolkit/ctx/AtomicDirContext.class */
public abstract class AtomicDirContext extends ComponentDirContext {
    protected abstract Attributes a_getAttributes(String str, String[] strArr, Continuation continuation) throws NamingException;

    protected abstract void a_modifyAttributes(String str, int i2, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract void a_modifyAttributes(String str, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException;

    protected abstract void a_bind(String str, Object obj, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract void a_rebind(String str, Object obj, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract DirContext a_createSubcontext(String str, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<SearchResult> a_search(Attributes attributes, String[] strArr, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<SearchResult> a_search(String str, String str2, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<SearchResult> a_search(String str, String str2, SearchControls searchControls, Continuation continuation) throws NamingException;

    protected abstract DirContext a_getSchema(Continuation continuation) throws NamingException;

    protected abstract DirContext a_getSchemaClassDefinition(Continuation continuation) throws NamingException;

    protected AtomicDirContext() {
        this._contextType = 3;
    }

    protected Attributes a_getAttributes_nns(String str, String[] strArr, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
        return null;
    }

    protected void a_modifyAttributes_nns(String str, int i2, Attributes attributes, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
    }

    protected void a_modifyAttributes_nns(String str, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
    }

    protected void a_bind_nns(String str, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
    }

    protected void a_rebind_nns(String str, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
    }

    protected DirContext a_createSubcontext_nns(String str, Attributes attributes, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
        return null;
    }

    protected NamingEnumeration<SearchResult> a_search_nns(Attributes attributes, String[] strArr, Continuation continuation) throws NamingException {
        a_processJunction_nns(continuation);
        return null;
    }

    protected NamingEnumeration<SearchResult> a_search_nns(String str, String str2, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
        return null;
    }

    protected NamingEnumeration<SearchResult> a_search_nns(String str, String str2, SearchControls searchControls, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
        return null;
    }

    protected DirContext a_getSchema_nns(Continuation continuation) throws NamingException {
        a_processJunction_nns(continuation);
        return null;
    }

    protected DirContext a_getSchemaDefinition_nns(Continuation continuation) throws NamingException {
        a_processJunction_nns(continuation);
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected Attributes c_getAttributes(Name name, String[] strArr, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            return a_getAttributes(name.toString(), strArr, continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_modifyAttributes(Name name, int i2, Attributes attributes, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            a_modifyAttributes(name.toString(), i2, attributes, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_modifyAttributes(Name name, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            a_modifyAttributes(name.toString(), modificationItemArr, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_bind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            a_bind(name.toString(), obj, attributes, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_rebind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            a_rebind(name.toString(), obj, attributes, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected DirContext c_createSubcontext(Name name, Attributes attributes, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            return a_createSubcontext(name.toString(), attributes, continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected NamingEnumeration<SearchResult> c_search(Name name, Attributes attributes, String[] strArr, Continuation continuation) throws NamingException {
        if (resolve_to_context(name, continuation)) {
            return a_search(attributes, strArr, continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected NamingEnumeration<SearchResult> c_search(Name name, String str, SearchControls searchControls, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            return a_search(name.toString(), str, searchControls, continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected NamingEnumeration<SearchResult> c_search(Name name, String str, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            return a_search(name.toString(), str, objArr, searchControls, continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected DirContext c_getSchema(Name name, Continuation continuation) throws NamingException {
        if (resolve_to_context(name, continuation)) {
            return a_getSchema(continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected DirContext c_getSchemaClassDefinition(Name name, Continuation continuation) throws NamingException {
        if (resolve_to_context(name, continuation)) {
            return a_getSchemaClassDefinition(continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected Attributes c_getAttributes_nns(Name name, String[] strArr, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context_nns(name, continuation)) {
            return a_getAttributes_nns(name.toString(), strArr, continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_modifyAttributes_nns(Name name, int i2, Attributes attributes, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context_nns(name, continuation)) {
            a_modifyAttributes_nns(name.toString(), i2, attributes, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_modifyAttributes_nns(Name name, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context_nns(name, continuation)) {
            a_modifyAttributes_nns(name.toString(), modificationItemArr, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_bind_nns(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context_nns(name, continuation)) {
            a_bind_nns(name.toString(), obj, attributes, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_rebind_nns(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context_nns(name, continuation)) {
            a_rebind_nns(name.toString(), obj, attributes, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected DirContext c_createSubcontext_nns(Name name, Attributes attributes, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context_nns(name, continuation)) {
            return a_createSubcontext_nns(name.toString(), attributes, continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected NamingEnumeration<SearchResult> c_search_nns(Name name, Attributes attributes, String[] strArr, Continuation continuation) throws NamingException {
        resolve_to_nns_and_continue(name, continuation);
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected NamingEnumeration<SearchResult> c_search_nns(Name name, String str, SearchControls searchControls, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context_nns(name, continuation)) {
            return a_search_nns(name.toString(), str, searchControls, continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected NamingEnumeration<SearchResult> c_search_nns(Name name, String str, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context_nns(name, continuation)) {
            return a_search_nns(name.toString(), str, objArr, searchControls, continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected DirContext c_getSchema_nns(Name name, Continuation continuation) throws NamingException {
        resolve_to_nns_and_continue(name, continuation);
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected DirContext c_getSchemaClassDefinition_nns(Name name, Continuation continuation) throws NamingException {
        resolve_to_nns_and_continue(name, continuation);
        return null;
    }
}
