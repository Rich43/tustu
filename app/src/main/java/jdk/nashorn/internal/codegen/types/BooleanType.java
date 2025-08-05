package jdk.nashorn.internal.codegen.types;

import com.sun.javafx.fxml.BeanAdapter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.CompilerConstants;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/types/BooleanType.class */
public final class BooleanType extends Type {
    private static final long serialVersionUID = 1;
    private static final CompilerConstants.Call VALUE_OF;
    private static final CompilerConstants.Call TO_STRING;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BooleanType.class.desiredAssertionStatus();
        VALUE_OF = CompilerConstants.staticCallNoLookup(Boolean.class, BeanAdapter.VALUE_OF_METHOD_NAME, Boolean.class, Boolean.TYPE);
        TO_STRING = CompilerConstants.staticCallNoLookup(Boolean.class, "toString", String.class, Boolean.TYPE);
    }

    protected BooleanType() {
        super("boolean", Boolean.TYPE, 1, 1);
    }

    @Override // jdk.nashorn.internal.codegen.types.Type
    public Type nextWider() {
        return INT;
    }

    @Override // jdk.nashorn.internal.codegen.types.Type
    public Class<?> getBoxedType() {
        return Boolean.class;
    }

    @Override // jdk.nashorn.internal.codegen.types.Type
    public char getBytecodeStackType() {
        return 'I';
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type loadUndefined(MethodVisitor method) {
        method.visitLdcInsn(0);
        return BOOLEAN;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type loadForcedInitializer(MethodVisitor method) {
        method.visitInsn(3);
        return BOOLEAN;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public void _return(MethodVisitor method) {
        method.visitInsn(172);
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type load(MethodVisitor method, int slot) {
        if (!$assertionsDisabled && slot == -1) {
            throw new AssertionError();
        }
        method.visitVarInsn(21, slot);
        return BOOLEAN;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public void store(MethodVisitor method, int slot) {
        if (!$assertionsDisabled && slot == -1) {
            throw new AssertionError();
        }
        method.visitVarInsn(54, slot);
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type ldc(MethodVisitor method, Object c2) {
        if (!$assertionsDisabled && !(c2 instanceof Boolean)) {
            throw new AssertionError();
        }
        method.visitInsn(((Boolean) c2).booleanValue() ? 4 : 3);
        return BOOLEAN;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type convert(MethodVisitor method, Type to) {
        if (isEquivalentTo(to)) {
            return to;
        }
        if (to.isNumber()) {
            method.visitInsn(135);
        } else if (to.isLong()) {
            method.visitInsn(133);
        } else if (!to.isInteger()) {
            if (to.isString()) {
                invokestatic(method, TO_STRING);
            } else if (to.isObject()) {
                invokestatic(method, VALUE_OF);
            } else {
                throw new UnsupportedOperationException("Illegal conversion " + ((Object) this) + " -> " + ((Object) to));
            }
        }
        return to;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type add(MethodVisitor method, int programPoint) {
        if (programPoint == -1) {
            method.visitInsn(96);
        } else {
            method.visitInvokeDynamicInsn("iadd", "(II)I", MATHBOOTSTRAP, Integer.valueOf(programPoint));
        }
        return INT;
    }
}
