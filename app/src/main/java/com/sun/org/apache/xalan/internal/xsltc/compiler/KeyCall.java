package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/KeyCall.class */
final class KeyCall extends FunctionCall {
    private Expression _name;
    private Expression _value;
    private Type _valueType;
    private QName _resolvedQName;

    public KeyCall(QName fname, Vector arguments) {
        super(fname, arguments);
        this._resolvedQName = null;
        switch (argumentCount()) {
            case 1:
                this._name = null;
                this._value = argument(0);
                break;
            case 2:
                this._name = argument(0);
                this._value = argument(1);
                break;
            default:
                this._value = null;
                this._name = null;
                break;
        }
    }

    public void addParentDependency() {
        SyntaxTreeNode node;
        if (this._resolvedQName == null) {
            return;
        }
        SyntaxTreeNode parent = this;
        while (true) {
            node = parent;
            if (node == null || (node instanceof TopLevelElement)) {
                break;
            } else {
                parent = node.getParent();
            }
        }
        TopLevelElement parent2 = (TopLevelElement) node;
        if (parent2 != null) {
            parent2.addDependency(getSymbolTable().getKey(this._resolvedQName));
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type returnType = super.typeCheck(stable);
        if (this._name != null) {
            Type nameType = this._name.typeCheck(stable);
            if (this._name instanceof LiteralExpr) {
                LiteralExpr literal = (LiteralExpr) this._name;
                this._resolvedQName = getParser().getQNameIgnoreDefaultNs(literal.getValue());
            } else if (!(nameType instanceof StringType)) {
                this._name = new CastExpr(this._name, Type.String);
            }
        }
        this._valueType = this._value.typeCheck(stable);
        if (this._valueType != Type.NodeSet && this._valueType != Type.Reference && this._valueType != Type.String) {
            this._value = new CastExpr(this._value, Type.String);
            this._valueType = this._value.typeCheck(stable);
        }
        addParentDependency();
        return returnType;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int getKeyIndex = cpg.addMethodref(Constants.TRANSLET_CLASS, "getKeyIndex", "(Ljava/lang/String;)Lcom/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex;");
        int keyDom = cpg.addMethodref(Constants.KEY_INDEX_CLASS, "setDom", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;I)V");
        int getKeyIterator = cpg.addMethodref(Constants.KEY_INDEX_CLASS, "getKeyIndexIterator", "(" + this._valueType.toSignature() + "Z)" + Constants.KEY_INDEX_ITERATOR_SIG);
        il.append(classGen.loadTranslet());
        if (this._name == null) {
            il.append(new PUSH(cpg, "##id"));
        } else if (this._resolvedQName != null) {
            il.append(new PUSH(cpg, this._resolvedQName.toString()));
        } else {
            this._name.translate(classGen, methodGen);
        }
        il.append(new INVOKEVIRTUAL(getKeyIndex));
        il.append(DUP);
        il.append(methodGen.loadDOM());
        il.append(methodGen.loadCurrentNode());
        il.append(new INVOKEVIRTUAL(keyDom));
        this._value.translate(classGen, methodGen);
        il.append(this._name != null ? ICONST_1 : ICONST_0);
        il.append(new INVOKEVIRTUAL(getKeyIterator));
    }
}
