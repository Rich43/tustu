package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.impl.xs.XSImplementationImpl;
import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMXSImplementationSourceImpl.class */
public class DOMXSImplementationSourceImpl extends DOMImplementationSourceImpl {
    @Override // com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl, org.w3c.dom.DOMImplementationSource
    public DOMImplementation getDOMImplementation(String features) {
        DOMImplementation impl = super.getDOMImplementation(features);
        if (impl != null) {
            return impl;
        }
        DOMImplementation impl2 = PSVIDOMImplementationImpl.getDOMImplementation();
        if (testImpl(impl2, features)) {
            return impl2;
        }
        DOMImplementation impl3 = XSImplementationImpl.getDOMImplementation();
        if (testImpl(impl3, features)) {
            return impl3;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl, org.w3c.dom.DOMImplementationSource
    public DOMImplementationList getDOMImplementationList(String features) {
        Vector implementations = new Vector();
        DOMImplementationList list = super.getDOMImplementationList(features);
        for (int i2 = 0; i2 < list.getLength(); i2++) {
            implementations.addElement(list.item(i2));
        }
        DOMImplementation impl = PSVIDOMImplementationImpl.getDOMImplementation();
        if (testImpl(impl, features)) {
            implementations.addElement(impl);
        }
        DOMImplementation impl2 = XSImplementationImpl.getDOMImplementation();
        if (testImpl(impl2, features)) {
            implementations.addElement(impl2);
        }
        return new DOMImplementationListImpl(implementations);
    }
}
