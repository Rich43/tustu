package com.sun.org.apache.xalan.internal.lib;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/lib/ExsltBase.class */
public abstract class ExsltBase {
    protected static String toString(Node n2) throws DOMException {
        if (n2 instanceof DTMNodeProxy) {
            return ((DTMNodeProxy) n2).getStringValue();
        }
        String value = n2.getNodeValue();
        if (value == null) {
            NodeList nodelist = n2.getChildNodes();
            StringBuffer buf = new StringBuffer();
            for (int i2 = 0; i2 < nodelist.getLength(); i2++) {
                Node childNode = nodelist.item(i2);
                buf.append(toString(childNode));
            }
            return buf.toString();
        }
        return value;
    }

    protected static double toNumber(Node n2) throws DOMException {
        double d2;
        String str = toString(n2);
        try {
            d2 = Double.valueOf(str).doubleValue();
        } catch (NumberFormatException e2) {
            d2 = Double.NaN;
        }
        return d2;
    }
}
