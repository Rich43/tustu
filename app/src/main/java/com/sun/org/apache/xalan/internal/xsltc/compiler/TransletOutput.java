package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/TransletOutput.class */
final class TransletOutput extends Instruction {
    private Expression _filename;
    private boolean _append;

    TransletOutput() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("TransletOutput: " + ((Object) this._filename));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String filename = getAttribute(DeploymentDescriptorParser.ATTR_FILE);
        String append = getAttribute("append");
        if (filename == null || filename.equals("")) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, DeploymentDescriptorParser.ATTR_FILE);
        }
        this._filename = AttributeValue.create(this, filename, parser);
        if (append != null && (append.toLowerCase().equals("yes") || append.toLowerCase().equals("true"))) {
            this._append = true;
        } else {
            this._append = false;
        }
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type type = this._filename.typeCheck(stable);
        if (!(type instanceof StringType)) {
            this._filename = new CastExpr(this._filename, Type.String);
        }
        typeCheckContents(stable);
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        boolean isSecureProcessing = classGen.getParser().getXSLTC().isSecureProcessing();
        if (isSecureProcessing) {
            int index = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "unallowed_extension_elementF", "(Ljava/lang/String;)V");
            il.append(new PUSH(cpg, "redirect"));
            il.append(new INVOKESTATIC(index));
            return;
        }
        il.append(methodGen.loadHandler());
        int open = cpg.addMethodref(Constants.TRANSLET_CLASS, "openOutputHandler", "(Ljava/lang/String;Z)Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
        int close = cpg.addMethodref(Constants.TRANSLET_CLASS, "closeOutputHandler", "(Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
        il.append(classGen.loadTranslet());
        this._filename.translate(classGen, methodGen);
        il.append(new PUSH(cpg, this._append));
        il.append(new INVOKEVIRTUAL(open));
        il.append(methodGen.storeHandler());
        translateContents(classGen, methodGen);
        il.append(classGen.loadTranslet());
        il.append(methodGen.loadHandler());
        il.append(new INVOKEVIRTUAL(close));
        il.append(methodGen.storeHandler());
    }
}
