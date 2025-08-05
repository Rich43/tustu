package com.sun.org.apache.xalan.internal.extensions;

import com.sun.org.apache.xml.internal.utils.QName;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/extensions/ExpressionContext.class */
public interface ExpressionContext {
    Node getContextNode();

    NodeIterator getContextNodes();

    ErrorListener getErrorListener();

    double toNumber(Node node);

    String toString(Node node);

    XObject getVariableOrParam(QName qName) throws TransformerException;

    XPathContext getXPathContext() throws TransformerException;
}
