package com.sun.org.apache.xalan.internal.lib;

import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeIterator;
import com.sun.org.apache.xpath.internal.NodeSet;
import com.sun.org.apache.xpath.internal.axes.RTFIterator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/lib/ExsltCommon.class */
public class ExsltCommon {
    public static String objectType(Object obj) {
        if (obj instanceof String) {
            return "string";
        }
        if (obj instanceof Boolean) {
            return "boolean";
        }
        if (obj instanceof Number) {
            return "number";
        }
        if (obj instanceof DTMNodeIterator) {
            DTMIterator dtmI = ((DTMNodeIterator) obj).getDTMIterator();
            if (dtmI instanceof RTFIterator) {
                return "RTF";
            }
            return "node-set";
        }
        return "unknown";
    }

    public static NodeSet nodeSet(ExpressionContext myProcessor, Object rtf) {
        return Extensions.nodeset(myProcessor, rtf);
    }
}
