package com.sun.org.apache.xml.internal.security.c14n.helper;

import java.io.Serializable;
import java.util.Comparator;
import org.w3c.dom.Attr;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/helper/AttrCompare.class */
public class AttrCompare implements Comparator<Attr>, Serializable {
    private static final long serialVersionUID = -7113259629930576230L;
    private static final int ATTR0_BEFORE_ATTR1 = -1;
    private static final int ATTR1_BEFORE_ATTR0 = 1;
    private static final String XMLNS = "http://www.w3.org/2000/xmlns/";

    @Override // java.util.Comparator
    public int compare(Attr attr, Attr attr2) {
        String namespaceURI = attr.getNamespaceURI();
        String namespaceURI2 = attr2.getNamespaceURI();
        boolean zEquals = "http://www.w3.org/2000/xmlns/".equals(namespaceURI);
        boolean zEquals2 = "http://www.w3.org/2000/xmlns/".equals(namespaceURI2);
        if (zEquals) {
            if (zEquals2) {
                String localName = attr.getLocalName();
                String localName2 = attr2.getLocalName();
                if ("xmlns".equals(localName)) {
                    localName = "";
                }
                if ("xmlns".equals(localName2)) {
                    localName2 = "";
                }
                return localName.compareTo(localName2);
            }
            return -1;
        }
        if (zEquals2) {
            return 1;
        }
        if (namespaceURI == null) {
            if (namespaceURI2 == null) {
                return attr.getName().compareTo(attr2.getName());
            }
            return -1;
        }
        if (namespaceURI2 == null) {
            return 1;
        }
        int iCompareTo = namespaceURI.compareTo(namespaceURI2);
        if (iCompareTo != 0) {
            return iCompareTo;
        }
        return attr.getLocalName().compareTo(attr2.getLocalName());
    }
}
