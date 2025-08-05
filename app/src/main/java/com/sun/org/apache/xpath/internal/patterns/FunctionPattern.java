package com.sun.org.apache.xpath.internal.patterns;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/patterns/FunctionPattern.class */
public class FunctionPattern extends StepPattern {
    static final long serialVersionUID = -5426793413091209944L;
    Expression m_functionExpr;

    public FunctionPattern(Expression expr, int axis, int predaxis) {
        super(0, null, null, axis, predaxis);
        this.m_functionExpr = expr;
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.StepPattern, com.sun.org.apache.xpath.internal.patterns.NodeTest
    public final void calcScore() {
        this.m_score = SCORE_OTHER;
        if (null == this.m_targetString) {
            calcTargetString();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.StepPattern, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
        this.m_functionExpr.fixupVariables(vars, globalsSize);
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.StepPattern, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt, int context) throws TransformerException {
        DTMIterator nl = this.m_functionExpr.asIterator(xctxt, context);
        XNumber score = SCORE_NONE;
        if (null != nl) {
            do {
                int n2 = nl.nextNode();
                if (-1 == n2) {
                    break;
                }
                score = n2 == context ? SCORE_OTHER : SCORE_NONE;
            } while (score != SCORE_OTHER);
        }
        nl.detach();
        return score;
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.StepPattern, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt, int context, DTM dtm, int expType) throws TransformerException {
        DTMIterator nl = this.m_functionExpr.asIterator(xctxt, context);
        XNumber score = SCORE_NONE;
        if (null != nl) {
            do {
                int n2 = nl.nextNode();
                if (-1 == n2) {
                    break;
                }
                score = n2 == context ? SCORE_OTHER : SCORE_NONE;
            } while (score != SCORE_OTHER);
            nl.detach();
        }
        return score;
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.StepPattern, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        int context = xctxt.getCurrentNode();
        DTMIterator nl = this.m_functionExpr.asIterator(xctxt, context);
        XNumber score = SCORE_NONE;
        if (null != nl) {
            do {
                int n2 = nl.nextNode();
                if (-1 == n2) {
                    break;
                }
                score = n2 == context ? SCORE_OTHER : SCORE_NONE;
            } while (score != SCORE_OTHER);
            nl.detach();
        }
        return score;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/patterns/FunctionPattern$FunctionOwner.class */
    class FunctionOwner implements ExpressionOwner {
        FunctionOwner() {
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return FunctionPattern.this.m_functionExpr;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(FunctionPattern.this);
            FunctionPattern.this.m_functionExpr = exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.StepPattern
    protected void callSubtreeVisitors(XPathVisitor visitor) {
        this.m_functionExpr.callVisitors(new FunctionOwner(), visitor);
        super.callSubtreeVisitors(visitor);
    }
}
