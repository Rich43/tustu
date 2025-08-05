package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.GOTO_W;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Expression.class */
abstract class Expression extends SyntaxTreeNode {
    protected Type _type;
    protected FlowList _trueList = new FlowList();
    protected FlowList _falseList = new FlowList();

    public abstract String toString();

    Expression() {
    }

    public Type getType() {
        return this._type;
    }

    public boolean hasPositionCall() {
        return false;
    }

    public boolean hasLastCall() {
        return false;
    }

    public Object evaluateAtCompileTime() {
        return null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return typeCheckContents(stable);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ErrorMsg msg = new ErrorMsg(ErrorMsg.NOT_IMPLEMENTED_ERR, (Object) getClass(), (SyntaxTreeNode) this);
        getParser().reportError(2, msg);
    }

    public final InstructionList compile(ClassGenerator classGen, MethodGenerator methodGen) {
        InstructionList save = methodGen.getInstructionList();
        InstructionList result = new InstructionList();
        methodGen.setInstructionList(result);
        translate(classGen, methodGen);
        methodGen.setInstructionList(save);
        return result;
    }

    public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
        translate(classGen, methodGen);
        if (this._type instanceof BooleanType) {
            desynthesize(classGen, methodGen);
        }
    }

    public void startIterator(ClassGenerator classGen, MethodGenerator methodGen) {
        if (!(this._type instanceof NodeSetType)) {
            return;
        }
        Expression expr = this;
        if (expr instanceof CastExpr) {
            expr = ((CastExpr) expr).getExpr();
        }
        if (!(expr instanceof VariableRefBase)) {
            InstructionList il = methodGen.getInstructionList();
            il.append(methodGen.loadContextNode());
            il.append(methodGen.setStartNode());
        }
    }

    public void synthesize(ClassGenerator classGen, MethodGenerator methodGen) {
        classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        this._trueList.backPatch(il.append(ICONST_1));
        BranchHandle truec = il.append((BranchInstruction) new GOTO_W(null));
        this._falseList.backPatch(il.append(ICONST_0));
        truec.setTarget(il.append(NOP));
    }

    public void desynthesize(ClassGenerator classGen, MethodGenerator methodGen) {
        InstructionList il = methodGen.getInstructionList();
        this._falseList.add(il.append((BranchInstruction) new IFEQ(null)));
    }

    public FlowList getFalseList() {
        return this._falseList;
    }

    public FlowList getTrueList() {
        return this._trueList;
    }

    public void backPatchFalseList(InstructionHandle ih) {
        this._falseList.backPatch(ih);
    }

    public void backPatchTrueList(InstructionHandle ih) {
        this._trueList.backPatch(ih);
    }

    public MethodType lookupPrimop(SymbolTable stable, String op, MethodType ctype) {
        MethodType result = null;
        Vector primop = stable.lookupPrimop(op);
        if (primop != null) {
            int n2 = primop.size();
            int minDistance = Integer.MAX_VALUE;
            for (int i2 = 0; i2 < n2; i2++) {
                MethodType ptype = (MethodType) primop.elementAt(i2);
                if (ptype.argsCount() == ctype.argsCount()) {
                    if (result == null) {
                        result = ptype;
                    }
                    int distance = ctype.distanceTo(ptype);
                    if (distance < minDistance) {
                        minDistance = distance;
                        result = ptype;
                    }
                }
            }
        }
        return result;
    }
}
