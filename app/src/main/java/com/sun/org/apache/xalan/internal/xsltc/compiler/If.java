package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/If.class */
final class If extends Instruction {
    private Expression _test;
    private boolean _ignore = false;

    If() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("If");
        indent(indent + 4);
        System.out.print("test ");
        Util.println(this._test.toString());
        displayContents(indent + 4);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        this._test = parser.parseExpression(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_TEST, null);
        if (this._test.isDummy()) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_TEST);
            return;
        }
        Object result = this._test.evaluateAtCompileTime();
        if (result != null && (result instanceof Boolean)) {
            this._ignore = !((Boolean) result).booleanValue();
        }
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (!(this._test.typeCheck(stable) instanceof BooleanType)) {
            this._test = new CastExpr(this._test, Type.Boolean);
        }
        if (!this._ignore) {
            typeCheckContents(stable);
        }
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        InstructionList il = methodGen.getInstructionList();
        this._test.translateDesynthesized(classGen, methodGen);
        InstructionHandle truec = il.getEnd();
        if (!this._ignore) {
            translateContents(classGen, methodGen);
        }
        this._test.backPatchFalseList(il.append(NOP));
        this._test.backPatchTrueList(truec.getNext());
    }
}
