package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.utils.XML11Char;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ProcessingInstruction.class */
final class ProcessingInstruction extends Instruction {
    private AttributeValue _name;
    private boolean _isLiteral = false;

    ProcessingInstruction() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String name = getAttribute("name");
        if (name.length() > 0) {
            this._isLiteral = Util.isLiteral(name);
            if (this._isLiteral && !XML11Char.isXML11ValidNCName(name)) {
                ErrorMsg err = new ErrorMsg("INVALID_NCNAME_ERR", (Object) name, (SyntaxTreeNode) this);
                parser.reportError(3, err);
            }
            this._name = AttributeValue.create(this, name, parser);
        } else {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "name");
        }
        if (name.equals("xml")) {
            reportError(this, parser, ErrorMsg.ILLEGAL_PI_ERR, "xml");
        }
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        this._name.typeCheck(stable);
        typeCheckContents(stable);
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (!this._isLiteral) {
            LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType(Constants.STRING_SIG), null);
            this._name.translate(classGen, methodGen);
            nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
            il.append(new ALOAD(nameValue.getIndex()));
            int check = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "checkNCName", "(Ljava/lang/String;)V");
            il.append(new INVOKESTATIC(check));
            il.append(methodGen.loadHandler());
            il.append(DUP);
            nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));
        } else {
            il.append(methodGen.loadHandler());
            il.append(DUP);
            this._name.translate(classGen, methodGen);
        }
        il.append(classGen.loadTranslet());
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, "stringValueHandler", Constants.STRING_VALUE_HANDLER_SIG)));
        il.append(DUP);
        il.append(methodGen.storeHandler());
        translateContents(classGen, methodGen);
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.STRING_VALUE_HANDLER, "getValueOfPI", "()Ljava/lang/String;")));
        int processingInstruction = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "processingInstruction", "(Ljava/lang/String;Ljava/lang/String;)V");
        il.append(new INVOKEINTERFACE(processingInstruction, 3));
        il.append(methodGen.storeHandler());
    }
}
