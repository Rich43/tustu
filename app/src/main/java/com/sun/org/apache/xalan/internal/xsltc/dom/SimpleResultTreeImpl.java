package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
import com.sun.org.apache.xml.internal.serializer.EmptySerializer;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xml.internal.utils.XMLStringDefault;
import com.sun.org.apache.xpath.internal.compiler.PsuedoNames;
import java.util.Map;
import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SimpleResultTreeImpl.class */
public class SimpleResultTreeImpl extends EmptySerializer implements DOM, DTM {
    public static final int RTF_ROOT = 0;
    public static final int RTF_TEXT = 1;
    public static final int NUMBER_OF_NODES = 2;
    private static final String EMPTY_STR = "";
    private String _text;
    protected XSLTCDTMManager _dtmManager;
    private int _documentID;
    private static final DTMAxisIterator EMPTY_ITERATOR = new DTMAxisIteratorBase() { // from class: com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl.1
        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getLast() {
            return 0;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getPosition() {
            return 0;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator cloneIterator() {
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setRestartable(boolean isRestartable) {
        }
    };
    private static int _documentURIIndex = 0;
    protected int _size = 0;
    private BitArray _dontEscape = null;
    private boolean _escaping = true;
    protected String[] _textArray = new String[4];

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SimpleResultTreeImpl$SimpleIterator.class */
    public final class SimpleIterator extends DTMAxisIteratorBase {
        static final int DIRECTION_UP = 0;
        static final int DIRECTION_DOWN = 1;
        static final int NO_TYPE = -1;
        int _direction;
        int _type;
        int _currentNode;

        public SimpleIterator() {
            this._direction = 1;
            this._type = -1;
        }

        public SimpleIterator(int direction) {
            this._direction = 1;
            this._type = -1;
            this._direction = direction;
        }

        public SimpleIterator(int direction, int type) {
            this._direction = 1;
            this._type = -1;
            this._direction = direction;
            this._type = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._direction == 1) {
                while (this._currentNode < 2) {
                    if (this._type != -1) {
                        if ((this._currentNode == 0 && this._type == 0) || (this._currentNode == 1 && this._type == 3)) {
                            SimpleResultTreeImpl simpleResultTreeImpl = SimpleResultTreeImpl.this;
                            int i2 = this._currentNode;
                            this._currentNode = i2 + 1;
                            return returnNode(simpleResultTreeImpl.getNodeHandle(i2));
                        }
                        this._currentNode++;
                    } else {
                        SimpleResultTreeImpl simpleResultTreeImpl2 = SimpleResultTreeImpl.this;
                        int i3 = this._currentNode;
                        this._currentNode = i3 + 1;
                        return returnNode(simpleResultTreeImpl2.getNodeHandle(i3));
                    }
                }
                return -1;
            }
            while (this._currentNode >= 0) {
                if (this._type != -1) {
                    if ((this._currentNode == 0 && this._type == 0) || (this._currentNode == 1 && this._type == 3)) {
                        SimpleResultTreeImpl simpleResultTreeImpl3 = SimpleResultTreeImpl.this;
                        int i4 = this._currentNode;
                        this._currentNode = i4 - 1;
                        return returnNode(simpleResultTreeImpl3.getNodeHandle(i4));
                    }
                    this._currentNode--;
                } else {
                    SimpleResultTreeImpl simpleResultTreeImpl4 = SimpleResultTreeImpl.this;
                    int i5 = this._currentNode;
                    this._currentNode = i5 - 1;
                    return returnNode(simpleResultTreeImpl4.getNodeHandle(i5));
                }
            }
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int nodeHandle) {
            int nodeID = SimpleResultTreeImpl.this.getNodeIdent(nodeHandle);
            this._startNode = nodeID;
            if (!this._includeSelf && nodeID != -1) {
                if (this._direction == 1) {
                    nodeID++;
                } else if (this._direction == 0) {
                    nodeID--;
                }
            }
            this._currentNode = nodeID;
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            this._markedNode = this._currentNode;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            this._currentNode = this._markedNode;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SimpleResultTreeImpl$SingletonIterator.class */
    public final class SingletonIterator extends DTMAxisIteratorBase {
        static final int NO_TYPE = -1;
        int _type;
        int _currentNode;

        public SingletonIterator() {
            this._type = -1;
        }

        public SingletonIterator(int type) {
            this._type = -1;
            this._type = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            this._markedNode = this._currentNode;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            this._currentNode = this._markedNode;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int nodeHandle) {
            int nodeIdent = SimpleResultTreeImpl.this.getNodeIdent(nodeHandle);
            this._startNode = nodeIdent;
            this._currentNode = nodeIdent;
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._currentNode == -1) {
                return -1;
            }
            this._currentNode = -1;
            if (this._type != -1) {
                if ((this._currentNode == 0 && this._type == 0) || (this._currentNode == 1 && this._type == 3)) {
                    return SimpleResultTreeImpl.this.getNodeHandle(this._currentNode);
                }
                return -1;
            }
            return SimpleResultTreeImpl.this.getNodeHandle(this._currentNode);
        }
    }

    public SimpleResultTreeImpl(XSLTCDTMManager dtmManager, int documentID) {
        this._dtmManager = dtmManager;
        this._documentID = documentID;
    }

    public DTMManagerDefault getDTMManager() {
        return this._dtmManager;
    }

    public int getDocument() {
        return this._documentID;
    }

    public String getStringValue() {
        return this._text;
    }

    public DTMAxisIterator getIterator() {
        return new SingletonIterator(getDocument());
    }

    public DTMAxisIterator getChildren(int node) {
        return new SimpleIterator().setStartNode(node);
    }

    public DTMAxisIterator getTypedChildren(int type) {
        return new SimpleIterator(1, type);
    }

    public DTMAxisIterator getAxisIterator(int axis) {
        switch (axis) {
            case 0:
            case 10:
                return new SimpleIterator(0);
            case 1:
                return new SimpleIterator(0).includeSelf();
            case 2:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            default:
                return EMPTY_ITERATOR;
            case 3:
            case 4:
                return new SimpleIterator(1);
            case 5:
                return new SimpleIterator(1).includeSelf();
            case 13:
                return new SingletonIterator();
        }
    }

    public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
        switch (axis) {
            case 0:
            case 10:
                return new SimpleIterator(0, type);
            case 1:
                return new SimpleIterator(0, type).includeSelf();
            case 2:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            default:
                return EMPTY_ITERATOR;
            case 3:
            case 4:
                return new SimpleIterator(1, type);
            case 5:
                return new SimpleIterator(1, type).includeSelf();
            case 13:
                return new SingletonIterator(type);
        }
    }

    public DTMAxisIterator getNthDescendant(int node, int n2, boolean includeself) {
        return null;
    }

    public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
        return null;
    }

    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iter, int returnType, String value, boolean op) {
        return null;
    }

