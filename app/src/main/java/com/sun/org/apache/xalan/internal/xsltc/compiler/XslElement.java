package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.utils.XML11Char;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/XslElement.class */
final class XslElement extends Instruction {
    private String _prefix;
    private boolean _ignore = false;
    private boolean _isLiteralName = true;
    private AttributeValueTemplate _name;
    private AttributeValueTemplate _namespace;

    XslElement() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("Element " + ((Object) this._name));
        displayContents(indent + 4);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        SymbolTable stable = parser.getSymbolTable();
        String name = getAttribute("name");
        if (name == "") {
            ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ELEM_NAME_ERR, (Object) name, (SyntaxTreeNode) this);
            parser.reportError(4, msg);
            parseChildren(parser);
            this._ignore = true;
            return;
        }
        String namespace = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NAMESPACE);
        this._isLiteralName = Util.isLiteral(name);
        if (this._isLiteralName) {
            if (!XML11Char.isXML11ValidQName(name)) {
                ErrorMsg msg2 = new ErrorMsg(ErrorMsg.ILLEGAL_ELEM_NAME_ERR, (Object) name, (SyntaxTreeNode) this);
                parser.reportError(4, msg2);
                parseChildren(parser);
                this._ignore = true;
                return;
            }
            QName qname = parser.getQNameSafe(name);
            String prefix = qname.getPrefix();
            String local = qname.getLocalPart();
            if (prefix == null) {
                prefix = "";
            }
            if (!hasAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NAMESPACE)) {
                String namespace2 = lookupNamespace(prefix);
                if (namespace2 == null) {
                    ErrorMsg err = new ErrorMsg(ErrorMsg.NAMESPACE_UNDEF_ERR, (Object) prefix, (SyntaxTreeNode) this);
                    parser.reportError(4, err);
                    parseChildren(parser);
                    this._ignore = true;
                    return;
                }
                this._prefix = prefix;
                this._namespace = new AttributeValueTemplate(namespace2, parser, this);
            } else {
                if (prefix == "") {
                    if (Util.isLiteral(namespace)) {
                        prefix = lookupPrefix(namespace);
                        if (prefix == null) {
                            prefix = stable.generateNamespacePrefix();
                        }
                    }
                    StringBuffer newName = new StringBuffer(prefix);
                    if (prefix != "") {
                        newName.append(':');
                    }
                    name = newName.append(local).toString();
                }
                this._prefix = prefix;
                this._namespace = new AttributeValueTemplate(namespace, parser, this);
            }
        } else {
            this._namespace = namespace == "" ? null : new AttributeValueTemplate(namespace, parser, this);
        }
        this._name = new AttributeValueTemplate(name, parser, this);
        String useSets = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_USEATTRIBUTESETS);
        if (useSets.length() > 0) {
            if (!Util.isValidQNames(useSets)) {
                ErrorMsg err2 = new ErrorMsg("INVALID_QNAME_ERR", (Object) useSets, (SyntaxTreeNode) this);
                parser.reportError(3, err2);
            }
            setFirstElement(new UseAttributeSets(useSets, parser));
        }
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (!this._ignore) {
            this._name.typeCheck(stable);
            if (this._namespace != null) {
                this._namespace.typeCheck(stable);
            }
        }
        typeCheckContents(stable);
        return Type.Void;
    }

    public void translateLiteral(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (!this._ignore) {
            il.append(methodGen.loadHandler());
            this._name.translate(classGen, methodGen);
            il.append(DUP2);
            il.append(methodGen.startElement());
            if (this._namespace != null) {
                il.append(methodGen.loadHandler());
                il.append(new PUSH(cpg, this._prefix));
                this._namespace.translate(classGen, methodGen);
                il.append(methodGen.namespace());
            }
        }
        translateContents(classGen, methodGen);
        if (!this._ignore) {
            il.append(methodGen.endElement());
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._isLiteralName) {
            translateLiteral(classGen, methodGen);
            return;
        }
        if (!this._ignore) {
            LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType(Constants.STRING_SIG), null);
            this._name.translate(classGen, methodGen);
            nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
            il.append(new ALOAD(nameValue.getIndex()));
            int check = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "checkQName", "(Ljava/lang/String;)V");
            il.append(new INVOKESTATIC(check));
            il.append(methodGen.loadHandler());
            nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));
            if (this._namespace != null) {
                this._namespace.translate(classGen, methodGen);
            } else {
                il.append(ACONST_NULL);
            }
            il.append(methodGen.loadHandler());
            il.append(methodGen.loadDOM());
            il.append(methodGen.loadCurrentNode());
            il.append(new INVOKESTATIC(cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "startXslElement", "(Ljava/lang/String;Ljava/lang/String;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;I)Ljava/lang/String;")));
        }
        translateContents(classGen, methodGen);
        if (!this._ignore) {
            il.append(methodGen.endElement());
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translateContents(ClassGenerator classGen, MethodGenerator methodGen) {
        int n2 = elementCount();
        for (int i2 = 0; i2 < n2; i2++) {
            SyntaxTreeNode item = getContents().get(i2);
            if (!this._ignore || !(item instanceof XslAttribute)) {
                item.translate(classGen, methodGen);
            }
        }
    }
}
