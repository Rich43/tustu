package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/LogicalExpr.class */
final class LogicalExpr extends Expression {
    public static final int OR = 0;
    public static final int AND = 1;
    private final int _op;
    private Expression _left;
    private Expression _right;
    private static final String[] Ops = {"or", "and"};

    public LogicalExpr(int op, Expression left, Expression right) {
        this._op = op;
        this._left = left;
        left.setParent(this);
        this._right = right;
        right.setParent(this);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasPositionCall() {
        return this._left.hasPositionCall() || this._right.hasPositionCall();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasLastCall() {
        return this._left.hasLastCall() || this._right.hasLastCall();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public Object evaluateAtCompileTime() {
        Object leftb = this._left.evaluateAtCompileTime();
        Object rightb = this._right.evaluateAtCompileTime();
        if (leftb == null || rightb == null) {
            return null;
        }
        return this._op == 1 ? (leftb == Boolean.TRUE && rightb == Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE : (leftb == Boolean.TRUE || rightb == Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE;
    }

    public int getOp() {
        return this._op;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
        this._right.setParser(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return Ops[this._op] + '(' + ((Object) this._left) + ", " + ((Object) this._right) + ')';
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type tleft = this._left.typeCheck(stable);
        Type tright = this._right.typeCheck(stable);
        MethodType wantType = new MethodType(Type.Void, tleft, tright);
        MethodType haveType = lookupPrimop(stable, Ops[this._op], wantType);
        if (haveType != null) {
            Type arg1 = (Type) haveType.argsType().elementAt(0);
            if (!arg1.identicalTo(tleft)) {
                this._left = new CastExpr(this._left, arg1);
            }
            Type arg2 = (Type) haveType.argsType().elementAt(1);
            if (!arg2.identicalTo(tright)) {
                this._right = new CastExpr(this._right, arg1);
            }
            Type typeResultType = haveType.resultType();
            this._type = typeResultType;
            return typeResultType;
        }
        throw new TypeCheckError(this);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        translateDesynthesized(classGen, methodGen);
        synthesize(classGen, methodGen);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
        InstructionList il = methodGen.getInstructionList();
        getParent();
        if (this._op == 1) {
            this._left.translateDesynthesized(classGen, methodGen);
            InstructionHandle middle = il.append(NOP);
            this._right.translateDesynthesized(classGen, methodGen);
            InstructionHandle after = il.append(NOP);
            this._falseList.append(this._right._falseList.append(this._left._falseList));
            if (((this._left instanceof LogicalExpr) && ((LogicalExpr) this._left).getOp() == 0) || (this._left instanceof NotCall)) {
                this._left.backPatchTrueList(middle);
            } else {
                this._trueList.append(this._left._trueList);
            }
            if ((this._right instanceof LogicalExpr) && ((LogicalExpr) this._right).getOp() == 0) {
                this._right.backPatchTrueList(after);
                return;
            } else if (this._right instanceof NotCall) {
                this._right.backPatchTrueList(after);
                return;
            } else {
                this._trueList.append(this._right._trueList);
                return;
            }
        }
        this._left.translateDesynthesized(classGen, methodGen);
        InstructionHandle ih = il.append((BranchInstruction) new GOTO(null));
        this._right.translateDesynthesized(classGen, methodGen);
        this._left._trueList.backPatch(ih);
        this._left._falseList.backPatch(ih.getNext());
        this._falseList.append(this._right._falseList);
        this._trueList.add(ih).append(this._right._trueList);
    }
}
