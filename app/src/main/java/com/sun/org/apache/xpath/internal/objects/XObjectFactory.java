package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.axes.OneStepIterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XObjectFactory.class */
public class XObjectFactory {
    public static XObject create(Object val) {
        XObject result;
        if (val instanceof XObject) {
            result = (XObject) val;
        } else if (val instanceof String) {
            result = new XString((String) val);
        } else if (val instanceof Boolean) {
            result = new XBoolean((Boolean) val);
        } else if (val instanceof Double) {
            result = new XNumber((Double) val);
        } else {
            result = new XObject(val);
        }
        return result;
    }

    public static XObject create(Object val, XPathContext xctxt) {
        XObject result;
        if (val instanceof XObject) {
            result = (XObject) val;
        } else if (val instanceof String) {
            result = new XString((String) val);
        } else if (val instanceof Boolean) {
            result = new XBoolean((Boolean) val);
        } else if (val instanceof Number) {
            result = new XNumber((Number) val);
        } else if (val instanceof DTM) {
            DTM dtm = (DTM) val;
            try {
                int dtmRoot = dtm.getDocument();
                DTMAxisIterator iter = dtm.getAxisIterator(13);
                iter.setStartNode(dtmRoot);
                DTMIterator iterator = new OneStepIterator(iter, 13);
                iterator.setRoot(dtmRoot, xctxt);
                result = new XNodeSet(iterator);
            } catch (Exception ex) {
                throw new WrappedRuntimeException(ex);
            }
        } else if (val instanceof DTMAxisIterator) {
            DTMAxisIterator iter2 = (DTMAxisIterator) val;
            try {
                DTMIterator iterator2 = new OneStepIterator(iter2, 13);
                iterator2.setRoot(iter2.getStartNode(), xctxt);
                result = new XNodeSet(iterator2);
            } catch (Exception ex2) {
                throw new WrappedRuntimeException(ex2);
            }
        } else if (val instanceof DTMIterator) {
            result = new XNodeSet((DTMIterator) val);
        } else if (val instanceof Node) {
            result = new XNodeSetForDOM((Node) val, xctxt);
        } else if (val instanceof NodeList) {
            result = new XNodeSetForDOM((NodeList) val, xctxt);
        } else if (val instanceof NodeIterator) {
            result = new XNodeSetForDOM((NodeIterator) val, xctxt);
        } else {
            result = new XObject(val);
        }
        return result;
    }
}
