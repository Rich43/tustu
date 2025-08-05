package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import com.sun.org.apache.xpath.internal.compiler.PsuedoNames;
import javax.xml.transform.SourceLocator;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDocumentImpl.class */
public class DTMDocumentImpl implements DTM, ContentHandler, LexicalHandler {
    protected static final byte DOCHANDLE_SHIFT = 22;
    protected static final int NODEHANDLE_MASK = 8388607;
    protected static final int DOCHANDLE_MASK = -8388608;
    protected String m_documentBaseURI;
    private XMLStringFactory m_xsf;
    private static final String[] fixednames = {null, null, null, PsuedoNames.PSEUDONAME_TEXT, "#cdata_section", null, null, null, PsuedoNames.PSEUDONAME_COMMENT, "#document", null, "#document-fragment", null};
    int m_docHandle = -1;
    int m_docElement = -1;
    int currentParent = 0;
    int previousSibling = 0;
    protected int m_currentNode = -1;
    private boolean previousSiblingWasParent = false;
    int[] gotslot = new int[4];
    private boolean done = false;
    boolean m_isError = false;
    private final boolean DEBUG = false;
    private IncrementalSAXSource m_incrSAXSource = null;
    ChunkedIntArray nodes = new ChunkedIntArray(4);
    private FastStringBuffer m_char = new FastStringBuffer();
    private int m_char_current_start = 0;
    private DTMStringPool m_localNames = new DTMStringPool();
    private DTMStringPool m_nsNames = new DTMStringPool();
    private DTMStringPool m_prefixNames = new DTMStringPool();
    private ExpandedNameTable m_expandedNames = new ExpandedNameTable();

    public DTMDocumentImpl(DTMManager mgr, int documentNumber, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory) {
        initDocument(documentNumber);
        this.m_xsf = xstringfactory;
    }

    public void setIncrementalSAXSource(IncrementalSAXSource source) {
        this.m_incrSAXSource = source;
        source.setContentHandler(this);
        source.setLexicalHandler(this);
    }

