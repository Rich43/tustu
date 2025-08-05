package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.utils.NodeVector;
import com.sun.org.apache.xpath.internal.NodeSetDTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/NodeSequence.class */
public class NodeSequence extends XObject implements DTMIterator, Cloneable, PathComponent {
    static final long serialVersionUID = 3866261934726581044L;
    protected int m_last;
    protected int m_next;
    private IteratorCache m_cache;
    protected DTMIterator m_iter;
    protected DTMManager m_dtmMgr;

    protected NodeVector getVector() {
        NodeVector nv = this.m_cache != null ? this.m_cache.getVector() : null;
        return nv;
    }

    private IteratorCache getCache() {
        return this.m_cache;
    }

    protected void SetVector(NodeVector v2) {
        setObject(v2);
    }

    public boolean hasCache() {
        NodeVector nv = getVector();
        return nv != null;
    }

    private boolean cacheComplete() {
        boolean complete;
        if (this.m_cache == null) {
            complete = false;
        } else {
            complete = this.m_cache.isComplete();
        }
        return complete;
    }

    private void markCacheComplete() {
        NodeVector nv = getVector();
        if (nv == null) {
            return;
        }
        this.m_cache.setCacheComplete(true);
    }

    public final void setIter(DTMIterator iter) {
        this.m_iter = iter;
    }

    public final DTMIterator getContainedIter() {
        return this.m_iter;
    }

    private NodeSequence(DTMIterator iter, int context, XPathContext xctxt, boolean shouldCacheNodes) {
        this.m_last = -1;
        this.m_next = 0;
        setIter(iter);
        setRoot(context, xctxt);
        setShouldCacheNodes(shouldCacheNodes);
    }

    public NodeSequence(Object nodeVector) {
        super(nodeVector);
        this.m_last = -1;
        this.m_next = 0;
        if (nodeVector instanceof NodeVector) {
            SetVector((NodeVector) nodeVector);
        }
        if (null != nodeVector) {
            assertion(nodeVector instanceof NodeVector, "Must have a NodeVector as the object for NodeSequence!");
            if (nodeVector instanceof DTMIterator) {
                setIter((DTMIterator) nodeVector);
                this.m_last = ((DTMIterator) nodeVector).getLength();
            }
        }
    }

    private NodeSequence(DTMManager dtmMgr) {
        super(new NodeVector());
        this.m_last = -1;
        this.m_next = 0;
        this.m_last = 0;
        this.m_dtmMgr = dtmMgr;
    }

