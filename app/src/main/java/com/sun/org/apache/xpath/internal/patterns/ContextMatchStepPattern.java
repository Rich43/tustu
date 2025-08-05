package com.sun.org.apache.xpath.internal.patterns;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.axes.WalkerFactory;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/patterns/ContextMatchStepPattern.class */
public class ContextMatchStepPattern extends StepPattern {
    static final long serialVersionUID = -1888092779313211942L;

    public ContextMatchStepPattern(int axis, int paxis) {
        super(-1, axis, paxis);
    }

    @Override // com.sun.org.apache.xpath.internal.patterns.StepPattern, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        return xctxt.getIteratorRoot() == xctxt.getCurrentNode() ? getStaticScore() : SCORE_NONE;
    }

    public XObject executeRelativePathPattern(XPathContext xctxt, StepPattern prevStep) throws TransformerException {
        XObject score = NodeTest.SCORE_NONE;
        int context = xctxt.getCurrentNode();
        DTM dtm = xctxt.getDTM(context);
        if (null != dtm) {
            xctxt.getCurrentNode();
            int axis = this.m_axis;
            boolean needToTraverseAttrs = WalkerFactory.isDownwardAxisOfMany(axis);
            boolean iterRootIsAttr = dtm.getNodeType(xctxt.getIteratorRoot()) == 2;
            if (11 == axis && iterRootIsAttr) {
                axis = 15;
            }
            DTMAxisTraverser traverser = dtm.getAxisTraverser(axis);
            int iFirst = traverser.first(context);
            while (true) {
                int relative = iFirst;
                if (-1 == relative) {
                    break;
                }
                try {
                    xctxt.pushCurrentNode(relative);
                    score = execute(xctxt);
                    if (score != NodeTest.SCORE_NONE) {
                        if (executePredicates(xctxt, dtm, context)) {
                            return score;
                        }
                        score = NodeTest.SCORE_NONE;
                    }
                    if (needToTraverseAttrs && iterRootIsAttr && 1 == dtm.getNodeType(relative)) {
                        int xaxis = 2;
                        for (int i2 = 0; i2 < 2; i2++) {
                            DTMAxisTraverser atraverser = dtm.getAxisTraverser(xaxis);
                            for (int arelative = atraverser.first(relative); -1 != arelative; arelative = atraverser.next(relative, arelative)) {
                                try {
                                    xctxt.pushCurrentNode(arelative);
                                    score = execute(xctxt);
                                    if (score != NodeTest.SCORE_NONE && score != NodeTest.SCORE_NONE) {
                                        xctxt.popCurrentNode();
                                        xctxt.popCurrentNode();
                                        return score;
                                    }
                                    xctxt.popCurrentNode();
                                } finally {
                                    xctxt.popCurrentNode();
                                }
                            }
                            xaxis = 9;
                        }
                    }
                    xctxt.popCurrentNode();
                    iFirst = traverser.next(context, relative);
                } catch (Throwable th) {
                    xctxt.popCurrentNode();
                    throw th;
                }
            }
        }
        return score;
    }
}
