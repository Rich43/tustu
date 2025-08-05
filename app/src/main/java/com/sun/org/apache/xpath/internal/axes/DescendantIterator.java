package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.VariableStack;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.compiler.OpMap;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/DescendantIterator.class */
public class DescendantIterator extends LocPathIterator {
    static final long serialVersionUID = -1190338607743976938L;
    protected transient DTMAxisTraverser m_traverser;
    protected int m_axis;
    protected int m_extendedTypeID;

    DescendantIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
        super(compiler, opPos, analysis, false);
        int firstStepPos = OpMap.getFirstChildPos(opPos);
        int stepType = compiler.getOp(firstStepPos);
        boolean orSelf = 42 == stepType;
        boolean fromRoot = false;
        if (48 == stepType) {
            orSelf = true;
        } else if (50 == stepType) {
            fromRoot = true;
            int nextStepPos = compiler.getNextStepPos(firstStepPos);
            if (compiler.getOp(nextStepPos) == 42) {
                orSelf = true;
            }
        }
        int nextStepPos2 = firstStepPos;
        while (true) {
            nextStepPos2 = compiler.getNextStepPos(nextStepPos2);
            if (nextStepPos2 <= 0) {
                break;
            }
            int stepOp = compiler.getOp(nextStepPos2);
            if (-1 == stepOp) {
                break;
            } else {
                firstStepPos = nextStepPos2;
            }
        }
        orSelf = (analysis & 65536) != 0 ? false : orSelf;
        if (fromRoot) {
            if (orSelf) {
                this.m_axis = 18;
            } else {
                this.m_axis = 17;
            }
        } else if (orSelf) {
            this.m_axis = 5;
        } else {
            this.m_axis = 4;
        }
        int whatToShow = compiler.getWhatToShow(firstStepPos);
        if (0 == (whatToShow & 67) || whatToShow == -1) {
            initNodeTest(whatToShow);
        } else {
            initNodeTest(whatToShow, compiler.getStepNS(firstStepPos), compiler.getStepLocalName(firstStepPos));
        }
        initPredicateInfo(compiler, firstStepPos);
    }

    public DescendantIterator() {
        super(null);
        this.m_axis = 18;
        initNodeTest(-1);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        DescendantIterator clone = (DescendantIterator) super.cloneWithReset();
        clone.m_traverser = this.m_traverser;
        clone.resetProximityPositions();
        return clone;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        VariableStack vars;
        int savedStart;
        int next;
        int next2;
        int next3;
        if (this.m_foundLast) {
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
                if (0 == this.m_extendedTypeID) {
                    if (-1 == this.m_lastFetched) {
                        next3 = this.m_traverser.first(this.m_context);
                    } else {
                        next3 = this.m_traverser.next(this.m_context, this.m_lastFetched);
                    }
                    int i2 = next3;
                    this.m_lastFetched = i2;
                    next2 = i2;
                } else {
                    if (-1 == this.m_lastFetched) {
                        next = this.m_traverser.first(this.m_context, this.m_extendedTypeID);
                    } else {
                        next = this.m_traverser.next(this.m_context, this.m_lastFetched, this.m_extendedTypeID);
                    }
                    int i3 = next;
                    this.m_lastFetched = i3;
                    next2 = i3;
                }
                if (-1 == next2 || 1 == acceptNode(next2)) {
                    break;
                }
            } catch (Throwable th) {
                if (-1 != this.m_stackFrame) {
                    vars.setStackFrame(savedStart);
                }
                throw th;
            }
        } while (next2 != -1);
        if (-1 != next2) {
            this.m_pos++;
            int i4 = next2;
            if (-1 != this.m_stackFrame) {
                vars.setStackFrame(savedStart);
            }
            return i4;
        }
        this.m_foundLast = true;
        if (-1 != this.m_stackFrame) {
            vars.setStackFrame(savedStart);
        }
        return -1;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int context, Object environment) {
        super.setRoot(context, environment);
        this.m_traverser = this.m_cdtm.getAxisTraverser(this.m_axis);
        String localName = getLocalName();
        String namespace = getNamespace();
        int what = this.m_whatToShow;
        if (-1 == what || "*".equals(localName) || "*".equals(namespace)) {
            this.m_extendedTypeID = 0;
        } else {
            int type = getNodeTypeTest(what);
            this.m_extendedTypeID = this.m_cdtm.getExpandedTypeID(namespace, localName, type);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xpath.internal.Expression
    public int asNode(XPathContext xctxt) throws TransformerException {
        if (getPredicateCount() > 0) {
            return super.asNode(xctxt);
        }
        int current = xctxt.getCurrentNode();
        DTM dtm = xctxt.getDTM(current);
        DTMAxisTraverser traverser = dtm.getAxisTraverser(this.m_axis);
        String localName = getLocalName();
        String namespace = getNamespace();
        int what = this.m_whatToShow;
        if (-1 == what || localName == "*" || namespace == "*") {
            return traverser.first(current);
        }
        int type = getNodeTypeTest(what);
        int extendedType = dtm.getExpandedTypeID(namespace, localName, type);
        return traverser.first(current, extendedType);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        if (this.m_allowDetach) {
            this.m_traverser = null;
            this.m_extendedTypeID = 0;
            super.detach();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getAxis() {
        return this.m_axis;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr) || this.m_axis != ((DescendantIterator) expr).m_axis) {
            return false;
        }
        return true;
    }
}
