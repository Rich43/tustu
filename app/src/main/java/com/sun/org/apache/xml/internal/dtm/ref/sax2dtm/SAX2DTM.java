package com.sun.org.apache.xml.internal.dtm.ref.sax2dtm;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
import com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool;
import com.sun.org.apache.xml.internal.dtm.ref.DTMTreeWalker;
import com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource;
import com.sun.org.apache.xml.internal.dtm.ref.NodeLocator;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import com.sun.org.apache.xml.internal.utils.IntStack;
import com.sun.org.apache.xml.internal.utils.IntVector;
import com.sun.org.apache.xml.internal.utils.StringVector;
import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import com.sun.org.apache.xpath.internal.compiler.PsuedoNames;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM.class */
public class SAX2DTM extends DTMDefaultBaseIterators implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler, DeclHandler, LexicalHandler {
    private static final boolean DEBUG = false;
    private IncrementalSAXSource m_incrementalSAXSource;
    protected FastStringBuffer m_chars;
    protected SuballocatedIntVector m_data;
    protected transient IntStack m_parents;
    protected transient int m_previous;
    protected transient Vector m_prefixMappings;
    protected transient IntStack m_contextIndexes;
    protected transient int m_textType;
    protected transient int m_coalescedTextType;
    protected transient Locator m_locator;
    private transient String m_systemId;
    protected transient boolean m_insideDTD;
    protected DTMTreeWalker m_walker;
    protected DTMStringPool m_valuesOrPrefixes;
    protected boolean m_endDocumentOccured;
    protected SuballocatedIntVector m_dataOrQName;
    protected Map<String, Integer> m_idAttributes;
    private static final String[] m_fixednames = {null, null, null, PsuedoNames.PSEUDONAME_TEXT, "#cdata_section", null, null, null, PsuedoNames.PSEUDONAME_COMMENT, "#document", null, "#document-fragment", null};
    private Vector m_entities;
    private static final int ENTITY_FIELD_PUBLICID = 0;
    private static final int ENTITY_FIELD_SYSTEMID = 1;
    private static final int ENTITY_FIELD_NOTATIONNAME = 2;
    private static final int ENTITY_FIELD_NAME = 3;
    private static final int ENTITY_FIELDS_PER = 4;
    protected int m_textPendingStart;
    protected boolean m_useSourceLocationProperty;
    protected StringVector m_sourceSystemId;
    protected IntVector m_sourceLine;
    protected IntVector m_sourceColumn;
    boolean m_pastFirstElement;

