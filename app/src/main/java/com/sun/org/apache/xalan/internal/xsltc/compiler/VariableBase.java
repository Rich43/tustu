package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import java.util.Vector;
import javax.swing.plaf.basic.BasicRootPaneUI;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/VariableBase.class */
class VariableBase extends TopLevelElement {
    protected QName _name;
    protected String _escapedName;
    protected Type _type;
    protected boolean _isLocal;
    protected LocalVariableGen _local;
    protected com.sun.org.apache.bcel.internal.generic.Instruction _loadInstruction;
    protected com.sun.org.apache.bcel.internal.generic.Instruction _storeInstruction;
    protected Expression _select;
    protected String select;
    protected Vector<VariableRefBase> _refs = new Vector<>(2);
    protected boolean _ignore = false;

    VariableBase() {
    }

    public void disable() {
        this._ignore = true;
    }

    public void addReference(VariableRefBase vref) {
        this._refs.addElement(vref);
    }

    public void copyReferences(VariableBase var) {
        int size = this._refs.size();
        for (int i2 = 0; i2 < size; i2++) {
            var.addReference(this._refs.get(i2));
        }
    }

    public void mapRegister(MethodGenerator methodGen) {
        if (this._local == null) {
            InstructionList il = methodGen.getInstructionList();
            String name = getEscapedName();
            com.sun.org.apache.bcel.internal.generic.Type varType = this._type.toJCType();
            this._local = methodGen.addLocalVariable2(name, varType, il.getEnd());
        }
    }

    public void unmapRegister(ClassGenerator classGen, MethodGenerator methodGen) {
        if (this._local != null) {
            if (this._type instanceof ResultTreeType) {
                ConstantPoolGen cpg = classGen.getConstantPool();
                InstructionList il = methodGen.getInstructionList();
                if (classGen.getStylesheet().callsNodeset() && classGen.getDOMClass().equals(Constants.MULTI_DOM_CLASS)) {
                    int removeDA = cpg.addMethodref(Constants.MULTI_DOM_CLASS, "removeDOMAdapter", "(Lcom/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter;)V");
                    il.append(methodGen.loadDOM());
                    il.append(new CHECKCAST(cpg.addClass(Constants.MULTI_DOM_CLASS)));
                    il.append(loadInstruction());
                    il.append(new CHECKCAST(cpg.addClass(Constants.DOM_ADAPTER_CLASS)));
                    il.append(new INVOKEVIRTUAL(removeDA));
                }
                int release = cpg.addInterfaceMethodref(Constants.DOM_IMPL_CLASS, BasicRootPaneUI.Actions.RELEASE, "()V");
                il.append(loadInstruction());
                il.append(new INVOKEINTERFACE(release, 1));
            }
            this._local.setEnd(methodGen.getInstructionList().getEnd());
            methodGen.removeLocalVariable(this._local);
            this._refs = null;
            this._local = null;
        }
    }

    public com.sun.org.apache.bcel.internal.generic.Instruction loadInstruction() {
        if (this._loadInstruction == null) {
            this._loadInstruction = this._type.LOAD(this._local.getIndex());
        }
        return this._loadInstruction;
    }

    public com.sun.org.apache.bcel.internal.generic.Instruction storeInstruction() {
        if (this._storeInstruction == null) {
            this._storeInstruction = this._type.STORE(this._local.getIndex());
        }
        return this._storeInstruction;
    }

    public Expression getExpression() {
        return this._select;
    }

    public String toString() {
        return "variable(" + ((Object) this._name) + ")";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        System.out.println("Variable " + ((Object) this._name));
        if (this._select != null) {
            indent(indent + 4);
            System.out.println("select " + this._select.toString());
        }
        displayContents(indent + 4);
    }

    public Type getType() {
        return this._type;
    }

    public QName getName() {
        return this._name;
    }

    public String getEscapedName() {
        return this._escapedName;
    }

    public void setName(QName name) {
        this._name = name;
        this._escapedName = Util.escape(name.getStringRep());
    }

    public boolean isLocal() {
        return this._isLocal;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String name = getAttribute("name");
        if (name.length() > 0) {
            if (!XML11Char.isXML11ValidQName(name)) {
                ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) name, (SyntaxTreeNode) this);
                parser.reportError(3, err);
            }
            setName(parser.getQNameIgnoreDefaultNs(name));
        } else {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "name");
        }
        VariableBase other = parser.lookupVariable(this._name);
        if (other != null && other.getParent() == getParent()) {
            reportError(this, parser, ErrorMsg.VARIABLE_REDEF_ERR, name);
        }
        this.select = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT);
        if (this.select.length() > 0) {
            this._select = getParser().parseExpression(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, null);
            if (this._select.isDummy()) {
                reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT);
                return;
            }
        }
        parseChildren(parser);
    }

    public void translateValue(ClassGenerator classGen, MethodGenerator methodGen) {
        if (this._select != null) {
            this._select.translate(classGen, methodGen);
            if (this._select.getType() instanceof NodeSetType) {
                ConstantPoolGen cpg = classGen.getConstantPool();
                InstructionList il = methodGen.getInstructionList();
                int initCNI = cpg.addMethodref(Constants.CACHED_NODE_LIST_ITERATOR_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
                il.append(new NEW(cpg.addClass(Constants.CACHED_NODE_LIST_ITERATOR_CLASS)));
                il.append(DUP_X1);
                il.append(SWAP);
                il.append(new INVOKESPECIAL(initCNI));
            }
            this._select.startIterator(classGen, methodGen);
            return;
        }
        if (hasContents()) {
            compileResultTree(classGen, methodGen);
        } else {
            methodGen.getInstructionList().append(new PUSH(classGen.getConstantPool(), ""));
        }
    }
}
