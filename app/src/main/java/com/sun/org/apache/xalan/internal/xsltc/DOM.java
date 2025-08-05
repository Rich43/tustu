package com.sun.org.apache.xalan.internal.xsltc;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/DOM.class */
public interface DOM {
    public static final int FIRST_TYPE = 0;
    public static final int NO_TYPE = -1;
    public static final int NULL = 0;
    public static final int RETURN_CURRENT = 0;
    public static final int RETURN_PARENT = 1;
    public static final int SIMPLE_RTF = 0;
    public static final int ADAPTIVE_RTF = 1;
    public static final int TREE_RTF = 2;

    DTMAxisIterator getIterator();

    String getStringValue();

    DTMAxisIterator getChildren(int i2);

    DTMAxisIterator getTypedChildren(int i2);

    DTMAxisIterator getAxisIterator(int i2);

    DTMAxisIterator getTypedAxisIterator(int i2, int i3);

    DTMAxisIterator getNthDescendant(int i2, int i3, boolean z2);

    DTMAxisIterator getNamespaceAxisIterator(int i2, int i3);

    DTMAxisIterator getNodeValueIterator(DTMAxisIterator dTMAxisIterator, int i2, String str, boolean z2);

    DTMAxisIterator orderNodes(DTMAxisIterator dTMAxisIterator, int i2);

    String getNodeName(int i2);

    String getNodeNameX(int i2);

    String getNamespaceName(int i2);

    int getExpandedTypeID(int i2);

    int getNamespaceType(int i2);

    int getParent(int i2);

    int getAttributeNode(int i2, int i3);

    String getStringValueX(int i2);

    void copy(int i2, SerializationHandler serializationHandler) throws TransletException;

    void copy(DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler) throws TransletException;

    String shallowCopy(int i2, SerializationHandler serializationHandler) throws TransletException;

    boolean lessThan(int i2, int i3);

    void characters(int i2, SerializationHandler serializationHandler) throws TransletException;

    Node makeNode(int i2);

    Node makeNode(DTMAxisIterator dTMAxisIterator);

    NodeList makeNodeList(int i2);

    NodeList makeNodeList(DTMAxisIterator dTMAxisIterator);

    String getLanguage(int i2);

    int getSize();

    String getDocumentURI(int i2);

    void setFilter(StripFilter stripFilter);

    void setupMapping(String[] strArr, String[] strArr2, int[] iArr, String[] strArr3);

    boolean isElement(int i2);

    boolean isAttribute(int i2);

    String lookupNamespace(int i2, String str) throws TransletException;

    int getNodeIdent(int i2);

    int getNodeHandle(int i2);

    DOM getResultTreeFrag(int i2, int i3);

    DOM getResultTreeFrag(int i2, int i3, boolean z2);

    SerializationHandler getOutputDomBuilder();

    int getNSType(int i2);

    int getDocument();

    String getUnparsedEntityURI(String str);

    Map<String, Integer> getElementsWithIDs();

    void release();
}
