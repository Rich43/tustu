package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.compiler.OpMap;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.patterns.ContextMatchStepPattern;
import com.sun.org.apache.xpath.internal.patterns.FunctionPattern;
import com.sun.org.apache.xpath.internal.patterns.StepPattern;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/WalkerFactory.class */
public class WalkerFactory {
    static final boolean DEBUG_PATTERN_CREATION = false;
    static final boolean DEBUG_WALKER_CREATION = false;
    static final boolean DEBUG_ITERATOR_CREATION = false;
    public static final int BITS_COUNT = 255;
    public static final int BITS_RESERVED = 3840;
    public static final int BIT_PREDICATE = 4096;
    public static final int BIT_ANCESTOR = 8192;
    public static final int BIT_ANCESTOR_OR_SELF = 16384;
    public static final int BIT_ATTRIBUTE = 32768;
    public static final int BIT_CHILD = 65536;
    public static final int BIT_DESCENDANT = 131072;
    public static final int BIT_DESCENDANT_OR_SELF = 262144;
    public static final int BIT_FOLLOWING = 524288;
    public static final int BIT_FOLLOWING_SIBLING = 1048576;
    public static final int BIT_NAMESPACE = 2097152;
    public static final int BIT_PARENT = 4194304;
    public static final int BIT_PRECEDING = 8388608;
    public static final int BIT_PRECEDING_SIBLING = 16777216;
    public static final int BIT_SELF = 33554432;
    public static final int BIT_FILTER = 67108864;
    public static final int BIT_ROOT = 134217728;
    public static final int BITMASK_TRAVERSES_OUTSIDE_SUBTREE = 234381312;
    public static final int BIT_BACKWARDS_SELF = 268435456;
    public static final int BIT_ANY_DESCENDANT_FROM_ROOT = 536870912;
    public static final int BIT_NODETEST_ANY = 1073741824;
    public static final int BIT_MATCH_PATTERN = Integer.MIN_VALUE;

    static AxesWalker loadOneWalker(WalkingIterator lpi, Compiler compiler, int stepOpCodePos) throws TransformerException {
        AxesWalker firstWalker = null;
        int stepType = compiler.getOp(stepOpCodePos);
        if (stepType != -1) {
            firstWalker = createDefaultWalker(compiler, stepType, lpi, 0);
            firstWalker.init(compiler, stepOpCodePos, stepType);
        }
        return firstWalker;
    }

    static AxesWalker loadWalkers(WalkingIterator lpi, Compiler compiler, int stepOpCodePos, int stepIndex) throws TransformerException {
        AxesWalker firstWalker = null;
        AxesWalker prevWalker = null;
        int analysis = analyze(compiler, stepOpCodePos, stepIndex);
        do {
            int stepType = compiler.getOp(stepOpCodePos);
            if (-1 == stepType) {
                break;
            }
            AxesWalker walker = createDefaultWalker(compiler, stepOpCodePos, lpi, analysis);
            walker.init(compiler, stepOpCodePos, stepType);
            walker.exprSetParent(lpi);
            if (null == firstWalker) {
                firstWalker = walker;
            } else {
                prevWalker.setNextWalker(walker);
                walker.setPrevWalker(prevWalker);
            }
            prevWalker = walker;
            stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
        } while (stepOpCodePos >= 0);
        return firstWalker;
    }

    public static boolean isSet(int analysis, int bits) {
        return 0 != (analysis & bits);
    }

    public static void diagnoseIterator(String name, int analysis, Compiler compiler) {
        System.out.println(compiler.toString() + ", " + name + ", " + Integer.toBinaryString(analysis) + ", " + getAnalysisString(analysis));
    }

