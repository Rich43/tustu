package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Fallback.class */
final class Fallback extends Instruction {
    private boolean _active = false;

    Fallback() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._active) {
            return typeCheckContents(stable);
        }
        return Type.Void;
    }

    public void activate() {
        this._active = true;
    }

    public String toString() {
        return com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_FALLBACK_STRING;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        if (this._active) {
            parseChildren(parser);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        classGen.getConstantPool();
        methodGen.getInstructionList();
        if (this._active) {
            translateContents(classGen, methodGen);
        }
    }
}
