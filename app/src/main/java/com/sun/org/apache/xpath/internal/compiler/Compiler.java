package com.sun.org.apache.xpath.internal.compiler;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xml.internal.utils.QName;
import com.sun.org.apache.xml.internal.utils.SAXSourceLocator;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.axes.UnionPathIterator;
import com.sun.org.apache.xpath.internal.axes.WalkerFactory;
import com.sun.org.apache.xpath.internal.functions.FuncExtFunction;
import com.sun.org.apache.xpath.internal.functions.FuncExtFunctionAvailable;
import com.sun.org.apache.xpath.internal.functions.Function;
import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XString;
import com.sun.org.apache.xpath.internal.operations.And;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.org.apache.xpath.internal.operations.Div;
import com.sun.org.apache.xpath.internal.operations.Equals;
import com.sun.org.apache.xpath.internal.operations.Gt;
import com.sun.org.apache.xpath.internal.operations.Gte;
import com.sun.org.apache.xpath.internal.operations.Lt;
import com.sun.org.apache.xpath.internal.operations.Lte;
import com.sun.org.apache.xpath.internal.operations.Minus;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.org.apache.xpath.internal.operations.Mult;
import com.sun.org.apache.xpath.internal.operations.Neg;
import com.sun.org.apache.xpath.internal.operations.NotEquals;
import com.sun.org.apache.xpath.internal.operations.Number;
import com.sun.org.apache.xpath.internal.operations.Operation;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.sun.org.apache.xpath.internal.operations.Plus;
import com.sun.org.apache.xpath.internal.operations.String;
import com.sun.org.apache.xpath.internal.operations.UnaryOperation;
import com.sun.org.apache.xpath.internal.operations.Variable;
import com.sun.org.apache.xpath.internal.patterns.FunctionPattern;
import com.sun.org.apache.xpath.internal.patterns.StepPattern;
import com.sun.org.apache.xpath.internal.patterns.UnionPattern;
import com.sun.org.apache.xpath.internal.res.XPATHErrorResources;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/compiler/Compiler.class */
public class Compiler extends OpMap {
    int countOp;
    private int locPathDepth;
    private static final boolean DEBUG = false;
    private static long s_nextMethodId = 0;
    private PrefixResolver m_currentPrefixResolver;
    ErrorListener m_errorHandler;
    SourceLocator m_locator;
    private FunctionTable m_functionTable;

    public Compiler(ErrorListener errorHandler, SourceLocator locator, FunctionTable fTable) {
        this.locPathDepth = -1;
        this.m_currentPrefixResolver = null;
        this.m_errorHandler = errorHandler;
        this.m_locator = locator;
        this.m_functionTable = fTable;
    }

    public Compiler() {
        this.locPathDepth = -1;
        this.m_currentPrefixResolver = null;
        this.m_errorHandler = null;
        this.m_locator = null;
    }

    public Expression compileExpression(int opPos) throws TransformerException {
        try {
            this.countOp = 0;
            return compile(opPos);
        } catch (StackOverflowError e2) {
            error(XPATHErrorResources.ER_COMPILATION_TOO_MANY_OPERATION, new Object[]{Integer.valueOf(this.countOp)});
            return null;
        }
    }

    private Expression compile(int opPos) throws TransformerException {
        int op = getOp(opPos);
        Expression expr = null;
        switch (op) {
            case 1:
                expr = compile(opPos + 2);
                break;
            case 2:
                expr = or(opPos);
                break;
            case 3:
                expr = and(opPos);
                break;
            case 4:
                expr = notequals(opPos);
                break;
            case 5:
                expr = equals(opPos);
                break;
            case 6:
                expr = lte(opPos);
                break;
            case 7:
                expr = lt(opPos);
                break;
            case 8:
                expr = gte(opPos);
                break;
            case 9:
                expr = gt(opPos);
                break;
            case 10:
                expr = plus(opPos);
                break;
            case 11:
                expr = minus(opPos);
                break;
            case 12:
                expr = mult(opPos);
                break;
            case 13:
                expr = div(opPos);
                break;
            case 14:
                expr = mod(opPos);
                break;
            case 15:
                error("ER_UNKNOWN_OPCODE", new Object[]{"quo"});
                break;
            case 16:
                expr = neg(opPos);
                break;
            case 17:
                expr = string(opPos);
                break;
            case 18:
                expr = bool(opPos);
                break;
            case 19:
                expr = number(opPos);
                break;
            case 20:
                expr = union(opPos);
                break;
            case 21:
                expr = literal(opPos);
                break;
            case 22:
                expr = variable(opPos);
                break;
            case 23:
                expr = group(opPos);
                break;
            case 24:
                expr = compileExtension(opPos);
                break;
            case 25:
                expr = compileFunction(opPos);
                break;
            case 26:
                expr = arg(opPos);
                break;
            case 27:
                expr = numberlit(opPos);
                break;
            case 28:
                expr = locationPath(opPos);
                break;
            case 29:
                expr = null;
                break;
            case 30:
                expr = matchPattern(opPos + 2);
                break;
            case 31:
                expr = locationPathPattern(opPos);
                break;
            default:
                error("ER_UNKNOWN_OPCODE", new Object[]{Integer.toString(getOp(opPos))});
                break;
        }
        return expr;
    }

