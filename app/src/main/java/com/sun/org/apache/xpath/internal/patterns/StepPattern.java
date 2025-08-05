package com.sun.org.apache.xpath.internal.patterns;

import com.sun.org.apache.xml.internal.dtm.Axis;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.axes.SubContextList;
import com.sun.org.apache.xpath.internal.compiler.PsuedoNames;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/patterns/StepPattern.class */
public class StepPattern extends NodeTest implements SubContextList, ExpressionOwner {
    static final long serialVersionUID = 9071668960168152644L;
    protected int m_axis;
    String m_targetString;
    StepPattern m_relativePathPattern;
    Expression[] m_predicates;
    private static final boolean DEBUG_MATCHES = false;

    public StepPattern(int whatToShow, String namespace, String name, int axis, int axisForPredicate) {
        super(whatToShow, namespace, name);
        this.m_axis = axis;
    }

    public StepPattern(int whatToShow, int axis, int axisForPredicate) {
        super(whatToShow);
        this.m_axis = axis;
    }

    public void calcTargetString() {
        int whatToShow = getWhatToShow();
        switch (whatToShow) {
            case -1:
                this.m_targetString = "*";
                break;
            case 1:
                if ("*" == this.m_name) {
                    this.m_targetString = "*";
                    break;
                } else {
                    this.m_targetString = this.m_name;
                    break;
                }
            case 4:
            case 8:
            case 12:
                this.m_targetString = PsuedoNames.PSEUDONAME_TEXT;
                break;
            case 128:
                this.m_targetString = PsuedoNames.PSEUDONAME_COMMENT;
                break;
            case 256:
            case 1280:
                this.m_targetString = "/";
                break;
            default:
                this.m_targetString = "*";
                break;
        }
    }

    public String getTargetString() {
        return this.m_targetString;
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
        if (null != this.m_predicates) {
            for (int i2 = 0; i2 < this.m_predicates.length; i2++) {
                this.m_predicates[i2].fixupVariables(vars, globalsSize);
            }
        }
        if (null != this.m_relativePathPattern) {
            this.m_relativePathPattern.fixupVariables(vars, globalsSize);
        }
    }

    public void setRelativePathPattern(StepPattern expr) {
        this.m_relativePathPattern = expr;
        expr.exprSetParent(this);
        calcScore();
    }

    public StepPattern getRelativePathPattern() {
        return this.m_relativePathPattern;
    }

