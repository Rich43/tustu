package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTMDOMException;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMNodeIterator.class */
public class DTMNodeIterator implements NodeIterator {
    private DTMIterator dtm_iter;
    private boolean valid = true;

    public DTMNodeIterator(DTMIterator dtmIterator) {
        try {
            this.dtm_iter = (DTMIterator) dtmIterator.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new WrappedRuntimeException(cnse);
        }
    }

    public DTMIterator getDTMIterator() {
        return this.dtm_iter;
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public void detach() {
        this.valid = false;
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public boolean getExpandEntityReferences() {
        return false;
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public NodeFilter getFilter() {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public Node getRoot() {
        int handle = this.dtm_iter.getRoot();
        return this.dtm_iter.getDTM(handle).getNode(handle);
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public int getWhatToShow() {
        return this.dtm_iter.getWhatToShow();
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public Node nextNode() throws DOMException {
        if (!this.valid) {
            throw new DTMDOMException((short) 11);
        }
        int handle = this.dtm_iter.nextNode();
        if (handle == -1) {
            return null;
        }
        return this.dtm_iter.getDTM(handle).getNode(handle);
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public Node previousNode() {
        if (!this.valid) {
            throw new DTMDOMException((short) 11);
        }
        int handle = this.dtm_iter.previousNode();
        if (handle == -1) {
            return null;
        }
        return this.dtm_iter.getDTM(handle).getNode(handle);
    }
}
