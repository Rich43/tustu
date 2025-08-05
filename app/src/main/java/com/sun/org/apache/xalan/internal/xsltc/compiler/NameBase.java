package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/NameBase.class */
class NameBase extends FunctionCall {
    private Expression _param;
    private Type _paramType;

    public NameBase(QName fname) {
        super(fname);
        this._param = null;
        this._paramType = Type.Node;
    }

    public NameBase(QName fname, Vector arguments) {
        super(fname, arguments);
        this._param = null;
        this._paramType = Type.Node;
        this._param = argument(0);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        switch (argumentCount()) {
            case 0:
                this._paramType = Type.Node;
                break;
            case 1:
                this._paramType = this._param.typeCheck(stable);
                break;
            default:
                throw new TypeCheckError(this);
        }
        if (this._paramType != Type.NodeSet && this._paramType != Type.Node && this._paramType != Type.Reference) {
            throw new TypeCheckError(this);
        }
        Type type = Type.String;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public Type getType() {
        return this._type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(methodGen.loadDOM());
        if (argumentCount() == 0) {
            il.append(methodGen.loadContextNode());
            return;
        }
        if (this._paramType == Type.Node) {
            this._param.translate(classGen, methodGen);
            return;
        }
        if (this._paramType == Type.Reference) {
            this._param.translate(classGen, methodGen);
            il.append(new INVOKESTATIC(cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "referenceToNodeSet", "(Ljava/lang/Object;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;")));
            il.append(methodGen.nextNode());
        } else {
            this._param.translate(classGen, methodGen);
            this._param.startIterator(classGen, methodGen);
            il.append(methodGen.nextNode());
        }
    }
}
