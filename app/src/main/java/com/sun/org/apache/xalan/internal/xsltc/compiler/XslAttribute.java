package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.serializer.ElemDesc;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/XslAttribute.class */
final class XslAttribute extends Instruction {
    private String _prefix;
    private AttributeValue _name;
    private AttributeValueTemplate _namespace = null;
    private boolean _ignore = false;
    private boolean _isLiteral = false;

    XslAttribute() {
    }

    public AttributeValue getName() {
        return this._name;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("Attribute " + ((Object) this._name));
        displayContents(indent + 4);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        SyntaxTreeNode item;
        boolean generated = false;
        SymbolTable stable = parser.getSymbolTable();
        String name = getAttribute("name");
        String namespace = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NAMESPACE);
        QName qname = parser.getQName(name, false);
        String prefix = qname.getPrefix();
        if ((prefix != null && prefix.equals("xmlns")) || name.equals("xmlns")) {
            reportError(this, parser, ErrorMsg.ILLEGAL_ATTR_NAME_ERR, name);
            return;
        }
        this._isLiteral = Util.isLiteral(name);
        if (this._isLiteral && !XML11Char.isXML11ValidQName(name)) {
            reportError(this, parser, ErrorMsg.ILLEGAL_ATTR_NAME_ERR, name);
            return;
        }
        SyntaxTreeNode parent = getParent();
        List<SyntaxTreeNode> siblings = parent.getContents();
        for (int i2 = 0; i2 < parent.elementCount() && (item = siblings.get(i2)) != this; i2++) {
            if (!(item instanceof XslAttribute) && !(item instanceof UseAttributeSets) && !(item instanceof LiteralAttribute) && !(item instanceof Text) && !(item instanceof If) && !(item instanceof Choose) && !(item instanceof CopyOf) && !(item instanceof VariableBase)) {
                reportWarning(this, parser, "STRAY_ATTRIBUTE_ERR", name);
            }
        }
        if (namespace != null && namespace != "") {
            this._prefix = lookupPrefix(namespace);
            this._namespace = new AttributeValueTemplate(namespace, parser, this);
        } else if (prefix != null && prefix != "") {
            this._prefix = prefix;
            namespace = lookupNamespace(prefix);
            if (namespace != null) {
                this._namespace = new AttributeValueTemplate(namespace, parser, this);
            }
        }
        if (this._namespace != null) {
            if (this._prefix == null || this._prefix == "") {
                if (prefix != null) {
                    this._prefix = prefix;
                } else {
                    this._prefix = stable.generateNamespacePrefix();
                    generated = true;
                }
            } else if (prefix != null && !prefix.equals(this._prefix)) {
                this._prefix = prefix;
            }
            name = this._prefix + CallSiteDescriptor.TOKEN_DELIMITER + qname.getLocalPart();
            if ((parent instanceof LiteralElement) && !generated) {
                ((LiteralElement) parent).registerNamespace(this._prefix, namespace, stable, false);
            }
        }
        if (parent instanceof LiteralElement) {
            ((LiteralElement) parent).addAttribute(this);
        }
        this._name = AttributeValue.create(this, name, parser);
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (!this._ignore) {
            this._name.typeCheck(stable);
            if (this._namespace != null) {
                this._namespace.typeCheck(stable);
            }
            typeCheckContents(stable);
        }
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._ignore) {
            return;
        }
        this._ignore = true;
        if (this._namespace != null) {
            il.append(methodGen.loadHandler());
            il.append(new PUSH(cpg, this._prefix));
            this._namespace.translate(classGen, methodGen);
            il.append(methodGen.namespace());
        }
        if (!this._isLiteral) {
            LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType(Constants.STRING_SIG), null);
            this._name.translate(classGen, methodGen);
            nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
            il.append(new ALOAD(nameValue.getIndex()));
            int check = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "checkAttribQName", "(Ljava/lang/String;)V");
            il.append(new INVOKESTATIC(check));
            il.append(methodGen.loadHandler());
            il.append(DUP);
            nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));
        } else {
            il.append(methodGen.loadHandler());
            il.append(DUP);
            this._name.translate(classGen, methodGen);
        }
        if (elementCount() == 1 && (elementAt(0) instanceof Text)) {
            il.append(new PUSH(cpg, ((Text) elementAt(0)).getText()));
        } else {
            il.append(classGen.loadTranslet());
            il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, "stringValueHandler", Constants.STRING_VALUE_HANDLER_SIG)));
            il.append(DUP);
            il.append(methodGen.storeHandler());
            translateContents(classGen, methodGen);
            il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.STRING_VALUE_HANDLER, "getValue", "()Ljava/lang/String;")));
        }
        SyntaxTreeNode parent = getParent();
        if ((parent instanceof LiteralElement) && ((LiteralElement) parent).allAttributesUnique()) {
            int flags = 0;
            ElemDesc elemDesc = ((LiteralElement) parent).getElemDesc();
            if (elemDesc != null && (this._name instanceof SimpleAttributeValue)) {
                String attrName = ((SimpleAttributeValue) this._name).toString();
                if (elemDesc.isAttrFlagSet(attrName, 4)) {
                    flags = 0 | 2;
                } else if (elemDesc.isAttrFlagSet(attrName, 2)) {
                    flags = 0 | 4;
                }
            }
            il.append(new PUSH(cpg, flags));
            il.append(methodGen.uniqueAttribute());
        } else {
            il.append(methodGen.attribute());
        }
        il.append(methodGen.storeHandler());
    }
}
