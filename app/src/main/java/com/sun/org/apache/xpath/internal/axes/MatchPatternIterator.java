package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xpath.internal.VariableStack;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.compiler.OpMap;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.patterns.NodeTest;
import com.sun.org.apache.xpath.internal.patterns.StepPattern;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/MatchPatternIterator.class */
public class MatchPatternIterator extends LocPathIterator {
    static final long serialVersionUID = -5201153767396296474L;
    protected StepPattern m_pattern;
    protected int m_superAxis;
    protected DTMAxisTraverser m_traverser;
    private static final boolean DEBUG = false;

    MatchPatternIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
        super(compiler, opPos, analysis, false);
        this.m_superAxis = -1;
        int firstStepPos = OpMap.getFirstChildPos(opPos);
        this.m_pattern = WalkerFactory.loadSteps(this, compiler, firstStepPos, 0);
        boolean fromRoot = false;
        boolean walkBack = false;
        boolean walkDescendants = false;
        boolean walkAttributes = false;
        fromRoot = 0 != (analysis & 671088640) ? true : fromRoot;
        walkBack = 0 != (analysis & 98066432) ? true : walkBack;
        walkDescendants = 0 != (analysis & 458752) ? true : walkDescendants;
        walkAttributes = 0 != (analysis & 2129920) ? true : walkAttributes;
        if (fromRoot || walkBack) {
            if (walkAttributes) {
                this.m_superAxis = 16;
                return;
            } else {
                this.m_superAxis = 17;
                return;
            }
        }
        if (walkDescendants) {
            if (walkAttributes) {
                this.m_superAxis = 14;
                return;
            } else {
                this.m_superAxis = 5;
                return;
            }
        }
        this.m_superAxis = 16;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int context, Object environment) {
        super.setRoot(context, environment);
        this.m_traverser = this.m_cdtm.getAxisTraverser(this.m_superAxis);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        if (this.m_allowDetach) {
            this.m_traverser = null;
            super.detach();
        }
    }

    protected int getNextNode() {
        int next;
        if (-1 == this.m_lastFetched) {
            next = this.m_traverser.first(this.m_context);
        } else {
            next = this.m_traverser.next(this.m_context, this.m_lastFetched);
        }
        this.m_lastFetched = next;
        return this.m_lastFetched;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        VariableStack vars;
        int savedStart;
        int next;
        if (this.m_foundLast) {
            return -1;
        }
        if (-1 != this.m_stackFrame) {
            vars = this.m_execContext.getVarStack();
            savedStart = vars.getStackFrame();
            vars.setStackFrame(this.m_stackFrame);
        } else {
            vars = null;
            savedStart = 0;
        }
        do {
            try {
                next = getNextNode();
                if (-1 == next || 1 == acceptNode(next, this.m_execContext)) {
                    break;
                }
            } catch (Throwable th) {
                if (-1 != this.m_stackFrame) {
                    vars.setStackFrame(savedStart);
                }
                throw th;
            }
        } while (next != -1);
        if (-1 != next) {
            incrementCurrentPos();
            if (-1 != this.m_stackFrame) {
                vars.setStackFrame(savedStart);
            }
            return next;
        }
        this.m_foundLast = true;
        if (-1 != this.m_stackFrame) {
            vars.setStackFrame(savedStart);
        }
        return -1;
    }

    public short acceptNode(int n2, XPathContext xctxt) {
        try {
            try {
                xctxt.pushCurrentNode(n2);
                xctxt.pushIteratorRoot(this.m_context);
                XObject score = this.m_pattern.execute(xctxt);
                return score == NodeTest.SCORE_NONE ? (short) 3 : (short) 1;
            } catch (TransformerException se) {
                throw new RuntimeException(se.getMessage());
            }
        } finally {
            xctxt.popCurrentNode();
            xctxt.popIteratorRoot();
        }
    }
}
