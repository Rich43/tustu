package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/AxesWalker.class */
public class AxesWalker extends PredicatedNodeTest implements Cloneable, PathComponent, ExpressionOwner {
    static final long serialVersionUID = -2966031951306601247L;
    private DTM m_dtm;
    transient int m_root;
    private transient int m_currentNode;
    transient boolean m_isFresh;
    protected AxesWalker m_nextWalker;
    AxesWalker m_prevWalker;
    protected int m_axis;
    protected DTMAxisTraverser m_traverser;

    public AxesWalker(LocPathIterator locPathIterator, int axis) {
        super(locPathIterator);
        this.m_root = -1;
        this.m_currentNode = -1;
        this.m_axis = -1;
        this.m_axis = axis;
    }

    public final WalkingIterator wi() {
        return (WalkingIterator) this.m_lpi;
    }

    public void init(Compiler compiler, int opPos, int stepType) throws TransformerException {
        initPredicateInfo(compiler, opPos);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public Object clone() throws CloneNotSupportedException {
        AxesWalker clone = (AxesWalker) super.clone();
        return clone;
    }

    AxesWalker cloneDeep(WalkingIterator cloneOwner, Vector cloneList) throws CloneNotSupportedException {
        AxesWalker clone = findClone(this, cloneList);
        if (null != clone) {
            return clone;
        }
        AxesWalker clone2 = (AxesWalker) clone();
        clone2.setLocPathIterator(cloneOwner);
        if (null != cloneList) {
            cloneList.addElement(this);
            cloneList.addElement(clone2);
        }
        if (wi().m_lastUsedWalker == this) {
            cloneOwner.m_lastUsedWalker = clone2;
        }
        if (null != this.m_nextWalker) {
            clone2.m_nextWalker = this.m_nextWalker.cloneDeep(cloneOwner, cloneList);
        }
        if (null != cloneList) {
            if (null != this.m_prevWalker) {
                clone2.m_prevWalker = this.m_prevWalker.cloneDeep(cloneOwner, cloneList);
            }
        } else if (null != this.m_nextWalker) {
            clone2.m_nextWalker.m_prevWalker = clone2;
        }
        return clone2;
    }

    static AxesWalker findClone(AxesWalker key, Vector cloneList) {
        if (null != cloneList) {
            int n2 = cloneList.size();
            for (int i2 = 0; i2 < n2; i2 += 2) {
                if (key == cloneList.elementAt(i2)) {
                    return (AxesWalker) cloneList.elementAt(i2 + 1);
                }
            }
            return null;
        }
        return null;
    }

    public void detach() {
        this.m_currentNode = -1;
        this.m_dtm = null;
        this.m_traverser = null;
        this.m_isFresh = true;
        this.m_root = -1;
    }

    public int getRoot() {
        return this.m_root;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PathComponent
    public int getAnalysisBits() {
        int axis = getAxis();
        int bit = WalkerFactory.getAnalysisBitFromAxes(axis);
        return bit;
    }

    public void setRoot(int root) {
        XPathContext xctxt = wi().getXPathContext();
        this.m_dtm = xctxt.getDTM(root);
        this.m_traverser = this.m_dtm.getAxisTraverser(this.m_axis);
        this.m_isFresh = true;
        this.m_foundLast = false;
        this.m_root = root;
        this.m_currentNode = root;
        if (-1 == root) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_SETTING_WALKER_ROOT_TO_NULL", null));
        }
        resetProximityPositions();
    }

    public final int getCurrentNode() {
        return this.m_currentNode;
    }

    public void setNextWalker(AxesWalker walker) {
        this.m_nextWalker = walker;
    }

    public AxesWalker getNextWalker() {
        return this.m_nextWalker;
    }

    public void setPrevWalker(AxesWalker walker) {
        this.m_prevWalker = walker;
    }

    public AxesWalker getPrevWalker() {
        return this.m_prevWalker;
    }

    private int returnNextNode(int n2) {
        return n2;
    }

    protected int getNextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        if (this.m_isFresh) {
            this.m_currentNode = this.m_traverser.first(this.m_root);
            this.m_isFresh = false;
        } else if (-1 != this.m_currentNode) {
            this.m_currentNode = this.m_traverser.next(this.m_root, this.m_currentNode);
        }
        if (-1 == this.m_currentNode) {
            this.m_foundLast = true;
        }
        return this.m_currentNode;
    }

    public int nextNode() {
        int nextNode = -1;
        AxesWalker walker = wi().getLastUsedWalker();
        while (true) {
            if (null == walker) {
                break;
            }
            nextNode = walker.getNextNode();
            if (-1 == nextNode) {
                walker = walker.m_prevWalker;
            } else if (walker.acceptNode(nextNode) != 1) {
                continue;
            } else {
                if (null == walker.m_nextWalker) {
                    wi().setLastUsedWalker(walker);
                    break;
                }
                AxesWalker prev = walker;
                walker = walker.m_nextWalker;
                walker.setRoot(nextNode);
                walker.m_prevWalker = prev;
            }
        }
        return nextNode;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.axes.SubContextList
    public int getLastPos(XPathContext xctxt) {
        int pos = getProximityPosition();
        try {
            AxesWalker walker = (AxesWalker) clone();
            walker.setPredicateCount(this.m_predicateIndex);
            walker.setNextWalker(null);
            walker.setPrevWalker(null);
            WalkingIterator lpi = wi();
            AxesWalker savedWalker = lpi.getLastUsedWalker();
            try {
                lpi.setLastUsedWalker(walker);
                while (-1 != walker.nextNode()) {
                    pos++;
                }
                return pos;
            } finally {
                lpi.setLastUsedWalker(savedWalker);
            }
        } catch (CloneNotSupportedException e2) {
            return -1;
        }
    }

    public void setDefaultDTM(DTM dtm) {
        this.m_dtm = dtm;
    }

    public DTM getDTM(int node) {
        return wi().getXPathContext().getDTM(node);
    }

    public boolean isDocOrdered() {
        return true;
    }

    public int getAxis() {
        return this.m_axis;
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        if (visitor.visitStep(owner, this)) {
            callPredicateVisitors(visitor);
            if (null != this.m_nextWalker) {
                this.m_nextWalker.callVisitors(this, visitor);
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public Expression getExpression() {
        return this.m_nextWalker;
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public void setExpression(Expression exp) {
        exp.exprSetParent(this);
        this.m_nextWalker = (AxesWalker) exp;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        AxesWalker walker = (AxesWalker) expr;
        if (this.m_axis != walker.m_axis) {
            return false;
        }
        return true;
    }
}
