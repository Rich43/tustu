package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFNULL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/ObjectType.class */
public final class ObjectType extends Type {
    private String _javaClassName;
    private Class _clazz;

    protected ObjectType(String javaClassName) {
        this._javaClassName = Constants.OBJECT_CLASS;
        this._clazz = Object.class;
        this._javaClassName = javaClassName;
        try {
            this._clazz = ObjectFactory.findProviderClass(javaClassName, true);
        } catch (ClassNotFoundException e2) {
            this._clazz = null;
        }
    }

    protected ObjectType(Class clazz) {
        this._javaClassName = Constants.OBJECT_CLASS;
        this._clazz = Object.class;
        this._clazz = clazz;
        this._javaClassName = clazz.getName();
    }

    public int hashCode() {
        return Object.class.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof ObjectType;
    }

    public String getJavaClassName() {
        return this._javaClassName;
    }

    public Class getJavaClass() {
        return this._clazz;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toString() {
        return this._javaClassName;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean identicalTo(Type other) {
        return this == other;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toSignature() {
        StringBuffer result = new StringBuffer("L");
        result.append(this._javaClassName.replace('.', '/')).append(';');
        return result.toString();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return Util.getJCRefType(toSignature());
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
        if (type == Type.String) {
            translateTo(classGen, methodGen, (StringType) type);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
            classGen.getParser().reportError(2, err);
        }
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(DUP);
        BranchHandle ifNull = il.append((BranchInstruction) new IFNULL(null));
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(this._javaClassName, "toString", "()Ljava/lang/String;")));
        BranchHandle gotobh = il.append((BranchInstruction) new GOTO(null));
        ifNull.setTarget(il.append(POP));
        il.append(new PUSH(cpg, ""));
        gotobh.setTarget(il.append(NOP));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        if (clazz.isAssignableFrom(this._clazz)) {
            methodGen.getInstructionList().append(NOP);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getClass().toString());
            classGen.getParser().reportError(2, err);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        methodGen.getInstructionList().append(NOP);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction LOAD(int slot) {
        return new ALOAD(slot);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction STORE(int slot) {
        return new ASTORE(slot);
    }
}
