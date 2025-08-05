package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.GOTO_W;
import com.sun.org.apache.bcel.internal.generic.IFLT;
import com.sun.org.apache.bcel.internal.generic.IFNE;
import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPLT;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPNE;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.dtm.Axis;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/StepPattern.class */
class StepPattern extends RelativePathPattern {
    private static final int NO_CONTEXT = 0;
    private static final int SIMPLE_CONTEXT = 1;
    private static final int GENERAL_CONTEXT = 2;
    protected final int _axis;
    protected final int _nodeType;
    protected Vector _predicates;
    private int _contextCase;
    private Step _step = null;
    private boolean _isEpsilon = false;
    private double _priority = Double.MAX_VALUE;

    public StepPattern(int axis, int nodeType, Vector predicates) {
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

    public int getNodeType() {
        return this._nodeType;
    }

    public void setPriority(double priority) {
        this._priority = priority;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public StepPattern getKernelPattern() {
        return this;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public boolean isWildcard() {
        return this._isEpsilon && !hasPredicates();
    }

    public StepPattern setPredicates(Vector predicates) {
        this._predicates = predicates;
        return this;
    }

    protected boolean hasPredicates() {
        return this._predicates != null && this._predicates.size() > 0;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public double getDefaultPriority() {
        if (this._priority != Double.MAX_VALUE) {
            return this._priority;
        }
        if (hasPredicates()) {
            return 0.5d;
        }
        switch (this._nodeType) {
            case -1:
                return -0.5d;
            case 0:
                return 0.0d;
            default:
                return this._nodeType >= 14 ? 0.0d : -0.5d;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public int getAxis() {
        return this._axis;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public void reduceKernelPattern() {
        this._isEpsilon = true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        String string;
        StringBuffer buffer = new StringBuffer("stepPattern(\"");
        StringBuffer stringBufferAppend = buffer.append(Axis.getNames(this._axis)).append("\", ");
        if (this._isEpsilon) {
            string = "epsilon{" + Integer.toString(this._nodeType) + "}";
        } else {
            string = Integer.toString(this._nodeType);
        }
        stringBufferAppend.append(string);
        if (this._predicates != null) {
            buffer.append(", ").append(this._predicates.toString());
        }
        return buffer.append(')').toString();
    }

    private int analyzeCases() {
        boolean noContext = true;
        int n2 = this._predicates.size();
        for (int i2 = 0; i2 < n2 && noContext; i2++) {
            Predicate pred = (Predicate) this._predicates.elementAt(i2);
            if (pred.isNthPositionFilter() || pred.hasPositionCall() || pred.hasLastCall()) {
                noContext = false;
            }
        }
        if (noContext) {
            return 0;
        }
        if (n2 == 1) {
            return 1;
        }
        return 2;
    }

    private String getNextFieldName() {
        return "__step_pattern_iter_" + getXSLTC().nextStepPatternSerial();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (hasPredicates()) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Predicate pred = (Predicate) this._predicates.elementAt(i2);
                pred.typeCheck(stable);
            }
            this._contextCase = analyzeCases();
            Step step = null;
            if (this._contextCase == 1) {
                Predicate pred2 = (Predicate) this._predicates.elementAt(0);
                if (pred2.isNthPositionFilter()) {
                    this._contextCase = 2;
                    step = new Step(this._axis, this._nodeType, this._predicates);
                } else {
                    step = new Step(this._axis, this._nodeType, null);
                }
            } else if (this._contextCase == 2) {
                int len = this._predicates.size();
                for (int i3 = 0; i3 < len; i3++) {
                    ((Predicate) this._predicates.elementAt(i3)).dontOptimize();
                }
                step = new Step(this._axis, this._nodeType, this._predicates);
            }
            if (step != null) {
                step.setParser(getParser());
                step.typeCheck(stable);
                this._step = step;
            }
        }
        return this._axis == 3 ? Type.Element : Type.Attribute;
    }

    private void translateKernel(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._nodeType == 1) {
            int check = cpg.addInterfaceMethodref(Constants.DOM_INTF, "isElement", "(I)Z");
            il.append(methodGen.loadDOM());
            il.append(SWAP);
            il.append(new INVOKEINTERFACE(check, 2));
            BranchHandle icmp = il.append((BranchInstruction) new IFNE(null));
            this._falseList.add(il.append((BranchInstruction) new GOTO_W(null)));
            icmp.setTarget(il.append(NOP));
            return;
        }
        if (this._nodeType == 2) {
            int check2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, "isAttribute", "(I)Z");
            il.append(methodGen.loadDOM());
            il.append(SWAP);
            il.append(new INVOKEINTERFACE(check2, 2));
            BranchHandle icmp2 = il.append((BranchInstruction) new IFNE(null));
            this._falseList.add(il.append((BranchInstruction) new GOTO_W(null)));
            icmp2.setTarget(il.append(NOP));
            return;
        }
        int getEType = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getExpandedTypeID", Constants.GET_PARENT_SIG);
        il.append(methodGen.loadDOM());
        il.append(SWAP);
        il.append(new INVOKEINTERFACE(getEType, 2));
        il.append(new PUSH(cpg, this._nodeType));
        BranchHandle icmp3 = il.append((BranchInstruction) new IF_ICMPEQ(null));
        this._falseList.add(il.append((BranchInstruction) new GOTO_W(null)));
        icmp3.setTarget(il.append(NOP));
    }

    private void translateNoContext(ClassGenerator classGen, MethodGenerator methodGen) {
        classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(methodGen.loadCurrentNode());
        il.append(SWAP);
        il.append(methodGen.storeCurrentNode());
        if (!this._isEpsilon) {
            il.append(methodGen.loadCurrentNode());
            translateKernel(classGen, methodGen);
        }
        int n2 = this._predicates.size();
        for (int i2 = 0; i2 < n2; i2++) {
            Predicate pred = (Predicate) this._predicates.elementAt(i2);
            Expression exp = pred.getExpr();
            exp.translateDesynthesized(classGen, methodGen);
            this._trueList.append(exp._trueList);
            this._falseList.append(exp._falseList);
        }
        InstructionHandle restore = il.append(methodGen.storeCurrentNode());
        backPatchTrueList(restore);
        BranchHandle skipFalse = il.append((BranchInstruction) new GOTO(null));
        InstructionHandle restore2 = il.append(methodGen.storeCurrentNode());
        backPatchFalseList(restore2);
        this._falseList.add(il.append((BranchInstruction) new GOTO(null)));
        skipFalse.setTarget(il.append(NOP));
    }

    private void translateSimpleContext(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        LocalVariableGen match = methodGen.addLocalVariable("step_pattern_tmp1", Util.getJCRefType("I"), null, null);
        match.setStart(il.append(new ISTORE(match.getIndex())));
        if (!this._isEpsilon) {
            il.append(new ILOAD(match.getIndex()));
            translateKernel(classGen, methodGen);
        }
        il.append(methodGen.loadCurrentNode());
        il.append(methodGen.loadIterator());
        int index = cpg.addMethodref(Constants.MATCHING_ITERATOR, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(ILcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
        this._step.translate(classGen, methodGen);
        LocalVariableGen stepIteratorTemp = methodGen.addLocalVariable("step_pattern_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        stepIteratorTemp.setStart(il.append(new ASTORE(stepIteratorTemp.getIndex())));
        il.append(new NEW(cpg.addClass(Constants.MATCHING_ITERATOR)));
        il.append(DUP);
        il.append(new ILOAD(match.getIndex()));
        stepIteratorTemp.setEnd(il.append(new ALOAD(stepIteratorTemp.getIndex())));
        il.append(new INVOKESPECIAL(index));
        il.append(methodGen.loadDOM());
        il.append(new ILOAD(match.getIndex()));
        int index2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_PARENT, Constants.GET_PARENT_SIG);
        il.append(new INVOKEINTERFACE(index2, 2));
        il.append(methodGen.setStartNode());
        il.append(methodGen.storeIterator());
        match.setEnd(il.append(new ILOAD(match.getIndex())));
        il.append(methodGen.storeCurrentNode());
        Predicate pred = (Predicate) this._predicates.elementAt(0);
        Expression exp = pred.getExpr();
        exp.translateDesynthesized(classGen, methodGen);
        InstructionHandle restore = il.append(methodGen.storeIterator());
        il.append(methodGen.storeCurrentNode());
        exp.backPatchTrueList(restore);
        BranchHandle skipFalse = il.append((BranchInstruction) new GOTO(null));
        InstructionHandle restore2 = il.append(methodGen.storeIterator());
        il.append(methodGen.storeCurrentNode());
        exp.backPatchFalseList(restore2);
        this._falseList.add(il.append((BranchInstruction) new GOTO(null)));
        skipFalse.setTarget(il.append(NOP));
    }

    private void translateGeneralContext(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int iteratorIndex = 0;
        BranchHandle ifBlock = null;
        String iteratorName = getNextFieldName();
        LocalVariableGen node = methodGen.addLocalVariable("step_pattern_tmp1", Util.getJCRefType("I"), null, null);
        node.setStart(il.append(new ISTORE(node.getIndex())));
        LocalVariableGen iter = methodGen.addLocalVariable("step_pattern_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        if (!classGen.isExternal()) {
            Field iterator = new Field(2, cpg.addUtf8(iteratorName), cpg.addUtf8("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, cpg.getConstantPool());
            classGen.addField(iterator);
            iteratorIndex = cpg.addFieldref(classGen.getClassName(), iteratorName, "Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
            il.append(classGen.loadTranslet());
            il.append(new GETFIELD(iteratorIndex));
            il.append(DUP);
            iter.setStart(il.append(new ASTORE(iter.getIndex())));
            ifBlock = il.append((BranchInstruction) new IFNONNULL(null));
            il.append(classGen.loadTranslet());
        }
        this._step.translate(classGen, methodGen);
        InstructionHandle iterStore = il.append(new ASTORE(iter.getIndex()));
        if (!classGen.isExternal()) {
            il.append(new ALOAD(iter.getIndex()));
            il.append(new PUTFIELD(iteratorIndex));
            ifBlock.setTarget(il.append(NOP));
        } else {
            iter.setStart(iterStore);
        }
        il.append(methodGen.loadDOM());
        il.append(new ILOAD(node.getIndex()));
        int index = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_PARENT, Constants.GET_PARENT_SIG);
        il.append(new INVOKEINTERFACE(index, 2));
        il.append(new ALOAD(iter.getIndex()));
        il.append(SWAP);
        il.append(methodGen.setStartNode());
        LocalVariableGen node2 = methodGen.addLocalVariable("step_pattern_tmp3", Util.getJCRefType("I"), null, null);
        BranchHandle skipNext = il.append((BranchInstruction) new GOTO(null));
        InstructionHandle next = il.append(new ALOAD(iter.getIndex()));
        node2.setStart(next);
        InstructionHandle begin = il.append(methodGen.nextNode());
        il.append(DUP);
        il.append(new ISTORE(node2.getIndex()));
        this._falseList.add(il.append((BranchInstruction) new IFLT(null)));
        il.append(new ILOAD(node2.getIndex()));
        il.append(new ILOAD(node.getIndex()));
        iter.setEnd(il.append((BranchInstruction) new IF_ICMPLT(next)));
        node2.setEnd(il.append(new ILOAD(node2.getIndex())));
        node.setEnd(il.append(new ILOAD(node.getIndex())));
        this._falseList.add(il.append((BranchInstruction) new IF_ICMPNE(null)));
        skipNext.setTarget(begin);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (hasPredicates()) {
            switch (this._contextCase) {
                case 0:
                    translateNoContext(classGen, methodGen);
                    break;
                case 1:
                    translateSimpleContext(classGen, methodGen);
                    break;
                default:
                    translateGeneralContext(classGen, methodGen);
                    break;
            }
        }
        if (isWildcard()) {
            il.append(POP);
        } else {
            translateKernel(classGen, methodGen);
        }
    }
}
