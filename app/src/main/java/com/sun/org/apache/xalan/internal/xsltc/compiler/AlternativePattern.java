package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/AlternativePattern.class */
final class AlternativePattern extends Pattern {
    private final Pattern _left;
    private final Pattern _right;

    public AlternativePattern(Pattern left, Pattern right) {
        this._left = left;
        this._right = right;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
        this._right.setParser(parser);
    }

    public Pattern getLeft() {
        return this._left;
    }

    public Pattern getRight() {
        return this._right;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        this._left.typeCheck(stable);
        this._right.typeCheck(stable);
        return null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern
    public double getPriority() {
        double left = this._left.getPriority();
        double right = this._right.getPriority();
        if (left < right) {
            return left;
        }
        return right;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "alternative(" + ((Object) this._left) + ", " + ((Object) this._right) + ')';
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        InstructionList il = methodGen.getInstructionList();
        this._left.translate(classGen, methodGen);
        InstructionHandle gotot = il.append((BranchInstruction) new GOTO(null));
        il.append(methodGen.loadContextNode());
        this._right.translate(classGen, methodGen);
        this._left._trueList.backPatch(gotot);
        this._left._falseList.backPatch(gotot.getNext());
        this._trueList.append(this._right._trueList.add(gotot));
        this._falseList.append(this._right._falseList);
    }
}
