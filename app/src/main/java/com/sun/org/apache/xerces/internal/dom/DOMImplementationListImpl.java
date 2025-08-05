package com.sun.org.apache.xerces.internal.dom;

import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMImplementationListImpl.class */
public class DOMImplementationListImpl implements DOMImplementationList {
    private Vector fImplementations;

    public DOMImplementationListImpl() {
        this.fImplementations = new Vector();
    }

    public DOMImplementationListImpl(Vector params) {
        this.fImplementations = params;
    }

    @Override // org.w3c.dom.DOMImplementationList
    public DOMImplementation item(int index) {
        try {
            return (DOMImplementation) this.fImplementations.elementAt(index);
        } catch (ArrayIndexOutOfBoundsException e2) {
            return null;
        }
    }

    @Override // org.w3c.dom.DOMImplementationList
    public int getLength() {
        return this.fImplementations.size();
    }
}
