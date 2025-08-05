package jdk.nashorn.internal.codegen.types;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.runtime.JSType;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/types/LongType.class */
class LongType extends Type {
    private static final long serialVersionUID = 1;
    private static final CompilerConstants.Call VALUE_OF;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LongType.class.desiredAssertionStatus();
        VALUE_OF = CompilerConstants.staticCallNoLookup(Long.class, BeanAdapter.VALUE_OF_METHOD_NAME, Long.class, Long.TYPE);
    }

    protected LongType(String name) {
        super(name, Long.TYPE, 3, 2);
    }

    protected LongType() {
        this(SchemaSymbols.ATTVAL_LONG);
    }

    @Override // jdk.nashorn.internal.codegen.types.Type
    public Type nextWider() {
        return NUMBER;
    }

    @Override // jdk.nashorn.internal.codegen.types.Type
    public Class<?> getBoxedType() {
        return Long.class;
    }

    @Override // jdk.nashorn.internal.codegen.types.Type
    public char getBytecodeStackType() {
        return 'J';
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type load(MethodVisitor method, int slot) {
        if (!$assertionsDisabled && slot == -1) {
            throw new AssertionError();
        }
        method.visitVarInsn(22, slot);
        return LONG;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public void store(MethodVisitor method, int slot) {
        if (!$assertionsDisabled && slot == -1) {
            throw new AssertionError();
        }
        method.visitVarInsn(55, slot);
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type ldc(MethodVisitor method, Object c2) {
        if (!$assertionsDisabled && !(c2 instanceof Long)) {
            throw new AssertionError();
        }
        long value = ((Long) c2).longValue();
        if (value == 0) {
            method.visitInsn(9);
        } else if (value == 1) {
            method.visitInsn(10);
        } else {
            method.visitLdcInsn(c2);
        }
        return Type.LONG;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type convert(MethodVisitor method, Type to) {
        if (isEquivalentTo(to)) {
            return to;
        }
        if (to.isNumber()) {
            method.visitInsn(138);
        } else if (to.isInteger()) {
            invokestatic(method, JSType.TO_INT32_L);
        } else if (to.isBoolean()) {
            method.visitInsn(136);
        } else if (to.isObject()) {
            invokestatic(method, VALUE_OF);
        } else if (!$assertionsDisabled) {
            throw new AssertionError((Object) ("Illegal conversion " + ((Object) this) + " -> " + ((Object) to)));
        }
        return to;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type add(MethodVisitor method, int programPoint) {
        if (programPoint == -1) {
            method.visitInsn(97);
        } else {
            method.visitInvokeDynamicInsn("ladd", "(JJ)J", MATHBOOTSTRAP, Integer.valueOf(programPoint));
        }
        return LONG;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public void _return(MethodVisitor method) {
        method.visitInsn(173);
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type loadUndefined(MethodVisitor method) {
        method.visitLdcInsn(0L);
        return LONG;
    }

    @Override // jdk.nashorn.internal.codegen.types.BytecodeOps
    public Type loadForcedInitializer(MethodVisitor method) {
        method.visitInsn(9);
        return LONG;
    }
}
