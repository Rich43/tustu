package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ObjectType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/CastCall.class */
final class CastCall extends FunctionCall {
    private String _className;
    private Expression _right;

    public CastCall(QName fname, Vector arguments) {
        super(fname, arguments);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (argumentCount() != 2) {
            throw new TypeCheckError(new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR, (Object) getName(), (SyntaxTreeNode) this));
        }
        Expression exp = argument(0);
        if (exp instanceof LiteralExpr) {
            this._className = ((LiteralExpr) exp).getValue();
            this._type = Type.newObjectType(this._className);
            this._right = argument(1);
            Type tright = this._right.typeCheck(stable);
            if (tright != Type.Reference && !(tright instanceof ObjectType)) {
                throw new TypeCheckError(new ErrorMsg("DATA_CONVERSION_ERR", tright, this._type, this));
            }
            return this._type;
        }
        throw new TypeCheckError(new ErrorMsg(ErrorMsg.NEED_LITERAL_ERR, (Object) getName(), (SyntaxTreeNode) this));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        this._right.translate(classGen, methodGen);
        il.append(new CHECKCAST(cpg.addClass(this._className)));
    }
}
