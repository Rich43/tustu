package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.ICONST;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.dtm.Axis;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Step.class */
final class Step extends RelativeLocationPath {
    private int _axis;
    private Vector _predicates;
    private boolean _hadPredicates = false;
    private int _nodeType;

    public Step(int axis, int nodeType, Vector predicates) {
        this._axis = axis;
        this._nodeType = nodeType;
        this._predicates = predicates;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._predicates != null) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Predicate exp = (Predicate) this._predicates.elementAt(i2);
                exp.setParser(parser);
                exp.setParent(this);
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.RelativeLocationPath
    public int getAxis() {
        return this._axis;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.RelativeLocationPath
    public void setAxis(int axis) {
        this._axis = axis;
    }

    public int getNodeType() {
        return this._nodeType;
    }

    public Vector getPredicates() {
        return this._predicates;
    }

    public void addPredicates(Vector predicates) {
        if (this._predicates == null) {
            this._predicates = predicates;
        } else {
            this._predicates.addAll(predicates);
        }
    }

    private boolean hasParentPattern() {
        SyntaxTreeNode parent = getParent();
        return (parent instanceof ParentPattern) || (parent instanceof ParentLocationPath) || (parent instanceof UnionPathExpr) || (parent instanceof FilterParentPath);
    }

    private boolean hasParentLocationPath() {
        return getParent() instanceof ParentLocationPath;
    }

    private boolean hasPredicates() {
        return this._predicates != null && this._predicates.size() > 0;
    }

    private boolean isPredicate() {
        SyntaxTreeNode parent = this;
        while (parent != null) {
            parent = parent.getParent();
            if (parent instanceof Predicate) {
                return true;
            }
        }
        return false;
    }

    public boolean isAbbreviatedDot() {
        return this._nodeType == -1 && this._axis == 13;
    }

    public boolean isAbbreviatedDDot() {
        return this._nodeType == -1 && this._axis == 10;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        this._hadPredicates = hasPredicates();
        if (isAbbreviatedDot()) {
            this._type = (hasParentPattern() || hasPredicates() || hasParentLocationPath()) ? Type.NodeSet : Type.Node;
        } else {
            this._type = Type.NodeSet;
        }
        if (this._predicates != null) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Expression pred = (Expression) this._predicates.elementAt(i2);
                pred.typeCheck(stable);
            }
        }
        return this._type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        translateStep(classGen, methodGen, hasPredicates() ? this._predicates.size() - 1 : -1);
    }

    private void translateStep(ClassGenerator classGen, MethodGenerator methodGen, int predicateIndex) {
        String namespace;
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (predicateIndex >= 0) {
            translatePredicates(classGen, methodGen, predicateIndex);
            return;
        }
        int star = 0;
        String name = null;
        XSLTC xsltc = getParser().getXSLTC();
        if (this._nodeType >= 14) {
            Vector ni = xsltc.getNamesIndex();
            name = (String) ni.elementAt(this._nodeType - 14);
            star = name.lastIndexOf(42);
        }
        if (this._axis == 2 && this._nodeType != 2 && this._nodeType != -1 && !hasParentPattern() && star == 0) {
            int iter = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getTypedAxisIterator", "(II)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
            il.append(methodGen.loadDOM());
            il.append(new PUSH(cpg, 2));
            il.append(new PUSH(cpg, this._nodeType));
            il.append(new INVOKEINTERFACE(iter, 3));
            return;
        }
        SyntaxTreeNode parent = getParent();
        if (isAbbreviatedDot()) {
            if (this._type == Type.Node) {
                il.append(methodGen.loadContextNode());
                return;
            }
            if (parent instanceof ParentLocationPath) {
                int init = cpg.addMethodref(Constants.SINGLETON_ITERATOR, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(I)V");
                il.append(new NEW(cpg.addClass(Constants.SINGLETON_ITERATOR)));
                il.append(DUP);
                il.append(methodGen.loadContextNode());
                il.append(new INVOKESPECIAL(init));
                return;
            }
            int git = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getAxisIterator", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
            il.append(methodGen.loadDOM());
            il.append(new PUSH(cpg, this._axis));
            il.append(new INVOKEINTERFACE(git, 2));
            return;
        }
        if ((parent instanceof ParentLocationPath) && (parent.getParent() instanceof ParentLocationPath) && this._nodeType == 1 && !this._hadPredicates) {
            this._nodeType = -1;
        }
        switch (this._nodeType) {
            case -1:
                break;
            case 0:
            default:
                if (star > 1) {
                    if (this._axis == 2) {
                        namespace = name.substring(0, star - 2);
                    } else {
                        namespace = name.substring(0, star - 1);
                    }
                    int nsType = xsltc.registerNamespace(namespace);
                    int ns = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getNamespaceAxisIterator", "(II)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
                    il.append(methodGen.loadDOM());
                    il.append(new PUSH(cpg, this._axis));
                    il.append(new PUSH(cpg, nsType));
                    il.append(new INVOKEINTERFACE(ns, 3));
                    return;
                }
            case 1:
                int ty = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getTypedAxisIterator", "(II)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
                il.append(methodGen.loadDOM());
                il.append(new PUSH(cpg, this._axis));
                il.append(new PUSH(cpg, this._nodeType));
                il.append(new INVOKEINTERFACE(ty, 3));
                return;
            case 2:
                this._axis = 2;
                break;
        }
        int git2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getAxisIterator", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(methodGen.loadDOM());
        il.append(new PUSH(cpg, this._axis));
        il.append(new INVOKEINTERFACE(git2, 2));
    }

    public void translatePredicates(ClassGenerator classGen, MethodGenerator methodGen, int predicateIndex) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (predicateIndex < 0) {
            translateStep(classGen, methodGen, predicateIndex);
            return;
        }
        int predicateIndex2 = predicateIndex - 1;
        Predicate predicate = (Predicate) this._predicates.get(predicateIndex);
        if (predicate.isNodeValueTest()) {
            Step step = predicate.getStep();
            il.append(methodGen.loadDOM());
            if (step.isAbbreviatedDot()) {
                translateStep(classGen, methodGen, predicateIndex2);
                il.append(new ICONST(0));
            } else {
                ParentLocationPath path = new ParentLocationPath(this, step);
                step._parent = path;
                this._parent = path;
                try {
                    path.typeCheck(getParser().getSymbolTable());
                } catch (TypeCheckError e2) {
                }
                translateStep(classGen, methodGen, predicateIndex2);
                path.translateStep(classGen, methodGen);
                il.append(new ICONST(1));
            }
            predicate.translate(classGen, methodGen);
            int idx = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_NODE_VALUE_ITERATOR, Constants.GET_NODE_VALUE_ITERATOR_SIG);
            il.append(new INVOKEINTERFACE(idx, 5));
            return;
        }
        if (predicate.isNthDescendant()) {
            il.append(methodGen.loadDOM());
            il.append(new PUSH(cpg, predicate.getPosType()));
            predicate.translate(classGen, methodGen);
            il.append(new ICONST(0));
            int idx2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getNthDescendant", "(IIZ)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
            il.append(new INVOKEINTERFACE(idx2, 4));
            return;
        }
        if (predicate.isNthPositionFilter()) {
            int idx3 = cpg.addMethodref(Constants.NTH_ITERATOR_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;I)V");
            translatePredicates(classGen, methodGen, predicateIndex2);
            LocalVariableGen iteratorTemp = methodGen.addLocalVariable("step_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
            iteratorTemp.setStart(il.append(new ASTORE(iteratorTemp.getIndex())));
            predicate.translate(classGen, methodGen);
            LocalVariableGen predicateValueTemp = methodGen.addLocalVariable("step_tmp2", Util.getJCRefType("I"), null, null);
            predicateValueTemp.setStart(il.append(new ISTORE(predicateValueTemp.getIndex())));
            il.append(new NEW(cpg.addClass(Constants.NTH_ITERATOR_CLASS)));
            il.append(DUP);
            iteratorTemp.setEnd(il.append(new ALOAD(iteratorTemp.getIndex())));
            predicateValueTemp.setEnd(il.append(new ILOAD(predicateValueTemp.getIndex())));
            il.append(new INVOKESPECIAL(idx3));
            return;
        }
        int idx4 = cpg.addMethodref(Constants.CURRENT_NODE_LIST_ITERATOR, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xalan/internal/xsltc/dom/CurrentNodeListFilter;ILcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;)V");
        translatePredicates(classGen, methodGen, predicateIndex2);
        LocalVariableGen iteratorTemp2 = methodGen.addLocalVariable("step_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        iteratorTemp2.setStart(il.append(new ASTORE(iteratorTemp2.getIndex())));
        predicate.translateFilter(classGen, methodGen);
        LocalVariableGen filterTemp = methodGen.addLocalVariable("step_tmp2", Util.getJCRefType(Constants.CURRENT_NODE_LIST_FILTER_SIG), null, null);
        filterTemp.setStart(il.append(new ASTORE(filterTemp.getIndex())));
        il.append(new NEW(cpg.addClass(Constants.CURRENT_NODE_LIST_ITERATOR)));
        il.append(DUP);
        iteratorTemp2.setEnd(il.append(new ALOAD(iteratorTemp2.getIndex())));
        filterTemp.setEnd(il.append(new ALOAD(filterTemp.getIndex())));
        il.append(methodGen.loadCurrentNode());
        il.append(classGen.loadTranslet());
        if (classGen.isExternal()) {
            String className = classGen.getClassName();
            il.append(new CHECKCAST(cpg.addClass(className)));
        }
        il.append(new INVOKESPECIAL(idx4));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        StringBuffer buffer = new StringBuffer("step(\"");
        buffer.append(Axis.getNames(this._axis)).append("\", ").append(this._nodeType);
        if (this._predicates != null) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Predicate pred = (Predicate) this._predicates.elementAt(i2);
                buffer.append(", ").append(pred.toString());
            }
        }
        return buffer.append(')').toString();
    }
}
