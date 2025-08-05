package com.sun.org.apache.xpath.internal;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMFilter;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.utils.NodeVector;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/NodeSetDTM.class */
public class NodeSetDTM extends NodeVector implements DTMIterator, Cloneable {
    static final long serialVersionUID = 7686480133331317070L;
    DTMManager m_manager;
    protected transient int m_next;
    protected transient boolean m_mutable;
    protected transient boolean m_cacheNodes;
    protected int m_root;
    private transient int m_last;

    public NodeSetDTM(DTMManager dtmManager) {
        this.m_next = 0;
        this.m_mutable = true;
        this.m_cacheNodes = true;
        this.m_root = -1;
        this.m_last = 0;
        this.m_manager = dtmManager;
    }

    public NodeSetDTM(int blocksize, int dummy, DTMManager dtmManager) {
        super(blocksize);
        this.m_next = 0;
        this.m_mutable = true;
        this.m_cacheNodes = true;
        this.m_root = -1;
        this.m_last = 0;
        this.m_manager = dtmManager;
    }

    public NodeSetDTM(NodeSetDTM nodelist) {
        this.m_next = 0;
        this.m_mutable = true;
        this.m_cacheNodes = true;
        this.m_root = -1;
        this.m_last = 0;
        this.m_manager = nodelist.getDTMManager();
        this.m_root = nodelist.getRoot();
        addNodes(nodelist);
    }

    public NodeSetDTM(DTMIterator ni) {
        this.m_next = 0;
        this.m_mutable = true;
        this.m_cacheNodes = true;
        this.m_root = -1;
        this.m_last = 0;
        this.m_manager = ni.getDTMManager();
        this.m_root = ni.getRoot();
        addNodes(ni);
    }

    public NodeSetDTM(NodeIterator iterator, XPathContext xctxt) throws DOMException {
        this.m_next = 0;
        this.m_mutable = true;
        this.m_cacheNodes = true;
        this.m_root = -1;
        this.m_last = 0;
        this.m_manager = xctxt.getDTMManager();
        while (true) {
            Node node = iterator.nextNode();
            if (null != node) {
                int handle = xctxt.getDTMHandleFromNode(node);
                addNodeInDocOrder(handle, xctxt);
            } else {
                return;
            }
        }
    }

    public NodeSetDTM(NodeList nodeList, XPathContext xctxt) {
        this.m_next = 0;
        this.m_mutable = true;
        this.m_cacheNodes = true;
        this.m_root = -1;
        this.m_last = 0;
        this.m_manager = xctxt.getDTMManager();
        int n2 = nodeList.getLength();
        for (int i2 = 0; i2 < n2; i2++) {
            Node node = nodeList.item(i2);
            int handle = xctxt.getDTMHandleFromNode(node);
            addNode(handle);
        }
    }

    public NodeSetDTM(int node, DTMManager dtmManager) {
        this.m_next = 0;
        this.m_mutable = true;
        this.m_cacheNodes = true;
        this.m_root = -1;
        this.m_last = 0;
        this.m_manager = dtmManager;
        addNode(node);
    }

