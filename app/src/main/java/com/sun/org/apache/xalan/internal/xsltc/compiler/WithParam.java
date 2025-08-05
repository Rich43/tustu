package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import javax.swing.plaf.basic.BasicRootPaneUI;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/WithParam.class */
final class WithParam extends Instruction {
    private QName _name;
    protected String _escapedName;
    private Expression _select;
    private LocalVariableGen _domAdapter;
    private boolean _doParameterOptimization = false;

    WithParam() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("with-param " + ((Object) this._name));
        if (this._select != null) {
            indent(indent + 4);
            Util.println("select " + this._select.toString());
        }
        displayContents(indent + 4);
    }

    public String getEscapedName() {
        return this._escapedName;
    }

    public QName getName() {
        return this._name;
    }

    public void setName(QName name) {
        this._name = name;
        this._escapedName = Util.escape(name.getStringRep());
    }

    public void setDoParameterOptimization(boolean flag) {
        this._doParameterOptimization = flag;
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
        String select = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT);
        if (select.length() > 0) {
            this._select = parser.parseExpression(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, null);
        }
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._select != null) {
            Type tselect = this._select.typeCheck(stable);
            if (!(tselect instanceof ReferenceType)) {
                this._select = new CastExpr(this._select, Type.Reference);
            }
        } else {
            typeCheckContents(stable);
        }
        return Type.Void;
    }

    public void translateValue(ClassGenerator classGen, MethodGenerator methodGen) {
        if (this._select != null) {
            this._select.translate(classGen, methodGen);
            this._select.startIterator(classGen, methodGen);
        } else {
            if (hasContents()) {
                InstructionList il = methodGen.getInstructionList();
                compileResultTree(classGen, methodGen);
                this._domAdapter = methodGen.addLocalVariable2("@" + this._escapedName, Type.ResultTree.toJCType(), il.getEnd());
                il.append(DUP);
                il.append(new ASTORE(this._domAdapter.getIndex()));
                return;
            }
            ConstantPoolGen cpg = classGen.getConstantPool();
            methodGen.getInstructionList().append(new PUSH(cpg, ""));
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._doParameterOptimization) {
            translateValue(classGen, methodGen);
            return;
        }
        String name = Util.escape(getEscapedName());
        il.append(classGen.loadTranslet());
        il.append(new PUSH(cpg, name));
        translateValue(classGen, methodGen);
        il.append(new PUSH(cpg, false));
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.TRANSLET_CLASS, Constants.ADD_PARAMETER, Constants.ADD_PARAMETER_SIG)));
        il.append(POP);
    }

    public void releaseResultTree(ClassGenerator classGen, MethodGenerator methodGen) {
        if (this._domAdapter != null) {
            ConstantPoolGen cpg = classGen.getConstantPool();
            InstructionList il = methodGen.getInstructionList();
            if (classGen.getStylesheet().callsNodeset() && classGen.getDOMClass().equals(Constants.MULTI_DOM_CLASS)) {
                int removeDA = cpg.addMethodref(Constants.MULTI_DOM_CLASS, "removeDOMAdapter", "(Lcom/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter;)V");
                il.append(methodGen.loadDOM());
                il.append(new CHECKCAST(cpg.addClass(Constants.MULTI_DOM_CLASS)));
                il.append(new ALOAD(this._domAdapter.getIndex()));
                il.append(new CHECKCAST(cpg.addClass(Constants.DOM_ADAPTER_CLASS)));
                il.append(new INVOKEVIRTUAL(removeDA));
            }
            int release = cpg.addInterfaceMethodref(Constants.DOM_IMPL_CLASS, BasicRootPaneUI.Actions.RELEASE, "()V");
            il.append(new ALOAD(this._domAdapter.getIndex()));
            il.append(new INVOKEINTERFACE(release, 1));
            this._domAdapter.setEnd(il.getEnd());
            methodGen.removeLocalVariable(this._domAdapter);
            this._domAdapter = null;
        }
    }
}
