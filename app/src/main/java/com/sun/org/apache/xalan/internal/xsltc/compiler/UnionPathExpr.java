package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xml.internal.dtm.Axis;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/UnionPathExpr.class */
final class UnionPathExpr extends Expression {
    private final Expression _pathExpr;
    private final Expression _rest;
    private boolean _reverse = false;
    private Expression[] _components;

    public UnionPathExpr(Expression pathExpr, Expression rest) {
        this._pathExpr = pathExpr;
        this._rest = rest;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        Vector components = new Vector();
        flatten(components);
        int size = components.size();
        this._components = (Expression[]) components.toArray(new Expression[size]);
        for (int i2 = 0; i2 < size; i2++) {
            this._components[i2].setParser(parser);
            this._components[i2].setParent(this);
            if (this._components[i2] instanceof Step) {
                Step step = (Step) this._components[i2];
                int axis = step.getAxis();
                int type = step.getNodeType();
                if (axis == 2 || type == 2) {
                    this._components[i2] = this._components[0];
                    this._components[0] = step;
                }
                if (Axis.isReverse(axis)) {
                    this._reverse = true;
                }
            }
        }
        if (getParent() instanceof Expression) {
            this._reverse = false;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        int length = this._components.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (this._components[i2].typeCheck(stable) != Type.NodeSet) {
                this._components[i2] = new CastExpr(this._components[i2], Type.NodeSet);
            }
        }
        Type type = Type.NodeSet;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "union(" + ((Object) this._pathExpr) + ", " + ((Object) this._rest) + ')';
    }

    private void flatten(Vector components) {
        components.addElement(this._pathExpr);
        if (this._rest != null) {
            if (this._rest instanceof UnionPathExpr) {
                ((UnionPathExpr) this._rest).flatten(components);
            } else {
                components.addElement(this._rest);
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int init = cpg.addMethodref(Constants.UNION_ITERATOR_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
        int iter = cpg.addMethodref(Constants.UNION_ITERATOR_CLASS, Constants.ADD_ITERATOR, Constants.ADD_ITERATOR_SIG);
        il.append(new NEW(cpg.addClass(Constants.UNION_ITERATOR_CLASS)));
        il.append(DUP);
        il.append(methodGen.loadDOM());
        il.append(new INVOKESPECIAL(init));
        int length = this._components.length;
        for (int i2 = 0; i2 < length; i2++) {
            this._components[i2].translate(classGen, methodGen);
            il.append(new INVOKEVIRTUAL(iter));
        }
        if (this._reverse) {
            int order = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.ORDER_ITERATOR, Constants.ORDER_ITERATOR_SIG);
            il.append(methodGen.loadDOM());
            il.append(SWAP);
            il.append(methodGen.loadContextNode());
            il.append(new INVOKEINTERFACE(order, 3));
        }
    }
}