    private Expression compileOperation(Operation operation, int opPos) throws TransformerException {
        this.countOp++;
        int leftPos = getFirstChildPos(opPos);
        int rightPos = getNextOpPos(leftPos);
        operation.setLeftRight(compile(leftPos), compile(rightPos));
        return operation;
    }

    private Expression compileUnary(UnaryOperation unary, int opPos) throws TransformerException {
        int rightPos = getFirstChildPos(opPos);
        unary.setRight(compile(rightPos));
        return unary;
    }

    protected Expression or(int opPos) throws TransformerException {
        return compileOperation(new Or(), opPos);
    }

    protected Expression and(int opPos) throws TransformerException {
        return compileOperation(new And(), opPos);
    }

    protected Expression notequals(int opPos) throws TransformerException {
        return compileOperation(new NotEquals(), opPos);
    }

    protected Expression equals(int opPos) throws TransformerException {
        return compileOperation(new Equals(), opPos);
    }

    protected Expression lte(int opPos) throws TransformerException {
        return compileOperation(new Lte(), opPos);
    }

    protected Expression lt(int opPos) throws TransformerException {
        return compileOperation(new Lt(), opPos);
    }

    protected Expression gte(int opPos) throws TransformerException {
        return compileOperation(new Gte(), opPos);
    }

    protected Expression gt(int opPos) throws TransformerException {
        return compileOperation(new Gt(), opPos);
    }

    protected Expression plus(int opPos) throws TransformerException {
        return compileOperation(new Plus(), opPos);
    }

    protected Expression minus(int opPos) throws TransformerException {
        return compileOperation(new Minus(), opPos);
    }

    protected Expression mult(int opPos) throws TransformerException {
        return compileOperation(new Mult(), opPos);
    }

    protected Expression div(int opPos) throws TransformerException {
        return compileOperation(new Div(), opPos);
    }

    protected Expression mod(int opPos) throws TransformerException {
        return compileOperation(new Mod(), opPos);
    }

    protected Expression neg(int opPos) throws TransformerException {
        return compileUnary(new Neg(), opPos);
    }

    protected Expression string(int opPos) throws TransformerException {
        return compileUnary(new String(), opPos);
    }

    protected Expression bool(int opPos) throws TransformerException {
        return compileUnary(new Bool(), opPos);
    }

    protected Expression number(int opPos) throws TransformerException {
        return compileUnary(new Number(), opPos);
    }

    protected Expression literal(int opPos) {
        return (XString) getTokenQueue().elementAt(getOp(getFirstChildPos(opPos)));
    }

    protected Expression numberlit(int opPos) {
        return (XNumber) getTokenQueue().elementAt(getOp(getFirstChildPos(opPos)));
    }

    protected Expression variable(int opPos) throws TransformerException {
        Variable var = new Variable();
        int opPos2 = getFirstChildPos(opPos);
        int nsPos = getOp(opPos2);
        String namespace = -2 == nsPos ? null : (String) getTokenQueue().elementAt(nsPos);
        String localname = (String) getTokenQueue().elementAt(getOp(opPos2 + 1));
        QName qname = new QName(namespace, localname);
        var.setQName(qname);
        return var;
    }

    protected Expression group(int opPos) throws TransformerException {
        return compile(opPos + 2);
    }

    protected Expression arg(int opPos) throws TransformerException {
        return compile(opPos + 2);
    }

    protected Expression union(int opPos) throws TransformerException {
        this.locPathDepth++;
        try {
            return UnionPathIterator.createUnionIterator(this, opPos);
        } finally {
            this.locPathDepth--;
        }
    }

    public int getLocationPathDepth() {
        return this.locPathDepth;
    }

