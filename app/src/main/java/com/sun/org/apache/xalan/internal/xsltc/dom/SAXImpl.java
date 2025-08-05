package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xml.internal.dtm.Axis;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIterNodeList;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
import com.sun.org.apache.xml.internal.dtm.ref.EmptyIterator;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import com.sun.org.apache.xml.internal.serializer.ToXMLSAXHandler;
import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SAXImpl.class */
public final class SAXImpl extends SAX2DTM2 implements DOMEnhancedForDTM, DOMBuilder {
    private int _uriCount;
    private int[] _xmlSpaceStack;
    private int _idx;
    private boolean _preserve;
    private static final String XML_PREFIX = "xml";
    private static final String XMLSPACE_STRING = "xml:space";
    private static final String PRESERVE_STRING = "preserve";
    private static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
    private boolean _escaping;
    private boolean _disableEscaping;
    private int _textNodeToProcess;
    private static final String EMPTYSTRING = "";
    private int _namesSize;
    private Map<Integer, Integer> _nsIndex;
    private int _size;
    private BitArray _dontEscape;
    private Document _document;
    private Map<Node, Integer> _node2Ids;
    private boolean _hasDOMSource;
    private XSLTCDTMManager _dtmManager;
    private Node[] _nodes;
    private NodeList[] _nodeLists;
    private static final DTMAxisIterator EMPTYITERATOR = EmptyIterator.getInstance();
    private static int _documentURIIndex = 0;

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM
    public void setDocumentURI(String uri) {
        if (uri != null) {
            setDocumentBaseURI(SystemIDResolver.getAbsoluteURI(uri));
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM
    public String getDocumentURI() {
        String baseURI = getDocumentBaseURI();
        if (baseURI != null) {
            return baseURI;
        }
        StringBuilder sbAppend = new StringBuilder().append("rtf");
        int i2 = _documentURIIndex;
        _documentURIIndex = i2 + 1;
        return sbAppend.append(i2).toString();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getDocumentURI(int node) {
        return getDocumentURI();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void setupMapping(String[] names, String[] urisArray, int[] typesArray, String[] namespaces) {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String lookupNamespace(int node, String prefix) throws TransletException {
        int nsnode;
        SAX2DTM2.AncestorIterator ancestors = new SAX2DTM2.AncestorIterator();
        if (isElement(node)) {
            ancestors.includeSelf();
        }
        ancestors.setStartNode(node);
        while (true) {
            int anode = ancestors.next();
            if (anode != -1) {
                DTMDefaultBaseIterators.NamespaceIterator namespaces = new DTMDefaultBaseIterators.NamespaceIterator();
                namespaces.setStartNode(anode);
                do {
                    nsnode = namespaces.next();
                    if (nsnode != -1) {
                    }
                } while (!getLocalName(nsnode).equals(prefix));
                return getNodeValue(nsnode);
            }
            BasisLibrary.runTimeError(BasisLibrary.NAMESPACE_PREFIX_ERR, prefix);
            return null;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean isElement(int node) {
        return getNodeType(node) == 1;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean isAttribute(int node) {
        return getNodeType(node) == 2;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getSize() {
        return getNumberOfNodes();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void setFilter(StripFilter filter) {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean lessThan(int node1, int node2) {
        if (node1 == -1) {
            return false;
        }
        return node2 == -1 || node1 < node2;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public Node makeNode(int index) {
        if (this._nodes == null) {
            this._nodes = new Node[this._namesSize];
        }
        int nodeID = makeNodeIdentity(index);
        if (nodeID < 0) {
            return null;
        }
        if (nodeID < this._nodes.length) {
            if (this._nodes[nodeID] != null) {
                return this._nodes[nodeID];
            }
            Node[] nodeArr = this._nodes;
            DTMNodeProxy dTMNodeProxy = new DTMNodeProxy(this, index);
            nodeArr[nodeID] = dTMNodeProxy;
            return dTMNodeProxy;
        }
        return new DTMNodeProxy(this, index);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public Node makeNode(DTMAxisIterator iter) {
        return makeNode(iter.next());
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public NodeList makeNodeList(int index) {
        if (this._nodeLists == null) {
            this._nodeLists = new NodeList[this._namesSize];
        }
        int nodeID = makeNodeIdentity(index);
        if (nodeID < 0) {
            return null;
        }
        if (nodeID < this._nodeLists.length) {
            if (this._nodeLists[nodeID] != null) {
                return this._nodeLists[nodeID];
            }
            NodeList[] nodeListArr = this._nodeLists;
            DTMAxisIterNodeList dTMAxisIterNodeList = new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(this, index));
            nodeListArr[nodeID] = dTMAxisIterNodeList;
            return dTMAxisIterNodeList;
        }
        return new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(this, index));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public NodeList makeNodeList(DTMAxisIterator iter) {
        return new DTMAxisIterNodeList(this, iter);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SAXImpl$TypedNamespaceIterator.class */
    public class TypedNamespaceIterator extends DTMDefaultBaseIterators.NamespaceIterator {
        private String _nsPrefix;

        public TypedNamespaceIterator(int nodeType) {
            super();
            if (SAXImpl.this.m_expandedNameTable != null) {
                this._nsPrefix = SAXImpl.this.m_expandedNameTable.getLocalName(nodeType);
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.NamespaceIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._nsPrefix == null || this._nsPrefix.length() == 0) {
                return -1;
            }
            int next = super.next();
            while (true) {
                int node = next;
                if (node != -1) {
                    if (this._nsPrefix.compareTo(SAXImpl.this.getLocalName(node)) != 0) {
                        next = super.next();
                    } else {
                        return returnNode(node);
                    }
                } else {
                    return -1;
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SAXImpl$NodeValueIterator.class */
    private final class NodeValueIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private DTMAxisIterator _source;
        private String _value;
        private boolean _op;
        private final boolean _isReverse;
        private int _returnType;

        public NodeValueIterator(DTMAxisIterator source, int returnType, String value, boolean op) {
            super();
            this._returnType = 1;
            this._source = source;
            this._returnType = returnType;
            this._value = value;
            this._op = op;
            this._isReverse = source.isReverse();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public boolean isReverse() {
            return this._isReverse;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator cloneIterator() {
            try {
                NodeValueIterator clone = (NodeValueIterator) super.clone();
                clone._isRestartable = false;
                clone._source = this._source.cloneIterator();
                clone._value = this._value;
                clone._op = this._op;
                return clone.reset();
            } catch (CloneNotSupportedException e2) {
                BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e2.toString());
                return null;
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setRestartable(boolean isRestartable) {
            this._isRestartable = isRestartable;
            this._source.setRestartable(isRestartable);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            this._source.reset();
            return resetPosition();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node;
            String val;
            do {
                node = this._source.next();
                if (node != -1) {
                    val = SAXImpl.this.getStringValueX(node);
                } else {
                    return -1;
                }
            } while (this._value.equals(val) != this._op);
            if (this._returnType == 0) {
                return returnNode(node);
            }
            return returnNode(SAXImpl.this.getParent(node));
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (this._isRestartable) {
                DTMAxisIterator dTMAxisIterator = this._source;
                this._startNode = node;
                dTMAxisIterator.setStartNode(node);
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            this._source.setMark();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            this._source.gotoMark();
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op) {
        return new NodeValueIterator(iterator, type, value, op);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
        return new DupFilterIterator(source);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getIterator() {
        return new DTMDefaultBaseIterators.SingletonIterator(getDocument(), true);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNSType(int node) {
        String s2 = getNamespaceURI(node);
        if (s2 == null) {
            return 0;
        }
        int eType = getIdForNamespace(s2);
        return this._nsIndex.get(new Integer(eType)).intValue();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNamespaceType(int node) {
        return super.getNamespaceType(node);
    }

    public int getGeneralizedType(String name) {
        return getGeneralizedType(name, true);
    }

    public int getGeneralizedType(String name, boolean searchOnly) {
        int code;
        String ns = null;
        int index = name.lastIndexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        if (index > -1) {
            ns = name.substring(0, index);
        }
        int lNameStartIdx = index + 1;
        if (name.charAt(lNameStartIdx) == '@') {
            code = 2;
            lNameStartIdx++;
        } else {
            code = 1;
        }
        String lName = lNameStartIdx == 0 ? name : name.substring(lNameStartIdx);
        return this.m_expandedNameTable.getExpandedTypeID(ns, lName, code, searchOnly);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM
    public short[] getMapping(String[] names, String[] uris, int[] types) {
        if (this._namesSize < 0) {
            return getMapping2(names, uris, types);
        }
        int namesLength = names.length;
        int exLength = this.m_expandedNameTable.getSize();
        short[] result = new short[exLength];
        for (int i2 = 0; i2 < 14; i2++) {
            result[i2] = (short) i2;
        }
        for (int i3 = 14; i3 < exLength; i3++) {
            result[i3] = this.m_expandedNameTable.getType(i3);
        }
        for (int i4 = 0; i4 < namesLength; i4++) {
            int genType = this.m_expandedNameTable.getExpandedTypeID(uris[i4], names[i4], types[i4], true);
            if (genType >= 0 && genType < exLength) {
                result[genType] = (short) (i4 + 14);
            }
        }
        return result;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM
    public int[] getReverseMapping(String[] names, String[] uris, int[] types) {
        int[] result = new int[names.length + 14];
        for (int i2 = 0; i2 < 14; i2++) {
            result[i2] = i2;
        }
        for (int i3 = 0; i3 < names.length; i3++) {
            int type = this.m_expandedNameTable.getExpandedTypeID(uris[i3], names[i3], types[i3], true);
            result[i3 + 14] = type;
        }
        return result;
    }

    private short[] getMapping2(String[] names, String[] uris, int[] types) {
        int namesLength = names.length;
        int exLength = this.m_expandedNameTable.getSize();
        int[] generalizedTypes = null;
        if (namesLength > 0) {
            generalizedTypes = new int[namesLength];
        }
        int resultLength = exLength;
        for (int i2 = 0; i2 < namesLength; i2++) {
            generalizedTypes[i2] = this.m_expandedNameTable.getExpandedTypeID(uris[i2], names[i2], types[i2], false);
            if (this._namesSize < 0 && generalizedTypes[i2] >= resultLength) {
                resultLength = generalizedTypes[i2] + 1;
            }
        }
        short[] result = new short[resultLength];
        for (int i3 = 0; i3 < 14; i3++) {
            result[i3] = (short) i3;
        }
        for (int i4 = 14; i4 < exLength; i4++) {
            result[i4] = this.m_expandedNameTable.getType(i4);
        }
        for (int i5 = 0; i5 < namesLength; i5++) {
            int genType = generalizedTypes[i5];
            if (genType >= 0 && genType < resultLength) {
                result[genType] = (short) (i5 + 14);
            }
        }
        return result;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM
    public short[] getNamespaceMapping(String[] namespaces) {
        int nsLength = namespaces.length;
        int mappingLength = this._uriCount;
        short[] result = new short[mappingLength];
        for (int i2 = 0; i2 < mappingLength; i2++) {
            result[i2] = -1;
        }
        for (int i3 = 0; i3 < nsLength; i3++) {
            int eType = getIdForNamespace(namespaces[i3]);
            Integer type = this._nsIndex.get(Integer.valueOf(eType));
            if (type != null) {
                result[type.intValue()] = (short) i3;
            }
        }
        return result;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM
    public short[] getReverseNamespaceMapping(String[] namespaces) {
        int length = namespaces.length;
        short[] result = new short[length];
        for (int i2 = 0; i2 < length; i2++) {
            int eType = getIdForNamespace(namespaces[i2]);
            Integer type = this._nsIndex.get(Integer.valueOf(eType));
            result[i2] = type == null ? (short) -1 : type.shortValue();
        }
        return result;
    }

    public SAXImpl(XSLTCDTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, boolean buildIdIndex) {
        this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, buildIdIndex, false);
    }

    public SAXImpl(XSLTCDTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean buildIdIndex, boolean newNameTable) {
        super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, false, buildIdIndex, newNameTable);
        this._uriCount = 0;
        this._idx = 1;
        this._preserve = false;
        this._escaping = true;
        this._disableEscaping = false;
        this._textNodeToProcess = -1;
        this._namesSize = -1;
        this._nsIndex = new HashMap();
        this._size = 0;
        this._dontEscape = null;
        this._node2Ids = null;
        this._hasDOMSource = false;
        this._dtmManager = mgr;
        this._size = blocksize;
        this._xmlSpaceStack = new int[blocksize <= 64 ? 4 : 64];
        this._xmlSpaceStack[0] = 0;
        if (source instanceof DOMSource) {
            this._hasDOMSource = true;
            DOMSource domsrc = (DOMSource) source;
            Node node = domsrc.getNode();
            if (node instanceof Document) {
                this._document = (Document) node;
            } else {
                this._document = node.getOwnerDocument();
            }
            this._node2Ids = new HashMap();
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public void migrateTo(DTMManager manager) {
        super.migrateTo(manager);
        if (manager instanceof XSLTCDTMManager) {
            this._dtmManager = (XSLTCDTMManager) manager;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public int getElementById(String idString) {
        Integer id;
        Node node = this._document.getElementById(idString);
        if (node == null || (id = this._node2Ids.get(node)) == null) {
            return -1;
        }
        return id.intValue();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM
    public boolean hasDOMSource() {
        return this._hasDOMSource;
    }

    private void xmlSpaceDefine(String val, int node) {
        boolean setting = val.equals("preserve");
        if (setting != this._preserve) {
            int[] iArr = this._xmlSpaceStack;
            int i2 = this._idx;
            this._idx = i2 + 1;
            iArr[i2] = node;
            this._preserve = setting;
        }
    }

    private void xmlSpaceRevert(int node) {
        if (node == this._xmlSpaceStack[this._idx - 1]) {
            this._idx--;
            this._preserve = !this._preserve;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    protected boolean getShouldStripWhitespace() {
        if (this._preserve) {
            return false;
        }
        return super.getShouldStripWhitespace();
    }

    private void handleTextEscaping() {
        if (this._disableEscaping && this._textNodeToProcess != -1 && _type(this._textNodeToProcess) == 3) {
            if (this._dontEscape == null) {
                this._dontEscape = new BitArray(this._size);
            }
            if (this._textNodeToProcess >= this._dontEscape.size()) {
                this._dontEscape.resize(this._dontEscape.size() * 2);
            }
            this._dontEscape.setBit(this._textNodeToProcess);
            this._disableEscaping = false;
        }
        this._textNodeToProcess = -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        this._disableEscaping = !this._escaping;
        this._textNodeToProcess = getNumberOfNodes();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2, com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        super.startDocument();
        Map<Integer, Integer> map = this._nsIndex;
        int i2 = this._uriCount;
        this._uriCount = i2 + 1;
        map.put(0, Integer.valueOf(i2));
        definePrefixAndUri("xml", "http://www.w3.org/XML/1998/namespace");
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2, com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        super.endDocument();
        handleTextEscaping();
        this._namesSize = this.m_expandedNameTable.getSize();
    }

    public void startElement(String uri, String localName, String qname, Attributes attributes, Node node) throws SAXException {
        startElement(uri, localName, qname, attributes);
        if (this.m_buildIdIndex) {
            this._node2Ids.put(node, new Integer(this.m_parents.peek()));
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2, com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException {
        int index;
        super.startElement(uri, localName, qname, attributes);
        handleTextEscaping();
        if (this.m_wsfilter != null && (index = attributes.getIndex("xml:space")) >= 0) {
            xmlSpaceDefine(attributes.getValue(index), this.m_parents.peek());
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2, com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qname) throws SAXException {
        super.endElement(namespaceURI, localName, qname);
        handleTextEscaping();
        if (this.m_wsfilter != null) {
            xmlSpaceRevert(this.m_previous);
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2, com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        super.processingInstruction(target, data);
        handleTextEscaping();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        super.ignorableWhitespace(ch, start, length);
        this._textNodeToProcess = getNumberOfNodes();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        super.startPrefixMapping(prefix, uri);
        handleTextEscaping();
        definePrefixAndUri(prefix, uri);
    }

    private void definePrefixAndUri(String prefix, String uri) throws SAXException {
        Integer eType = new Integer(getIdForNamespace(uri));
        if (this._nsIndex.get(eType) == null) {
            Map<Integer, Integer> map = this._nsIndex;
            int i2 = this._uriCount;
            this._uriCount = i2 + 1;
            map.put(eType, Integer.valueOf(i2));
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2, com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        super.comment(ch, start, length);
        handleTextEscaping();
    }

    public boolean setEscaping(boolean value) {
        boolean temp = this._escaping;
        this._escaping = value;
        return temp;
    }

    public void print(int node, int level) {
        switch (getNodeType(node)) {
            case 0:
            case 9:
                print(getFirstChild(node), level);
                break;
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
            default:
                String name = getNodeName(node);
                System.out.print("<" + name);
                int firstAttribute = getFirstAttribute(node);
                while (true) {
                    int a2 = firstAttribute;
                    if (a2 != -1) {
                        System.out.print("\n" + getNodeName(a2) + "=\"" + getStringValueX(a2) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        firstAttribute = getNextAttribute(a2);
                    } else {
                        System.out.print('>');
                        int firstChild = getFirstChild(node);
                        while (true) {
                            int child = firstChild;
                            if (child != -1) {
                                print(child, level + 1);
                                firstChild = getNextSibling(child);
                            } else {
                                System.out.println("</" + name + '>');
                                break;
                            }
                        }
                    }
                }
            case 3:
            case 7:
            case 8:
                System.out.print(getStringValueX(node));
                break;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2, com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeName(int node) {
        short type = getNodeType(node);
        switch (type) {
            case 0:
            case 3:
            case 8:
            case 9:
                return "";
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            case 10:
            case 11:
            case 12:
            default:
                return super.getNodeName(node);
            case 13:
                return getLocalName(node);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getNamespaceName(int node) {
        String s2;
        return (node == -1 || (s2 = getNamespaceURI(node)) == null) ? "" : s2;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getAttributeNode(int type, int element) {
        int firstAttribute = getFirstAttribute(element);
        while (true) {
            int attr = firstAttribute;
            if (attr != -1) {
                if (getExpandedTypeID(attr) == type) {
                    return attr;
                }
                firstAttribute = getNextAttribute(attr);
            } else {
                return -1;
            }
        }
    }

    public String getAttributeValue(int type, int element) {
        int attr = getAttributeNode(type, element);
        return attr != -1 ? getStringValueX(attr) : "";
    }

    public String getAttributeValue(String name, int element) {
        return getAttributeValue(getGeneralizedType(name), element);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getChildren(int node) {
        return new SAX2DTM2.ChildrenIterator().setStartNode(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getTypedChildren(int type) {
        return new SAX2DTM2.TypedChildrenIterator(type);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators, com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getAxisIterator(int axis) {
        switch (axis) {
            case 0:
                return new SAX2DTM2.AncestorIterator();
            case 1:
                return new SAX2DTM2.AncestorIterator().includeSelf();
            case 2:
                return new SAX2DTM2.AttributeIterator();
            case 3:
                return new SAX2DTM2.ChildrenIterator();
            case 4:
                return new SAX2DTM2.DescendantIterator();
            case 5:
                return new SAX2DTM2.DescendantIterator().includeSelf();
            case 6:
                return new SAX2DTM2.FollowingIterator();
            case 7:
                return new SAX2DTM2.FollowingSiblingIterator();
            case 8:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            default:
                BasisLibrary.runTimeError(BasisLibrary.AXIS_SUPPORT_ERR, Axis.getNames(axis));
                return null;
            case 9:
                return new DTMDefaultBaseIterators.NamespaceIterator();
            case 10:
                return new SAX2DTM2.ParentIterator();
            case 11:
                return new SAX2DTM2.PrecedingIterator();
            case 12:
                return new SAX2DTM2.PrecedingSiblingIterator();
            case 13:
                return new DTMDefaultBaseIterators.SingletonIterator(this);
            case 19:
                return new DTMDefaultBaseIterators.RootIterator();
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators, com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
        if (axis == 3) {
            return new SAX2DTM2.TypedChildrenIterator(type);
        }
        if (type == -1) {
            return EMPTYITERATOR;
        }
        switch (axis) {
            case 0:
                return new SAX2DTM2.TypedAncestorIterator(type);
            case 1:
                return new SAX2DTM2.TypedAncestorIterator(type).includeSelf();
            case 2:
                return new SAX2DTM2.TypedAttributeIterator(type);
            case 3:
                return new SAX2DTM2.TypedChildrenIterator(type);
            case 4:
                return new SAX2DTM2.TypedDescendantIterator(type);
            case 5:
                return new SAX2DTM2.TypedDescendantIterator(type).includeSelf();
            case 6:
                return new SAX2DTM2.TypedFollowingIterator(type);
            case 7:
                return new SAX2DTM2.TypedFollowingSiblingIterator(type);
            case 8:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            default:
                BasisLibrary.runTimeError(BasisLibrary.TYPED_AXIS_SUPPORT_ERR, Axis.getNames(axis));
                return null;
            case 9:
                return new TypedNamespaceIterator(type);
            case 10:
                return new SAX2DTM2.ParentIterator().setNodeType(type);
            case 11:
                return new SAX2DTM2.TypedPrecedingIterator(type);
            case 12:
                return new SAX2DTM2.TypedPrecedingSiblingIterator(type);
            case 13:
                return new SAX2DTM2.TypedSingletonIterator(type);
            case 19:
                return new SAX2DTM2.TypedRootIterator(type);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
        if (ns == -1) {
            return EMPTYITERATOR;
        }
        switch (axis) {
            case 2:
                return new NamespaceAttributeIterator(ns);
            case 3:
                return new NamespaceChildrenIterator(ns);
            default:
                return new NamespaceWildcardIterator(axis, ns);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SAXImpl$NamespaceWildcardIterator.class */
    public final class NamespaceWildcardIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        protected int m_nsType;
        protected DTMAxisIterator m_baseIterator;

        public NamespaceWildcardIterator(int axis, int nsType) {
            super();
            this.m_nsType = nsType;
            switch (axis) {
                case 2:
                    this.m_baseIterator = SAXImpl.this.getAxisIterator(axis);
                case 9:
                    this.m_baseIterator = SAXImpl.this.getAxisIterator(axis);
                    break;
            }
            this.m_baseIterator = SAXImpl.this.getTypedAxisIterator(axis, 1);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (this._isRestartable) {
                this._startNode = node;
                this.m_baseIterator.setStartNode(node);
                resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node;
            do {
                node = this.m_baseIterator.next();
                if (node == -1) {
                    return -1;
                }
            } while (SAXImpl.this.getNSType(node) != this.m_nsType);
            return returnNode(node);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator cloneIterator() {
            try {
                DTMAxisIterator nestedClone = this.m_baseIterator.cloneIterator();
                NamespaceWildcardIterator clone = (NamespaceWildcardIterator) super.clone();
                clone.m_baseIterator = nestedClone;
                clone.m_nsType = this.m_nsType;
                clone._isRestartable = false;
                return clone;
            } catch (CloneNotSupportedException e2) {
                BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e2.toString());
                return null;
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public boolean isReverse() {
            return this.m_baseIterator.isReverse();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            this.m_baseIterator.setMark();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            this.m_baseIterator.gotoMark();
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SAXImpl$NamespaceChildrenIterator.class */
    public final class NamespaceChildrenIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _nsType;

        public NamespaceChildrenIterator(int type) {
            super();
            this._nsType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAXImpl.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = node == -1 ? -1 : -2;
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._currentNode != -1) {
                int i_firstch = -2 == this._currentNode ? SAXImpl.this._firstch(SAXImpl.this.makeNodeIdentity(this._startNode)) : SAXImpl.this._nextsib(this._currentNode);
                while (true) {
                    int node = i_firstch;
                    if (node != -1) {
                        int nodeHandle = SAXImpl.this.makeNodeHandle(node);
                        if (SAXImpl.this.getNSType(nodeHandle) != this._nsType) {
                            i_firstch = SAXImpl.this._nextsib(node);
                        } else {
                            this._currentNode = node;
                            return returnNode(nodeHandle);
                        }
                    } else {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SAXImpl$NamespaceAttributeIterator.class */
    public final class NamespaceAttributeIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _nsType;

        public NamespaceAttributeIterator(int nsType) {
            super();
            this._nsType = nsType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            int node2;
            if (node == 0) {
                node = SAXImpl.this.getDocument();
            }
            if (this._isRestartable) {
                int nsType = this._nsType;
                this._startNode = node;
                int firstAttribute = SAXImpl.this.getFirstAttribute(node);
                while (true) {
                    node2 = firstAttribute;
                    if (node2 == -1 || SAXImpl.this.getNSType(node2) == nsType) {
                        break;
                    }
                    firstAttribute = SAXImpl.this.getNextAttribute(node2);
                }
                this._currentNode = node2;
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int nextNode;
            int node = this._currentNode;
            int nsType = this._nsType;
            if (node == -1) {
                return -1;
            }
            int nextAttribute = SAXImpl.this.getNextAttribute(node);
            while (true) {
                nextNode = nextAttribute;
                if (nextNode == -1 || SAXImpl.this.getNSType(nextNode) == nsType) {
                    break;
                }
                nextAttribute = SAXImpl.this.getNextAttribute(nextNode);
            }
            this._currentNode = nextNode;
            return returnNode(node);
        }
    }

    public DTMAxisIterator getTypedDescendantIterator(int type) {
        return new SAX2DTM2.TypedDescendantIterator(type);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNthDescendant(int type, int n2, boolean includeself) {
        return new DTMDefaultBaseIterators.NthDescendantIterator(n2);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void characters(int node, SerializationHandler handler) throws TransletException {
        if (node != -1) {
            try {
                dispatchCharactersEvents(node, handler, false);
            } catch (SAXException e2) {
                throw new TransletException(e2);
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void copy(DTMAxisIterator nodes, SerializationHandler handler) throws TransletException {
        while (true) {
            int node = nodes.next();
            if (node != -1) {
                copy(node, handler);
            } else {
                return;
            }
        }
    }

    public void copy(SerializationHandler handler) throws TransletException {
        copy(getDocument(), handler);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void copy(int node, SerializationHandler handler) throws TransletException {
        copy(node, handler, false);
    }

    private final void copy(int node, SerializationHandler handler, boolean isChild) throws TransletException {
        int nodeID = makeNodeIdentity(node);
        int eType = _exptype2(nodeID);
        int type = _exptype2Type(eType);
        try {
            switch (type) {
                case 0:
                case 9:
                    int c2 = _firstch2(nodeID);
                    while (c2 != -1) {
                        copy(makeNodeHandle(c2), handler, true);
                        c2 = _nextsib2(c2);
                    }
                    break;
                case 1:
                case 4:
                case 5:
                case 6:
                case 10:
                case 11:
                case 12:
                default:
                    if (type == 1) {
                        String name = copyElement(nodeID, eType, handler);
                        copyNS(nodeID, handler, !isChild);
                        copyAttributes(nodeID, handler);
                        int c3 = _firstch2(nodeID);
                        while (c3 != -1) {
                            copy(makeNodeHandle(c3), handler, true);
                            c3 = _nextsib2(c3);
                        }
                        handler.endElement(name);
                        break;
                    } else {
                        String uri = getNamespaceName(node);
                        if (uri.length() != 0) {
                            String prefix = getPrefix(node);
                            handler.namespaceAfterStartElement(prefix, uri);
                        }
                        handler.addAttribute(getNodeName(node), getNodeValue(node));
                        break;
                    }
                case 2:
                    copyAttribute(nodeID, eType, handler);
                    break;
                case 3:
                    boolean oldEscapeSetting = false;
                    boolean escapeBit = false;
                    if (this._dontEscape != null) {
                        escapeBit = this._dontEscape.getBit(getNodeIdent(node));
                        if (escapeBit) {
                            oldEscapeSetting = handler.setEscaping(false);
                        }
                    }
                    copyTextNode(nodeID, handler);
                    if (escapeBit) {
                        handler.setEscaping(oldEscapeSetting);
                        break;
                    }
                    break;
                case 7:
                    copyPI(node, handler);
                    break;
                case 8:
                    handler.comment(getStringValueX(node));
                    break;
                case 13:
                    handler.namespaceAfterStartElement(getNodeNameX(node), getNodeValue(node));
                    break;
            }
        } catch (Exception e2) {
            throw new TransletException(e2);
        }
    }

    private void copyPI(int node, SerializationHandler handler) throws TransletException {
        String target = getNodeName(node);
        String value = getStringValueX(node);
        try {
            handler.processingInstruction(target, value);
        } catch (Exception e2) {
            throw new TransletException(e2);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
        int nodeID = makeNodeIdentity(node);
        int exptype = _exptype2(nodeID);
        int type = _exptype2Type(exptype);
        try {
            switch (type) {
                case 0:
                case 9:
                    return "";
                case 1:
                    String name = copyElement(nodeID, exptype, handler);
                    copyNS(nodeID, handler, true);
                    return name;
                case 2:
                    copyAttribute(nodeID, exptype, handler);
                    return null;
                case 3:
                    copyTextNode(nodeID, handler);
                    return null;
                case 4:
                case 5:
                case 6:
                case 10:
                case 11:
                case 12:
                default:
                    String uri1 = getNamespaceName(node);
                    if (uri1.length() != 0) {
                        String prefix = getPrefix(node);
                        handler.namespaceAfterStartElement(prefix, uri1);
                    }
                    handler.addAttribute(getNodeName(node), getNodeValue(node));
                    return null;
                case 7:
                    copyPI(node, handler);
                    return null;
                case 8:
                    handler.comment(getStringValueX(node));
                    return null;
                case 13:
                    handler.namespaceAfterStartElement(getNodeNameX(node), getNodeValue(node));
                    return null;
            }
        } catch (Exception e2) {
            throw new TransletException(e2);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getLanguage(int node) {
        int langAttr;
        int parent = node;
        while (true) {
            int parent2 = parent;
            if (-1 != parent2) {
                if (1 == getNodeType(parent2) && -1 != (langAttr = getAttributeNode(parent2, "http://www.w3.org/XML/1998/namespace", "lang"))) {
                    return getNodeValue(langAttr);
                }
                parent = getParent(parent2);
            } else {
                return null;
            }
        }
    }

    public DOMBuilder getBuilder() {
        return this;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public SerializationHandler getOutputDomBuilder() {
        return new ToXMLSAXHandler(this, "UTF-8");
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DOM getResultTreeFrag(int initSize, int rtfType) {
        return getResultTreeFrag(initSize, rtfType, true);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager) {
        if (rtfType == 0) {
            if (addToManager) {
                int dtmPos = this._dtmManager.getFirstFreeDTMID();
                SimpleResultTreeImpl rtf = new SimpleResultTreeImpl(this._dtmManager, dtmPos << 16);
                this._dtmManager.addDTM(rtf, dtmPos, 0);
                return rtf;
            }
            return new SimpleResultTreeImpl(this._dtmManager, 0);
        }
        if (rtfType == 1) {
            if (addToManager) {
                int dtmPos2 = this._dtmManager.getFirstFreeDTMID();
                AdaptiveResultTreeImpl rtf2 = new AdaptiveResultTreeImpl(this._dtmManager, dtmPos2 << 16, this.m_wsfilter, initSize, this.m_buildIdIndex);
                this._dtmManager.addDTM(rtf2, dtmPos2, 0);
                return rtf2;
            }
            return new AdaptiveResultTreeImpl(this._dtmManager, 0, this.m_wsfilter, initSize, this.m_buildIdIndex);
        }
        return (DOM) this._dtmManager.getDTM(null, true, this.m_wsfilter, true, false, false, initSize, this.m_buildIdIndex);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public Map<String, Integer> getElementsWithIDs() {
        return this.m_idAttributes;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getUnparsedEntityURI(String name) {
        if (this._document != null) {
            String uri = "";
            DocumentType doctype = this._document.getDoctype();
            if (doctype != null) {
                NamedNodeMap entities = doctype.getEntities();
                if (entities == null) {
                    return uri;
                }
                Entity entity = (Entity) entities.getNamedItem(name);
                if (entity == null) {
                    return uri;
                }
                String notationName = entity.getNotationName();
                if (notationName != null) {
                    uri = entity.getSystemId();
                    if (uri == null) {
                        uri = entity.getPublicId();
                    }
                }
            }
            return uri;
        }
        return super.getUnparsedEntityURI(name);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void release() {
        this._dtmManager.release(this, true);
    }
}
