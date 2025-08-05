package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.utils.IntVector;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMAxisIterNodeList.class */
public class DTMAxisIterNodeList extends DTMNodeListBase {
    private DTM m_dtm;
    private DTMAxisIterator m_iter;
    private IntVector m_cachedNodes;
    private int m_last;

    private DTMAxisIterNodeList() {
        this.m_last = -1;
    }

    public DTMAxisIterNodeList(DTM dtm, DTMAxisIterator dtmAxisIterator) {
        this.m_last = -1;
        if (dtmAxisIterator == null) {
            this.m_last = 0;
        } else {
            this.m_cachedNodes = new IntVector();
            this.m_dtm = dtm;
        }
        this.m_iter = dtmAxisIterator;
    }

    public DTMAxisIterator getDTMAxisIterator() {
        return this.m_iter;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMNodeListBase, org.w3c.dom.NodeList
    public Node item(int index) {
        if (this.m_iter != null) {
            int node = 0;
            int count = this.m_cachedNodes.size();
            if (count > index) {
                int node2 = this.m_cachedNodes.elementAt(index);
                return this.m_dtm.getNode(node2);
            }
            if (this.m_last == -1) {
                while (count <= index) {
                    int next = this.m_iter.next();
                    node = next;
                    if (next == -1) {
                        break;
                    }
                    this.m_cachedNodes.addElement(node);
                    count++;
                }
                if (node == -1) {
                    this.m_last = count;
                    return null;
                }
                return this.m_dtm.getNode(node);
            }
            return null;
        }
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMNodeListBase, org.w3c.dom.NodeList
    public int getLength() {
        if (this.m_last == -1) {
            while (true) {
                int node = this.m_iter.next();
                if (node == -1) {
                    break;
                }
                this.m_cachedNodes.addElement(node);
            }
            this.m_last = this.m_cachedNodes.size();
        }
        return this.m_last;
    }
}