    public void setEnvironment(Object environment) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getRoot() {
        if (-1 == this.m_root) {
            if (size() > 0) {
                return item(0);
            }
            return -1;
        }
        return this.m_root;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int context, Object environment) {
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public Object clone() throws CloneNotSupportedException {
        NodeSetDTM clone = (NodeSetDTM) super.clone();
        return clone;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        NodeSetDTM clone = (NodeSetDTM) clone();
        clone.reset();
        return clone;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void reset() {
        this.m_next = 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getWhatToShow() {
        return -17;
    }

    public DTMFilter getFilter() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean getExpandEntityReferences() {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public DTM getDTM(int nodeHandle) {
        return this.m_manager.getDTM(nodeHandle);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public DTMManager getDTMManager() {
        return this.m_manager;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        if (this.m_next < size()) {
            int next = elementAt(this.m_next);
            this.m_next++;
            return next;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int previousNode() {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_ITERATE", null));
        }
        if (this.m_next - 1 > 0) {
            this.m_next--;
            return elementAt(this.m_next);
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void allowDetachToRelease(boolean allowRelease) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean isFresh() {
        return this.m_next == 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void runTo(int index) {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", null));
        }
        if (index >= 0 && this.m_next < this.m_firstFree) {
            this.m_next = index;
        } else {
            this.m_next = this.m_firstFree - 1;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int item(int index) {
        runTo(index);
        return elementAt(index);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getLength() {
        runTo(-1);
        return size();
    }

    public void addNode(int n2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        addElement(n2);
    }

    public void insertNode(int n2, int pos) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        insertElementAt(n2, pos);
    }

    public void removeNode(int n2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        removeElement(n2);
    }

    public void addNodes(DTMIterator iterator) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        if (null == iterator) {
            return;
        }
        while (true) {
            int obj = iterator.nextNode();
            if (-1 != obj) {
                addElement(obj);
            } else {
                return;
            }
        }
    }

    public void addNodesInDocOrder(DTMIterator iterator, XPathContext support) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        while (true) {
            int node = iterator.nextNode();
            if (-1 != node) {
                addNodeInDocOrder(node, support);
            } else {
                return;
            }
        }
    }

    public int addNodeInDocOrder(int node, boolean test, XPathContext support) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        int insertIndex = -1;
        if (test) {
            int size = size();
            int i2 = size - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                }
                int child = elementAt(i2);
                if (child == node) {
                    i2 = -2;
                    break;
                }
                DTM dtm = support.getDTM(node);
                if (!dtm.isNodeAfter(node, child)) {
                    break;
                }
                i2--;
            }
            if (i2 != -2) {
                insertIndex = i2 + 1;
                insertElementAt(node, insertIndex);
            }
        } else {
            insertIndex = size();
            boolean foundit = false;
            int i3 = 0;
            while (true) {
                if (i3 >= insertIndex) {
                    break;
                }
                if (i3 != node) {
                    i3++;
                } else {
                    foundit = true;
                    break;
                }
            }
            if (!foundit) {
                addElement(node);
            }
        }
        return insertIndex;
    }

    public int addNodeInDocOrder(int node, XPathContext support) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        return addNodeInDocOrder(node, true, support);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public int size() {
        return super.size();
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public void addElement(int value) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.addElement(value);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public void insertElementAt(int value, int at2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.insertElementAt(value, at2);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public void appendNodes(NodeVector nodes) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.appendNodes(nodes);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public void removeAllElements() {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.removeAllElements();
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public boolean removeElement(int s2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        return super.removeElement(s2);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public void removeElementAt(int i2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.removeElementAt(i2);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public void setElementAt(int node, int index) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.setElementAt(node, index);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setItem(int node, int index) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.setElementAt(node, index);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public int elementAt(int i2) {
        runTo(i2);
        return super.elementAt(i2);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public boolean contains(int s2) {
        runTo(-1);
        return super.contains(s2);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public int indexOf(int elem, int index) {
        runTo(-1);
        return super.indexOf(elem, index);
    }

    @Override // com.sun.org.apache.xml.internal.utils.NodeVector
    public int indexOf(int elem) {
        runTo(-1);
        return super.indexOf(elem);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getCurrentPos() {
        return this.m_next;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setCurrentPos(int i2) {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", null));
        }
        this.m_next = i2;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getCurrentNode() {
        if (!this.m_cacheNodes) {
            throw new RuntimeException("This NodeSetDTM can not do indexing or counting functions!");
        }
        int saved = this.m_next;
        int current = this.m_next > 0 ? this.m_next - 1 : this.m_next;
        int n2 = current < this.m_firstFree ? elementAt(current) : -1;
        this.m_next = saved;
        return n2;
    }

    public boolean getShouldCacheNodes() {
        return this.m_cacheNodes;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setShouldCacheNodes(boolean b2) {
        if (!isFresh()) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_CALL_SETSHOULDCACHENODE", null));
        }
        this.m_cacheNodes = b2;
        this.m_mutable = true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean isMutable() {
        return this.m_mutable;
    }

    public int getLast() {
        return this.m_last;
    }

    public void setLast(int last) {
        this.m_last = last;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean isDocOrdered() {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getAxis() {
        return -1;
    }
}