    FunctionTable getFunctionTable() {
        return this.m_functionTable;
    }

    public Expression locationPath(int opPos) throws TransformerException {
        this.locPathDepth++;
        try {
            Expression expression = (Expression) WalkerFactory.newDTMIterator(this, opPos, this.locPathDepth == 0);
            this.locPathDepth--;
            return expression;
        } catch (Throwable th) {
            this.locPathDepth--;
            throw th;
        }
    }

    public Expression predicate(int opPos) throws TransformerException {
        return compile(opPos + 2);
    }

    protected Expression matchPattern(int opPos) throws TransformerException {
        this.locPathDepth++;
        int nextOpPos = opPos;
        int i2 = 0;
        while (getOp(nextOpPos) == 31) {
            try {
                nextOpPos = getNextOpPos(nextOpPos);
                i2++;
            } catch (Throwable th) {
                this.locPathDepth--;
                throw th;
            }
        }
        if (i2 == 1) {
            Expression expressionCompile = compile(opPos);
            this.locPathDepth--;
            return expressionCompile;
        }
        UnionPattern up = new UnionPattern();
        StepPattern[] patterns = new StepPattern[i2];
        int i3 = 0;
        while (getOp(opPos) == 31) {
            int nextOpPos2 = getNextOpPos(opPos);
            patterns[i3] = (StepPattern) compile(opPos);
            opPos = nextOpPos2;
            i3++;
        }
        up.setPatterns(patterns);
        this.locPathDepth--;
        return up;
    }

    public Expression locationPathPattern(int opPos) throws TransformerException {
        return stepPattern(getFirstChildPos(opPos), 0, null);
    }

    public int getWhatToShow(int opPos) {
        int axesType = getOp(opPos);
        int testType = getOp(opPos + 3);
        switch (testType) {
            case 34:
                switch (axesType) {
                    case 39:
                    case 51:
                        return 2;
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 50:
                    default:
                        return 1;
                    case 49:
                        return 4096;
                    case 52:
                    case 53:
                        return 1;
                }
            case 35:
                return 1280;
            case OpCodes.NODETYPE_COMMENT /* 1030 */:
                return 128;
            case OpCodes.NODETYPE_TEXT /* 1031 */:
                return 12;
            case OpCodes.NODETYPE_PI /* 1032 */:
                return 64;
            case 1033:
                switch (axesType) {
                    case 38:
                    case 42:
                    case 48:
                        return -1;
                    case 39:
                    case 51:
                        return 2;
                    case 40:
                    case 41:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 50:
                    default:
                        if (getOp(0) == 30) {
                            return -1283;
                        }
                        return -3;
                    case 49:
                        return 4096;
                }
            case OpCodes.NODETYPE_FUNCTEST /* 1034 */:
                return 65536;
            default:
                return -1;
        }
    }

    protected StepPattern stepPattern(int opPos, int stepCount, StepPattern ancestorPattern) throws TransformerException {
        int argLen;
        StepPattern pattern;
        int stepType = getOp(opPos);
        if (-1 == stepType) {
            return null;
        }
        int endStep = getNextOpPos(opPos);
        switch (stepType) {
            case 25:
                argLen = getOp(opPos + 1);
                pattern = new FunctionPattern(compileFunction(opPos), 10, 3);
                break;
            case 50:
                argLen = getArgLengthOfStep(opPos);
                opPos = getFirstChildPosOfStep(opPos);
                pattern = new StepPattern(1280, 10, 3);
                break;
            case 51:
                argLen = getArgLengthOfStep(opPos);
                opPos = getFirstChildPosOfStep(opPos);
                pattern = new StepPattern(2, getStepNS(opPos), getStepLocalName(opPos), 10, 2);
                break;
            case 52:
                argLen = getArgLengthOfStep(opPos);
                opPos = getFirstChildPosOfStep(opPos);
                int what = getWhatToShow(opPos);
                if (1280 == what) {
                }
                pattern = new StepPattern(getWhatToShow(opPos), getStepNS(opPos), getStepLocalName(opPos), 0, 3);
                break;
            case 53:
                argLen = getArgLengthOfStep(opPos);
                opPos = getFirstChildPosOfStep(opPos);
                pattern = new StepPattern(getWhatToShow(opPos), getStepNS(opPos), getStepLocalName(opPos), 10, 3);
                break;
            default:
                error("ER_UNKNOWN_MATCH_OPERATION", null);
                return null;
        }
        pattern.setPredicates(getCompiledPredicates(opPos + argLen));
        if (null != ancestorPattern) {
            pattern.setRelativePathPattern(ancestorPattern);
        }
        StepPattern relativePathPattern = stepPattern(endStep, stepCount + 1, pattern);
        return null != relativePathPattern ? relativePathPattern : pattern;
    }

