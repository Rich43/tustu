package com.sun.jndi.toolkit.dir;

import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/toolkit/dir/ContextEnumerator.class */
public class ContextEnumerator implements NamingEnumeration<Binding> {
    private static boolean debug = false;
    private NamingEnumeration<Binding> children;
    private Binding currentChild;
    private boolean currentReturned;
    private Context root;
    private ContextEnumerator currentChildEnum;
    private boolean currentChildExpanded;
    private boolean rootProcessed;
    private int scope;
    private String contextName;

    public ContextEnumerator(Context context) throws NamingException {
        this(context, 2);
    }

    public ContextEnumerator(Context context, int i2) throws NamingException {
        this(context, i2, "", i2 != 1);
    }

    protected ContextEnumerator(Context context, int i2, String str, boolean z2) throws NamingException {
        this.children = null;
        this.currentChild = null;
        this.currentReturned = false;
        this.currentChildEnum = null;
        this.currentChildExpanded = false;
        this.rootProcessed = false;
        this.scope = 2;
        this.contextName = "";
        if (context == null) {
            throw new IllegalArgumentException("null context passed");
        }
        this.root = context;
        if (i2 != 0) {
            this.children = getImmediateChildren(context);
        }
        this.scope = i2;
        this.contextName = str;
        this.rootProcessed = !z2;
        prepNextChild();
    }

    protected NamingEnumeration<Binding> getImmediateChildren(Context context) throws NamingException {
        return context.listBindings("");
    }

    protected ContextEnumerator newEnumerator(Context context, int i2, String str, boolean z2) throws NamingException {
        return new ContextEnumerator(context, i2, str, z2);
    }

    @Override // javax.naming.NamingEnumeration
    public boolean hasMore() throws NamingException {
        return !this.rootProcessed || (this.scope != 0 && hasMoreDescendants());
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        try {
            return hasMore();
        } catch (NamingException e2) {
            return false;
        }
    }

    @Override // java.util.Enumeration
    /* renamed from: nextElement */
    public Binding nextElement2() {
        try {
            return next();
        } catch (NamingException e2) {
            throw new NoSuchElementException(e2.toString());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javax.naming.NamingEnumeration
    public Binding next() throws NamingException {
        if (!this.rootProcessed) {
            this.rootProcessed = true;
            return new Binding("", this.root.getClass().getName(), this.root, true);
        }
        if (this.scope != 0 && hasMoreDescendants()) {
            return getNextDescendant();
        }
        throw new NoSuchElementException();
    }

    @Override // javax.naming.NamingEnumeration
    public void close() throws NamingException {
        this.root = null;
    }

    private boolean hasMoreChildren() throws NamingException {
        return this.children != null && this.children.hasMore();
    }

    private Binding getNextChild() throws NamingException {
        Binding binding;
        Binding next = this.children.next();
        if (next.isRelative() && !this.contextName.equals("")) {
            Name name = this.root.getNameParser("").parse(this.contextName);
            name.add(next.getName());
            if (debug) {
                System.out.println("ContextEnumerator: adding " + ((Object) name));
            }
            binding = new Binding(name.toString(), next.getClassName(), next.getObject(), next.isRelative());
        } else {
            if (debug) {
                System.out.println("ContextEnumerator: using old binding");
            }
            binding = next;
        }
        return binding;
    }

    private boolean hasMoreDescendants() throws NamingException {
        if (!this.currentReturned) {
            if (debug) {
                System.out.println("hasMoreDescendants returning " + (this.currentChild != null));
            }
            return this.currentChild != null;
        }
        if (this.currentChildExpanded && this.currentChildEnum.hasMore()) {
            if (debug) {
                System.out.println("hasMoreDescendants returning true");
                return true;
            }
            return true;
        }
        if (debug) {
            System.out.println("hasMoreDescendants returning hasMoreChildren");
        }
        return hasMoreChildren();
    }

    private Binding getNextDescendant() throws NamingException {
        if (!this.currentReturned) {
            if (debug) {
                System.out.println("getNextDescedant: simple case");
            }
            this.currentReturned = true;
            return this.currentChild;
        }
        if (this.currentChildExpanded && this.currentChildEnum.hasMore()) {
            if (debug) {
                System.out.println("getNextDescedant: expanded case");
            }
            return this.currentChildEnum.next();
        }
        if (debug) {
            System.out.println("getNextDescedant: next case");
        }
        prepNextChild();
        return getNextDescendant();
    }

    private void prepNextChild() throws NamingException {
        if (hasMoreChildren()) {
            try {
                this.currentChild = getNextChild();
                this.currentReturned = false;
            } catch (NamingException e2) {
                if (debug) {
                    System.out.println(e2);
                }
                if (debug) {
                    e2.printStackTrace();
                }
            }
            if (this.scope == 2 && (this.currentChild.getObject() instanceof Context)) {
                this.currentChildEnum = newEnumerator((Context) this.currentChild.getObject(), this.scope, this.currentChild.getName(), false);
                this.currentChildExpanded = true;
                if (debug) {
                    System.out.println("prepNextChild: expanded");
                    return;
                }
                return;
            }
            this.currentChildExpanded = false;
            this.currentChildEnum = null;
            if (debug) {
                System.out.println("prepNextChild: normal");
                return;
            }
            return;
        }
        this.currentChild = null;
    }
}
