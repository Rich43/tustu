package javax.bluetooth;

import com.intel.bluetooth.BluetoothConsts;
import com.intel.bluetooth.Utils;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/UUID.class */
public class UUID {
    private byte[] uuidValue;

    public UUID(long uuidValue) {
        this(Utils.toHexString(uuidValue), true);
        if (uuidValue < 0 || uuidValue > 4294967295L) {
            throw new IllegalArgumentException("uuidValue is not in the range [0, 2^32 -1]");
        }
    }

    public UUID(String uuidValue, boolean shortUUID) {
        if (uuidValue == null) {
            throw new NullPointerException("uuidValue is null");
        }
        int length = uuidValue.length();
        if (shortUUID) {
            if (length < 1 || length > 8) {
                throw new IllegalArgumentException();
            }
            this.uuidValue = Utils.UUIDToByteArray(new StringBuffer().append("00000000".substring(length)).append(uuidValue).append(BluetoothConsts.SHORT_UUID_BASE).toString());
            return;
        }
        if (length < 1 || length > 32) {
            throw new IllegalArgumentException();
        }
        this.uuidValue = Utils.UUIDToByteArray(new StringBuffer().append("00000000000000000000000000000000".substring(length)).append(uuidValue).toString());
    }

    public String toString() {
        return Utils.UUIDByteArrayToString(this.uuidValue);
    }

    public boolean equals(Object value) {
        if (value == null || !(value instanceof UUID)) {
            return false;
        }
        for (int i2 = 0; i2 < 16; i2++) {
            if (this.uuidValue[i2] != ((UUID) value).uuidValue[i2]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return ((this.uuidValue[12] << 24) & (-16777216)) | ((this.uuidValue[13] << 16) & 16711680) | ((this.uuidValue[14] << 8) & NormalizerImpl.CC_MASK) | (this.uuidValue[15] & 255);
    }
}