    public static DTMIterator newDTMIterator(Compiler compiler, int opPos, boolean isTopLevel) throws TransformerException {
        DTMIterator iter;
        int firstStepPos = OpMap.getFirstChildPos(opPos);
        int analysis = analyze(compiler, firstStepPos, 0);
        boolean isOneStep = isOneStep(analysis);
        if (isOneStep && walksSelfOnly(analysis) && isWild(analysis) && !hasPredicate(analysis)) {
            iter = new SelfIteratorNoPredicate(compiler, opPos, analysis);
        } else if (walksChildrenOnly(analysis) && isOneStep) {
            if (isWild(analysis) && !hasPredicate(analysis)) {
                iter = new ChildIterator(compiler, opPos, analysis);
            } else {
                iter = new ChildTestIterator(compiler, opPos, analysis);
            }
        } else if (isOneStep && walksAttributes(analysis)) {
            iter = new AttributeIterator(compiler, opPos, analysis);
        } else if (isOneStep && !walksFilteredList(analysis)) {
            if (!walksNamespaces(analysis) && (walksInDocOrder(analysis) || isSet(analysis, 4194304))) {
                iter = new OneStepIteratorForward(compiler, opPos, analysis);
            } else {
                iter = new OneStepIterator(compiler, opPos, analysis);
            }
        } else if (isOptimizableForDescendantIterator(compiler, firstStepPos, 0)) {
            iter = new DescendantIterator(compiler, opPos, analysis);
        } else if (isNaturalDocOrder(compiler, firstStepPos, 0, analysis)) {
            iter = new WalkingIterator(compiler, opPos, analysis, true);
        } else {
            iter = new WalkingIteratorSorted(compiler, opPos, analysis, true);
        }
        if (iter instanceof LocPathIterator) {
            ((LocPathIterator) iter).setIsTopLevel(isTopLevel);
        }
        return iter;
    }

