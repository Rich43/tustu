package com.sun.org.apache.xpath.internal.compiler;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.utils.ObjectVector;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xpath.internal.XPathProcessorException;
import com.sun.org.apache.xpath.internal.domapi.XPathStylesheetDOM3Exception;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XString;
import com.sun.org.apache.xpath.internal.res.XPATHErrorResources;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import jdk.xml.internal.XMLSecurityManager;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/compiler/XPathParser.class */
public class XPathParser {
    public static final String CONTINUE_AFTER_FATAL_ERROR = "CONTINUE_AFTER_FATAL_ERROR";
    private OpMap m_ops;
    transient String m_token;
    transient char m_tokenChar = 0;
    int m_queueMark = 0;
    protected static final int FILTER_MATCH_FAILED = 0;
    protected static final int FILTER_MATCH_PRIMARY = 1;
    protected static final int FILTER_MATCH_PREDICATES = 2;
    private int countPredicate;
    XMLSecurityManager m_xmlSecMgr;
    PrefixResolver m_namespaceContext;
    private ErrorListener m_errorListener;
    SourceLocator m_sourceLocator;
    private FunctionTable m_functionTable;

    public XPathParser(ErrorListener errorListener, SourceLocator sourceLocator, XMLSecurityManager xmlSecMgr) {
        this.m_errorListener = errorListener;
        this.m_sourceLocator = sourceLocator;
        this.m_xmlSecMgr = xmlSecMgr;
    }

    public void initXPath(Compiler compiler, String expression, PrefixResolver namespaceContext) throws TransformerException {
        this.m_ops = compiler;
        this.m_namespaceContext = namespaceContext;
        this.m_functionTable = compiler.getFunctionTable();
        Lexer lexer = new Lexer(compiler, namespaceContext, this, this.m_xmlSecMgr);
        lexer.tokenize(expression);
        this.m_ops.setOp(0, 1);
        this.m_ops.setOp(1, 2);
        try {
            nextToken();
            Expr();
            if (null != this.m_token) {
                String extraTokens = "";
                while (null != this.m_token) {
                    extraTokens = extraTokens + PdfOps.SINGLE_QUOTE_TOKEN + this.m_token + PdfOps.SINGLE_QUOTE_TOKEN;
                    nextToken();
                    if (null != this.m_token) {
                        extraTokens = extraTokens + ", ";
                    }
                }
                error("ER_EXTRA_ILLEGAL_TOKENS", new Object[]{extraTokens});
            }
        } catch (XPathProcessorException e2) {
            if (CONTINUE_AFTER_FATAL_ERROR.equals(e2.getMessage())) {
                initXPath(compiler, "/..", namespaceContext);
            } else {
                throw e2;
            }
        } catch (StackOverflowError e3) {
            error(XPATHErrorResources.ER_PREDICATE_TOO_MANY_OPEN, new Object[]{this.m_token, Integer.valueOf(this.m_queueMark), Integer.valueOf(this.countPredicate)});
        }
        compiler.shrink();
    }

    public void initMatchPattern(Compiler compiler, String expression, PrefixResolver namespaceContext) throws TransformerException {
        this.m_ops = compiler;
        this.m_namespaceContext = namespaceContext;
        this.m_functionTable = compiler.getFunctionTable();
        Lexer lexer = new Lexer(compiler, namespaceContext, this, this.m_xmlSecMgr);
        lexer.tokenize(expression);
        this.m_ops.setOp(0, 30);
        this.m_ops.setOp(1, 2);
        nextToken();
        try {
            Pattern();
        } catch (StackOverflowError e2) {
            error(XPATHErrorResources.ER_PREDICATE_TOO_MANY_OPEN, new Object[]{this.m_token, Integer.valueOf(this.m_queueMark), Integer.valueOf(this.countPredicate)});
        }
        if (null != this.m_token) {
            String extraTokens = "";
            while (null != this.m_token) {
                extraTokens = extraTokens + PdfOps.SINGLE_QUOTE_TOKEN + this.m_token + PdfOps.SINGLE_QUOTE_TOKEN;
                nextToken();
                if (null != this.m_token) {
                    extraTokens = extraTokens + ", ";
                }
            }
            error("ER_EXTRA_ILLEGAL_TOKENS", new Object[]{extraTokens});
        }
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.shrink();
    }