    public SAX2DTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
        this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, false);
    }

    public SAX2DTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable) {
        super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
        this.m_incrementalSAXSource = null;
        this.m_previous = 0;
        this.m_prefixMappings = new Vector();
        this.m_textType = 3;
        this.m_coalescedTextType = 3;
        this.m_locator = null;
        this.m_systemId = null;
        this.m_insideDTD = false;
        this.m_walker = new DTMTreeWalker();
        this.m_endDocumentOccured = false;
        this.m_idAttributes = new HashMap();
        this.m_entities = null;
        this.m_textPendingStart = -1;
        this.m_useSourceLocationProperty = false;
        this.m_pastFirstElement = false;
        if (blocksize <= 64) {
            this.m_data = new SuballocatedIntVector(blocksize, 4);
            this.m_dataOrQName = new SuballocatedIntVector(blocksize, 4);
            this.m_valuesOrPrefixes = new DTMStringPool(16);
            this.m_chars = new FastStringBuffer(7, 10);
            this.m_contextIndexes = new IntStack(4);
            this.m_parents = new IntStack(4);
        } else {
            this.m_data = new SuballocatedIntVector(blocksize, 32);
            this.m_dataOrQName = new SuballocatedIntVector(blocksize, 32);
            this.m_valuesOrPrefixes = new DTMStringPool();
            this.m_chars = new FastStringBuffer(10, 13);
            this.m_contextIndexes = new IntStack();
            this.m_parents = new IntStack();
        }
        this.m_data.addElement(0);
        this.m_useSourceLocationProperty = mgr.getSource_location();
        this.m_sourceSystemId = this.m_useSourceLocationProperty ? new StringVector() : null;
        this.m_sourceLine = this.m_useSourceLocationProperty ? new IntVector() : null;
        this.m_sourceColumn = this.m_useSourceLocationProperty ? new IntVector() : null;
    }

    public void setUseSourceLocation(boolean useSourceLocation) {
        this.m_useSourceLocationProperty = useSourceLocation;
    }

    /* JADX WARN: Incorrect condition in loop: B:7:0x0017 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected int _dataOrQName(int r4) {
        /*
            r3 = this;
            r0 = r4
            r1 = r3
            int r1 = r1.m_size
            if (r0 >= r1) goto L11
            r0 = r3
            com.sun.org.apache.xml.internal.utils.SuballocatedIntVector r0 = r0.m_dataOrQName
            r1 = r4
            int r0 = r0.elementAt(r1)
            return r0
        L11:
            r0 = r3
            boolean r0 = r0.nextNode()
            r5 = r0
            r0 = r5
            if (r0 != 0) goto L1c
            r0 = -1
            return r0
        L1c:
            r0 = r4
            r1 = r3
            int r1 = r1.m_size
            if (r0 >= r1) goto L2d
            r0 = r3
            com.sun.org.apache.xml.internal.utils.SuballocatedIntVector r0 = r0.m_dataOrQName
            r1 = r4
            int r0 = r0.elementAt(r1)
            return r0
        L2d:
            goto L11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM._dataOrQName(int):int");
    }

    public void clearCoRoutine() {
        clearCoRoutine(true);
    }

    public void clearCoRoutine(boolean callDoTerminate) {
        if (null != this.m_incrementalSAXSource) {
            if (callDoTerminate) {
                this.m_incrementalSAXSource.deliverMoreNodes(false);
            }
            this.m_incrementalSAXSource = null;
        }
    }

    public void setIncrementalSAXSource(IncrementalSAXSource incrementalSAXSource) {
        this.m_incrementalSAXSource = incrementalSAXSource;
        incrementalSAXSource.setContentHandler(this);
        incrementalSAXSource.setLexicalHandler(this);
        incrementalSAXSource.setDTDHandler(this);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public ContentHandler getContentHandler() {
        if (this.m_incrementalSAXSource.getClass().getName().equals("com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter")) {
            return (ContentHandler) this.m_incrementalSAXSource;
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public LexicalHandler getLexicalHandler() {
        if (this.m_incrementalSAXSource.getClass().getName().equals("com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter")) {
            return (LexicalHandler) this.m_incrementalSAXSource;
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public EntityResolver getEntityResolver() {
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DTDHandler getDTDHandler() {
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public ErrorHandler getErrorHandler() {
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DeclHandler getDeclHandler() {
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean needsTwoThreads() {
        return null != this.m_incrementalSAXSource;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException, ArrayIndexOutOfBoundsException {
        int identity = makeNodeIdentity(nodeHandle);
        if (identity == -1) {
            return;
        }
        int type = _type(identity);
        if (isTextType(type)) {
            int dataIndex = this.m_dataOrQName.elementAt(identity);
            int offset = this.m_data.elementAt(dataIndex);
            int length = this.m_data.elementAt(dataIndex + 1);
            if (normalize) {
                this.m_chars.sendNormalizedSAXcharacters(ch, offset, length);
                return;
            } else {
                this.m_chars.sendSAXcharacters(ch, offset, length);
                return;
            }
        }
        int firstChild = _firstch(identity);
        if (-1 != firstChild) {
            int offset2 = -1;
            int length2 = 0;
            int identity2 = firstChild;
            do {
                if (isTextType(_type(identity2))) {
                    int dataIndex2 = _dataOrQName(identity2);
                    if (-1 == offset2) {
                        offset2 = this.m_data.elementAt(dataIndex2);
                    }
                    length2 += this.m_data.elementAt(dataIndex2 + 1);
                }
                identity2 = getNextNodeIdentity(identity2);
                if (-1 == identity2) {
                    break;
                }
            } while (_parent(identity2) >= identity);
            if (length2 > 0) {
                if (normalize) {
                    this.m_chars.sendNormalizedSAXcharacters(ch, offset2, length2);
                    return;
                } else {
                    this.m_chars.sendSAXcharacters(ch, offset2, length2);
                    return;
                }
            }
            return;
        }
        if (type != 1) {
            int dataIndex3 = _dataOrQName(identity);
            if (dataIndex3 < 0) {
                dataIndex3 = this.m_data.elementAt((-dataIndex3) + 1);
            }
            String str = this.m_valuesOrPrefixes.indexToString(dataIndex3);
            if (normalize) {
                FastStringBuffer.sendNormalizedSAXcharacters(str.toCharArray(), 0, str.length(), ch);
            } else {
                ch.characters(str.toCharArray(), 0, str.length());
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeName(int nodeHandle) {
        int expandedTypeID = getExpandedTypeID(nodeHandle);
        int namespaceID = this.m_expandedNameTable.getNamespaceID(expandedTypeID);
        if (0 == namespaceID) {
            int type = getNodeType(nodeHandle);
            if (type == 13) {
                if (null == this.m_expandedNameTable.getLocalName(expandedTypeID)) {
                    return "xmlns";
                }
                return "xmlns:" + this.m_expandedNameTable.getLocalName(expandedTypeID);
            }
            if (0 == this.m_expandedNameTable.getLocalNameID(expandedTypeID)) {
                return m_fixednames[type];
            }
            return this.m_expandedNameTable.getLocalName(expandedTypeID);
        }
        int qnameIndex = this.m_dataOrQName.elementAt(makeNodeIdentity(nodeHandle));
        if (qnameIndex < 0) {
            qnameIndex = this.m_data.elementAt(-qnameIndex);
        }
        return this.m_valuesOrPrefixes.indexToString(qnameIndex);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeNameX(int nodeHandle) {
        int expandedTypeID = getExpandedTypeID(nodeHandle);
        int namespaceID = this.m_expandedNameTable.getNamespaceID(expandedTypeID);
        if (0 == namespaceID) {
            String name = this.m_expandedNameTable.getLocalName(expandedTypeID);
            if (name == null) {
                return "";
            }
            return name;
        }
        int qnameIndex = this.m_dataOrQName.elementAt(makeNodeIdentity(nodeHandle));
        if (qnameIndex < 0) {
            qnameIndex = this.m_data.elementAt(-qnameIndex);
        }
        return this.m_valuesOrPrefixes.indexToString(qnameIndex);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isAttributeSpecified(int attributeHandle) {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentTypeDeclarationSystemIdentifier() {
        error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    protected int getNextNodeIdentity(int identity) {
        int identity2 = identity + 1;
        while (identity2 >= this.m_size) {
            if (null == this.m_incrementalSAXSource) {
                return -1;
            }
            nextNode();
        }
        return identity2;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public void dispatchToEvents(int nodeHandle, ContentHandler ch) throws SAXException {
        DTMTreeWalker treeWalker = this.m_walker;
        ContentHandler prevCH = treeWalker.getcontentHandler();
        if (null != prevCH) {
            treeWalker = new DTMTreeWalker();
        }
        treeWalker.setcontentHandler(ch);
        treeWalker.setDTM(this);
        try {
            treeWalker.traverse(nodeHandle);
            treeWalker.setcontentHandler(null);
        } catch (Throwable th) {
            treeWalker.setcontentHandler(null);
            throw th;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    public int getNumberOfNodes() {
        return this.m_size;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    protected boolean nextNode() {
        if (null == this.m_incrementalSAXSource) {
            return false;
        }
        if (this.m_endDocumentOccured) {
            clearCoRoutine();
            return false;
        }
        Object gotMore = this.m_incrementalSAXSource.deliverMoreNodes(true);
        if (!(gotMore instanceof Boolean)) {
            if (gotMore instanceof RuntimeException) {
                throw ((RuntimeException) gotMore);
            }
            if (gotMore instanceof Exception) {
                throw new WrappedRuntimeException((Exception) gotMore);
            }
            clearCoRoutine();
            return false;
        }
        if (gotMore != Boolean.TRUE) {
            clearCoRoutine();
            return true;
        }
        return true;
    }

    private final boolean isTextType(int type) {
        return 3 == type || 4 == type;
    }

    protected int addNode(int type, int expandedTypeID, int parentIndex, int previousSibling, int dataOrPrefix, boolean canHaveFirstChild) {
        int nodeIndex = this.m_size;
        this.m_size = nodeIndex + 1;
        if (this.m_dtmIdent.size() == (nodeIndex >>> 16)) {
            addNewDTMID(nodeIndex);
        }
        this.m_firstch.addElement(canHaveFirstChild ? -2 : -1);
        this.m_nextsib.addElement(-2);
        this.m_parent.addElement(parentIndex);
        this.m_exptype.addElement(expandedTypeID);
        this.m_dataOrQName.addElement(dataOrPrefix);
        if (this.m_prevsib != null) {
            this.m_prevsib.addElement(previousSibling);
        }
        if (-1 != previousSibling) {
            this.m_nextsib.setElementAt(nodeIndex, previousSibling);
        }
        if (this.m_locator != null && this.m_useSourceLocationProperty) {
            setSourceLocation();
        }
        switch (type) {
            case 2:
                break;
            case 13:
                declareNamespaceInContext(parentIndex, nodeIndex);
                break;
            default:
                if (-1 == previousSibling && -1 != parentIndex) {
                    this.m_firstch.setElementAt(nodeIndex, parentIndex);
                    break;
                }
                break;
        }
        return nodeIndex;
    }

    protected void addNewDTMID(int nodeIndex) {
        try {
            if (this.m_mgr == null) {
                throw new ClassCastException();
            }
            DTMManagerDefault mgrD = (DTMManagerDefault) this.m_mgr;
            int id = mgrD.getFirstFreeDTMID();
            mgrD.addDTM(this, id, nodeIndex);
            this.m_dtmIdent.addElement(id << 16);
        } catch (ClassCastException e2) {
            error(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public void migrateTo(DTMManager manager) {
        super.migrateTo(manager);
        int numDTMs = this.m_dtmIdent.size();
        int dtmId = this.m_mgrDefault.getFirstFreeDTMID();
        int nodeIndex = 0;
        for (int i2 = 0; i2 < numDTMs; i2++) {
            this.m_dtmIdent.setElementAt(dtmId << 16, i2);
            this.m_mgrDefault.addDTM(this, dtmId, nodeIndex);
            dtmId++;
            nodeIndex += 65536;
        }
    }

    protected void setSourceLocation() {
        this.m_sourceSystemId.addElement(this.m_locator.getSystemId());
        this.m_sourceLine.addElement(this.m_locator.getLineNumber());
        this.m_sourceColumn.addElement(this.m_locator.getColumnNumber());
        if (this.m_sourceSystemId.size() != this.m_size) {
            String msg = "CODING ERROR in Source Location: " + this.m_size + " != " + this.m_sourceSystemId.size();
            System.err.println(msg);
            throw new RuntimeException(msg);
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeValue(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        int type = _type(identity);
        if (isTextType(type)) {
            int dataIndex = _dataOrQName(identity);
            int offset = this.m_data.elementAt(dataIndex);
            int length = this.m_data.elementAt(dataIndex + 1);
            return this.m_chars.getString(offset, length);
        }
        if (1 == type || 11 == type || 9 == type) {
            return null;
        }
        int dataIndex2 = _dataOrQName(identity);
        if (dataIndex2 < 0) {
            dataIndex2 = this.m_data.elementAt((-dataIndex2) + 1);
        }
        return this.m_valuesOrPrefixes.indexToString(dataIndex2);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getLocalName(int nodeHandle) {
        return this.m_expandedNameTable.getLocalName(_exptype(makeNodeIdentity(nodeHandle)));
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getUnparsedEntityURI(String name) {
        String url = "";
        if (null == this.m_entities) {
            return url;
        }
        int n2 = this.m_entities.size();
        int i2 = 0;
        while (true) {
            if (i2 >= n2) {
                break;
            }
            String ename = (String) this.m_entities.elementAt(i2 + 3);
            if (null == ename || !ename.equals(name)) {
                i2 += 4;
            } else {
                String nname = (String) this.m_entities.elementAt(i2 + 2);
                if (null != nname) {
                    url = (String) this.m_entities.elementAt(i2 + 1);
                    if (null == url) {
                        url = (String) this.m_entities.elementAt(i2 + 0);
                    }
                }
            }
        }
        return url;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getPrefix(int nodeHandle) throws ArrayIndexOutOfBoundsException {
        int prefixIndex;
        int identity = makeNodeIdentity(nodeHandle);
        int type = _type(identity);
        if (1 == type) {
            int prefixIndex2 = _dataOrQName(identity);
            if (0 == prefixIndex2) {
                return "";
            }
            String qname = this.m_valuesOrPrefixes.indexToString(prefixIndex2);
            return getPrefix(qname, null);
        }
        if (2 == type && (prefixIndex = _dataOrQName(identity)) < 0) {
            String qname2 = this.m_valuesOrPrefixes.indexToString(this.m_data.elementAt(-prefixIndex));
            return getPrefix(qname2, null);
        }
        return "";
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public int getAttributeNode(int nodeHandle, String namespaceURI, String name) {
        int firstAttribute = getFirstAttribute(nodeHandle);
        while (true) {
            int attrH = firstAttribute;
            if (-1 != attrH) {
                String attrNS = getNamespaceURI(attrH);
                String attrName = getLocalName(attrH);
                boolean nsMatch = namespaceURI == attrNS || (namespaceURI != null && namespaceURI.equals(attrNS));
                if (!nsMatch || !name.equals(attrName)) {
                    firstAttribute = getNextAttribute(attrH);
                } else {
                    return attrH;
                }
            } else {
                return -1;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentTypeDeclarationPublicIdentifier() {
        error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNamespaceURI(int nodeHandle) {
        return this.m_expandedNameTable.getNamespace(_exptype(makeNodeIdentity(nodeHandle)));
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public XMLString getStringValue(int nodeHandle) {
        int type;
        int identity = makeNodeIdentity(nodeHandle);
        if (identity == -1) {
            type = -1;
        } else {
            type = _type(identity);
        }
        if (isTextType(type)) {
            int dataIndex = _dataOrQName(identity);
            int offset = this.m_data.elementAt(dataIndex);
            int length = this.m_data.elementAt(dataIndex + 1);
            return this.m_xstrf.newstr(this.m_chars, offset, length);
        }
        int firstChild = _firstch(identity);
        if (-1 != firstChild) {
            int offset2 = -1;
            int length2 = 0;
            int identity2 = firstChild;
            do {
                int type2 = _type(identity2);
                if (isTextType(type2)) {
                    int dataIndex2 = _dataOrQName(identity2);
                    if (-1 == offset2) {
                        offset2 = this.m_data.elementAt(dataIndex2);
                    }
                    length2 += this.m_data.elementAt(dataIndex2 + 1);
                }
                identity2 = getNextNodeIdentity(identity2);
                if (-1 == identity2) {
                    break;
                }
            } while (_parent(identity2) >= identity);
            if (length2 > 0) {
                return this.m_xstrf.newstr(this.m_chars, offset2, length2);
            }
        } else if (type != 1) {
            int dataIndex3 = _dataOrQName(identity);
            if (dataIndex3 < 0) {
                dataIndex3 = this.m_data.elementAt((-dataIndex3) + 1);
            }
            return this.m_xstrf.newstr(this.m_valuesOrPrefixes.indexToString(dataIndex3));
        }
        return this.m_xstrf.emptystr();
    }

    public boolean isWhitespace(int nodeHandle) {
        int type;
        int identity = makeNodeIdentity(nodeHandle);
        if (identity == -1) {
            type = -1;
        } else {
            type = _type(identity);
        }
        if (isTextType(type)) {
            int dataIndex = _dataOrQName(identity);
            int offset = this.m_data.elementAt(dataIndex);
            int length = this.m_data.elementAt(dataIndex + 1);
            return this.m_chars.isWhitespace(offset, length);
        }
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public int getElementById(String elementId) {
        Integer intObj;
        boolean isMore = true;
        do {
            intObj = this.m_idAttributes.get(elementId);
            if (null != intObj) {
                return makeNodeHandle(intObj.intValue());
            }
            if (isMore && !this.m_endDocumentOccured) {
                isMore = nextNode();
            } else {
                return -1;
            }
        } while (null == intObj);
        return -1;
    }

    public String getPrefix(String qname, String uri) {
        String prefix;
        int uriIndex = -1;
        if (null != uri && uri.length() > 0) {
            do {
                uriIndex = this.m_prefixMappings.indexOf(uri, uriIndex + 1);
            } while ((uriIndex & 1) == 0);
            if (uriIndex >= 0) {
                prefix = (String) this.m_prefixMappings.elementAt(uriIndex - 1);
            } else if (null != qname) {
                int indexOfNSSep = qname.indexOf(58);
                if (qname.equals("xmlns")) {
                    prefix = "";
                } else if (qname.startsWith("xmlns:")) {
                    prefix = qname.substring(indexOfNSSep + 1);
                } else {
                    prefix = indexOfNSSep > 0 ? qname.substring(0, indexOfNSSep) : null;
                }
            } else {
                prefix = null;
            }
        } else if (null != qname) {
            int indexOfNSSep2 = qname.indexOf(58);
            if (indexOfNSSep2 > 0) {
                if (qname.startsWith("xmlns:")) {
                    prefix = qname.substring(indexOfNSSep2 + 1);
                } else {
                    prefix = qname.substring(0, indexOfNSSep2);
                }
            } else if (qname.equals("xmlns")) {
                prefix = "";
            } else {
                prefix = null;
            }
        } else {
            prefix = null;
        }
        return prefix;
    }

    public int getIdForNamespace(String uri) {
        return this.m_valuesOrPrefixes.stringToIndex(uri);
    }

    public String getNamespaceURI(String prefix) {
        String uri = "";
        int prefixIndex = this.m_contextIndexes.peek() - 1;
        if (null == prefix) {
            prefix = "";
        }
        do {
            prefixIndex = this.m_prefixMappings.indexOf(prefix, prefixIndex + 1);
            if (prefixIndex < 0) {
                break;
            }
        } while ((prefixIndex & 1) == 1);
        if (prefixIndex > -1) {
            uri = (String) this.m_prefixMappings.elementAt(prefixIndex + 1);
        }
        return uri;
    }

    public void setIDAttribute(String id, int elem) {
        this.m_idAttributes.put(id, Integer.valueOf(elem));
    }

    protected void charactersFlush() {
        if (this.m_textPendingStart >= 0) {
            int length = this.m_chars.size() - this.m_textPendingStart;
            boolean doStrip = false;
            if (getShouldStripWhitespace()) {
                doStrip = this.m_chars.isWhitespace(this.m_textPendingStart, length);
            }
            if (doStrip) {
                this.m_chars.setLength(this.m_textPendingStart);
            } else if (length > 0) {
                int exName = this.m_expandedNameTable.getExpandedTypeID(3);
                int dataIndex = this.m_data.size();
                this.m_previous = addNode(this.m_coalescedTextType, exName, this.m_parents.peek(), this.m_previous, dataIndex, false);
                this.m_data.addElement(this.m_textPendingStart);
                this.m_data.addElement(length);
            }
            this.m_textPendingStart = -1;
            this.m_coalescedTextType = 3;
            this.m_textType = 3;
        }
    }

    @Override // org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        return null;
    }

    @Override // org.xml.sax.DTDHandler
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        if (null == this.m_entities) {
            this.m_entities = new Vector();
        }
        try {
            String systemId2 = SystemIDResolver.getAbsoluteURI(systemId, getDocumentBaseURI());
            this.m_entities.addElement(publicId);
            this.m_entities.addElement(systemId2);
            this.m_entities.addElement(notationName);
            this.m_entities.addElement(name);
        } catch (Exception e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        this.m_locator = locator;
        this.m_systemId = locator.getSystemId();
    }

    public void startDocument() throws SAXException {
        int doc = addNode(9, this.m_expandedNameTable.getExpandedTypeID(9), -1, -1, 0, true);
        this.m_parents.push(doc);
        this.m_previous = -1;
        this.m_contextIndexes.push(this.m_prefixMappings.size());
    }

    public void endDocument() throws SAXException {
        charactersFlush();
        this.m_nextsib.setElementAt(-1, 0);
        if (this.m_firstch.elementAt(0) == -2) {
            this.m_firstch.setElementAt(-1, 0);
        }
        if (-1 != this.m_previous) {
            this.m_nextsib.setElementAt(-1, this.m_previous);
        }
        this.m_parents = null;
        this.m_prefixMappings = null;
        this.m_contextIndexes = null;
        this.m_endDocumentOccured = true;
        this.m_locator = null;
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (null == prefix) {
            prefix = "";
        }
        this.m_prefixMappings.addElement(prefix);
        this.m_prefixMappings.addElement(uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
        if (null == prefix) {
            prefix = "";
        }
        int index = this.m_contextIndexes.peek() - 1;
        do {
            index = this.m_prefixMappings.indexOf(prefix, index + 1);
            if (index < 0) {
                break;
            }
        } while ((index & 1) == 1);
        if (index > -1) {
            this.m_prefixMappings.setElementAt("%@$#^@#", index);
            this.m_prefixMappings.setElementAt("%@$#^@#", index + 1);
        }
    }

    protected boolean declAlreadyDeclared(String prefix) {
        int startDecls = this.m_contextIndexes.peek();
        Vector prefixMappings = this.m_prefixMappings;
        int nDecls = prefixMappings.size();
        for (int i2 = startDecls; i2 < nDecls; i2 += 2) {
            String prefixDecl = (String) prefixMappings.elementAt(i2);
            if (prefixDecl != null && prefixDecl.equals(prefix)) {
                return true;
            }
        }
        return false;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        boolean shouldStripWhitespace;
        int nodeType;
        charactersFlush();
        int exName = this.m_expandedNameTable.getExpandedTypeID(uri, localName, 1);
        int prefixIndex = null != getPrefix(qName, uri) ? this.m_valuesOrPrefixes.stringToIndex(qName) : 0;
        int elemNode = addNode(1, exName, this.m_parents.peek(), this.m_previous, prefixIndex, true);
        if (this.m_indexing) {
            indexNode(exName, elemNode);
        }
        this.m_parents.push(elemNode);
        int startDecls = this.m_contextIndexes.peek();
        int nDecls = this.m_prefixMappings.size();
        int prev = -1;
        if (!this.m_pastFirstElement) {
            prev = addNode(13, this.m_expandedNameTable.getExpandedTypeID(null, "xml", 13), elemNode, -1, this.m_valuesOrPrefixes.stringToIndex("http://www.w3.org/XML/1998/namespace"), false);
            this.m_pastFirstElement = true;
        }
        for (int i2 = startDecls; i2 < nDecls; i2 += 2) {
            String prefix = (String) this.m_prefixMappings.elementAt(i2);
            if (prefix != null) {
                String declURL = (String) this.m_prefixMappings.elementAt(i2 + 1);
                prev = addNode(13, this.m_expandedNameTable.getExpandedTypeID(null, prefix, 13), elemNode, prev, this.m_valuesOrPrefixes.stringToIndex(declURL), false);
            }
        }
        int n2 = attributes.getLength();
        for (int i3 = 0; i3 < n2; i3++) {
            String attrUri = attributes.getURI(i3);
            String attrQName = attributes.getQName(i3);
            String valString = attributes.getValue(i3);
            String prefix2 = getPrefix(attrQName, attrUri);
            String attrLocalName = attributes.getLocalName(i3);
            if (null != attrQName && (attrQName.equals("xmlns") || attrQName.startsWith("xmlns:"))) {
                if (!declAlreadyDeclared(prefix2)) {
                    nodeType = 13;
                }
            } else {
                nodeType = 2;
                if (attributes.getType(i3).equalsIgnoreCase("ID")) {
                    setIDAttribute(valString, elemNode);
                }
            }
            if (null == valString) {
                valString = "";
            }
            int val = this.m_valuesOrPrefixes.stringToIndex(valString);
            if (null != prefix2) {
                int prefixIndex2 = this.m_valuesOrPrefixes.stringToIndex(attrQName);
                int dataIndex = this.m_data.size();
                this.m_data.addElement(prefixIndex2);
                this.m_data.addElement(val);
                val = -dataIndex;
            }
            prev = addNode(nodeType, this.m_expandedNameTable.getExpandedTypeID(attrUri, attrLocalName, nodeType), elemNode, prev, val, false);
        }
        if (-1 != prev) {
            this.m_nextsib.setElementAt(-1, prev);
        }
        if (null != this.m_wsfilter) {
            short wsv = this.m_wsfilter.getShouldStripSpace(makeNodeHandle(elemNode), this);
            if (3 == wsv) {
                shouldStripWhitespace = getShouldStripWhitespace();
            } else {
                shouldStripWhitespace = 2 == wsv;
            }
            boolean shouldStrip = shouldStripWhitespace;
            pushShouldStripWhitespace(shouldStrip);
        }
        this.m_previous = -1;
        this.m_contextIndexes.push(this.m_prefixMappings.size());
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        charactersFlush();
        this.m_contextIndexes.quickPop(1);
        int topContextIndex = this.m_contextIndexes.peek();
        if (topContextIndex != this.m_prefixMappings.size()) {
            this.m_prefixMappings.setSize(topContextIndex);
        }
        int lastNode = this.m_previous;
        this.m_previous = this.m_parents.pop();
        if (-1 == lastNode) {
            this.m_firstch.setElementAt(-1, this.m_previous);
        } else {
            this.m_nextsib.setElementAt(-1, lastNode);
        }
        popShouldStripWhitespace();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.m_textPendingStart == -1) {
            this.m_textPendingStart = this.m_chars.size();
            this.m_coalescedTextType = this.m_textType;
        } else if (this.m_textType == 3) {
            this.m_coalescedTextType = 3;
        }
        this.m_chars.append(ch, start, length);
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        characters(ch, start, length);
    }

    public void processingInstruction(String target, String data) throws SAXException {
        charactersFlush();
        int exName = this.m_expandedNameTable.getExpandedTypeID(null, target, 7);
        int dataIndex = this.m_valuesOrPrefixes.stringToIndex(data);
        this.m_previous = addNode(7, exName, this.m_parents.peek(), this.m_previous, dataIndex, false);
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException e2) throws SAXException {
        System.err.println(e2.getMessage());
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException e2) throws SAXException {
        throw e2;
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException e2) throws SAXException {
        throw e2;
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void elementDecl(String name, String model) throws SAXException {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void internalEntityDecl(String name, String value) throws SAXException {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        this.m_insideDTD = true;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
        this.m_insideDTD = false;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        this.m_textType = 4;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        this.m_textType = 3;
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.m_insideDTD) {
            return;
        }
        charactersFlush();
        int exName = this.m_expandedNameTable.getExpandedTypeID(8);
        int dataIndex = this.m_valuesOrPrefixes.stringToIndex(new String(ch, start, length));
        this.m_previous = addNode(8, exName, this.m_parents.peek(), this.m_previous, dataIndex, false);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void setProperty(String property, Object value) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public SourceLocator getSourceLocatorFor(int node) {
        if (this.m_useSourceLocationProperty) {
            int node2 = makeNodeIdentity(node);
            return new NodeLocator(null, this.m_sourceSystemId.elementAt(node2), this.m_sourceLine.elementAt(node2), this.m_sourceColumn.elementAt(node2));
        }
        if (this.m_locator != null) {
            return new NodeLocator(null, this.m_locator.getSystemId(), -1, -1);
        }
        if (this.m_systemId != null) {
            return new NodeLocator(null, this.m_systemId, -1, -1);
        }
        return null;
    }

    public String getFixedNames(int type) {
        return m_fixednames[type];
    }
}
