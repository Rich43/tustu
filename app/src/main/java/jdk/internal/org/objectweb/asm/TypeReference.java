package jdk.internal.org.objectweb.asm;

import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/TypeReference.class */
public class TypeReference {
    public static final int CLASS_TYPE_PARAMETER = 0;
    public static final int METHOD_TYPE_PARAMETER = 1;
    public static final int CLASS_EXTENDS = 16;
    public static final int CLASS_TYPE_PARAMETER_BOUND = 17;
    public static final int METHOD_TYPE_PARAMETER_BOUND = 18;
    public static final int FIELD = 19;
    public static final int METHOD_RETURN = 20;
    public static final int METHOD_RECEIVER = 21;
    public static final int METHOD_FORMAL_PARAMETER = 22;
    public static final int THROWS = 23;
    public static final int LOCAL_VARIABLE = 64;
    public static final int RESOURCE_VARIABLE = 65;
    public static final int EXCEPTION_PARAMETER = 66;
    public static final int INSTANCEOF = 67;
    public static final int NEW = 68;
    public static final int CONSTRUCTOR_REFERENCE = 69;
    public static final int METHOD_REFERENCE = 70;
    public static final int CAST = 71;
    public static final int CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
    public static final int METHOD_INVOCATION_TYPE_ARGUMENT = 73;
    public static final int CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
    public static final int METHOD_REFERENCE_TYPE_ARGUMENT = 75;
    private int value;

    public TypeReference(int i2) {
        this.value = i2;
    }

    public static TypeReference newTypeReference(int i2) {
        return new TypeReference(i2 << 24);
    }

    public static TypeReference newTypeParameterReference(int i2, int i3) {
        return new TypeReference((i2 << 24) | (i3 << 16));
    }

    public static TypeReference newTypeParameterBoundReference(int i2, int i3, int i4) {
        return new TypeReference((i2 << 24) | (i3 << 16) | (i4 << 8));
    }

    public static TypeReference newSuperTypeReference(int i2) {
        return new TypeReference(268435456 | ((i2 & 65535) << 8));
    }

    public static TypeReference newFormalParameterReference(int i2) {
        return new TypeReference(369098752 | (i2 << 16));
    }

    public static TypeReference newExceptionReference(int i2) {
        return new TypeReference(385875968 | (i2 << 8));
    }

    public static TypeReference newTryCatchReference(int i2) {
        return new TypeReference(1107296256 | (i2 << 8));
    }

    public static TypeReference newTypeArgumentReference(int i2, int i3) {
        return new TypeReference((i2 << 24) | i3);
    }

    public int getSort() {
        return this.value >>> 24;
    }

    public int getTypeParameterIndex() {
        return (this.value & 16711680) >> 16;
    }

    public int getTypeParameterBoundIndex() {
        return (this.value & NormalizerImpl.CC_MASK) >> 8;
    }

    public int getSuperTypeIndex() {
        return (short) ((this.value & 16776960) >> 8);
    }

    public int getFormalParameterIndex() {
        return (this.value & 16711680) >> 16;
    }

    public int getExceptionIndex() {
        return (this.value & 16776960) >> 8;
    }

    public int getTryCatchBlockIndex() {
        return (this.value & 16776960) >> 8;
    }

    public int getTypeArgumentIndex() {
        return this.value & 255;
    }

    public int getValue() {
        return this.value;
    }
}
