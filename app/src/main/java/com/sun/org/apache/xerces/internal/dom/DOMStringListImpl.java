package com.sun.org.apache.xerces.internal.dom;

import java.util.ArrayList;
import java.util.Vector;
import org.w3c.dom.DOMStringList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMStringListImpl.class */
public class DOMStringListImpl implements DOMStringList {
    private final ArrayList fStrings;

    public DOMStringListImpl() {
        this.fStrings = new ArrayList();
    }

    public DOMStringListImpl(ArrayList params) {
        this.fStrings = params;
    }

    public DOMStringListImpl(Vector params) {
        this.fStrings = new ArrayList(params);
    }

    @Override // org.w3c.dom.DOMStringList
    public String item(int index) {
        int length = getLength();
        if (index >= 0 && index < length) {
            return (String) this.fStrings.get(index);
        }
        return null;
    }

    @Override // org.w3c.dom.DOMStringList
    public int getLength() {
        return this.fStrings.size();
    }

    @Override // org.w3c.dom.DOMStringList
    public boolean contains(String param) {
        return this.fStrings.contains(param);
    }

    public void add(String param) {
        this.fStrings.add(param);
    }
}
