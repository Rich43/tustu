package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import java.util.StringTokenizer;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Whitespace.class */
final class Whitespace extends TopLevelElement {
    public static final int USE_PREDICATE = 0;
    public static final int STRIP_SPACE = 1;
    public static final int PRESERVE_SPACE = 2;
    public static final int RULE_NONE = 0;
    public static final int RULE_ELEMENT = 1;
    public static final int RULE_NAMESPACE = 2;
    public static final int RULE_ALL = 3;
    private String _elementList;
    private int _action;
    private int _importPrecedence;

    Whitespace() {
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Whitespace$WhitespaceRule.class */
    private static final class WhitespaceRule {
        private final int _action;
        private String _namespace;
        private String _element;
        private int _type;
        private int _priority;

        public WhitespaceRule(int action, String element, int precedence) {
            this._action = action;
            int colon = element.lastIndexOf(58);
            if (colon >= 0) {
                this._namespace = element.substring(0, colon);
                this._element = element.substring(colon + 1, element.length());
            } else {
                this._namespace = "";
                this._element = element;
            }
            this._priority = precedence << 2;
            if (this._element.equals("*")) {
                if (this._namespace == "") {
                    this._type = 3;
                    this._priority += 2;
                    return;
                } else {
                    this._type = 2;
                    this._priority++;
                    return;
                }
            }
            this._type = 1;
        }

        public int compareTo(WhitespaceRule other) {
            if (this._priority < other._priority) {
                return -1;
            }
            return this._priority > other._priority ? 1 : 0;
        }

        public int getAction() {
            return this._action;
        }

        public int getStrength() {
            return this._type;
        }

        public int getPriority() {
            return this._priority;
        }

        public String getElement() {
            return this._element;
        }

        public String getNamespace() {
            return this._namespace;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String namespace;
        this._action = this._qname.getLocalPart().endsWith(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_STRIPSPACE_STRING) ? 1 : 2;
        this._importPrecedence = parser.getCurrentImportPrecedence();
        this._elementList = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ELEMENTS);
        if (this._elementList == null || this._elementList.length() == 0) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ELEMENTS);
            return;
        }
        parser.getSymbolTable();
        StringTokenizer list = new StringTokenizer(this._elementList);
        StringBuffer elements = new StringBuffer("");
        while (list.hasMoreElements()) {
            String token = list.nextToken();
            int col = token.indexOf(58);
            if (col != -1 && (namespace = lookupNamespace(token.substring(0, col))) != null) {
                elements.append(namespace).append(':').append(token.substring(col + 1));
            } else {
                elements.append(token);
            }
            if (list.hasMoreElements()) {
                elements.append(" ");
            }
        }
        this._elementList = elements.toString();
    }

    public Vector getRules() {
        Vector rules = new Vector();
        StringTokenizer list = new StringTokenizer(this._elementList);
        while (list.hasMoreElements()) {
            rules.add(new WhitespaceRule(this._action, list.nextToken(), this._importPrecedence));
        }
        return rules;
    }

    private static WhitespaceRule findContradictingRule(Vector rules, WhitespaceRule rule) {
        WhitespaceRule currentRule;
        for (int i2 = 0; i2 < rules.size() && (currentRule = (WhitespaceRule) rules.elementAt(i2)) != rule; i2++) {
            switch (currentRule.getStrength()) {
                case 1:
                    if (!rule.getElement().equals(currentRule.getElement())) {
                        continue;
                    }
                case 2:
                    if (rule.getNamespace().equals(currentRule.getNamespace())) {
                        break;
                    }
                case 3:
                    break;
                default:
            }
            return currentRule;
        }
        return null;
    }

    private static int prioritizeRules(Vector rules) {
        int defaultAction = 2;
        quicksort(rules, 0, rules.size() - 1);
        boolean strip = false;
        for (int i2 = 0; i2 < rules.size(); i2++) {
            if (((WhitespaceRule) rules.elementAt(i2)).getAction() == 1) {
                strip = true;
            }
        }
        if (!strip) {
            rules.removeAllElements();
            return 2;
        }
        int idx = 0;
        while (idx < rules.size()) {
            WhitespaceRule currentRule = (WhitespaceRule) rules.elementAt(idx);
            if (findContradictingRule(rules, currentRule) != null) {
                rules.remove(idx);
            } else {
                if (currentRule.getStrength() == 3) {
                    defaultAction = currentRule.getAction();
                    for (int i3 = idx; i3 < rules.size(); i3++) {
                        rules.removeElementAt(i3);
                    }
                }
                idx++;
            }
        }
        if (rules.size() == 0) {
            return defaultAction;
        }
        while (((WhitespaceRule) rules.lastElement()).getAction() == defaultAction) {
            rules.removeElementAt(rules.size() - 1);
            if (rules.size() <= 0) {
                break;
            }
        }
        return defaultAction;
    }

    public static void compileStripSpace(BranchHandle[] strip, int sCount, InstructionList il) {
        InstructionHandle target = il.append(ICONST_1);
        il.append(IRETURN);
        for (int i2 = 0; i2 < sCount; i2++) {
            strip[i2].setTarget(target);
        }
    }

    public static void compilePreserveSpace(BranchHandle[] preserve, int pCount, InstructionList il) {
        InstructionHandle target = il.append(ICONST_0);
        il.append(IRETURN);
        for (int i2 = 0; i2 < pCount; i2++) {
            preserve[i2].setTarget(target);
        }
    }

    private static void compilePredicate(Vector rules, int defaultAction, ClassGenerator classGen) {
        QName qname;
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = new InstructionList();
        XSLTC xsltc = classGen.getParser().getXSLTC();
        MethodGenerator stripSpace = new MethodGenerator(17, Type.BOOLEAN, new Type[]{Util.getJCRefType(Constants.DOM_INTF_SIG), Type.INT, Type.INT}, new String[]{Constants.DOM_PNAME, "node", "type"}, Constants.STRIP_SPACE, classGen.getClassName(), il, cpg);
        classGen.addInterface(Constants.STRIP_SPACE_INTF);
        int paramDom = stripSpace.getLocalIndex(Constants.DOM_PNAME);
        int paramCurrent = stripSpace.getLocalIndex("node");
        int paramType = stripSpace.getLocalIndex("type");
        BranchHandle[] strip = new BranchHandle[rules.size()];
        BranchHandle[] preserve = new BranchHandle[rules.size()];
        int sCount = 0;
        int pCount = 0;
        for (int i2 = 0; i2 < rules.size(); i2++) {
            WhitespaceRule rule = (WhitespaceRule) rules.elementAt(i2);
            int gns = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getNamespaceName", "(I)Ljava/lang/String;");
            int strcmp = cpg.addMethodref("java/lang/String", "compareTo", Constants.STRING_TO_INT_SIG);
            if (rule.getStrength() == 2) {
                il.append(new ALOAD(paramDom));
                il.append(new ILOAD(paramCurrent));
                il.append(new INVOKEINTERFACE(gns, 2));
                il.append(new PUSH(cpg, rule.getNamespace()));
                il.append(new INVOKEVIRTUAL(strcmp));
                il.append(ICONST_0);
                if (rule.getAction() == 1) {
                    int i3 = sCount;
                    sCount++;
                    strip[i3] = il.append((BranchInstruction) new IF_ICMPEQ(null));
                } else {
                    int i4 = pCount;
                    pCount++;
                    preserve[i4] = il.append((BranchInstruction) new IF_ICMPEQ(null));
                }
            } else if (rule.getStrength() == 1) {
                Parser parser = classGen.getParser();
                if (rule.getNamespace() != "") {
                    qname = parser.getQName(rule.getNamespace(), (String) null, rule.getElement());
                } else {
                    qname = parser.getQName(rule.getElement());
                }
                int elementType = xsltc.registerElement(qname);
                il.append(new ILOAD(paramType));
                il.append(new PUSH(cpg, elementType));
                if (rule.getAction() == 1) {
                    int i5 = sCount;
                    sCount++;
                    strip[i5] = il.append((BranchInstruction) new IF_ICMPEQ(null));
                } else {
                    int i6 = pCount;
                    pCount++;
                    preserve[i6] = il.append((BranchInstruction) new IF_ICMPEQ(null));
                }
            }
        }
        if (defaultAction == 1) {
            compileStripSpace(strip, sCount, il);
            compilePreserveSpace(preserve, pCount, il);
        } else {
            compilePreserveSpace(preserve, pCount, il);
            compileStripSpace(strip, sCount, il);
        }
        classGen.addMethod(stripSpace);
    }

    private static void compileDefault(int defaultAction, ClassGenerator classGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = new InstructionList();
        classGen.getParser().getXSLTC();
        MethodGenerator stripSpace = new MethodGenerator(17, Type.BOOLEAN, new Type[]{Util.getJCRefType(Constants.DOM_INTF_SIG), Type.INT, Type.INT}, new String[]{Constants.DOM_PNAME, "node", "type"}, Constants.STRIP_SPACE, classGen.getClassName(), il, cpg);
        classGen.addInterface(Constants.STRIP_SPACE_INTF);
        if (defaultAction == 1) {
            il.append(ICONST_1);
        } else {
            il.append(ICONST_0);
        }
        il.append(IRETURN);
        classGen.addMethod(stripSpace);
    }

    public static int translateRules(Vector rules, ClassGenerator classGen) {
        int defaultAction = prioritizeRules(rules);
        if (rules.size() == 0) {
            compileDefault(defaultAction, classGen);
            return defaultAction;
        }
        compilePredicate(rules, defaultAction, classGen);
        return 0;
    }

    private static void quicksort(Vector rules, int p2, int r2) {
        while (p2 < r2) {
            int q2 = partition(rules, p2, r2);
            quicksort(rules, p2, q2);
            p2 = q2 + 1;
        }
    }

    private static int partition(Vector rules, int p2, int r2) {
        WhitespaceRule x2 = (WhitespaceRule) rules.elementAt((p2 + r2) >>> 1);
        int i2 = p2 - 1;
        int j2 = r2 + 1;
        while (true) {
            j2--;
            if (x2.compareTo((WhitespaceRule) rules.elementAt(j2)) >= 0) {
                do {
                    i2++;
                } while (x2.compareTo((WhitespaceRule) rules.elementAt(i2)) > 0);
                if (i2 < j2) {
                    WhitespaceRule tmp = (WhitespaceRule) rules.elementAt(i2);
                    rules.setElementAt(rules.elementAt(j2), i2);
                    rules.setElementAt(tmp, j2);
                } else {
                    return j2;
                }
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }
}
