package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/DocumentCall.class */
final class DocumentCall extends FunctionCall {
    private Expression _arg1;
    private Expression _arg2;
    private Type _arg1Type;

    public DocumentCall(QName fname, Vector arguments) {
        super(fname, arguments);
        this._arg1 = null;
        this._arg2 = null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        int ac2 = argumentCount();
        if (ac2 < 1 || ac2 > 2) {
            ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR, (SyntaxTreeNode) this);
            throw new TypeCheckError(msg);
        }
        if (getStylesheet() == null) {
            ErrorMsg msg2 = new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR, (SyntaxTreeNode) this);
            throw new TypeCheckError(msg2);
        }
        this._arg1 = argument(0);
        if (this._arg1 == null) {
            ErrorMsg msg3 = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, (SyntaxTreeNode) this);
            throw new TypeCheckError(msg3);
        }
        this._arg1Type = this._arg1.typeCheck(stable);
        if (this._arg1Type != Type.NodeSet && this._arg1Type != Type.String) {
            this._arg1 = new CastExpr(this._arg1, Type.String);
        }
        if (ac2 == 2) {
            this._arg2 = argument(1);
            if (this._arg2 == null) {
                ErrorMsg msg4 = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, (SyntaxTreeNode) this);
                throw new TypeCheckError(msg4);
            }
            Type arg2Type = this._arg2.typeCheck(stable);
            if (arg2Type.identicalTo(Type.Node)) {
                this._arg2 = new CastExpr(this._arg2, Type.NodeSet);
            } else if (!arg2Type.identicalTo(Type.NodeSet)) {
                ErrorMsg msg5 = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, (SyntaxTreeNode) this);
                throw new TypeCheckError(msg5);
            }
        }
        Type type = Type.NodeSet;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        String docParamList;
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int ac2 = argumentCount();
        int domField = cpg.addFieldref(classGen.getClassName(), Constants.DOM_FIELD, Constants.DOM_INTF_SIG);
        if (ac2 == 1) {
            docParamList = "(Ljava/lang/Object;Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;";
        } else {
            docParamList = "(Ljava/lang/Object;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;";
        }
        int docIdx = cpg.addMethodref(Constants.LOAD_DOCUMENT_CLASS, "documentF", docParamList);
        this._arg1.translate(classGen, methodGen);
        if (this._arg1Type == Type.NodeSet) {
            this._arg1.startIterator(classGen, methodGen);
        }
        if (ac2 == 2) {
            this._arg2.translate(classGen, methodGen);
            this._arg2.startIterator(classGen, methodGen);
        }
        il.append(new PUSH(cpg, getStylesheet().getSystemId()));
        il.append(classGen.loadTranslet());
        il.append(DUP);
        il.append(new GETFIELD(domField));
        il.append(new INVOKESTATIC(docIdx));
    }
}
