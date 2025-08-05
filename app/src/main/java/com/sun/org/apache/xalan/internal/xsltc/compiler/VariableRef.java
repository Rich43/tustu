package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/VariableRef.class */
final class VariableRef extends VariableRefBase {
    public VariableRef(Variable variable) {
        super(variable);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        Closure variableClosure;
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._type.implementedAsMethod()) {
            return;
        }
        String name = this._variable.getEscapedName();
        String signature = this._type.toSignature();
        if (this._variable.isLocal()) {
            if (classGen.isExternal()) {
                Closure parentClosure = this._closure;
                while (true) {
                    variableClosure = parentClosure;
                    if (variableClosure == null || variableClosure.inInnerClass()) {
                        break;
                    } else {
                        parentClosure = variableClosure.getParentClosure();
                    }
                }
                if (variableClosure != null) {
                    il.append(ALOAD_0);
                    il.append(new GETFIELD(cpg.addFieldref(variableClosure.getInnerClassName(), name, signature)));
                } else {
                    il.append(this._variable.loadInstruction());
                }
            } else {
                il.append(this._variable.loadInstruction());
            }
        } else {
            String className = classGen.getClassName();
            il.append(classGen.loadTranslet());
            if (classGen.isExternal()) {
                il.append(new CHECKCAST(cpg.addClass(className)));
            }
            il.append(new GETFIELD(cpg.addFieldref(className, name, signature)));
        }
        if (this._variable.getType() instanceof NodeSetType) {
            int clone = cpg.addInterfaceMethodref(Constants.NODE_ITERATOR, "cloneIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
            il.append(new INVOKEINTERFACE(clone, 1));
        }
    }
}
