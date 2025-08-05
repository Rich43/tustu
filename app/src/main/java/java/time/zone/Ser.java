package java.time.zone;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;
import java.time.ZoneOffset;

/* loaded from: rt.jar:java/time/zone/Ser.class */
final class Ser implements Externalizable {
    private static final long serialVersionUID = -8885321777449118786L;
    static final byte ZRULES = 1;
    static final byte ZOT = 2;
    static final byte ZOTRULE = 3;
    private byte type;
    private Object object;

    public Ser() {
    }

    Ser(byte b2, Object obj) {
        this.type = b2;
        this.object = obj;
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        writeInternal(this.type, this.object, objectOutput);
    }

    static void write(Object obj, DataOutput dataOutput) throws IOException {
        writeInternal((byte) 1, obj, dataOutput);
    }

    private static void writeInternal(byte b2, Object obj, DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(b2);
        switch (b2) {
            case 1:
                ((ZoneRules) obj).writeExternal(dataOutput);
                return;
            case 2:
                ((ZoneOffsetTransition) obj).writeExternal(dataOutput);
                return;
            case 3:
                ((ZoneOffsetTransitionRule) obj).writeExternal(dataOutput);
                return;
            default:
                throw new InvalidClassException("Unknown serialized type");
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.type = objectInput.readByte();
        this.object = readInternal(this.type, objectInput);
    }

    static Object read(DataInput dataInput) throws IOException, ClassNotFoundException {
        return readInternal(dataInput.readByte(), dataInput);
    }

    private static Object readInternal(byte b2, DataInput dataInput) throws IOException, ClassNotFoundException {
        switch (b2) {
            case 1:
                return ZoneRules.readExternal(dataInput);
            case 2:
                return ZoneOffsetTransition.readExternal(dataInput);
            case 3:
                return ZoneOffsetTransitionRule.readExternal(dataInput);
            default:
                throw new StreamCorruptedException("Unknown serialized type");
        }
    }

    private Object readResolve() {
        return this.object;
    }

    static void writeOffset(ZoneOffset zoneOffset, DataOutput dataOutput) throws IOException {
        int totalSeconds = zoneOffset.getTotalSeconds();
        int i2 = totalSeconds % 900 == 0 ? totalSeconds / 900 : 127;
        dataOutput.writeByte(i2);
        if (i2 == 127) {
            dataOutput.writeInt(totalSeconds);
        }
    }

    static ZoneOffset readOffset(DataInput dataInput) throws IOException {
        byte b2 = dataInput.readByte();
        return b2 == Byte.MAX_VALUE ? ZoneOffset.ofTotalSeconds(dataInput.readInt()) : ZoneOffset.ofTotalSeconds(b2 * 900);
    }

    static void writeEpochSec(long j2, DataOutput dataOutput) throws IOException {
        if (j2 >= -4575744000L && j2 < 10413792000L && j2 % 900 == 0) {
            int i2 = (int) ((j2 + 4575744000L) / 900);
            dataOutput.writeByte((i2 >>> 16) & 255);
            dataOutput.writeByte((i2 >>> 8) & 255);
            dataOutput.writeByte(i2 & 255);
            return;
        }
        dataOutput.writeByte(255);
        dataOutput.writeLong(j2);
    }

    static long readEpochSec(DataInput dataInput) throws IOException {
        if ((dataInput.readByte() & 255) == 255) {
            return dataInput.readLong();
        }
        int i2 = dataInput.readByte() & 255;
        return ((((r0 << 16) + (i2 << 8)) + (dataInput.readByte() & 255)) * 900) - 4575744000L;
    }
}
