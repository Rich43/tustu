package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.patterns.NodeTest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.xml.transform.TransformerException;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/PredicatedNodeTest.class */
public abstract class PredicatedNodeTest extends NodeTest implements SubContextList {
    static final long serialVersionUID = -6193530757296377351L;
    protected LocPathIterator m_lpi;
    private Expression[] m_predicates;
    protected transient int[] m_proximityPositions;
    static final boolean DEBUG_PREDICATECOUNTING = false;
    protected int m_predCount = -1;
    protected transient boolean m_foundLast = false;
    transient int m_predicateIndex = -1;

    public abstract int getLastPos(XPathContext xPathContext);

    PredicatedNodeTest(LocPathIterator locPathIterator) {
        this.m_lpi = locPathIterator;
    }

    PredicatedNodeTest() {
    }

    private void readObject(ObjectInputStream stream) throws TransformerException, IOException {
        try {
            stream.defaultReadObject();
            this.m_predicateIndex = -1;
            this.m_predCount = -1;
            resetProximityPositions();
        } catch (ClassNotFoundException cnfe) {
            throw new TransformerException(cnfe);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        PredicatedNodeTest clone = (PredicatedNodeTest) super.clone();
        if (null != this.m_proximityPositions && this.m_proximityPositions == clone.m_proximityPositions) {
            clone.m_proximityPositions = new int[this.m_proximityPositions.length];
            System.arraycopy(this.m_proximityPositions, 0, clone.m_proximityPositions, 0, this.m_proximityPositions.length);
        }
        if (clone.m_lpi == this) {
            clone.m_lpi = (LocPathIterator) clone;
        }
        return clone;
    }

    public int getPredicateCount() {
        if (-1 == this.m_predCount) {
            if (null == this.m_predicates) {
                return 0;
            }
            return this.m_predicates.length;
        }
        return this.m_predCount;
    }

    public void setPredicateCount(int count) {
        if (count > 0) {
            Expression[] newPredicates = new Expression[count];
            for (int i2 = 0; i2 < count; i2++) {
                newPredicates[i2] = this.m_predicates[i2];
            }
            this.m_predicates = newPredicates;
            return;
        }
        this.m_predicates = null;
    }

    protected void initPredicateInfo(Compiler compiler, int opPos) throws TransformerException {
        int pos = compiler.getFirstPredicateOpPos(opPos);
        if (pos > 0) {
            this.m_predicates = compiler.getCompiledPredicates(pos);
            if (null != this.m_predicates) {
                for (int i2 = 0; i2 < this.m_predicates.length; i2++) {
                    this.m_predicates[i2].exprSetParent(this);
                }
            }
        }
    }

    public Expression getPredicate(int index) {
        return this.m_predicates[index];
    }

    public int getProximityPosition() {
        return getProximityPosition(this.m_predicateIndex);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.SubContextList
    public int getProximityPosition(XPathContext xctxt) {
        return getProximityPosition();
    }

    protected int getProximityPosition(int predicateIndex) {
        if (predicateIndex >= 0) {
            return this.m_proximityPositions[predicateIndex];
        }
        return 0;
    }

    public void resetProximityPositions() {
        int nPredicates = getPredicateCount();
        if (nPredicates > 0) {
            if (null == this.m_proximityPositions) {
                this.m_proximityPositions = new int[nPredicates];
            }
            for (int i2 = 0; i2 < nPredicates; i2++) {
                try {
                    initProximityPosition(i2);
                } catch (Exception e2) {
                    throw new WrappedRuntimeException(e2);
                }
            }
        }
    }

    public void initProximityPosition(int i2) throws TransformerException {
        this.m_proximityPositions[i2] = 0;
    }

    protected void countProximityPosition(int i2) {
        int[] pp = this.m_proximityPositions;
        if (null != pp && i2 < pp.length) {
            pp[i2] = pp[i2] + 1;
        }
    }

    public boolean isReverseAxes() {
        return false;
    }

    public int getPredicateIndex() {
        return this.m_predicateIndex;
    }

    boolean executePredicates(int context, XPathContext xctxt) throws TransformerException {
        int nPredicates = getPredicateCount();
        if (nPredicates == 0) {
            return true;
        }
        xctxt.getNamespaceContext();
        try {
            this.m_predicateIndex = 0;
            xctxt.pushSubContextList(this);
            xctxt.pushNamespaceContext(this.m_lpi.getPrefixResolver());
            xctxt.pushCurrentNode(context);
            for (int i2 = 0; i2 < nPredicates; i2++) {
                XObject pred = this.m_predicates[i2].execute(xctxt);
                if (2 == pred.getType()) {
                    int proxPos = getProximityPosition(this.m_predicateIndex);
                    int predIndex = (int) pred.num();
                    if (proxPos != predIndex) {
                        return false;
                    }
                    if (this.m_predicates[i2].isStableNumber() && i2 == nPredicates - 1) {
                        this.m_foundLast = true;
                    }
                } else if (!pred.bool()) {
                    xctxt.popCurrentNode();
                    xctxt.popNamespaceContext();
                    xctxt.popSubContextList();
                    this.m_predicateIndex = -1;
                    return false;
                }
                int i3 = this.m_predicateIndex + 1;
                this.m_predicateIndex = i3;
                countProximityPosition(i3);
            }
            xctxt.popCurrentNode();
            xctxt.popNamespaceContext();
            xctxt.popSubContextList();
            this.m_predicateIndex = -1;
            return true;
        } finally {
            xctxt.popCurrentNode();
            xctxt.popNamespaceContext();
            xctxt.popSubContextList();
            this.m_predicateIndex = -1;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
        int nPredicates = getPredicateCount();
        for (int i2 = 0; i2 < nPredicates; i2++) {
            this.m_predicates[i2].fixupVariables(vars, globalsSize);
        }
    }

    protected String nodeToString(int n2) {
        if (-1 != n2) {
            DTM dtm = this.m_lpi.getXPathContext().getDTM(n2);
            return dtm.getNodeName(n2) + VectorFormat.DEFAULT_PREFIX + (n2 + 1) + "}";
        }
        return FXMLLoader.NULL_KEYWORD;
    }

    public short acceptNode(int n2) {
        XPathContext xctxt = this.m_lpi.getXPathContext();
        try {
            try {
                xctxt.pushCurrentNode(n2);
                XObject score = execute(xctxt, n2);
                if (score == NodeTest.SCORE_NONE) {
                    xctxt.popCurrentNode();
                    return (short) 3;
                }
                if (getPredicateCount() > 0) {
                    countProximityPosition(0);
                    if (!executePredicates(n2, xctxt)) {
                        return (short) 3;
                    }
                }
                xctxt.popCurrentNode();
                return (short) 1;
            } catch (TransformerException se) {
                throw new RuntimeException(se.getMessage());
            }
        } finally {
            xctxt.popCurrentNode();
        }
    }

    public LocPathIterator getLocPathIterator() {
        return this.m_lpi;
    }

    public void setLocPathIterator(LocPathIterator li) {
        this.m_lpi = li;
        if (this != li) {
            li.exprSetParent(this);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean canTraverseOutsideSubtree() {
        int n2 = getPredicateCount();
        for (int i2 = 0; i2 < n2; i2++) {
            if (getPredicate(i2).canTraverseOutsideSubtree()) {
                return true;
            }
        }
        return false;
    }

    public void callPredicateVisitors(XPathVisitor visitor) {
        if (null != this.m_predicates) {
            int n2 = this.m_predicates.length;
            for (int i2 = 0; i2 < n2; i2++) {
                ExpressionOwner predOwner = new PredOwner(i2);
                if (visitor.visitPredicate(predOwner, this.m_predicates[i2])) {
                    this.m_predicates[i2].callVisitors(predOwner, visitor);
                }
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        PredicatedNodeTest pnt = (PredicatedNodeTest) expr;
        if (null != this.m_predicates) {
            int n2 = this.m_predicates.length;
            if (null == pnt.m_predicates || pnt.m_predicates.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; i2++) {
                if (!this.m_predicates[i2].deepEquals(pnt.m_predicates[i2])) {
                    return false;
                }
            }
            return true;
        }
        if (null != pnt.m_predicates) {
            return false;
        }
        return true;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/PredicatedNodeTest$PredOwner.class */
    class PredOwner implements ExpressionOwner {
        int m_index;

        PredOwner(int index) {
            this.m_index = index;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return PredicatedNodeTest.this.m_predicates[this.m_index];
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(PredicatedNodeTest.this);
            PredicatedNodeTest.this.m_predicates[this.m_index] = exp;
        }
    }
}
