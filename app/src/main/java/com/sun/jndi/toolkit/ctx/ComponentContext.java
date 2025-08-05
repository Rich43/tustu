package com.sun.jndi.toolkit.ctx;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ResolveResult;

/* loaded from: rt.jar:com/sun/jndi/toolkit/ctx/ComponentContext.class */
public abstract class ComponentContext extends PartialCompositeContext {
    private static int debug = 0;
    protected static final byte USE_CONTINUATION = 1;
    protected static final byte TERMINAL_COMPONENT = 2;
    protected static final byte TERMINAL_NNS_COMPONENT = 3;

    protected abstract Object c_lookup(Name name, Continuation continuation) throws NamingException;

    protected abstract Object c_lookupLink(Name name, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<NameClassPair> c_list(Name name, Continuation continuation) throws NamingException;

    protected abstract NamingEnumeration<Binding> c_listBindings(Name name, Continuation continuation) throws NamingException;

    protected abstract void c_bind(Name name, Object obj, Continuation continuation) throws NamingException;

    protected abstract void c_rebind(Name name, Object obj, Continuation continuation) throws NamingException;

    protected abstract void c_unbind(Name name, Continuation continuation) throws NamingException;

    protected abstract void c_destroySubcontext(Name name, Continuation continuation) throws NamingException;

    protected abstract Context c_createSubcontext(Name name, Continuation continuation) throws NamingException;

    protected abstract void c_rename(Name name, Name name2, Continuation continuation) throws NamingException;

    protected abstract NameParser c_getNameParser(Name name, Continuation continuation) throws NamingException;

    protected ComponentContext() {
        this._contextType = 2;
    }

    protected HeadTail p_parseComponent(Name name, Continuation continuation) throws NamingException {
        int i2;
        Name nameAdd;
        Name suffix;
        if (name.isEmpty() || name.get(0).equals("")) {
            i2 = 0;
        } else {
            i2 = 1;
        }
        if (name instanceof CompositeName) {
            nameAdd = name.getPrefix(i2);
            suffix = name.getSuffix(i2);
        } else {
            nameAdd = new CompositeName().add(name.toString());
            suffix = null;
        }
        if (debug > 2) {
            System.err.println("ORIG: " + ((Object) name));
            System.err.println("PREFIX: " + ((Object) name));
            System.err.println("SUFFIX: " + ((Object) null));
        }
        return new HeadTail(nameAdd, suffix);
    }

    protected Object c_resolveIntermediate_nns(Name name, Continuation continuation) throws NamingException {
        try {
            final Object objC_lookup = c_lookup(name, continuation);
            if (objC_lookup != null && getClass().isInstance(objC_lookup)) {
                continuation.setContinueNNS(objC_lookup, name, this);
                return null;
            }
            if (objC_lookup != null && !(objC_lookup instanceof Context)) {
                Reference reference = new Reference(Constants.OBJECT_CLASS, new RefAddr("nns") { // from class: com.sun.jndi.toolkit.ctx.ComponentContext.1
                    private static final long serialVersionUID = -8831204798861786362L;

                    @Override // javax.naming.RefAddr
                    public Object getContent() {
                        return objC_lookup;
                    }
                });
                CompositeName compositeName = (CompositeName) name.clone();
                compositeName.add("");
                continuation.setContinue(reference, compositeName, this);
                return null;
            }
            return objC_lookup;
        } catch (NamingException e2) {
            e2.appendRemainingComponent("");
            throw e2;
        }
    }

    protected Object c_lookup_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected Object c_lookupLink_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected NamingEnumeration<NameClassPair> c_list_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected NamingEnumeration<Binding> c_listBindings_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected void c_bind_nns(Name name, Object obj, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
    }

    protected void c_rebind_nns(Name name, Object obj, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
    }

    protected void c_unbind_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
    }

