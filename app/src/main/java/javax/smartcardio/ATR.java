package javax.smartcardio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

/* loaded from: rt.jar:javax/smartcardio/ATR.class */
public final class ATR implements Serializable {
    private static final long serialVersionUID = 6695383790847736493L;
    private byte[] atr;
    private transient int startHistorical;
    private transient int nHistorical;

    public ATR(byte[] bArr) {
        this.atr = (byte[]) bArr.clone();
        parse();
    }

    private void parse() {
        if (this.atr.length < 2) {
            return;
        }
        if (this.atr[0] != 59 && this.atr[0] != 63) {
            return;
        }
        int i2 = (this.atr[1] & 240) >> 4;
        int i3 = this.atr[1] & 15;
        int i4 = 2;
        while (i2 != 0 && i4 < this.atr.length) {
            if ((i2 & 1) != 0) {
                i4++;
            }
            if ((i2 & 2) != 0) {
                i4++;
            }
            if ((i2 & 4) != 0) {
                i4++;
            }
            if ((i2 & 8) != 0) {
                if (i4 >= this.atr.length) {
                    return;
                }
                int i5 = i4;
                i4++;
                i2 = (this.atr[i5] & 240) >> 4;
            } else {
                i2 = 0;
            }
        }
        int i6 = i4 + i3;
        if (i6 == this.atr.length || i6 == this.atr.length - 1) {
            this.startHistorical = i4;
            this.nHistorical = i3;
        }
    }

    public byte[] getBytes() {
        return (byte[]) this.atr.clone();
    }

    public byte[] getHistoricalBytes() {
        byte[] bArr = new byte[this.nHistorical];
        System.arraycopy(this.atr, this.startHistorical, bArr, 0, this.nHistorical);
        return bArr;
    }

    public String toString() {
        return "ATR: " + this.atr.length + " bytes";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ATR)) {
            return false;
        }
        return Arrays.equals(this.atr, ((ATR) obj).atr);
    }

    public int hashCode() {
        return Arrays.hashCode(this.atr);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.atr = (byte[]) objectInputStream.readUnshared();
        parse();
    }
}
