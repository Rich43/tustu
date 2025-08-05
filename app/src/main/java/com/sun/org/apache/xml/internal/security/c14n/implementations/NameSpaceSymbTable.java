package com.sun.org.apache.xml.internal.security.c14n.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/NameSpaceSymbTable.class */
public class NameSpaceSymbTable {
    private static final String XMLNS = "xmlns";
    private static final SymbMap initialMap = new SymbMap();
    private boolean cloned = true;
    private List<SymbMap> level = new ArrayList();
    private SymbMap symb = (SymbMap) initialMap.clone();

    static {
        NameSpaceSymbEntry nameSpaceSymbEntry = new NameSpaceSymbEntry("", null, true, "xmlns");
        nameSpaceSymbEntry.lastrendered = "";
        initialMap.put("xmlns", nameSpaceSymbEntry);
    }

    public void getUnrenderedNodes(Collection<Attr> collection) {
        for (NameSpaceSymbEntry nameSpaceSymbEntry : this.symb.entrySet()) {
            if (!nameSpaceSymbEntry.rendered && nameSpaceSymbEntry.f12009n != null) {
                NameSpaceSymbEntry nameSpaceSymbEntry2 = (NameSpaceSymbEntry) nameSpaceSymbEntry.clone();
                needsClone();
                this.symb.put(nameSpaceSymbEntry2.prefix, nameSpaceSymbEntry2);
                nameSpaceSymbEntry2.lastrendered = nameSpaceSymbEntry2.uri;
                nameSpaceSymbEntry2.rendered = true;
                collection.add(nameSpaceSymbEntry2.f12009n);
            }
        }
    }

    public void outputNodePush() {
        push();
    }

    public void outputNodePop() {
        pop();
    }

    public void push() {
        this.level.add(null);
        this.cloned = false;
    }

    public void pop() {
        int size = this.level.size() - 1;
        SymbMap symbMapRemove = this.level.remove(size);
        if (symbMapRemove != null) {
            this.symb = symbMapRemove;
            if (size == 0) {
                this.cloned = false;
                return;
            } else {
                this.cloned = this.level.get(size - 1) != this.symb;
                return;
            }
        }
        this.cloned = false;
    }

    final void needsClone() {
        if (!this.cloned) {
            this.level.set(this.level.size() - 1, this.symb);
            this.symb = (SymbMap) this.symb.clone();
            this.cloned = true;
        }
    }

    public Attr getMapping(String str) {
        NameSpaceSymbEntry nameSpaceSymbEntry = this.symb.get(str);
        if (nameSpaceSymbEntry == null || nameSpaceSymbEntry.rendered) {
            return null;
        }
        NameSpaceSymbEntry nameSpaceSymbEntry2 = (NameSpaceSymbEntry) nameSpaceSymbEntry.clone();
        needsClone();
        this.symb.put(str, nameSpaceSymbEntry2);
        nameSpaceSymbEntry2.rendered = true;
        nameSpaceSymbEntry2.lastrendered = nameSpaceSymbEntry2.uri;
        return nameSpaceSymbEntry2.f12009n;
    }

    public Attr getMappingWithoutRendered(String str) {
        NameSpaceSymbEntry nameSpaceSymbEntry = this.symb.get(str);
        if (nameSpaceSymbEntry == null || nameSpaceSymbEntry.rendered) {
            return null;
        }
        return nameSpaceSymbEntry.f12009n;
    }

    public boolean addMapping(String str, String str2, Attr attr) {
        NameSpaceSymbEntry nameSpaceSymbEntry = this.symb.get(str);
        if (nameSpaceSymbEntry != null && str2.equals(nameSpaceSymbEntry.uri)) {
            return false;
        }
        NameSpaceSymbEntry nameSpaceSymbEntry2 = new NameSpaceSymbEntry(str2, attr, false, str);
        needsClone();
        this.symb.put(str, nameSpaceSymbEntry2);
        if (nameSpaceSymbEntry != null) {
            nameSpaceSymbEntry2.lastrendered = nameSpaceSymbEntry.lastrendered;
            if (nameSpaceSymbEntry.lastrendered != null && nameSpaceSymbEntry.lastrendered.equals(str2)) {
                nameSpaceSymbEntry2.rendered = true;
                return true;
            }
            return true;
        }
        return true;
    }

    public Node addMappingAndRender(String str, String str2, Attr attr) {
        NameSpaceSymbEntry nameSpaceSymbEntry = this.symb.get(str);
        if (nameSpaceSymbEntry != null && str2.equals(nameSpaceSymbEntry.uri)) {
            if (!nameSpaceSymbEntry.rendered) {
                NameSpaceSymbEntry nameSpaceSymbEntry2 = (NameSpaceSymbEntry) nameSpaceSymbEntry.clone();
                needsClone();
                this.symb.put(str, nameSpaceSymbEntry2);
                nameSpaceSymbEntry2.lastrendered = str2;
                nameSpaceSymbEntry2.rendered = true;
                return nameSpaceSymbEntry2.f12009n;
            }
            return null;
        }
        NameSpaceSymbEntry nameSpaceSymbEntry3 = new NameSpaceSymbEntry(str2, attr, true, str);
        nameSpaceSymbEntry3.lastrendered = str2;
        needsClone();
        this.symb.put(str, nameSpaceSymbEntry3);
        if (nameSpaceSymbEntry != null && nameSpaceSymbEntry.lastrendered != null && nameSpaceSymbEntry.lastrendered.equals(str2)) {
            nameSpaceSymbEntry3.rendered = true;
            return null;
        }
        return nameSpaceSymbEntry3.f12009n;
    }

    public int getLevel() {
        return this.level.size();
    }

    public void removeMapping(String str) {
        if (this.symb.get(str) != null) {
            needsClone();
            this.symb.put(str, null);
        }
    }

    public void removeMappingIfNotRender(String str) {
        NameSpaceSymbEntry nameSpaceSymbEntry = this.symb.get(str);
        if (nameSpaceSymbEntry != null && !nameSpaceSymbEntry.rendered) {
            needsClone();
            this.symb.put(str, null);
        }
    }

    public boolean removeMappingIfRender(String str) {
        NameSpaceSymbEntry nameSpaceSymbEntry = this.symb.get(str);
        if (nameSpaceSymbEntry != null && nameSpaceSymbEntry.rendered) {
            needsClone();
            this.symb.put(str, null);
            return false;
        }
        return false;
    }
}
