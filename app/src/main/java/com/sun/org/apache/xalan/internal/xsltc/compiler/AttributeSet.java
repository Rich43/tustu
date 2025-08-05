package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.AttributeSetMethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/AttributeSet.class */
final class AttributeSet extends TopLevelElement {
    private static final String AttributeSetPrefix = "$as$";
    private QName _name;
    private UseAttributeSets _useSets;
    private AttributeSet _mergeSet;
    private String _method;
    private boolean _ignore = false;

    AttributeSet() {
    }

    public QName getName() {
        return this._name;
    }

    public String getMethodName() {
        return this._method;
    }

    public void ignore() {
        this._ignore = true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String name = getAttribute("name");
        if (!XML11Char.isXML11ValidQName(name)) {
            ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) name, (SyntaxTreeNode) this);
            parser.reportError(3, err);
        }
        this._name = parser.getQNameIgnoreDefaultNs(name);
        if (this._name == null || this._name.equals("")) {
            ErrorMsg msg = new ErrorMsg(ErrorMsg.UNNAMED_ATTRIBSET_ERR, (SyntaxTreeNode) this);
            parser.reportError(3, msg);
        }
        String useSets = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_USEATTRIBUTESETS);
        if (useSets.length() > 0) {
            if (!Util.isValidQNames(useSets)) {
                ErrorMsg err2 = new ErrorMsg("INVALID_QNAME_ERR", (Object) useSets, (SyntaxTreeNode) this);
                parser.reportError(3, err2);
            }
            this._useSets = new UseAttributeSets(useSets, parser);
        }
        List<SyntaxTreeNode> contents = getContents();
        int count = contents.size();
        for (int i2 = 0; i2 < count; i2++) {
            SyntaxTreeNode child = contents.get(i2);
            if (child instanceof XslAttribute) {
                parser.getSymbolTable().setCurrentNode(child);
                child.parseContents(parser);
            } else if (!(child instanceof Text)) {
                ErrorMsg msg2 = new ErrorMsg(ErrorMsg.ILLEGAL_CHILD_ERR, (SyntaxTreeNode) this);
                parser.reportError(3, msg2);
            }
        }
        parser.getSymbolTable().setCurrentNode(this);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._ignore) {
            return Type.Void;
        }
        this._mergeSet = stable.addAttributeSet(this);
        this._method = AttributeSetPrefix + getXSLTC().nextAttributeSetSerial();
        if (this._useSets != null) {
            this._useSets.typeCheck(stable);
        }
        typeCheckContents(stable);
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        if (this._ignore) {
            return;
        }
        MethodGenerator methodGen2 = new AttributeSetMethodGenerator(this._method, classGen);
        if (this._mergeSet != null) {
            ConstantPoolGen cpg = classGen.getConstantPool();
            InstructionList il = methodGen2.getInstructionList();
            String methodName = this._mergeSet.getMethodName();
            il.append(classGen.loadTranslet());
            il.append(methodGen2.loadDOM());
            il.append(methodGen2.loadIterator());
            il.append(methodGen2.loadHandler());
            il.append(methodGen2.loadCurrentNode());
            int method = cpg.addMethodref(classGen.getClassName(), methodName, Constants.ATTR_SET_SIG);
            il.append(new INVOKESPECIAL(method));
        }
        if (this._useSets != null) {
            this._useSets.translate(classGen, methodGen2);
        }
        Iterator<SyntaxTreeNode> attributes = elements();
        while (attributes.hasNext()) {
            SyntaxTreeNode element = attributes.next();
            if (element instanceof XslAttribute) {
                XslAttribute attribute = (XslAttribute) element;
                attribute.translate(classGen, methodGen2);
            }
        }
        methodGen2.getInstructionList().append(RETURN);
        classGen.addMethod(methodGen2);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("attribute-set: ");
        Iterator<SyntaxTreeNode> attributes = elements();
        while (attributes.hasNext()) {
            XslAttribute attribute = (XslAttribute) attributes.next();
            buf.append((Object) attribute);
        }
        return buf.toString();
    }
}
