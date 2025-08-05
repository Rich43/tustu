package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xml.internal.dtm.DTMException;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.utils.BoolStack;
import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBase.class */
public abstract class DTMDefaultBase implements DTM {
    static final boolean JJK_DEBUG = false;
    public static final int ROOTNODE = 0;
    protected int m_size;
    protected SuballocatedIntVector m_exptype;
    protected SuballocatedIntVector m_firstch;
    protected SuballocatedIntVector m_nextsib;
    protected SuballocatedIntVector m_prevsib;
    protected SuballocatedIntVector m_parent;
    protected Vector m_namespaceDeclSets;
    protected SuballocatedIntVector m_namespaceDeclSetElements;
    protected int[][][] m_elemIndexes;
    public static final int DEFAULT_BLOCKSIZE = 512;
    public static final int DEFAULT_NUMBLOCKS = 32;
    public static final int DEFAULT_NUMBLOCKS_SMALL = 4;
    protected static final int NOTPROCESSED = -2;
    public DTMManager m_mgr;
    protected DTMManagerDefault m_mgrDefault;
    protected SuballocatedIntVector m_dtmIdent;
    protected String m_documentBaseURI;
    protected DTMWSFilter m_wsfilter;
    protected boolean m_shouldStripWS;
    protected BoolStack m_shouldStripWhitespaceStack;
    protected XMLStringFactory m_xstrf;
    protected ExpandedNameTable m_expandedNameTable;
    protected boolean m_indexing;
    protected DTMAxisTraverser[] m_traversers;
    private Vector m_namespaceLists;

    protected abstract int getNextNodeIdentity(int i2);

    protected abstract boolean nextNode();

    protected abstract int getNumberOfNodes();

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract int getAttributeNode(int i2, String str, String str2);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract XMLString getStringValue(int i2);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract String getNodeName(int i2);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract String getLocalName(int i2);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract String getPrefix(int i2);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract String getNamespaceURI(int i2);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract String getNodeValue(int i2);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract String getDocumentTypeDeclarationSystemIdentifier();

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract String getDocumentTypeDeclarationPublicIdentifier();

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract int getElementById(String str);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract String getUnparsedEntityURI(String str);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract boolean isAttributeSpecified(int i2);

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract void dispatchCharactersEvents(int i2, ContentHandler contentHandler, boolean z2) throws SAXException;

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public abstract void dispatchToEvents(int i2, ContentHandler contentHandler) throws SAXException;

