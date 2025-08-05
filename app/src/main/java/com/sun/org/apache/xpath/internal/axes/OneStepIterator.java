package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.compiler.OpMap;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/OneStepIterator.class */
public class OneStepIterator extends ChildTestIterator {
    static final long serialVersionUID = 4623710779664998283L;
    protected int m_axis;
    protected DTMAxisIterator m_iterator;

    OneStepIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
        super(compiler, opPos, analysis);
        this.m_axis = -1;
        int firstStepPos = OpMap.getFirstChildPos(opPos);
        this.m_axis = WalkerFactory.getAxisFromStep(compiler, firstStepPos);
    }

    public OneStepIterator(DTMAxisIterator iterator, int axis) throws TransformerException {
        super(null);
        this.m_axis = -1;
        this.m_iterator = iterator;
        this.m_axis = axis;
        initNodeTest(-1);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int context, Object environment) {
        super.setRoot(context, environment);
        if (this.m_axis > -1) {
            this.m_iterator = this.m_cdtm.getAxisIterator(this.m_axis);
        }
        this.m_iterator.setStartNode(this.m_context);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        if (this.m_allowDetach) {
            if (this.m_axis > -1) {
                this.m_iterator = null;
            }
            super.detach();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.BasicTestIterator
    protected int getNextNode() {
        int next = this.m_iterator.next();
        this.m_lastFetched = next;
        return next;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public Object clone() throws CloneNotSupportedException {
        OneStepIterator clone = (OneStepIterator) super.clone();
        if (this.m_iterator != null) {
            clone.m_iterator = this.m_iterator.cloneIterator();
        }
        return clone;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.BasicTestIterator, com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        OneStepIterator clone = (OneStepIterator) super.cloneWithReset();
        clone.m_iterator = this.m_iterator;
        return clone;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public boolean isReverseAxes() {
        return this.m_iterator.isReverse();
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    protected int getProximityPosition(int predicateIndex) {
        if (!isReverseAxes()) {
            return super.getProximityPosition(predicateIndex);
        }
        if (predicateIndex < 0) {
            return -1;
        }
        if (this.m_proximityPositions[predicateIndex] <= 0) {
            XPathContext xctxt = getXPathContext();
            try {
                OneStepIterator clone = (OneStepIterator) clone();
                int root = getRoot();
                xctxt.pushCurrentNode(root);
                clone.setRoot(root, xctxt);
                clone.m_predCount = predicateIndex;
                int count = 1;
                while (-1 != clone.nextNode()) {
                    count++;
                }
                int[] iArr = this.m_proximityPositions;
                iArr[predicateIndex] = iArr[predicateIndex] + count;
                xctxt.popCurrentNode();
            } catch (CloneNotSupportedException e2) {
                xctxt.popCurrentNode();
            } catch (Throwable th) {
                xctxt.popCurrentNode();
                throw th;
            }
        }
        return this.m_proximityPositions[predicateIndex];
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getLength() {
        if (!isReverseAxes()) {
            return super.getLength();
        }
        boolean isPredicateTest = this == this.m_execContext.getSubContextList();
        getPredicateCount();
        if (-1 != this.m_length && isPredicateTest && this.m_predicateIndex < 1) {
            return this.m_length;
        }
        int count = 0;
        XPathContext xctxt = getXPathContext();
        try {
            OneStepIterator clone = (OneStepIterator) cloneWithReset();
            int root = getRoot();
            xctxt.pushCurrentNode(root);
            clone.setRoot(root, xctxt);
            clone.m_predCount = this.m_predicateIndex;
            while (-1 != clone.nextNode()) {
                count++;
            }
            xctxt.popCurrentNode();
        } catch (CloneNotSupportedException e2) {
            xctxt.popCurrentNode();
        } catch (Throwable th) {
            xctxt.popCurrentNode();
            throw th;
        }
        if (isPredicateTest && this.m_predicateIndex < 1) {
            this.m_length = count;
        }
        return count;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    protected void countProximityPosition(int i2) {
        if (!isReverseAxes()) {
            super.countProximityPosition(i2);
        } else if (i2 < this.m_proximityPositions.length) {
            int[] iArr = this.m_proximityPositions;
            iArr[i2] = iArr[i2] - 1;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void reset() {
        super.reset();
        if (null != this.m_iterator) {
            this.m_iterator.reset();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getAxis() {
        return this.m_axis;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr) || this.m_axis != ((OneStepIterator) expr).m_axis) {
            return false;
        }
        return true;
    }
}