    public static int getAxisFromStep(Compiler compiler, int stepOpCodePos) throws TransformerException {
        int stepType = compiler.getOp(stepOpCodePos);
        switch (stepType) {
            case 22:
            case 23:
            case 24:
            case 25:
                return 20;
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            default:
                throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[]{Integer.toString(stepType)}));
            case 37:
                return 0;
            case 38:
                return 1;
            case 39:
                return 2;
            case 40:
                return 3;
            case 41:
                return 4;
            case 42:
                return 5;
            case 43:
                return 6;
            case 44:
                return 7;
            case 45:
                return 10;
            case 46:
                return 11;
            case 47:
                return 12;
            case 48:
                return 13;
            case 49:
                return 9;
            case 50:
                return 19;
        }
    }

    public static int getAnalysisBitFromAxes(int axis) {
        switch (axis) {
            case 0:
                return 8192;
            case 1:
                return 16384;
            case 2:
                return 32768;
            case 3:
                return 65536;
            case 4:
                return 131072;
            case 5:
                return 262144;
            case 6:
                return 524288;
            case 7:
                return 1048576;
            case 8:
            case 9:
                return 2097152;
            case 10:
                return 4194304;
            case 11:
                return 8388608;
            case 12:
                return 16777216;
            case 13:
                return 33554432;
            case 14:
                return 262144;
            case 15:
            default:
                return 67108864;
            case 16:
            case 17:
            case 18:
                return 536870912;
            case 19:
                return 134217728;
            case 20:
                return 67108864;
        }
    }

    static boolean functionProximateOrContainsProximate(Compiler compiler, int opPos) {
        int endFunc = (opPos + compiler.getOp(opPos + 1)) - 1;
        int opPos2 = OpMap.getFirstChildPos(opPos);
        int funcID = compiler.getOp(opPos2);
        switch (funcID) {
            case 1:
            case 2:
                break;
            default:
                int i2 = 0;
                int p2 = opPos2 + 1;
                while (p2 < endFunc) {
                    int innerExprOpPos = p2 + 2;
                    compiler.getOp(innerExprOpPos);
                    boolean prox = isProximateInnerExpr(compiler, innerExprOpPos);
                    if (prox) {
                        break;
                    } else {
                        p2 = compiler.getNextOpPos(p2);
                        i2++;
                    }
                }
                break;
        }
        return true;
    }

    static boolean isProximateInnerExpr(Compiler compiler, int opPos) {
        int op = compiler.getOp(opPos);
        int innerExprOpPos = opPos + 2;
        switch (op) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                int leftPos = OpMap.getFirstChildPos(op);
                int rightPos = compiler.getNextOpPos(leftPos);
                boolean isProx = isProximateInnerExpr(compiler, leftPos);
                if (!isProx) {
                    boolean isProx2 = isProximateInnerExpr(compiler, rightPos);
                    if (isProx2) {
                    }
                }
                break;
            case 25:
                boolean isProx3 = functionProximateOrContainsProximate(compiler, opPos);
                if (isProx3) {
                }
                break;
            case 26:
                if (isProximateInnerExpr(compiler, innerExprOpPos)) {
                }
                break;
        }
        return true;
    }

    public static boolean mightBeProximate(Compiler compiler, int opPos, int stepType) throws TransformerException {
        switch (stepType) {
            case 22:
            case 23:
            case 24:
            case 25:
                compiler.getArgLength(opPos);
                break;
            default:
                compiler.getArgLengthOfStep(opPos);
                break;
        }
        int predPos = compiler.getFirstPredicateOpPos(opPos);
        int count = 0;
        while (29 == compiler.getOp(predPos)) {
            count++;
            int innerExprOpPos = predPos + 2;
            int predOp = compiler.getOp(innerExprOpPos);
            switch (predOp) {
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    int leftPos = OpMap.getFirstChildPos(innerExprOpPos);
                    int rightPos = compiler.getNextOpPos(leftPos);
                    boolean isProx = isProximateInnerExpr(compiler, leftPos);
                    if (isProx) {
                        return true;
                    }
                    boolean isProx2 = isProximateInnerExpr(compiler, rightPos);
                    if (!isProx2) {
                        break;
                    } else {
                        return true;
                    }
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 20:
                case 21:
                case 23:
                case 24:
                case 26:
                default:
                    return true;
                case 19:
                case 27:
                    return true;
                case 22:
                    return true;
                case 25:
                    boolean isProx3 = functionProximateOrContainsProximate(compiler, innerExprOpPos);
                    if (!isProx3) {
                        break;
                    } else {
                        return true;
                    }
                case 28:
                    break;
            }
            predPos = compiler.getNextOpPos(predPos);
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x015a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean isOptimizableForDescendantIterator(com.sun.org.apache.xpath.internal.compiler.Compiler r8, int r9, int r10) throws javax.xml.transform.TransformerException {
        /*
            Method dump skipped, instructions count: 348
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xpath.internal.axes.WalkerFactory.isOptimizableForDescendantIterator(com.sun.org.apache.xpath.internal.compiler.Compiler, int, int):boolean");
    }

    private static int analyze(Compiler compiler, int stepOpCodePos, int stepIndex) throws TransformerException {
        int stepCount = 0;
        int analysisResult = 0;
        do {
            int stepType = compiler.getOp(stepOpCodePos);
            if (-1 != stepType) {
                stepCount++;
                boolean predAnalysis = analyzePredicate(compiler, stepOpCodePos, stepType);
                if (predAnalysis) {
                    analysisResult |= 4096;
                }
                switch (stepType) {
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                        analysisResult |= 67108864;
                        break;
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    default:
                        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[]{Integer.toString(stepType)}));
                    case 37:
                        analysisResult |= 8192;
                        break;
                    case 38:
                        analysisResult |= 16384;
                        break;
                    case 39:
                        analysisResult |= 32768;
                        break;
                    case 40:
                        analysisResult |= 65536;
                        break;
                    case 41:
                        analysisResult |= 131072;
                        break;
                    case 42:
                        if (2 == stepCount && 134217728 == analysisResult) {
                            analysisResult |= 536870912;
                        }
                        analysisResult |= 262144;
                        break;
                    case 43:
                        analysisResult |= 524288;
                        break;
                    case 44:
                        analysisResult |= 1048576;
                        break;
                    case 45:
                        analysisResult |= 4194304;
                        break;
                    case 46:
                        analysisResult |= 8388608;
                        break;
                    case 47:
                        analysisResult |= 16777216;
                        break;
                    case 48:
                        analysisResult |= 33554432;
                        break;
                    case 49:
                        analysisResult |= 2097152;
                        break;
                    case 50:
                        analysisResult |= 134217728;
                        break;
                    case 51:
                        analysisResult |= -2147450880;
                        break;
                    case 52:
                        analysisResult |= -2147475456;
                        break;
                    case 53:
                        analysisResult |= -2143289344;
                        break;
                }
                if (1033 == compiler.getOp(stepOpCodePos + 3)) {
                    analysisResult |= 1073741824;
                }
                stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
            }
            return analysisResult | (stepCount & 255);
        } while (stepOpCodePos >= 0);
        return analysisResult | (stepCount & 255);
    }

    public static boolean isDownwardAxisOfMany(int axis) {
        return 5 == axis || 4 == axis || 6 == axis || 11 == axis;
    }

    static StepPattern loadSteps(MatchPatternIterator mpi, Compiler compiler, int stepOpCodePos, int stepIndex) throws TransformerException {
        StepPattern step = null;
        StepPattern firstStep = null;
        StepPattern prevStep = null;
        int analysis = analyze(compiler, stepOpCodePos, stepIndex);
        while (-1 != compiler.getOp(stepOpCodePos)) {
            step = createDefaultStepPattern(compiler, stepOpCodePos, mpi, analysis, firstStep, prevStep);
            if (null == firstStep) {
                firstStep = step;
            } else {
                step.setRelativePathPattern(prevStep);
            }
            prevStep = step;
            stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
            if (stepOpCodePos < 0) {
                break;
            }
        }
        int axis = 13;
        StepPattern tail = step;
        StepPattern relativePathPattern = step;
        while (true) {
            StepPattern pat = relativePathPattern;
            if (null == pat) {
                break;
            }
            int nextAxis = pat.getAxis();
            pat.setAxis(axis);
            int whatToShow = pat.getWhatToShow();
            if (whatToShow == 2 || whatToShow == 4096) {
                int newAxis = whatToShow == 2 ? 2 : 9;
                if (isDownwardAxisOfMany(axis)) {
                    StepPattern attrPat = new StepPattern(whatToShow, pat.getNamespace(), pat.getLocalName(), newAxis, 0);
                    XNumber score = pat.getStaticScore();
                    pat.setNamespace(null);
                    pat.setLocalName("*");
                    attrPat.setPredicates(pat.getPredicates());
                    pat.setPredicates(null);
                    pat.setWhatToShow(1);
                    StepPattern rel = pat.getRelativePathPattern();
                    pat.setRelativePathPattern(attrPat);
                    attrPat.setRelativePathPattern(rel);
                    attrPat.setStaticScore(score);
                    if (11 == pat.getAxis()) {
                        pat.setAxis(15);
                    } else if (4 == pat.getAxis()) {
                        pat.setAxis(5);
                    }
                    pat = attrPat;
                } else if (3 == pat.getAxis()) {
                    pat.setAxis(2);
                }
            }
            axis = nextAxis;
            tail = pat;
            relativePathPattern = pat.getRelativePathPattern();
        }
        if (axis < 16) {
            StepPattern selfPattern = new ContextMatchStepPattern(axis, 13);
            XNumber score2 = tail.getStaticScore();
            tail.setRelativePathPattern(selfPattern);
            tail.setStaticScore(score2);
            selfPattern.setStaticScore(score2);
        }
        return step;
    }

    private static StepPattern createDefaultStepPattern(Compiler compiler, int opPos, MatchPatternIterator mpi, int analysis, StepPattern tail, StepPattern head) throws TransformerException {
        int axis;
        int predicateAxis;
        Expression expr;
        int stepType = compiler.getOp(opPos);
        compiler.getWhatToShow(opPos);
        StepPattern ai2 = null;
        switch (stepType) {
            case 22:
            case 23:
            case 24:
            case 25:
                switch (stepType) {
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                        expr = compiler.compileExpression(opPos);
                        break;
                    default:
                        expr = compiler.compileExpression(opPos + 2);
                        break;
                }
                axis = 20;
                predicateAxis = 20;
                ai2 = new FunctionPattern(expr, 20, 20);
                break;
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            default:
                throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[]{Integer.toString(stepType)}));
            case 37:
                axis = 4;
                predicateAxis = 0;
                break;
            case 38:
                axis = 5;
                predicateAxis = 1;
                break;
            case 39:
                axis = 10;
                predicateAxis = 2;
                break;
            case 40:
                axis = 10;
                predicateAxis = 3;
                break;
            case 41:
                axis = 0;
                predicateAxis = 4;
                break;
            case 42:
                axis = 1;
                predicateAxis = 5;
                break;
            case 43:
                axis = 11;
                predicateAxis = 6;
                break;
            case 44:
                axis = 12;
                predicateAxis = 7;
                break;
            case 45:
                axis = 3;
                predicateAxis = 10;
                break;
            case 46:
                axis = 6;
                predicateAxis = 11;
                break;
            case 47:
                axis = 7;
                predicateAxis = 12;
                break;
            case 48:
                axis = 13;
                predicateAxis = 13;
                break;
            case 49:
                axis = 10;
                predicateAxis = 9;
                break;
            case 50:
                axis = 19;
                predicateAxis = 19;
                ai2 = new StepPattern(1280, 19, 19);
                break;
        }
        if (null == ai2) {
            int whatToShow = compiler.getWhatToShow(opPos);
            ai2 = new StepPattern(whatToShow, compiler.getStepNS(opPos), compiler.getStepLocalName(opPos), axis, predicateAxis);
        }
        int argLen = compiler.getFirstPredicateOpPos(opPos);
        ai2.setPredicates(compiler.getCompiledPredicates(argLen));
        return ai2;
    }

    static boolean analyzePredicate(Compiler compiler, int opPos, int stepType) throws TransformerException {
        switch (stepType) {
            case 22:
            case 23:
            case 24:
            case 25:
                compiler.getArgLength(opPos);
                break;
            default:
                compiler.getArgLengthOfStep(opPos);
                break;
        }
        int pos = compiler.getFirstPredicateOpPos(opPos);
        int nPredicates = compiler.countPredicates(pos);
        return nPredicates > 0;
    }

    private static AxesWalker createDefaultWalker(Compiler compiler, int opPos, WalkingIterator lpi, int analysis) {
        AxesWalker ai2;
        int stepType = compiler.getOp(opPos);
        boolean simpleInit = false;
        int i2 = analysis & 255;
        switch (stepType) {
            case 22:
            case 23:
            case 24:
            case 25:
                ai2 = new FilterExprWalker(lpi);
                simpleInit = true;
                break;
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            default:
                throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[]{Integer.toString(stepType)}));
            case 37:
                ai2 = new ReverseAxesWalker(lpi, 0);
                break;
            case 38:
                ai2 = new ReverseAxesWalker(lpi, 1);
                break;
            case 39:
                ai2 = new AxesWalker(lpi, 2);
                break;
            case 40:
                ai2 = new AxesWalker(lpi, 3);
                break;
            case 41:
                ai2 = new AxesWalker(lpi, 4);
                break;
            case 42:
                ai2 = new AxesWalker(lpi, 5);
                break;
            case 43:
                ai2 = new AxesWalker(lpi, 6);
                break;
            case 44:
                ai2 = new AxesWalker(lpi, 7);
                break;
            case 45:
                ai2 = new ReverseAxesWalker(lpi, 10);
                break;
            case 46:
                ai2 = new ReverseAxesWalker(lpi, 11);
                break;
            case 47:
                ai2 = new ReverseAxesWalker(lpi, 12);
                break;
            case 48:
                ai2 = new AxesWalker(lpi, 13);
                break;
            case 49:
                ai2 = new AxesWalker(lpi, 9);
                break;
            case 50:
                ai2 = new AxesWalker(lpi, 19);
                break;
        }
        if (simpleInit) {
            ai2.initNodeTest(-1);
        } else {
            int whatToShow = compiler.getWhatToShow(opPos);
            if (0 == (whatToShow & 4163) || whatToShow == -1) {
                ai2.initNodeTest(whatToShow);
            } else {
                ai2.initNodeTest(whatToShow, compiler.getStepNS(opPos), compiler.getStepLocalName(opPos));
            }
        }
        return ai2;
    }

    public static String getAnalysisString(int analysis) {
        StringBuffer buf = new StringBuffer();
        buf.append("count: ").append(getStepCount(analysis)).append(' ');
        if ((analysis & 1073741824) != 0) {
            buf.append("NTANY|");
        }
        if ((analysis & 4096) != 0) {
            buf.append("PRED|");
        }
        if ((analysis & 8192) != 0) {
            buf.append("ANC|");
        }
        if ((analysis & 16384) != 0) {
            buf.append("ANCOS|");
        }
        if ((analysis & 32768) != 0) {
            buf.append("ATTR|");
        }
        if ((analysis & 65536) != 0) {
            buf.append("CH|");
        }
        if ((analysis & 131072) != 0) {
            buf.append("DESC|");
        }
        if ((analysis & 262144) != 0) {
            buf.append("DESCOS|");
        }
        if ((analysis & 524288) != 0) {
            buf.append("FOL|");
        }
        if ((analysis & 1048576) != 0) {
            buf.append("FOLS|");
        }
        if ((analysis & 2097152) != 0) {
            buf.append("NS|");
        }
        if ((analysis & 4194304) != 0) {
            buf.append("P|");
        }
        if ((analysis & 8388608) != 0) {
            buf.append("PREC|");
        }
        if ((analysis & 16777216) != 0) {
            buf.append("PRECS|");
        }
        if ((analysis & 33554432) != 0) {
            buf.append(".|");
        }
        if ((analysis & 67108864) != 0) {
            buf.append("FLT|");
        }
        if ((analysis & 134217728) != 0) {
            buf.append("R|");
        }
        return buf.toString();
    }

    public static boolean hasPredicate(int analysis) {
        return 0 != (analysis & 4096);
    }

    public static boolean isWild(int analysis) {
        return 0 != (analysis & 1073741824);
    }

    public static boolean walksAncestors(int analysis) {
        return isSet(analysis, 24576);
    }

    public static boolean walksAttributes(int analysis) {
        return 0 != (analysis & 32768);
    }

    public static boolean walksNamespaces(int analysis) {
        return 0 != (analysis & 2097152);
    }

    public static boolean walksChildren(int analysis) {
        return 0 != (analysis & 65536);
    }

    public static boolean walksDescendants(int analysis) {
        return isSet(analysis, 393216);
    }

    public static boolean walksSubtree(int analysis) {
        return isSet(analysis, 458752);
    }

    public static boolean walksSubtreeOnlyMaybeAbsolute(int analysis) {
        return (!walksSubtree(analysis) || walksExtraNodes(analysis) || walksUp(analysis) || walksSideways(analysis)) ? false : true;
    }

    public static boolean walksSubtreeOnly(int analysis) {
        return walksSubtreeOnlyMaybeAbsolute(analysis) && !isAbsolute(analysis);
    }

    public static boolean walksFilteredList(int analysis) {
        return isSet(analysis, 67108864);
    }

    public static boolean walksSubtreeOnlyFromRootOrContext(int analysis) {
        return (!walksSubtree(analysis) || walksExtraNodes(analysis) || walksUp(analysis) || walksSideways(analysis) || isSet(analysis, 67108864)) ? false : true;
    }

    public static boolean walksInDocOrder(int analysis) {
        return (walksSubtreeOnlyMaybeAbsolute(analysis) || walksExtraNodesOnly(analysis) || walksFollowingOnlyMaybeAbsolute(analysis)) && !isSet(analysis, 67108864);
    }

    public static boolean walksFollowingOnlyMaybeAbsolute(int analysis) {
        return (!isSet(analysis, 35127296) || walksSubtree(analysis) || walksUp(analysis) || walksSideways(analysis)) ? false : true;
    }

    public static boolean walksUp(int analysis) {
        return isSet(analysis, 4218880);
    }

    public static boolean walksSideways(int analysis) {
        return isSet(analysis, 26738688);
    }

    public static boolean walksExtraNodes(int analysis) {
        return isSet(analysis, 2129920);
    }

    public static boolean walksExtraNodesOnly(int analysis) {
        return (!walksExtraNodes(analysis) || isSet(analysis, 33554432) || walksSubtree(analysis) || walksUp(analysis) || walksSideways(analysis) || isAbsolute(analysis)) ? false : true;
    }

    public static boolean isAbsolute(int analysis) {
        return isSet(analysis, 201326592);
    }

    public static boolean walksChildrenOnly(int analysis) {
        return (!walksChildren(analysis) || isSet(analysis, 33554432) || walksExtraNodes(analysis) || walksDescendants(analysis) || walksUp(analysis) || walksSideways(analysis) || (isAbsolute(analysis) && !isSet(analysis, 134217728))) ? false : true;
    }

    public static boolean walksChildrenAndExtraAndSelfOnly(int analysis) {
        return (!walksChildren(analysis) || walksDescendants(analysis) || walksUp(analysis) || walksSideways(analysis) || (isAbsolute(analysis) && !isSet(analysis, 134217728))) ? false : true;
    }

    public static boolean walksDescendantsAndExtraAndSelfOnly(int analysis) {
        return (walksChildren(analysis) || !walksDescendants(analysis) || walksUp(analysis) || walksSideways(analysis) || (isAbsolute(analysis) && !isSet(analysis, 134217728))) ? false : true;
    }

    public static boolean walksSelfOnly(int analysis) {
        return (!isSet(analysis, 33554432) || walksSubtree(analysis) || walksUp(analysis) || walksSideways(analysis) || isAbsolute(analysis)) ? false : true;
    }

    public static boolean walksUpOnly(int analysis) {
        return (walksSubtree(analysis) || !walksUp(analysis) || walksSideways(analysis) || isAbsolute(analysis)) ? false : true;
    }

    public static boolean walksDownOnly(int analysis) {
        return (!walksSubtree(analysis) || walksUp(analysis) || walksSideways(analysis) || isAbsolute(analysis)) ? false : true;
    }

    public static boolean walksDownExtraOnly(int analysis) {
        return (!walksSubtree(analysis) || !walksExtraNodes(analysis) || walksUp(analysis) || walksSideways(analysis) || isAbsolute(analysis)) ? false : true;
    }

    public static boolean canSkipSubtrees(int analysis) {
        return isSet(analysis, 65536) | walksSideways(analysis);
    }

    public static boolean canCrissCross(int analysis) {
        if (walksSelfOnly(analysis)) {
            return false;
        }
        if ((!walksDownOnly(analysis) || canSkipSubtrees(analysis)) && !walksChildrenAndExtraAndSelfOnly(analysis) && !walksDescendantsAndExtraAndSelfOnly(analysis) && !walksUpOnly(analysis) && !walksExtraNodesOnly(analysis) && walksSubtree(analysis)) {
            if (walksSideways(analysis) || walksUp(analysis) || canSkipSubtrees(analysis)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isNaturalDocOrder(int analysis) {
        if (!canCrissCross(analysis) && !isSet(analysis, 2097152) && !walksFilteredList(analysis) && walksInDocOrder(analysis)) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0127 A[LOOP:0: B:17:0x0031->B:42:0x0127, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00fd A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x012d A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean isNaturalDocOrder(com.sun.org.apache.xpath.internal.compiler.Compiler r8, int r9, int r10, int r11) throws javax.xml.transform.TransformerException {
        /*
            r0 = r11
            boolean r0 = canCrissCross(r0)
            if (r0 == 0) goto L9
            r0 = 0
            return r0
        L9:
            r0 = r11
            r1 = 2097152(0x200000, float:2.938736E-39)
            boolean r0 = isSet(r0, r1)
            if (r0 == 0) goto L14
            r0 = 0
            return r0
        L14:
            r0 = r11
            r1 = 1572864(0x180000, float:2.204052E-39)
            boolean r0 = isSet(r0, r1)
            if (r0 == 0) goto L28
            r0 = r11
            r1 = 25165824(0x1800000, float:4.7019774E-38)
            boolean r0 = isSet(r0, r1)
            if (r0 == 0) goto L28
            r0 = 0
            return r0
        L28:
            r0 = 0
            r13 = r0
            r0 = 0
            r14 = r0
            r0 = 0
            r15 = r0
        L31:
            r0 = -1
            r1 = r8
            r2 = r9
            int r1 = r1.getOp(r2)
            r2 = r1
            r12 = r2
            if (r0 == r1) goto L12d
            int r13 = r13 + 1
            r0 = r12
            switch(r0) {
                case 22: goto Lee;
                case 23: goto Lee;
                case 24: goto Lee;
                case 25: goto Lee;
                case 26: goto Lff;
                case 27: goto Lff;
                case 28: goto Lff;
                case 29: goto Lff;
                case 30: goto Lff;
                case 31: goto Lff;
                case 32: goto Lff;
                case 33: goto Lff;
                case 34: goto Lff;
                case 35: goto Lff;
                case 36: goto Lff;
                case 37: goto Lee;
                case 38: goto Lee;
                case 39: goto Ld0;
                case 40: goto Lf8;
                case 41: goto Lee;
                case 42: goto Lee;
                case 43: goto Lee;
                case 44: goto Lee;
                case 45: goto Lee;
                case 46: goto Lee;
                case 47: goto Lee;
                case 48: goto Lf8;
                case 49: goto Lee;
                case 50: goto Lf8;
                case 51: goto Ld0;
                case 52: goto Lee;
                case 53: goto Lee;
                default: goto Lff;
            }
        Ld0:
            r0 = r14
            if (r0 == 0) goto Ld7
            r0 = 0
            return r0
        Ld7:
            r0 = r8
            r1 = r9
            java.lang.String r0 = r0.getStepLocalName(r1)
            r16 = r0
            r0 = r16
            java.lang.String r1 = "*"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L118
            r0 = 1
            r14 = r0
            goto L118
        Lee:
            r0 = r15
            if (r0 <= 0) goto Lf5
            r0 = 0
            return r0
        Lf5:
            int r15 = r15 + 1
        Lf8:
            r0 = r14
            if (r0 == 0) goto L118
            r0 = 0
            return r0
        Lff:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r1 = r0
            java.lang.String r2 = "ER_NULL_ERROR_HANDLER"
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r4 = r3
            r5 = 0
            r6 = r12
            java.lang.String r6 = java.lang.Integer.toString(r6)
            r4[r5] = r6
            java.lang.String r2 = com.sun.org.apache.xalan.internal.res.XSLMessages.createXPATHMessage(r2, r3)
            r1.<init>(r2)
            throw r0
        L118:
            r0 = r8
            r1 = r9
            int r0 = r0.getNextStepPos(r1)
            r16 = r0
            r0 = r16
            if (r0 >= 0) goto L127
            goto L12d
        L127:
            r0 = r16
            r9 = r0
            goto L31
        L12d:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xpath.internal.axes.WalkerFactory.isNaturalDocOrder(com.sun.org.apache.xpath.internal.compiler.Compiler, int, int, int):boolean");
    }

    public static boolean isOneStep(int analysis) {
        return (analysis & 255) == 1;
    }

    public static int getStepCount(int analysis) {
        return analysis & 255;
    }
}