    private final int appendNode(int w0, int w1, int w2, int w3) throws ArrayIndexOutOfBoundsException {
        int slotnumber = this.nodes.appendSlot(w0, w1, w2, w3);
        if (this.previousSiblingWasParent) {
            this.nodes.writeEntry(this.previousSibling, 2, slotnumber);
        }
        this.previousSiblingWasParent = false;
        return slotnumber;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void setFeature(String featureId, boolean state) {
    }

    public void setLocalNameTable(DTMStringPool poolRef) {
        this.m_localNames = poolRef;
    }

    public DTMStringPool getLocalNameTable() {
        return this.m_localNames;
    }

    public void setNsNameTable(DTMStringPool poolRef) {
        this.m_nsNames = poolRef;
    }

    public DTMStringPool getNsNameTable() {
        return this.m_nsNames;
    }

    public void setPrefixNameTable(DTMStringPool poolRef) {
        this.m_prefixNames = poolRef;
    }

    public DTMStringPool getPrefixNameTable() {
        return this.m_prefixNames;
    }

    void setContentBuffer(FastStringBuffer buffer) {
        this.m_char = buffer;
    }

    FastStringBuffer getContentBuffer() {
        return this.m_char;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public ContentHandler getContentHandler() {
        if (this.m_incrSAXSource instanceof IncrementalSAXSource_Filter) {
            return (ContentHandler) this.m_incrSAXSource;
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public LexicalHandler getLexicalHandler() {
        if (this.m_incrSAXSource instanceof IncrementalSAXSource_Filter) {
            return (LexicalHandler) this.m_incrSAXSource;
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DeclHandler getDeclHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean needsTwoThreads() {
        return null != this.m_incrSAXSource;
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.m_char.append(ch, start, length);
    }

    private void processAccumulatedText() throws ArrayIndexOutOfBoundsException {
        int len = this.m_char.length();
        if (len != this.m_char_current_start) {
            appendTextChild(this.m_char_current_start, len - this.m_char_current_start);
            this.m_char_current_start = len;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        appendEndDocument();
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException, ArrayIndexOutOfBoundsException {
        processAccumulatedText();
        appendEndElement();
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException, ArrayIndexOutOfBoundsException {
        processAccumulatedText();
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException, ArrayIndexOutOfBoundsException {
        processAccumulatedText();
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        appendStartDocument();
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException, ArrayIndexOutOfBoundsException {
        String prefix;
        String localName2;
        String prefix2;
        processAccumulatedText();
        String prefix3 = null;
        int colon = qName.indexOf(58);
        if (colon > 0) {
            prefix3 = qName.substring(0, colon);
        }
        System.out.println("Prefix=" + prefix3 + " index=" + this.m_prefixNames.stringToIndex(prefix3));
        appendStartElement(this.m_nsNames.stringToIndex(namespaceURI), this.m_localNames.stringToIndex(localName), this.m_prefixNames.stringToIndex(prefix3));
        int nAtts = atts == null ? 0 : atts.getLength();
        for (int i2 = nAtts - 1; i2 >= 0; i2--) {
            String qName2 = atts.getQName(i2);
            if (qName2.startsWith("xmlns:") || "xmlns".equals(qName2)) {
                int colon2 = qName2.indexOf(58);
                if (colon2 > 0) {
                    prefix2 = qName2.substring(0, colon2);
                } else {
                    prefix2 = null;
                }
                appendNSDeclaration(this.m_prefixNames.stringToIndex(prefix2), this.m_nsNames.stringToIndex(atts.getValue(i2)), atts.getType(i2).equalsIgnoreCase("ID"));
            }
        }
        for (int i3 = nAtts - 1; i3 >= 0; i3--) {
            String qName3 = atts.getQName(i3);
            if (!qName3.startsWith("xmlns:") && !"xmlns".equals(qName3)) {
                int colon3 = qName3.indexOf(58);
                if (colon3 > 0) {
                    prefix = qName3.substring(0, colon3);
                    localName2 = qName3.substring(colon3 + 1);
                } else {
                    prefix = "";
                    localName2 = qName3;
                }
                this.m_char.append(atts.getValue(i3));
                int contentEnd = this.m_char.length();
                if (!"xmlns".equals(prefix) && !"xmlns".equals(qName3)) {
                    appendAttribute(this.m_nsNames.stringToIndex(atts.getURI(i3)), this.m_localNames.stringToIndex(localName2), this.m_prefixNames.stringToIndex(prefix), atts.getType(i3).equalsIgnoreCase("ID"), this.m_char_current_start, contentEnd - this.m_char_current_start);
                }
                this.m_char_current_start = contentEnd;
            }
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException, ArrayIndexOutOfBoundsException {
        processAccumulatedText();
        this.m_char.append(ch, start, length);
        appendComment(this.m_char_current_start, length);
        this.m_char_current_start += length;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }

    final void initDocument(int documentNumber) {
        this.m_docHandle = documentNumber << 22;
        this.nodes.writeSlot(0, 9, -1, -1, 0);
        this.done = false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean hasChildNodes(int nodeHandle) {
        return getFirstChild(nodeHandle) != -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getFirstChild(int nodeHandle) {
        int nodeHandle2 = nodeHandle & 8388607;
        this.nodes.readSlot(nodeHandle2, this.gotslot);
        short type = (short) (this.gotslot[0] & 65535);
        if (type == 1 || type == 9 || type == 5) {
            int kid = nodeHandle2 + 1;
            this.nodes.readSlot(kid, this.gotslot);
            while (2 == (this.gotslot[0] & 65535)) {
                kid = this.gotslot[2];
                if (kid == -1) {
                    return -1;
                }
                this.nodes.readSlot(kid, this.gotslot);
            }
            if (this.gotslot[1] == nodeHandle2) {
                int firstChild = kid | this.m_docHandle;
                return firstChild;
            }
            return -1;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getLastChild(int nodeHandle) throws ArrayIndexOutOfBoundsException {
        int lastChild = -1;
        int firstChild = getFirstChild(nodeHandle & 8388607);
        while (true) {
            int nextkid = firstChild;
            if (nextkid != -1) {
                lastChild = nextkid;
                firstChild = getNextSibling(nextkid);
            } else {
                return lastChild | this.m_docHandle;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getAttributeNode(int nodeHandle, String namespaceURI, String name) {
        int nsIndex = this.m_nsNames.stringToIndex(namespaceURI);
        int nameIndex = this.m_localNames.stringToIndex(name);
        int nodeHandle2 = nodeHandle & 8388607;
        this.nodes.readSlot(nodeHandle2, this.gotslot);
        short type = (short) (this.gotslot[0] & 65535);
        if (type == 1) {
            nodeHandle2++;
        }
        while (type == 2) {
            if (nsIndex == (this.gotslot[0] << 16) && this.gotslot[3] == nameIndex) {
                return nodeHandle2 | this.m_docHandle;
            }
            nodeHandle2 = this.gotslot[2];
            this.nodes.readSlot(nodeHandle2, this.gotslot);
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getFirstAttribute(int nodeHandle) {
        int nodeHandle2 = nodeHandle & 8388607;
        if (1 != (this.nodes.readEntry(nodeHandle2, 0) & 65535)) {
            return -1;
        }
        int nodeHandle3 = nodeHandle2 + 1;
        if (2 == (this.nodes.readEntry(nodeHandle3, 0) & 65535)) {
            return nodeHandle3 | this.m_docHandle;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getFirstNamespaceNode(int nodeHandle, boolean inScope) {
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getNextSibling(int nodeHandle) throws ArrayIndexOutOfBoundsException {
        int nodeHandle2 = nodeHandle & 8388607;
        if (nodeHandle2 == 0) {
            return -1;
        }
        short type = (short) (this.nodes.readEntry(nodeHandle2, 0) & 65535);
        if (type == 1 || type == 2 || type == 5) {
            int nextSib = this.nodes.readEntry(nodeHandle2, 2);
            if (nextSib == -1) {
                return -1;
            }
            if (nextSib != 0) {
                return this.m_docHandle | nextSib;
            }
        }
        int thisParent = this.nodes.readEntry(nodeHandle2, 1);
        int nodeHandle3 = nodeHandle2 + 1;
        if (this.nodes.readEntry(nodeHandle3, 1) == thisParent) {
            return this.m_docHandle | nodeHandle3;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getPreviousSibling(int nodeHandle) throws ArrayIndexOutOfBoundsException {
        int nodeHandle2 = nodeHandle & 8388607;
        if (nodeHandle2 == 0) {
            return -1;
        }
        int parent = this.nodes.readEntry(nodeHandle2, 1);
        int kid = -1;
        int firstChild = getFirstChild(parent);
        while (true) {
            int nextkid = firstChild;
            if (nextkid != nodeHandle2) {
                kid = nextkid;
                firstChild = getNextSibling(nextkid);
            } else {
                return kid | this.m_docHandle;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getNextAttribute(int nodeHandle) {
        int nodeHandle2 = nodeHandle & 8388607;
        this.nodes.readSlot(nodeHandle2, this.gotslot);
        short type = (short) (this.gotslot[0] & 65535);
        if (type == 1) {
            return getFirstAttribute(nodeHandle2);
        }
        if (type == 2 && this.gotslot[2] != -1) {
            return this.m_docHandle | this.gotslot[2];
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope) {
        return -1;
    }

    public int getNextDescendant(int subtreeRootHandle, int nodeHandle) {
        int subtreeRootHandle2 = subtreeRootHandle & 8388607;
        int nodeHandle2 = nodeHandle & 8388607;
        if (nodeHandle2 == 0) {
            return -1;
        }
        while (!this.m_isError) {
            if (!this.done || nodeHandle2 <= this.nodes.slotsUsed()) {
                if (nodeHandle2 > subtreeRootHandle2) {
                    this.nodes.readSlot(nodeHandle2 + 1, this.gotslot);
                    if (this.gotslot[2] != 0) {
                        short type = (short) (this.gotslot[0] & 65535);
                        if (type == 2) {
                            nodeHandle2 += 2;
                        } else {
                            int nextParentPos = this.gotslot[1];
                            if (nextParentPos >= subtreeRootHandle2) {
                                return this.m_docHandle | (nodeHandle2 + 1);
                            }
                            return -1;
                        }
                    } else if (this.done) {
                        return -1;
                    }
                } else {
                    nodeHandle2++;
                }
            } else {
                return -1;
            }
        }
        return -1;
    }

    public int getNextFollowing(int axisContextHandle, int nodeHandle) {
        return -1;
    }

    public int getNextPreceding(int axisContextHandle, int nodeHandle) {
        int nodeHandle2 = nodeHandle & 8388607;
        while (nodeHandle2 > 1) {
            nodeHandle2--;
            if (2 != (this.nodes.readEntry(nodeHandle2, 0) & 65535)) {
                return this.m_docHandle | this.nodes.specialFind(axisContextHandle, nodeHandle2);
            }
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getParent(int nodeHandle) {
        return this.m_docHandle | this.nodes.readEntry(nodeHandle, 1);
    }

    public int getDocumentRoot() {
        return this.m_docHandle | this.m_docElement;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getDocument() {
        return this.m_docHandle;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getOwnerDocument(int nodeHandle) {
        if ((nodeHandle & 8388607) == 0) {
            return -1;
        }
        return nodeHandle & DOCHANDLE_MASK;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getDocumentRoot(int nodeHandle) {
        if ((nodeHandle & 8388607) == 0) {
            return -1;
        }
        return nodeHandle & DOCHANDLE_MASK;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public XMLString getStringValue(int nodeHandle) {
        this.nodes.readSlot(nodeHandle, this.gotslot);
        int nodetype = this.gotslot[0] & 255;
        String value = null;
        switch (nodetype) {
            case 3:
            case 4:
            case 8:
                value = this.m_char.getString(this.gotslot[2], this.gotslot[3]);
                break;
        }
        return this.m_xsf.newstr(value);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getStringValueChunkCount(int nodeHandle) {
        return 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen) {
        return new char[0];
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getExpandedTypeID(int nodeHandle) throws ArrayIndexOutOfBoundsException {
        this.nodes.readSlot(nodeHandle, this.gotslot);
        String qName = this.m_localNames.indexToString(this.gotslot[3]);
        int colonpos = qName.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        String localName = qName.substring(colonpos + 1);
        String namespace = this.m_nsNames.indexToString(this.gotslot[0] << 16);
        String expandedName = namespace + CallSiteDescriptor.TOKEN_DELIMITER + localName;
        int expandedNameID = this.m_nsNames.stringToIndex(expandedName);
        return expandedNameID;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getExpandedTypeID(String namespace, String localName, int type) {
        String expandedName = namespace + CallSiteDescriptor.TOKEN_DELIMITER + localName;
        int expandedNameID = this.m_nsNames.stringToIndex(expandedName);
        return expandedNameID;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getLocalNameFromExpandedNameID(int ExpandedNameID) throws ArrayIndexOutOfBoundsException {
        String expandedName = this.m_localNames.indexToString(ExpandedNameID);
        int colonpos = expandedName.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        String localName = expandedName.substring(colonpos + 1);
        return localName;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getNamespaceFromExpandedNameID(int ExpandedNameID) throws ArrayIndexOutOfBoundsException {
        String expandedName = this.m_localNames.indexToString(ExpandedNameID);
        int colonpos = expandedName.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        String nsName = expandedName.substring(0, colonpos);
        return nsName;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeName(int nodeHandle) throws ArrayIndexOutOfBoundsException {
        this.nodes.readSlot(nodeHandle, this.gotslot);
        short type = (short) (this.gotslot[0] & 65535);
        String name = fixednames[type];
        if (null == name) {
            int i2 = this.gotslot[3];
            System.out.println("got i=" + i2 + " " + (i2 >> 16) + "/" + (i2 & 65535));
            name = this.m_localNames.indexToString(i2 & 65535);
            String prefix = this.m_prefixNames.indexToString(i2 >> 16);
            if (prefix != null && prefix.length() > 0) {
                name = prefix + CallSiteDescriptor.TOKEN_DELIMITER + name;
            }
        }
        return name;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeNameX(int nodeHandle) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getLocalName(int nodeHandle) throws ArrayIndexOutOfBoundsException {
        this.nodes.readSlot(nodeHandle, this.gotslot);
        short type = (short) (this.gotslot[0] & 65535);
        String name = "";
        if (type == 1 || type == 2) {
            int i2 = this.gotslot[3];
            name = this.m_localNames.indexToString(i2 & 65535);
            if (name == null) {
                name = "";
            }
        }
        return name;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getPrefix(int nodeHandle) throws ArrayIndexOutOfBoundsException {
        this.nodes.readSlot(nodeHandle, this.gotslot);
        short type = (short) (this.gotslot[0] & 65535);
        String name = "";
        if (type == 1 || type == 2) {
            int i2 = this.gotslot[3];
            name = this.m_prefixNames.indexToString(i2 >> 16);
            if (name == null) {
                name = "";
            }
        }
        return name;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getNamespaceURI(int nodeHandle) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeValue(int nodeHandle) {
        this.nodes.readSlot(nodeHandle, this.gotslot);
        int nodetype = this.gotslot[0] & 255;
        String value = null;
        switch (nodetype) {
            case 2:
                this.nodes.readSlot(nodeHandle + 1, this.gotslot);
            case 3:
            case 4:
            case 8:
                value = this.m_char.getString(this.gotslot[2], this.gotslot[3]);
                break;
        }
        return value;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public short getNodeType(int nodeHandle) {
        return (short) (this.nodes.readEntry(nodeHandle, 0) & 65535);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public short getLevel(int nodeHandle) throws ArrayIndexOutOfBoundsException {
        short count = 0;
        while (nodeHandle != 0) {
            count = (short) (count + 1);
            nodeHandle = this.nodes.readEntry(nodeHandle, 1);
        }
        return count;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isSupported(String feature, String version) {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentBaseURI() {
        return this.m_documentBaseURI;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void setDocumentBaseURI(String baseURI) {
        this.m_documentBaseURI = baseURI;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentSystemIdentifier(int nodeHandle) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentEncoding(int nodeHandle) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentStandalone(int nodeHandle) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentVersion(int documentHandle) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean getDocumentAllDeclarationsProcessed() {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentTypeDeclarationSystemIdentifier() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentTypeDeclarationPublicIdentifier() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getElementById(String elementId) {
        return 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getUnparsedEntityURI(String name) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean supportsPreStripping() {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isNodeAfter(int nodeHandle1, int nodeHandle2) {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isCharacterElementContentWhitespace(int nodeHandle) {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isDocumentAllDeclarationsProcessed(int documentHandle) {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isAttributeSpecified(int attributeHandle) {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void dispatchToEvents(int nodeHandle, ContentHandler ch) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public Node getNode(int nodeHandle) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void appendChild(int newChild, boolean clone, boolean cloneDepth) {
        boolean sameDoc = (newChild & DOCHANDLE_MASK) == this.m_docHandle;
        if (clone || !sameDoc) {
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void appendTextChild(String str) {
    }

    void appendTextChild(int m_char_current_start, int contentLength) throws ArrayIndexOutOfBoundsException {
        int w1 = this.currentParent;
        int ourslot = appendNode(3, w1, m_char_current_start, contentLength);
        this.previousSibling = ourslot;
    }

    void appendComment(int m_char_current_start, int contentLength) throws ArrayIndexOutOfBoundsException {
        int w1 = this.currentParent;
        int ourslot = appendNode(8, w1, m_char_current_start, contentLength);
        this.previousSibling = ourslot;
    }

    void appendStartElement(int namespaceIndex, int localNameIndex, int prefixIndex) throws ArrayIndexOutOfBoundsException {
        int w0 = (namespaceIndex << 16) | 1;
        int w1 = this.currentParent;
        int w3 = localNameIndex | (prefixIndex << 16);
        System.out.println("set w3=" + w3 + " " + (w3 >> 16) + "/" + (w3 & 65535));
        int ourslot = appendNode(w0, w1, 0, w3);
        this.currentParent = ourslot;
        this.previousSibling = 0;
        if (this.m_docElement == -1) {
            this.m_docElement = ourslot;
        }
    }

    void appendNSDeclaration(int prefixIndex, int namespaceIndex, boolean isID) throws ArrayIndexOutOfBoundsException {
        this.m_nsNames.stringToIndex("http://www.w3.org/2000/xmlns/");
        int w0 = 13 | (this.m_nsNames.stringToIndex("http://www.w3.org/2000/xmlns/") << 16);
        int w1 = this.currentParent;
        int ourslot = appendNode(w0, w1, 0, namespaceIndex);
        this.previousSibling = ourslot;
        this.previousSiblingWasParent = false;
    }

    void appendAttribute(int namespaceIndex, int localNameIndex, int prefixIndex, boolean isID, int m_char_current_start, int contentLength) throws ArrayIndexOutOfBoundsException {
        int w0 = 2 | (namespaceIndex << 16);
        int w1 = this.currentParent;
        int w3 = localNameIndex | (prefixIndex << 16);
        System.out.println("set w3=" + w3 + " " + (w3 >> 16) + "/" + (w3 & 65535));
        int ourslot = appendNode(w0, w1, 0, w3);
        this.previousSibling = ourslot;
        appendNode(3, ourslot, m_char_current_start, contentLength);
        this.previousSiblingWasParent = true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisTraverser getAxisTraverser(int axis) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getAxisIterator(int axis) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
        return null;
    }

    void appendEndElement() throws ArrayIndexOutOfBoundsException {
        if (this.previousSiblingWasParent) {
            this.nodes.writeEntry(this.previousSibling, 2, -1);
        }
        this.previousSibling = this.currentParent;
        this.nodes.readSlot(this.currentParent, this.gotslot);
        this.currentParent = this.gotslot[1] & 65535;
        this.previousSiblingWasParent = true;
    }

    void appendStartDocument() {
        this.m_docElement = -1;
        initDocument(0);
    }

    void appendEndDocument() {
        this.done = true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void setProperty(String property, Object value) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public SourceLocator getSourceLocatorFor(int node) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void documentRegistration() {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void documentRelease() {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void migrateTo(DTMManager manager) {
    }
}