    public void setErrorHandler(ErrorListener handler) {
        this.m_errorListener = handler;
    }

    public ErrorListener getErrorListener() {
        return this.m_errorListener;
    }

    final boolean tokenIs(String s2) {
        return this.m_token != null ? this.m_token.equals(s2) : s2 == null;
    }

    final boolean tokenIs(char c2) {
        return this.m_token != null && this.m_tokenChar == c2;
    }

    final boolean lookahead(char c2, int n2) {
        boolean b2;
        int pos = this.m_queueMark + n2;
        if (pos <= this.m_ops.getTokenQueueSize() && pos > 0 && this.m_ops.getTokenQueueSize() != 0) {
            String tok = (String) this.m_ops.m_tokenQueue.elementAt(pos - 1);
            boolean z2 = tok.length() == 1 && tok.charAt(0) == c2;
            b2 = z2;
        } else {
            b2 = false;
        }
        return b2;
    }

    private final boolean lookbehind(char c2, int n2) {
        boolean isToken;
        int lookBehindPos = this.m_queueMark - (n2 + 1);
        if (lookBehindPos >= 0) {
            String lookbehind = (String) this.m_ops.m_tokenQueue.elementAt(lookBehindPos);
            if (lookbehind.length() == 1) {
                char c0 = lookbehind == null ? '|' : lookbehind.charAt(0);
                boolean z2 = c0 != '|' && c0 == c2;
                isToken = z2;
            } else {
                isToken = false;
            }
        } else {
            isToken = false;
        }
        return isToken;
    }

    private final boolean lookbehindHasToken(int n2) {
        boolean hasToken;
        if (this.m_queueMark - n2 > 0) {
            String lookbehind = (String) this.m_ops.m_tokenQueue.elementAt(this.m_queueMark - (n2 - 1));
            char c0 = lookbehind == null ? '|' : lookbehind.charAt(0);
            hasToken = c0 != '|';
        } else {
            hasToken = false;
        }
        return hasToken;
    }

    private final boolean lookahead(String s2, int n2) {
        boolean isToken;
        if (this.m_queueMark + n2 <= this.m_ops.getTokenQueueSize()) {
            String lookahead = (String) this.m_ops.m_tokenQueue.elementAt(this.m_queueMark + (n2 - 1));
            isToken = lookahead != null ? lookahead.equals(s2) : s2 == null;
        } else {
            isToken = null == s2;
        }
        return isToken;
    }

    private final void nextToken() {
        if (this.m_queueMark < this.m_ops.getTokenQueueSize()) {
            ObjectVector objectVector = this.m_ops.m_tokenQueue;
            int i2 = this.m_queueMark;
            this.m_queueMark = i2 + 1;
            this.m_token = (String) objectVector.elementAt(i2);
            this.m_tokenChar = this.m_token.charAt(0);
            return;
        }
        this.m_token = null;
        this.m_tokenChar = (char) 0;
    }

    private final String getTokenRelative(int i2) {
        String tok;
        int relative = this.m_queueMark + i2;
        if (relative > 0 && relative < this.m_ops.getTokenQueueSize()) {
            tok = (String) this.m_ops.m_tokenQueue.elementAt(relative);
        } else {
            tok = null;
        }
        return tok;
    }

    private final void prevToken() {
        if (this.m_queueMark > 0) {
            this.m_queueMark--;
            this.m_token = (String) this.m_ops.m_tokenQueue.elementAt(this.m_queueMark);
            this.m_tokenChar = this.m_token.charAt(0);
        } else {
            this.m_token = null;
            this.m_tokenChar = (char) 0;
        }
    }

    private final void consumeExpected(String expected) throws TransformerException {
        if (tokenIs(expected)) {
            nextToken();
        } else {
            error("ER_EXPECTED_BUT_FOUND", new Object[]{expected, this.m_token});
            throw new XPathProcessorException(CONTINUE_AFTER_FATAL_ERROR);
        }
    }

