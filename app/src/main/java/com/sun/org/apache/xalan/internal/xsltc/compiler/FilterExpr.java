package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/FilterExpr.class */
class FilterExpr extends Expression {
    private Expression _primary;
    private final Vector _predicates;

    public FilterExpr(Expression primary, Vector predicates) {
        this._primary = primary;
        this._predicates = predicates;
        primary.setParent(this);
    }

    protected Expression getExpr() {
        if (this._primary instanceof CastExpr) {
            return ((CastExpr) this._primary).getExpr();
        }
        return this._primary;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        this._primary.setParser(parser);
        if (this._predicates != null) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Expression exp = (Expression) this._predicates.elementAt(i2);
                exp.setParser(parser);
                exp.setParent(this);
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "filter-expr(" + ((Object) this._primary) + ", " + ((Object) this._predicates) + ")";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type ptype = this._primary.typeCheck(stable);
        boolean canOptimize = this._primary instanceof KeyCall;
        if (!(ptype instanceof NodeSetType)) {
            if (ptype instanceof ReferenceType) {
                this._primary = new CastExpr(this._primary, Type.NodeSet);
            } else {
                throw new TypeCheckError(this);
            }
        }
        int n2 = this._predicates.size();
        for (int i2 = 0; i2 < n2; i2++) {
            Predicate pred = (Predicate) this._predicates.elementAt(i2);
            if (!canOptimize) {
                pred.dontOptimize();
            }
            pred.typeCheck(stable);
        }
        Type type = Type.NodeSet;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        translateFilterExpr(classGen, methodGen, this._predicates == null ? -1 : this._predicates.size() - 1);
    }

    private void translateFilterExpr(ClassGenerator classGen, MethodGenerator methodGen, int predicateIndex) {
        if (predicateIndex >= 0) {
            translatePredicates(classGen, methodGen, predicateIndex);
        } else {
            this._primary.translate(classGen, methodGen);
        }
    }

    public void translatePredicates(ClassGenerator classGen, MethodGenerator methodGen, int predicateIndex) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (predicateIndex < 0) {
            translateFilterExpr(classGen, methodGen, predicateIndex);
            return;
        }
        Predicate predicate = (Predicate) this._predicates.get(predicateIndex);
        translatePredicates(classGen, methodGen, predicateIndex - 1);
        if (predicate.isNthPositionFilter()) {
            int nthIteratorIdx = cpg.addMethodref(Constants.NTH_ITERATOR_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;I)V");
            LocalVariableGen iteratorTemp = methodGen.addLocalVariable("filter_expr_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
            iteratorTemp.setStart(il.append(new ASTORE(iteratorTemp.getIndex())));
            predicate.translate(classGen, methodGen);
            LocalVariableGen predicateValueTemp = methodGen.addLocalVariable("filter_expr_tmp2", Util.getJCRefType("I"), null, null);
            predicateValueTemp.setStart(il.append(new ISTORE(predicateValueTemp.getIndex())));
            il.append(new NEW(cpg.addClass(Constants.NTH_ITERATOR_CLASS)));
            il.append(DUP);
            iteratorTemp.setEnd(il.append(new ALOAD(iteratorTemp.getIndex())));
            predicateValueTemp.setEnd(il.append(new ILOAD(predicateValueTemp.getIndex())));
            il.append(new INVOKESPECIAL(nthIteratorIdx));
            return;
        }
        int initCNLI = cpg.addMethodref(Constants.CURRENT_NODE_LIST_ITERATOR, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;ZLcom/sun/org/apache/xalan/internal/xsltc/dom/CurrentNodeListFilter;ILcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;)V");
        LocalVariableGen nodeIteratorTemp = methodGen.addLocalVariable("filter_expr_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        nodeIteratorTemp.setStart(il.append(new ASTORE(nodeIteratorTemp.getIndex())));
        predicate.translate(classGen, methodGen);
        LocalVariableGen filterTemp = methodGen.addLocalVariable("filter_expr_tmp2", Util.getJCRefType(Constants.CURRENT_NODE_LIST_FILTER_SIG), null, null);
        filterTemp.setStart(il.append(new ASTORE(filterTemp.getIndex())));
        il.append(new NEW(cpg.addClass(Constants.CURRENT_NODE_LIST_ITERATOR)));
        il.append(DUP);
        nodeIteratorTemp.setEnd(il.append(new ALOAD(nodeIteratorTemp.getIndex())));
        il.append(ICONST_1);
        filterTemp.setEnd(il.append(new ALOAD(filterTemp.getIndex())));
        il.append(methodGen.loadCurrentNode());
        il.append(classGen.loadTranslet());
        il.append(new INVOKESPECIAL(initCNLI));
    }
}
