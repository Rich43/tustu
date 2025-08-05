package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter.class */
public final class DOMAdapter implements DOM {
    private DOMEnhancedForDTM _enhancedDOM;
    private DOM _dom;
    private String[] _namesArray;
    private String[] _urisArray;
    private int[] _typesArray;
    private String[] _namespaceArray;
    private short[] _mapping = null;
    private int[] _reverse = null;
    private short[] _NSmapping = null;
    private short[] _NSreverse = null;
    private int _multiDOMMask;

    public DOMAdapter(DOM dom, String[] namesArray, String[] urisArray, int[] typesArray, String[] namespaceArray) {
        if (dom instanceof DOMEnhancedForDTM) {
            this._enhancedDOM = (DOMEnhancedForDTM) dom;
        }
        this._dom = dom;
        this._namesArray = namesArray;
        this._urisArray = urisArray;
        this._typesArray = typesArray;
        this._namespaceArray = namespaceArray;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void setupMapping(String[] names, String[] urisArray, int[] typesArray, String[] namespaces) {
        this._namesArray = names;
        this._urisArray = urisArray;
        this._typesArray = typesArray;
        this._namespaceArray = namespaces;
    }

    public String[] getNamesArray() {
        return this._namesArray;
    }

    public String[] getUrisArray() {
        return this._urisArray;
    }

    public int[] getTypesArray() {
        return this._typesArray;
    }

    public String[] getNamespaceArray() {
        return this._namespaceArray;
    }

    public DOM getDOMImpl() {
        return this._dom;
    }

    private short[] getMapping() {
        if (this._mapping == null && this._enhancedDOM != null) {
            this._mapping = this._enhancedDOM.getMapping(this._namesArray, this._urisArray, this._typesArray);
        }
        return this._mapping;
    }

    private int[] getReverse() {
        if (this._reverse == null && this._enhancedDOM != null) {
            this._reverse = this._enhancedDOM.getReverseMapping(this._namesArray, this._urisArray, this._typesArray);
        }
        return this._reverse;
    }

    private short[] getNSMapping() {
        if (this._NSmapping == null && this._enhancedDOM != null) {
            this._NSmapping = this._enhancedDOM.getNamespaceMapping(this._namespaceArray);
        }
        return this._NSmapping;
    }

    private short[] getNSReverse() {
        if (this._NSreverse == null && this._enhancedDOM != null) {
            this._NSreverse = this._enhancedDOM.getReverseNamespaceMapping(this._namespaceArray);
        }
        return this._NSreverse;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getIterator() {
        return this._dom.getIterator();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getStringValue() {
        return this._dom.getStringValue();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getChildren(int node) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getChildren(node);
        }
        DTMAxisIterator iterator = this._dom.getChildren(node);
        return iterator.setStartNode(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void setFilter(StripFilter filter) {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getTypedChildren(int type) {
        int[] reverse = getReverse();
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getTypedChildren(reverse[type]);
        }
        return this._dom.getTypedChildren(type);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
        return this._dom.getNamespaceAxisIterator(axis, getNSReverse()[ns]);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getAxisIterator(int axis) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getAxisIterator(axis);
        }
        return this._dom.getAxisIterator(axis);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
        int[] reverse = getReverse();
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getTypedAxisIterator(axis, reverse[type]);
        }
        return this._dom.getTypedAxisIterator(axis, type);
    }

    public int getMultiDOMMask() {
        return this._multiDOMMask;
    }

    public void setMultiDOMMask(int mask) {
        this._multiDOMMask = mask;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNthDescendant(int type, int n2, boolean includeself) {
        return this._dom.getNthDescendant(getReverse()[type], n2, includeself);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op) {
        return this._dom.getNodeValueIterator(iterator, type, value, op);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
        return this._dom.orderNodes(source, node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public int getExpandedTypeID(int node) {
        int type;
        short[] mapping = getMapping();
        if (this._enhancedDOM != null) {
            type = mapping[this._enhancedDOM.getExpandedTypeID2(node)];
        } else if (null != mapping) {
            type = mapping[this._dom.getExpandedTypeID(node)];
        } else {
            type = this._dom.getExpandedTypeID(node);
        }
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNamespaceType(int node) {
        return getNSMapping()[this._dom.getNSType(node)];
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNSType(int node) {
        return this._dom.getNSType(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public int getParent(int node) {
        return this._dom.getParent(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getAttributeNode(int type, int element) {
        return this._dom.getAttributeNode(getReverse()[type], element);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeName(int node) {
        if (node == -1) {
            return "";
        }
        return this._dom.getNodeName(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeNameX(int node) {
        if (node == -1) {
            return "";
        }
        return this._dom.getNodeNameX(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getNamespaceName(int node) {
        if (node == -1) {
            return "";
        }
        return this._dom.getNamespaceName(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getStringValueX(int node) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getStringValueX(node);
        }
        if (node == -1) {
            return "";
        }
        return this._dom.getStringValueX(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void copy(int node, SerializationHandler handler) throws TransletException {
        this._dom.copy(node, handler);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void copy(DTMAxisIterator nodes, SerializationHandler handler) throws TransletException {
        this._dom.copy(nodes, handler);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.shallowCopy(node, handler);
        }
        return this._dom.shallowCopy(node, handler);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean lessThan(int node1, int node2) {
        return this._dom.lessThan(node1, node2);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void characters(int textNode, SerializationHandler handler) throws TransletException {
        if (this._enhancedDOM != null) {
            this._enhancedDOM.characters(textNode, handler);
        } else {
            this._dom.characters(textNode, handler);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public Node makeNode(int index) {
        return this._dom.makeNode(index);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public Node makeNode(DTMAxisIterator iter) {
        return this._dom.makeNode(iter);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public NodeList makeNodeList(int index) {
        return this._dom.makeNodeList(index);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public NodeList makeNodeList(DTMAxisIterator iter) {
        return this._dom.makeNodeList(iter);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getLanguage(int node) {
        return this._dom.getLanguage(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getSize() {
        return this._dom.getSize();
    }

    public void setDocumentURI(String uri) {
        if (this._enhancedDOM != null) {
            this._enhancedDOM.setDocumentURI(uri);
        }
    }

    public String getDocumentURI() {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getDocumentURI();
        }
        return "";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getDocumentURI(int node) {
        return this._dom.getDocumentURI(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public int getDocument() {
        return this._dom.getDocument();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean isElement(int node) {
        return this._dom.isElement(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean isAttribute(int node) {
        return this._dom.isAttribute(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNodeIdent(int nodeHandle) {
        return this._dom.getNodeIdent(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNodeHandle(int nodeId) {
        return this._dom.getNodeHandle(nodeId);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DOM getResultTreeFrag(int initSize, int rtfType) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getResultTreeFrag(initSize, rtfType);
        }
        return this._dom.getResultTreeFrag(initSize, rtfType);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getResultTreeFrag(initSize, rtfType, addToManager);
        }
        return this._dom.getResultTreeFrag(initSize, rtfType, addToManager);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public SerializationHandler getOutputDomBuilder() {
        return this._dom.getOutputDomBuilder();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String lookupNamespace(int node, String prefix) throws TransletException {
        return this._dom.lookupNamespace(node, prefix);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public String getUnparsedEntityURI(String entity) {
        return this._dom.getUnparsedEntityURI(entity);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public Map<String, Integer> getElementsWithIDs() {
        return this._dom.getElementsWithIDs();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void release() {
        this._dom.release();
    }
}
