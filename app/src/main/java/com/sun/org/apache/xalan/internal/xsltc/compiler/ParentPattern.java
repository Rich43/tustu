package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
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

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ParentPattern.class */
final class ParentPattern extends RelativePathPattern {
    private final Pattern _left;
    private final RelativePathPattern _right;

    public ParentPattern(Pattern left, RelativePathPattern right) {
        this._left = left;
        left.setParent(this);
        this._right = right;
        right.setParent(this);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
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
        this._left.typeCheck(stable);
        return this._right.typeCheck(stable);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        LocalVariableGen local = methodGen.addLocalVariable2("ppt", Util.getJCRefType("I"), null);
        com.sun.org.apache.bcel.internal.generic.Instruction loadLocal = new ILOAD(local.getIndex());
        com.sun.org.apache.bcel.internal.generic.Instruction storeLocal = new ISTORE(local.getIndex());
        if (this._right.isWildcard()) {
            il.append(methodGen.loadDOM());
            il.append(SWAP);
        } else if (this._right instanceof StepPattern) {
            il.append(DUP);
            local.setStart(il.append(storeLocal));
            this._right.translate(classGen, methodGen);
            il.append(methodGen.loadDOM());
            local.setEnd(il.append(loadLocal));
        } else {
            this._right.translate(classGen, methodGen);
            if (this._right instanceof AncestorPattern) {
                il.append(methodGen.loadDOM());
                il.append(SWAP);
            }
        }
        int getParent = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_PARENT, Constants.GET_PARENT_SIG);
        il.append(new INVOKEINTERFACE(getParent, 2));
        SyntaxTreeNode p2 = getParent();
        if (p2 == null || (p2 instanceof Instruction) || (p2 instanceof TopLevelElement)) {
            this._left.translate(classGen, methodGen);
        } else {
            il.append(DUP);
            InstructionHandle storeInst = il.append(storeLocal);
            if (local.getStart() == null) {
                local.setStart(storeInst);
            }
            this._left.translate(classGen, methodGen);
            il.append(methodGen.loadDOM());
            local.setEnd(il.append(loadLocal));
        }
        methodGen.removeLocalVariable(local);
        if (this._right instanceof AncestorPattern) {
            AncestorPattern ancestor = (AncestorPattern) this._right;
            this._left.backPatchFalseList(ancestor.getLoopHandle());
        }
        this._trueList.append(this._right._trueList.append(this._left._trueList));
        this._falseList.append(this._right._falseList.append(this._left._falseList));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "Parent(" + ((Object) this._left) + ", " + ((Object) this._right) + ')';
    }
}
