package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMException;
import com.sun.org.apache.xml.internal.dtm.DTMFilter;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTM;
import com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
import com.sun.org.apache.xml.internal.utils.XMLReaderManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMManagerDefault.class */
public class DTMManagerDefault extends DTMManager {
    private static final boolean DUMPTREE = false;
    private static final boolean DEBUG = false;
    protected DTM[] m_dtms = new DTM[256];
    int[] m_dtm_offsets = new int[256];
    protected XMLReaderManager m_readerManager = null;
    protected DefaultHandler m_defaultHandler = new DefaultHandler();
    private ExpandedNameTable m_expandedNameTable = new ExpandedNameTable();

    public synchronized void addDTM(DTM dtm, int id) {
        addDTM(dtm, id, 0);
    }

    public synchronized void addDTM(DTM dtm, int id, int offset) {
        if (id >= 65536) {
            throw new DTMException(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
        }
        int oldlen = this.m_dtms.length;
        if (oldlen <= id) {
            int newlen = Math.min(id + 256, 65536);
            DTM[] new_m_dtms = new DTM[newlen];
            System.arraycopy(this.m_dtms, 0, new_m_dtms, 0, oldlen);
            this.m_dtms = new_m_dtms;
            int[] new_m_dtm_offsets = new int[newlen];
            System.arraycopy(this.m_dtm_offsets, 0, new_m_dtm_offsets, 0, oldlen);
            this.m_dtm_offsets = new_m_dtm_offsets;
        }
        this.m_dtms[id] = dtm;
        this.m_dtm_offsets[id] = offset;
        dtm.documentRegistration();
    }

    public synchronized int getFirstFreeDTMID() {
        int n2 = this.m_dtms.length;
        for (int i2 = 1; i2 < n2; i2++) {
            if (null == this.m_dtms[i2]) {
                return i2;
            }
        }
        return n2;
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x019f A[DONT_GENERATE] */
    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized com.sun.org.apache.xml.internal.dtm.DTM getDTM(javax.xml.transform.Source r10, boolean r11, com.sun.org.apache.xml.internal.dtm.DTMWSFilter r12, boolean r13, boolean r14) {
        /*
            Method dump skipped, instructions count: 900
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault.getDTM(javax.xml.transform.Source, boolean, com.sun.org.apache.xml.internal.dtm.DTMWSFilter, boolean, boolean):com.sun.org.apache.xml.internal.dtm.DTM");
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized int getDTMHandleFromNode(Node node) {
        int handle;
        int handle2;
        if (null == node) {
            throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_NODE_NON_NULL", null));
        }
        if (node instanceof DTMNodeProxy) {
            return ((DTMNodeProxy) node).getDTMNodeNumber();
        }
        int max = this.m_dtms.length;
        for (int i2 = 0; i2 < max; i2++) {
            DTM thisDTM = this.m_dtms[i2];
            if (null != thisDTM && (thisDTM instanceof DOM2DTM) && (handle2 = ((DOM2DTM) thisDTM).getHandleOfNode(node)) != -1) {
                return handle2;
            }
        }
        Node root = node;
        Node ownerElement = root.getNodeType() == 2 ? ((Attr) root).getOwnerElement() : root.getParentNode();
        while (true) {
            Node p2 = ownerElement;
            if (p2 == null) {
                break;
            }
            root = p2;
            ownerElement = p2.getParentNode();
        }
        DOM2DTM dtm = (DOM2DTM) getDTM(new DOMSource(root), false, null, true, true);
        if (node instanceof DOM2DTMdefaultNamespaceDeclarationNode) {
            int handle3 = dtm.getHandleOfNode(((Attr) node).getOwnerElement());
            handle = dtm.getAttributeNode(handle3, node.getNamespaceURI(), node.getLocalName());
        } else {
            handle = dtm.getHandleOfNode(node);
        }
        if (-1 == handle) {
            throw new RuntimeException(XMLMessages.createXMLMessage("ER_COULD_NOT_RESOLVE_NODE", null));
        }
        return handle;
    }

    public synchronized XMLReader getXMLReader(Source inputSource) {
        try {
            XMLReader reader = inputSource instanceof SAXSource ? ((SAXSource) inputSource).getXMLReader() : null;
            if (null == reader) {
                if (this.m_readerManager == null) {
                    this.m_readerManager = XMLReaderManager.getInstance(super.overrideDefaultParser());
                }
                reader = this.m_readerManager.getXMLReader();
            }
            return reader;
        } catch (SAXException se) {
            throw new DTMException(se.getMessage(), se);
        }
    }

    public synchronized void releaseXMLReader(XMLReader reader) {
        if (this.m_readerManager != null) {
            this.m_readerManager.releaseXMLReader(reader);
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTM getDTM(int nodeHandle) {
        try {
            return this.m_dtms[nodeHandle >>> 16];
        } catch (ArrayIndexOutOfBoundsException e2) {
            if (nodeHandle == -1) {
                return null;
            }
            throw e2;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized int getDTMIdentity(DTM dtm) {
        if (dtm instanceof DTMDefaultBase) {
            DTMDefaultBase dtmdb = (DTMDefaultBase) dtm;
            if (dtmdb.getManager() == this) {
                return dtmdb.getDTMIDs().elementAt(0);
            }
            return -1;
        }
        int n2 = this.m_dtms.length;
        for (int i2 = 0; i2 < n2; i2++) {
            DTM tdtm = this.m_dtms[i2];
            if (tdtm == dtm && this.m_dtm_offsets[i2] == 0) {
                return i2 << 16;
            }
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized boolean release(DTM dtm, boolean shouldHardDelete) {
        if (dtm instanceof SAX2DTM) {
            ((SAX2DTM) dtm).clearCoRoutine();
        }
        if (dtm instanceof DTMDefaultBase) {
            SuballocatedIntVector ids = ((DTMDefaultBase) dtm).getDTMIDs();
            for (int i2 = ids.size() - 1; i2 >= 0; i2--) {
                this.m_dtms[ids.elementAt(i2) >>> 16] = null;
            }
        } else {
            int i3 = getDTMIdentity(dtm);
            if (i3 >= 0) {
                this.m_dtms[i3 >>> 16] = null;
            }
        }
        dtm.documentRelease();
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTM createDocumentFragment() {
        try {
            DocumentBuilderFactory dbf = JdkXmlUtils.getDOMFactory(super.overrideDefaultParser());
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Node df = doc.createDocumentFragment();
            return getDTM(new DOMSource(df), true, null, false, false);
        } catch (Exception e2) {
            throw new DTMException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTMIterator createDTMIterator(int whatToShow, DTMFilter filter, boolean entityReferenceExpansion) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTMIterator createDTMIterator(String xpathString, PrefixResolver presolver) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTMIterator createDTMIterator(int node) {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTMIterator createDTMIterator(Object xpathCompiler, int pos) {
        return null;
    }

    public ExpandedNameTable getExpandedNameTable(DTM dtm) {
        return this.m_expandedNameTable;
    }
}
