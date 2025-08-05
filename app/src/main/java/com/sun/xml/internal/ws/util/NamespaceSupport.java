package com.sun.xml.internal.ws.util;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/NamespaceSupport.class */
public final class NamespaceSupport {
    public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
    private static final Iterable<String> EMPTY_ENUMERATION = new ArrayList();
    private Context[] contexts;
    private Context currentContext;
    private int contextPos;

    public NamespaceSupport() {
        reset();
    }

    public NamespaceSupport(NamespaceSupport that) {
        this.contexts = new Context[that.contexts.length];
        this.currentContext = null;
        this.contextPos = that.contextPos;
        Context currentParent = null;
        for (int i2 = 0; i2 < that.contexts.length; i2++) {
            Context thatContext = that.contexts[i2];
            if (thatContext == null) {
                this.contexts[i2] = null;
            } else {
                Context thisContext = new Context(thatContext, currentParent);
                this.contexts[i2] = thisContext;
                if (that.currentContext == thatContext) {
                    this.currentContext = thisContext;
                }
                currentParent = thisContext;
            }
        }
    }

    public void reset() {
        this.contexts = new Context[32];
        this.contextPos = 0;
        Context[] contextArr = this.contexts;
        int i2 = this.contextPos;
        Context context = new Context();
        this.currentContext = context;
        contextArr[i2] = context;
        this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
    }

    public void pushContext() {
        int max = this.contexts.length;
        this.contextPos++;
        if (this.contextPos >= max) {
            Context[] newContexts = new Context[max * 2];
            System.arraycopy(this.contexts, 0, newContexts, 0, max);
            this.contexts = newContexts;
        }
        this.currentContext = this.contexts[this.contextPos];
        if (this.currentContext == null) {
            Context[] contextArr = this.contexts;
            int i2 = this.contextPos;
            Context context = new Context();
            this.currentContext = context;
            contextArr[i2] = context;
        }
        if (this.contextPos > 0) {
            this.currentContext.setParent(this.contexts[this.contextPos - 1]);
        }
    }

    public void popContext() {
        this.contextPos--;
        if (this.contextPos < 0) {
            throw new EmptyStackException();
        }
        this.currentContext = this.contexts[this.contextPos];
    }

    public void slideContextUp() {
        this.contextPos--;
        this.currentContext = this.contexts[this.contextPos];
    }

    public void slideContextDown() {
        this.contextPos++;
        if (this.contexts[this.contextPos] == null) {
            this.contexts[this.contextPos] = this.contexts[this.contextPos - 1];
        }
        this.currentContext = this.contexts[this.contextPos];
    }

    public boolean declarePrefix(String prefix, String uri) {
        if ((prefix.equals("xml") && !uri.equals("http://www.w3.org/XML/1998/namespace")) || prefix.equals("xmlns")) {
            return false;
        }
        this.currentContext.declarePrefix(prefix, uri);
        return true;
    }

    public String[] processName(String qName, String[] parts, boolean isAttribute) {
        String[] myParts = this.currentContext.processName(qName, isAttribute);
        if (myParts == null) {
            return null;
        }
        parts[0] = myParts[0];
        parts[1] = myParts[1];
        parts[2] = myParts[2];
        return parts;
    }

    public String getURI(String prefix) {
        return this.currentContext.getURI(prefix);
    }

    public Iterable<String> getPrefixes() {
        return this.currentContext.getPrefixes();
    }

    public String getPrefix(String uri) {
        return this.currentContext.getPrefix(uri);
    }

    public Iterator getPrefixes(String uri) {
        List prefixes = new ArrayList();
        for (String prefix : getPrefixes()) {
            if (uri.equals(getURI(prefix))) {
                prefixes.add(prefix);
            }
        }
        return prefixes.iterator();
    }

