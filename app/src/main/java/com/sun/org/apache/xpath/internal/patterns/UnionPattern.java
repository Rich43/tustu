package com.sun.org.apache.xpath.internal.patterns;

import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/patterns/UnionPattern.class */
public class UnionPattern extends Expression {
    static final long serialVersionUID = -6670449967116905820L;
    private StepPattern[] m_patterns;

    @Override // com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        for (int i2 = 0; i2 < this.m_patterns.length; i2++) {
            this.m_patterns[i2].fixupVariables(vars, globalsSize);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean canTraverseOutsideSubtree() {
        if (null != this.m_patterns) {
            int n2 = this.m_patterns.length;
            for (int i2 = 0; i2 < n2; i2++) {
                if (this.m_patterns[i2].canTraverseOutsideSubtree()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void setPatterns(StepPattern[] patterns) {
        this.m_patterns = patterns;
        if (null != patterns) {
            for (StepPattern stepPattern : patterns) {
                stepPattern.exprSetParent(this);
            }
        }
    }

    public StepPattern[] getPatterns() {
        return this.m_patterns;
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        XObject bestScore = null;
        int n2 = this.m_patterns.length;
        for (int i2 = 0; i2 < n2; i2++) {
            XObject score = this.m_patterns[i2].execute(xctxt);
            if (score != NodeTest.SCORE_NONE) {
                if (null == bestScore) {
                    bestScore = score;
                } else if (score.num() > bestScore.num()) {
                    bestScore = score;
                }
            }
        }
        if (null == bestScore) {
            bestScore = NodeTest.SCORE_NONE;
        }
        return bestScore;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/patterns/UnionPattern$UnionPathPartOwner.class */
    class UnionPathPartOwner implements ExpressionOwner {
        int m_index;

        UnionPathPartOwner(int index) {
            this.m_index = index;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return UnionPattern.this.m_patterns[this.m_index];
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(UnionPattern.this);
            UnionPattern.this.m_patterns[this.m_index] = (StepPattern) exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        visitor.visitUnionPattern(owner, this);
        if (null != this.m_patterns) {
            int n2 = this.m_patterns.length;
            for (int i2 = 0; i2 < n2; i2++) {
                this.m_patterns[i2].callVisitors(new UnionPathPartOwner(i2), visitor);
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!isSameClass(expr)) {
            return false;
        }
        UnionPattern up = (UnionPattern) expr;
        if (null != this.m_patterns) {
            int n2 = this.m_patterns.length;
            if (null == up.m_patterns || up.m_patterns.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; i2++) {
                if (!this.m_patterns[i2].deepEquals(up.m_patterns[i2])) {
                    return false;
                }
            }
            return true;
        }
        if (up.m_patterns != null) {
            return false;
        }
        return true;
    }
}
