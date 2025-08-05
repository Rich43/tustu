package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.dom.CoreDOMImplementationImpl;
import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSException;
import com.sun.org.apache.xerces.internal.xs.XSImplementation;
import com.sun.org.apache.xerces.internal.xs.XSLoader;
import java.util.MissingResourceException;
import org.w3c.dom.DOMImplementation;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSImplementationImpl.class */
public class XSImplementationImpl extends CoreDOMImplementationImpl implements XSImplementation {
    static XSImplementationImpl singleton = new XSImplementationImpl();

    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDOMImplementationImpl, org.w3c.dom.DOMImplementation
    public boolean hasFeature(String feature, String version) {
        return (feature.equalsIgnoreCase("XS-Loader") && (version == null || version.equals("1.0"))) || super.hasFeature(feature, version);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSImplementation
    public XSLoader createXSLoader(StringList versions) throws XSException, MissingResourceException {
        XSLoader loader = new XSLoaderImpl();
        if (versions == null) {
            return loader;
        }
        for (int i2 = 0; i2 < versions.getLength(); i2++) {
            if (!versions.item(i2).equals("1.0")) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{versions.item(i2)});
                throw new XSException((short) 1, msg);
            }
        }
        return loader;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSImplementation
    public StringList getRecognizedVersions() {
        StringListImpl list = new StringListImpl(new String[]{"1.0"}, 1);
        return list;
    }
}