    public Iterable<String> getDeclaredPrefixes() {
        return this.currentContext.getDeclaredPrefixes();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/NamespaceSupport$Context.class */
    static final class Context {
        HashMap prefixTable;
        HashMap uriTable;
        HashMap elementNameTable;
        HashMap attributeNameTable;
        String defaultNS;
        private ArrayList declarations;
        private boolean tablesDirty;
        private Context parent;

        Context() {
            this.defaultNS = null;
            this.declarations = null;
            this.tablesDirty = false;
            this.parent = null;
            copyTables();
        }

        Context(Context that, Context newParent) {
            this.defaultNS = null;
            this.declarations = null;
            this.tablesDirty = false;
            this.parent = null;
            if (that == null) {
                copyTables();
                return;
            }
            if (newParent != null && !that.tablesDirty) {
                this.prefixTable = that.prefixTable == that.parent.prefixTable ? newParent.prefixTable : (HashMap) that.prefixTable.clone();
                this.uriTable = that.uriTable == that.parent.uriTable ? newParent.uriTable : (HashMap) that.uriTable.clone();
                this.elementNameTable = that.elementNameTable == that.parent.elementNameTable ? newParent.elementNameTable : (HashMap) that.elementNameTable.clone();
                this.attributeNameTable = that.attributeNameTable == that.parent.attributeNameTable ? newParent.attributeNameTable : (HashMap) that.attributeNameTable.clone();
                this.defaultNS = that.defaultNS == that.parent.defaultNS ? newParent.defaultNS : that.defaultNS;
            } else {
                this.prefixTable = (HashMap) that.prefixTable.clone();
                this.uriTable = (HashMap) that.uriTable.clone();
                this.elementNameTable = (HashMap) that.elementNameTable.clone();
                this.attributeNameTable = (HashMap) that.attributeNameTable.clone();
                this.defaultNS = that.defaultNS;
            }
            this.tablesDirty = that.tablesDirty;
            this.parent = newParent;
            this.declarations = that.declarations == null ? null : (ArrayList) that.declarations.clone();
        }

        void setParent(Context parent) {
            this.parent = parent;
            this.declarations = null;
            this.prefixTable = parent.prefixTable;
            this.uriTable = parent.uriTable;
            this.elementNameTable = parent.elementNameTable;
            this.attributeNameTable = parent.attributeNameTable;
            this.defaultNS = parent.defaultNS;
            this.tablesDirty = false;
        }

        void declarePrefix(String prefix, String uri) {
            if (!this.tablesDirty) {
                copyTables();
            }
            if (this.declarations == null) {
                this.declarations = new ArrayList();
            }
            String prefix2 = prefix.intern();
            String uri2 = uri.intern();
            if ("".equals(prefix2)) {
                if ("".equals(uri2)) {
                    this.defaultNS = null;
                } else {
                    this.defaultNS = uri2;
                }
            } else {
                this.prefixTable.put(prefix2, uri2);
                this.uriTable.put(uri2, prefix2);
            }
            this.declarations.add(prefix2);
        }

        String[] processName(String qName, boolean isAttribute) {
            Map table;
            String uri;
            if (isAttribute) {
                table = this.elementNameTable;
            } else {
                table = this.attributeNameTable;
            }
            String[] name = (String[]) table.get(qName);
            if (name != null) {
                return name;
            }
            String[] name2 = new String[3];
            int index = qName.indexOf(58);
            if (index == -1) {
                if (isAttribute || this.defaultNS == null) {
                    name2[0] = "";
                } else {
                    name2[0] = this.defaultNS;
                }
                name2[1] = qName.intern();
                name2[2] = name2[1];
            } else {
                String prefix = qName.substring(0, index);
                String local = qName.substring(index + 1);
                if ("".equals(prefix)) {
                    uri = this.defaultNS;
                } else {
                    uri = (String) this.prefixTable.get(prefix);
                }
                if (uri == null) {
                    return null;
                }
                name2[0] = uri;
                name2[1] = local.intern();
                name2[2] = qName.intern();
            }
            table.put(name2[2], name2);
            this.tablesDirty = true;
            return name2;
        }

        String getURI(String prefix) {
            if ("".equals(prefix)) {
                return this.defaultNS;
            }
            if (this.prefixTable == null) {
                return null;
            }
            return (String) this.prefixTable.get(prefix);
        }

        String getPrefix(String uri) {
            if (this.uriTable == null) {
                return null;
            }
            return (String) this.uriTable.get(uri);
        }

        Iterable<String> getDeclaredPrefixes() {
            if (this.declarations == null) {
                return NamespaceSupport.EMPTY_ENUMERATION;
            }
            return this.declarations;
        }

        Iterable<String> getPrefixes() {
            if (this.prefixTable == null) {
                return NamespaceSupport.EMPTY_ENUMERATION;
            }
            return this.prefixTable.keySet();
        }

        private void copyTables() {
            if (this.prefixTable != null) {
                this.prefixTable = (HashMap) this.prefixTable.clone();
            } else {
                this.prefixTable = new HashMap();
            }
            if (this.uriTable != null) {
                this.uriTable = (HashMap) this.uriTable.clone();
            } else {
                this.uriTable = new HashMap();
            }
            this.elementNameTable = new HashMap();
            this.attributeNameTable = new HashMap();
            this.tablesDirty = true;
        }
    }
}
