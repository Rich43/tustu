package com.sun.org.apache.xml.internal.security.c14n.helper;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/helper/C14nHelper.class */
public final class C14nHelper {
    private C14nHelper() {
    }

    public static boolean namespaceIsRelative(Attr attr) {
        return !namespaceIsAbsolute(attr);
    }

    public static boolean namespaceIsRelative(String str) {
        return !namespaceIsAbsolute(str);
    }

    public static boolean namespaceIsAbsolute(Attr attr) {
        return namespaceIsAbsolute(attr.getValue());
    }

    public static boolean namespaceIsAbsolute(String str) {
        return str.length() == 0 || str.indexOf(58) > 0;
    }

    public static void assertNotRelativeNS(Attr attr) throws CanonicalizationException {
        if (attr == null) {
            return;
        }
        String nodeName = attr.getNodeName();
        boolean zEquals = "xmlns".equals(nodeName);
        boolean zStartsWith = nodeName.startsWith("xmlns:");
        if ((zEquals || zStartsWith) && namespaceIsRelative(attr)) {
            throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", new Object[]{attr.getOwnerElement().getTagName(), nodeName, attr.getValue()});
        }
    }

    public static void checkTraversability(Document document) throws CanonicalizationException {
        if (!document.isSupported("Traversal", "2.0")) {
            throw new CanonicalizationException("c14n.Canonicalizer.TraversalNotSupported", new Object[]{document.getImplementation().getClass().getName()});
        }
    }

    public static void checkForRelativeNamespace(Element element) throws CanonicalizationException {
        if (element != null) {
            NamedNodeMap attributes = element.getAttributes();
            int length = attributes.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                assertNotRelativeNS((Attr) attributes.item(i2));
            }
            return;
        }
        throw new CanonicalizationException("Called checkForRelativeNamespace() on null");
    }
}
