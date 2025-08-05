package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xpath.internal.compiler.Keywords;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ApplyImports.class */
final class ApplyImports extends Instruction {
    private QName _modeName;
    private int _precedence;

    ApplyImports() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("ApplyTemplates");
        indent(indent + 4);
        if (this._modeName != null) {
            indent(indent + 4);
            Util.println("mode " + ((Object) this._modeName));
        }
    }

    public boolean hasWithParams() {
        return hasContents();
    }

    private int getMinPrecedence(int max) {
        Stylesheet stylesheet = getStylesheet();
        while (true) {
            Stylesheet includeRoot = stylesheet;
            if (includeRoot._includedFrom != null) {
                stylesheet = includeRoot._includedFrom;
            } else {
                return includeRoot.getMinimumDescendantPrecedence();
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        Stylesheet stylesheet = getStylesheet();
        stylesheet.setTemplateInlining(false);
        Template template = getTemplate();
        this._modeName = template.getModeName();
        this._precedence = template.getImportPrecedence();
        parser.getTopLevelStylesheet();
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        typeCheckContents(stable);
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        Stylesheet stylesheet = classGen.getStylesheet();
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        methodGen.getLocalIndex(Keywords.FUNC_CURRENT_STRING);
        il.append(classGen.loadTranslet());
        il.append(methodGen.loadDOM());
        il.append(methodGen.loadIterator());
        il.append(methodGen.loadHandler());
        il.append(methodGen.loadCurrentNode());
        if (stylesheet.hasLocalParams()) {
            il.append(classGen.loadTranslet());
            int pushFrame = cpg.addMethodref(Constants.TRANSLET_CLASS, Constants.PUSH_PARAM_FRAME, "()V");
            il.append(new INVOKEVIRTUAL(pushFrame));
        }
        int maxPrecedence = this._precedence;
        int minPrecedence = getMinPrecedence(maxPrecedence);
        Mode mode = stylesheet.getMode(this._modeName);
        String functionName = mode.functionName(minPrecedence, maxPrecedence);
        String className = classGen.getStylesheet().getClassName();
        String signature = classGen.getApplyTemplatesSigForImport();
        int applyTemplates = cpg.addMethodref(className, functionName, signature);
        il.append(new INVOKEVIRTUAL(applyTemplates));
        if (stylesheet.hasLocalParams()) {
            il.append(classGen.loadTranslet());
            int pushFrame2 = cpg.addMethodref(Constants.TRANSLET_CLASS, Constants.POP_PARAM_FRAME, "()V");
            il.append(new INVOKEVIRTUAL(pushFrame2));
        }
    }
}
