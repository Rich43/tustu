package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xml.internal.utils.XML11Char;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/DecimalFormatting.class */
final class DecimalFormatting extends TopLevelElement {
    private static final String DFS_CLASS = "java.text.DecimalFormatSymbols";
    private static final String DFS_SIG = "Ljava/text/DecimalFormatSymbols;";
    private QName _name = null;

    DecimalFormatting() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String name = getAttribute("name");
        if (name.length() > 0 && !XML11Char.isXML11ValidQName(name)) {
            ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) name, (SyntaxTreeNode) this);
            parser.reportError(3, err);
        }
        this._name = parser.getQNameIgnoreDefaultNs(name);
        if (this._name == null) {
            this._name = parser.getQNameIgnoreDefaultNs("");
        }
        SymbolTable stable = parser.getSymbolTable();
        if (stable.getDecimalFormatting(this._name) != null) {
            reportWarning(this, parser, ErrorMsg.SYMBOLS_REDEF_ERR, this._name.toString());
        } else {
            stable.addDecimalFormatting(this._name, this);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int init = cpg.addMethodref(DFS_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Ljava/util/Locale;)V");
        il.append(classGen.loadTranslet());
        il.append(new PUSH(cpg, this._name.toString()));
        il.append(new NEW(cpg.addClass(DFS_CLASS)));
        il.append(DUP);
        il.append(new GETSTATIC(cpg.addFieldref(Constants.LOCALE_CLASS, "US", Constants.LOCALE_SIG)));
        il.append(new INVOKESPECIAL(init));
        String tmp = getAttribute("NaN");
        if (tmp == null || tmp.equals("")) {
            int nan = cpg.addMethodref(DFS_CLASS, "setNaN", "(Ljava/lang/String;)V");
            il.append(DUP);
            il.append(new PUSH(cpg, "NaN"));
            il.append(new INVOKEVIRTUAL(nan));
        }
        String tmp2 = getAttribute("infinity");
        if (tmp2 == null || tmp2.equals("")) {
            int inf = cpg.addMethodref(DFS_CLASS, "setInfinity", "(Ljava/lang/String;)V");
            il.append(DUP);
            il.append(new PUSH(cpg, com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_INFINITY));
            il.append(new INVOKEVIRTUAL(inf));
        }
        int nAttributes = this._attributes.getLength();
        for (int i2 = 0; i2 < nAttributes; i2++) {
            String name = this._attributes.getQName(i2);
            String value = this._attributes.getValue(i2);
            boolean valid = true;
            int method = 0;
            if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DECIMALSEPARATOR)) {
                method = cpg.addMethodref(DFS_CLASS, "setDecimalSeparator", "(C)V");
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_GROUPINGSEPARATOR)) {
                method = cpg.addMethodref(DFS_CLASS, "setGroupingSeparator", "(C)V");
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MINUSSIGN)) {
                method = cpg.addMethodref(DFS_CLASS, "setMinusSign", "(C)V");
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_PERCENT)) {
                method = cpg.addMethodref(DFS_CLASS, "setPercent", "(C)V");
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_PERMILLE)) {
                method = cpg.addMethodref(DFS_CLASS, "setPerMill", "(C)V");
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ZERODIGIT)) {
                method = cpg.addMethodref(DFS_CLASS, "setZeroDigit", "(C)V");
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DIGIT)) {
                method = cpg.addMethodref(DFS_CLASS, "setDigit", "(C)V");
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_PATTERNSEPARATOR)) {
                method = cpg.addMethodref(DFS_CLASS, "setPatternSeparator", "(C)V");
            } else if (name.equals("NaN")) {
                method = cpg.addMethodref(DFS_CLASS, "setNaN", "(Ljava/lang/String;)V");
                il.append(DUP);
                il.append(new PUSH(cpg, value));
                il.append(new INVOKEVIRTUAL(method));
                valid = false;
            } else if (name.equals("infinity")) {
                method = cpg.addMethodref(DFS_CLASS, "setInfinity", "(Ljava/lang/String;)V");
                il.append(DUP);
                il.append(new PUSH(cpg, value));
                il.append(new INVOKEVIRTUAL(method));
                valid = false;
            } else {
                valid = false;
            }
            if (valid) {
                il.append(DUP);
                il.append(new PUSH(cpg, (int) value.charAt(0)));
                il.append(new INVOKEVIRTUAL(method));
            }
        }
        int put = cpg.addMethodref(Constants.TRANSLET_CLASS, "addDecimalFormat", "(Ljava/lang/String;Ljava/text/DecimalFormatSymbols;)V");
        il.append(new INVOKEVIRTUAL(put));
    }

    public static void translateDefaultDFS(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int init = cpg.addMethodref(DFS_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Ljava/util/Locale;)V");
        il.append(classGen.loadTranslet());
        il.append(new PUSH(cpg, ""));
        il.append(new NEW(cpg.addClass(DFS_CLASS)));
        il.append(DUP);
        il.append(new GETSTATIC(cpg.addFieldref(Constants.LOCALE_CLASS, "US", Constants.LOCALE_SIG)));
        il.append(new INVOKESPECIAL(init));
        int nan = cpg.addMethodref(DFS_CLASS, "setNaN", "(Ljava/lang/String;)V");
        il.append(DUP);
        il.append(new PUSH(cpg, "NaN"));
        il.append(new INVOKEVIRTUAL(nan));
        int inf = cpg.addMethodref(DFS_CLASS, "setInfinity", "(Ljava/lang/String;)V");
        il.append(DUP);
        il.append(new PUSH(cpg, com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_INFINITY));
        il.append(new INVOKEVIRTUAL(inf));
        int put = cpg.addMethodref(Constants.TRANSLET_CLASS, "addDecimalFormat", "(Ljava/lang/String;Ljava/text/DecimalFormatSymbols;)V");
        il.append(new INVOKEVIRTUAL(put));
    }
}