    protected Context c_createSubcontext_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected void c_destroySubcontext_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
    }

    protected void c_rename_nns(Name name, Name name2, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
    }

    protected NameParser c_getNameParser_nns(Name name, Continuation continuation) throws NamingException {
        c_processJunction_nns(name, continuation);
        return null;
    }

    protected void c_processJunction_nns(Name name, Continuation continuation) throws NamingException {
        if (name.isEmpty()) {
            continuation.setContinue(new Reference(Constants.OBJECT_CLASS, new RefAddr("nns") { // from class: com.sun.jndi.toolkit.ctx.ComponentContext.2
                private static final long serialVersionUID = -1389472957988053402L;

                @Override // javax.naming.RefAddr
                public Object getContent() {
                    return ComponentContext.this;
                }
            }), _NNS_NAME, this);
            return;
        }
        try {
            Object objC_lookup = c_lookup(name, continuation);
            if (continuation.isContinue()) {
                continuation.appendRemainingComponent("");
            } else {
                continuation.setContinueNNS(objC_lookup, name, this);
            }
        } catch (NamingException e2) {
            e2.appendRemainingComponent("");
            throw e2;
        }
    }

    protected HeadTail p_resolveIntermediate(Name name, Continuation continuation) throws NamingException {
        int i2 = 1;
        continuation.setSuccess();
        HeadTail headTailP_parseComponent = p_parseComponent(name, continuation);
        Name tail = headTailP_parseComponent.getTail();
        Name head = headTailP_parseComponent.getHead();
        if (tail == null || tail.isEmpty()) {
            i2 = 2;
        } else if (!tail.get(0).equals("")) {
            try {
                Object objC_resolveIntermediate_nns = c_resolveIntermediate_nns(head, continuation);
                if (objC_resolveIntermediate_nns != null) {
                    continuation.setContinue(objC_resolveIntermediate_nns, head, this, tail);
                } else if (continuation.isContinue()) {
                    checkAndAdjustRemainingName(continuation.getRemainingName());
                    continuation.appendRemainingName(tail);
                }
            } catch (NamingException e2) {
                checkAndAdjustRemainingName(e2.getRemainingName());
                e2.appendRemainingName(tail);
                throw e2;
            }
        } else if (tail.size() == 1) {
            i2 = 3;
        } else if (head.isEmpty() || isAllEmpty(tail)) {
            Name suffix = tail.getSuffix(1);
            try {
                Object objC_lookup_nns = c_lookup_nns(head, continuation);
                if (objC_lookup_nns != null) {
                    continuation.setContinue(objC_lookup_nns, head, this, suffix);
                } else if (continuation.isContinue()) {
                    continuation.appendRemainingName(suffix);
                }
            } catch (NamingException e3) {
                e3.appendRemainingName(suffix);
                throw e3;
            }
        } else {
            try {
                Object objC_resolveIntermediate_nns2 = c_resolveIntermediate_nns(head, continuation);
                if (objC_resolveIntermediate_nns2 != null) {
                    continuation.setContinue(objC_resolveIntermediate_nns2, head, this, tail);
                } else if (continuation.isContinue()) {
                    checkAndAdjustRemainingName(continuation.getRemainingName());
                    continuation.appendRemainingName(tail);
                }
            } catch (NamingException e4) {
                checkAndAdjustRemainingName(e4.getRemainingName());
                e4.appendRemainingName(tail);
                throw e4;
            }
        }
        headTailP_parseComponent.setStatus(i2);
        return headTailP_parseComponent;
    }

    void checkAndAdjustRemainingName(Name name) throws InvalidNameException {
        int size;
        if (name != null && (size = name.size()) > 1 && name.get(size - 1).equals("")) {
            name.remove(size - 1);
        }
    }

    protected boolean isAllEmpty(Name name) {
        int size = name.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (!name.get(i2).equals("")) {
                return false;
            }
        }
        return true;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected ResolveResult p_resolveToClass(Name name, Class<?> cls, Continuation continuation) throws NamingException {
        if (cls.isInstance(this)) {
            continuation.setSuccess();
            return new ResolveResult(this, name);
        }
        ResolveResult resolveResult = null;
        switch (p_resolveIntermediate(name, continuation).getStatus()) {
            case 2:
                continuation.setSuccess();
                break;
            case 3:
                Object objP_lookup = p_lookup(name, continuation);
                if (!continuation.isContinue() && cls.isInstance(objP_lookup)) {
                    resolveResult = new ResolveResult(objP_lookup, _EMPTY_NAME);
                    break;
                }
                break;
        }
        return resolveResult;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected Object p_lookup(Name name, Continuation continuation) throws NamingException {
        Object objC_lookup = null;
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                objC_lookup = c_lookup(headTailP_resolveIntermediate.getHead(), continuation);
                if (objC_lookup instanceof LinkRef) {
                    continuation.setContinue(objC_lookup, headTailP_resolveIntermediate.getHead(), this);
                    objC_lookup = null;
                    break;
                }
                break;
            case 3:
                objC_lookup = c_lookup_nns(headTailP_resolveIntermediate.getHead(), continuation);
                if (objC_lookup instanceof LinkRef) {
                    continuation.setContinue(objC_lookup, headTailP_resolveIntermediate.getHead(), this);
                    objC_lookup = null;
                    break;
                }
                break;
        }
        return objC_lookup;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected NamingEnumeration<NameClassPair> p_list(Name name, Continuation continuation) throws NamingException {
        NamingEnumeration<NameClassPair> namingEnumerationC_list = null;
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                if (debug > 0) {
                    System.out.println("c_list(" + ((Object) headTailP_resolveIntermediate.getHead()) + ")");
                }
                namingEnumerationC_list = c_list(headTailP_resolveIntermediate.getHead(), continuation);
                break;
            case 3:
                if (debug > 0) {
                    System.out.println("c_list_nns(" + ((Object) headTailP_resolveIntermediate.getHead()) + ")");
                }
                namingEnumerationC_list = c_list_nns(headTailP_resolveIntermediate.getHead(), continuation);
                break;
        }
        return namingEnumerationC_list;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected NamingEnumeration<Binding> p_listBindings(Name name, Continuation continuation) throws NamingException {
        NamingEnumeration<Binding> namingEnumerationC_listBindings = null;
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                namingEnumerationC_listBindings = c_listBindings(headTailP_resolveIntermediate.getHead(), continuation);
                break;
            case 3:
                namingEnumerationC_listBindings = c_listBindings_nns(headTailP_resolveIntermediate.getHead(), continuation);
                break;
        }
        return namingEnumerationC_listBindings;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected void p_bind(Name name, Object obj, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                c_bind(headTailP_resolveIntermediate.getHead(), obj, continuation);
                break;
            case 3:
                c_bind_nns(headTailP_resolveIntermediate.getHead(), obj, continuation);
                break;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected void p_rebind(Name name, Object obj, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                c_rebind(headTailP_resolveIntermediate.getHead(), obj, continuation);
                break;
            case 3:
                c_rebind_nns(headTailP_resolveIntermediate.getHead(), obj, continuation);
                break;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected void p_unbind(Name name, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                c_unbind(headTailP_resolveIntermediate.getHead(), continuation);
                break;
            case 3:
                c_unbind_nns(headTailP_resolveIntermediate.getHead(), continuation);
                break;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected void p_destroySubcontext(Name name, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                c_destroySubcontext(headTailP_resolveIntermediate.getHead(), continuation);
                break;
            case 3:
                c_destroySubcontext_nns(headTailP_resolveIntermediate.getHead(), continuation);
                break;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected Context p_createSubcontext(Name name, Continuation continuation) throws NamingException {
        Context contextC_createSubcontext = null;
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                contextC_createSubcontext = c_createSubcontext(headTailP_resolveIntermediate.getHead(), continuation);
                break;
            case 3:
                contextC_createSubcontext = c_createSubcontext_nns(headTailP_resolveIntermediate.getHead(), continuation);
                break;
        }
        return contextC_createSubcontext;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected void p_rename(Name name, Name name2, Continuation continuation) throws NamingException {
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                c_rename(headTailP_resolveIntermediate.getHead(), name2, continuation);
                break;
            case 3:
                c_rename_nns(headTailP_resolveIntermediate.getHead(), name2, continuation);
                break;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected NameParser p_getNameParser(Name name, Continuation continuation) throws NamingException {
        NameParser nameParserC_getNameParser = null;
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                nameParserC_getNameParser = c_getNameParser(headTailP_resolveIntermediate.getHead(), continuation);
                break;
            case 3:
                nameParserC_getNameParser = c_getNameParser_nns(headTailP_resolveIntermediate.getHead(), continuation);
                break;
        }
        return nameParserC_getNameParser;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected Object p_lookupLink(Name name, Continuation continuation) throws NamingException {
        Object objC_lookupLink = null;
        HeadTail headTailP_resolveIntermediate = p_resolveIntermediate(name, continuation);
        switch (headTailP_resolveIntermediate.getStatus()) {
            case 2:
                objC_lookupLink = c_lookupLink(headTailP_resolveIntermediate.getHead(), continuation);
                break;
            case 3:
                objC_lookupLink = c_lookupLink_nns(headTailP_resolveIntermediate.getHead(), continuation);
                break;
        }
        return objC_lookupLink;
    }
}