    public DTMDefaultBase(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
        this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, false);
    }

    public DTMDefaultBase(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable) {
        int numblocks;
        this.m_size = 0;
        this.m_namespaceDeclSets = null;
        this.m_namespaceDeclSetElements = null;
        this.m_mgrDefault = null;
        this.m_shouldStripWS = false;
        this.m_namespaceLists = null;
        if (blocksize <= 64) {
            numblocks = 4;
            this.m_dtmIdent = new SuballocatedIntVector(4, 1);
        } else {
            numblocks = 32;
            this.m_dtmIdent = new SuballocatedIntVector(32);
        }
        this.m_exptype = new SuballocatedIntVector(blocksize, numblocks);
        this.m_firstch = new SuballocatedIntVector(blocksize, numblocks);
        this.m_nextsib = new SuballocatedIntVector(blocksize, numblocks);
        this.m_parent = new SuballocatedIntVector(blocksize, numblocks);
        if (usePrevsib) {
            this.m_prevsib = new SuballocatedIntVector(blocksize, numblocks);
        }
        this.m_mgr = mgr;
        if (mgr instanceof DTMManagerDefault) {
            this.m_mgrDefault = (DTMManagerDefault) mgr;
        }
        this.m_documentBaseURI = null != source ? source.getSystemId() : null;
        this.m_dtmIdent.setElementAt(dtmIdentity, 0);
        this.m_wsfilter = whiteSpaceFilter;
        this.m_xstrf = xstringfactory;
        this.m_indexing = doIndexing;
        if (doIndexing) {
            this.m_expandedNameTable = new ExpandedNameTable();
        } else {
            this.m_expandedNameTable = this.m_mgrDefault.getExpandedNameTable(this);
        }
        if (null != whiteSpaceFilter) {
            this.m_shouldStripWhitespaceStack = new BoolStack();
            pushShouldStripWhitespace(false);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v17, types: [int[]] */
    /* JADX WARN: Type inference failed for: r0v39, types: [int[]] */
    /* JADX WARN: Type inference failed for: r1v28, types: [int[][], int[][][]] */
    /* JADX WARN: Type inference failed for: r1v5, types: [int[][], int[][][]] */
    protected void ensureSizeOfIndex(int namespaceID, int LocalNameID) {
        if (null == this.m_elemIndexes) {
            this.m_elemIndexes = new int[namespaceID + 20][];
        } else if (this.m_elemIndexes.length <= namespaceID) {
            int[][][] indexes = this.m_elemIndexes;
            this.m_elemIndexes = new int[namespaceID + 20][];
            System.arraycopy(indexes, 0, this.m_elemIndexes, 0, indexes.length);
        }
        int[][] localNameIndex = this.m_elemIndexes[namespaceID];
        if (null == localNameIndex) {
            localNameIndex = new int[LocalNameID + 100];
            this.m_elemIndexes[namespaceID] = localNameIndex;
        } else if (localNameIndex.length <= LocalNameID) {
            localNameIndex = new int[LocalNameID + 100];
            System.arraycopy(localNameIndex, 0, localNameIndex, 0, localNameIndex.length);
            this.m_elemIndexes[namespaceID] = localNameIndex;
        }
        int[] elemHandles = localNameIndex[LocalNameID];
        if (null == elemHandles) {
            int[] elemHandles2 = new int[128];
            localNameIndex[LocalNameID] = elemHandles2;
            elemHandles2[0] = 1;
        } else if (elemHandles.length <= elemHandles[0] + 1) {
            int[] elemHandles3 = new int[elemHandles[0] + 1024];
            System.arraycopy(elemHandles, 0, elemHandles3, 0, elemHandles.length);
            localNameIndex[LocalNameID] = elemHandles3;
        }
    }

    protected void indexNode(int expandedTypeID, int identity) {
        ExpandedNameTable ent = this.m_expandedNameTable;
        short type = ent.getType(expandedTypeID);
        if (1 == type) {
            int namespaceID = ent.getNamespaceID(expandedTypeID);
            int localNameID = ent.getLocalNameID(expandedTypeID);
            ensureSizeOfIndex(namespaceID, localNameID);
            int[] index = this.m_elemIndexes[namespaceID][localNameID];
            index[index[0]] = identity;
            index[0] = index[0] + 1;
        }
    }

    protected int findGTE(int[] list, int start, int len, int value) {
        int low = start;
        int high = start + (len - 1);
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int c2 = list[mid];
            if (c2 > value) {
                high = mid - 1;
            } else if (c2 < value) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        if (low > high || list[low] <= value) {
            return -1;
        }
        return low;
    }

    int findElementFromIndex(int nsIndex, int lnIndex, int firstPotential) {
        int[][] lnIndexs;
        int[] elems;
        int pos;
        int[][][] indexes = this.m_elemIndexes;
        if (null != indexes && nsIndex < indexes.length && null != (lnIndexs = indexes[nsIndex]) && lnIndex < lnIndexs.length && null != (elems = lnIndexs[lnIndex]) && (pos = findGTE(elems, 1, elems[0], firstPotential)) > -1) {
            return elems[pos];
        }
        return -2;
    }

    protected short _type(int identity) {
        int info = _exptype(identity);
        if (-1 != info) {
            return this.m_expandedNameTable.getType(info);
        }
        return (short) -1;
    }

    protected int _exptype(int identity) {
        if (identity == -1) {
            return -1;
        }
        while (identity >= this.m_size) {
            if (!nextNode() && identity >= this.m_size) {
                return -1;
            }
        }
        return this.m_exptype.elementAt(identity);
    }

    protected int _level(int identity) {
        while (identity >= this.m_size) {
            boolean isMore = nextNode();
            if (!isMore && identity >= this.m_size) {
                return -1;
            }
        }
        int i2 = 0;
        while (true) {
            int i_parent = _parent(identity);
            identity = i_parent;
            if (-1 != i_parent) {
                i2++;
            } else {
                return i2;
            }
        }
    }

    protected int _firstch(int identity) {
        int info = identity >= this.m_size ? -2 : this.m_firstch.elementAt(identity);
        while (info == -2) {
            boolean isMore = nextNode();
            if (identity >= this.m_size && !isMore) {
                return -1;
            }
            info = this.m_firstch.elementAt(identity);
            if (info == -2 && !isMore) {
                return -1;
            }
        }
        return info;
    }

    protected int _nextsib(int identity) {
        int info = identity >= this.m_size ? -2 : this.m_nextsib.elementAt(identity);
        while (info == -2) {
            boolean isMore = nextNode();
            if (identity >= this.m_size && !isMore) {
                return -1;
            }
            info = this.m_nextsib.elementAt(identity);
            if (info == -2 && !isMore) {
                return -1;
            }
        }
        return info;
    }

    protected int _prevsib(int identity) {
        if (identity < this.m_size) {
            return this.m_prevsib.elementAt(identity);
        }
        do {
            boolean isMore = nextNode();
            if (identity >= this.m_size && !isMore) {
                return -1;
            }
        } while (identity >= this.m_size);
        return this.m_prevsib.elementAt(identity);
    }

    protected int _parent(int identity) {
        if (identity < this.m_size) {
            return this.m_parent.elementAt(identity);
        }
        do {
            boolean isMore = nextNode();
            if (identity >= this.m_size && !isMore) {
                return -1;
            }
        } while (identity >= this.m_size);
        return this.m_parent.elementAt(identity);
    }

    public void dumpDTM(OutputStream os) {
        String typestring;
        if (os == null) {
            try {
                File f2 = new File("DTMDump" + hashCode() + ".txt");
                System.err.println("Dumping... " + f2.getAbsolutePath());
                os = new FileOutputStream(f2);
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
                throw new RuntimeException(ioe.getMessage());
            }
        }
        PrintStream ps = new PrintStream(os);
        while (nextNode()) {
        }
        int nRecords = this.m_size;
        ps.println("Total nodes: " + nRecords);
        for (int index = 0; index < nRecords; index++) {
            int i2 = makeNodeHandle(index);
            ps.println("=========== index=" + index + " handle=" + i2 + " ===========");
            ps.println("NodeName: " + getNodeName(i2));
            ps.println("NodeNameX: " + getNodeNameX(i2));
            ps.println("LocalName: " + getLocalName(i2));
            ps.println("NamespaceURI: " + getNamespaceURI(i2));
            ps.println("Prefix: " + getPrefix(i2));
            int exTypeID = _exptype(index);
            ps.println("Expanded Type ID: " + Integer.toHexString(exTypeID));
            int type = _type(index);
            switch (type) {
                case -1:
                    typestring = "NULL";
                    break;
                case 0:
                default:
                    typestring = "Unknown!";
                    break;
                case 1:
                    typestring = "ELEMENT_NODE";
                    break;
                case 2:
                    typestring = "ATTRIBUTE_NODE";
                    break;
                case 3:
                    typestring = "TEXT_NODE";
                    break;
                case 4:
                    typestring = "CDATA_SECTION_NODE";
                    break;
                case 5:
                    typestring = "ENTITY_REFERENCE_NODE";
                    break;
                case 6:
                    typestring = "ENTITY_NODE";
                    break;
                case 7:
                    typestring = "PROCESSING_INSTRUCTION_NODE";
                    break;
                case 8:
                    typestring = "COMMENT_NODE";
                    break;
                case 9:
                    typestring = "DOCUMENT_NODE";
                    break;
                case 10:
                    typestring = "DOCUMENT_NODE";
                    break;
                case 11:
                    typestring = "DOCUMENT_FRAGMENT_NODE";
                    break;
                case 12:
                    typestring = "NOTATION_NODE";
                    break;
                case 13:
                    typestring = "NAMESPACE_NODE";
                    break;
            }
            ps.println("Type: " + typestring);
            int firstChild = _firstch(index);
            if (-1 == firstChild) {
                ps.println("First child: DTM.NULL");
            } else if (-2 == firstChild) {
                ps.println("First child: NOTPROCESSED");
            } else {
                ps.println("First child: " + firstChild);
            }
            if (this.m_prevsib != null) {
                int prevSibling = _prevsib(index);
                if (-1 == prevSibling) {
                    ps.println("Prev sibling: DTM.NULL");
                } else if (-2 == prevSibling) {
                    ps.println("Prev sibling: NOTPROCESSED");
                } else {
                    ps.println("Prev sibling: " + prevSibling);
                }
            }
            int nextSibling = _nextsib(index);
            if (-1 == nextSibling) {
                ps.println("Next sibling: DTM.NULL");
            } else if (-2 == nextSibling) {
                ps.println("Next sibling: NOTPROCESSED");
            } else {
                ps.println("Next sibling: " + nextSibling);
            }
            int parent = _parent(index);
            if (-1 == parent) {
                ps.println("Parent: DTM.NULL");
            } else if (-2 == parent) {
                ps.println("Parent: NOTPROCESSED");
            } else {
                ps.println("Parent: " + parent);
            }
            int level = _level(index);
            ps.println("Level: " + level);
            ps.println("Node Value: " + getNodeValue(i2));
            ps.println("String Value: " + ((Object) getStringValue(i2)));
        }
    }

    public String dumpNode(int nodeHandle) {
        String typestring;
        if (nodeHandle == -1) {
            return "[null]";
        }
        switch (getNodeType(nodeHandle)) {
            case -1:
                typestring = FXMLLoader.NULL_KEYWORD;
                break;
            case 0:
            default:
                typestring = "Unknown!";
                break;
            case 1:
                typestring = "ELEMENT";
                break;
            case 2:
                typestring = "ATTR";
                break;
            case 3:
                typestring = "TEXT";
                break;
            case 4:
                typestring = "CDATA";
                break;
            case 5:
                typestring = "ENT_REF";
                break;
            case 6:
                typestring = SchemaSymbols.ATTVAL_ENTITY;
                break;
            case 7:
                typestring = "PI";
                break;
            case 8:
                typestring = "COMMENT";
                break;
            case 9:
                typestring = "DOC";
                break;
            case 10:
                typestring = "DOC_TYPE";
                break;
            case 11:
                typestring = "DOC_FRAG";
                break;
            case 12:
                typestring = SchemaSymbols.ATTVAL_NOTATION;
                break;
            case 13:
                typestring = "NAMESPACE";
                break;
        }
        return "[" + nodeHandle + ": " + typestring + "(0x" + Integer.toHexString(getExpandedTypeID(nodeHandle)) + ") " + getNodeNameX(nodeHandle) + " {" + getNamespaceURI(nodeHandle) + "}=\"" + getNodeValue(nodeHandle) + "\"]";
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void setFeature(String featureId, boolean state) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean hasChildNodes(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        int firstChild = _firstch(identity);
        return firstChild != -1;
    }

    public final int makeNodeHandle(int nodeIdentity) {
        if (-1 == nodeIdentity) {
            return -1;
        }
        return this.m_dtmIdent.elementAt(nodeIdentity >>> 16) + (nodeIdentity & 65535);
    }

    public final int makeNodeIdentity(int nodeHandle) {
        if (-1 == nodeHandle) {
            return -1;
        }
        if (this.m_mgrDefault != null) {
            int whichDTMindex = nodeHandle >>> 16;
            if (this.m_mgrDefault.m_dtms[whichDTMindex] != this) {
                return -1;
            }
            return this.m_mgrDefault.m_dtm_offsets[whichDTMindex] | (nodeHandle & 65535);
        }
        int whichDTMid = this.m_dtmIdent.indexOf(nodeHandle & DTMManager.IDENT_DTM_DEFAULT);
        if (whichDTMid == -1) {
            return -1;
        }
        return (whichDTMid << 16) + (nodeHandle & 65535);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getFirstChild(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        int firstChild = _firstch(identity);
        return makeNodeHandle(firstChild);
    }

    public int getTypedFirstChild(int nodeHandle, int nodeType) {
        int firstChild;
        if (nodeType < 14) {
            int i_firstch = _firstch(makeNodeIdentity(nodeHandle));
            while (true) {
                firstChild = i_firstch;
                if (firstChild != -1) {
                    int eType = _exptype(firstChild);
                    if (eType == nodeType || (eType >= 14 && this.m_expandedNameTable.getType(eType) == nodeType)) {
                        break;
                    }
                    i_firstch = _nextsib(firstChild);
                } else {
                    return -1;
                }
            }
            return makeNodeHandle(firstChild);
        }
        int i_firstch2 = _firstch(makeNodeIdentity(nodeHandle));
        while (true) {
            int firstChild2 = i_firstch2;
            if (firstChild2 != -1) {
                if (_exptype(firstChild2) != nodeType) {
                    i_firstch2 = _nextsib(firstChild2);
                } else {
                    return makeNodeHandle(firstChild2);
                }
            } else {
                return -1;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getLastChild(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        int child = _firstch(identity);
        int lastChild = -1;
        while (child != -1) {
            lastChild = child;
            child = _nextsib(child);
        }
        return makeNodeHandle(lastChild);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getFirstAttribute(int nodeHandle) {
        int nodeID = makeNodeIdentity(nodeHandle);
        return makeNodeHandle(getFirstAttributeIdentity(nodeID));
    }

    protected int getFirstAttributeIdentity(int identity) {
        int type;
        if (1 == _type(identity)) {
            do {
                int nextNodeIdentity = getNextNodeIdentity(identity);
                identity = nextNodeIdentity;
                if (-1 != nextNodeIdentity) {
                    type = _type(identity);
                    if (type == 2) {
                        return identity;
                    }
                } else {
                    return -1;
                }
            } while (13 == type);
            return -1;
        }
        return -1;
    }

    protected int getTypedAttribute(int nodeHandle, int attType) {
        if (1 == getNodeType(nodeHandle)) {
            int identity = makeNodeIdentity(nodeHandle);
            while (true) {
                int nextNodeIdentity = getNextNodeIdentity(identity);
                identity = nextNodeIdentity;
                if (-1 != nextNodeIdentity) {
                    int type = _type(identity);
                    if (type == 2) {
                        if (_exptype(identity) == attType) {
                            return makeNodeHandle(identity);
                        }
                    } else if (13 != type) {
                        return -1;
                    }
                } else {
                    return -1;
                }
            }
        } else {
            return -1;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getNextSibling(int nodeHandle) {
        if (nodeHandle == -1) {
            return -1;
        }
        return makeNodeHandle(_nextsib(makeNodeIdentity(nodeHandle)));
    }

    public int getTypedNextSibling(int nodeHandle, int nodeType) {
        int eType;
        if (nodeHandle == -1) {
            return -1;
        }
        int node = makeNodeIdentity(nodeHandle);
        do {
            int i_nextsib = _nextsib(node);
            node = i_nextsib;
            if (i_nextsib == -1 || (eType = _exptype(node)) == nodeType) {
                break;
            }
        } while (this.m_expandedNameTable.getType(eType) != nodeType);
        if (node == -1) {
            return -1;
        }
        return makeNodeHandle(node);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getPreviousSibling(int nodeHandle) {
        if (nodeHandle == -1) {
            return -1;
        }
        if (this.m_prevsib != null) {
            return makeNodeHandle(_prevsib(makeNodeIdentity(nodeHandle)));
        }
        int nodeID = makeNodeIdentity(nodeHandle);
        int parent = _parent(nodeID);
        int node = _firstch(parent);
        int result = -1;
        while (node != nodeID) {
            result = node;
            node = _nextsib(node);
        }
        return makeNodeHandle(result);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getNextAttribute(int nodeHandle) {
        int nodeID = makeNodeIdentity(nodeHandle);
        if (_type(nodeID) == 2) {
            return makeNodeHandle(getNextAttributeIdentity(nodeID));
        }
        return -1;
    }

    protected int getNextAttributeIdentity(int identity) {
        int type;
        do {
            int nextNodeIdentity = getNextNodeIdentity(identity);
            identity = nextNodeIdentity;
            if (-1 != nextNodeIdentity) {
                type = _type(identity);
                if (type == 2) {
                    return identity;
                }
            } else {
                return -1;
            }
        } while (type == 13);
        return -1;
    }

    protected void declareNamespaceInContext(int elementNodeIndex, int namespaceNodeIndex) {
        SuballocatedIntVector nsList = null;
        if (this.m_namespaceDeclSets == null) {
            this.m_namespaceDeclSetElements = new SuballocatedIntVector(32);
            this.m_namespaceDeclSetElements.addElement(elementNodeIndex);
            this.m_namespaceDeclSets = new Vector();
            nsList = new SuballocatedIntVector(32);
            this.m_namespaceDeclSets.addElement(nsList);
        } else {
            int last = this.m_namespaceDeclSetElements.size() - 1;
            if (last >= 0 && elementNodeIndex == this.m_namespaceDeclSetElements.elementAt(last)) {
                nsList = (SuballocatedIntVector) this.m_namespaceDeclSets.elementAt(last);
            }
        }
        if (nsList == null) {
            this.m_namespaceDeclSetElements.addElement(elementNodeIndex);
            SuballocatedIntVector inherited = findNamespaceContext(_parent(elementNodeIndex));
            if (inherited != null) {
                int isize = inherited.size();
                nsList = new SuballocatedIntVector(Math.max(Math.min(isize + 16, 2048), 32));
                for (int i2 = 0; i2 < isize; i2++) {
                    nsList.addElement(inherited.elementAt(i2));
                }
            } else {
                nsList = new SuballocatedIntVector(32);
            }
            this.m_namespaceDeclSets.addElement(nsList);
        }
        int newEType = _exptype(namespaceNodeIndex);
        for (int i3 = nsList.size() - 1; i3 >= 0; i3--) {
            if (newEType == getExpandedTypeID(nsList.elementAt(i3))) {
                nsList.setElementAt(makeNodeHandle(namespaceNodeIndex), i3);
                return;
            }
        }
        nsList.addElement(makeNodeHandle(namespaceNodeIndex));
    }

    protected SuballocatedIntVector findNamespaceContext(int elementNodeIndex) {
        int uppermostNSCandidateID;
        if (null != this.m_namespaceDeclSetElements) {
            int wouldBeAt = findInSortedSuballocatedIntVector(this.m_namespaceDeclSetElements, elementNodeIndex);
            if (wouldBeAt >= 0) {
                return (SuballocatedIntVector) this.m_namespaceDeclSets.elementAt(wouldBeAt);
            }
            if (wouldBeAt == -1) {
                return null;
            }
            int wouldBeAt2 = ((-1) - wouldBeAt) - 1;
            int candidate = this.m_namespaceDeclSetElements.elementAt(wouldBeAt2);
            int ancestor = _parent(elementNodeIndex);
            if (wouldBeAt2 == 0 && candidate < ancestor) {
                int rootHandle = getDocumentRoot(makeNodeHandle(elementNodeIndex));
                int rootID = makeNodeIdentity(rootHandle);
                if (getNodeType(rootHandle) == 9) {
                    int ch = _firstch(rootID);
                    uppermostNSCandidateID = ch != -1 ? ch : rootID;
                } else {
                    uppermostNSCandidateID = rootID;
                }
                if (candidate == uppermostNSCandidateID) {
                    return (SuballocatedIntVector) this.m_namespaceDeclSets.elementAt(wouldBeAt2);
                }
            }
            while (wouldBeAt2 >= 0 && ancestor > 0) {
                if (candidate == ancestor) {
                    return (SuballocatedIntVector) this.m_namespaceDeclSets.elementAt(wouldBeAt2);
                }
                if (candidate < ancestor) {
                    do {
                        ancestor = _parent(ancestor);
                    } while (candidate < ancestor);
                } else if (wouldBeAt2 > 0) {
                    wouldBeAt2--;
                    candidate = this.m_namespaceDeclSetElements.elementAt(wouldBeAt2);
                } else {
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    protected int findInSortedSuballocatedIntVector(SuballocatedIntVector vector, int lookfor) {
        int i2 = 0;
        if (vector != null) {
            int first = 0;
            int last = vector.size() - 1;
            while (first <= last) {
                i2 = (first + last) / 2;
                int test = lookfor - vector.elementAt(i2);
                if (test == 0) {
                    return i2;
                }
                if (test < 0) {
                    last = i2 - 1;
                } else {
                    first = i2 + 1;
                }
            }
            if (first > i2) {
                i2 = first;
            }
        }
        return (-1) - i2;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getFirstNamespaceNode(int nodeHandle, boolean inScope) {
        int type;
        SuballocatedIntVector nsContext;
        if (inScope) {
            int identity = makeNodeIdentity(nodeHandle);
            if (_type(identity) != 1 || (nsContext = findNamespaceContext(identity)) == null || nsContext.size() < 1) {
                return -1;
            }
            return nsContext.elementAt(0);
        }
        int identity2 = makeNodeIdentity(nodeHandle);
        if (_type(identity2) == 1) {
            do {
                int nextNodeIdentity = getNextNodeIdentity(identity2);
                identity2 = nextNodeIdentity;
                if (-1 != nextNodeIdentity) {
                    type = _type(identity2);
                    if (type == 13) {
                        return makeNodeHandle(identity2);
                    }
                } else {
                    return -1;
                }
            } while (2 == type);
            return -1;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getNextNamespaceNode(int baseHandle, int nodeHandle, boolean inScope) {
        int type;
        int i2;
        if (inScope) {
            SuballocatedIntVector nsContext = findNamespaceContext(makeNodeIdentity(baseHandle));
            if (nsContext == null || (i2 = 1 + nsContext.indexOf(nodeHandle)) <= 0 || i2 == nsContext.size()) {
                return -1;
            }
            return nsContext.elementAt(i2);
        }
        int identity = makeNodeIdentity(nodeHandle);
        do {
            int nextNodeIdentity = getNextNodeIdentity(identity);
            identity = nextNodeIdentity;
            if (-1 != nextNodeIdentity) {
                type = _type(identity);
                if (type == 13) {
                    return makeNodeHandle(identity);
                }
            } else {
                return -1;
            }
        } while (type == 2);
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getParent(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        if (identity > 0) {
            return makeNodeHandle(_parent(identity));
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getDocument() {
        return this.m_dtmIdent.elementAt(0);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getOwnerDocument(int nodeHandle) {
        if (9 == getNodeType(nodeHandle)) {
            return -1;
        }
        return getDocumentRoot(nodeHandle);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getDocumentRoot(int nodeHandle) {
        return getManager().getDTM(nodeHandle).getDocument();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getStringValueChunkCount(int nodeHandle) {
        error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen) {
        error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getExpandedTypeID(int nodeHandle) {
        int id = makeNodeIdentity(nodeHandle);
        if (id == -1) {
            return -1;
        }
        return _exptype(id);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public int getExpandedTypeID(String namespace, String localName, int type) {
        ExpandedNameTable ent = this.m_expandedNameTable;
        return ent.getExpandedTypeID(namespace, localName, type);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getLocalNameFromExpandedNameID(int expandedNameID) {
        return this.m_expandedNameTable.getLocalName(expandedNameID);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getNamespaceFromExpandedNameID(int expandedNameID) {
        return this.m_expandedNameTable.getNamespace(expandedNameID);
    }

    public int getNamespaceType(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        int expandedNameID = _exptype(identity);
        return this.m_expandedNameTable.getNamespaceID(expandedNameID);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeNameX(int nodeHandle) {
        error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public short getNodeType(int nodeHandle) {
        if (nodeHandle == -1) {
            return (short) -1;
        }
        return this.m_expandedNameTable.getType(_exptype(makeNodeIdentity(nodeHandle)));
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public short getLevel(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        return (short) (_level(identity) + 1);
    }

    public int getNodeIdent(int nodeHandle) {
        return makeNodeIdentity(nodeHandle);
    }

    public int getNodeHandle(int nodeId) {
        return makeNodeHandle(nodeId);
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
        return this.m_documentBaseURI;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentEncoding(int nodeHandle) {
        return "UTF-8";
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
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean supportsPreStripping() {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isNodeAfter(int nodeHandle1, int nodeHandle2) {
        int index1 = makeNodeIdentity(nodeHandle1);
        int index2 = makeNodeIdentity(nodeHandle2);
        return (index1 == -1 || index2 == -1 || index1 > index2) ? false : true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isCharacterElementContentWhitespace(int nodeHandle) {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isDocumentAllDeclarationsProcessed(int documentHandle) {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public Node getNode(int nodeHandle) {
        return new DTMNodeProxy(this, nodeHandle);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void appendChild(int newChild, boolean clone, boolean cloneDepth) {
        error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void appendTextChild(String str) {
        error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
    }

    protected void error(String msg) {
        throw new DTMException(msg);
    }

    protected boolean getShouldStripWhitespace() {
        return this.m_shouldStripWS;
    }

    protected void pushShouldStripWhitespace(boolean shouldStrip) {
        this.m_shouldStripWS = shouldStrip;
        if (null != this.m_shouldStripWhitespaceStack) {
            this.m_shouldStripWhitespaceStack.push(shouldStrip);
        }
    }

    protected void popShouldStripWhitespace() {
        if (null != this.m_shouldStripWhitespaceStack) {
            this.m_shouldStripWS = this.m_shouldStripWhitespaceStack.popAndTop();
        }
    }

    protected void setShouldStripWhitespace(boolean shouldStrip) {
        this.m_shouldStripWS = shouldStrip;
        if (null != this.m_shouldStripWhitespaceStack) {
            this.m_shouldStripWhitespaceStack.setTop(shouldStrip);
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void documentRegistration() {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void documentRelease() {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void migrateTo(DTMManager mgr) {
        this.m_mgr = mgr;
        if (mgr instanceof DTMManagerDefault) {
            this.m_mgrDefault = (DTMManagerDefault) mgr;
        }
    }

    public DTMManager getManager() {
        return this.m_mgr;
    }

    public SuballocatedIntVector getDTMIDs() {
        if (this.m_mgr == null) {
            return null;
        }
        return this.m_dtmIdent;
    }
}
