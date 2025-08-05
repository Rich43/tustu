package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFLT;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/AncestorPattern.class */
final class AncestorPattern extends RelativePathPattern {
    private final Pattern _left;
    private final RelativePathPattern _right;
    private InstructionHandle _loop;

    public AncestorPattern(RelativePathPattern right) {
        this(null, right);
    }

    public AncestorPattern(Pattern left, RelativePathPattern right) {
        this._left = left;
        this._right = right;
        right.setParent(this);
        if (left != null) {
            left.setParent(this);
        }
    }

    public InstructionHandle getLoopHandle() {
        return this._loop;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._left != null) {
            this._left.setParser(parser);
        }
        this._right.setParser(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public boolean isWildcard() {
        return false;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public StepPattern getKernelPattern() {
        return this._right.getKernelPattern();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public void reduceKernelPattern() {
        this._right.reduceKernelPattern();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._left != null) {
            this._left.typeCheck(stable);
        }
        return this._right.typeCheck(stable);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        LocalVariableGen local = methodGen.addLocalVariable2("app", Util.getJCRefType("I"), il.getEnd());
        com.sun.org.apache.bcel.internal.generic.Instruction loadLocal = new ILOAD(local.getIndex());
        com.sun.org.apache.bcel.internal.generic.Instruction storeLocal = new ISTORE(local.getIndex());
        if (this._right instanceof StepPattern) {
            il.append(DUP);
            il.append(storeLocal);
            this._right.translate(classGen, methodGen);
            il.append(methodGen.loadDOM());
            il.append(loadLocal);
        } else {
            this._right.translate(classGen, methodGen);
            if (this._right instanceof AncestorPattern) {
                il.append(methodGen.loadDOM());
                il.append(SWAP);
            }
        }
        if (this._left != null) {
            int getParent = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_PARENT, Constants.GET_PARENT_SIG);
            InstructionHandle parent = il.append(new INVOKEINTERFACE(getParent, 2));
            il.append(DUP);
            il.append(storeLocal);
            this._falseList.add(il.append((BranchInstruction) new IFLT(null)));
            il.append(loadLocal);
            this._left.translate(classGen, methodGen);
            SyntaxTreeNode p2 = getParent();
            if (p2 != null && !(p2 instanceof Instruction) && !(p2 instanceof TopLevelElement)) {
                il.append(loadLocal);
            }
            BranchHandle exit = il.append((BranchInstruction) new GOTO(null));
            this._loop = il.append(methodGen.loadDOM());
            il.append(loadLocal);
            local.setEnd(this._loop);
            il.append((BranchInstruction) new GOTO(parent));
            exit.setTarget(il.append(NOP));
            this._left.backPatchFalseList(this._loop);
            this._trueList.append(this._left._trueList);
        } else {
            il.append(POP2);
        }
        if (this._right instanceof AncestorPattern) {
            AncestorPattern ancestor = (AncestorPattern) this._right;
            this._falseList.backPatch(ancestor.getLoopHandle());
        }
        this._trueList.append(this._right._trueList);
        this._falseList.append(this._right._falseList);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "AncestorPattern(" + ((Object) this._left) + ", " + ((Object) this._right) + ')';
    }
}
