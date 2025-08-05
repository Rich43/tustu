package com.sun.org.apache.xerces.internal.dom;

import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;
import org.w3c.dom.DOMImplementationSource;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMImplementationSourceImpl.class */
public class DOMImplementationSourceImpl implements DOMImplementationSource {
    @Override // org.w3c.dom.DOMImplementationSource
    public DOMImplementation getDOMImplementation(String features) {
        DOMImplementation impl = CoreDOMImplementationImpl.getDOMImplementation();
        if (testImpl(impl, features)) {
            return impl;
        }
        DOMImplementation impl2 = DOMImplementationImpl.getDOMImplementation();
        if (testImpl(impl2, features)) {
            return impl2;
        }
        return null;
    }

    @Override // org.w3c.dom.DOMImplementationSource
    public DOMImplementationList getDOMImplementationList(String features) {
        DOMImplementation impl = CoreDOMImplementationImpl.getDOMImplementation();
        Vector implementations = new Vector();
        if (testImpl(impl, features)) {
            implementations.addElement(impl);
        }
        DOMImplementation impl2 = DOMImplementationImpl.getDOMImplementation();
        if (testImpl(impl2, features)) {
            implementations.addElement(impl2);
        }
        return new DOMImplementationListImpl(implementations);
    }

    boolean testImpl(DOMImplementation impl, String features) {
        String version;
        StringTokenizer st = new StringTokenizer(features);
        String feature = null;
        if (st.hasMoreTokens()) {
            feature = st.nextToken();
        }
        while (feature != null) {
            boolean isVersion = false;
            if (st.hasMoreTokens()) {
                version = st.nextToken();
                char c2 = version.charAt(0);
                switch (c2) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        isVersion = true;
                        break;
                }
            } else {
                version = null;
            }
            if (isVersion) {
                if (!impl.hasFeature(feature, version)) {
                    return false;
                }
                if (st.hasMoreTokens()) {
                    feature = st.nextToken();
                } else {
                    feature = null;
                }
            } else {
                if (!impl.hasFeature(feature, null)) {
                    return false;
                }
                feature = version;
            }
        }
        return true;
    }
}
