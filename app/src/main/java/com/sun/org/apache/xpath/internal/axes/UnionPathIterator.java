package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.compiler.OpMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/UnionPathIterator.class */
public class UnionPathIterator extends LocPathIterator implements Cloneable, DTMIterator, Serializable, PathComponent {
    static final long serialVersionUID = -3910351546843826781L;
    protected LocPathIterator[] m_exprs;
    protected DTMIterator[] m_iterators;

    public UnionPathIterator() {
        this.m_iterators = null;
        this.m_exprs = null;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int context, Object environment) {
        super.setRoot(context, environment);
        try {
            if (null != this.m_exprs) {
                int n2 = this.m_exprs.length;
                DTMIterator[] newIters = new DTMIterator[n2];
                for (int i2 = 0; i2 < n2; i2++) {
                    DTMIterator iter = this.m_exprs[i2].asIterator(this.m_execContext, context);
                    newIters[i2] = iter;
                    iter.nextNode();
                }
                this.m_iterators = newIters;
            }
        } catch (Exception e2) {
            throw new WrappedRuntimeException(e2);
        }
    }

    public void addIterator(DTMIterator dTMIterator) {
        if (null == this.m_iterators) {
            this.m_iterators = new DTMIterator[1];
            this.m_iterators[0] = dTMIterator;
        } else {
            DTMIterator[] exprs = this.m_iterators;
            int len = this.m_iterators.length;
            this.m_iterators = new DTMIterator[len + 1];
            System.arraycopy(exprs, 0, this.m_iterators, 0, len);
            this.m_iterators[len] = dTMIterator;
        }
        dTMIterator.nextNode();
        if (dTMIterator instanceof Expression) {
            ((Expression) dTMIterator).exprSetParent(this);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        if (this.m_allowDetach && null != this.m_iterators) {
            int n2 = this.m_iterators.length;
            for (int i2 = 0; i2 < n2; i2++) {
                this.m_iterators[i2].detach();
            }
            this.m_iterators = null;
        }
    }

    public UnionPathIterator(Compiler compiler, int opPos) throws TransformerException {
        loadLocationPaths(compiler, OpMap.getFirstChildPos(opPos), 0);
    }

    public static LocPathIterator createUnionIterator(Compiler compiler, int opPos) throws TransformerException {
        UnionPathIterator upi = new UnionPathIterator(compiler, opPos);
        int nPaths = upi.m_exprs.length;
        boolean isAllChildIterators = true;
        int i2 = 0;
        while (true) {
            if (i2 >= nPaths) {
                break;
            }
            LocPathIterator lpi = upi.m_exprs[i2];
            if (lpi.getAxis() != 3) {
                isAllChildIterators = false;
                break;
            }
            if (!HasPositionalPredChecker.check(lpi)) {
                i2++;
            } else {
                isAllChildIterators = false;
                break;
            }
        }
        if (isAllChildIterators) {
            UnionChildIterator uci = new UnionChildIterator();
            for (int i3 = 0; i3 < nPaths; i3++) {
                uci.addNodeTest(upi.m_exprs[i3]);
            }
            return uci;
        }
        return upi;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xpath.internal.axes.PathComponent
    public int getAnalysisBits() {
        int bits = 0;
        if (this.m_exprs != null) {
            int n2 = this.m_exprs.length;
            for (int i2 = 0; i2 < n2; i2++) {
                int bit = this.m_exprs[i2].getAnalysisBits();
                bits |= bit;
            }
        }
        return bits;
    }

    private void readObject(ObjectInputStream stream) throws TransformerException, IOException {
        try {
            stream.defaultReadObject();
            this.m_clones = new IteratorPool(this);
        } catch (ClassNotFoundException cnfe) {
            throw new TransformerException(cnfe);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public Object clone() throws CloneNotSupportedException {
        UnionPathIterator clone = (UnionPathIterator) super.clone();
        if (this.m_iterators != null) {
            int n2 = this.m_iterators.length;
            clone.m_iterators = new DTMIterator[n2];
            for (int i2 = 0; i2 < n2; i2++) {
                clone.m_iterators[i2] = (DTMIterator) this.m_iterators[i2].clone();
            }
        }
        return clone;
    }

    protected LocPathIterator createDTMIterator(Compiler compiler, int opPos) throws TransformerException {
        LocPathIterator lpi = (LocPathIterator) WalkerFactory.newDTMIterator(compiler, opPos, compiler.getLocationPathDepth() <= 0);
        return lpi;
    }

    protected void loadLocationPaths(Compiler compiler, int opPos, int count) throws TransformerException {
        int steptype = compiler.getOp(opPos);
        if (steptype == 28) {
            loadLocationPaths(compiler, compiler.getNextOpPos(opPos), count + 1);
            this.m_exprs[count] = createDTMIterator(compiler, opPos);
            this.m_exprs[count].exprSetParent(this);
            return;
        }
        switch (steptype) {
            case 22:
            case 23:
            case 24:
            case 25:
                loadLocationPaths(compiler, compiler.getNextOpPos(opPos), count + 1);
                WalkingIterator iter = new WalkingIterator(compiler.getNamespaceContext());
                iter.exprSetParent(this);
                if (compiler.getLocationPathDepth() <= 0) {
                    iter.setIsTopLevel(true);
                }
                iter.m_firstWalker = new FilterExprWalker(iter);
                iter.m_firstWalker.init(compiler, opPos, steptype);
                this.m_exprs[count] = iter;
                break;
            default:
                this.m_exprs = new LocPathIterator[count];
                break;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        int earliestNode = -1;
        if (null != this.m_iterators) {
            int n2 = this.m_iterators.length;
            int iteratorUsed = -1;
            for (int i2 = 0; i2 < n2; i2++) {
                int node = this.m_iterators[i2].getCurrentNode();
                if (-1 != node) {
                    if (-1 == earliestNode) {
                        iteratorUsed = i2;
                        earliestNode = node;
                    } else if (node == earliestNode) {
                        this.m_iterators[i2].nextNode();
                    } else {
                        DTM dtm = getDTM(node);
                        if (dtm.isNodeAfter(node, earliestNode)) {
                            iteratorUsed = i2;
                            earliestNode = node;
                        }
                    }
                }
            }
            if (-1 != earliestNode) {
                this.m_iterators[iteratorUsed].nextNode();
                incrementCurrentPos();
            } else {
                this.m_foundLast = true;
            }
        }
        this.m_lastFetched = earliestNode;
        return earliestNode;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        for (int i2 = 0; i2 < this.m_exprs.length; i2++) {
            this.m_exprs[i2].fixupVariables(vars, globalsSize);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getAxis() {
        return -1;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/UnionPathIterator$iterOwner.class */
    class iterOwner implements ExpressionOwner {
        int m_index;

        iterOwner(int index) {
            this.m_index = index;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return UnionPathIterator.this.m_exprs[this.m_index];
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression expression) {
            Expression expression2;
            if (!(expression instanceof LocPathIterator)) {
                WalkingIterator walkingIterator = new WalkingIterator(UnionPathIterator.this.getPrefixResolver());
                FilterExprWalker filterExprWalker = new FilterExprWalker(walkingIterator);
                walkingIterator.setFirstWalker(filterExprWalker);
                filterExprWalker.setInnerExpression(expression);
                walkingIterator.exprSetParent(UnionPathIterator.this);
                filterExprWalker.exprSetParent(walkingIterator);
                expression.exprSetParent(filterExprWalker);
                expression2 = walkingIterator;
            } else {
                expression.exprSetParent(UnionPathIterator.this);
                expression2 = expression;
            }
            UnionPathIterator.this.m_exprs[this.m_index] = (LocPathIterator) expression2;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        if (visitor.visitUnionPath(owner, this) && null != this.m_exprs) {
            int n2 = this.m_exprs.length;
            for (int i2 = 0; i2 < n2; i2++) {
                this.m_exprs[i2].callVisitors(new iterOwner(i2), visitor);
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        UnionPathIterator upi = (UnionPathIterator) expr;
        if (null != this.m_exprs) {
            int n2 = this.m_exprs.length;
            if (null == upi.m_exprs || upi.m_exprs.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; i2++) {
                if (!this.m_exprs[i2].deepEquals(upi.m_exprs[i2])) {
                    return false;
                }
            }
            return true;
        }
        if (null != upi.m_exprs) {
            return false;
        }
        return true;
    }
}
