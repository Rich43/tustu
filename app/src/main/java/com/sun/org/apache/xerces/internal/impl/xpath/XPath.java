package com.sun.org.apache.xerces.internal.impl.xpath;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/XPath.class */
public class XPath {
    private static final boolean DEBUG_ALL = false;
    private static final boolean DEBUG_XPATH_PARSE = false;
    private static final boolean DEBUG_ANY = false;
    protected String fExpression;
    protected SymbolTable fSymbolTable;
    protected LocationPath[] fLocationPaths;

    public XPath(String xpath, SymbolTable symbolTable, NamespaceContext context) throws XPathException {
        this.fExpression = xpath;
        this.fSymbolTable = symbolTable;
        parseExpression(context);
    }

    public LocationPath[] getLocationPaths() {
        LocationPath[] ret = new LocationPath[this.fLocationPaths.length];
        for (int i2 = 0; i2 < this.fLocationPaths.length; i2++) {
            ret[i2] = (LocationPath) this.fLocationPaths[i2].clone();
        }
        return ret;
    }

    public LocationPath getLocationPath() {
        return (LocationPath) this.fLocationPaths[0].clone();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < this.fLocationPaths.length; i2++) {
            if (i2 > 0) {
                buf.append(CallSiteDescriptor.OPERATOR_DELIMITER);
            }
            buf.append(this.fLocationPaths[i2].toString());
        }
        return buf.toString();
    }

    private static void check(boolean b2) throws XPathException {
        if (!b2) {
            throw new XPathException("c-general-xpath");
        }
    }

    private LocationPath buildLocationPath(Vector stepsVector) throws XPathException {
        int size = stepsVector.size();
        check(size != 0);
        Step[] steps = new Step[size];
        stepsVector.copyInto(steps);
        stepsVector.removeAllElements();
        return new LocationPath(steps);
    }

    private void parseExpression(NamespaceContext context) throws XPathException {
        Tokens xtokens = new Tokens(this.fSymbolTable);
        Scanner scanner = new Scanner(this.fSymbolTable) { // from class: com.sun.org.apache.xerces.internal.impl.xpath.XPath.1
            @Override // com.sun.org.apache.xerces.internal.impl.xpath.XPath.Scanner
            protected void addToken(Tokens tokens, int token) throws XPathException {
                if (token == 6 || token == 35 || token == 11 || token == 21 || token == 4 || token == 9 || token == 10 || token == 22 || token == 23 || token == 36 || token == 8) {
                    super.addToken(tokens, token);
                    return;
                }
                throw new XPathException("c-general-xpath");
            }
        };
        int length = this.fExpression.length();
        boolean success = scanner.scanExpr(this.fSymbolTable, xtokens, this.fExpression, 0, length);
        if (!success) {
            throw new XPathException("c-general-xpath");
        }
        Vector stepsVector = new Vector();
        Vector locationPathsVector = new Vector();
        boolean expectingStep = true;
        boolean expectingDoubleColon = false;
        while (xtokens.hasMore()) {
            int token = xtokens.nextToken();
            switch (token) {
                case 4:
                    check(expectingStep);
                    expectingStep = false;
                    if (stepsVector.size() != 0) {
                        break;
                    } else {
                        Axis axis = new Axis((short) 3);
                        NodeTest nodeTest = new NodeTest((short) 3);
                        Step step = new Step(axis, nodeTest);
                        stepsVector.addElement(step);
                        if (!xtokens.hasMore() || xtokens.peekToken() != 22) {
                            break;
                        } else {
                            xtokens.nextToken();
                            Axis axis2 = new Axis((short) 4);
                            NodeTest nodeTest2 = new NodeTest((short) 3);
                            Step step2 = new Step(axis2, nodeTest2);
                            stepsVector.addElement(step2);
                            expectingStep = true;
                            break;
                        }
                    }
                case 5:
                case 7:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                default:
                    throw new XPathException("c-general-xpath");
                case 6:
                    check(expectingStep);
                    Step step3 = new Step(new Axis((short) 2), parseNodeTest(xtokens.nextToken(), xtokens, context));
                    stepsVector.addElement(step3);
                    expectingStep = false;
                    break;
                case 8:
                    check(expectingStep);
                    check(expectingDoubleColon);
                    expectingDoubleColon = false;
                    break;
                case 9:
                case 10:
                case 11:
                    check(expectingStep);
                    Step step4 = new Step(new Axis((short) 1), parseNodeTest(token, xtokens, context));
                    stepsVector.addElement(step4);
                    expectingStep = false;
                    break;
                case 21:
                    check(!expectingStep);
                    expectingStep = true;
                    break;
                case 22:
                    throw new XPathException("c-general-xpath");
                case 23:
                    check(!expectingStep);
                    locationPathsVector.addElement(buildLocationPath(stepsVector));
                    expectingStep = true;
                    break;
                case 35:
                    check(expectingStep);
                    expectingDoubleColon = true;
                    if (xtokens.nextToken() != 8) {
                        break;
                    } else {
                        Step step5 = new Step(new Axis((short) 2), parseNodeTest(xtokens.nextToken(), xtokens, context));
                        stepsVector.addElement(step5);
                        expectingStep = false;
                        expectingDoubleColon = false;
                        break;
                    }
                case 36:
                    check(expectingStep);
                    expectingDoubleColon = true;
                    break;
            }
        }
        check(!expectingStep);
        locationPathsVector.addElement(buildLocationPath(stepsVector));
        this.fLocationPaths = new LocationPath[locationPathsVector.size()];
        locationPathsVector.copyInto(this.fLocationPaths);
    }

    private NodeTest parseNodeTest(int typeToken, Tokens xtokens, NamespaceContext context) throws XPathException {
        switch (typeToken) {
            case 9:
                return new NodeTest((short) 2);
            case 10:
            case 11:
                String prefix = xtokens.nextTokenAsString();
                String uri = null;
                if (context != null && prefix != XMLSymbols.EMPTY_STRING) {
                    uri = context.getURI(prefix);
                }
                if (prefix != XMLSymbols.EMPTY_STRING && context != null && uri == null) {
                    throw new XPathException("c-general-xpath-ns");
                }
                if (typeToken == 10) {
                    return new NodeTest(prefix, uri);
                }
                String localpart = xtokens.nextTokenAsString();
                String rawname = prefix != XMLSymbols.EMPTY_STRING ? this.fSymbolTable.addSymbol(prefix + ':' + localpart) : localpart;
                return new NodeTest(new QName(prefix, localpart, rawname, uri));
            default:
                throw new XPathException("c-general-xpath");
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/XPath$LocationPath.class */
    public static class LocationPath implements Cloneable {
        public Step[] steps;

        public LocationPath(Step[] steps) {
            this.steps = steps;
        }

        protected LocationPath(LocationPath path) {
            this.steps = new Step[path.steps.length];
            for (int i2 = 0; i2 < this.steps.length; i2++) {
                this.steps[i2] = (Step) path.steps[i2].clone();
            }
        }

        public String toString() {
            StringBuffer str = new StringBuffer();
            for (int i2 = 0; i2 < this.steps.length; i2++) {
                if (i2 > 0 && this.steps[i2 - 1].axis.type != 4 && this.steps[i2].axis.type != 4) {
                    str.append('/');
                }
                str.append(this.steps[i2].toString());
            }
            return str.toString();
        }

        public Object clone() {
            return new LocationPath(this);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/XPath$Step.class */
    public static class Step implements Cloneable {
        public Axis axis;
        public NodeTest nodeTest;

        public Step(Axis axis, NodeTest nodeTest) {
            this.axis = axis;
            this.nodeTest = nodeTest;
        }

        protected Step(Step step) {
            this.axis = (Axis) step.axis.clone();
            this.nodeTest = (NodeTest) step.nodeTest.clone();
        }

        public String toString() {
            if (this.axis.type == 3) {
                return ".";
            }
            if (this.axis.type == 2) {
                return "@" + this.nodeTest.toString();
            }
            if (this.axis.type == 1) {
                return this.nodeTest.toString();
            }
            if (this.axis.type == 4) {
                return "//";
            }
            return "??? (" + ((int) this.axis.type) + ')';
        }

        public Object clone() {
            return new Step(this);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/XPath$Axis.class */
    public static class Axis implements Cloneable {
        public static final short CHILD = 1;
        public static final short ATTRIBUTE = 2;
        public static final short SELF = 3;
        public static final short DESCENDANT = 4;
        public short type;

        public Axis(short type) {
            this.type = type;
        }

        protected Axis(Axis axis) {
            this.type = axis.type;
        }

        public String toString() {
            switch (this.type) {
                case 1:
                    return "child";
                case 2:
                    return "attribute";
                case 3:
                    return "self";
                case 4:
                    return "descendant";
                default:
                    return "???";
            }
        }

        public Object clone() {
            return new Axis(this);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/XPath$NodeTest.class */
    public static class NodeTest implements Cloneable {
        public static final short QNAME = 1;
        public static final short WILDCARD = 2;
        public static final short NODE = 3;
        public static final short NAMESPACE = 4;
        public short type;
        public final QName name;

        public NodeTest(short type) {
            this.name = new QName();
            this.type = type;
        }

        public NodeTest(QName name) {
            this.name = new QName();
            this.type = (short) 1;
            this.name.setValues(name);
        }

        public NodeTest(String prefix, String uri) {
            this.name = new QName();
            this.type = (short) 4;
            this.name.setValues(prefix, null, null, uri);
        }

        public NodeTest(NodeTest nodeTest) {
            this.name = new QName();
            this.type = nodeTest.type;
            this.name.setValues(nodeTest.name);
        }

        public String toString() {
            switch (this.type) {
                case 1:
                    if (this.name.prefix.length() != 0) {
                        if (this.name.uri != null) {
                            return this.name.prefix + ':' + this.name.localpart;
                        }
                        return VectorFormat.DEFAULT_PREFIX + this.name.uri + '}' + this.name.prefix + ':' + this.name.localpart;
                    }
                    return this.name.localpart;
                case 2:
                    return "*";
                case 3:
                    return "node()";
                case 4:
                    if (this.name.prefix.length() != 0) {
                        if (this.name.uri != null) {
                            return this.name.prefix + ":*";
                        }
                        return VectorFormat.DEFAULT_PREFIX + this.name.uri + '}' + this.name.prefix + ":*";
                    }
                    return "???:*";
                default:
                    return "???";
            }
        }

        public Object clone() {
            return new NodeTest(this);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/XPath$Tokens.class */
    private static final class Tokens {
        static final boolean DUMP_TOKENS = false;
        public static final int EXPRTOKEN_OPEN_PAREN = 0;
        public static final int EXPRTOKEN_CLOSE_PAREN = 1;
        public static final int EXPRTOKEN_OPEN_BRACKET = 2;
        public static final int EXPRTOKEN_CLOSE_BRACKET = 3;
        public static final int EXPRTOKEN_PERIOD = 4;
        public static final int EXPRTOKEN_DOUBLE_PERIOD = 5;
        public static final int EXPRTOKEN_ATSIGN = 6;
        public static final int EXPRTOKEN_COMMA = 7;
        public static final int EXPRTOKEN_DOUBLE_COLON = 8;
        public static final int EXPRTOKEN_NAMETEST_ANY = 9;
        public static final int EXPRTOKEN_NAMETEST_NAMESPACE = 10;
        public static final int EXPRTOKEN_NAMETEST_QNAME = 11;
        public static final int EXPRTOKEN_NODETYPE_COMMENT = 12;
        public static final int EXPRTOKEN_NODETYPE_TEXT = 13;
        public static final int EXPRTOKEN_NODETYPE_PI = 14;
        public static final int EXPRTOKEN_NODETYPE_NODE = 15;
        public static final int EXPRTOKEN_OPERATOR_AND = 16;
        public static final int EXPRTOKEN_OPERATOR_OR = 17;
        public static final int EXPRTOKEN_OPERATOR_MOD = 18;
        public static final int EXPRTOKEN_OPERATOR_DIV = 19;
        public static final int EXPRTOKEN_OPERATOR_MULT = 20;
        public static final int EXPRTOKEN_OPERATOR_SLASH = 21;
        public static final int EXPRTOKEN_OPERATOR_DOUBLE_SLASH = 22;
        public static final int EXPRTOKEN_OPERATOR_UNION = 23;
        public static final int EXPRTOKEN_OPERATOR_PLUS = 24;
        public static final int EXPRTOKEN_OPERATOR_MINUS = 25;
        public static final int EXPRTOKEN_OPERATOR_EQUAL = 26;
        public static final int EXPRTOKEN_OPERATOR_NOT_EQUAL = 27;
        public static final int EXPRTOKEN_OPERATOR_LESS = 28;
        public static final int EXPRTOKEN_OPERATOR_LESS_EQUAL = 29;
        public static final int EXPRTOKEN_OPERATOR_GREATER = 30;
        public static final int EXPRTOKEN_OPERATOR_GREATER_EQUAL = 31;
        public static final int EXPRTOKEN_FUNCTION_NAME = 32;
        public static final int EXPRTOKEN_AXISNAME_ANCESTOR = 33;
        public static final int EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF = 34;
        public static final int EXPRTOKEN_AXISNAME_ATTRIBUTE = 35;
        public static final int EXPRTOKEN_AXISNAME_CHILD = 36;
        public static final int EXPRTOKEN_AXISNAME_DESCENDANT = 37;
        public static final int EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF = 38;
        public static final int EXPRTOKEN_AXISNAME_FOLLOWING = 39;
        public static final int EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING = 40;
        public static final int EXPRTOKEN_AXISNAME_NAMESPACE = 41;
        public static final int EXPRTOKEN_AXISNAME_PARENT = 42;
        public static final int EXPRTOKEN_AXISNAME_PRECEDING = 43;
        public static final int EXPRTOKEN_AXISNAME_PRECEDING_SIBLING = 44;
        public static final int EXPRTOKEN_AXISNAME_SELF = 45;
        public static final int EXPRTOKEN_LITERAL = 46;
        public static final int EXPRTOKEN_NUMBER = 47;
        public static final int EXPRTOKEN_VARIABLE_REFERENCE = 48;
        private static final String[] fgTokenNames = {"EXPRTOKEN_OPEN_PAREN", "EXPRTOKEN_CLOSE_PAREN", "EXPRTOKEN_OPEN_BRACKET", "EXPRTOKEN_CLOSE_BRACKET", "EXPRTOKEN_PERIOD", "EXPRTOKEN_DOUBLE_PERIOD", "EXPRTOKEN_ATSIGN", "EXPRTOKEN_COMMA", "EXPRTOKEN_DOUBLE_COLON", "EXPRTOKEN_NAMETEST_ANY", "EXPRTOKEN_NAMETEST_NAMESPACE", "EXPRTOKEN_NAMETEST_QNAME", "EXPRTOKEN_NODETYPE_COMMENT", "EXPRTOKEN_NODETYPE_TEXT", "EXPRTOKEN_NODETYPE_PI", "EXPRTOKEN_NODETYPE_NODE", "EXPRTOKEN_OPERATOR_AND", "EXPRTOKEN_OPERATOR_OR", "EXPRTOKEN_OPERATOR_MOD", "EXPRTOKEN_OPERATOR_DIV", "EXPRTOKEN_OPERATOR_MULT", "EXPRTOKEN_OPERATOR_SLASH", "EXPRTOKEN_OPERATOR_DOUBLE_SLASH", "EXPRTOKEN_OPERATOR_UNION", "EXPRTOKEN_OPERATOR_PLUS", "EXPRTOKEN_OPERATOR_MINUS", "EXPRTOKEN_OPERATOR_EQUAL", "EXPRTOKEN_OPERATOR_NOT_EQUAL", "EXPRTOKEN_OPERATOR_LESS", "EXPRTOKEN_OPERATOR_LESS_EQUAL", "EXPRTOKEN_OPERATOR_GREATER", "EXPRTOKEN_OPERATOR_GREATER_EQUAL", "EXPRTOKEN_FUNCTION_NAME", "EXPRTOKEN_AXISNAME_ANCESTOR", "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF", "EXPRTOKEN_AXISNAME_ATTRIBUTE", "EXPRTOKEN_AXISNAME_CHILD", "EXPRTOKEN_AXISNAME_DESCENDANT", "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF", "EXPRTOKEN_AXISNAME_FOLLOWING", "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING", "EXPRTOKEN_AXISNAME_NAMESPACE", "EXPRTOKEN_AXISNAME_PARENT", "EXPRTOKEN_AXISNAME_PRECEDING", "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING", "EXPRTOKEN_AXISNAME_SELF", "EXPRTOKEN_LITERAL", "EXPRTOKEN_NUMBER", "EXPRTOKEN_VARIABLE_REFERENCE"};
        private static final int INITIAL_TOKEN_COUNT = 256;
        private SymbolTable fSymbolTable;
        private int fCurrentTokenIndex;
        private int[] fTokens = new int[256];
        private int fTokenCount = 0;
        private Map<String, Integer> fSymbolMapping = new HashMap();
        private Map<Integer, String> fTokenNames = new HashMap();

        public Tokens(SymbolTable symbolTable) {
            this.fSymbolTable = symbolTable;
            String[] symbols = {"ancestor", "ancestor-or-self", "attribute", "child", "descendant", "descendant-or-self", "following", "following-sibling", Constants.ATTRNAME_NAMESPACE, "parent", "preceding", "preceding-sibling", "self"};
            for (int i2 = 0; i2 < symbols.length; i2++) {
                this.fSymbolMapping.put(this.fSymbolTable.addSymbol(symbols[i2]), Integer.valueOf(i2));
            }
            this.fTokenNames.put(0, "EXPRTOKEN_OPEN_PAREN");
            this.fTokenNames.put(1, "EXPRTOKEN_CLOSE_PAREN");
            this.fTokenNames.put(2, "EXPRTOKEN_OPEN_BRACKET");
            this.fTokenNames.put(3, "EXPRTOKEN_CLOSE_BRACKET");
            this.fTokenNames.put(4, "EXPRTOKEN_PERIOD");
            this.fTokenNames.put(5, "EXPRTOKEN_DOUBLE_PERIOD");
            this.fTokenNames.put(6, "EXPRTOKEN_ATSIGN");
            this.fTokenNames.put(7, "EXPRTOKEN_COMMA");
            this.fTokenNames.put(8, "EXPRTOKEN_DOUBLE_COLON");
            this.fTokenNames.put(9, "EXPRTOKEN_NAMETEST_ANY");
            this.fTokenNames.put(10, "EXPRTOKEN_NAMETEST_NAMESPACE");
            this.fTokenNames.put(11, "EXPRTOKEN_NAMETEST_QNAME");
            this.fTokenNames.put(12, "EXPRTOKEN_NODETYPE_COMMENT");
            this.fTokenNames.put(13, "EXPRTOKEN_NODETYPE_TEXT");
            this.fTokenNames.put(14, "EXPRTOKEN_NODETYPE_PI");
            this.fTokenNames.put(15, "EXPRTOKEN_NODETYPE_NODE");
            this.fTokenNames.put(16, "EXPRTOKEN_OPERATOR_AND");
            this.fTokenNames.put(17, "EXPRTOKEN_OPERATOR_OR");
            this.fTokenNames.put(18, "EXPRTOKEN_OPERATOR_MOD");
            this.fTokenNames.put(19, "EXPRTOKEN_OPERATOR_DIV");
            this.fTokenNames.put(20, "EXPRTOKEN_OPERATOR_MULT");
            this.fTokenNames.put(21, "EXPRTOKEN_OPERATOR_SLASH");
            this.fTokenNames.put(22, "EXPRTOKEN_OPERATOR_DOUBLE_SLASH");
            this.fTokenNames.put(23, "EXPRTOKEN_OPERATOR_UNION");
            this.fTokenNames.put(24, "EXPRTOKEN_OPERATOR_PLUS");
            this.fTokenNames.put(25, "EXPRTOKEN_OPERATOR_MINUS");
            this.fTokenNames.put(26, "EXPRTOKEN_OPERATOR_EQUAL");
            this.fTokenNames.put(27, "EXPRTOKEN_OPERATOR_NOT_EQUAL");
            this.fTokenNames.put(28, "EXPRTOKEN_OPERATOR_LESS");
            this.fTokenNames.put(29, "EXPRTOKEN_OPERATOR_LESS_EQUAL");
            this.fTokenNames.put(30, "EXPRTOKEN_OPERATOR_GREATER");
            this.fTokenNames.put(31, "EXPRTOKEN_OPERATOR_GREATER_EQUAL");
            this.fTokenNames.put(32, "EXPRTOKEN_FUNCTION_NAME");
            this.fTokenNames.put(33, "EXPRTOKEN_AXISNAME_ANCESTOR");
            this.fTokenNames.put(34, "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF");
            this.fTokenNames.put(35, "EXPRTOKEN_AXISNAME_ATTRIBUTE");
            this.fTokenNames.put(36, "EXPRTOKEN_AXISNAME_CHILD");
            this.fTokenNames.put(37, "EXPRTOKEN_AXISNAME_DESCENDANT");
            this.fTokenNames.put(38, "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF");
            this.fTokenNames.put(39, "EXPRTOKEN_AXISNAME_FOLLOWING");
            this.fTokenNames.put(40, "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING");
            this.fTokenNames.put(41, "EXPRTOKEN_AXISNAME_NAMESPACE");
            this.fTokenNames.put(42, "EXPRTOKEN_AXISNAME_PARENT");
            this.fTokenNames.put(43, "EXPRTOKEN_AXISNAME_PRECEDING");
            this.fTokenNames.put(44, "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING");
            this.fTokenNames.put(45, "EXPRTOKEN_AXISNAME_SELF");
            this.fTokenNames.put(46, "EXPRTOKEN_LITERAL");
            this.fTokenNames.put(47, "EXPRTOKEN_NUMBER");
            this.fTokenNames.put(48, "EXPRTOKEN_VARIABLE_REFERENCE");
        }

        public String getTokenString(int token) {
            return this.fTokenNames.get(Integer.valueOf(token));
        }

        public void addToken(String tokenStr) {
            Integer tokenInt = null;
            for (Map.Entry<Integer, String> entry : this.fTokenNames.entrySet()) {
                if (entry.getValue().equals(tokenStr)) {
                    tokenInt = entry.getKey();
                }
            }
            if (tokenInt == null) {
                tokenInt = Integer.valueOf(this.fTokenNames.size());
                this.fTokenNames.put(tokenInt, tokenStr);
            }
            addToken(tokenInt.intValue());
        }

        public void addToken(int token) {
            try {
                this.fTokens[this.fTokenCount] = token;
            } catch (ArrayIndexOutOfBoundsException e2) {
                int[] oldList = this.fTokens;
                this.fTokens = new int[this.fTokenCount << 1];
                System.arraycopy(oldList, 0, this.fTokens, 0, this.fTokenCount);
                this.fTokens[this.fTokenCount] = token;
            }
            this.fTokenCount++;
        }

        public void rewind() {
            this.fCurrentTokenIndex = 0;
        }

        public boolean hasMore() {
            return this.fCurrentTokenIndex < this.fTokenCount;
        }

        public int nextToken() throws XPathException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                throw new XPathException("c-general-xpath");
            }
            int[] iArr = this.fTokens;
            int i2 = this.fCurrentTokenIndex;
            this.fCurrentTokenIndex = i2 + 1;
            return iArr[i2];
        }

        public int peekToken() throws XPathException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                throw new XPathException("c-general-xpath");
            }
            return this.fTokens[this.fCurrentTokenIndex];
        }

        public String nextTokenAsString() throws XPathException {
            String s2 = getTokenString(nextToken());
            if (s2 == null) {
                throw new XPathException("c-general-xpath");
            }
            return s2;
        }

        public void dumpTokens() {
            int i2 = 0;
            while (i2 < this.fTokenCount) {
                switch (this.fTokens[i2]) {
                    case 0:
                        System.out.print("<OPEN_PAREN/>");
                        break;
                    case 1:
                        System.out.print("<CLOSE_PAREN/>");
                        break;
                    case 2:
                        System.out.print("<OPEN_BRACKET/>");
                        break;
                    case 3:
                        System.out.print("<CLOSE_BRACKET/>");
                        break;
                    case 4:
                        System.out.print("<PERIOD/>");
                        break;
                    case 5:
                        System.out.print("<DOUBLE_PERIOD/>");
                        break;
                    case 6:
                        System.out.print("<ATSIGN/>");
                        break;
                    case 7:
                        System.out.print("<COMMA/>");
                        break;
                    case 8:
                        System.out.print("<DOUBLE_COLON/>");
                        break;
                    case 9:
                        System.out.print("<NAMETEST_ANY/>");
                        break;
                    case 10:
                        System.out.print("<NAMETEST_NAMESPACE");
                        i2++;
                        System.out.print(" prefix=\"" + getTokenString(this.fTokens[i2]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        System.out.print("/>");
                        break;
                    case 11:
                        System.out.print("<NAMETEST_QNAME");
                        int i3 = i2 + 1;
                        if (this.fTokens[i3] != -1) {
                            System.out.print(" prefix=\"" + getTokenString(this.fTokens[i3]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        }
                        i2 = i3 + 1;
                        System.out.print(" localpart=\"" + getTokenString(this.fTokens[i2]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        System.out.print("/>");
                        break;
                    case 12:
                        System.out.print("<NODETYPE_COMMENT/>");
                        break;
                    case 13:
                        System.out.print("<NODETYPE_TEXT/>");
                        break;
                    case 14:
                        System.out.print("<NODETYPE_PI/>");
                        break;
                    case 15:
                        System.out.print("<NODETYPE_NODE/>");
                        break;
                    case 16:
                        System.out.print("<OPERATOR_AND/>");
                        break;
                    case 17:
                        System.out.print("<OPERATOR_OR/>");
                        break;
                    case 18:
                        System.out.print("<OPERATOR_MOD/>");
                        break;
                    case 19:
                        System.out.print("<OPERATOR_DIV/>");
                        break;
                    case 20:
                        System.out.print("<OPERATOR_MULT/>");
                        break;
                    case 21:
                        System.out.print("<OPERATOR_SLASH/>");
                        if (i2 + 1 >= this.fTokenCount) {
                            break;
                        } else {
                            System.out.println();
                            System.out.print(sun.security.pkcs11.wrapper.Constants.INDENT);
                            break;
                        }
                    case 22:
                        System.out.print("<OPERATOR_DOUBLE_SLASH/>");
                        break;
                    case 23:
                        System.out.print("<OPERATOR_UNION/>");
                        break;
                    case 24:
                        System.out.print("<OPERATOR_PLUS/>");
                        break;
                    case 25:
                        System.out.print("<OPERATOR_MINUS/>");
                        break;
                    case 26:
                        System.out.print("<OPERATOR_EQUAL/>");
                        break;
                    case 27:
                        System.out.print("<OPERATOR_NOT_EQUAL/>");
                        break;
                    case 28:
                        System.out.print("<OPERATOR_LESS/>");
                        break;
                    case 29:
                        System.out.print("<OPERATOR_LESS_EQUAL/>");
                        break;
                    case 30:
                        System.out.print("<OPERATOR_GREATER/>");
                        break;
                    case 31:
                        System.out.print("<OPERATOR_GREATER_EQUAL/>");
                        break;
                    case 32:
                        System.out.print("<FUNCTION_NAME");
                        int i4 = i2 + 1;
                        if (this.fTokens[i4] != -1) {
                            System.out.print(" prefix=\"" + getTokenString(this.fTokens[i4]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        }
                        i2 = i4 + 1;
                        System.out.print(" localpart=\"" + getTokenString(this.fTokens[i2]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        System.out.print("/>");
                        break;
                    case 33:
                        System.out.print("<AXISNAME_ANCESTOR/>");
                        break;
                    case 34:
                        System.out.print("<AXISNAME_ANCESTOR_OR_SELF/>");
                        break;
                    case 35:
                        System.out.print("<AXISNAME_ATTRIBUTE/>");
                        break;
                    case 36:
                        System.out.print("<AXISNAME_CHILD/>");
                        break;
                    case 37:
                        System.out.print("<AXISNAME_DESCENDANT/>");
                        break;
                    case 38:
                        System.out.print("<AXISNAME_DESCENDANT_OR_SELF/>");
                        break;
                    case 39:
                        System.out.print("<AXISNAME_FOLLOWING/>");
                        break;
                    case 40:
                        System.out.print("<AXISNAME_FOLLOWING_SIBLING/>");
                        break;
                    case 41:
                        System.out.print("<AXISNAME_NAMESPACE/>");
                        break;
                    case 42:
                        System.out.print("<AXISNAME_PARENT/>");
                        break;
                    case 43:
                        System.out.print("<AXISNAME_PRECEDING/>");
                        break;
                    case 44:
                        System.out.print("<AXISNAME_PRECEDING_SIBLING/>");
                        break;
                    case 45:
                        System.out.print("<AXISNAME_SELF/>");
                        break;
                    case 46:
                        System.out.print("<LITERAL");
                        i2++;
                        System.out.print(" value=\"" + getTokenString(this.fTokens[i2]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        System.out.print("/>");
                        break;
                    case 47:
                        System.out.print("<NUMBER");
                        int i5 = i2 + 1;
                        System.out.print(" whole=\"" + getTokenString(this.fTokens[i5]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        i2 = i5 + 1;
                        System.out.print(" part=\"" + getTokenString(this.fTokens[i2]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        System.out.print("/>");
                        break;
                    case 48:
                        System.out.print("<VARIABLE_REFERENCE");
                        int i6 = i2 + 1;
                        if (this.fTokens[i6] != -1) {
                            System.out.print(" prefix=\"" + getTokenString(this.fTokens[i6]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        }
                        i2 = i6 + 1;
                        System.out.print(" localpart=\"" + getTokenString(this.fTokens[i2]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                        System.out.print("/>");
                        break;
                    default:
                        System.out.println("<???/>");
                        break;
                }
                i2++;
            }
            System.out.println();
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/XPath$Scanner.class */
    private static class Scanner {
        private static final byte CHARTYPE_INVALID = 0;
        private static final byte CHARTYPE_OTHER = 1;
        private static final byte CHARTYPE_WHITESPACE = 2;
        private static final byte CHARTYPE_EXCLAMATION = 3;
        private static final byte CHARTYPE_QUOTE = 4;
        private static final byte CHARTYPE_DOLLAR = 5;
        private static final byte CHARTYPE_OPEN_PAREN = 6;
        private static final byte CHARTYPE_CLOSE_PAREN = 7;
        private static final byte CHARTYPE_STAR = 8;
        private static final byte CHARTYPE_PLUS = 9;
        private static final byte CHARTYPE_COMMA = 10;
        private static final byte CHARTYPE_MINUS = 11;
        private static final byte CHARTYPE_PERIOD = 12;
        private static final byte CHARTYPE_SLASH = 13;
        private static final byte CHARTYPE_DIGIT = 14;
        private static final byte CHARTYPE_COLON = 15;
        private static final byte CHARTYPE_LESS = 16;
        private static final byte CHARTYPE_EQUAL = 17;
        private static final byte CHARTYPE_GREATER = 18;
        private static final byte CHARTYPE_ATSIGN = 19;
        private static final byte CHARTYPE_LETTER = 20;
        private static final byte CHARTYPE_OPEN_BRACKET = 21;
        private static final byte CHARTYPE_CLOSE_BRACKET = 22;
        private static final byte CHARTYPE_UNDERSCORE = 23;
        private static final byte CHARTYPE_UNION = 24;
        private static final byte CHARTYPE_NONASCII = 25;
        private SymbolTable fSymbolTable;
        private static final byte[] fASCIICharMap = {0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 1, 5, 1, 1, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 1, 16, 17, 18, 1, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 1, 22, 1, 23, 1, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 1, 24, 1, 1, 1};
        private static final String fAndSymbol = "and".intern();
        private static final String fOrSymbol = "or".intern();
        private static final String fModSymbol = "mod".intern();
        private static final String fDivSymbol = "div".intern();
        private static final String fCommentSymbol = "comment".intern();
        private static final String fTextSymbol = "text".intern();
        private static final String fPISymbol = Constants.ELEMNAME_PI_STRING.intern();
        private static final String fNodeSymbol = "node".intern();
        private static final String fAncestorSymbol = "ancestor".intern();
        private static final String fAncestorOrSelfSymbol = "ancestor-or-self".intern();
        private static final String fAttributeSymbol = "attribute".intern();
        private static final String fChildSymbol = "child".intern();
        private static final String fDescendantSymbol = "descendant".intern();
        private static final String fDescendantOrSelfSymbol = "descendant-or-self".intern();
        private static final String fFollowingSymbol = "following".intern();
        private static final String fFollowingSiblingSymbol = "following-sibling".intern();
        private static final String fNamespaceSymbol = Constants.ATTRNAME_NAMESPACE.intern();
        private static final String fParentSymbol = "parent".intern();
        private static final String fPrecedingSymbol = "preceding".intern();
        private static final String fPrecedingSiblingSymbol = "preceding-sibling".intern();
        private static final String fSelfSymbol = "self".intern();

        public Scanner(SymbolTable symbolTable) {
            this.fSymbolTable = symbolTable;
        }

        public boolean scanExpr(SymbolTable symbolTable, Tokens tokens, String data, int currentOffset, int endOffset) throws XPathException {
            int ch;
            int ch2;
            int ch3;
            String prefixHandle;
            boolean starIsMultiplyOperator = false;
            while (currentOffset != endOffset) {
                int iCharAt = data.charAt(currentOffset);
                while (true) {
                    ch = iCharAt;
                    if (ch == 32 || ch == 10 || ch == 9 || ch == 13) {
                        currentOffset++;
                        if (currentOffset != endOffset) {
                            iCharAt = data.charAt(currentOffset);
                        }
                    }
                }
                if (currentOffset != endOffset) {
                    byte chartype = ch >= 128 ? (byte) 25 : fASCIICharMap[ch];
                    switch (chartype) {
                        case 3:
                            int currentOffset2 = currentOffset + 1;
                            if (currentOffset2 == endOffset || data.charAt(currentOffset2) != 61) {
                                return false;
                            }
                            addToken(tokens, 27);
                            starIsMultiplyOperator = false;
                            currentOffset = currentOffset2 + 1;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 4:
                            int currentOffset3 = currentOffset + 1;
                            if (currentOffset3 == endOffset) {
                                return false;
                            }
                            int ch4 = data.charAt(currentOffset3);
                            while (ch4 != ch) {
                                currentOffset3++;
                                if (currentOffset3 == endOffset) {
                                    return false;
                                }
                                ch4 = data.charAt(currentOffset3);
                            }
                            int litLength = currentOffset3 - currentOffset3;
                            addToken(tokens, 46);
                            starIsMultiplyOperator = true;
                            tokens.addToken(symbolTable.addSymbol(data.substring(currentOffset3, currentOffset3 + litLength)));
                            currentOffset = currentOffset3 + 1;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 5:
                            int currentOffset4 = currentOffset + 1;
                            if (currentOffset4 == endOffset) {
                                return false;
                            }
                            currentOffset = scanNCName(data, endOffset, currentOffset4);
                            if (currentOffset == currentOffset4) {
                                return false;
                            }
                            if (currentOffset < endOffset) {
                                ch3 = data.charAt(currentOffset);
                            } else {
                                ch3 = -1;
                            }
                            String nameHandle = symbolTable.addSymbol(data.substring(currentOffset4, currentOffset));
                            if (ch3 != 58) {
                                prefixHandle = XMLSymbols.EMPTY_STRING;
                            } else {
                                prefixHandle = nameHandle;
                                int currentOffset5 = currentOffset + 1;
                                if (currentOffset5 == endOffset) {
                                    return false;
                                }
                                currentOffset = scanNCName(data, endOffset, currentOffset5);
                                if (currentOffset == currentOffset5) {
                                    return false;
                                }
                                if (currentOffset < endOffset) {
                                    data.charAt(currentOffset);
                                }
                                nameHandle = symbolTable.addSymbol(data.substring(currentOffset5, currentOffset));
                            }
                            addToken(tokens, 48);
                            starIsMultiplyOperator = true;
                            tokens.addToken(prefixHandle);
                            tokens.addToken(nameHandle);
                            break;
                        case 6:
                            addToken(tokens, 0);
                            starIsMultiplyOperator = false;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 7:
                            addToken(tokens, 1);
                            starIsMultiplyOperator = true;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 8:
                            if (starIsMultiplyOperator) {
                                addToken(tokens, 20);
                                starIsMultiplyOperator = false;
                            } else {
                                addToken(tokens, 9);
                                starIsMultiplyOperator = true;
                            }
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 9:
                            addToken(tokens, 24);
                            starIsMultiplyOperator = false;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 10:
                            addToken(tokens, 7);
                            starIsMultiplyOperator = false;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 11:
                            addToken(tokens, 25);
                            starIsMultiplyOperator = false;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 12:
                            if (currentOffset + 1 == endOffset) {
                                addToken(tokens, 4);
                                starIsMultiplyOperator = true;
                                currentOffset++;
                                break;
                            } else {
                                int ch5 = data.charAt(currentOffset + 1);
                                if (ch5 == 46) {
                                    addToken(tokens, 5);
                                    starIsMultiplyOperator = true;
                                    currentOffset += 2;
                                } else if (ch5 >= 48 && ch5 <= 57) {
                                    addToken(tokens, 47);
                                    starIsMultiplyOperator = true;
                                    currentOffset = scanNumber(tokens, data, endOffset, currentOffset);
                                } else if (ch5 == 47) {
                                    addToken(tokens, 4);
                                    starIsMultiplyOperator = true;
                                    currentOffset++;
                                } else if (ch5 == 124) {
                                    addToken(tokens, 4);
                                    starIsMultiplyOperator = true;
                                    currentOffset++;
                                    break;
                                } else if (ch5 == 32 || ch5 == 10 || ch5 == 9 || ch5 == 13) {
                                    while (true) {
                                        currentOffset++;
                                        if (currentOffset != endOffset) {
                                            ch5 = data.charAt(currentOffset);
                                            if (ch5 == 32 || ch5 == 10 || ch5 == 9 || ch5 == 13) {
                                            }
                                        }
                                    }
                                    if (currentOffset == endOffset || ch5 == 124 || ch5 == 47) {
                                        addToken(tokens, 4);
                                        starIsMultiplyOperator = true;
                                        break;
                                    } else {
                                        throw new XPathException("c-general-xpath");
                                    }
                                } else {
                                    throw new XPathException("c-general-xpath");
                                }
                                if (currentOffset == endOffset) {
                                }
                                break;
                            }
                            break;
                        case 13:
                            currentOffset++;
                            if (currentOffset == endOffset) {
                                addToken(tokens, 21);
                                starIsMultiplyOperator = false;
                                break;
                            } else if (data.charAt(currentOffset) == 47) {
                                addToken(tokens, 22);
                                starIsMultiplyOperator = false;
                                currentOffset++;
                                if (currentOffset == endOffset) {
                                }
                                break;
                            } else {
                                addToken(tokens, 21);
                                starIsMultiplyOperator = false;
                                break;
                            }
                        case 14:
                            addToken(tokens, 47);
                            starIsMultiplyOperator = true;
                            currentOffset = scanNumber(tokens, data, endOffset, currentOffset);
                            break;
                        case 15:
                            int currentOffset6 = currentOffset + 1;
                            if (currentOffset6 == endOffset || data.charAt(currentOffset6) != 58) {
                                return false;
                            }
                            addToken(tokens, 8);
                            starIsMultiplyOperator = false;
                            currentOffset = currentOffset6 + 1;
                            if (currentOffset == endOffset) {
                            }
                            break;
                            break;
                        case 16:
                            currentOffset++;
                            if (currentOffset == endOffset) {
                                addToken(tokens, 28);
                                starIsMultiplyOperator = false;
                                break;
                            } else if (data.charAt(currentOffset) == 61) {
                                addToken(tokens, 29);
                                starIsMultiplyOperator = false;
                                currentOffset++;
                                if (currentOffset == endOffset) {
                                }
                                break;
                            } else {
                                addToken(tokens, 28);
                                starIsMultiplyOperator = false;
                                break;
                            }
                        case 17:
                            addToken(tokens, 26);
                            starIsMultiplyOperator = false;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 18:
                            currentOffset++;
                            if (currentOffset == endOffset) {
                                addToken(tokens, 30);
                                starIsMultiplyOperator = false;
                                break;
                            } else if (data.charAt(currentOffset) == 61) {
                                addToken(tokens, 31);
                                starIsMultiplyOperator = false;
                                currentOffset++;
                                if (currentOffset == endOffset) {
                                }
                                break;
                            } else {
                                addToken(tokens, 30);
                                starIsMultiplyOperator = false;
                                break;
                            }
                        case 19:
                            addToken(tokens, 6);
                            starIsMultiplyOperator = false;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 20:
                        case 23:
                        case 25:
                            int nameOffset = currentOffset;
                            currentOffset = scanNCName(data, endOffset, currentOffset);
                            if (currentOffset == nameOffset) {
                                return false;
                            }
                            if (currentOffset < endOffset) {
                                ch2 = data.charAt(currentOffset);
                            } else {
                                ch2 = -1;
                            }
                            String nameHandle2 = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
                            boolean isNameTestNCName = false;
                            boolean isAxisName = false;
                            String prefixHandle2 = XMLSymbols.EMPTY_STRING;
                            if (ch2 == 58) {
                                int currentOffset7 = currentOffset + 1;
                                if (currentOffset7 == endOffset) {
                                    return false;
                                }
                                ch2 = data.charAt(currentOffset7);
                                if (ch2 == 42) {
                                    currentOffset = currentOffset7 + 1;
                                    if (currentOffset < endOffset) {
                                        ch2 = data.charAt(currentOffset);
                                    }
                                    isNameTestNCName = true;
                                } else if (ch2 == 58) {
                                    currentOffset = currentOffset7 + 1;
                                    if (currentOffset < endOffset) {
                                        ch2 = data.charAt(currentOffset);
                                    }
                                    isAxisName = true;
                                } else {
                                    prefixHandle2 = nameHandle2;
                                    currentOffset = scanNCName(data, endOffset, currentOffset7);
                                    if (currentOffset == currentOffset7) {
                                        return false;
                                    }
                                    if (currentOffset < endOffset) {
                                        ch2 = data.charAt(currentOffset);
                                    } else {
                                        ch2 = -1;
                                    }
                                    nameHandle2 = symbolTable.addSymbol(data.substring(currentOffset7, currentOffset));
                                }
                            }
                            while (true) {
                                if (ch2 == 32 || ch2 == 10 || ch2 == 9 || ch2 == 13) {
                                    currentOffset++;
                                    if (currentOffset != endOffset) {
                                        ch2 = data.charAt(currentOffset);
                                    }
                                }
                            }
                            if (starIsMultiplyOperator) {
                                if (nameHandle2 == fAndSymbol) {
                                    addToken(tokens, 16);
                                    starIsMultiplyOperator = false;
                                } else if (nameHandle2 == fOrSymbol) {
                                    addToken(tokens, 17);
                                    starIsMultiplyOperator = false;
                                } else if (nameHandle2 == fModSymbol) {
                                    addToken(tokens, 18);
                                    starIsMultiplyOperator = false;
                                } else if (nameHandle2 == fDivSymbol) {
                                    addToken(tokens, 19);
                                    starIsMultiplyOperator = false;
                                } else {
                                    return false;
                                }
                                if (!isNameTestNCName && !isAxisName) {
                                    break;
                                } else {
                                    return false;
                                }
                            } else if (ch2 == 40 && !isNameTestNCName && !isAxisName) {
                                if (nameHandle2 == fCommentSymbol) {
                                    addToken(tokens, 12);
                                } else if (nameHandle2 == fTextSymbol) {
                                    addToken(tokens, 13);
                                } else if (nameHandle2 == fPISymbol) {
                                    addToken(tokens, 14);
                                } else if (nameHandle2 == fNodeSymbol) {
                                    addToken(tokens, 15);
                                } else {
                                    addToken(tokens, 32);
                                    tokens.addToken(prefixHandle2);
                                    tokens.addToken(nameHandle2);
                                }
                                addToken(tokens, 0);
                                starIsMultiplyOperator = false;
                                currentOffset++;
                                if (currentOffset == endOffset) {
                                }
                                break;
                            } else if (isAxisName || (ch2 == 58 && currentOffset + 1 < endOffset && data.charAt(currentOffset + 1) == ':')) {
                                if (nameHandle2 == fAncestorSymbol) {
                                    addToken(tokens, 33);
                                } else if (nameHandle2 == fAncestorOrSelfSymbol) {
                                    addToken(tokens, 34);
                                } else if (nameHandle2 == fAttributeSymbol) {
                                    addToken(tokens, 35);
                                } else if (nameHandle2 == fChildSymbol) {
                                    addToken(tokens, 36);
                                } else if (nameHandle2 == fDescendantSymbol) {
                                    addToken(tokens, 37);
                                } else if (nameHandle2 == fDescendantOrSelfSymbol) {
                                    addToken(tokens, 38);
                                } else if (nameHandle2 == fFollowingSymbol) {
                                    addToken(tokens, 39);
                                } else if (nameHandle2 == fFollowingSiblingSymbol) {
                                    addToken(tokens, 40);
                                } else if (nameHandle2 == fNamespaceSymbol) {
                                    addToken(tokens, 41);
                                } else if (nameHandle2 == fParentSymbol) {
                                    addToken(tokens, 42);
                                } else if (nameHandle2 == fPrecedingSymbol) {
                                    addToken(tokens, 43);
                                } else if (nameHandle2 == fPrecedingSiblingSymbol) {
                                    addToken(tokens, 44);
                                } else if (nameHandle2 == fSelfSymbol) {
                                    addToken(tokens, 45);
                                } else {
                                    return false;
                                }
                                if (isNameTestNCName) {
                                    return false;
                                }
                                addToken(tokens, 8);
                                starIsMultiplyOperator = false;
                                if (!isAxisName) {
                                    currentOffset = currentOffset + 1 + 1;
                                    if (currentOffset == endOffset) {
                                    }
                                    break;
                                } else {
                                    break;
                                }
                            } else if (isNameTestNCName) {
                                addToken(tokens, 10);
                                starIsMultiplyOperator = true;
                                tokens.addToken(nameHandle2);
                                break;
                            } else {
                                addToken(tokens, 11);
                                starIsMultiplyOperator = true;
                                tokens.addToken(prefixHandle2);
                                tokens.addToken(nameHandle2);
                                break;
                            }
                            break;
                        case 21:
                            addToken(tokens, 2);
                            starIsMultiplyOperator = false;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 22:
                            addToken(tokens, 3);
                            starIsMultiplyOperator = true;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                        case 24:
                            addToken(tokens, 23);
                            starIsMultiplyOperator = false;
                            currentOffset++;
                            if (currentOffset == endOffset) {
                            }
                            break;
                    }
                } else {
                    return true;
                }
            }
            return true;
        }

        int scanNCName(String data, int endOffset, int currentOffset) {
            int ch = data.charAt(currentOffset);
            if (ch >= 128) {
                if (!XMLChar.isNameStart(ch)) {
                    return currentOffset;
                }
            } else {
                byte chartype = fASCIICharMap[ch];
                if (chartype != 20 && chartype != 23) {
                    return currentOffset;
                }
            }
            while (true) {
                currentOffset++;
                if (currentOffset < endOffset) {
                    int ch2 = data.charAt(currentOffset);
                    if (ch2 >= 128) {
                        if (!XMLChar.isName(ch2)) {
                            break;
                        }
                    } else {
                        byte chartype2 = fASCIICharMap[ch2];
                        if (chartype2 != 20 && chartype2 != 14 && chartype2 != 12 && chartype2 != 11 && chartype2 != 23) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            return currentOffset;
        }

        private int scanNumber(Tokens tokens, String data, int endOffset, int currentOffset) {
            int ch = data.charAt(currentOffset);
            int whole = 0;
            int part = 0;
            while (ch >= 48 && ch <= 57) {
                whole = (whole * 10) + (ch - 48);
                currentOffset++;
                if (currentOffset == endOffset) {
                    break;
                }
                ch = data.charAt(currentOffset);
            }
            if (ch == 46) {
                currentOffset++;
                if (currentOffset < endOffset) {
                    int iCharAt = data.charAt(currentOffset);
                    while (true) {
                        int ch2 = iCharAt;
                        if (ch2 < 48 || ch2 > 57) {
                            break;
                        }
                        part = (part * 10) + (ch2 - 48);
                        currentOffset++;
                        if (currentOffset == endOffset) {
                            break;
                        }
                        iCharAt = data.charAt(currentOffset);
                    }
                    if (part != 0) {
                        throw new RuntimeException("find a solution!");
                    }
                }
            }
            tokens.addToken(whole);
            tokens.addToken(part);
            return currentOffset;
        }

        protected void addToken(Tokens tokens, int token) throws XPathException {
            tokens.addToken(token);
        }
    }

    public static void main(String[] argv) throws Exception {
        for (String expression : argv) {
            System.out.println("# XPath expression: \"" + expression + '\"');
            try {
                SymbolTable symbolTable = new SymbolTable();
                XPath xpath = new XPath(expression, symbolTable, null);
                System.out.println("expanded xpath: \"" + xpath.toString() + '\"');
            } catch (XPathException e2) {
                System.out.println("error: " + e2.getMessage());
            }
        }
    }
}
