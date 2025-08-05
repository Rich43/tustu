package jdk.internal.org.objectweb.asm.commons;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/LocalVariablesSorter.class */
public class LocalVariablesSorter extends MethodVisitor {
    private static final Type OBJECT_TYPE = Type.getObjectType("java/lang/Object");
    private int[] mapping;
    private Object[] newLocals;
    protected final int firstLocal;
    protected int nextLocal;
    private boolean changed;

    public LocalVariablesSorter(int i2, String str, MethodVisitor methodVisitor) {
        this(Opcodes.ASM5, i2, str, methodVisitor);
        if (getClass() != LocalVariablesSorter.class) {
            throw new IllegalStateException();
        }
    }

    protected LocalVariablesSorter(int i2, int i3, String str, MethodVisitor methodVisitor) {
        super(i2, methodVisitor);
        this.mapping = new int[40];
        this.newLocals = new Object[20];
        Type[] argumentTypes = Type.getArgumentTypes(str);
        this.nextLocal = (8 & i3) == 0 ? 1 : 0;
        for (Type type : argumentTypes) {
            this.nextLocal += type.getSize();
        }
        this.firstLocal = this.nextLocal;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i2, int i3) {
        Type type;
        switch (i2) {
            case 21:
            case 54:
                type = Type.INT_TYPE;
                break;
            case 22:
            case 55:
                type = Type.LONG_TYPE;
                break;
            case 23:
            case 56:
                type = Type.FLOAT_TYPE;
                break;
            case 24:
            case 57:
                type = Type.DOUBLE_TYPE;
                break;
            default:
                type = OBJECT_TYPE;
                break;
        }
        this.mv.visitVarInsn(i2, remap(i3, type));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i2, int i3) {
        this.mv.visitIincInsn(remap(i2, Type.INT_TYPE), i3);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i2, int i3) {
        this.mv.visitMaxs(i2, this.nextLocal);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2) {
        this.mv.visitLocalVariable(str, str2, str3, label, label2, remap(i2, Type.getType(str2)));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        Type type = Type.getType(str);
        int[] iArr2 = new int[iArr.length];
        for (int i3 = 0; i3 < iArr2.length; i3++) {
            iArr2[i3] = remap(iArr[i3], type);
        }
        return this.mv.visitLocalVariableAnnotation(i2, typePath, labelArr, labelArr2, iArr2, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        if (i2 != -1) {
            throw new IllegalStateException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
        }
        if (!this.changed) {
            this.mv.visitFrame(i2, i3, objArr, i4, objArr2);
            return;
        }
        Object[] objArr3 = new Object[this.newLocals.length];
        System.arraycopy(this.newLocals, 0, objArr3, 0, objArr3.length);
        updateNewLocals(this.newLocals);
        int i5 = 0;
        for (int i6 = 0; i6 < i3; i6++) {
            Object obj = objArr[i6];
            int i7 = (obj == Opcodes.LONG || obj == Opcodes.DOUBLE) ? 2 : 1;
            if (obj != Opcodes.TOP) {
                Type objectType = OBJECT_TYPE;
                if (obj == Opcodes.INTEGER) {
                    objectType = Type.INT_TYPE;
                } else if (obj == Opcodes.FLOAT) {
                    objectType = Type.FLOAT_TYPE;
                } else if (obj == Opcodes.LONG) {
                    objectType = Type.LONG_TYPE;
                } else if (obj == Opcodes.DOUBLE) {
                    objectType = Type.DOUBLE_TYPE;
                } else if (obj instanceof String) {
                    objectType = Type.getObjectType((String) obj);
                }
                setFrameLocal(remap(i5, objectType), obj);
            }
            i5 += i7;
        }
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        while (i8 < this.newLocals.length) {
            int i11 = i8;
            i8++;
            Object obj2 = this.newLocals[i11];
            if (obj2 != null && obj2 != Opcodes.TOP) {
                this.newLocals[i10] = obj2;
                i9 = i10 + 1;
                if (obj2 == Opcodes.LONG || obj2 == Opcodes.DOUBLE) {
                    i8++;
                }
            } else {
                this.newLocals[i10] = Opcodes.TOP;
            }
            i10++;
        }
        this.mv.visitFrame(i2, i9, this.newLocals, i4, objArr2);
        this.newLocals = objArr3;
    }

    public int newLocal(Type type) {
        Object internalName;
        switch (type.getSort()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                internalName = Opcodes.INTEGER;
                break;
            case 6:
                internalName = Opcodes.FLOAT;
                break;
            case 7:
                internalName = Opcodes.LONG;
                break;
            case 8:
                internalName = Opcodes.DOUBLE;
                break;
            case 9:
                internalName = type.getDescriptor();
                break;
            default:
                internalName = type.getInternalName();
                break;
        }
        int iNewLocalMapping = newLocalMapping(type);
        setLocalType(iNewLocalMapping, type);
        setFrameLocal(iNewLocalMapping, internalName);
        this.changed = true;
        return iNewLocalMapping;
    }

    protected void updateNewLocals(Object[] objArr) {
    }

    protected void setLocalType(int i2, Type type) {
    }

    private void setFrameLocal(int i2, Object obj) {
        int length = this.newLocals.length;
        if (i2 >= length) {
            Object[] objArr = new Object[Math.max(2 * length, i2 + 1)];
            System.arraycopy(this.newLocals, 0, objArr, 0, length);
            this.newLocals = objArr;
        }
        this.newLocals[i2] = obj;
    }

    private int remap(int i2, Type type) {
        int iNewLocalMapping;
        if (i2 + type.getSize() <= this.firstLocal) {
            return i2;
        }
        int size = ((2 * i2) + type.getSize()) - 1;
        int length = this.mapping.length;
        if (size >= length) {
            int[] iArr = new int[Math.max(2 * length, size + 1)];
            System.arraycopy(this.mapping, 0, iArr, 0, length);
            this.mapping = iArr;
        }
        int i3 = this.mapping[size];
        if (i3 == 0) {
            iNewLocalMapping = newLocalMapping(type);
            setLocalType(iNewLocalMapping, type);
            this.mapping[size] = iNewLocalMapping + 1;
        } else {
            iNewLocalMapping = i3 - 1;
        }
        if (iNewLocalMapping != i2) {
            this.changed = true;
        }
        return iNewLocalMapping;
    }

    protected int newLocalMapping(Type type) {
        int i2 = this.nextLocal;
        this.nextLocal += type.getSize();
        return i2;
    }
}
