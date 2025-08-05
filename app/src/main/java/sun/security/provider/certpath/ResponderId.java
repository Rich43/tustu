package sun.security.provider.certpath;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Arrays;
import javax.security.auth.x500.X500Principal;
import sun.security.util.DerValue;
import sun.security.x509.KeyIdentifier;

/* loaded from: rt.jar:sun/security/provider/certpath/ResponderId.class */
public final class ResponderId {
    private Type type;
    private X500Principal responderName;
    private KeyIdentifier responderKeyId;
    private byte[] encodedRid;

    /* loaded from: rt.jar:sun/security/provider/certpath/ResponderId$Type.class */
    public enum Type {
        BY_NAME(1, "byName"),
        BY_KEY(2, "byKey");

        private final int tagNumber;
        private final String ridTypeName;

        Type(int i2, String str) {
            this.tagNumber = i2;
            this.ridTypeName = str;
        }

        public int value() {
            return this.tagNumber;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.ridTypeName;
        }
    }

    public ResponderId(X500Principal x500Principal) throws IOException {
        this.responderName = x500Principal;
        this.responderKeyId = null;
        this.encodedRid = principalToBytes();
        this.type = Type.BY_NAME;
    }

    public ResponderId(PublicKey publicKey) throws IOException {
        this.responderKeyId = new KeyIdentifier(publicKey);
        this.responderName = null;
        this.encodedRid = keyIdToBytes();
        this.type = Type.BY_KEY;
    }

    public ResponderId(byte[] bArr) throws IOException {
        DerValue derValue = new DerValue(bArr);
        if (derValue.isContextSpecific((byte) Type.BY_NAME.value()) && derValue.isConstructed()) {
            this.responderName = new X500Principal(derValue.getDataBytes());
            this.encodedRid = principalToBytes();
            this.type = Type.BY_NAME;
        } else {
            if (derValue.isContextSpecific((byte) Type.BY_KEY.value()) && derValue.isConstructed()) {
                this.responderKeyId = new KeyIdentifier(new DerValue(derValue.getDataBytes()));
                this.encodedRid = keyIdToBytes();
                this.type = Type.BY_KEY;
                return;
            }
            throw new IOException("Invalid ResponderId content");
        }
    }

    public byte[] getEncoded() {
        return (byte[]) this.encodedRid.clone();
    }

    public Type getType() {
        return this.type;
    }

    public int length() {
        return this.encodedRid.length;
    }

    public X500Principal getResponderName() {
        return this.responderName;
    }

    public KeyIdentifier getKeyIdentifier() {
        return this.responderKeyId;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof ResponderId) {
            return Arrays.equals(this.encodedRid, ((ResponderId) obj).getEncoded());
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.encodedRid);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (this.type) {
            case BY_NAME:
                sb.append((Object) this.type).append(": ").append((Object) this.responderName);
                break;
            case BY_KEY:
                sb.append((Object) this.type).append(": ");
                for (byte b2 : this.responderKeyId.getIdentifier()) {
                    sb.append(String.format("%02X", Byte.valueOf(b2)));
                }
                break;
            default:
                sb.append("Unknown ResponderId Type: ").append((Object) this.type);
                break;
        }
        return sb.toString();
    }

    private byte[] principalToBytes() throws IOException {
        return new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) Type.BY_NAME.value()), this.responderName.getEncoded()).toByteArray();
    }

    private byte[] keyIdToBytes() throws IOException {
        return new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) Type.BY_KEY.value()), new DerValue((byte) 4, this.responderKeyId.getIdentifier()).toByteArray()).toByteArray();
    }
}
