package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/FilterParentPath.class */
final class FilterParentPath extends Expression {
    private Expression _filterExpr;
    private Expression _path;
    private boolean _hasDescendantAxis = false;

    public FilterParentPath(Expression filterExpr, Expression path) {
        this._path = path;
        path.setParent(this);
        this._filterExpr = filterExpr;
        filterExpr.setParent(this);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        this._filterExpr.setParser(parser);
        this._path.setParser(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "FilterParentPath(" + ((Object) this._filterExpr) + ", " + ((Object) this._path) + ')';
    }

    public void setDescendantAxis() {
        this._hasDescendantAxis = true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type ftype = this._filterExpr.typeCheck(stable);
        if (!(ftype instanceof NodeSetType)) {
            if ((ftype instanceof ReferenceType) || (ftype instanceof NodeType)) {
                this._filterExpr = new CastExpr(this._filterExpr, Type.NodeSet);
            } else {
                throw new TypeCheckError(this);
            }
        }
        Type ptype = this._path.typeCheck(stable);
        if (!(ptype instanceof NodeSetType)) {
            this._path = new CastExpr(this._path, Type.NodeSet);
        }
        Type type = Type.NodeSet;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int initSI = cpg.addMethodref(Constants.STEP_ITERATOR_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
        this._filterExpr.translate(classGen, methodGen);
        LocalVariableGen filterTemp = methodGen.addLocalVariable("filter_parent_path_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        filterTemp.setStart(il.append(new ASTORE(filterTemp.getIndex())));
        this._path.translate(classGen, methodGen);
        LocalVariableGen pathTemp = methodGen.addLocalVariable("filter_parent_path_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        pathTemp.setStart(il.append(new ASTORE(pathTemp.getIndex())));
        il.append(new NEW(cpg.addClass(Constants.STEP_ITERATOR_CLASS)));
        il.append(DUP);
        filterTemp.setEnd(il.append(new ALOAD(filterTemp.getIndex())));
        pathTemp.setEnd(il.append(new ALOAD(pathTemp.getIndex())));
        il.append(new INVOKESPECIAL(initSI));
        if (this._hasDescendantAxis) {
            int incl = cpg.addMethodref(Constants.NODE_ITERATOR_BASE, "includeSelf", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
            il.append(new INVOKEVIRTUAL(incl));
        }
        SyntaxTreeNode parent = getParent();
        boolean parentAlreadyOrdered = (parent instanceof RelativeLocationPath) || (parent instanceof FilterParentPath) || (parent instanceof KeyCall) || (parent instanceof CurrentCall) || (parent instanceof DocumentCall);
        if (!parentAlreadyOrdered) {
            int order = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.ORDER_ITERATOR, Constants.ORDER_ITERATOR_SIG);
            il.append(methodGen.loadDOM());
            il.append(SWAP);
            il.append(methodGen.loadContextNode());
            il.append(new INVOKEINTERFACE(order, 3));
        }
    }
}
