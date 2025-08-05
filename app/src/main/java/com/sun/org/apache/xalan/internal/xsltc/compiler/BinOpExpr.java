package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import javafx.fxml.FXMLLoader;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/BinOpExpr.class */
final class BinOpExpr extends Expression {
    public static final int PLUS = 0;
    public static final int MINUS = 1;
    public static final int TIMES = 2;
    public static final int DIV = 3;
    public static final int MOD = 4;
    private static final String[] Ops = {Marker.ANY_NON_NULL_MARKER, LanguageTag.SEP, "*", "/", FXMLLoader.RESOURCE_KEY_PREFIX};
    private int _op;
    private Expression _left;
    private Expression _right;

    public BinOpExpr(int op, Expression left, Expression right) {
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

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        this._left.setParser(parser);
        this._right.setParser(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type tleft = this._left.typeCheck(stable);
        Type tright = this._right.typeCheck(stable);
        MethodType ptype = lookupPrimop(stable, Ops[this._op], new MethodType(Type.Void, tleft, tright));
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
        InstructionList il = methodGen.getInstructionList();
        this._left.translate(classGen, methodGen);
        this._right.translate(classGen, methodGen);
        switch (this._op) {
            case 0:
                il.append(this._type.ADD());
                break;
            case 1:
                il.append(this._type.SUB());
                break;
            case 2:
                il.append(this._type.MUL());
                break;
            case 3:
                il.append(this._type.DIV());
                break;
            case 4:
                il.append(this._type.REM());
                break;
            default:
                ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_BINARY_OP_ERR, (SyntaxTreeNode) this);
                getParser().reportError(3, msg);
                break;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return Ops[this._op] + '(' + ((Object) this._left) + ", " + ((Object) this._right) + ')';
    }
}
