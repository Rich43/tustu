package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import java.util.List;
import java.util.Vector;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/UnsupportedElement.class */
final class UnsupportedElement extends SyntaxTreeNode {
    private Vector _fallbacks;
    private ErrorMsg _message;
    private boolean _isExtension;

    public UnsupportedElement(String uri, String prefix, String local, boolean isExtension) {
        super(uri, prefix, local);
        this._fallbacks = null;
        this._message = null;
        this._isExtension = false;
        this._isExtension = isExtension;
    }

    public void setErrorMessage(ErrorMsg message) {
        this._message = message;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("Unsupported element = " + this._qname.getNamespace() + CallSiteDescriptor.TOKEN_DELIMITER + this._qname.getLocalPart());
        displayContents(indent + 4);
    }

    private void processFallbacks(Parser parser) {
        List<SyntaxTreeNode> children = getContents();
        if (children != null) {
            int count = children.size();
            for (int i2 = 0; i2 < count; i2++) {
                SyntaxTreeNode child = children.get(i2);
                if (child instanceof Fallback) {
                    Fallback fallback = (Fallback) child;
                    fallback.activate();
                    fallback.parseContents(parser);
                    if (this._fallbacks == null) {
                        this._fallbacks = new Vector();
                    }
                    this._fallbacks.addElement(child);
                }
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        processFallbacks(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._fallbacks != null) {
            int count = this._fallbacks.size();
            for (int i2 = 0; i2 < count; i2++) {
                Fallback fallback = (Fallback) this._fallbacks.elementAt(i2);
                fallback.typeCheck(stable);
            }
        }
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        if (this._fallbacks != null) {
            int count = this._fallbacks.size();
            for (int i2 = 0; i2 < count; i2++) {
                Fallback fallback = (Fallback) this._fallbacks.elementAt(i2);
                fallback.translate(classGen, methodGen);
            }
            return;
        }
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int unsupportedElem = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "unsupported_ElementF", "(Ljava/lang/String;Z)V");
        il.append(new PUSH(cpg, getQName().toString()));
        il.append(new PUSH(cpg, this._isExtension));
        il.append(new INVOKESTATIC(unsupportedElem));
    }
}
