package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO_W;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/AbsolutePathPattern.class */
final class AbsolutePathPattern extends LocationPathPattern {
    private final RelativePathPattern _left;

    public AbsolutePathPattern(RelativePathPattern left) {
        this._left = left;
        if (left != null) {
            left.setParent(this);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._left != null) {
            this._left.setParser(parser);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return this._left == null ? Type.Root : this._left.typeCheck(stable);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public boolean isWildcard() {
        return false;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public StepPattern getKernelPattern() {
        if (this._left != null) {
            return this._left.getKernelPattern();
        }
        return null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public void reduceKernelPattern() {
        this._left.reduceKernelPattern();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._left != null) {
            if (this._left instanceof StepPattern) {
                LocalVariableGen local = methodGen.addLocalVariable2("apptmp", Util.getJCRefType("I"), null);
                il.append(DUP);
                local.setStart(il.append(new ISTORE(local.getIndex())));
                this._left.translate(classGen, methodGen);
                il.append(methodGen.loadDOM());
                local.setEnd(il.append(new ILOAD(local.getIndex())));
                methodGen.removeLocalVariable(local);
            } else {
                this._left.translate(classGen, methodGen);
            }
        }
        int getParent = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_PARENT, Constants.GET_PARENT_SIG);
        int getType = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getExpandedTypeID", Constants.GET_PARENT_SIG);
        InstructionHandle begin = il.append(methodGen.loadDOM());
        il.append(SWAP);
        il.append(new INVOKEINTERFACE(getParent, 2));
        if (this._left instanceof AncestorPattern) {
            il.append(methodGen.loadDOM());
            il.append(SWAP);
        }
        il.append(new INVOKEINTERFACE(getType, 2));
        il.append(new PUSH(cpg, 9));
        BranchHandle skip = il.append((BranchInstruction) new IF_ICMPEQ(null));
        this._falseList.add(il.append((BranchInstruction) new GOTO_W(null)));
        skip.setTarget(il.append(NOP));
        if (this._left != null) {
            this._left.backPatchTrueList(begin);
            if (this._left instanceof AncestorPattern) {
                AncestorPattern ancestor = (AncestorPattern) this._left;
                this._falseList.backPatch(ancestor.getLoopHandle());
            }
            this._falseList.append(this._left._falseList);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "absolutePathPattern(" + (this._left != null ? this._left.toString() : ")");
    }
}
