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
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ParentLocationPath.class */
final class ParentLocationPath extends RelativeLocationPath {
    private Expression _step;
    private final RelativeLocationPath _path;
    private Type stype;
    private boolean _orderNodes = false;
    private boolean _axisMismatch;

    public ParentLocationPath(RelativeLocationPath path, Expression step) {
        this._axisMismatch = false;
        this._path = path;
        this._step = step;
        this._path.setParent(this);
        this._step.setParent(this);
        if (this._step instanceof Step) {
            this._axisMismatch = checkAxisMismatch();
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.RelativeLocationPath
    public void setAxis(int axis) {
        this._path.setAxis(axis);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.RelativeLocationPath
    public int getAxis() {
        return this._path.getAxis();
    }

    public RelativeLocationPath getPath() {
        return this._path;
    }

    public Expression getStep() {
        return this._step;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        this._step.setParser(parser);
        this._path.setParser(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "ParentLocationPath(" + ((Object) this._path) + ", " + ((Object) this._step) + ')';
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        this.stype = this._step.typeCheck(stable);
        this._path.typeCheck(stable);
        if (this._axisMismatch) {
            enableNodeOrdering();
        }
        Type type = Type.NodeSet;
        this._type = type;
        return type;
    }

    public void enableNodeOrdering() {
        SyntaxTreeNode parent = getParent();
        if (parent instanceof ParentLocationPath) {
            ((ParentLocationPath) parent).enableNodeOrdering();
        } else {
            this._orderNodes = true;
        }
    }

    public boolean checkAxisMismatch() {
        int left = this._path.getAxis();
        int right = ((Step) this._step).getAxis();
        if ((left == 0 || left == 1) && (right == 3 || right == 4 || right == 5 || right == 10 || right == 11 || right == 12)) {
            return true;
        }
        if ((left == 3 && right == 0) || right == 1 || right == 10 || right == 11 || left == 4 || left == 5) {
            return true;
        }
        if ((left == 6 || left == 7) && (right == 6 || right == 10 || right == 11 || right == 12)) {
            return true;
        }
        if ((left == 11 || left == 12) && (right == 4 || right == 5 || right == 6 || right == 7 || right == 10 || right == 11 || right == 12)) {
            return true;
        }
        if (right == 6 && left == 3 && (this._path instanceof Step)) {
            int type = ((Step) this._path).getNodeType();
            return type == 2;
        }
        return false;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        this._path.translate(classGen, methodGen);
        translateStep(classGen, methodGen);
    }

    public void translateStep(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        LocalVariableGen pathTemp = methodGen.addLocalVariable("parent_location_path_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        pathTemp.setStart(il.append(new ASTORE(pathTemp.getIndex())));
        this._step.translate(classGen, methodGen);
        LocalVariableGen stepTemp = methodGen.addLocalVariable("parent_location_path_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        stepTemp.setStart(il.append(new ASTORE(stepTemp.getIndex())));
        int initSI = cpg.addMethodref(Constants.STEP_ITERATOR_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
        il.append(new NEW(cpg.addClass(Constants.STEP_ITERATOR_CLASS)));
        il.append(DUP);
        pathTemp.setEnd(il.append(new ALOAD(pathTemp.getIndex())));
        stepTemp.setEnd(il.append(new ALOAD(stepTemp.getIndex())));
        il.append(new INVOKESPECIAL(initSI));
        Expression stp = this._step;
        if (stp instanceof ParentLocationPath) {
            stp = ((ParentLocationPath) stp).getStep();
        }
        if ((this._path instanceof Step) && (stp instanceof Step)) {
            int path = ((Step) this._path).getAxis();
            int step = ((Step) stp).getAxis();
            if ((path == 5 && step == 3) || (path == 11 && step == 10)) {
                int incl = cpg.addMethodref(Constants.NODE_ITERATOR_BASE, "includeSelf", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
                il.append(new INVOKEVIRTUAL(incl));
            }
        }
        if (this._orderNodes) {
            int order = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.ORDER_ITERATOR, Constants.ORDER_ITERATOR_SIG);
            il.append(methodGen.loadDOM());
            il.append(SWAP);
            il.append(methodGen.loadContextNode());
            il.append(new INVOKEINTERFACE(order, 3));
        }
    }
}
