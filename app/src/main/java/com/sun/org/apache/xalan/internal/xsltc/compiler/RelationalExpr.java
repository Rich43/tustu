package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Operators;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/RelationalExpr.class */
final class RelationalExpr extends Expression {
    private int _op;
    private Expression _left;
    private Expression _right;

    public RelationalExpr(int op, Expression left, Expression right) {
        this._op = op;
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

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasPositionCall() {
        return this._left.hasPositionCall() || this._right.hasPositionCall();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasLastCall() {
        return this._left.hasLastCall() || this._right.hasLastCall();
    }

    public boolean hasReferenceArgs() {
        return (this._left.getType() instanceof ReferenceType) || (this._right.getType() instanceof ReferenceType);
    }

    public boolean hasNodeArgs() {
        return (this._left.getType() instanceof NodeType) || (this._right.getType() instanceof NodeType);
    }

    public boolean hasNodeSetArgs() {
        return (this._left.getType() instanceof NodeSetType) || (this._right.getType() instanceof NodeSetType);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type type;
        Type tleft = this._left.typeCheck(stable);
        Type tright = this._right.typeCheck(stable);
        if ((tleft instanceof ResultTreeType) && (tright instanceof ResultTreeType)) {
            this._right = new CastExpr(this._right, Type.Real);
            this._left = new CastExpr(this._left, Type.Real);
            Type type2 = Type.Boolean;
            this._type = type2;
            return type2;
        }
        if (hasReferenceArgs()) {
            Type typeL = null;
            Type typeR = null;
            if ((tleft instanceof ReferenceType) && (this._left instanceof VariableRefBase)) {
                VariableRefBase ref = (VariableRefBase) this._left;
                VariableBase var = ref.getVariable();
                typeL = var.getType();
            }
            if ((tright instanceof ReferenceType) && (this._right instanceof VariableRefBase)) {
                VariableRefBase ref2 = (VariableRefBase) this._right;
                VariableBase var2 = ref2.getVariable();
                typeR = var2.getType();
            }
            if (typeL == null) {
                type = typeR;
            } else if (typeR == null) {
                type = typeL;
            } else {
                type = Type.Real;
            }
            if (type == null) {
                type = Type.Real;
            }
            this._right = new CastExpr(this._right, type);
            this._left = new CastExpr(this._left, type);
            Type type3 = Type.Boolean;
            this._type = type3;
            return type3;
        }
        if (hasNodeSetArgs()) {
            if (tright instanceof NodeSetType) {
                Expression temp = this._right;
                this._right = this._left;
                this._left = temp;
                this._op = this._op == 2 ? 3 : this._op == 3 ? 2 : this._op == 4 ? 5 : 4;
                tright = this._right.getType();
            }
            if (tright instanceof NodeType) {
                this._right = new CastExpr(this._right, Type.NodeSet);
            }
            if (tright instanceof IntType) {
                this._right = new CastExpr(this._right, Type.Real);
            }
            if (tright instanceof ResultTreeType) {
                this._right = new CastExpr(this._right, Type.String);
            }
            Type type4 = Type.Boolean;
            this._type = type4;
            return type4;
        }
        if (hasNodeArgs()) {
            if (tleft instanceof BooleanType) {
                this._right = new CastExpr(this._right, Type.Boolean);
                tright = Type.Boolean;
            }
            if (tright instanceof BooleanType) {
                this._left = new CastExpr(this._left, Type.Boolean);
                tleft = Type.Boolean;
            }
        }
        MethodType ptype = lookupPrimop(stable, Operators.getOpNames(this._op), new MethodType(Type.Void, tleft, tright));
        if (ptype != null) {
            Type arg1 = (Type) ptype.argsType().elementAt(0);
            if (!arg1.identicalTo(tleft)) {
                this._left = new CastExpr(this._left, arg1);
            }
            Type arg2 = (Type) ptype.argsType().elementAt(1);
            if (!arg2.identicalTo(tright)) {
                this._right = new CastExpr(this._right, arg1);
            }
            Type typeResultType = ptype.resultType();
            this._type = typeResultType;
            return typeResultType;
        }
        throw new TypeCheckError(this);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        if (hasNodeSetArgs() || hasReferenceArgs()) {
            ConstantPoolGen cpg = classGen.getConstantPool();
            InstructionList il = methodGen.getInstructionList();
            this._left.translate(classGen, methodGen);
            this._left.startIterator(classGen, methodGen);
            this._right.translate(classGen, methodGen);
            this._right.startIterator(classGen, methodGen);
            il.append(new PUSH(cpg, this._op));
            il.append(methodGen.loadDOM());
            int index = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "compare", "(" + this._left.getType().toSignature() + this._right.getType().toSignature() + "I" + Constants.DOM_INTF_SIG + ")Z");
            il.append(new INVOKESTATIC(index));
            return;
        }
        translateDesynthesized(classGen, methodGen);
        synthesize(classGen, methodGen);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
        if (hasNodeSetArgs() || hasReferenceArgs()) {
            translate(classGen, methodGen);
            desynthesize(classGen, methodGen);
            return;
        }
        BranchInstruction bi2 = null;
        InstructionList il = methodGen.getInstructionList();
        this._left.translate(classGen, methodGen);
        this._right.translate(classGen, methodGen);
        boolean tozero = false;
        Type tleft = this._left.getType();
        if (tleft instanceof RealType) {
            il.append(tleft.CMP(this._op == 3 || this._op == 5));
            tleft = Type.Int;
            tozero = true;
        }
        switch (this._op) {
            case 2:
                bi2 = tleft.LE(tozero);
                break;
            case 3:
                bi2 = tleft.GE(tozero);
                break;
            case 4:
                bi2 = tleft.LT(tozero);
                break;
            case 5:
                bi2 = tleft.GT(tozero);
                break;
            default:
                ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_RELAT_OP_ERR, (SyntaxTreeNode) this);
                getParser().reportError(2, msg);
                break;
        }
        this._falseList.add(il.append(bi2));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return Operators.getOpNames(this._op) + '(' + ((Object) this._left) + ", " + ((Object) this._right) + ')';
    }
}