    private final void consumeExpected(char expected) throws TransformerException {
        if (tokenIs(expected)) {
            nextToken();
        } else {
            error("ER_EXPECTED_BUT_FOUND", new Object[]{String.valueOf(expected), this.m_token});
            throw new XPathProcessorException(CONTINUE_AFTER_FATAL_ERROR);
        }
    }

    void warn(String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHWarning(msg, args);
        ErrorListener ehandler = getErrorListener();
        if (null != ehandler) {
            ehandler.warning(new TransformerException(fmsg, this.m_sourceLocator));
        } else {
            System.err.println(fmsg);
        }
    }

    private void assertion(boolean b2, String msg) {
        if (!b2) {
            String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{msg});
            throw new RuntimeException(fMsg);
        }
    }

    void error(String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHMessage(msg, args);
        ErrorListener ehandler = getErrorListener();
        TransformerException te = new TransformerException(fmsg, this.m_sourceLocator);
        if (null != ehandler) {
            ehandler.fatalError(te);
            return;
        }
        throw te;
    }

    void errorForDOM3(String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHMessage(msg, args);
        ErrorListener ehandler = getErrorListener();
        TransformerException te = new XPathStylesheetDOM3Exception(fmsg, this.m_sourceLocator);
        if (null != ehandler) {
            ehandler.fatalError(te);
            return;
        }
        throw te;
    }

    protected String dumpRemainingTokenQueue() {
        String returnMsg;
        String msg;
        int q2 = this.m_queueMark;
        if (q2 < this.m_ops.getTokenQueueSize()) {
            String str = "\n Remaining tokens: (";
            while (true) {
                msg = str;
                if (q2 >= this.m_ops.getTokenQueueSize()) {
                    break;
                }
                int i2 = q2;
                q2++;
                String t2 = (String) this.m_ops.m_tokenQueue.elementAt(i2);
                str = msg + " '" + t2 + PdfOps.SINGLE_QUOTE_TOKEN;
            }
            returnMsg = msg + ")";
        } else {
            returnMsg = "";
        }
        return returnMsg;
    }

    final int getFunctionToken(String key) {
        int tok;
        try {
            Object id = Keywords.lookupNodeTest(key);
            if (null == id) {
                id = this.m_functionTable.getFunctionID(key);
            }
            tok = ((Integer) id).intValue();
        } catch (ClassCastException e2) {
            tok = -1;
        } catch (NullPointerException e3) {
            tok = -1;
        }
        return tok;
    }

    void insertOp(int pos, int length, int op) {
        int totalLen = this.m_ops.getOp(1);
        for (int i2 = totalLen - 1; i2 >= pos; i2--) {
            this.m_ops.setOp(i2 + length, this.m_ops.getOp(i2));
        }
        this.m_ops.setOp(pos, op);
        this.m_ops.setOp(1, totalLen + length);
    }

    void appendOp(int length, int op) {
        int totalLen = this.m_ops.getOp(1);
        this.m_ops.setOp(totalLen, op);
        this.m_ops.setOp(totalLen + 1, length);
        this.m_ops.setOp(1, totalLen + length);
    }

    protected void Expr() throws TransformerException {
        OrExpr();
    }

    protected void OrExpr() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        AndExpr();
        if (null != this.m_token && tokenIs("or")) {
            nextToken();
            insertOp(opPos, 2, 2);
            OrExpr();
            this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
        }
    }

    protected void AndExpr() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        EqualityExpr(-1);
        if (null != this.m_token && tokenIs("and")) {
            nextToken();
            insertOp(opPos, 2, 3);
            AndExpr();
            this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
        }
    }

    protected int EqualityExpr(int addPos) throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        if (-1 == addPos) {
            addPos = opPos;
        }
        RelationalExpr(-1);
        if (null != this.m_token) {
            if (tokenIs('!') && lookahead('=', 1)) {
                nextToken();
                nextToken();
                insertOp(addPos, 2, 4);
                int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
                int addPos2 = EqualityExpr(addPos);
                this.m_ops.setOp(addPos2 + 1, this.m_ops.getOp(addPos2 + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
                addPos = addPos2 + 2;
            } else if (tokenIs('=')) {
                nextToken();
                insertOp(addPos, 2, 5);
                int opPlusLeftHandLen2 = this.m_ops.getOp(1) - addPos;
                int addPos3 = EqualityExpr(addPos);
                this.m_ops.setOp(addPos3 + 1, this.m_ops.getOp(addPos3 + opPlusLeftHandLen2 + 1) + opPlusLeftHandLen2);
                addPos = addPos3 + 2;
            }
        }
        return addPos;
    }

    protected int RelationalExpr(int addPos) throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        if (-1 == addPos) {
            addPos = opPos;
        }
        AdditiveExpr(-1);
        if (null != this.m_token) {
            if (tokenIs('<')) {
                nextToken();
                if (tokenIs('=')) {
                    nextToken();
                    insertOp(addPos, 2, 6);
                } else {
                    insertOp(addPos, 2, 7);
                }
                int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
                int addPos2 = RelationalExpr(addPos);
                this.m_ops.setOp(addPos2 + 1, this.m_ops.getOp(addPos2 + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
                addPos = addPos2 + 2;
            } else if (tokenIs('>')) {
                nextToken();
                if (tokenIs('=')) {
                    nextToken();
                    insertOp(addPos, 2, 8);
                } else {
                    insertOp(addPos, 2, 9);
                }
                int opPlusLeftHandLen2 = this.m_ops.getOp(1) - addPos;
                int addPos3 = RelationalExpr(addPos);
                this.m_ops.setOp(addPos3 + 1, this.m_ops.getOp(addPos3 + opPlusLeftHandLen2 + 1) + opPlusLeftHandLen2);
                addPos = addPos3 + 2;
            }
        }
        return addPos;
    }

    protected int AdditiveExpr(int addPos) throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        if (-1 == addPos) {
            addPos = opPos;
        }
        MultiplicativeExpr(-1);
        if (null != this.m_token) {
            if (tokenIs('+')) {
                nextToken();
                insertOp(addPos, 2, 10);
                int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
                int addPos2 = AdditiveExpr(addPos);
                this.m_ops.setOp(addPos2 + 1, this.m_ops.getOp(addPos2 + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
                addPos = addPos2 + 2;
            } else if (tokenIs('-')) {
                nextToken();
                insertOp(addPos, 2, 11);
                int opPlusLeftHandLen2 = this.m_ops.getOp(1) - addPos;
                int addPos3 = AdditiveExpr(addPos);
                this.m_ops.setOp(addPos3 + 1, this.m_ops.getOp(addPos3 + opPlusLeftHandLen2 + 1) + opPlusLeftHandLen2);
                addPos = addPos3 + 2;
            }
        }
        return addPos;
    }

    protected int MultiplicativeExpr(int addPos) throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        if (-1 == addPos) {
            addPos = opPos;
        }
        UnaryExpr();
        if (null != this.m_token) {
            if (tokenIs('*')) {
                nextToken();
                insertOp(addPos, 2, 12);
                int opPlusLeftHandLen = this.m_ops.getOp(1) - addPos;
                int addPos2 = MultiplicativeExpr(addPos);
                this.m_ops.setOp(addPos2 + 1, this.m_ops.getOp(addPos2 + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
                addPos = addPos2 + 2;
            } else if (tokenIs("div")) {
                nextToken();
                insertOp(addPos, 2, 13);
                int opPlusLeftHandLen2 = this.m_ops.getOp(1) - addPos;
                int addPos3 = MultiplicativeExpr(addPos);
                this.m_ops.setOp(addPos3 + 1, this.m_ops.getOp(addPos3 + opPlusLeftHandLen2 + 1) + opPlusLeftHandLen2);
                addPos = addPos3 + 2;
            } else if (tokenIs("mod")) {
                nextToken();
                insertOp(addPos, 2, 14);
                int opPlusLeftHandLen3 = this.m_ops.getOp(1) - addPos;
                int addPos4 = MultiplicativeExpr(addPos);
                this.m_ops.setOp(addPos4 + 1, this.m_ops.getOp(addPos4 + opPlusLeftHandLen3 + 1) + opPlusLeftHandLen3);
                addPos = addPos4 + 2;
            } else if (tokenIs("quo")) {
                nextToken();
                insertOp(addPos, 2, 15);
                int opPlusLeftHandLen4 = this.m_ops.getOp(1) - addPos;
                int addPos5 = MultiplicativeExpr(addPos);
                this.m_ops.setOp(addPos5 + 1, this.m_ops.getOp(addPos5 + opPlusLeftHandLen4 + 1) + opPlusLeftHandLen4);
                addPos = addPos5 + 2;
            }
        }
        return addPos;
    }

    protected void UnaryExpr() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        boolean isNeg = false;
        if (this.m_tokenChar == '-') {
            nextToken();
            appendOp(2, 16);
            isNeg = true;
        }
        UnionExpr();
        if (isNeg) {
            this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
        }
    }

    protected void StringExpr() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        appendOp(2, 17);
        Expr();
        this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
    }

    protected void BooleanExpr() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        appendOp(2, 18);
        Expr();
        int opLen = this.m_ops.getOp(1) - opPos;
        if (opLen == 2) {
            error("ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL", null);
        }
        this.m_ops.setOp(opPos + 1, opLen);
    }

    protected void NumberExpr() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        appendOp(2, 19);
        Expr();
        this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
    }

    protected void UnionExpr() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        boolean foundUnion = false;
        do {
            PathExpr();
            if (!tokenIs('|')) {
                break;
            }
            if (false == foundUnion) {
                foundUnion = true;
                insertOp(opPos, 2, 20);
            }
            nextToken();
        } while (1 != 0);
        this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
    }

    protected void PathExpr() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        int filterExprMatch = FilterExpr();
        if (filterExprMatch != 0) {
            boolean locationPathStarted = filterExprMatch == 2;
            if (tokenIs('/')) {
                nextToken();
                if (!locationPathStarted) {
                    insertOp(opPos, 2, 28);
                    locationPathStarted = true;
                }
                if (!RelativeLocationPath()) {
                    error("ER_EXPECTED_REL_LOC_PATH", null);
                }
            }
            if (locationPathStarted) {
                this.m_ops.setOp(this.m_ops.getOp(1), -1);
                this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
                this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
                return;
            }
            return;
        }
        LocationPath();
    }

    protected int FilterExpr() throws TransformerException {
        int filterMatch;
        int opPos = this.m_ops.getOp(1);
        if (PrimaryExpr()) {
            if (tokenIs('[')) {
                insertOp(opPos, 2, 28);
                while (tokenIs('[')) {
                    Predicate();
                }
                filterMatch = 2;
            } else {
                filterMatch = 1;
            }
        } else {
            filterMatch = 0;
        }
        return filterMatch;
    }

    protected boolean PrimaryExpr() throws TransformerException {
        boolean matchFound;
        int opPos = this.m_ops.getOp(1);
        if (this.m_tokenChar == '\'' || this.m_tokenChar == '\"') {
            appendOp(2, 21);
            Literal();
            this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
            matchFound = true;
        } else if (this.m_tokenChar == '$') {
            nextToken();
            appendOp(2, 22);
            QName();
            this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
            matchFound = true;
        } else if (this.m_tokenChar == '(') {
            nextToken();
            appendOp(2, 23);
            Expr();
            consumeExpected(')');
            this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
            matchFound = true;
        } else if (null != this.m_token && (('.' == this.m_tokenChar && this.m_token.length() > 1 && Character.isDigit(this.m_token.charAt(1))) || Character.isDigit(this.m_tokenChar))) {
            appendOp(2, 27);
            Number();
            this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
            matchFound = true;
        } else if (lookahead('(', 1) || (lookahead(':', 1) && lookahead('(', 3))) {
            matchFound = FunctionCall();
        } else {
            matchFound = false;
        }
        return matchFound;
    }

    protected void Argument() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        appendOp(2, 26);
        Expr();
        this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
    }

    protected boolean FunctionCall() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        if (lookahead(':', 1)) {
            appendOp(4, 24);
            this.m_ops.setOp(opPos + 1 + 1, this.m_queueMark - 1);
            nextToken();
            consumeExpected(':');
            this.m_ops.setOp(opPos + 1 + 2, this.m_queueMark - 1);
            nextToken();
        } else {
            int funcTok = getFunctionToken(this.m_token);
            if (-1 == funcTok) {
                error("ER_COULDNOT_FIND_FUNCTION", new Object[]{this.m_token});
            }
            switch (funcTok) {
                case OpCodes.NODETYPE_COMMENT /* 1030 */:
                case OpCodes.NODETYPE_TEXT /* 1031 */:
                case OpCodes.NODETYPE_PI /* 1032 */:
                case 1033:
                    return false;
                default:
                    appendOp(3, 25);
                    this.m_ops.setOp(opPos + 1 + 1, funcTok);
                    nextToken();
                    break;
            }
        }
        consumeExpected('(');
        while (!tokenIs(')') && this.m_token != null) {
            if (tokenIs(',')) {
                error("ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG", null);
            }
            Argument();
            if (!tokenIs(')')) {
                consumeExpected(',');
                if (tokenIs(')')) {
                    error("ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG", null);
                }
            }
        }
        consumeExpected(')');
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
        return true;
    }

    protected void LocationPath() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        appendOp(2, 28);
        boolean seenSlash = tokenIs('/');
        if (seenSlash) {
            appendOp(4, 50);
            this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
            this.m_ops.setOp(this.m_ops.getOp(1) - 1, 35);
            nextToken();
        } else if (this.m_token == null) {
            error("ER_EXPECTED_LOC_PATH_AT_END_EXPR", null);
        }
        if (this.m_token != null && !RelativeLocationPath() && !seenSlash) {
            error("ER_EXPECTED_LOC_PATH", new Object[]{this.m_token});
        }
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
    }

    protected boolean RelativeLocationPath() throws TransformerException {
        if (!Step()) {
            return false;
        }
        while (tokenIs('/')) {
            nextToken();
            if (!Step()) {
                error("ER_EXPECTED_LOC_STEP", null);
            }
        }
        return true;
    }

    protected boolean Step() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        boolean doubleSlash = tokenIs('/');
        if (doubleSlash) {
            nextToken();
            appendOp(2, 42);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            this.m_ops.setOp(this.m_ops.getOp(1), 1033);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            this.m_ops.setOp(opPos + 1 + 1, this.m_ops.getOp(1) - opPos);
            this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
            opPos = this.m_ops.getOp(1);
        }
        if (tokenIs(".")) {
            nextToken();
            if (tokenIs('[')) {
                error("ER_PREDICATE_ILLEGAL_SYNTAX", null);
            }
            appendOp(4, 48);
            this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
            this.m_ops.setOp(this.m_ops.getOp(1) - 1, 1033);
            return true;
        }
        if (tokenIs(Constants.ATTRVAL_PARENT)) {
            nextToken();
            appendOp(4, 45);
            this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
            this.m_ops.setOp(this.m_ops.getOp(1) - 1, 1033);
            return true;
        }
        if (tokenIs('*') || tokenIs('@') || tokenIs('_') || (this.m_token != null && Character.isLetter(this.m_token.charAt(0)))) {
            Basis();
            while (tokenIs('[')) {
                Predicate();
            }
            this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
            return true;
        }
        if (doubleSlash) {
            error("ER_EXPECTED_LOC_STEP", null);
            return false;
        }
        return false;
    }

    protected void Basis() throws TransformerException {
        int axesType;
        int opPos = this.m_ops.getOp(1);
        if (lookahead("::", 1)) {
            axesType = AxisName();
            nextToken();
            nextToken();
        } else if (tokenIs('@')) {
            axesType = 39;
            appendOp(2, 39);
            nextToken();
        } else {
            axesType = 40;
            appendOp(2, 40);
        }
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        NodeTest(axesType);
        this.m_ops.setOp(opPos + 1 + 1, this.m_ops.getOp(1) - opPos);
    }

    protected int AxisName() throws TransformerException {
        Object val = Keywords.getAxisName(this.m_token);
        if (null == val) {
            error("ER_ILLEGAL_AXIS_NAME", new Object[]{this.m_token});
        }
        int axesType = ((Integer) val).intValue();
        appendOp(2, axesType);
        return axesType;
    }

    protected void NodeTest(int axesType) throws TransformerException {
        if (lookahead('(', 1)) {
            Object nodeTestOp = Keywords.getNodeType(this.m_token);
            if (null == nodeTestOp) {
                error("ER_UNKNOWN_NODETYPE", new Object[]{this.m_token});
                return;
            }
            nextToken();
            int nt = ((Integer) nodeTestOp).intValue();
            this.m_ops.setOp(this.m_ops.getOp(1), nt);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            consumeExpected('(');
            if (1032 == nt && !tokenIs(')')) {
                Literal();
            }
            consumeExpected(')');
            return;
        }
        this.m_ops.setOp(this.m_ops.getOp(1), 34);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        if (lookahead(':', 1)) {
            if (tokenIs('*')) {
                this.m_ops.setOp(this.m_ops.getOp(1), -3);
            } else {
                this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
                if (!Character.isLetter(this.m_tokenChar) && !tokenIs('_')) {
                    error("ER_EXPECTED_NODE_TEST", null);
                }
            }
            nextToken();
            consumeExpected(':');
        } else {
            this.m_ops.setOp(this.m_ops.getOp(1), -2);
        }
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        if (tokenIs('*')) {
            this.m_ops.setOp(this.m_ops.getOp(1), -3);
        } else {
            this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
            if (!Character.isLetter(this.m_tokenChar) && !tokenIs('_')) {
                error("ER_EXPECTED_NODE_TEST", null);
            }
        }
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        nextToken();
    }

    protected void Predicate() throws TransformerException {
        if (tokenIs('[')) {
            this.countPredicate++;
            nextToken();
            PredicateExpr();
            this.countPredicate--;
            consumeExpected(']');
        }
    }

    protected void PredicateExpr() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        appendOp(2, 29);
        Expr();
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
    }

    protected void QName() throws TransformerException {
        if (lookahead(':', 1)) {
            this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            nextToken();
            consumeExpected(':');
        } else {
            this.m_ops.setOp(this.m_ops.getOp(1), -2);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        }
        this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        nextToken();
    }

    protected void NCName() {
        this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        nextToken();
    }

    protected void Literal() throws TransformerException {
        int last = this.m_token.length() - 1;
        char c0 = this.m_tokenChar;
        char cX = this.m_token.charAt(last);
        if ((c0 == '\"' && cX == '\"') || (c0 == '\'' && cX == '\'')) {
            int tokenQueuePos = this.m_queueMark - 1;
            this.m_ops.m_tokenQueue.setElementAt(null, tokenQueuePos);
            Object obj = new XString(this.m_token.substring(1, last));
            this.m_ops.m_tokenQueue.setElementAt(obj, tokenQueuePos);
            this.m_ops.setOp(this.m_ops.getOp(1), tokenQueuePos);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            nextToken();
            return;
        }
        error("ER_PATTERN_LITERAL_NEEDS_BE_QUOTED", new Object[]{this.m_token});
    }

    protected void Number() throws TransformerException {
        double num;
        if (null != this.m_token) {
            try {
            } catch (NumberFormatException e2) {
                num = 0.0d;
                error("ER_COULDNOT_BE_FORMATTED_TO_NUMBER", new Object[]{this.m_token});
            }
            if (this.m_token.indexOf(101) > -1 || this.m_token.indexOf(69) > -1) {
                throw new NumberFormatException();
            }
            num = Double.valueOf(this.m_token).doubleValue();
            this.m_ops.m_tokenQueue.setElementAt(new XNumber(num), this.m_queueMark - 1);
            this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            nextToken();
        }
    }

    protected void Pattern() throws TransformerException {
        while (true) {
            LocationPathPattern();
            if (tokenIs('|')) {
                nextToken();
            } else {
                return;
            }
        }
    }

    protected void LocationPathPattern() throws TransformerException {
        int opPos = this.m_ops.getOp(1);
        int relativePathStatus = 0;
        appendOp(2, 31);
        if (lookahead('(', 1) && (tokenIs("id") || tokenIs("key"))) {
            IdKeyPattern();
            if (tokenIs('/')) {
                nextToken();
                if (tokenIs('/')) {
                    appendOp(4, 52);
                    nextToken();
                } else {
                    appendOp(4, 53);
                }
                this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
                this.m_ops.setOp(this.m_ops.getOp(1) - 1, OpCodes.NODETYPE_FUNCTEST);
                relativePathStatus = 2;
            }
        } else if (tokenIs('/')) {
            if (lookahead('/', 1)) {
                appendOp(4, 52);
                nextToken();
                relativePathStatus = 2;
            } else {
                appendOp(4, 50);
                relativePathStatus = 1;
            }
            this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
            this.m_ops.setOp(this.m_ops.getOp(1) - 1, 35);
            nextToken();
        } else {
            relativePathStatus = 2;
        }
        if (relativePathStatus != 0) {
            if (!tokenIs('|') && null != this.m_token) {
                RelativePathPattern();
            } else if (relativePathStatus == 2) {
                error("ER_EXPECTED_REL_PATH_PATTERN", null);
            }
        }
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
    }

    protected void IdKeyPattern() throws TransformerException {
        FunctionCall();
    }

    protected void RelativePathPattern() throws TransformerException {
        boolean zStepPattern = StepPattern(false);
        while (true) {
            boolean trailingSlashConsumed = zStepPattern;
            if (tokenIs('/')) {
                nextToken();
                zStepPattern = StepPattern(!trailingSlashConsumed);
            } else {
                return;
            }
        }
    }

    protected boolean StepPattern(boolean isLeadingSlashPermitted) throws TransformerException {
        return AbbreviatedNodeTestStep(isLeadingSlashPermitted);
    }

    protected boolean AbbreviatedNodeTestStep(boolean isLeadingSlashPermitted) throws TransformerException {
        int axesType;
        boolean trailingSlashConsumed;
        int opPos = this.m_ops.getOp(1);
        int matchTypePos = -1;
        if (tokenIs('@')) {
            axesType = 51;
            appendOp(2, 51);
            nextToken();
        } else if (lookahead("::", 1)) {
            if (tokenIs("attribute")) {
                axesType = 51;
                appendOp(2, 51);
            } else if (tokenIs("child")) {
                matchTypePos = this.m_ops.getOp(1);
                axesType = 53;
                appendOp(2, 53);
            } else {
                axesType = -1;
                error("ER_AXES_NOT_ALLOWED", new Object[]{this.m_token});
            }
            nextToken();
            nextToken();
        } else if (tokenIs('/')) {
            if (!isLeadingSlashPermitted) {
                error("ER_EXPECTED_STEP_PATTERN", null);
            }
            axesType = 52;
            appendOp(2, 52);
            nextToken();
        } else {
            matchTypePos = this.m_ops.getOp(1);
            axesType = 53;
            appendOp(2, 53);
        }
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        NodeTest(axesType);
        this.m_ops.setOp(opPos + 1 + 1, this.m_ops.getOp(1) - opPos);
        while (tokenIs('[')) {
            Predicate();
        }
        if (matchTypePos > -1 && tokenIs('/') && lookahead('/', 1)) {
            this.m_ops.setOp(matchTypePos, 52);
            nextToken();
            trailingSlashConsumed = true;
        } else {
            trailingSlashConsumed = false;
        }
        this.m_ops.setOp(opPos + 1, this.m_ops.getOp(1) - opPos);
        return trailingSlashConsumed;
    }
}
