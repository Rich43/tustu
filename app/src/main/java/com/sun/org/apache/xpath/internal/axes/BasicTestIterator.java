package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xpath.internal.VariableStack;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.compiler.OpMap;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/BasicTestIterator.class */
public abstract class BasicTestIterator extends LocPathIterator {
    static final long serialVersionUID = 3505378079378096623L;

    protected abstract int getNextNode();

    protected BasicTestIterator() {
    }

    protected BasicTestIterator(PrefixResolver nscontext) {
        super(nscontext);
    }

    protected BasicTestIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
        super(compiler, opPos, analysis, false);
        int firstStepPos = OpMap.getFirstChildPos(opPos);
        int whatToShow = compiler.getWhatToShow(firstStepPos);
        if (0 == (whatToShow & 4163) || whatToShow == -1) {
            initNodeTest(whatToShow);
        } else {
            initNodeTest(whatToShow, compiler.getStepNS(firstStepPos), compiler.getStepLocalName(firstStepPos));
        }
        initPredicateInfo(compiler, firstStepPos);
    }

    protected BasicTestIterator(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers) throws TransformerException {
        super(compiler, opPos, analysis, shouldLoadWalkers);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        VariableStack vars;
        int savedStart;
        int next;
        if (this.m_foundLast) {
            this.m_lastFetched = -1;
            return -1;
        }
        if (-1 == this.m_lastFetched) {
            resetProximityPositions();
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
                if (-1 == next || 1 == acceptNode(next)) {
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
            this.m_pos++;
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

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        ChildTestIterator clone = (ChildTestIterator) super.cloneWithReset();
        clone.resetProximityPositions();
        return clone;
    }
}
