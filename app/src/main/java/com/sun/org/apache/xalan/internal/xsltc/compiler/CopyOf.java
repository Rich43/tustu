package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/CopyOf.class */
final class CopyOf extends Instruction {
    private Expression _select;

    CopyOf() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("CopyOf");
        indent(indent + 4);
        Util.println("select " + this._select.toString());
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        this._select = parser.parseExpression(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, null);
        if (this._select.isDummy()) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type tselect = this._select.typeCheck(stable);
        if (!(tselect instanceof NodeType) && !(tselect instanceof NodeSetType) && !(tselect instanceof ReferenceType) && !(tselect instanceof ResultTreeType)) {
            this._select = new CastExpr(this._select, Type.String);
        }
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        Type tselect = this._select.getType();
        int cpy1 = cpg.addInterfaceMethodref(Constants.DOM_INTF, "copy", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
        int cpy2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, "copy", Constants.CHARACTERS_SIG);
        int getDoc = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getDocument", "()I");
        if (tselect instanceof NodeSetType) {
            il.append(methodGen.loadDOM());
            this._select.translate(classGen, methodGen);
            this._select.startIterator(classGen, methodGen);
            il.append(methodGen.loadHandler());
            il.append(new INVOKEINTERFACE(cpy1, 3));
            return;
        }
        if (tselect instanceof NodeType) {
            il.append(methodGen.loadDOM());
            this._select.translate(classGen, methodGen);
            il.append(methodGen.loadHandler());
            il.append(new INVOKEINTERFACE(cpy2, 3));
            return;
        }
        if (tselect instanceof ResultTreeType) {
            this._select.translate(classGen, methodGen);
            il.append(DUP);
            il.append(new INVOKEINTERFACE(getDoc, 1));
            il.append(methodGen.loadHandler());
            il.append(new INVOKEINTERFACE(cpy2, 3));
            return;
        }
        if (tselect instanceof ReferenceType) {
            this._select.translate(classGen, methodGen);
            il.append(methodGen.loadHandler());
            il.append(methodGen.loadCurrentNode());
            il.append(methodGen.loadDOM());
            int copy = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "copy", "(Ljava/lang/Object;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;ILcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
            il.append(new INVOKESTATIC(copy));
            return;
        }
        il.append(classGen.loadTranslet());
        this._select.translate(classGen, methodGen);
        il.append(methodGen.loadHandler());
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.TRANSLET_CLASS, "characters", Constants.CHARACTERSW_SIG)));
    }
}
