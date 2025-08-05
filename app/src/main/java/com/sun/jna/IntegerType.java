package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/IntegerType.class */
public abstract class IntegerType extends Number implements NativeMapped {
    private int size;
    private long value;
    private Number number;

    public IntegerType(int size) {
        this(size, 0L);
    }

    public IntegerType(int size, long value) {
        this.size = size;
        setValue(value);
    }

    public void setValue(long value) {
        long truncated = value;
        this.value = value;
        switch (this.size) {
            case 1:
                truncated = (byte) value;
                this.number = new Byte((byte) value);
                break;
            case 2:
                truncated = (short) value;
                this.number = new Short((short) value);
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            default:
                throw new IllegalArgumentException(new StringBuffer().append("Unsupported size: ").append(this.size).toString());
            case 4:
                truncated = (int) value;
                this.number = new Integer((int) value);
                break;
            case 8:
                this.number = new Long(value);
                break;
        }
        if (this.size < 8) {
            long mask = ((1 << (this.size * 8)) - 1) ^ (-1);
            if ((value < 0 && truncated != value) || (value >= 0 && (mask & value) != 0)) {
                throw new IllegalArgumentException(new StringBuffer().append("Argument value 0x").append(Long.toHexString(value)).append(" exceeds native capacity (").append(this.size).append(" bytes) mask=0x").append(Long.toHexString(mask)).toString());
            }
        }
    }

    @Override // com.sun.jna.NativeMapped
    public Object toNative() {
        return this.number;
    }

    @Override // com.sun.jna.NativeMapped
    public Object fromNative(Object nativeValue, FromNativeContext context) {
        long value = nativeValue == null ? 0L : ((Number) nativeValue).longValue();
        try {
            IntegerType number = (IntegerType) getClass().newInstance();
            number.setValue(value);
            return number;
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException(new StringBuffer().append("Not allowed to instantiate ").append((Object) getClass()).toString());
        } catch (InstantiationException e3) {
            throw new IllegalArgumentException(new StringBuffer().append("Can't instantiate ").append((Object) getClass()).toString());
        }
    }

    @Override // com.sun.jna.NativeMapped
    public Class nativeType() {
        return this.number.getClass();
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.number.intValue();
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.number.longValue();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.number.floatValue();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.number.doubleValue();
    }

    public boolean equals(Object rhs) {
        return (rhs instanceof IntegerType) && this.number.equals(((IntegerType) rhs).number);
    }

    public String toString() {
        return this.number.toString();
    }

    public int hashCode() {
        return this.number.hashCode();
    }
}
