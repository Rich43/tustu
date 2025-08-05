package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.serializer.ElemDesc;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/LiteralAttribute.class */
final class LiteralAttribute extends Instruction {
    private final String _name;
    private final AttributeValue _value;

    public LiteralAttribute(String name, String value, Parser parser, SyntaxTreeNode parent) {
        this._name = name;
        setParent(parent);
        this._value = AttributeValue.create(this, value, parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("LiteralAttribute name=" + this._name + " value=" + ((Object) this._value));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        this._value.typeCheck(stable);
        typeCheckContents(stable);
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    protected boolean contextDependent() {
        return this._value.contextDependent();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(methodGen.loadHandler());
        il.append(new PUSH(cpg, this._name));
        this._value.translate(classGen, methodGen);
        SyntaxTreeNode parent = getParent();
        if ((parent instanceof LiteralElement) && ((LiteralElement) parent).allAttributesUnique()) {
            int flags = 0;
            boolean isHTMLAttrEmpty = false;
            ElemDesc elemDesc = ((LiteralElement) parent).getElemDesc();
            if (elemDesc != null) {
                if (elemDesc.isAttrFlagSet(this._name, 4)) {
                    flags = 0 | 2;
                    isHTMLAttrEmpty = true;
                } else if (elemDesc.isAttrFlagSet(this._name, 2)) {
                    flags = 0 | 4;
                }
            }
            if (this._value instanceof SimpleAttributeValue) {
                String attrValue = ((SimpleAttributeValue) this._value).toString();
                if (!hasBadChars(attrValue) && !isHTMLAttrEmpty) {
                    flags |= 1;
                }
            }
            il.append(new PUSH(cpg, flags));
            il.append(methodGen.uniqueAttribute());
            return;
        }
        il.append(methodGen.attribute());
    }

    private boolean hasBadChars(String value) {
        char[] chars = value.toCharArray();
        for (char ch : chars) {
            if (ch < ' ' || '~' < ch || ch == '<' || ch == '>' || ch == '&' || ch == '\"') {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return this._name;
    }

    public AttributeValue getValue() {
        return this._value;
    }
}
