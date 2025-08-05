package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.IFNE;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPNE;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NumberType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Operators;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/EqualityExpr.class */
final class EqualityExpr extends Expression {
    private final int _op;
    private Expression _left;
    private Expression _right;

    public EqualityExpr(int op, Expression left, Expression right) {
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
    public String toString() {
        return Operators.getOpNames(this._op) + '(' + ((Object) this._left) + ", " + ((Object) this._right) + ')';
    }

    public Expression getLeft() {
        return this._left;
    }

    public Expression getRight() {
        return this._right;
    }

    public boolean getOp() {
        return this._op != 1;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasPositionCall() {
        return this._left.hasPositionCall() || this._right.hasPositionCall();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasLastCall() {
        return this._left.hasLastCall() || this._right.hasLastCall();
    }

    private void swapArguments() {
        Expression temp = this._left;
        this._left = this._right;
        this._right = temp;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type tleft = this._left.typeCheck(stable);
        Type tright = this._right.typeCheck(stable);
        if (tleft.isSimple() && tright.isSimple()) {
            if (tleft != tright) {
                if (tleft instanceof BooleanType) {
                    this._right = new CastExpr(this._right, Type.Boolean);
                } else if (tright instanceof BooleanType) {
                    this._left = new CastExpr(this._left, Type.Boolean);
                } else if ((tleft instanceof NumberType) || (tright instanceof NumberType)) {
                    this._left = new CastExpr(this._left, Type.Real);
                    this._right = new CastExpr(this._right, Type.Real);
                } else {
                    this._left = new CastExpr(this._left, Type.String);
                    this._right = new CastExpr(this._right, Type.String);
                }
            }
        } else if (tleft instanceof ReferenceType) {
            this._right = new CastExpr(this._right, Type.Reference);
        } else if (tright instanceof ReferenceType) {
            this._left = new CastExpr(this._left, Type.Reference);
        } else if ((tleft instanceof NodeType) && tright == Type.String) {
            this._left = new CastExpr(this._left, Type.String);
        } else if (tleft == Type.String && (tright instanceof NodeType)) {
            this._right = new CastExpr(this._right, Type.String);
        } else if ((tleft instanceof NodeType) && (tright instanceof NodeType)) {
            this._left = new CastExpr(this._left, Type.String);
            this._right = new CastExpr(this._right, Type.String);
        } else if (!(tleft instanceof NodeType) || !(tright instanceof NodeSetType)) {
            if ((tleft instanceof NodeSetType) && (tright instanceof NodeType)) {
                swapArguments();
            } else {
                if (tleft instanceof NodeType) {
                    this._left = new CastExpr(this._left, Type.NodeSet);
                }
                if (tright instanceof NodeType) {
                    this._right = new CastExpr(this._right, Type.NodeSet);
                }
                if (tleft.isSimple() || ((tleft instanceof ResultTreeType) && (tright instanceof NodeSetType))) {
                    swapArguments();
                }
                if (this._right.getType() instanceof IntType) {
                    this._right = new CastExpr(this._right, Type.Real);
                }
            }
        }
        Type type = Type.Boolean;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
        Type tleft = this._left.getType();
        InstructionList il = methodGen.getInstructionList();
        if (tleft instanceof BooleanType) {
            this._left.translate(classGen, methodGen);
            this._right.translate(classGen, methodGen);
            this._falseList.add(il.append(this._op == 0 ? new IF_ICMPNE(null) : new IF_ICMPEQ(null)));
        } else {
            if (tleft instanceof NumberType) {
                this._left.translate(classGen, methodGen);
                this._right.translate(classGen, methodGen);
                if (tleft instanceof RealType) {
                    il.append(DCMPG);
                    this._falseList.add(il.append(this._op == 0 ? new IFNE(null) : new IFEQ(null)));
                    return;
                } else {
                    this._falseList.add(il.append(this._op == 0 ? new IF_ICMPNE(null) : new IF_ICMPEQ(null)));
                    return;
                }
            }
            translate(classGen, methodGen);
            desynthesize(classGen, methodGen);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        Type tleft = this._left.getType();
        Type tright = this._right.getType();
        if ((tleft instanceof BooleanType) || (tleft instanceof NumberType)) {
            translateDesynthesized(classGen, methodGen);
            synthesize(classGen, methodGen);
            return;
        }
        if (tleft instanceof StringType) {
            int equals = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
            this._left.translate(classGen, methodGen);
            this._right.translate(classGen, methodGen);
            il.append(new INVOKEVIRTUAL(equals));
            if (this._op == 1) {
                il.append(ICONST_1);
                il.append(IXOR);
                return;
            }
            return;
        }
        if (tleft instanceof ResultTreeType) {
            if (tright instanceof BooleanType) {
                this._right.translate(classGen, methodGen);
                if (this._op == 1) {
                    il.append(ICONST_1);
                    il.append(IXOR);
                    return;
                }
                return;
            }
            if (tright instanceof RealType) {
                this._left.translate(classGen, methodGen);
                tleft.translateTo(classGen, methodGen, Type.Real);
                this._right.translate(classGen, methodGen);
                il.append(DCMPG);
                BranchHandle falsec = il.append(this._op == 0 ? new IFNE(null) : new IFEQ(null));
                il.append(ICONST_1);
                BranchHandle truec = il.append((BranchInstruction) new GOTO(null));
                falsec.setTarget(il.append(ICONST_0));
                truec.setTarget(il.append(NOP));
                return;
            }
            this._left.translate(classGen, methodGen);
            tleft.translateTo(classGen, methodGen, Type.String);
            this._right.translate(classGen, methodGen);
            if (tright instanceof ResultTreeType) {
                tright.translateTo(classGen, methodGen, Type.String);
            }
            int equals2 = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
            il.append(new INVOKEVIRTUAL(equals2));
            if (this._op == 1) {
                il.append(ICONST_1);
                il.append(IXOR);
                return;
            }
            return;
        }
        if ((tleft instanceof NodeSetType) && (tright instanceof BooleanType)) {
            this._left.translate(classGen, methodGen);
            this._left.startIterator(classGen, methodGen);
            Type.NodeSet.translateTo(classGen, methodGen, Type.Boolean);
            this._right.translate(classGen, methodGen);
            il.append(IXOR);
            if (this._op == 0) {
                il.append(ICONST_1);
                il.append(IXOR);
                return;
            }
            return;
        }
        if ((tleft instanceof NodeSetType) && (tright instanceof StringType)) {
            this._left.translate(classGen, methodGen);
            this._left.startIterator(classGen, methodGen);
            this._right.translate(classGen, methodGen);
            il.append(new PUSH(cpg, this._op));
            il.append(methodGen.loadDOM());
            int cmp = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "compare", "(" + tleft.toSignature() + tright.toSignature() + "I" + Constants.DOM_INTF_SIG + ")Z");
            il.append(new INVOKESTATIC(cmp));
            return;
        }
        this._left.translate(classGen, methodGen);
        this._left.startIterator(classGen, methodGen);
        this._right.translate(classGen, methodGen);
        this._right.startIterator(classGen, methodGen);
        if (tright instanceof ResultTreeType) {
            tright.translateTo(classGen, methodGen, Type.String);
            tright = Type.String;
        }
        il.append(new PUSH(cpg, this._op));
        il.append(methodGen.loadDOM());
        int compare = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "compare", "(" + tleft.toSignature() + tright.toSignature() + "I" + Constants.DOM_INTF_SIG + ")Z");
        il.append(new INVOKESTATIC(compare));
    }
}
