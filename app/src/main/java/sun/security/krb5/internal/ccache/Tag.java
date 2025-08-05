package sun.security.krb5.internal.ccache;

import java.io.ByteArrayOutputStream;

/* loaded from: rt.jar:sun/security/krb5/internal/ccache/Tag.class */
public class Tag {
    int tag;
    Integer time_offset;
    Integer usec_offset;
    int tagLen = 8;
    int length = 4 + this.tagLen;

    public Tag(int i2, int i3, Integer num, Integer num2) {
        this.tag = i3;
        this.time_offset = num;
        this.usec_offset = num2;
    }

    public Tag(int i2) {
        this.tag = i2;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(this.length);
        byteArrayOutputStream.write(this.tag);
        byteArrayOutputStream.write(this.tagLen);
        if (this.time_offset != null) {
            byteArrayOutputStream.write(this.time_offset.intValue());
        }
        if (this.usec_offset != null) {
            byteArrayOutputStream.write(this.usec_offset.intValue());
        }
        return byteArrayOutputStream.toByteArray();
    }
}
