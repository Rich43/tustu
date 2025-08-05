package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ProcessingInstructionPattern.class */
final class ProcessingInstructionPattern extends StepPattern {
    private String _name;
    private boolean _typeChecked;

    public ProcessingInstructionPattern(String name) {
        super(3, 7, null);
        this._name = null;
        this._typeChecked = false;
        this._name = name;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.StepPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public double getDefaultPriority() {
        return this._name != null ? 0.0d : -0.5d;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.StepPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        if (this._predicates == null) {
            return "processing-instruction(" + this._name + ")";
        }
        return "processing-instruction(" + this._name + ")" + ((Object) this._predicates);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.StepPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public void reduceKernelPattern() {
        this._typeChecked = true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.StepPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public boolean isWildcard() {
        return false;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.StepPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (hasPredicates()) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Predicate pred = (Predicate) this._predicates.elementAt(i2);
                pred.typeCheck(stable);
            }
        }
        return Type.NodeSet;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.StepPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int gname = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getNodeName", "(I)Ljava/lang/String;");
        int cmp = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
        il.append(methodGen.loadCurrentNode());
        il.append(SWAP);
        il.append(methodGen.storeCurrentNode());
        if (!this._typeChecked) {
            il.append(methodGen.loadCurrentNode());
            int getType = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getExpandedTypeID", Constants.GET_PARENT_SIG);
            il.append(methodGen.loadDOM());
            il.append(methodGen.loadCurrentNode());
            il.append(new INVOKEINTERFACE(getType, 2));
            il.append(new PUSH(cpg, 7));
            this._falseList.add(il.append((BranchInstruction) new IF_ICMPEQ(null)));
        }
        il.append(new PUSH(cpg, this._name));
        il.append(methodGen.loadDOM());
        il.append(methodGen.loadCurrentNode());
        il.append(new INVOKEINTERFACE(gname, 2));
        il.append(new INVOKEVIRTUAL(cmp));
        this._falseList.add(il.append((BranchInstruction) new IFEQ(null)));
        if (hasPredicates()) {
            int n2 = this._predicates.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Predicate pred = (Predicate) this._predicates.elementAt(i2);
                Expression exp = pred.getExpr();
                exp.translateDesynthesized(classGen, methodGen);
                this._trueList.append(exp._trueList);
                this._falseList.append(exp._falseList);
            }
        }
        InstructionHandle restore = il.append(methodGen.storeCurrentNode());
        backPatchTrueList(restore);
        BranchHandle skipFalse = il.append((BranchInstruction) new GOTO(null));
        InstructionHandle restore2 = il.append(methodGen.storeCurrentNode());
        backPatchFalseList(restore2);
        this._falseList.add(il.append((BranchInstruction) new GOTO(null)));
        skipFalse.setTarget(il.append(NOP));
    }
}
