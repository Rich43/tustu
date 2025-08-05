package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/WalkingIteratorSorted.class */
public class WalkingIteratorSorted extends WalkingIterator {
    static final long serialVersionUID = -4512512007542368213L;
    protected boolean m_inNaturalOrderStatic;

    public WalkingIteratorSorted(PrefixResolver nscontext) {
        super(nscontext);
        this.m_inNaturalOrderStatic = false;
    }

    WalkingIteratorSorted(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers) throws TransformerException {
        super(compiler, opPos, analysis, shouldLoadWalkers);
        this.m_inNaturalOrderStatic = false;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean isDocOrdered() {
        return this.m_inNaturalOrderStatic;
    }

    boolean canBeWalkedInNaturalDocOrderStatic() {
        if (null != this.m_firstWalker) {
            AxesWalker walker = this.m_firstWalker;
            int i2 = 0;
            while (null != walker) {
                int axis = walker.getAxis();
                if (walker.isDocOrdered()) {
                    boolean isSimpleDownAxis = axis == 3 || axis == 13 || axis == 19;
                    if (isSimpleDownAxis || axis == -1) {
                        walker = walker.getNextWalker();
                        i2++;
                    } else {
                        boolean isLastWalker = null == walker.getNextWalker();
                        if (isLastWalker) {
                            if ((walker.isDocOrdered() && (axis == 4 || axis == 5 || axis == 17 || axis == 18)) || axis == 2) {
                                return true;
                            }
                            return false;
                        }
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.WalkingIterator, com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
        int analysis = getAnalysisBits();
        if (WalkerFactory.isNaturalDocOrder(analysis)) {
            this.m_inNaturalOrderStatic = true;
        } else {
            this.m_inNaturalOrderStatic = false;
        }
    }
}
