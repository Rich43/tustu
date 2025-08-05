package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ArrayType.class */
public final class ArrayType extends ReferenceType {
    private int dimensions;
    private Type basic_type;

    public ArrayType(byte type, int dimensions) {
        this(BasicType.getType(type), dimensions);
    }

    public ArrayType(String class_name, int dimensions) {
        this(new ObjectType(class_name), dimensions);
    }

    public ArrayType(Type type, int dimensions) {
        super((byte) 13, "<dummy>");
        if (dimensions < 1 || dimensions > 255) {
            throw new ClassGenException("Invalid number of dimensions: " + dimensions);
        }
        switch (type.getType()) {
            case 12:
                throw new ClassGenException("Invalid type: void[]");
            case 13:
                ArrayType array = (ArrayType) type;
                this.dimensions = dimensions + array.dimensions;
                this.basic_type = array.basic_type;
                break;
            default:
                this.dimensions = dimensions;
                this.basic_type = type;
                break;
        }
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < this.dimensions; i2++) {
            buf.append('[');
        }
        buf.append(this.basic_type.getSignature());
        this.signature = buf.toString();
    }

    public Type getBasicType() {
        return this.basic_type;
    }

    public Type getElementType() {
        if (this.dimensions == 1) {
            return this.basic_type;
        }
        return new ArrayType(this.basic_type, this.dimensions - 1);
    }

    public int getDimensions() {
        return this.dimensions;
    }

    public int hashCode() {
        return this.basic_type.hashCode() ^ this.dimensions;
    }

    public boolean equals(Object type) {
        if (type instanceof ArrayType) {
            ArrayType array = (ArrayType) type;
            return array.dimensions == this.dimensions && array.basic_type.equals(this.basic_type);
        }
        return false;
    }
}
