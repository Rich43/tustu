package com.sun.jndi.toolkit.ctx;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;

/* loaded from: rt.jar:com/sun/jndi/toolkit/ctx/AtomicContext.class */
public abstract class AtomicContext extends ComponentContext {
    private static int debug = 0;

    protected abstract Object a_lookup(String str, Continuation continuation) throws NamingException;

    protected abstract Object a_lookupLink(String str, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<NameClassPair> a_list(Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<Binding> a_listBindings(Continuation continuation) throws NamingException;

    protected abstract void a_bind(String str, Object obj, Continuation continuation) throws NamingException;

    protected abstract void a_rebind(String str, Object obj, Continuation continuation) throws NamingException;

    protected abstract void a_unbind(String str, Continuation continuation) throws NamingException;

    protected abstract void a_destroySubcontext(String str, Continuation continuation) throws NamingException;

    protected abstract Context a_createSubcontext(String str, Continuation continuation) throws NamingException;

    protected abstract void a_rename(String str, Name name, Continuation continuation) throws NamingException;

    protected abstract NameParser a_getNameParser(Continuation continuation) throws NamingException;

    protected abstract StringHeadTail c_parseComponent(String str, Continuation continuation) throws NamingException;

    protected AtomicContext() {
        this._contextType = 3;
    }

    protected Object a_resolveIntermediate_nns(String str, Continuation continuation) throws NamingException {
        try {
            final Object objA_lookup = a_lookup(str, continuation);
            if (objA_lookup != null && getClass().isInstance(objA_lookup)) {
                continuation.setContinueNNS(objA_lookup, str, this);
                return null;
            }
            if (objA_lookup != null && !(objA_lookup instanceof Context)) {
                Reference reference = new Reference(Constants.OBJECT_CLASS, new RefAddr("nns") { // from class: com.sun.jndi.toolkit.ctx.AtomicContext.1
                    private static final long serialVersionUID = -3399518522645918499L;

                    @Override // javax.naming.RefAddr
                    public Object getContent() {
                        return objA_lookup;
                    }
                });
                CompositeName compositeName = new CompositeName();
                compositeName.add(str);
                compositeName.add("");
                continuation.setContinue(reference, compositeName, this);
                return null;
            }
            return objA_lookup;
        } catch (NamingException e2) {
            e2.appendRemainingComponent("");
            throw e2;
        }
    }

    protected Object a_lookup_nns(String str, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
        return null;
    }

    protected Object a_lookupLink_nns(String str, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
        return null;
    }

    protected NamingEnumeration<NameClassPair> a_list_nns(Continuation continuation) throws NamingException {
        a_processJunction_nns(continuation);
        return null;
    }

    protected NamingEnumeration<Binding> a_listBindings_nns(Continuation continuation) throws NamingException {
        a_processJunction_nns(continuation);
        return null;
    }

    protected void a_bind_nns(String str, Object obj, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
    }

    protected void a_rebind_nns(String str, Object obj, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
    }

    protected void a_unbind_nns(String str, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
    }

    protected Context a_createSubcontext_nns(String str, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
        return null;
    }

    protected void a_destroySubcontext_nns(String str, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
    }

    protected void a_rename_nns(String str, Name name, Continuation continuation) throws NamingException {
        a_processJunction_nns(str, continuation);
    }

    protected NameParser a_getNameParser_nns(Continuation continuation) throws NamingException {
        a_processJunction_nns(continuation);
        return null;
    }

    protected boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected Object c_lookup(Name name, Continuation continuation) throws NamingException {
        Object objA_lookup = null;
        if (resolve_to_penultimate_context(name, continuation)) {
            objA_lookup = a_lookup(name.toString(), continuation);
            if (objA_lookup != null && (objA_lookup instanceof LinkRef)) {
                continuation.setContinue(objA_lookup, name, this);
                objA_lookup = null;
            }
        }
        return objA_lookup;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected Object c_lookupLink(Name name, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            return a_lookupLink(name.toString(), continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected NamingEnumeration<NameClassPair> c_list(Name name, Continuation continuation) throws NamingException {
        if (resolve_to_context(name, continuation)) {
            return a_list(continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected NamingEnumeration<Binding> c_listBindings(Name name, Continuation continuation) throws NamingException {
        if (resolve_to_context(name, continuation)) {
            return a_listBindings(continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_bind(Name name, Object obj, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            a_bind(name.toString(), obj, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_rebind(Name name, Object obj, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            a_rebind(name.toString(), obj, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_unbind(Name name, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            a_unbind(name.toString(), continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_destroySubcontext(Name name, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            a_destroySubcontext(name.toString(), continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected Context c_createSubcontext(Name name, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            return a_createSubcontext(name.toString(), continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_rename(Name name, Name name2, Continuation continuation) throws NamingException {
        if (resolve_to_penultimate_context(name, continuation)) {
            a_rename(name.toString(), name2, continuation);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected NameParser c_getNameParser(Name name, Continuation continuation) throws NamingException {
        if (resolve_to_context(name, continuation)) {
            return a_getNameParser(continuation);
        }
        return null;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected Object c_resolveIntermediate_nns(Name name, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            Object objA_resolveIntermediate_nns = null;
            if (resolve_to_penultimate_context_nns(name, continuation)) {
                objA_resolveIntermediate_nns = a_resolveIntermediate_nns(name.toString(), continuation);
                if (objA_resolveIntermediate_nns != null && (objA_resolveIntermediate_nns instanceof LinkRef)) {
                    continuation.setContinue(objA_resolveIntermediate_nns, name, this);
                    objA_resolveIntermediate_nns = null;
                }
            }
            return objA_resolveIntermediate_nns;
        }
        return super.c_resolveIntermediate_nns(name, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected Object c_lookup_nns(Name name, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            Object objA_lookup_nns = null;
            if (resolve_to_penultimate_context_nns(name, continuation)) {
                objA_lookup_nns = a_lookup_nns(name.toString(), continuation);
                if (objA_lookup_nns != null && (objA_lookup_nns instanceof LinkRef)) {
                    continuation.setContinue(objA_lookup_nns, name, this);
                    objA_lookup_nns = null;
                }
            }
            return objA_lookup_nns;
        }
        return super.c_lookup_nns(name, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected Object c_lookupLink_nns(Name name, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            resolve_to_nns_and_continue(name, continuation);
            return null;
        }
        return super.c_lookupLink_nns(name, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected NamingEnumeration<NameClassPair> c_list_nns(Name name, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            resolve_to_nns_and_continue(name, continuation);
            return null;
        }
        return super.c_list_nns(name, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected NamingEnumeration<Binding> c_listBindings_nns(Name name, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            resolve_to_nns_and_continue(name, continuation);
            return null;
        }
        return super.c_listBindings_nns(name, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_bind_nns(Name name, Object obj, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            if (resolve_to_penultimate_context_nns(name, continuation)) {
                a_bind_nns(name.toString(), obj, continuation);
                return;
            }
            return;
        }
        super.c_bind_nns(name, obj, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_rebind_nns(Name name, Object obj, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            if (resolve_to_penultimate_context_nns(name, continuation)) {
                a_rebind_nns(name.toString(), obj, continuation);
                return;
            }
            return;
        }
        super.c_rebind_nns(name, obj, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_unbind_nns(Name name, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            if (resolve_to_penultimate_context_nns(name, continuation)) {
                a_unbind_nns(name.toString(), continuation);
                return;
            }
            return;
        }
        super.c_unbind_nns(name, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected Context c_createSubcontext_nns(Name name, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            if (resolve_to_penultimate_context_nns(name, continuation)) {
                return a_createSubcontext_nns(name.toString(), continuation);
            }
            return null;
        }
        return super.c_createSubcontext_nns(name, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_destroySubcontext_nns(Name name, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            if (resolve_to_penultimate_context_nns(name, continuation)) {
                a_destroySubcontext_nns(name.toString(), continuation);
                return;
            }
            return;
        }
        super.c_destroySubcontext_nns(name, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_rename_nns(Name name, Name name2, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            if (resolve_to_penultimate_context_nns(name, continuation)) {
                a_rename_nns(name.toString(), name2, continuation);
                return;
            }
            return;
        }
        super.c_rename_nns(name, name2, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentContext
    protected NameParser c_getNameParser_nns(Name name, Continuation continuation) throws NamingException {
        if (this._contextType == 3) {
            resolve_to_nns_and_continue(name, continuation);
            return null;
        }
        return super.c_getNameParser_nns(name, continuation);
    }

    protected void a_processJunction_nns(String str, Continuation continuation) throws NamingException {
        if (str.equals("")) {
            NameNotFoundException nameNotFoundException = new NameNotFoundException();
            continuation.setErrorNNS(this, str);
            throw continuation.fillInException(nameNotFoundException);
        }
        try {
            Object objA_lookup = a_lookup(str, continuation);
            if (continuation.isContinue()) {
                continuation.appendRemainingComponent("");
            } else {
                continuation.setContinueNNS(objA_lookup, str, this);
            }
        } catch (NamingException e2) {
            e2.appendRemainingComponent("");
            throw e2;
        }
    }

    protected void a_processJunction_nns(Continuation continuation) throws NamingException {
        continuation.setContinue(new Reference(Constants.OBJECT_CLASS, new RefAddr("nns") { // from class: com.sun.jndi.toolkit.ctx.AtomicContext.2
            private static final long serialVersionUID = 3449785852664978312L;

            @Override // javax.naming.RefAddr
            public Object getContent() {
                return AtomicContext.this;
            }
        }), _NNS_NAME, this);
    }

    protected boolean resolve_to_context(Name name, Continuation continuation) throws NamingException {
        String string = name.toString();
        StringHeadTail stringHeadTailC_parseComponent = c_parseComponent(string, continuation);
        String tail = stringHeadTailC_parseComponent.getTail();
        String head = stringHeadTailC_parseComponent.getHead();
        if (debug > 0) {
            System.out.println("RESOLVE TO CONTEXT(" + string + ") = {" + head + ", " + tail + "}");
        }
        if (head == null) {
            throw continuation.fillInException(new InvalidNameException());
        }
        if (!isEmpty(head)) {
            try {
                Object objA_lookup = a_lookup(head, continuation);
                if (objA_lookup != null) {
                    continuation.setContinue(objA_lookup, head, this, tail == null ? "" : tail);
                } else if (continuation.isContinue()) {
                    continuation.appendRemainingComponent(tail);
                }
                return false;
            } catch (NamingException e2) {
                e2.appendRemainingComponent(tail);
                throw e2;
            }
        }
        continuation.setSuccess();
        return true;
    }

    protected boolean resolve_to_penultimate_context(Name name, Continuation continuation) throws NamingException {
        String string = name.toString();
        if (debug > 0) {
            System.out.println("RESOLVE TO PENULTIMATE" + string);
        }
        StringHeadTail stringHeadTailC_parseComponent = c_parseComponent(string, continuation);
        String tail = stringHeadTailC_parseComponent.getTail();
        String head = stringHeadTailC_parseComponent.getHead();
        if (head == null) {
            throw continuation.fillInException(new InvalidNameException());
        }
        if (!isEmpty(tail)) {
            try {
                Object objA_lookup = a_lookup(head, continuation);
                if (objA_lookup != null) {
                    continuation.setContinue(objA_lookup, head, this, tail);
                } else if (continuation.isContinue()) {
                    continuation.appendRemainingComponent(tail);
                }
                return false;
            } catch (NamingException e2) {
                e2.appendRemainingComponent(tail);
                throw e2;
            }
        }
        continuation.setSuccess();
        return true;
    }

    protected boolean resolve_to_penultimate_context_nns(Name name, Continuation continuation) throws NamingException {
        try {
            if (debug > 0) {
                System.out.println("RESOLVE TO PENULTIMATE NNS" + name.toString());
            }
            boolean zResolve_to_penultimate_context = resolve_to_penultimate_context(name, continuation);
            if (continuation.isContinue()) {
                continuation.appendRemainingComponent("");
            }
            return zResolve_to_penultimate_context;
        } catch (NamingException e2) {
            e2.appendRemainingComponent("");
            throw e2;
        }
    }

    protected void resolve_to_nns_and_continue(Name name, Continuation continuation) throws NamingException {
        Object objA_lookup_nns;
        if (debug > 0) {
            System.out.println("RESOLVE TO NNS AND CONTINUE" + name.toString());
        }
        if (resolve_to_penultimate_context_nns(name, continuation) && (objA_lookup_nns = a_lookup_nns(name.toString(), continuation)) != null) {
            continuation.setContinue(objA_lookup_nns, name, this);
        }
    }
}