    public Expression[] getCompiledPredicates(int opPos) throws TransformerException {
        int count = countPredicates(opPos);
        if (count > 0) {
            Expression[] predicates = new Expression[count];
            compilePredicates(opPos, predicates);
            return predicates;
        }
        return null;
    }

    public int countPredicates(int opPos) throws TransformerException {
        int count = 0;
        while (29 == getOp(opPos)) {
            count++;
            opPos = getNextOpPos(opPos);
        }
        return count;
    }

    private void compilePredicates(int opPos, Expression[] predicates) throws TransformerException {
        int i2 = 0;
        while (29 == getOp(opPos)) {
            predicates[i2] = predicate(opPos);
            opPos = getNextOpPos(opPos);
            i2++;
        }
    }

    Expression compileFunction(int opPos) throws TransformerException {
        int endFunc = (opPos + getOp(opPos + 1)) - 1;
        int opPos2 = getFirstChildPos(opPos);
        int funcID = getOp(opPos2);
        int opPos3 = opPos2 + 1;
        if (-1 != funcID) {
            Function func = this.m_functionTable.getFunction(funcID);
            if (func instanceof FuncExtFunctionAvailable) {
                ((FuncExtFunctionAvailable) func).setFunctionTable(this.m_functionTable);
            }
            func.postCompileStep(this);
            int i2 = 0;
            int p2 = opPos3;
            while (p2 < endFunc) {
                try {
                    func.setArg(compile(p2), i2);
                    p2 = getNextOpPos(p2);
                    i2++;
                } catch (WrongNumberArgsException wnae) {
                    String name = this.m_functionTable.getFunctionName(funcID);
                    this.m_errorHandler.fatalError(new TransformerException(XSLMessages.createXPATHMessage("ER_ONLY_ALLOWS", new Object[]{name, wnae.getMessage()}), this.m_locator));
                }
            }
            func.checkNumberArgs(i2);
            return func;
        }
        error("ER_FUNCTION_TOKEN_NOT_FOUND", null);
        return null;
    }

    private synchronized long getNextMethodId() {
        if (s_nextMethodId == Long.MAX_VALUE) {
            s_nextMethodId = 0L;
        }
        long j2 = s_nextMethodId;
        s_nextMethodId = j2 + 1;
        return j2;
    }

    private Expression compileExtension(int opPos) throws TransformerException {
        int endExtFunc = (opPos + getOp(opPos + 1)) - 1;
        int opPos2 = getFirstChildPos(opPos);
        String ns = (String) getTokenQueue().elementAt(getOp(opPos2));
        int opPos3 = opPos2 + 1;
        String funcName = (String) getTokenQueue().elementAt(getOp(opPos3));
        int opPos4 = opPos3 + 1;
        Function extension = new FuncExtFunction(ns, funcName, String.valueOf(getNextMethodId()));
        int i2 = 0;
        while (opPos4 < endExtFunc) {
            try {
                int nextOpPos = getNextOpPos(opPos4);
                extension.setArg(compile(opPos4), i2);
                opPos4 = nextOpPos;
                i2++;
            } catch (WrongNumberArgsException e2) {
            }
        }
        return extension;
    }

    public void warn(String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHWarning(msg, args);
        if (null != this.m_errorHandler) {
            this.m_errorHandler.warning(new TransformerException(fmsg, this.m_locator));
        } else {
            System.out.println(fmsg + "; file " + this.m_locator.getSystemId() + "; line " + this.m_locator.getLineNumber() + "; column " + this.m_locator.getColumnNumber());
        }
    }

    public void assertion(boolean b2, String msg) {
        if (!b2) {
            String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{msg});
            throw new RuntimeException(fMsg);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.compiler.OpMap
    public void error(String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHMessage(msg, args);
        if (null != this.m_errorHandler) {
            this.m_errorHandler.fatalError(new TransformerException(fmsg, this.m_locator));
            return;
        }
        throw new TransformerException(fmsg, (SAXSourceLocator) this.m_locator);
    }

    public PrefixResolver getNamespaceContext() {
        return this.m_currentPrefixResolver;
    }

    public void setNamespaceContext(PrefixResolver pr) {
        this.m_currentPrefixResolver = pr;
    }
}