    public NodeSequence() {
        this.m_last = -1;
        this.m_next = 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public DTM getDTM(int nodeHandle) {
        DTMManager mgr = getDTMManager();
        if (null != mgr) {
            return getDTMManager().getDTM(nodeHandle);
        }
        assertion(false, "Can not get a DTM Unless a DTMManager has been set!");
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public DTMManager getDTMManager() {
        return this.m_dtmMgr;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getRoot() {
        if (null != this.m_iter) {
            return this.m_iter.getRoot();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int nodeHandle, Object environment) {
        if (nodeHandle == -1) {
            throw new RuntimeException("Unable to evaluate expression using this context");
        }
        if (null != this.m_iter) {
            XPathContext xctxt = (XPathContext) environment;
            this.m_dtmMgr = xctxt.getDTMManager();
            this.m_iter.setRoot(nodeHandle, environment);
            if (!this.m_iter.isDocOrdered()) {
                if (!hasCache()) {
                    setShouldCacheNodes(true);
                }
                runTo(-1);
                this.m_next = 0;
                return;
            }
            return;
        }
        assertion(false, "Can not setRoot on a non-iterated NodeSequence!");
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void reset() {
        this.m_next = 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getWhatToShow() {
        if (hasCache()) {
            return -17;
        }
        return this.m_iter.getWhatToShow();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean getExpandEntityReferences() {
        if (null != this.m_iter) {
            return this.m_iter.getExpandEntityReferences();
        }
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        NodeVector vec = getVector();
        if (null != vec) {
            if (this.m_next < vec.size()) {
                int next = vec.elementAt(this.m_next);
                this.m_next++;
                return next;
            }
            if (cacheComplete() || -1 != this.m_last || null == this.m_iter) {
                this.m_next++;
                return -1;
            }
        }
        if (null == this.m_iter) {
            return -1;
        }
        int next2 = this.m_iter.nextNode();
        if (-1 != next2) {
            if (hasCache()) {
                if (this.m_iter.isDocOrdered()) {
                    getVector().addElement(next2);
                    this.m_next++;
                } else {
                    int insertIndex = addNodeInDocOrder(next2);
                    if (insertIndex >= 0) {
                        this.m_next++;
                    }
                }
            } else {
                this.m_next++;
            }
        } else {
            markCacheComplete();
            this.m_last = this.m_next;
            this.m_next++;
        }
        return next2;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int previousNode() {
        if (hasCache()) {
            if (this.m_next <= 0) {
                return -1;
            }
            this.m_next--;
            return item(this.m_next);
        }
        this.m_iter.previousNode();
        this.m_next = this.m_iter.getCurrentPos();
        return this.m_next;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        if (null != this.m_iter) {
            this.m_iter.detach();
        }
        super.detach();
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void allowDetachToRelease(boolean allowRelease) {
        if (false == allowRelease && !hasCache()) {
            setShouldCacheNodes(true);
        }
        if (null != this.m_iter) {
            this.m_iter.allowDetachToRelease(allowRelease);
        }
        super.allowDetachToRelease(allowRelease);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getCurrentNode() {
        if (hasCache()) {
            int currentIndex = this.m_next - 1;
            NodeVector vec = getVector();
            if (currentIndex >= 0 && currentIndex < vec.size()) {
                return vec.elementAt(currentIndex);
            }
            return -1;
        }
        if (null != this.m_iter) {
            return this.m_iter.getCurrentNode();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean isFresh() {
        return 0 == this.m_next;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setShouldCacheNodes(boolean b2) {
        if (b2) {
            if (!hasCache()) {
                SetVector(new NodeVector());
                return;
            }
            return;
        }
        SetVector(null);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean isMutable() {
        return hasCache();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getCurrentPos() {
        return this.m_next;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void runTo(int index) {
        if (-1 == index) {
            int pos = this.m_next;
            while (-1 != nextNode()) {
            }
            this.m_next = pos;
        } else {
            if (this.m_next == index) {
                return;
            }
            if (hasCache() && index < getVector().size()) {
                this.m_next = index;
                return;
            }
            if (null == getVector() && index < this.m_next) {
                while (this.m_next >= index && -1 != previousNode()) {
                }
            } else {
                while (this.m_next < index && -1 != nextNode()) {
                }
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setCurrentPos(int i2) {
        runTo(i2);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int item(int index) {
        setCurrentPos(index);
        int n2 = nextNode();
        this.m_next = index;
        return n2;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setItem(int node, int index) {
        NodeVector vec = getVector();
        if (null != vec) {
            int oldNode = vec.elementAt(index);
            if (oldNode != node && this.m_cache.useCount() > 1) {
                IteratorCache newCache = new IteratorCache();
                try {
                    NodeVector nv = (NodeVector) vec.clone();
                    newCache.setVector(nv);
                    newCache.setCacheComplete(true);
                    this.m_cache = newCache;
                    vec = nv;
                    super.setObject(nv);
                } catch (CloneNotSupportedException e2) {
                    e2.printStackTrace();
                    RuntimeException rte = new RuntimeException(e2.getMessage());
                    throw rte;
                }
            }
            vec.setElementAt(node, index);
            this.m_last = vec.size();
            return;
        }
        this.m_iter.setItem(node, index);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getLength() {
        IteratorCache cache = getCache();
        if (cache == null) {
            if (-1 != this.m_last) {
                return this.m_last;
            }
            int length = this.m_iter.getLength();
            this.m_last = length;
            return length;
        }
        if (!cache.isComplete()) {
            if (this.m_iter instanceof NodeSetDTM) {
                return this.m_iter.getLength();
            }
            if (-1 == this.m_last) {
                int pos = this.m_next;
                runTo(-1);
                this.m_next = pos;
            }
            return this.m_last;
        }
        NodeVector nv = cache.getVector();
        return nv.size();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        NodeSequence seq = (NodeSequence) super.clone();
        seq.m_next = 0;
        if (this.m_cache != null) {
            this.m_cache.increaseUseCount();
        }
        return seq;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public Object clone() throws CloneNotSupportedException {
        NodeSequence clone = (NodeSequence) super.clone();
        if (null != this.m_iter) {
            clone.m_iter = (DTMIterator) this.m_iter.clone();
        }
        if (this.m_cache != null) {
            this.m_cache.increaseUseCount();
        }
        return clone;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean isDocOrdered() {
        if (null != this.m_iter) {
            return this.m_iter.isDocOrdered();
        }
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getAxis() {
        if (null != this.m_iter) {
            return this.m_iter.getAxis();
        }
        assertion(false, "Can not getAxis from a non-iterated node sequence!");
        return 0;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PathComponent
    public int getAnalysisBits() {
        if (null != this.m_iter && (this.m_iter instanceof PathComponent)) {
            return ((PathComponent) this.m_iter).getAnalysisBits();
        }
        return 0;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
    }

    protected int addNodeInDocOrder(int node) {
        assertion(hasCache(), "addNodeInDocOrder must be done on a mutable sequence!");
        int insertIndex = -1;
        NodeVector vec = getVector();
        int size = vec.size();
        int i2 = size - 1;
        while (true) {
            if (i2 < 0) {
                break;
            }
            int child = vec.elementAt(i2);
            if (child == node) {
                i2 = -2;
                break;
            }
            DTM dtm = this.m_dtmMgr.getDTM(node);
            if (!dtm.isNodeAfter(node, child)) {
                break;
            }
            i2--;
        }
        if (i2 != -2) {
            insertIndex = i2 + 1;
            vec.insertElementAt(node, insertIndex);
        }
        return insertIndex;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    protected void setObject(Object obj) {
        if (!(obj instanceof NodeVector)) {
            if (obj instanceof IteratorCache) {
                IteratorCache cache = (IteratorCache) obj;
                this.m_cache = cache;
                this.m_cache.increaseUseCount();
                super.setObject(cache.getVector());
                return;
            }
            super.setObject(obj);
            return;
        }
        super.setObject(obj);
        NodeVector v2 = (NodeVector) obj;
        if (this.m_cache == null) {
            if (v2 != null) {
                this.m_cache = new IteratorCache();
                this.m_cache.setVector(v2);
                return;
            }
            return;
        }
        this.m_cache.setVector(v2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/NodeSequence$IteratorCache.class */
    protected static final class IteratorCache {
        private NodeVector m_vec2 = null;
        private boolean m_isComplete2 = false;
        private int m_useCount2 = 1;

        IteratorCache() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int useCount() {
            return this.m_useCount2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void increaseUseCount() {
            if (this.m_vec2 != null) {
                this.m_useCount2++;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setVector(NodeVector nv) {
            this.m_vec2 = nv;
            this.m_useCount2 = 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public NodeVector getVector() {
            return this.m_vec2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCacheComplete(boolean b2) {
            this.m_isComplete2 = b2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isComplete() {
            return this.m_isComplete2;
        }
    }

    protected IteratorCache getIteratorCache() {
        return this.m_cache;
    }
}
