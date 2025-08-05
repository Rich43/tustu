package java.awt.image;

import sun.awt.image.SunWritableRaster;
import sun.java2d.StateTrackable;
import sun.java2d.StateTrackableDelegate;

/* loaded from: rt.jar:java/awt/image/DataBuffer.class */
public abstract class DataBuffer {
    public static final int TYPE_BYTE = 0;
    public static final int TYPE_USHORT = 1;
    public static final int TYPE_SHORT = 2;
    public static final int TYPE_INT = 3;
    public static final int TYPE_FLOAT = 4;
    public static final int TYPE_DOUBLE = 5;
    public static final int TYPE_UNDEFINED = 32;
    protected int dataType;
    protected int banks;
    protected int offset;
    protected int size;
    protected int[] offsets;
    StateTrackableDelegate theTrackable;
    private static final int[] dataTypeSize = {8, 16, 16, 32, 32, 64};

    public abstract int getElem(int i2, int i3);

    public abstract void setElem(int i2, int i3, int i4);

    static {
        SunWritableRaster.setDataStealer(new SunWritableRaster.DataStealer() { // from class: java.awt.image.DataBuffer.1
            @Override // sun.awt.image.SunWritableRaster.DataStealer
            public byte[] getData(DataBufferByte dataBufferByte, int i2) {
                return dataBufferByte.bankdata[i2];
            }

            @Override // sun.awt.image.SunWritableRaster.DataStealer
            public short[] getData(DataBufferUShort dataBufferUShort, int i2) {
                return dataBufferUShort.bankdata[i2];
            }

            @Override // sun.awt.image.SunWritableRaster.DataStealer
            public int[] getData(DataBufferInt dataBufferInt, int i2) {
                return dataBufferInt.bankdata[i2];
            }

            @Override // sun.awt.image.SunWritableRaster.DataStealer
            public StateTrackableDelegate getTrackable(DataBuffer dataBuffer) {
                return dataBuffer.theTrackable;
            }

            @Override // sun.awt.image.SunWritableRaster.DataStealer
            public void setTrackable(DataBuffer dataBuffer, StateTrackableDelegate stateTrackableDelegate) {
                dataBuffer.theTrackable = stateTrackableDelegate;
            }
        });
    }

    public static int getDataTypeSize(int i2) {
        if (i2 < 0 || i2 > 5) {
            throw new IllegalArgumentException("Unknown data type " + i2);
        }
        return dataTypeSize[i2];
    }

    protected DataBuffer(int i2, int i3) {
        this(StateTrackable.State.UNTRACKABLE, i2, i3);
    }

    DataBuffer(StateTrackable.State state, int i2, int i3) {
        this.theTrackable = StateTrackableDelegate.createInstance(state);
        this.dataType = i2;
        this.banks = 1;
        this.size = i3;
        this.offset = 0;
        this.offsets = new int[1];
    }

    protected DataBuffer(int i2, int i3, int i4) {
        this(StateTrackable.State.UNTRACKABLE, i2, i3, i4);
    }

    DataBuffer(StateTrackable.State state, int i2, int i3, int i4) {
        this.theTrackable = StateTrackableDelegate.createInstance(state);
        this.dataType = i2;
        this.banks = i4;
        this.size = i3;
        this.offset = 0;
        this.offsets = new int[this.banks];
    }

    protected DataBuffer(int i2, int i3, int i4, int i5) {
        this(StateTrackable.State.UNTRACKABLE, i2, i3, i4, i5);
    }

    DataBuffer(StateTrackable.State state, int i2, int i3, int i4, int i5) {
        this.theTrackable = StateTrackableDelegate.createInstance(state);
        this.dataType = i2;
        this.banks = i4;
        this.size = i3;
        this.offset = i5;
        this.offsets = new int[i4];
        for (int i6 = 0; i6 < i4; i6++) {
            this.offsets[i6] = i5;
        }
    }

    protected DataBuffer(int i2, int i3, int i4, int[] iArr) {
        this(StateTrackable.State.UNTRACKABLE, i2, i3, i4, iArr);
    }

    DataBuffer(StateTrackable.State state, int i2, int i3, int i4, int[] iArr) {
        if (i4 != iArr.length) {
            throw new ArrayIndexOutOfBoundsException("Number of banks does not match number of bank offsets");
        }
        this.theTrackable = StateTrackableDelegate.createInstance(state);
        this.dataType = i2;
        this.banks = i4;
        this.size = i3;
        this.offset = iArr[0];
        this.offsets = (int[]) iArr.clone();
    }

    public int getDataType() {
        return this.dataType;
    }

    public int getSize() {
        return this.size;
    }

    public int getOffset() {
        return this.offset;
    }

    public int[] getOffsets() {
        return (int[]) this.offsets.clone();
    }

    public int getNumBanks() {
        return this.banks;
    }

    public int getElem(int i2) {
        return getElem(0, i2);
    }

    public void setElem(int i2, int i3) {
        setElem(0, i2, i3);
    }

    public float getElemFloat(int i2) {
        return getElem(i2);
    }

    public float getElemFloat(int i2, int i3) {
        return getElem(i2, i3);
    }

    public void setElemFloat(int i2, float f2) {
        setElem(i2, (int) f2);
    }

    public void setElemFloat(int i2, int i3, float f2) {
        setElem(i2, i3, (int) f2);
    }

    public double getElemDouble(int i2) {
        return getElem(i2);
    }

    public double getElemDouble(int i2, int i3) {
        return getElem(i2, i3);
    }

    public void setElemDouble(int i2, double d2) {
        setElem(i2, (int) d2);
    }

    public void setElemDouble(int i2, int i3, double d2) {
        setElem(i2, i3, (int) d2);
    }

    static int[] toIntArray(Object obj) {
        if (obj instanceof int[]) {
            return (int[]) obj;
        }
        if (obj == null) {
            return null;
        }
        if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            int[] iArr = new int[sArr.length];
            for (int i2 = 0; i2 < sArr.length; i2++) {
                iArr[i2] = sArr[i2] & 65535;
            }
            return iArr;
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            int[] iArr2 = new int[bArr.length];
            for (int i3 = 0; i3 < bArr.length; i3++) {
                iArr2[i3] = 255 & bArr[i3];
            }
            return iArr2;
        }
        return null;
    }
}
