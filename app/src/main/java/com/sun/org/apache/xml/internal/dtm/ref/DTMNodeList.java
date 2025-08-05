package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMNodeList.class */
public class DTMNodeList extends DTMNodeListBase {
    private DTMIterator m_iter;

    private DTMNodeList() {
    }

    public DTMNodeList(DTMIterator dtmIterator) {
        if (dtmIterator != null) {
            int pos = dtmIterator.getCurrentPos();
            try {
                this.m_iter = dtmIterator.cloneWithReset();
            } catch (CloneNotSupportedException e2) {
                this.m_iter = dtmIterator;
            }
            this.m_iter.setShouldCacheNodes(true);
            this.m_iter.runTo(-1);
            this.m_iter.setCurrentPos(pos);
        }
    }

    public DTMIterator getDTMIterator() {
        return this.m_iter;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMNodeListBase, org.w3c.dom.NodeList
    public Node item(int index) {
        int handle;
        if (this.m_iter == null || (handle = this.m_iter.item(index)) == -1) {
            return null;
        }
        return this.m_iter.getDTM(handle).getNode(handle);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMNodeListBase, org.w3c.dom.NodeList
    public int getLength() {
        if (this.m_iter != null) {
            return this.m_iter.getLength();
        }
        return 0;
    }
}