    public Expression[] getPredicates() {
        return this.m_predicates;
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

    public Expression getPredicate(int i2) {
        return this.m_predicates[i2];
    }

    public final int getPredicateCount() {
        if (null == this.m_predicates) {
            return 0;
        }
        return this.m_predicates.length;
    }

    public void setPredicates(Expression[] predicates) {
        this.m_predicates = predicates;
        if (null != predicates) {
            for (Expression expression : predicates) {
                expression.exprSetParent(this);
            }
        }
        calcScore();
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest
    public void calcScore() {
        if (getPredicateCount() > 0 || null != this.m_relativePathPattern) {
            this.m_score = SCORE_OTHER;
        } else {
            super.calcScore();
        }
        if (null == this.m_targetString) {
            calcTargetString();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt, int currentNode) throws TransformerException {
        DTM dtm = xctxt.getDTM(currentNode);
        if (dtm != null) {
            int expType = dtm.getExpandedTypeID(currentNode);
            return execute(xctxt, currentNode, dtm, expType);
        }
        return NodeTest.SCORE_NONE;
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        return execute(xctxt, xctxt.getCurrentNode());
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt, int currentNode, DTM dtm, int expType) throws TransformerException {
        if (this.m_whatToShow == 65536) {
            if (null != this.m_relativePathPattern) {
                return this.m_relativePathPattern.execute(xctxt);
            }
            return NodeTest.SCORE_NONE;
        }
        XObject score = super.execute(xctxt, currentNode, dtm, expType);
        if (score == NodeTest.SCORE_NONE) {
            return NodeTest.SCORE_NONE;
        }
        if (getPredicateCount() != 0 && !executePredicates(xctxt, dtm, currentNode)) {
            return NodeTest.SCORE_NONE;
        }
        if (null != this.m_relativePathPattern) {
            return this.m_relativePathPattern.executeRelativePathPattern(xctxt, dtm, currentNode);
        }
        return score;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0062, code lost:
    
        throw new java.lang.Error("Why: Should never have been called");
     */
    /* JADX WARN: Finally extract failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final boolean checkProximityPosition(com.sun.org.apache.xpath.internal.XPathContext r6, int r7, com.sun.org.apache.xml.internal.dtm.DTM r8, int r9, int r10) {
        /*
            Method dump skipped, instructions count: 262
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xpath.internal.patterns.StepPattern.checkProximityPosition(com.sun.org.apache.xpath.internal.XPathContext, int, com.sun.org.apache.xml.internal.dtm.DTM, int, int):boolean");
    }

    /* JADX WARN: Finally extract failed */
    private final int getProximityPosition(XPathContext xctxt, int predPos, boolean findLast) {
        int pos = 0;
        int context = xctxt.getCurrentNode();
        DTM dtm = xctxt.getDTM(context);
        int parent = dtm.getParent(context);
        try {
            DTMAxisTraverser traverser = dtm.getAxisTraverser(3);
            for (int child = traverser.first(parent); -1 != child; child = traverser.next(parent, child)) {
                try {
                    xctxt.pushCurrentNode(child);
                    if (NodeTest.SCORE_NONE != super.execute(xctxt, child)) {
                        boolean pass = true;
                        try {
                            xctxt.pushSubContextList(this);
                            int i2 = 0;
                            while (true) {
                                if (i2 >= predPos) {
                                    break;
                                }
                                xctxt.pushPredicatePos(i2);
                                try {
                                    XObject pred = this.m_predicates[i2].execute(xctxt);
                                    try {
                                        if (2 == pred.getType()) {
                                            if (pos + 1 != ((int) pred.numWithSideEffects())) {
                                                pass = false;
                                                pred.detach();
                                                xctxt.popPredicatePos();
                                                break;
                                            }
                                            pred.detach();
                                            xctxt.popPredicatePos();
                                            i2++;
                                        } else {
                                            if (!pred.boolWithSideEffects()) {
                                                pass = false;
                                                pred.detach();
                                                xctxt.popPredicatePos();
                                                break;
                                            }
                                            pred.detach();
                                            xctxt.popPredicatePos();
                                            i2++;
                                        }
                                    } finally {
                                    }
                                } finally {
                                }
                            }
                            xctxt.popSubContextList();
                            if (pass) {
                                pos++;
                            }
                            if (!findLast && child == context) {
                                return pos;
                            }
                        } catch (Throwable th) {
                            xctxt.popSubContextList();
                            throw th;
                        }
                    }
                    xctxt.popCurrentNode();
                } finally {
                    xctxt.popCurrentNode();
                }
            }
            return pos;
        } catch (TransformerException se) {
            throw new RuntimeException(se.getMessage());
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.SubContextList
    public int getProximityPosition(XPathContext xctxt) {
        return getProximityPosition(xctxt, xctxt.getPredicatePos(), false);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.SubContextList
    public int getLastPos(XPathContext xctxt) {
        return getProximityPosition(xctxt, xctxt.getPredicatePos(), true);
    }

    protected final XObject executeRelativePathPattern(XPathContext xctxt, DTM dtm, int currentNode) throws TransformerException {
        XObject score = NodeTest.SCORE_NONE;
        DTMAxisTraverser traverser = dtm.getAxisTraverser(this.m_axis);
        int iFirst = traverser.first(currentNode);
        while (true) {
            int relative = iFirst;
            if (-1 == relative) {
                break;
            }
            try {
                xctxt.pushCurrentNode(relative);
                score = execute(xctxt);
                if (score != NodeTest.SCORE_NONE) {
                    break;
                }
                xctxt.popCurrentNode();
                iFirst = traverser.next(currentNode, relative);
            } finally {
                xctxt.popCurrentNode();
            }
        }
        return score;
    }

    /* JADX WARN: Finally extract failed */
    protected final boolean executePredicates(XPathContext xctxt, DTM dtm, int currentNode) throws TransformerException {
        boolean result = true;
        boolean positionAlreadySeen = false;
        int n2 = getPredicateCount();
        try {
            xctxt.pushSubContextList(this);
            int i2 = 0;
            while (true) {
                if (i2 >= n2) {
                    break;
                }
                xctxt.pushPredicatePos(i2);
                try {
                    XObject pred = this.m_predicates[i2].execute(xctxt);
                    try {
                        if (2 == pred.getType()) {
                            int pos = (int) pred.num();
                            if (positionAlreadySeen) {
                                result = pos == 1;
                                pred.detach();
                                xctxt.popPredicatePos();
                            } else {
                                positionAlreadySeen = true;
                                if (!checkProximityPosition(xctxt, i2, dtm, currentNode, pos)) {
                                    result = false;
                                    pred.detach();
                                    xctxt.popPredicatePos();
                                    break;
                                }
                                pred.detach();
                                xctxt.popPredicatePos();
                                i2++;
                            }
                        } else {
                            if (!pred.boolWithSideEffects()) {
                                result = false;
                                pred.detach();
                                xctxt.popPredicatePos();
                                break;
                            }
                            pred.detach();
                            xctxt.popPredicatePos();
                            i2++;
                        }
                    } catch (Throwable th) {
                        pred.detach();
                        throw th;
                    }
                } catch (Throwable th2) {
                    xctxt.popPredicatePos();
                    throw th2;
                }
            }
            return result;
        } finally {
            xctxt.popSubContextList();
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        StepPattern stepPattern = this;
        while (true) {
            StepPattern pat = stepPattern;
            if (pat != null) {
                if (pat != this) {
                    buf.append("/");
                }
                buf.append(Axis.getNames(pat.m_axis));
                buf.append("::");
                if (20480 == pat.m_whatToShow) {
                    buf.append("doc()");
                } else if (65536 == pat.m_whatToShow) {
                    buf.append("function()");
                } else if (-1 == pat.m_whatToShow) {
                    buf.append("node()");
                } else if (4 == pat.m_whatToShow) {
                    buf.append("text()");
                } else if (64 == pat.m_whatToShow) {
                    buf.append("processing-instruction(");
                    if (null != pat.m_name) {
                        buf.append(pat.m_name);
                    }
                    buf.append(")");
                } else if (128 == pat.m_whatToShow) {
                    buf.append("comment()");
                } else if (null != pat.m_name) {
                    if (2 == pat.m_whatToShow) {
                        buf.append("@");
                    }
                    if (null != pat.m_namespace) {
                        buf.append(VectorFormat.DEFAULT_PREFIX);
                        buf.append(pat.m_namespace);
                        buf.append("}");
                    }
                    buf.append(pat.m_name);
                } else if (2 == pat.m_whatToShow) {
                    buf.append("@");
                } else if (1280 == pat.m_whatToShow) {
                    buf.append("doc-root()");
                } else {
                    buf.append('?').append(Integer.toHexString(pat.m_whatToShow));
                }
                if (null != pat.m_predicates) {
                    for (int i2 = 0; i2 < pat.m_predicates.length; i2++) {
                        buf.append("[");
                        buf.append((Object) pat.m_predicates[i2]);
                        buf.append("]");
                    }
                }
                stepPattern = pat.m_relativePathPattern;
            } else {
                return buf.toString();
            }
        }
    }

    public double getMatchScore(XPathContext xctxt, int context) throws TransformerException {
        xctxt.pushCurrentNode(context);
        xctxt.pushCurrentExpressionNode(context);
        try {
            XObject score = execute(xctxt);
            double dNum = score.num();
            xctxt.popCurrentNode();
            xctxt.popCurrentExpressionNode();
            return dNum;
        } catch (Throwable th) {
            xctxt.popCurrentNode();
            xctxt.popCurrentExpressionNode();
            throw th;
        }
    }

    public void setAxis(int axis) {
        this.m_axis = axis;
    }

    public int getAxis() {
        return this.m_axis;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/patterns/StepPattern$PredOwner.class */
    class PredOwner implements ExpressionOwner {
        int m_index;

        PredOwner(int index) {
            this.m_index = index;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return StepPattern.this.m_predicates[this.m_index];
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(StepPattern.this);
            StepPattern.this.m_predicates[this.m_index] = exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        if (visitor.visitMatchPattern(owner, this)) {
            callSubtreeVisitors(visitor);
        }
    }

    protected void callSubtreeVisitors(XPathVisitor visitor) {
        if (null != this.m_predicates) {
            int n2 = this.m_predicates.length;
            for (int i2 = 0; i2 < n2; i2++) {
                ExpressionOwner predOwner = new PredOwner(i2);
                if (visitor.visitPredicate(predOwner, this.m_predicates[i2])) {
                    this.m_predicates[i2].callVisitors(predOwner, visitor);
                }
            }
        }
        if (null != this.m_relativePathPattern) {
            this.m_relativePathPattern.callVisitors(this, visitor);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public Expression getExpression() {
        return this.m_relativePathPattern;
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public void setExpression(Expression exp) {
        exp.exprSetParent(this);
        this.m_relativePathPattern = (StepPattern) exp;
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        StepPattern sp = (StepPattern) expr;
        if (null != this.m_predicates) {
            int n2 = this.m_predicates.length;
            if (null == sp.m_predicates || sp.m_predicates.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; i2++) {
                if (!this.m_predicates[i2].deepEquals(sp.m_predicates[i2])) {
                    return false;
                }
            }
        } else if (null != sp.m_predicates) {
            return false;
        }
        if (null != this.m_relativePathPattern) {
            if (!this.m_relativePathPattern.deepEquals(sp.m_relativePathPattern)) {
                return false;
            }
            return true;
        }
        if (sp.m_relativePathPattern != null) {
            return false;
        }
        return true;
    }
}
