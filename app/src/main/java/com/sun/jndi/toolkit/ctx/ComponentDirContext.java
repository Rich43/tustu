package com.sun.jndi.toolkit.ctx;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/* loaded from: rt.jar:com/sun/jndi/toolkit/ctx/ComponentDirContext.class */
public abstract class ComponentDirContext extends PartialCompositeDirContext {
    protected abstract Attributes c_getAttributes(Name name, String[] strArr, Continuation continuation) throws NamingException;

    protected abstract void c_modifyAttributes(Name name, int i2, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract void c_modifyAttributes(Name name, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException;

    protected abstract void c_bind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract void c_rebind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract DirContext c_createSubcontext(Name name, Attributes attributes, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<SearchResult> c_search(Name name, Attributes attributes, String[] strArr, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<SearchResult> c_search(Name name, String str, SearchControls searchControls, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<SearchResult> c_search(Name name, String str, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException;

    protected abstract DirContext c_getSchema(Name name, Continuation continuation) throws NamingException;

    protected abstract DirContext c_getSchemaClassDefinition(Name name, Continuation continuation) throws NamingException;

    protected ComponentDirContext() {
        this._contextType = 2;
    }

    protected Attributes c_getAttributes_nns(Name name, String[] strArr, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected void c_modifyAttributes_nns(Name name, int i2, Attributes attributes, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
    }

    protected void c_modifyAttributes_nns(Name name, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
    }

    protected void c_bind_nns(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
    }

    protected void c_rebind_nns(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
    }

    protected DirContext c_createSubcontext_nns(Name name, Attributes attributes, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected NamingEnumeration<SearchResult> c_search_nns(Name name, Attributes attributes, String[] strArr, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected NamingEnumeration<SearchResult> c_search_nns(Name name, String str, SearchControls searchControls, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected NamingEnumeration<SearchResult> c_search_nns(Name name, String str, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected DirContext c_getSchema_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected DirContext c_getSchemaClassDefinition_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected Attributes p_getAttributes(Name name, String[] strArr, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        Attributes attributesC_getAttributes = null;
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                attributesC_getAttributes = c_getAttributes(headTailP_resolveIntermediate.getHead(), strArr, continuation);
                break;
            case 3:
                attributesC_getAttributes = c_getAttributes_nns(headTailP_resolveIntermediate.getHead(), strArr, continuation);
                break;
        }
        return attributesC_getAttributes;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected void p_modifyAttributes(Name name, int i2, Attributes attributes, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                c_modifyAttributes(headTailP_resolveIntermediate.getHead(), i2, attributes, continuation);
                break;
            case 3:
                c_modifyAttributes_nns(headTailP_resolveIntermediate.getHead(), i2, attributes, continuation);
                break;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected void p_modifyAttributes(Name name, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                c_modifyAttributes(headTailP_resolveIntermediate.getHead(), modificationItemArr, continuation);
                break;
            case 3:
                c_modifyAttributes_nns(headTailP_resolveIntermediate.getHead(), modificationItemArr, continuation);
                break;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected void p_bind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                c_bind(headTailP_resolveIntermediate.getHead(), obj, attributes, continuation);
                break;
            case 3:
                c_bind_nns(headTailP_resolveIntermediate.getHead(), obj, attributes, continuation);
                break;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected void p_rebind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                c_rebind(headTailP_resolveIntermediate.getHead(), obj, attributes, continuation);
                break;
            case 3:
                c_rebind_nns(headTailP_resolveIntermediate.getHead(), obj, attributes, continuation);
                break;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected DirContext p_createSubcontext(Name name, Attributes attributes, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        DirContext dirContextC_createSubcontext = null;
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                dirContextC_createSubcontext = c_createSubcontext(headTailP_resolveIntermediate.getHead(), attributes, continuation);
                break;
            case 3:
                dirContextC_createSubcontext = c_createSubcontext_nns(headTailP_resolveIntermediate.getHead(), attributes, continuation);
                break;
        }
        return dirContextC_createSubcontext;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected NamingEnumeration<SearchResult> p_search(Name name, Attributes attributes, String[] strArr, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        NamingEnumeration<SearchResult> namingEnumerationC_search = null;
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                namingEnumerationC_search = c_search(headTailP_resolveIntermediate.getHead(), attributes, strArr, continuation);
                break;
            case 3:
                namingEnumerationC_search = c_search_nns(headTailP_resolveIntermediate.getHead(), attributes, strArr, continuation);
                break;
        }
        return namingEnumerationC_search;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected NamingEnumeration<SearchResult> p_search(Name name, String str, SearchControls searchControls, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        NamingEnumeration<SearchResult> namingEnumerationC_search = null;
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                namingEnumerationC_search = c_search(headTailP_resolveIntermediate.getHead(), str, searchControls, continuation);
                break;
            case 3:
                namingEnumerationC_search = c_search_nns(headTailP_resolveIntermediate.getHead(), str, searchControls, continuation);
                break;
        }
        return namingEnumerationC_search;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected NamingEnumeration<SearchResult> p_search(Name name, String str, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        NamingEnumeration<SearchResult> namingEnumerationC_search = null;
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                namingEnumerationC_search = c_search(headTailP_resolveIntermediate.getHead(), str, objArr, searchControls, continuation);
                break;
            case 3:
                namingEnumerationC_search = c_search_nns(headTailP_resolveIntermediate.getHead(), str, objArr, searchControls, continuation);
                break;
        }
        return namingEnumerationC_search;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected DirContext p_getSchema(Name name, Continuation continuation) throws NamingException {
        DirContext dirContextC_getSchema = null;
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                dirContextC_getSchema = c_getSchema(headTailP_resolveIntermediate.getHead(), continuation);
                break;
            case 3:
                dirContextC_getSchema = c_getSchema_nns(headTailP_resolveIntermediate.getHead(), continuation);
                break;
        }
        return dirContextC_getSchema;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeDirContext
    protected DirContext p_getSchemaClassDefinition(Name name, Continuation continuation) throws NamingException {
        DirContext dirContextC_getSchemaClassDefinition = null;
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                dirContextC_getSchemaClassDefinition = c_getSchemaClassDefinition(headTailP_resolveIntermediate.getHead(), continuation);
                break;
            case 3:
                dirContextC_getSchemaClassDefinition = c_getSchemaClassDefinition_nns(headTailP_resolveIntermediate.getHead(), continuation);
                break;
        }
        return dirContextC_getSchemaClassDefinition;
    }
}
