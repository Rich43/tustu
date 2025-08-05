package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Pattern.class */
public abstract class Pattern extends Expression {
    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public abstract Type typeCheck(SymbolTable symbolTable) throws TypeCheckError;

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public abstract void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator);

    public abstract double getPriority();

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ MethodType lookupPrimop(SymbolTable symbolTable, String str, MethodType methodType) {
        return super.lookupPrimop(symbolTable, str, methodType);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ void backPatchTrueList(InstructionHandle instructionHandle) {
        super.backPatchTrueList(instructionHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ void backPatchFalseList(InstructionHandle instructionHandle) {
        super.backPatchFalseList(instructionHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ FlowList getTrueList() {
        return super.getTrueList();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ FlowList getFalseList() {
        return super.getFalseList();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ void desynthesize(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        super.desynthesize(classGenerator, methodGenerator);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ void synthesize(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        super.synthesize(classGenerator, methodGenerator);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ void startIterator(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        super.startIterator(classGenerator, methodGenerator);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ void translateDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        super.translateDesynthesized(classGenerator, methodGenerator);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ Object evaluateAtCompileTime() {
        return super.evaluateAtCompileTime();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ boolean hasLastCall() {
        return super.hasLastCall();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ boolean hasPositionCall() {
        return super.hasPositionCall();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public /* bridge */ /* synthetic */ Type getType() {
        return super.getType();
    }
}
