package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFNE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/IdKeyPattern.class */
abstract class IdKeyPattern extends LocationPathPattern {
    protected RelativePathPattern _left = null;
    private String _index;
    private String _value;

    public IdKeyPattern(String index, String value) {
        this._index = null;
        this._value = null;
        this._index = index;
        this._value = value;
    }

    public String getIndexName() {
        return this._index;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return Type.NodeSet;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public boolean isWildcard() {
        return false;
    }

    public void setLeft(RelativePathPattern left) {
        this._left = left;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public StepPattern getKernelPattern() {
        return null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
    public void reduceKernelPattern() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "id/keyPattern(" + this._index + ", " + this._value + ')';
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int getKeyIndex = cpg.addMethodref(Constants.TRANSLET_CLASS, "getKeyIndex", "(Ljava/lang/String;)Lcom/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex;");
        int lookupId = cpg.addMethodref(Constants.KEY_INDEX_CLASS, "containsID", "(ILjava/lang/Object;)I");
        int lookupKey = cpg.addMethodref(Constants.KEY_INDEX_CLASS, "containsKey", "(ILjava/lang/Object;)I");
        cpg.addInterfaceMethodref(Constants.DOM_INTF, "getNodeIdent", Constants.GET_PARENT_SIG);
        il.append(classGen.loadTranslet());
        il.append(new PUSH(cpg, this._index));
        il.append(new INVOKEVIRTUAL(getKeyIndex));
        il.append(SWAP);
        il.append(new PUSH(cpg, this._value));
        if (this instanceof IdPattern) {
            il.append(new INVOKEVIRTUAL(lookupId));
        } else {
            il.append(new INVOKEVIRTUAL(lookupKey));
        }
        this._trueList.add(il.append((BranchInstruction) new IFNE(null)));
        this._falseList.add(il.append((BranchInstruction) new GOTO(null)));
    }
}
