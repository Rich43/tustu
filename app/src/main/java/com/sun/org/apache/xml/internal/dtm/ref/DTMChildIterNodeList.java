package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTM;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMChildIterNodeList.class */
public class DTMChildIterNodeList extends DTMNodeListBase {
    private int m_firstChild;
    private DTM m_parentDTM;

    private DTMChildIterNodeList() {
    }

    public DTMChildIterNodeList(DTM parentDTM, int parentHandle) {
        this.m_parentDTM = parentDTM;
        this.m_firstChild = parentDTM.getFirstChild(parentHandle);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMNodeListBase, org.w3c.dom.NodeList
    public Node item(int index) {
        int handle;
        int nextSibling = this.m_firstChild;
        while (true) {
            handle = nextSibling;
            index--;
            if (index < 0 || handle == -1) {
                break;
            }
            nextSibling = this.m_parentDTM.getNextSibling(handle);
        }
        if (handle == -1) {
            return null;
        }
        return this.m_parentDTM.getNode(handle);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMNodeListBase, org.w3c.dom.NodeList
    public int getLength() {
        int count = 0;
        int nextSibling = this.m_firstChild;
        while (true) {
            int handle = nextSibling;
            if (handle != -1) {
                count++;
                nextSibling = this.m_parentDTM.getNextSibling(handle);
            } else {
                return count;
            }
        }
    }
}
