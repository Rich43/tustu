package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import com.sun.org.apache.xml.internal.utils.XMLString;
import java.util.Map;
import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/AdaptiveResultTreeImpl.class */
public class AdaptiveResultTreeImpl extends SimpleResultTreeImpl {
    private static int _documentURIIndex = 0;
    private static final String EMPTY_STRING = "".intern();
    private SAXImpl _dom;
    private DTMWSFilter _wsfilter;
    private int _initSize;
    private boolean _buildIdIndex;
    private final AttributesImpl _attributes;
    private String _openElementName;

    public AdaptiveResultTreeImpl(XSLTCDTMManager dtmManager, int documentID, DTMWSFilter wsfilter, int initSize, boolean buildIdIndex) {
        super(dtmManager, documentID);
        this._attributes = new AttributesImpl();
        this._wsfilter = wsfilter;
        this._initSize = initSize;
        this._buildIdIndex = buildIdIndex;
    }

    public DOM getNestedDOM() {
        return this._dom;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public int getDocument() {
        if (this._dom != null) {
            return this._dom.getDocument();
        }
        return super.getDocument();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getStringValue() {
        if (this._dom != null) {
            return this._dom.getStringValue();
        }
        return super.getStringValue();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getIterator() {
        if (this._dom != null) {
            return this._dom.getIterator();
        }
        return super.getIterator();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getChildren(int node) {
        if (this._dom != null) {
            return this._dom.getChildren(node);
        }
        return super.getChildren(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getTypedChildren(int type) {
        if (this._dom != null) {
            return this._dom.getTypedChildren(type);
        }
        return super.getTypedChildren(type);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getAxisIterator(int axis) {
        if (this._dom != null) {
            return this._dom.getAxisIterator(axis);
        }
        return super.getAxisIterator(axis);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
        if (this._dom != null) {
            return this._dom.getTypedAxisIterator(axis, type);
        }
        return super.getTypedAxisIterator(axis, type);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNthDescendant(int node, int n2, boolean includeself) {
        if (this._dom != null) {
            return this._dom.getNthDescendant(node, n2, includeself);
        }
        return super.getNthDescendant(node, n2, includeself);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
        if (this._dom != null) {
            return this._dom.getNamespaceAxisIterator(axis, ns);
        }
        return super.getNamespaceAxisIterator(axis, ns);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iter, int returnType, String value, boolean op) {
        if (this._dom != null) {
            return this._dom.getNodeValueIterator(iter, returnType, value, op);
        }
        return super.getNodeValueIterator(iter, returnType, value, op);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
        if (this._dom != null) {
            return this._dom.orderNodes(source, node);
        }
        return super.orderNodes(source, node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeName(int node) {
        if (this._dom != null) {
            return this._dom.getNodeName(node);
        }
        return super.getNodeName(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeNameX(int node) {
        if (this._dom != null) {
            return this._dom.getNodeNameX(node);
        }
        return super.getNodeNameX(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getNamespaceName(int node) {
        if (this._dom != null) {
            return this._dom.getNamespaceName(node);
        }
        return super.getNamespaceName(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public int getExpandedTypeID(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getExpandedTypeID(nodeHandle);
        }
        return super.getExpandedTypeID(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNamespaceType(int node) {
        if (this._dom != null) {
            return this._dom.getNamespaceType(node);
        }
        return super.getNamespaceType(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public int getParent(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getParent(nodeHandle);
        }
        return super.getParent(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getAttributeNode(int gType, int element) {
        if (this._dom != null) {
            return this._dom.getAttributeNode(gType, element);
        }
        return super.getAttributeNode(gType, element);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getStringValueX(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getStringValueX(nodeHandle);
        }
        return super.getStringValueX(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public void copy(int node, SerializationHandler handler) throws TransletException {
        if (this._dom != null) {
            this._dom.copy(node, handler);
        } else {
            super.copy(node, handler);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public void copy(DTMAxisIterator nodes, SerializationHandler handler) throws TransletException {
        if (this._dom != null) {
            this._dom.copy(nodes, handler);
        } else {
            super.copy(nodes, handler);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
        if (this._dom != null) {
            return this._dom.shallowCopy(node, handler);
        }
        return super.shallowCopy(node, handler);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean lessThan(int node1, int node2) {
        if (this._dom != null) {
            return this._dom.lessThan(node1, node2);
        }
        return super.lessThan(node1, node2);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public void characters(int node, SerializationHandler handler) throws TransletException {
        if (this._dom != null) {
            this._dom.characters(node, handler);
        } else {
            super.characters(node, handler);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public Node makeNode(int index) {
        if (this._dom != null) {
            return this._dom.makeNode(index);
        }
        return super.makeNode(index);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public Node makeNode(DTMAxisIterator iter) {
        if (this._dom != null) {
            return this._dom.makeNode(iter);
        }
        return super.makeNode(iter);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public NodeList makeNodeList(int index) {
        if (this._dom != null) {
            return this._dom.makeNodeList(index);
        }
        return super.makeNodeList(index);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public NodeList makeNodeList(DTMAxisIterator iter) {
        if (this._dom != null) {
            return this._dom.makeNodeList(iter);
        }
        return super.makeNodeList(iter);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getLanguage(int node) {
        if (this._dom != null) {
            return this._dom.getLanguage(node);
        }
        return super.getLanguage(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getSize() {
        if (this._dom != null) {
            return this._dom.getSize();
        }
        return super.getSize();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getDocumentURI(int node) {
        if (this._dom != null) {
            return this._dom.getDocumentURI(node);
        }
        StringBuilder sbAppend = new StringBuilder().append("adaptive_rtf");
        int i2 = _documentURIIndex;
        _documentURIIndex = i2 + 1;
        return sbAppend.append(i2).toString();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public void setFilter(StripFilter filter) {
        if (this._dom != null) {
            this._dom.setFilter(filter);
        } else {
            super.setFilter(filter);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces) {
        if (this._dom != null) {
            this._dom.setupMapping(names, uris, types, namespaces);
        } else {
            super.setupMapping(names, uris, types, namespaces);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean isElement(int node) {
        if (this._dom != null) {
            return this._dom.isElement(node);
        }
        return super.isElement(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean isAttribute(int node) {
        if (this._dom != null) {
            return this._dom.isAttribute(node);
        }
        return super.isAttribute(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public String lookupNamespace(int node, String prefix) throws TransletException {
        if (this._dom != null) {
            return this._dom.lookupNamespace(node, prefix);
        }
        return super.lookupNamespace(node, prefix);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public final int getNodeIdent(int nodehandle) {
        if (this._dom != null) {
            return this._dom.getNodeIdent(nodehandle);
        }
        return super.getNodeIdent(nodehandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public final int getNodeHandle(int nodeId) {
        if (this._dom != null) {
            return this._dom.getNodeHandle(nodeId);
        }
        return super.getNodeHandle(nodeId);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public DOM getResultTreeFrag(int initialSize, int rtfType) {
        if (this._dom != null) {
            return this._dom.getResultTreeFrag(initialSize, rtfType);
        }
        return super.getResultTreeFrag(initialSize, rtfType);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public SerializationHandler getOutputDomBuilder() {
        return this;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNSType(int node) {
        if (this._dom != null) {
            return this._dom.getNSType(node);
        }
        return super.getNSType(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public String getUnparsedEntityURI(String name) {
        if (this._dom != null) {
            return this._dom.getUnparsedEntityURI(name);
        }
        return super.getUnparsedEntityURI(name);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public Map<String, Integer> getElementsWithIDs() {
        if (this._dom != null) {
            return this._dom.getElementsWithIDs();
        }
        return super.getElementsWithIDs();
    }

    private void maybeEmitStartElement() throws SAXException {
        if (this._openElementName != null) {
            int index = this._openElementName.indexOf(58);
            if (index < 0) {
                this._dom.startElement(null, this._openElementName, this._openElementName, this._attributes);
            } else {
                String uri = this._dom.getNamespaceURI(this._openElementName.substring(0, index));
                this._dom.startElement(uri, this._openElementName.substring(index + 1), this._openElementName, this._attributes);
            }
            this._openElementName = null;
        }
    }

    private void prepareNewDOM() throws SAXException {
        this._dom = (SAXImpl) this._dtmManager.getDTM(null, true, this._wsfilter, true, false, false, this._initSize, this._buildIdIndex);
        this._dom.startDocument();
        for (int i2 = 0; i2 < this._size; i2++) {
            String str = this._textArray[i2];
            this._dom.characters(str.toCharArray(), 0, str.length());
        }
        this._size = 0;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        if (this._dom != null) {
            this._dom.endDocument();
        } else {
            super.endDocument();
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String str) throws SAXException {
        if (this._dom != null) {
            characters(str.toCharArray(), 0, str.length());
        } else {
            super.characters(str);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void characters(char[] ch, int offset, int length) throws SAXException {
        if (this._dom != null) {
            maybeEmitStartElement();
            this._dom.characters(ch, offset, length);
        } else {
            super.characters(ch, offset, length);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public boolean setEscaping(boolean escape) throws SAXException {
        if (this._dom != null) {
            return this._dom.setEscaping(escape);
        }
        return super.setEscaping(escape);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementName) throws SAXException {
        if (this._dom == null) {
            prepareNewDOM();
        }
        maybeEmitStartElement();
        this._openElementName = elementName;
        this._attributes.clear();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String uri, String localName, String qName) throws SAXException {
        startElement(qName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        startElement(qName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String elementName) throws SAXException {
        maybeEmitStartElement();
        this._dom.endElement(null, null, elementName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        endElement(qName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String qName, String value) {
        int colonpos = qName.indexOf(58);
        String uri = EMPTY_STRING;
        String localName = qName;
        if (colonpos > 0) {
            String prefix = qName.substring(0, colonpos);
            localName = qName.substring(colonpos + 1);
            uri = this._dom.getNamespaceURI(prefix);
        }
        addAttribute(uri, localName, qName, "CDATA", value);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addUniqueAttribute(String qName, String value, int flags) throws SAXException {
        addAttribute(qName, value);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String qname, String type, String value) {
        if (this._openElementName != null) {
            this._attributes.addAttribute(uri, localName, qname, type, value);
        } else {
            BasisLibrary.runTimeError("STRAY_ATTRIBUTE_ERR", qname);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
        if (this._dom == null) {
            prepareNewDOM();
        }
        this._dom.startPrefixMapping(prefix, uri);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedLexicalHandler
    public void comment(String comment) throws SAXException {
        if (this._dom == null) {
            prepareNewDOM();
        }
        maybeEmitStartElement();
        char[] chars = comment.toCharArray();
        this._dom.comment(chars, 0, chars.length);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ext.LexicalHandler
    public void comment(char[] chars, int offset, int length) throws SAXException {
        if (this._dom == null) {
            prepareNewDOM();
        }
        maybeEmitStartElement();
        this._dom.comment(chars, offset, length);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        if (this._dom == null) {
            prepareNewDOM();
        }
        maybeEmitStartElement();
        this._dom.processingInstruction(target, data);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public void setFeature(String featureId, boolean state) {
        if (this._dom != null) {
            this._dom.setFeature(featureId, state);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public void setProperty(String property, Object value) {
        if (this._dom != null) {
            this._dom.setProperty(property, value);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisTraverser getAxisTraverser(int axis) {
        if (this._dom != null) {
            return this._dom.getAxisTraverser(axis);
        }
        return super.getAxisTraverser(axis);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean hasChildNodes(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.hasChildNodes(nodeHandle);
        }
        return super.hasChildNodes(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getFirstChild(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getFirstChild(nodeHandle);
        }
        return super.getFirstChild(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getLastChild(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getLastChild(nodeHandle);
        }
        return super.getLastChild(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getAttributeNode(int elementHandle, String namespaceURI, String name) {
        if (this._dom != null) {
            return this._dom.getAttributeNode(elementHandle, namespaceURI, name);
        }
        return super.getAttributeNode(elementHandle, namespaceURI, name);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getFirstAttribute(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getFirstAttribute(nodeHandle);
        }
        return super.getFirstAttribute(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getFirstNamespaceNode(int nodeHandle, boolean inScope) {
        if (this._dom != null) {
            return this._dom.getFirstNamespaceNode(nodeHandle, inScope);
        }
        return super.getFirstNamespaceNode(nodeHandle, inScope);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getNextSibling(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getNextSibling(nodeHandle);
        }
        return super.getNextSibling(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getPreviousSibling(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getPreviousSibling(nodeHandle);
        }
        return super.getPreviousSibling(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getNextAttribute(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getNextAttribute(nodeHandle);
        }
        return super.getNextAttribute(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope) {
        if (this._dom != null) {
            return this._dom.getNextNamespaceNode(baseHandle, namespaceHandle, inScope);
        }
        return super.getNextNamespaceNode(baseHandle, namespaceHandle, inScope);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getOwnerDocument(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getOwnerDocument(nodeHandle);
        }
        return super.getOwnerDocument(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getDocumentRoot(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getDocumentRoot(nodeHandle);
        }
        return super.getDocumentRoot(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public XMLString getStringValue(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getStringValue(nodeHandle);
        }
        return super.getStringValue(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getStringValueChunkCount(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getStringValueChunkCount(nodeHandle);
        }
        return super.getStringValueChunkCount(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen) {
        if (this._dom != null) {
            return this._dom.getStringValueChunk(nodeHandle, chunkIndex, startAndLen);
        }
        return super.getStringValueChunk(nodeHandle, chunkIndex, startAndLen);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getExpandedTypeID(String namespace, String localName, int type) {
        if (this._dom != null) {
            return this._dom.getExpandedTypeID(namespace, localName, type);
        }
        return super.getExpandedTypeID(namespace, localName, type);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getLocalNameFromExpandedNameID(int ExpandedNameID) {
        if (this._dom != null) {
            return this._dom.getLocalNameFromExpandedNameID(ExpandedNameID);
        }
        return super.getLocalNameFromExpandedNameID(ExpandedNameID);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNamespaceFromExpandedNameID(int ExpandedNameID) {
        if (this._dom != null) {
            return this._dom.getNamespaceFromExpandedNameID(ExpandedNameID);
        }
        return super.getNamespaceFromExpandedNameID(ExpandedNameID);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getLocalName(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getLocalName(nodeHandle);
        }
        return super.getLocalName(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getPrefix(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getPrefix(nodeHandle);
        }
        return super.getPrefix(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNamespaceURI(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getNamespaceURI(nodeHandle);
        }
        return super.getNamespaceURI(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeValue(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getNodeValue(nodeHandle);
        }
        return super.getNodeValue(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public short getNodeType(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getNodeType(nodeHandle);
        }
        return super.getNodeType(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public short getLevel(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getLevel(nodeHandle);
        }
        return super.getLevel(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isSupported(String feature, String version) {
        if (this._dom != null) {
            return this._dom.isSupported(feature, version);
        }
        return super.isSupported(feature, version);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentBaseURI() {
        if (this._dom != null) {
            return this._dom.getDocumentBaseURI();
        }
        return super.getDocumentBaseURI();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public void setDocumentBaseURI(String baseURI) {
        if (this._dom != null) {
            this._dom.setDocumentBaseURI(baseURI);
        } else {
            super.setDocumentBaseURI(baseURI);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentSystemIdentifier(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getDocumentSystemIdentifier(nodeHandle);
        }
        return super.getDocumentSystemIdentifier(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentEncoding(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getDocumentEncoding(nodeHandle);
        }
        return super.getDocumentEncoding(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentStandalone(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getDocumentStandalone(nodeHandle);
        }
        return super.getDocumentStandalone(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentVersion(int documentHandle) {
        if (this._dom != null) {
            return this._dom.getDocumentVersion(documentHandle);
        }
        return super.getDocumentVersion(documentHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean getDocumentAllDeclarationsProcessed() {
        if (this._dom != null) {
            return this._dom.getDocumentAllDeclarationsProcessed();
        }
        return super.getDocumentAllDeclarationsProcessed();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentTypeDeclarationSystemIdentifier() {
        if (this._dom != null) {
            return this._dom.getDocumentTypeDeclarationSystemIdentifier();
        }
        return super.getDocumentTypeDeclarationSystemIdentifier();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentTypeDeclarationPublicIdentifier() {
        if (this._dom != null) {
            return this._dom.getDocumentTypeDeclarationPublicIdentifier();
        }
        return super.getDocumentTypeDeclarationPublicIdentifier();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public int getElementById(String elementId) {
        if (this._dom != null) {
            return this._dom.getElementById(elementId);
        }
        return super.getElementById(elementId);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean supportsPreStripping() {
        if (this._dom != null) {
            return this._dom.supportsPreStripping();
        }
        return super.supportsPreStripping();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isNodeAfter(int firstNodeHandle, int secondNodeHandle) {
        if (this._dom != null) {
            return this._dom.isNodeAfter(firstNodeHandle, secondNodeHandle);
        }
        return super.isNodeAfter(firstNodeHandle, secondNodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isCharacterElementContentWhitespace(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.isCharacterElementContentWhitespace(nodeHandle);
        }
        return super.isCharacterElementContentWhitespace(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isDocumentAllDeclarationsProcessed(int documentHandle) {
        if (this._dom != null) {
            return this._dom.isDocumentAllDeclarationsProcessed(documentHandle);
        }
        return super.isDocumentAllDeclarationsProcessed(documentHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isAttributeSpecified(int attributeHandle) {
        if (this._dom != null) {
            return this._dom.isAttributeSpecified(attributeHandle);
        }
        return super.isAttributeSpecified(attributeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException {
        if (this._dom != null) {
            this._dom.dispatchCharactersEvents(nodeHandle, ch, normalize);
        } else {
            super.dispatchCharactersEvents(nodeHandle, ch, normalize);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public void dispatchToEvents(int nodeHandle, ContentHandler ch) throws SAXException {
        if (this._dom != null) {
            this._dom.dispatchToEvents(nodeHandle, ch);
        } else {
            super.dispatchToEvents(nodeHandle, ch);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public Node getNode(int nodeHandle) {
        if (this._dom != null) {
            return this._dom.getNode(nodeHandle);
        }
        return super.getNode(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean needsTwoThreads() {
        if (this._dom != null) {
            return this._dom.needsTwoThreads();
        }
        return super.needsTwoThreads();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public ContentHandler getContentHandler() {
        if (this._dom != null) {
            return this._dom.getContentHandler();
        }
        return super.getContentHandler();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public LexicalHandler getLexicalHandler() {
        if (this._dom != null) {
            return this._dom.getLexicalHandler();
        }
        return super.getLexicalHandler();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public EntityResolver getEntityResolver() {
        if (this._dom != null) {
            return this._dom.getEntityResolver();
        }
        return super.getEntityResolver();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public DTDHandler getDTDHandler() {
        if (this._dom != null) {
            return this._dom.getDTDHandler();
        }
        return super.getDTDHandler();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public ErrorHandler getErrorHandler() {
        if (this._dom != null) {
            return this._dom.getErrorHandler();
        }
        return super.getErrorHandler();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public DeclHandler getDeclHandler() {
        if (this._dom != null) {
            return this._dom.getDeclHandler();
        }
        return super.getDeclHandler();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public void appendChild(int newChild, boolean clone, boolean cloneDepth) {
        if (this._dom != null) {
            this._dom.appendChild(newChild, clone, cloneDepth);
        } else {
            super.appendChild(newChild, clone, cloneDepth);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public void appendTextChild(String str) {
        if (this._dom != null) {
            this._dom.appendTextChild(str);
        } else {
            super.appendTextChild(str);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public SourceLocator getSourceLocatorFor(int node) {
        if (this._dom != null) {
            return this._dom.getSourceLocatorFor(node);
        }
        return super.getSourceLocatorFor(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public void documentRegistration() {
        if (this._dom != null) {
            this._dom.documentRegistration();
        } else {
            super.documentRegistration();
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xml.internal.dtm.DTM
    public void documentRelease() {
        if (this._dom != null) {
            this._dom.documentRelease();
        } else {
            super.documentRelease();
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl, com.sun.org.apache.xalan.internal.xsltc.DOM
    public void release() {
        if (this._dom != null) {
            this._dom.release();
            this._dom = null;
        }
        super.release();
    }
}
