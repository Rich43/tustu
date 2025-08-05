package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.VariableStack;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.compiler.OpMap;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/WalkingIterator.class */
public class WalkingIterator extends LocPathIterator implements ExpressionOwner {
    static final long serialVersionUID = 9110225941815665906L;
    protected AxesWalker m_lastUsedWalker;
    protected AxesWalker m_firstWalker;

    WalkingIterator(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers) throws TransformerException {
        super(compiler, opPos, analysis, shouldLoadWalkers);
        int firstStepPos = OpMap.getFirstChildPos(opPos);
        if (shouldLoadWalkers) {
            this.m_firstWalker = WalkerFactory.loadWalkers(this, compiler, firstStepPos, 0);
            this.m_lastUsedWalker = this.m_firstWalker;
        }
    }

    public WalkingIterator(PrefixResolver nscontext) {
        super(nscontext);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xpath.internal.axes.PathComponent
    public int getAnalysisBits() {
        int bits = 0;
        if (null != this.m_firstWalker) {
            AxesWalker nextWalker = this.m_firstWalker;
            while (true) {
                AxesWalker walker = nextWalker;
                if (null == walker) {
                    break;
                }
                int bit = walker.getAnalysisBits();
                bits |= bit;
                nextWalker = walker.getNextWalker();
            }
        }
        return bits;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public Object clone() throws CloneNotSupportedException {
        WalkingIterator clone = (WalkingIterator) super.clone();
        if (null != this.m_firstWalker) {
            clone.m_firstWalker = this.m_firstWalker.cloneDeep(clone, null);
        }
        return clone;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void reset() {
        super.reset();
        if (null != this.m_firstWalker) {
            this.m_lastUsedWalker = this.m_firstWalker;
            this.m_firstWalker.setRoot(this.m_context);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int context, Object environment) {
        super.setRoot(context, environment);
        if (null != this.m_firstWalker) {
            this.m_firstWalker.setRoot(context);
            this.m_lastUsedWalker = this.m_firstWalker;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        if (-1 == this.m_stackFrame) {
            return returnNextNode(this.m_firstWalker.nextNode());
        }
        VariableStack vars = this.m_execContext.getVarStack();
        int savedStart = vars.getStackFrame();
        vars.setStackFrame(this.m_stackFrame);
        int n2 = returnNextNode(this.m_firstWalker.nextNode());
        vars.setStackFrame(savedStart);
        return n2;
    }

    public final AxesWalker getFirstWalker() {
        return this.m_firstWalker;
    }

    public final void setFirstWalker(AxesWalker walker) {
        this.m_firstWalker = walker;
    }

    public final void setLastUsedWalker(AxesWalker walker) {
        this.m_lastUsedWalker = walker;
    }

    public final AxesWalker getLastUsedWalker() {
        return this.m_lastUsedWalker;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        if (this.m_allowDetach) {
            AxesWalker nextWalker = this.m_firstWalker;
            while (true) {
                AxesWalker walker = nextWalker;
                if (null != walker) {
                    walker.detach();
                    nextWalker = walker.getNextWalker();
                } else {
                    this.m_lastUsedWalker = null;
                    super.detach();
                    return;
                }
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        this.m_predicateIndex = -1;
        AxesWalker nextWalker = this.m_firstWalker;
        while (true) {
            AxesWalker walker = nextWalker;
            if (null != walker) {
                walker.fixupVariables(vars, globalsSize);
                nextWalker = walker.getNextWalker();
            } else {
                return;
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        if (visitor.visitLocationPath(owner, this) && null != this.m_firstWalker) {
            this.m_firstWalker.callVisitors(this, visitor);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public Expression getExpression() {
        return this.m_firstWalker;
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public void setExpression(Expression exp) {
        exp.exprSetParent(this);
        this.m_firstWalker = (AxesWalker) exp;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x003a, code lost:
    
        if (null != r5) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x003f, code lost:
    
        if (null == r6) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0042, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0044, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:?, code lost:
    
        return false;
     */
    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean deepEquals(com.sun.org.apache.xpath.internal.Expression r4) {
        /*
            r3 = this;
            r0 = r3
            r1 = r4
            boolean r0 = super.deepEquals(r1)
            if (r0 != 0) goto La
            r0 = 0
            return r0
        La:
            r0 = r3
            com.sun.org.apache.xpath.internal.axes.AxesWalker r0 = r0.m_firstWalker
            r5 = r0
            r0 = r4
            com.sun.org.apache.xpath.internal.axes.WalkingIterator r0 = (com.sun.org.apache.xpath.internal.axes.WalkingIterator) r0
            com.sun.org.apache.xpath.internal.axes.AxesWalker r0 = r0.m_firstWalker
            r6 = r0
        L17:
            r0 = 0
            r1 = r5
            if (r0 == r1) goto L38
            r0 = 0
            r1 = r6
            if (r0 == r1) goto L38
            r0 = r5
            r1 = r6
            boolean r0 = r0.deepEquals(r1)
            if (r0 != 0) goto L2b
            r0 = 0
            return r0
        L2b:
            r0 = r5
            com.sun.org.apache.xpath.internal.axes.AxesWalker r0 = r0.getNextWalker()
            r5 = r0
            r0 = r6
            com.sun.org.apache.xpath.internal.axes.AxesWalker r0 = r0.getNextWalker()
            r6 = r0
            goto L17
        L38:
            r0 = 0
            r1 = r5
            if (r0 != r1) goto L42
            r0 = 0
            r1 = r6
            if (r0 == r1) goto L44
        L42:
            r0 = 0
            return r0
        L44:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xpath.internal.axes.WalkingIterator.deepEquals(com.sun.org.apache.xpath.internal.Expression):boolean");
    }
}
