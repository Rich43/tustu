package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ValueOf.class */
final class ValueOf extends Instruction {
    private Expression _select;
    private boolean _escaping = true;
    private boolean _isString = false;

    ValueOf() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("ValueOf");
        indent(indent + 4);
        Util.println("select " + this._select.toString());
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        this._select = parser.parseExpression(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, null);
        if (this._select.isDummy()) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT);
            return;
        }
        String str = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DISABLE_OUTPUT_ESCAPING);
        if (str == null || !str.equals("yes")) {
            return;
        }
        this._escaping = false;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type type = this._select.typeCheck(stable);
        if (type != null && !type.identicalTo(Type.Node)) {
            if (type.identicalTo(Type.NodeSet)) {
                this._select = new CastExpr(this._select, Type.Node);
            } else {
                this._isString = true;
                if (!type.identicalTo(Type.String)) {
                    this._select = new CastExpr(this._select, Type.String);
                }
                this._isString = true;
            }
        }
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int setEscaping = cpg.addInterfaceMethodref(Constants.OUTPUT_HANDLER, "setEscaping", "(Z)Z");
        if (!this._escaping) {
            il.append(methodGen.loadHandler());
            il.append(new PUSH(cpg, false));
            il.append(new INVOKEINTERFACE(setEscaping, 2));
        }
        if (this._isString) {
            int characters = cpg.addMethodref(Constants.TRANSLET_CLASS, "characters", Constants.CHARACTERSW_SIG);
            il.append(classGen.loadTranslet());
            this._select.translate(classGen, methodGen);
            il.append(methodGen.loadHandler());
            il.append(new INVOKEVIRTUAL(characters));
        } else {
            int characters2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, "characters", Constants.CHARACTERS_SIG);
            il.append(methodGen.loadDOM());
            this._select.translate(classGen, methodGen);
            il.append(methodGen.loadHandler());
            il.append(new INVOKEINTERFACE(characters2, 3));
        }
        if (!this._escaping) {
            il.append(methodGen.loadHandler());
            il.append(SWAP);
            il.append(new INVOKEINTERFACE(setEscaping, 2));
            il.append(POP);
        }
    }
}