    public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
        return source;
    }

    public String getNodeName(int node) {
        if (getNodeIdent(node) == 1) {
            return PsuedoNames.PSEUDONAME_TEXT;
        }
        return "";
    }

    public String getNodeNameX(int node) {
        return "";
    }

    public String getNamespaceName(int node) {
        return "";
    }

    public int getExpandedTypeID(int nodeHandle) {
        int nodeID = getNodeIdent(nodeHandle);
        if (nodeID == 1) {
            return 3;
        }
        if (nodeID == 0) {
            return 0;
        }
        return -1;
    }

    public int getNamespaceType(int node) {
        return 0;
    }

    public int getParent(int nodeHandle) {
        int nodeID = getNodeIdent(nodeHandle);
        if (nodeID == 1) {
            return getNodeHandle(0);
        }
        return -1;
    }

    public int getAttributeNode(int gType, int element) {
        return -1;
    }

    public String getStringValueX(int nodeHandle) {
        int nodeID = getNodeIdent(nodeHandle);
        if (nodeID == 0 || nodeID == 1) {
            return this._text;
        }
        return "";
    }

    public void copy(int node, SerializationHandler handler) throws TransletException {
        characters(node, handler);
    }

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

    public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
        characters(node, handler);
        return null;
    }

    public boolean lessThan(int node1, int node2) {
        if (node1 == -1) {
            return false;
        }
        return node2 == -1 || node1 < node2;
    }

    public void characters(int node, SerializationHandler handler) throws TransletException {
        int nodeID = getNodeIdent(node);
        if (nodeID == 0 || nodeID == 1) {
            boolean escapeBit = false;
            boolean oldEscapeSetting = false;
            for (int i2 = 0; i2 < this._size; i2++) {
                try {
                    if (this._dontEscape != null) {
                        escapeBit = this._dontEscape.getBit(i2);
                        if (escapeBit) {
                            oldEscapeSetting = handler.setEscaping(false);
                        }
                    }
                    handler.characters(this._textArray[i2]);
                    if (escapeBit) {
                        handler.setEscaping(oldEscapeSetting);
                    }
                } catch (SAXException e2) {
                    throw new TransletException(e2);
                }
            }
        }
    }

    public Node makeNode(int index) {
        return null;
    }

    public Node makeNode(DTMAxisIterator iter) {
        return null;
    }

    public NodeList makeNodeList(int index) {
        return null;
    }

    public NodeList makeNodeList(DTMAxisIterator iter) {
        return null;
    }

    public String getLanguage(int node) {
        return null;
    }

    public int getSize() {
        return 2;
    }

    public String getDocumentURI(int node) {
        StringBuilder sbAppend = new StringBuilder().append("simple_rtf");
        int i2 = _documentURIIndex;
        _documentURIIndex = i2 + 1;
        return sbAppend.append(i2).toString();
    }

    public void setFilter(StripFilter filter) {
    }

    public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces) {
    }

    public boolean isElement(int node) {
        return false;
    }

    public boolean isAttribute(int node) {
        return false;
    }

    public String lookupNamespace(int node, String prefix) throws TransletException {
        return null;
    }

    public int getNodeIdent(int nodehandle) {
        if (nodehandle != -1) {
            return nodehandle - this._documentID;
        }
        return -1;
    }

    public int getNodeHandle(int nodeId) {
        if (nodeId != -1) {
            return nodeId + this._documentID;
        }
        return -1;
    }

    public DOM getResultTreeFrag(int initialSize, int rtfType) {
        return null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DOM getResultTreeFrag(int initialSize, int rtfType, boolean addToManager) {
        return null;
    }

    public SerializationHandler getOutputDomBuilder() {
        return this;
    }

    public int getNSType(int node) {
        return 0;
    }

    public String getUnparsedEntityURI(String name) {
        return null;
    }

    public Map<String, Integer> getElementsWithIDs() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        if (this._size == 1) {
            this._text = this._textArray[0];
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i2 = 0; i2 < this._size; i2++) {
            buffer.append(this._textArray[i2]);
        }
        this._text = buffer.toString();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String str) throws SAXException {
        if (this._size >= this._textArray.length) {
            String[] newTextArray = new String[this._textArray.length * 2];
            System.arraycopy(this._textArray, 0, newTextArray, 0, this._textArray.length);
            this._textArray = newTextArray;
        }
        if (!this._escaping) {
            if (this._dontEscape == null) {
                this._dontEscape = new BitArray(8);
            }
            if (this._size >= this._dontEscape.size()) {
                this._dontEscape.resize(this._dontEscape.size() * 2);
            }
            this._dontEscape.setBit(this._size);
        }
        String[] strArr = this._textArray;
        int i2 = this._size;
        this._size = i2 + 1;
        strArr[i2] = str;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void characters(char[] ch, int offset, int length) throws SAXException {
        if (this._size >= this._textArray.length) {
            String[] newTextArray = new String[this._textArray.length * 2];
            System.arraycopy(this._textArray, 0, newTextArray, 0, this._textArray.length);
            this._textArray = newTextArray;
        }
        if (!this._escaping) {
            if (this._dontEscape == null) {
                this._dontEscape = new BitArray(8);
            }
            if (this._size >= this._dontEscape.size()) {
                this._dontEscape.resize(this._dontEscape.size() * 2);
            }
            this._dontEscape.setBit(this._size);
        }
        String[] strArr = this._textArray;
        int i2 = this._size;
        this._size = i2 + 1;
        strArr[i2] = new String(ch, offset, length);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public boolean setEscaping(boolean escape) throws SAXException {
        boolean temp = this._escaping;
        this._escaping = escape;
        return temp;
    }

    public void setFeature(String featureId, boolean state) {
    }

    public void setProperty(String property, Object value) {
    }

    public DTMAxisTraverser getAxisTraverser(int axis) {
        return null;
    }

    public boolean hasChildNodes(int nodeHandle) {
        return getNodeIdent(nodeHandle) == 0;
    }

    public int getFirstChild(int nodeHandle) {
        int nodeID = getNodeIdent(nodeHandle);
        if (nodeID == 0) {
            return getNodeHandle(1);
        }
        return -1;
    }

    public int getLastChild(int nodeHandle) {
        return getFirstChild(nodeHandle);
    }

    public int getAttributeNode(int elementHandle, String namespaceURI, String name) {
        return -1;
    }

    public int getFirstAttribute(int nodeHandle) {
        return -1;
    }

    public int getFirstNamespaceNode(int nodeHandle, boolean inScope) {
        return -1;
    }

    public int getNextSibling(int nodeHandle) {
        return -1;
    }

    public int getPreviousSibling(int nodeHandle) {
        return -1;
    }

    public int getNextAttribute(int nodeHandle) {
        return -1;
    }

    public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope) {
        return -1;
    }

    public int getOwnerDocument(int nodeHandle) {
        return getDocument();
    }

    public int getDocumentRoot(int nodeHandle) {
        return getDocument();
    }

    public XMLString getStringValue(int nodeHandle) {
        return new XMLStringDefault(getStringValueX(nodeHandle));
    }

    public int getStringValueChunkCount(int nodeHandle) {
        return 0;
    }

    public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen) {
        return null;
    }

    public int getExpandedTypeID(String namespace, String localName, int type) {
        return -1;
    }

    public String getLocalNameFromExpandedNameID(int ExpandedNameID) {
        return "";
    }

    public String getNamespaceFromExpandedNameID(int ExpandedNameID) {
        return "";
    }

    public String getLocalName(int nodeHandle) {
        return "";
    }

    public String getPrefix(int nodeHandle) {
        return null;
    }

    public String getNamespaceURI(int nodeHandle) {
        return "";
    }

    public String getNodeValue(int nodeHandle) {
        if (getNodeIdent(nodeHandle) == 1) {
            return this._text;
        }
        return null;
    }

    public short getNodeType(int nodeHandle) {
        int nodeID = getNodeIdent(nodeHandle);
        if (nodeID == 1) {
            return (short) 3;
        }
        if (nodeID == 0) {
            return (short) 0;
        }
        return (short) -1;
    }

    public short getLevel(int nodeHandle) {
        int nodeID = getNodeIdent(nodeHandle);
        if (nodeID == 1) {
            return (short) 2;
        }
        if (nodeID == 0) {
            return (short) 1;
        }
        return (short) -1;
    }

    public boolean isSupported(String feature, String version) {
        return false;
    }

    public String getDocumentBaseURI() {
        return "";
    }

    public void setDocumentBaseURI(String baseURI) {
    }

    public String getDocumentSystemIdentifier(int nodeHandle) {
        return null;
    }

    public String getDocumentEncoding(int nodeHandle) {
        return null;
    }

    public String getDocumentStandalone(int nodeHandle) {
        return null;
    }

    public String getDocumentVersion(int documentHandle) {
        return null;
    }

    public boolean getDocumentAllDeclarationsProcessed() {
        return false;
    }

    public String getDocumentTypeDeclarationSystemIdentifier() {
        return null;
    }

    public String getDocumentTypeDeclarationPublicIdentifier() {
        return null;
    }

    public int getElementById(String elementId) {
        return -1;
    }

    public boolean supportsPreStripping() {
        return false;
    }

    public boolean isNodeAfter(int firstNodeHandle, int secondNodeHandle) {
        return lessThan(firstNodeHandle, secondNodeHandle);
    }

    public boolean isCharacterElementContentWhitespace(int nodeHandle) {
        return false;
    }

    public boolean isDocumentAllDeclarationsProcessed(int documentHandle) {
        return false;
    }

    public boolean isAttributeSpecified(int attributeHandle) {
        return false;
    }

    public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException {
    }

    public void dispatchToEvents(int nodeHandle, ContentHandler ch) throws SAXException {
    }

    public Node getNode(int nodeHandle) {
        return makeNode(nodeHandle);
    }

    public boolean needsTwoThreads() {
        return false;
    }

    public ContentHandler getContentHandler() {
        return null;
    }

    public LexicalHandler getLexicalHandler() {
        return null;
    }

    public EntityResolver getEntityResolver() {
        return null;
    }

    public DTDHandler getDTDHandler() {
        return null;
    }

    public ErrorHandler getErrorHandler() {
        return null;
    }

    public DeclHandler getDeclHandler() {
        return null;
    }

    public void appendChild(int newChild, boolean clone, boolean cloneDepth) {
    }

    public void appendTextChild(String str) {
    }

    public SourceLocator getSourceLocatorFor(int node) {
        return null;
    }

    public void documentRegistration() {
    }

    public void documentRelease() {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void migrateTo(DTMManager manager) {
    }

    public void release() {
        if (this._documentID != 0) {
            this._dtmManager.release(this, true);
            this._documentID = 0;
        }
    }
}
