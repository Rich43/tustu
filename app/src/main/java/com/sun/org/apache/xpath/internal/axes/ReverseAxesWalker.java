package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xpath.internal.XPathContext;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/ReverseAxesWalker.class */
public class ReverseAxesWalker extends AxesWalker {
    static final long serialVersionUID = 2847007647832768941L;
    protected DTMAxisIterator m_iterator;

    ReverseAxesWalker(LocPathIterator locPathIterator, int axis) {
        super(locPathIterator, axis);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    public void setRoot(int root) {
        super.setRoot(root);
        this.m_iterator = getDTM(root).getAxisIterator(this.m_axis);
        this.m_iterator.setStartNode(root);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    public void detach() {
        this.m_iterator = null;
        super.detach();
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    protected int getNextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        int next = this.m_iterator.next();
        if (this.m_isFresh) {
            this.m_isFresh = false;
        }
        if (-1 == next) {
            this.m_foundLast = true;
        }
        return next;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public boolean isReverseAxes() {
        return true;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    protected int getProximityPosition(int predicateIndex) {
        if (predicateIndex < 0) {
            return -1;
        }
        int count = this.m_proximityPositions[predicateIndex];
        if (count <= 0) {
            AxesWalker savedWalker = wi().getLastUsedWalker();
            try {
                ReverseAxesWalker clone = (ReverseAxesWalker) clone();
                clone.setRoot(getRoot());
                clone.setPredicateCount(predicateIndex);
                clone.setPrevWalker(null);
                clone.setNextWalker(null);
                wi().setLastUsedWalker(clone);
                count++;
                while (-1 != clone.nextNode()) {
                    count++;
                }
                this.m_proximityPositions[predicateIndex] = count;
                wi().setLastUsedWalker(savedWalker);
            } catch (CloneNotSupportedException e2) {
                wi().setLastUsedWalker(savedWalker);
            } catch (Throwable th) {
                wi().setLastUsedWalker(savedWalker);
                throw th;
            }
        }
        return count;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    protected void countProximityPosition(int i2) {
        if (i2 < this.m_proximityPositions.length) {
            int[] iArr = this.m_proximityPositions;
            iArr[i2] = iArr[i2] - 1;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker, com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.axes.SubContextList
    public int getLastPos(XPathContext xctxt) {
        int count = 0;
        AxesWalker savedWalker = wi().getLastUsedWalker();
        try {
            ReverseAxesWalker clone = (ReverseAxesWalker) clone();
            clone.setRoot(getRoot());
            clone.setPredicateCount(getPredicateCount() - 1);
            clone.setPrevWalker(null);
            clone.setNextWalker(null);
            wi().setLastUsedWalker(clone);
            while (-1 != clone.nextNode()) {
                count++;
            }
            wi().setLastUsedWalker(savedWalker);
        } catch (CloneNotSupportedException e2) {
            wi().setLastUsedWalker(savedWalker);
        } catch (Throwable th) {
            wi().setLastUsedWalker(savedWalker);
            throw th;
        }
        return count;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    public boolean isDocOrdered() {
        return false;
    }
}
