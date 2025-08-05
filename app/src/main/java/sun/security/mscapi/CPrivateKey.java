package sun.security.mscapi;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import sun.security.mscapi.CKey;

/* loaded from: sunmscapi.jar:sun/security/mscapi/CPrivateKey.class */
class CPrivateKey extends CKey implements PrivateKey {
    private static final long serialVersionUID = 8113152807912338063L;

    private CPrivateKey(String str, CKey.NativeHandles nativeHandles, int i2) {
        super(str, nativeHandles, i2);
    }

    static CPrivateKey of(String str, long j2, long j3, int i2) {
        return of(str, new CKey.NativeHandles(j2, j3), i2);
    }

    public static CPrivateKey of(String str, CKey.NativeHandles nativeHandles, int i2) {
        return new CPrivateKey(str, nativeHandles, i2);
    }

    @Override // java.security.Key
    public String getFormat() {
        return null;
    }

    @Override // java.security.Key
    public byte[] getEncoded() {
        return null;
    }

    public String toString() {
        if (this.handles.hCryptKey != 0) {
            return this.algorithm + "PrivateKey [size=" + this.keyLength + " bits, type=" + getKeyType(this.handles.hCryptKey) + ", container=" + getContainerName(this.handles.hCryptProv) + "]";
        }
        return this.algorithm + "PrivateKey [size=" + this.keyLength + " bits, type=CNG]";
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        throw new NotSerializableException();
    }
}
